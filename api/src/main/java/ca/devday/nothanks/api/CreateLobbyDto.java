package ca.devday.nothanks.api;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CreateLobbyDto {
    private int capacity;
    private String playerName;
}
