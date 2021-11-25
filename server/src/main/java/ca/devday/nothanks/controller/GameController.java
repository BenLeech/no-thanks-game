package ca.devday.nothanks.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import ca.devday.nothanks.model.api.PlayerActionDto;
import ca.devday.nothanks.service.GameService;

@Controller
public class GameController {

    @Autowired
    private GameService gameService;

    @MessageMapping("/game/action")
    public void performAction(PlayerActionDto dto, Principal principal) {
        gameService.performAction(dto, principal.getName());
    }
}