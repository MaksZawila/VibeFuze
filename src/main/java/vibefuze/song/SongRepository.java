package vibefuze.song;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.*;
import java.util.concurrent.BlockingQueue;

public interface SongRepository extends MongoRepository<Song, String> {
    @Aggregation(pipeline = {
            "{'$match': {'genre': '?0'}}",
            "{'$sample': {'size': 1}}"
    })
    Song findOneByGenre(String genre);

    @Aggregation(pipeline = {
            "{'$match': {'genre': '?0'}}",
            "{'$sample': {'size': ?1}}"
    })
    List<Song> findSongsByGenre(String genre, Integer size);
}
