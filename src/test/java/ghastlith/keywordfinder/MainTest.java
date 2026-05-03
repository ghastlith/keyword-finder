package ghastlith.keywordfinder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import com.jayway.jsonpath.JsonPath;

import ghastlith.keywordfinder.api.healthcheck.HealthcheckController;
import ghastlith.keywordfinder.api.search.SearchController;
import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
public class MainTest {

  @Autowired private HealthcheckController healthcheckController;
  @Autowired private SearchController searchController;
  @Autowired private MockMvc mockMvc;

  @Test
  void sanityCheck() {
    assertThat(healthcheckController).isNotNull();
    assertThat(searchController).isNotNull();
  }

  @Test
  void integrationTest() throws Exception {
    final var healthcheckCheckResponse = mockMvc.perform(get("/"))
        .andExpect(status().isOk())
        .andReturn()
        .getResponse();

    log.info(
      "{}\n{}",
      healthcheckCheckResponse.getStatus(),
      healthcheckCheckResponse.getContentAsString()
    );

    final var searchNewSearchBody = "{ \"keyword\": \"magic\", \"baseurl\": \"https://example.com\" }";
    final var searchNewSearchResponse = mockMvc.perform(post("/search")
        .contentType(APPLICATION_JSON)
        .content(searchNewSearchBody))
        .andExpect(status().isCreated())
        .andReturn()
        .getResponse();

    log.info(
      "{}\n{}",
      searchNewSearchResponse.getStatus(),
      new JSONObject(searchNewSearchResponse.getContentAsString()).toString(4)
    );

    final var searchListSearchesResponse = mockMvc.perform(get("/search"))
        .andExpect(status().isOk())
        .andReturn()
        .getResponse();

    log.info(
      "{}\n{}",
      searchListSearchesResponse.getStatus(),
      new JSONObject(searchListSearchesResponse.getContentAsString()).toString(4)
    );

    final var searchId = JsonPath.read(searchNewSearchResponse.getContentAsString(), "$.id");
    final var searchDisplaySearchResponse = mockMvc.perform(get("/search/" + searchId))
        .andExpect(status().isOk())
        .andReturn()
        .getResponse();

    log.info(
      "{}\n{}",
      searchDisplaySearchResponse.getStatus(),
      new JSONObject(searchDisplaySearchResponse.getContentAsString()).toString(4)
    );
  }

}
