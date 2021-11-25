package ca.devday.nothanks.api.type;

public enum ActionType {
    NO_THANKS("NO_THANKS"),
    TAKE_CARD("TAKE_CARD");

    private final String value;

    ActionType(final String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
