package ghastlith.indexer.api.exception;

import java.time.Instant;
import java.util.List;

import lombok.Builder;

/**
 * The information related to an exception thrown inside any endpoint call too
 * later be formatted in a pattern that matches the one used by Spring.
 */
@Builder
public record ErrorResponse(
    Instant timestamp,
    Integer status,
    String error,
    List<String> message,
    String path
) {}
