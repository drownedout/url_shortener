package url_shortener.app.common;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class URLValidator {
    public static final URLValidator INSTANCE = new URLValidator();

    // Matches valid URL formats
    public static final String URL_REGEX = "^(http:\\/\\/www\\.|https:\\/\\/www\\.|http:\\/\\/" +
                                           "|https:\\/\\/)?[a-z0-9]+([\\-\\.]{1}[a-z0-9]+)*\\." +
                                           "[a-z]{2,5}(:[0-9]{1,5})?(\\/.*)?$";
    private static final Pattern URL_PATTERN = Pattern.compile(URL_REGEX);

    private URLValidator() {}

    // Verifies if the URL is valid
    public boolean isURLValid(String url) {
        Matcher matcher = URL_PATTERN.matcher(url);
        return matcher.matches();
    }
}
