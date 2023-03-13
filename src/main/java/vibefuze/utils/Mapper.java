package vibefuze.utils;

import org.springframework.stereotype.Component;
import vibefuze.dto.RoomDTO;
import vibefuze.room.Room;

@Component
public class Mapper {
    public static RoomDTO toRoomDTO(Room room) {
        return RoomDTO.builder()
                .type(room.getType().getValue())
                .state(room.getState().name())
                .currentRoundTime(room.getElapsedRoundTime())
                .roundLength(room.getRoundLength())
                .currentRound(room.getCurrentRound())
                .maxRounds(room.getMaxRounds())
                .players(room.getPlayers())
                .song(room.getCurrentSong())
                .build();
    }
}
