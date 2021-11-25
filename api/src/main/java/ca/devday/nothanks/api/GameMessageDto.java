package ca.devday.nothanks.model.api;

import ca.devday.nothanks.model.type.NotificationType;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class GameMessageDto {
    private NotificationType notificationType;
    private String payload;
}
