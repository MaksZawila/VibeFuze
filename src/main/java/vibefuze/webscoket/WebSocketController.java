package vibefuze.webscoket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.DependsOn;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import vibefuze.dto.RoomDTO;
import vibefuze.dto.RoomInfoDTO;
import vibefuze.room.Room;
import vibefuze.room.RoomService;
import vibefuze.utils.Mapper;

import javax.annotation.Resource;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@DependsOn({"roomInitializer"})
public class WebSocketController implements ApplicationListener<SessionDisconnectEvent> {
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Resource
    private Map<String, RoomService> roomServices;

    @MessageMapping("/room/{roomID}/join")
    @SendToUser("/private/room/{roomID}/room-details")
    private RoomDTO playerJoin(@DestinationVariable String roomID, String nickname, SimpMessageHeaderAccessor smha) {
        Room room = roomServices.get(roomID).getRoom();
        room.addPlayer(smha.getSessionId(), nickname);
        simpMessagingTemplate.convertAndSend("/all/room/"+roomID+"/update-players", room.getPlayers());
        return Mapper.toRoomDTO(room);
    }

    @MessageMapping("/room/{roomID}/check-phrase")
    @SendToUser("/private/room/{roomID}/wrong-phrase")
    private boolean checkPhrase(@DestinationVariable String roomID, String answer, SimpMessageHeaderAccessor smha) {
        Room room = roomServices.get(roomID).getRoom();
        boolean isCorrect = room.checkPhrase(smha.getSessionId(), answer);
        if(isCorrect)
            simpMessagingTemplate.convertAndSend("/all/room/"+roomID+"/update-players", room.getPlayers());
        return isCorrect;
    }

    @Override
    public void onApplicationEvent(SessionDisconnectEvent event) {
        roomServices.forEach((roomType, roomService) -> {
            if(roomService.getRoom().getPlayers().removeIf(player-> player.getSessionID().equals(event.getSessionId()))) {
                simpMessagingTemplate.convertAndSend("/all/room/" + roomType + "/update-players", roomService.getRoom().getPlayers());
            }
        });
    }

    @GetMapping("/get-all-rooms-info")
    public List<RoomInfoDTO> getAllRoomsInfo() {
        List<RoomInfoDTO> allRoomsInfoDTO = new LinkedList<>();
        roomServices.forEach((roomType, room) -> allRoomsInfoDTO.add(room.getRoomInfo()));
        return allRoomsInfoDTO;
    }
}