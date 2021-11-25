package ca.devday.nothanks.service;

import ca.devday.nothanks.api.CreateLobbyDto;
import ca.devday.nothanks.api.GameMessageDto;
import ca.devday.nothanks.api.JoinLobbyDto;
import ca.devday.nothanks.api.type.NotificationType;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import ca.devday.nothanks.model.Lobby;
import ca.devday.nothanks.model.Player;
import ca.devday.nothanks.repository.LobbyRepository;

import java.util.UUID;

@Component
public class LobbyService {

    private static final int MAX_CAPACITY = 7;
    private static final int MIN_CAPACITY = 3;
    private static final int JOIN_CODE_LENGTH = 4;

    @Autowired
    private LobbyRepository lobbyRepository;

    @Autowired
    private GameService gameService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public GameMessageDto createLobby(final String userId, final CreateLobbyDto dto) {
        String validation = validateCreateLobby(dto);
        if(validation != null) {
            return new GameMessageDto()
                .setNotificationType(NotificationType.ERROR)
                .setPayload(validation);
        }
        Lobby lobby = Lobby.newInstance(dto.getCapacity());
        lobby.setJoinCode(generateUniqueJoinCode());
        lobbyRepository.save(lobby);

        messagingTemplate.convertAndSendToUser(userId, "/queue/lobby", new GameMessageDto()
            .setNotificationType(NotificationType.LOBBY_CREATED)
            .setPayload(lobby.getJoinCode()));

        return joinLobby(userId, new JoinLobbyDto()
            .setJoinCode(lobby.getJoinCode())
            .setPlayerName(dto.getPlayerName()));
    }

    public GameMessageDto joinLobby(final String userId, final JoinLobbyDto dto) {
        Lobby lobby = lobbyRepository.get(dto.getJoinCode());
        String validation = validateJoinLobby(lobby, dto);
        if(validation != null) {
            return new GameMessageDto()
                .setNotificationType(NotificationType.ERROR)
                .setPayload(validation);
        }
        
        lobby.addPlayer(Player.newInstance(userId, dto.getPlayerName()));
        lobbyRepository.save(lobby);

        GameMessageDto messageDto = new GameMessageDto()
            .setNotificationType(NotificationType.PLAYER_JOINED)
            .setPayload(dto.getPlayerName());
        lobby.getPlayers().forEach(player -> messagingTemplate.convertAndSendToUser(player.getUserId(), "/queue/lobby", messageDto));
        return new GameMessageDto()
            .setNotificationType(NotificationType.JOINED_LOBBY)
            .setPayload(lobby.getJoinCode());
    }

    public void handlePlayerLeaving(Lobby lobby, Player player) {
        lobby.getPlayers().remove(player);
        lobbyRepository.save(lobby);

        messagingTemplate.convertAndSend("/topic/lobby", new GameMessageDto()
            .setNotificationType(NotificationType.PLAYER_LEFT)
            .setPayload(player.getName()));    
    }

    public void startGame(String gameId, String playerId) {
        Lobby lobby = lobbyRepository.get(gameId);
        String validation = validateStartGame(lobby, playerId);
        if(validation != null) {
            messagingTemplate.convertAndSendToUser(playerId, "/queue/lobby", new GameMessageDto()
                    .setNotificationType(NotificationType.ERROR)
                    .setPayload(validation));
        }

        lobby.setOpen(Boolean.FALSE);
        lobbyRepository.save(lobby);
        gameService.startGame(gameId);
    }

    private String validateStartGame(Lobby lobby, String playerId) {
        if(lobby == null) {
            return "Lobby not found";
        }
        if(BooleanUtils.isFalse(lobby.getOpen())) {
            return "The lobby is not open";
        }
        if(!playerId.equals(lobby.getHostPlayerId())) {
            return "Only the host can start the game";
        }
        if(lobby.getPlayers().size() < MIN_CAPACITY) {
            return "Not enough players to start the game";
        }
        return null;
    }

    private String validateCreateLobby(CreateLobbyDto createLobbyDto) {
        if(createLobbyDto.getCapacity() > MAX_CAPACITY) {
            return String.format("Capacity cannot be greater than %s", MAX_CAPACITY);
        }
        if(createLobbyDto.getCapacity() < MIN_CAPACITY) {
            return String.format("Capacity cannot be less than %s", MIN_CAPACITY);
        }
        return null;
    }

    private String validateJoinLobby(Lobby lobby, JoinLobbyDto dto) {
        if(lobby == null) {
            return "Lobby does not exist";
        }
        if(lobby.getPlayers().size() >= lobby.getCapacity()) {
            return "Lobby is full";
        }
        if(StringUtils.isBlank(dto.getPlayerName())) {
            return "Player name cannot be blank";
        }
        if(lobby.getPlayers().stream().anyMatch(player -> player.getName().equals(dto.getPlayerName()))) {
            return "A player with that name is already in the lobby";
        }
        return null;
    }

    private String generateUniqueJoinCode() {
        String joinCode = generateJoinCode();
        while(lobbyRepository.get(joinCode) != null) {
            joinCode = generateJoinCode();
        }
        return joinCode;
    }

    private String generateJoinCode() {
        return StringUtils.upperCase(RandomStringUtils.randomAlphabetic(JOIN_CODE_LENGTH));
    }

}
