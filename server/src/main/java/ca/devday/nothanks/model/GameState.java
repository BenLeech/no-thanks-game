package ca.devday.nothanks.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class GameState {
    private String id;
    private List<Card> cards = new ArrayList<>();
    private Card activeCard;
    private int currentPlayerTurn;
    private List<Player> players = new ArrayList<>();

    private GameState() {}

    public static GameState newInstance(String gameId) {
        GameState gameState = new GameState();
        gameState.id = gameId;
        return gameState;
    }
}
