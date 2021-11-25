package ca.devday.nothanks.listener;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import ca.devday.nothanks.model.GameState;
import ca.devday.nothanks.repository.GameRepository;
import ca.devday.nothanks.repository.LobbyRepository;
import ca.devday.nothanks.service.GameService;
import ca.devday.nothanks.service.LobbyService;

@Component
public class EventListener implements ApplicationListener<SessionDisconnectEvent> {

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private LobbyRepository lobbyRepository;

    @Autowired
    private GameService gameService;

    @Autowired
    private LobbyService lobbyService;

    @Override
    public void onApplicationEvent(SessionDisconnectEvent event) {
        // TODO handle user disconnect
        // lobbyService.handlePlayerLeaving(lobby, player);

        Optional<GameState> game = gameRepository.findGameByPlayerId(event.getUser().getName());
        // gameService.performAction(playerActionDto, playerId);
        // gameRepository.remove(game);
    }

}