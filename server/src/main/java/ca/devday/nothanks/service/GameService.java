package ca.devday.nothanks.service;

import ca.devday.nothanks.api.GameMessageDto;
import ca.devday.nothanks.api.PlayerActionDto;
import ca.devday.nothanks.api.type.ActionType;
import ca.devday.nothanks.api.type.NotificationType;
import ca.devday.nothanks.model.Card;
import ca.devday.nothanks.model.GameState;
import ca.devday.nothanks.model.Player;
import ca.devday.nothanks.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Component
public class GameService {

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public void performAction(PlayerActionDto playerActionDto, String playerId) {
        GameState gameState = gameRepository.get(playerActionDto.getGameId());
        if(gameState == null) {
            sendErrorMessage(playerId, "Game does not exist");
            return;
        }

        Player player = findPlayer(gameState, playerId);
        String validation = validateAction(gameState, player);
        if(validation != null) {
            sendErrorMessage(playerId, validation);
            return;
        }

        if(ActionType.NO_THANKS == playerActionDto.getActionType()) {
            performNoThanks(gameState, player);
        } else if (ActionType.TAKE_CARD == playerActionDto.getActionType()) {
            takeCard(gameState, player);
        }
    }

    private Player findPlayer(GameState gameState, String playerId) {
        return gameState.getPlayers().stream()
            .filter(player -> player.getUserId().equals(playerId))
            .findFirst()
            .orElse(null);
    }

    private String validateAction(GameState gameState, Player player) {
         if(player == null) {
            return "You are not in this game";
         } else if(!player.equals(getCurrentTurnPlayer(gameState))) {
            return "It is not your turn";
         }

         return null;
    }

    public void startGame(String gameId) {
        GameState gameState = GameState.newInstance(gameId);
        gameState.setCards(createCardDeck());
        gameRepository.save(gameState);
        sendGameMessage(gameState.getId(), new GameMessageDto()
            .setNotificationType(NotificationType.GAME_STARTED)
            .setPayload(null));
        drawCard(gameState);
    }

    private void sendGameMessage(String gameId, GameMessageDto messageDto) {
        messagingTemplate.convertAndSend(String.format("/topic/game/%s", gameId), messageDto);
    }

    private void sendErrorMessage(String playerId, String error) {
        messagingTemplate.convertAndSendToUser(playerId, "/queue/game/errors",
                new GameMessageDto()
                    .setNotificationType(NotificationType.ERROR)
                    .setPayload(error));
    }

    private List<Card> createCardDeck() {
        List<Card> cards = new ArrayList<>();
        for(int i = 3; i < 35; i++) {
            cards.add(Card.newInstance(i));
        }
        Collections.shuffle(cards);
        cards.subList(0, 9).clear();
        return cards;
    }

    public void performNoThanks(GameState gameState, Player player) {
        if(player.getChips() == 0) {
            sendErrorMessage(player.getUserId(), "No chips remaining");
        }
        gameState.getActiveCard().setChips(gameState.getActiveCard().getChips() + 1);
        player.setChips(player.getChips() - 1);
        sendGameMessage(gameState.getId(), new GameMessageDto()
            .setNotificationType(NotificationType.NO_THANKS)
            .setPayload(player.getName()));

        startNextTurn(gameState);
    }

    private int calculatePoints(Player player) {
        List<Card> cards = player.getCards();
        cards.sort(Comparator.comparing(Card::getNumber).reversed());

        int points = 0;
        Card previousCard = null;
        for(int i = 0; i < cards.size(); i++) {
            Card card = cards.get(i);
            if(i == cards.size()-1 || 
                (previousCard != null && card.getNumber() - previousCard.getNumber() == 1)) {
                points += cards.get(i).getNumber();  
            }
            previousCard = card;
        }

        points -= player.getChips();
        return points;
    }

    private void takeCard(GameState gameState, Player player) {
        player.getCards().add(gameState.getActiveCard());
        player.setChips(player.getChips() + gameState.getActiveCard().getChips());
        
        sendGameMessage(gameState.getId(), new GameMessageDto()
            .setNotificationType(NotificationType.TAKE_CARD)
            .setPayload(player.getName()));

        drawCard(gameState);
    }

    private Player getCurrentTurnPlayer(GameState gameState) {
        return gameState.getPlayers().get(gameState.getCurrentPlayerTurn());
    }

    private void drawCard(GameState gameState) {

        if(gameState.getCards().size() == 0) {
            endGame(gameState);
        }
        gameState.setActiveCard(gameState.getCards().get(0));
        gameState.getCards().remove(0);

        sendGameMessage(gameState.getId(), new GameMessageDto()
            .setNotificationType(NotificationType.DRAW_CARD)
            .setPayload(String.valueOf(gameState.getActiveCard().getNumber())));
    }

    private void endGame(GameState gameState) {
        setPlayerPoints(gameState);

        sendGameMessage(gameState.getId(), new GameMessageDto()
            .setNotificationType(NotificationType.GAME_OVER)
            .setPayload(findWinningPlayerName(gameState)));

        gameRepository.remove(gameState.getId());
    }

    private void setPlayerPoints(GameState gameState) {
        gameState.getPlayers()
            .forEach(player -> player.setPoints(calculatePoints(player)));
        gameRepository.save(gameState);
    }

    private String findWinningPlayerName(GameState gameState) {
        return gameState.getPlayers().stream()
            .reduce((a, b) -> a.getPoints() > b.getPoints() ? a : b)
            .map(Player::getName)
            .orElse("Winner not determined");
    }

    private void startNextTurn(GameState gameState) {
        if(gameState.getCurrentPlayerTurn() >= gameState.getPlayers().size()) {
            gameState.setCurrentPlayerTurn(0);
        } else {
            gameState.setCurrentPlayerTurn(gameState.getCurrentPlayerTurn() + 1);
        }

        Player newTurnPlayer = gameState.getPlayers().get(gameState.getCurrentPlayerTurn());
        sendGameMessage(gameState.getId(), new GameMessageDto()
            .setNotificationType(NotificationType.PLAYER_TURN)
            .setPayload(newTurnPlayer.getName()));
    }
}
