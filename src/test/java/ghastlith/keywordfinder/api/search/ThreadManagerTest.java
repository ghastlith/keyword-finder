package ghastlith.keywordfinder.api.search;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ghastlith.keywordfinder.api.search.model.SearchInformation;
import ghastlith.keywordfinder.http.HttpRequestSender;

@ExtendWith(MockitoExtension.class)
public class ThreadManagerTest {

  @Mock private SearchInformation mockInformation;
  @Mock private ExecutorService mockExecutor;
  @Mock private HttpRequestSender mockHttpRequestSender;
  @Mock private AtomicInteger mockRunningThreadsCount;

  private static final String ID = "05GNJVHm";
  private static final String KEYWORD = "magic";
  private static final String BASEURL = "https://example.com/";

  @Test
  void run_threadCounterIncrementedWhenNewThreadStarted() {
    // given
    final var runningThreadsCount = new AtomicInteger(0);
    final var threadManager = new ThreadManager(mockInformation, mockExecutor, mockHttpRequestSender, runningThreadsCount);

    // when
    threadManager.run(BASEURL);

    // then
    assertThat(runningThreadsCount.get()).isEqualTo(1);
  }

  @Test
  void run_shouldAddUniqueUrlToInformationUrlsMap() {
    // given
    final var information = new SearchInformation(ID, KEYWORD, BASEURL);
    final var threadManager = new ThreadManager(information, mockExecutor, mockHttpRequestSender, mockRunningThreadsCount);
    final var expectedMap = Map.of(
      BASEURL, false
    );

    // when
    threadManager.run(BASEURL);

    // then
    assertThat(information.getUrls()).isEqualTo(expectedMap);
  }

  @Test
  void updateUrlKeywordWasFound_shouldUpdateUrlsMapValueToTrueWhenKeywordWasFound() {
    // given
    final var information = new SearchInformation(ID, KEYWORD, BASEURL);
    final var threadManager = new ThreadManager(information, mockExecutor, mockHttpRequestSender, mockRunningThreadsCount);
    final var expectedMap = Map.of(
      BASEURL, true
    );

    // when
    threadManager.run(BASEURL);
    threadManager.updateUrlKeywordWasFound(BASEURL);

    // then
    assertThat(information.getUrls()).isEqualTo(expectedMap);
  }

  @Test
  void integration_searchInformationUrlsShouldReturnUniqueAndValidUrls() {
    // given
    final var information = new SearchInformation(ID, KEYWORD, BASEURL);
    final var threadManager = new ThreadManager(information, mockExecutor, mockHttpRequestSender, mockRunningThreadsCount);

    final var url1 = BASEURL;
    final var url2 = BASEURL + "/bar.html";
    final var url3 = BASEURL + "/foo.html";

    final var expectedUrlsMap = Map.of(
      url1, false,
      url2, true,
      url3, true
    );
    final var expectedUrlsKeywordFoundList = List.of(url2, url3);

    // when
    threadManager.run(url1);
    threadManager.run(url2);
    threadManager.run(url2); // repeating this value to test uniqueness
    threadManager.run(url3);

    threadManager.updateUrlKeywordWasFound(url2);
    threadManager.updateUrlKeywordWasFound(url3);

    // then
    assertThat(information.getUrls()).isEqualTo(expectedUrlsMap);
    assertThat(information.getUrlsKeywordFoundList()).isEqualTo(expectedUrlsKeywordFoundList);
  }

}
