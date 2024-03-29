package com.microsservicos.shoppingapi.integration;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsservicos.dto.ItemDto;
import com.microsservicos.dto.ReportDto;
import com.microsservicos.dto.ShopOutputDto;

@SpringBootTest(properties = {
    "spring.flyway.locations=classpath:db/migration,classpath:db/migration-test",
    "spring.flyway.clean-disabled=false",
    "spring.flyway.schemas=shopping",
    "spring.datasource.url = jdbc:h2:mem:testdb;DB_CLOSE_ON_EXIT=FALSE;MODE=PostgreSQL",
    "spring.datasource.driverClassName = org.h2.Driver",
    "spring.datasource.username = sa",
    "spring.datasource.password = password",
    "spring.datasource.hikari.maximumPoolSize=2",
    "spring.jpa.database-platform = org.hibernate.dialect.PostgreSQLDialect",
    "spring.jpa.properties.hibernate.dialect = org.hibernate.dialect.PostgreSQLDialect",
    "spring.jpa.properties.hibernate.default_schema = shopping",
    "server.port = -1",
    "logging.level.org.flywaydb=DEBUG"
})
@ActiveProfiles("test")
@DirtiesContext
@AutoConfigureMockMvc
public class ReportControllerTest {
  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  private ReportDto report = new ReportDto(1L, 2001.0, 2001.0);
  private ReportDto allReport = new ReportDto(2L, 2021.0, 1010.5);
  private ReportDto emptyReport = new ReportDto(0L, 0.0, 0.0);

  private static ItemDto ITEM1;
  private static ItemDto ITEM2;
  private static ShopOutputDto SHOP1;
  private static ShopOutputDto SHOP2;

  @BeforeAll
  private static void setup() throws ParseException {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
    ITEM1 = new ItemDto("video-game", 2001.0f, 1);
    ITEM2 = new ItemDto("video-game", 20.0f, 2);
    SHOP1 = new ShopOutputDto("12345678910", 2001.0, LocalDateTime.parse("2023-12-31 01:51:36.789", formatter), List.of(ITEM1));
    SHOP2 = new ShopOutputDto("12345678910", 20.0, LocalDateTime.parse("2020-12-31 01:51:36.789", formatter), List.of(ITEM2));
  }

  @Test
  public void reportSuccessAllResult() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/shopping/report")
        .param("start", "2000-01-01")
        .param("end", "2024-01-01")
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(allReport)));
  }

  @Test
  public void reportSuccessSingleOneResult() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/shopping/report")
        .param("start", "2023-01-01")
        .param("end", "2024-01-01")
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(report)));
  }

  @Test
  public void reportSuccessSingleTwoResult() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/shopping/report")
        .param("start", "2020-01-01")
        .param("end", "2021-01-01")
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(
            MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(new ReportDto(1L, 20.0, 20.0))));
  }

  @Test
  public void reportSuccessNoDataReturn() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/shopping/report")
        .param("start", "2000-01-01")
        .param("end", "2001-01-01")
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(emptyReport)));
  }

  @Test
  public void reportNoArguments() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/shopping/report")
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
  }

  @Test
  public void reportNoStartArg() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/shopping/report")
        .param("start", "2000-01-01")
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
  }

  @Test
  public void reportNoEndArg() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/shopping/report")
        .param("end", "2001-01-01")
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
  }

  @Test
  public void searchSuccessAllResultNoOptionalArgs() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/shopping/search")
        .param("start", "2000-01-01")
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(List.of(SHOP1, SHOP2))));
  }

  @Test
  public void searchSuccessFilterByMin() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/shopping/search")
        .param("start", "2000-01-01")
        .param("min", "1000")
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(List.of(SHOP1))));
  }

  @Test
  public void searchSuccessFilterByEnd() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/shopping/search")
        .param("start", "2000-01-01")
        .param("end", "2024-01-01")
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(List.of(SHOP1, SHOP2))));
  }

  @Test
  public void searchSuccessFilterByStart() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/shopping/search")
        .param("start", "2023-01-01")
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(List.of(SHOP1))));
  }

  @Test
  public void searchSuccessAllFilter() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/shopping/search")
        .param("start", "2000-01-01")
        .param("end", "2024-01-01")
        .param("min", "1000")
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(List.of(SHOP1))));
  }

  @Test
  public void searchNoStartArg() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/shopping/search")
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
  }

  @Test
  public void searchInvalidStartArg() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/shopping/search")
        .param("start", "test")
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
  }

  @Test
  public void searchInvalidEndArg() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/shopping/search")
        .param("start", "2000-01-01")
        .param("end", "test")
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
  }
  @Test
  public void searchInvalidMinArg() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/shopping/search")
        .param("start", "2000-01-01")
        .param("end", "2024-01-01")
        .param("min", "test")
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
  }
}
