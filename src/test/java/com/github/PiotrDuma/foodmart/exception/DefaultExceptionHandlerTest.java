package com.github.PiotrDuma.foodmart.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.github.PiotrDuma.foodmart.FoodmartRestfulApiApplication;
import com.github.PiotrDuma.foodmart.tools.JsonFormatter;
import java.time.Clock;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@WebMvcTest(ExceptionControllerMock.class)
@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = {DefaultExceptionHandlerTest.Config.class})
class DefaultExceptionHandlerTest {
  @Autowired
  private MockMvc mockMvc;

  @Test
  void shouldHandleRuntimeException() throws Exception{
    String expected = """
        {
            "requestURL": "/test/runtime",
            "errorMessage": "Something went wrong",
            "statusCode": 500,
            "timestamp": "2023-11-24T19:12:00.906435344"
        }
        """;

    MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/test/runtime"))
        .andExpect(status().is5xxServerError())
        .andExpect(content().contentType("application/json"))
        .andReturn();
    String result = mvcResult.getResponse().getContentAsString();

    assertEquals(JsonFormatter.getRawString(expected), result);
  }

  @Test
  void shouldHandlePathNotFoundException() throws Exception{
    String expected = """
        {
            "requestURL": "/test/path",
            "errorMessage": "The requested URL does not exist",
            "statusCode": 404,
            "timestamp": "2023-11-24T19:12:00.906435344"
        }
        """;

    MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/test/path"))
        .andExpect(status().isNotFound())
        .andExpect(content().contentType("application/json"))
        .andReturn();
    String result = mvcResult.getResponse().getContentAsString();

    assertEquals(JsonFormatter.getRawString(expected), result);
  }

  @Test
  void shouldHandleResourceNotFoundException() throws Exception{
    String expected = """
        {
            "requestURL": "/test/resource",
            "errorMessage": "Something is not found",
            "statusCode": 404,
            "timestamp": "2023-11-24T19:12:00.906435344"
        }
        """;

    MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/test/resource"))
        .andExpect(status().isNotFound())
        .andExpect(content().contentType("application/json"))
        .andReturn();
    String result = mvcResult.getResponse().getContentAsString();

    assertEquals(JsonFormatter.getRawString(expected), result);
  }

  @TestConfiguration
  @Import(FoodmartRestfulApiApplication.class)
  static class Config{
    private static final ZonedDateTime CURRENT_TIME = ZonedDateTime.of(
        2023,
        11,
        24,
        19,
        12,
        0,
        906435344,
        ZoneId.of("UTC"));

    @Bean
    @Primary
    public Clock getClock(){
      return Clock.fixed(CURRENT_TIME.toInstant(), CURRENT_TIME.getZone());
    }
  }
}