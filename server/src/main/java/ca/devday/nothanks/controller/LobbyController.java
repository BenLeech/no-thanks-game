package ca.devday.nothanks.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import ca.devday.nothanks.model.api.CreateLobbyDto;
import ca.devday.nothanks.model.api.JoinLobbyDto;
import ca.devday.nothanks.model.api.GameMessageDto;
import ca.devday.nothanks.service.LobbyService;

@Controller
public class LobbyController {

    @Autowired
    private LobbyService lobbyService;
    
    @MessageMapping("/create")
    @SendToUser("/topic/lobby")
    public GameMessageDto createLobby(final Principal principal, final CreateLobbyDto dto) {
        return lobbyService.createLobby(principal.getName(), dto);
    }

    @MessageMapping("/join")
    @SendToUser("/topic/lobby")
    public GameMessageDto joinLobby(final Principal principal, final JoinLobbyDto dto) {
        return lobbyService.joinLobby(principal.getName(), dto);
    }

    @MessageMapping("/start")
    public void startGame(Principal principal) {
        
    }
    
}
