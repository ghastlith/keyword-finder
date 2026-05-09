package ghastlith.indexer.api.search.data;

import ghastlith.indexer.api.search.model.SearchInformation;
import lombok.Builder;

/**
 * The information of a search request to be added to a
 * {@link ListDisplayResponse} that is returned to the user.
 */
@Builder
public record ListDisplayElement(
    String id,
    String keyword,
    String baseurl,
    String status,
    Integer looked,
    Integer found
) {

  /**
   * Constructor that builds a ListDisplayElement to return to the user in a
   * readable way.
   *
   * @param information the ListDisplayElement of the search
   * @return The ListDisplayElement based on the search information.
   */
  public static ListDisplayElement fromSearchInformation(final SearchInformation information) {
    return ListDisplayElement.builder()
        .id(information.getId())
        .keyword(information.getKeyword())
        .baseurl(information.getBaseurl())
        .status(information.getDone())
        .looked(information.getUrls().size())
        .found(information.getUrlsKeywordFoundList().size())
        .build();
  }

}
