package ca.devday.nothanks.controller;

import java.security.Principal;

import ca.devday.nothanks.api.CreateLobbyDto;
import ca.devday.nothanks.api.GameMessageDto;
import ca.devday.nothanks.api.JoinLobbyDto;
import ca.devday.nothanks.service.LobbyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

@Controller
public class LobbyController {

    @Autowired
    private LobbyService lobbyService;
    
    @MessageMapping("/create")
    @SendToUser("/queue/lobby")
    public GameMessageDto createLobby(final Principal principal, final CreateLobbyDto dto) {
        return lobbyService.createLobby(principal.getName(), dto);
    }

    @MessageMapping("/join")
    @SendToUser("/queue/lobby")
    public GameMessageDto joinLobby(final Principal principal, final JoinLobbyDto dto) {
        return lobbyService.joinLobby(principal.getName(), dto);
    }

    @MessageMapping("/start")
    public void startGame(String joinCode, Principal principal) {
        lobbyService.startGame(joinCode, principal.getName());
    }
    
}
