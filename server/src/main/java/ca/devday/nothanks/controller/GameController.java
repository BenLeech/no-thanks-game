package ca.devday.nothanks.controller;

import java.security.Principal;

import ca.devday.nothanks.api.PlayerActionDto;
import ca.devday.nothanks.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;


@Controller
public class GameController {

    @Autowired
    private GameService gameService;

    @MessageMapping("/game/action")
    public void performAction(PlayerActionDto dto, Principal principal) {
        gameService.performAction(dto, principal.getName());
    }
}
