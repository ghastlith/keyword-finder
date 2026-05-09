package ghastlith.indexer.api.search.data;

import org.hibernate.validator.constraints.URL;

import jakarta.validation.constraints.Size;

/**
 * The request body provided by the user to kickstart the search process.
 */
public record SearchRequestBody(
    @Size(min = 4, max = 32, message = "The keyword size must be between 4 and 32")
    String keyword,
    @URL(message = "The baseurl must be a valid url")
    String baseurl
) {}
