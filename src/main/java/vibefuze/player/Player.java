package vibefuze.player;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.util.UUID;

@Data
@Builder
public class Player {
    private String sessionID;
    private String nickname;
    private int points;
    private boolean titleGuessed;
    private boolean artistGuessed;

    public void addPoints(Integer points) {
        this.points += points;
    }

    public void resetPoints() {
        points = 0;
    }

    public void resetGuesses() {
        titleGuessed = false;
        artistGuessed = false;
    }

    public boolean guessedAll() {
        return titleGuessed && artistGuessed;
    }
}
