package ca.devday.nothanks.model;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Card {
    private int number;
    private int chips;

    private Card() {}

    public static Card newInstance(int number) {
        Card card = new Card();
        card.number = number;
        card.chips = 0;
        return card;
    }
}
