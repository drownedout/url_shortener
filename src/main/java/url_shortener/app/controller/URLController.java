package url_shortener.app.controller;

import java.io.IOException;
import java.net.URISyntaxException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import url_shortener.app.common.URLValidator;
import url_shortener.app.service.URLConversionService;

@RestController
public class URLController {
    private static final Logger LOGGER = LoggerFactory.getLogger(URLController.class);
    private final URLConversionService urlConversionService;

    public URLController(URLConversionService urlConversionService) {
        this.urlConversionService = urlConversionService;
    }

    @RequestMapping(value = "/shorten", method = RequestMethod.POST, consumes = {"application/json"})
    public String shortenURL(@RequestBody @Valid final RequestShortener requestShortener, HttpServletRequest request) throws Exception {
        LOGGER.info("Original URL: " + requestShortener.getUrl());

        // Retrieve the URL passed by the user
        String originalURL = requestShortener.getUrl();

        // If the URL is valid
        if (URLValidator.INSTANCE.isURLValid(originalURL)) {
            // localURL would be 'localhost' when running locally
            String localURL = request.getRequestURL().toString();
            // Concat generated local URL with the newly generate 'short' URL
            String shortenedUrl = urlConversionService.shortenURL(localURL, requestShortener.getUrl());

            LOGGER.info("Shortened the URL to: " + shortenedUrl);

            return shortenedUrl;
        }
        throw new Exception("The URL provided, " + originalURL + ", is invalid, please try again");
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public RedirectView redirectUrl(@PathVariable String id, HttpServletRequest request, HttpServletResponse response) throws IOException, URISyntaxException, Exception {
        RedirectView redirectView = new RedirectView();

        // Retrieves the original URL from Redis
        String originalURL = urlConversionService.getOriginalURLFromID(id);
        LOGGER.info("Original URL: " + originalURL);

        redirectView.setUrl("http://" + originalURL);

        return redirectView;
    }
}

class RequestShortener {
    private String url;

    @JsonCreator
    public RequestShortener(@JsonProperty("url") String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl() {
        this.url = url;
    }
}
