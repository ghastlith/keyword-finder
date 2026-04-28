package ghastlith.keywordfinder.api.search.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

public class SearchInformationTest {

  private static final String ID = "05GNJVHm";
  private static final String KEYWORD = "magic";
  private static final String BASEURL = "https://example.com/";

  @Test
  void getDone_shouldReturnDoneStatusWhenDoneIsTrue() {
    // given
    final var searchInformation = new SearchInformation(ID, KEYWORD, BASEURL);

    // when
    searchInformation.markAsDone();

    // then
    assertEquals("done", searchInformation.getDone());
  }

  @Test
  void getDone_shouldReturnRunningStatusWhenDoneIsFalse() {
    // given
    final var searchInformation = new SearchInformation(ID, KEYWORD, BASEURL);

    // when

    // then
    assertEquals("running", searchInformation.getDone());
  }

  @Test
  void updateDone_shouldSetDoneStatusToTrue() {
    // given
    final var searchInformation = new SearchInformation(ID, KEYWORD, BASEURL);

    // when
    searchInformation.markAsDone();

    // then
    assertEquals("done", searchInformation.getDone());
  }

  @Test
  void getUrlsKeywordFoundList_shouldReturnSortedListWithValuesWhenFoundUrls() {
    // given
    final var searchInformation = new SearchInformation(ID, KEYWORD, BASEURL);

    final var url1 = BASEURL + "/page1";
    final var url2 = BASEURL + "/page2";
    final var url3 = BASEURL + "/page3";

    searchInformation.getUrls().put(url3, true);
    searchInformation.getUrls().put(url2, false);
    searchInformation.getUrls().put(url1, true);

    final var expectedUrls = List.of(url1, url3);

    // when
    final var urls = searchInformation.getUrlsKeywordFoundList();

    // then
    assertEquals(expectedUrls, urls);
  }

  @Test
  void getUrlsKeywordFoundList_shouldReturnEmptyListWhenThereIsNoFoundUrls() {
    // given
    final var searchInformation = new SearchInformation(ID, KEYWORD, BASEURL);

    final var url1 = BASEURL + "/page1";
    final var url2 = BASEURL + "/page2";
    final var url3 = BASEURL + "/page3";

    searchInformation.getUrls().put(url3, false);
    searchInformation.getUrls().put(url2, false);
    searchInformation.getUrls().put(url1, false);

    final var expectedUrls = List.of();

    // when
    final var urls = searchInformation.getUrlsKeywordFoundList();

    // then
    assertEquals(expectedUrls, urls);
  }

  @Test
  void getUrlsKeywordFoundList_shouldReturnEmptyListWhenUrlsMapIsEmpty() {
    // given
    final var searchInformation = new SearchInformation(ID, KEYWORD, BASEURL);

    final var expectedUrls = List.of();

    // when
    final var urls = searchInformation.getUrlsKeywordFoundList();

    // then
    assertEquals(expectedUrls, urls);
  }

}
