package vibefuze.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class RoomInfoDTO {
    private final int playersCount;
    private final String name;
    private final String imageUrl;
}