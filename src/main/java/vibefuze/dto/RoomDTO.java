package vibefuze.dto;

import lombok.Builder;
import lombok.Data;
import vibefuze.player.Player;
import vibefuze.song.Song;

import java.util.Set;

@Data
@Builder
public class RoomDTO {
    private final String type;
    private final String state;
    private final long currentRoundTime;
    private final int roundLength;
    private final int currentRound;
    private final int maxRounds;
    private final Set<Player> players;
    private final Song song;
}
