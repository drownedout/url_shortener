package url_shortener.app.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import redis.clients.jedis.Jedis;

// @Repository’s job is to catch persistence specific exceptions and
// rethrow them as one of Spring’s unified unchecked exception.
@Repository
public class URLRepository {
    private final Jedis jedis;
    private final String idKey;
    private final String urlKey;
    private static final Logger LOGGER = LoggerFactory.getLogger(URLRepository.class);

    public URLRepository() {
        this.jedis = new Jedis();
        this.idKey = "id";
        this.urlKey = "url:"; // Redis best practices - name followed by a ":"
    }

    public Long incrementID() {
        Long id = jedis.incr(idKey);
        LOGGER.info("Incrementing ID: {}", id - 1);
        return id - 1;
    }

    public void saveURL(String key, String longUrl) {
        LOGGER.info("Saving: {} at {}", longUrl, key);

        // hset takes three parameters - key name, key value, and value
        jedis.hset(urlKey, key, longUrl);
    }

    public String getURL(Long id) throws Exception {
        LOGGER.info("Retrieving at {}", id);
        String url = jedis.hget(urlKey, urlKey + id);
        LOGGER.info("Retrieved {} at {}", url, id);

        if (url == null) {
            throw new Exception("URL at key," + id + ", does not exist");
        }

        return url;
    }
}
