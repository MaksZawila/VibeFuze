package vibefuze.room;

import lombok.Data;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import vibefuze.dto.RoomInfoDTO;
import vibefuze.player.Player;
import vibefuze.song.Song;
import vibefuze.song.SongRepository;
import vibefuze.utils.Mapper;

import java.util.List;

@Data
@Log
public class RoomService implements Runnable {
    private Room room;
    private SongRepository songRepository;
    private SimpMessagingTemplate simpMessagingTemplate;

    @Override
    @SneakyThrows
    public void run() {
        log.info("Room " + room.getType() + " is live.");
        while (!Thread.interrupted()) {
            startGame();
            while (room.getCurrentRound() < room.getMaxRounds()) {
                playSong();
                loadSong();
            }
            endGame();
        }
    }

    public void startGame() throws InterruptedException {
        room.setState(RoomState.START);
        room.setCurrentRound(0);
        room.setRoundStartTimestamp(System.currentTimeMillis());
        room.getPlayers().forEach(Player::resetPoints);
        List<Song> songs = songRepository.findSongsByGenre(room.getType().getValue(), room.getMaxRounds());
        room.setSongs(songs);
        simpMessagingTemplate.convertAndSend("/all/room/" + room.getType().getValue() + "/start-game", Mapper.toRoomDTO(room));
        Thread.sleep(2000);
    }

    public void playSong() throws InterruptedException {
        room.setState(RoomState.PLAY);
        room.setRoundGuesses(0);
        room.selectNextSong();
        room.roundIncrement();
        room.setRoundStartTimestamp(System.currentTimeMillis());
        this.simpMessagingTemplate.convertAndSend("/all/room/" + room.getType().getValue() + "/start-round", Mapper.toRoomDTO(room));
        Thread.sleep(room.getRoundLength() * 1000L);
    }

    public void loadSong() throws InterruptedException {
        room.setState(RoomState.LOAD);
        room.setCurrentSong(null);
        room.setRoundStartTimestamp(System.currentTimeMillis());
        room.getPlayers().forEach(Player::resetGuesses);
        this.simpMessagingTemplate.convertAndSend("/all/room/" + room.getType().getValue() + "/end-round", Mapper.toRoomDTO(room));
        if (room.getCurrentRound() <= room.getMaxRounds())
            Thread.sleep(2000);
    }

    public void endGame() throws InterruptedException {
        room.setState(RoomState.END);
        this.simpMessagingTemplate.convertAndSend("/all/room/" + room.getType().getValue() + "/end-game", Mapper.toRoomDTO(room));
        Thread.sleep(5000);
    }

    public RoomInfoDTO getRoomInfo() {
        return RoomInfoDTO.builder()
                .playersCount(room.getPlayers().size())
                .name(room.getType().getValue())
                .imageUrl(songRepository.findOneByGenre(room.getType().getValue()).getImageUrl())
                .build();
    }
}
