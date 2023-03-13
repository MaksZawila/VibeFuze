package vibefuze.room;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum RoomType {
    RAP("rap"),
    ROCK("rock"),
    POP("pop");
    private final String value;
}
