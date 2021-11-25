package ca.devday.nothanks.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import lombok.Data;

@Data
public class GameState {
    private UUID id;
    private List<Card> cards = new ArrayList<>();
    private Card activeCard;
    private int currentPlayerTurn;
    private List<Player> players = new ArrayList<>();

    private GameState() {}

    public static GameState newInstance() {
        GameState gameState = new GameState();
        gameState.id = UUID.randomUUID();
        return gameState;
    }
}