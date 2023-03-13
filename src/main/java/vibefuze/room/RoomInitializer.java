package vibefuze.room;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import vibefuze.song.SongRepository;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;

@Getter
@Configuration
public class RoomInitializer {
    private final SongRepository songRepository;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    public RoomInitializer(SongRepository songRepository, SimpMessagingTemplate simpMessagingTemplate) {
        this.songRepository = songRepository;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @Bean
    public Map<String, RoomService> createRooms() {
        Map<String, RoomService> roomServices = new HashMap<>();
        for (RoomType roomType : RoomType.values()) {
            Room room = Room.builder()
                    .type(roomType)
                    .state(RoomState.START)
                    .roundLength(30)
                    .maxRounds(10)
                    .players(new LinkedHashSet<>())
                    .build();

            RoomService roomService = new RoomService();
            roomService.setRoom(room);
            roomService.setSongRepository(songRepository);
            roomService.setSimpMessagingTemplate(simpMessagingTemplate);

            roomServices.put(roomType.getValue(), roomService);

            new Thread(roomService, roomType.toString()).start();
        }
        return roomServices;
    }

}
