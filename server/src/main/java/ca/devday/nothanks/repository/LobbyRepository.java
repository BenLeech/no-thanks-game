package ca.devday.nothanks.repository;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import ca.devday.nothanks.model.Lobby;

@Component
public class LobbyRepository {

    private Map<String, Lobby> lobbies = new HashMap<>();

    public Lobby get(String joinCode) {
        return this.lobbies.get(joinCode);
    }

    public void save(Lobby lobby) {
        this.lobbies.put(lobby.getJoinCode(), lobby);
    }

    public void remove(String joinCode) {
        this.lobbies.remove(joinCode);
    }

}