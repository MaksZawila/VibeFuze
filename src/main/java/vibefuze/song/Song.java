package vibefuze.song;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Set;

@Builder
@Data
@Document("songs")
public class Song {
    @Id
    private String id;
    private String title;
    private Set<String> artists;
    private String genre;
    private String imageUrl;
    private String previewUrl;
    private String url;
    private String releaseDate;
}
