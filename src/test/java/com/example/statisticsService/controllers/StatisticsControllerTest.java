package com.example.statisticsService.controllers;

import com.example.statisticsService.models.Statistics;
import com.example.statisticsService.services.TransactionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = StatisticsController.class)
public class StatisticsControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private TransactionService transactionService;

    @Test
    public void shouldReturnStatisticsForUploadedTransactions() throws Exception {
        final Statistics statistics = new Statistics(10.0, 5.4, 1.2, 5);
        when(transactionService.getStatistics()).thenReturn(statistics);
        final String expectedJson = "{ \"sum\": 10.0, \"average\": 2.0, \"max\": 5.4, \"min\": 1.2, \"count\": 5 }";
        final Statistics expectedStatistics = mapper.readValue(expectedJson, Statistics.class);

        final MvcResult result = mockMvc.perform(get("/statistics")
                .accept(APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("average", is(2.0)))
                .andReturn();

        verify(transactionService).getStatistics();
        assertThat(mapper.readValue(result.getResponse().getContentAsString(), Statistics.class), is(expectedStatistics));
    }
}
