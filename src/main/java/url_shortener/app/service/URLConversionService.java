package url_shortener.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import url_shortener.app.common.IDConverter;
import url_shortener.app.repository.URLRepository;

// @Service to indicate that it's holding the business logic
@Service
public class URLConversionService {
    private static final Logger LOGGER = LoggerFactory.getLogger(URLConversionService.class);
    private final URLRepository urlRepository;

    // @Autowired automatically searches for a respository
    @Autowired
    public URLConversionService(URLRepository urlRepository) {
        this.urlRepository = urlRepository;
    }

    /**
     * Converts the original URL passes from the user and creates a shortened URL
     *
     * @param  localURL              a url derived from the local host
     * @param  originalURL           the original url passed from the user
     * @return                       a unique shortened url
     */
    public String shortenURL(String localURL, String originalURL) {
        LOGGER.info("Shortening the url, {}", originalURL);
        String urlKey = "url:";
        String addressKey = "address:";

        boolean urlAlreadyExist = urlRepository.verifyExistingURL(originalURL);

        if (!urlAlreadyExist) {
          // Increment redis ID
          Long redisID = urlRepository.incrementID();

          urlRepository.saveURL(urlKey + redisID, originalURL);

          // Use the returned ID to create a unique base62 ID
          String uniqueID = IDConverter.INSTANCE.createUniqueID(redisID);
          // Generate an appropriately formatted local url
          String formattedLocalURL = formatLocalURLFromShortener(localURL);

          // Concat the newly formatted local url with a unique ID
          String shortenedURL = formattedLocalURL + uniqueID;

          return shortenedURL;

        }
          Long redisURLID = urlRepository.getRedisURLID(originalURL);
          String uniqueID = IDConverter.INSTANCE.createUniqueID(redisURLID);
          // Generate an appropriately formatted local url
          String formattedLocalURL = formatLocalURLFromShortener(localURL);

          // Concat the newly formatted local url with a unique ID
          String shortenedURL = formattedLocalURL + uniqueID;

          return shortenedURL;
    }

    /**
     * Retrieves the original URL from Redis
     *
     * @param  id              a unique base62 id
     * @return                 the original url passed from the user
     */
    public String getOriginalURLFromID(String id) throws Exception {
        Long redisID = IDConverter.INSTANCE.generateRedisKeyFromUniqueID(id);

        // Retrieve the ID from Redis
        String originalURL = urlRepository.getURL(redisID);

        LOGGER.info("Converting shortened URL back to {}", originalURL);

        return originalURL;
    }

    /**
     * Formats the local url
     *
     * @param  localURL              a URL derived from the local host
     * @return                       a formatted url to be used for concating the shortened url
     */
    private String formatLocalURLFromShortener(String localURL) {
        String[] addressComponents = localURL.split("/");

        StringBuilder formattedURL = new StringBuilder();

        for (int i = 0; i < addressComponents.length - 1; i++) {
            formattedURL.append(addressComponents[i]);
        }

        formattedURL.append('/');

        return formattedURL.toString();
    }
}
