package ghastlith.keywordfinder.api.search;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Service;

import ghastlith.keywordfinder.api.search.data.ListDisplayElement;
import ghastlith.keywordfinder.api.search.data.ListDisplayResponse;
import ghastlith.keywordfinder.api.search.data.NewSearchResponse;
import ghastlith.keywordfinder.api.search.data.SearchRequestBody;
import ghastlith.keywordfinder.api.search.data.SingleDisplayResponse;
import ghastlith.keywordfinder.api.search.exception.UnknownIdException;
import ghastlith.keywordfinder.api.search.model.SearchInformation;
import ghastlith.keywordfinder.api.search.utility.IdUtilities;
import ghastlith.keywordfinder.http.HttpRequestSender;
import lombok.RequiredArgsConstructor;

/**
 * Manages and performs all search related operations.
 */
@Service
@RequiredArgsConstructor
public class SearchService {

  private final ExecutorService executor;
  private final HttpRequestSender httpRequestSender;
  private final AtomicInteger runningThreadsCount;
  private final Map<String, SearchInformation> searches = new ConcurrentHashMap<>();

  /**
   * Starts a new search process based on the request body.
   *
   * @param body the request body provide by the user
   * @return A new {@link NewSearchResponse} containing the id.
   */
  public NewSearchResponse newSearch(final SearchRequestBody body) {
    final var id = IdUtilities.generateNewId(searches);

    final var information = new SearchInformation(id, body.keyword(), body.baseurl());
    this.searches.put(id, information);

    spawnFirstSearchThread(information);

    return new NewSearchResponse(id);
  }

  /**
   * Retrieves all the current searches and formats them to a user readable
   * output.
   *
   * @return The list of every {@link ListDisplayElement}.
   */
  public ListDisplayResponse listSearches() {
    final var elements = new ArrayList<ListDisplayElement>();

    for (final var entry : searches.entrySet()) {
      elements.add(ListDisplayElement.fromSearchInformation(entry.getValue()));
    }

    return new ListDisplayResponse(elements);
  }

  /**
   * Retrieves a single search and format it to a user readable output.
   *
   * @param id the id of the desired search information
   * @return The user readable data of the search information.
   */
  public SingleDisplayResponse displaySearch(final String id) {
    if (!IdUtilities.idExists(this.searches, id)) {
      throw new UnknownIdException(id);
    }

    final var information = this.searches.get(id);

    return SingleDisplayResponse.fromSearchInformation(information);
  }

  private void spawnFirstSearchThread(final SearchInformation information) {
    final var threadManager = new ThreadManager(information, executor, httpRequestSender, runningThreadsCount);
    threadManager.run(information.getBaseurl());
  }

}
