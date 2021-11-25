package ca.devday.nothanks.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Player {
    private String userId;
    private String name;
    private int points;
    private int chips;
    private List<Card> cards = new ArrayList<>();

    private Player() {}

    public static Player newInstance(String userId, String name) {
        Player player = new Player();
        player.userId = userId;
        player.name = name;
        return player;
    }
}
