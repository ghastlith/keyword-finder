package ghastlith.keywordfinder.http;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ghastlith.keywordfinder.http.exception.HttpErrorResponseException;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings({ "rawtypes", "unchecked" })
public class HttpRequestSenderTest {

  @Mock private HttpClient mockHttpClient;
  @Mock private HttpResponse mockHttpResponse;
  @InjectMocks private HttpRequestSender mockHttpRequestSender;

  private static final String URL = "https://example.com/";

  @Test
  void doGetRequest_shouldReturnResponseBodyAsListOfLinesWhenRequestIs2xxSuccessful() throws IOException, InterruptedException {
    // given
    final var expectedResponseBody = List.of("<html>", "successful request", "</html>");
    final var responseBodyMock = expectedResponseBody.stream();

    when(mockHttpResponse.statusCode()).thenReturn(200);
    when(mockHttpResponse.body()).thenReturn(responseBodyMock);
    when(mockHttpClient.send(any(), any())).thenReturn(mockHttpResponse);

    // when
    final var response = mockHttpRequestSender.doGetRequest(URL);

    // then
    assertThat(response).isEqualTo(expectedResponseBody);
  }

  @Test
  void doGetRequest_shouldThrowHttpErrorResponseExceptionWhenStatusIsNot2xxSuccessful() throws IOException, InterruptedException {
    // given
    when(mockHttpResponse.statusCode()).thenReturn(403);
    when(mockHttpClient.send(any(), any())).thenReturn(mockHttpResponse);

    // when
    final var throwable = catchThrowable(() -> mockHttpRequestSender.doGetRequest(URL));

    // then
    assertThat(throwable).isInstanceOf(HttpErrorResponseException.class);
  }

}
