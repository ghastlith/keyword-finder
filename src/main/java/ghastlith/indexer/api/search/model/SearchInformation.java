package ghastlith.indexer.api.search.model;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * The information of the user search.
 */
@Getter
@RequiredArgsConstructor
public class SearchInformation {

  private final String id;
  private final String keyword;
  private final String baseurl;
  private boolean done = false;
  private final Map<String, Boolean> urls = new ConcurrentHashMap<>();

  private static final String SEARCH_LOG_TEMPLATE = "search %s found %s in %d url(s)";

  /**
   * Converts the search status to a user readable string.
   *
   * @return The search status converted to a string.
   */
  public String getDone() {
    return done ? "done" : "running";
  }

  /**
   * Updates the search status to done.
   */
  public void markAsDone() {
    done = true;
  }

  /**
   * Filters every searched url to only those in which the word was found and
   * convert it to a sorted list.
   *
   * @return A sorted list containing urls.
   */
  public List<String> getUrlsKeywordFoundList() {
    return this.urls.entrySet()
        .stream()
        .filter(Entry::getValue)
        .map(Entry::getKey)
        .sorted()
        .toList();
  }

  /**
   * Generate a formatted log message based on finished search object attributes.
   *
   * @return The formatted log string message.
   */
  public String toLogMessage() {
    final var quantity = getUrlsKeywordFoundList().size();
    final var message = String.format(SEARCH_LOG_TEMPLATE, this.id, this.keyword, quantity);

    return message;
  }

}
