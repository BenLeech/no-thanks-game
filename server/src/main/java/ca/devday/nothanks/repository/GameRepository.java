package ca.devday.nothanks.repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Component;

import ca.devday.nothanks.model.GameState;

@Component
public class GameRepository {
    
    private Map<String, GameState> gameStates = new HashMap<>();

    public GameState get(String gameId) {
        return this.gameStates.get(gameId);
    }

    public Optional<GameState> findGameByPlayerId(String playerId) {
        return this.gameStates.values().stream()
            .filter(game -> game.getPlayers().stream().anyMatch(player -> player.getUserId().equals(playerId)))
            .findFirst();
    }

    public void save(GameState gameState) {
        this.gameStates.put(gameState.getId(), gameState);
    }

    public void remove(String gameId) {
        this.gameStates.remove(gameId);
    }
}
