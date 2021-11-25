package ca.devday.nothanks.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class Lobby {
    private String joinCode;
    private int capacity;
    private List<Player> players = new ArrayList<>();
    private Boolean open;

    private Lobby() {}

    public static Lobby newInstance(int capacity) {
        Lobby lobby = new Lobby();
        lobby.capacity = capacity;
        return lobby;
    }

    public List<Player> addPlayer(Player player) {
        players.add(player);
        return players;
    }

}