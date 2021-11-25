package ca.devday.nothanks.api.type;

public enum NotificationType {
    LOBBY_CREATED("LOBBY_CREATED"),
    JOINED_LOBBY("JOINED_LOBBY"),
    PLAYER_JOINED("PLAYER_JOINED"),
    PLAYER_LEFT("PLAYER_LEFT"),
    GAME_STARTED("GAME_STARTED"),
    ERROR("ERROR"),
    DRAW_CARD("DRAW_CARD"),
    NO_THANKS("NO_THANKS"),
    TAKE_CARD("TAKE_CARD"),
    GAME_OVER("GAME_OVER"),
    PLAYER_TURN("PLAYER_TURN");

    private final String value;

    NotificationType(final String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
