package ca.devday.nothanks.listener;

import ca.devday.nothanks.repository.GameRepository;
import ca.devday.nothanks.repository.LobbyRepository;
import ca.devday.nothanks.service.GameService;
import ca.devday.nothanks.service.LobbyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

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
    }

}
