package ghastlith.keywordfinder.api.search.data;

import java.util.List;

/**
 * The information of all search requests to be returned to the user.
 */
public record ListDisplayResponse(
    List<ListDisplayElement> searches
) {}
