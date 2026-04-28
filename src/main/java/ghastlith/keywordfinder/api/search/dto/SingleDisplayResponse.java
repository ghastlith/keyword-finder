package ghastlith.keywordfinder.api.search.dto;

import java.util.List;

import ghastlith.keywordfinder.api.search.model.SearchInformation;
import lombok.Builder;

/**
 * The information of a search request to be returned to the user.
 */
@Builder
public record SingleDisplayResponse(
    String id,
    String keyword,
    String baseurl,
    String status,
    Integer looked,
    Integer found,
    List<String> urls
) {

  /**
   * Constructor that builds a SingleDisplayResponse to return to the user in a
   * readable way.
   *
   * @param information the information of the search
   * @return The SingleDisplayResponse based on the search information.
   */
  public static SingleDisplayResponse fromSearchInformation(final SearchInformation information) {
    return SingleDisplayResponse.builder()
        .id(information.getId())
        .keyword(information.getKeyword())
        .baseurl(information.getBaseurl())
        .status(information.getDone())
        .looked(information.getUrls().size())
        .found(information.getUrlsKeywordFoundList().size())
        .urls(information.getUrlsKeywordFoundList())
        .build();
  }

}
