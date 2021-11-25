package ca.devday.nothanks.api;

import ca.devday.nothanks.api.type.NotificationType;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class GameMessageDto {
    private NotificationType notificationType;
    private String payload;
}
