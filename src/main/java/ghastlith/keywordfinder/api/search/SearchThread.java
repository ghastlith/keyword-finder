package ghastlith.keywordfinder.api.search;

import static java.util.regex.Pattern.CASE_INSENSITIVE;

import java.net.MalformedURLException;
import java.net.URI;
import java.util.regex.Pattern;

import ghastlith.keywordfinder.api.search.model.SearchInformation;
import ghastlith.keywordfinder.http.HttpRequestSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Contains the search functionalities and run them asynchronously for each url.
 */
@RequiredArgsConstructor
@Slf4j
public class SearchThread implements Runnable {

  private final ThreadManager threadManager;
  private final SearchInformation information;
  private final String currentUrl;
  private final HttpRequestSender httpRequestSender;

  private static final Pattern URL_REGEX_PATTERN = Pattern.compile("<a\\s+(?:[^>]*?\\s+)?href=([\"'])((?!\").*?)\\1", CASE_INSENSITIVE);

  /**
   * The runnable method to search for the desired informations on the web page.
   */
  @Override
  public void run() {
    final var lines = httpRequestSender.doGetRequest(this.currentUrl);
    var found = false;

    for (final var line : lines) {
      found = found || searchForKeyword(line);
      searchForNewUrls(line);
    }
  }

  private boolean searchForKeyword(final String line) {
    final var keyword = this.information.getKeyword().toLowerCase();

    if (line.toLowerCase().contains(keyword)) {
      this.threadManager.updateUrlKeywordWasFound(this.currentUrl);
      return true;
    }

    return false;
  }

  private void searchForNewUrls(final String line) {
    final var matcher = URL_REGEX_PATTERN.matcher(line);
    final var baseUrl = this.information.getBaseurl();

    while (matcher.find()) {
      final var foundUrl = matcher.group(2);
      final var formedUrl = buildUrlPath(foundUrl);

      final var startsWithHttp = formedUrl.startsWith("http");
      final var startsWithBaseurl = formedUrl.startsWith(baseUrl);

      if (null != formedUrl && startsWithHttp && startsWithBaseurl) {
        this.threadManager.run(formedUrl.toString());
      }
    }
  }

  private String buildUrlPath(final String foundUrl) {
    final var baseUrl = this.information.getBaseurl();

    try {
      return URI.create(baseUrl).resolve(foundUrl).toURL().toString();
    } catch (MalformedURLException e) {
      log.warn("there was an error building the url {}", foundUrl);
      return null;
    }
  }

}
