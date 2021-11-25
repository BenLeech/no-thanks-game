package ca.devday.nothanks.model.api;

import java.util.UUID;

import ca.devday.nothanks.model.type.ActionType;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class PlayerActionDto {
    private UUID gameId;
    private ActionType actionType;
}
