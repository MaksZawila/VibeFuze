package vibefuze.room;

import lombok.Builder;
import lombok.Data;
import lombok.extern.java.Log;
import vibefuze.player.Player;
import vibefuze.song.Song;
import vibefuze.utils.TextUtil;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@Builder
@Log
public class Room {
    private final RoomType type;
    private RoomState state;
    private long roundStartTimestamp;
    private final int roundLength;
    private int currentRound;
    private final int maxRounds;
    private Set<Player> players;
    private Song currentSong;

    private List<Song> songs;
    private int roundGuesses;


    public void addPlayer(String sessionID, String nickname) {
        this.players.add(Player.builder()
                        .sessionID(sessionID)
                        .nickname(nickname)
                        .build());
    }

    public boolean playerAlreadyInRoom(String sessionId) {
        return players.stream().anyMatch(player -> player.getSessionID().equals(sessionId));
    }

    public Long getElapsedRoundTime() {
        return System.currentTimeMillis() - roundStartTimestamp;
    }

    public void roundIncrement(){
        currentRound++;
    }
    public boolean checkPhrase(String id, String answer) {
            boolean res = false;
            Player player = players.stream().filter(p -> p.getSessionID().equals(id))
                    .findFirst()
                    .orElse(null);
            if (!state.equals(RoomState.PLAY) || player == null || player.guessedAll())
                return false;

            boolean isMatchingArtist = TextUtil.compare(answer, currentSong.getArtists());
            boolean isMatchingTitle = TextUtil.compare(answer, currentSong.getTitle());

            if (!player.isArtistGuessed() && isMatchingArtist) {
                playerGuessed(player);
                player.setArtistGuessed(true);
                res = true;
            }

            if (!player.isTitleGuessed() && isMatchingTitle) {
                playerGuessed(player);
                player.setTitleGuessed(true);
                res = true;
            }

            if (player.guessedAll()) {
                if (roundGuesses < 3) {
                    player.addPoints(300 - roundGuesses * 100);
                    res = true;
                }
                roundGuesses++;
            }
            return res;
    }

    public void playerGuessed(Player player) {
        player.addPoints(100);
    }



    public Set<Player> getPlayers() {
        players = players.stream()
                .sorted((p1,p2)-> p2.getPoints()-p1.getPoints())
                .collect(Collectors.toCollection(LinkedHashSet::new));
        return players;
    }

    public void selectNextSong() {
        currentSong = songs.get(currentRound);
    }
}