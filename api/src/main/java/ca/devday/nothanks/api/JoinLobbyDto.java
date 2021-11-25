package ca.devday.nothanks.model.api;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class JoinLobbyDto {
    private String joinCode;
    private String playerName;
}
