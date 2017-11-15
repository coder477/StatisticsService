package com.example.statisticsService.controllers;

import com.example.statisticsService.models.Transaction;
import com.example.statisticsService.services.TransactionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;

import static org.mockito.Mockito.verify;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = TransactionController.class)
public class TransactionsControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private TransactionService transactionService;

    @Test
    public void shouldReturnSuccessForTransactionPostRequest() throws Exception {
        final Transaction transaction = new Transaction(12.3, Instant.now());

        mockMvc.perform(post("/transactions")
                .contentType(APPLICATION_JSON_UTF8)
                .content(mapper.writeValueAsString(transaction))
                .accept(APPLICATION_JSON_UTF8))
                .andExpect(status().isCreated())
                .andExpect(content().string(""))
                .andReturn();

        verify(transactionService).add(transaction);
    }
}
