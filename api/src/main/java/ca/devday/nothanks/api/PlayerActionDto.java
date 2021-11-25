package ca.devday.nothanks.api;

import ca.devday.nothanks.api.type.ActionType;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.UUID;

@Data
@Accessors(chain = true)
public class PlayerActionDto {
    private String gameId;
    private ActionType actionType;
}
