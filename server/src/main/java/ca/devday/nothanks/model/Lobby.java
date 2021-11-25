package ca.devday.nothanks.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Lobby {
    private String joinCode;
    private int capacity;
    private List<Player> players = new ArrayList<>();
    private String hostPlayerId;
    private Boolean open;

    private Lobby() {}

    public static Lobby newInstance(int capacity) {
        Lobby lobby = new Lobby();
        lobby.capacity = capacity;
        lobby.open = true;
        return lobby;
    }

    public List<Player> addPlayer(Player player) {
        players.add(player);
        return players;
    }

}
