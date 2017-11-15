package com.example.statisticsService.controllers;

import com.example.statisticsService.models.Statistics;
import com.example.statisticsService.models.Transaction;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import static java.time.Instant.now;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpStatus.OK;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class StatisticsControllerIntTest {
    @LocalServerPort
    private Integer port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void shouldGetStatisticsForPostedTransactions() throws Exception {
        final Transaction transactionOne = new Transaction(8, now());
        final Transaction transactionTwo = new Transaction(4, now());
        Statistics expectedStatistic = new Statistics(12, 8, 4, 2, 6);

        restTemplate.postForEntity(createURLWithPort("/transactions"), transactionOne, String.class);
        restTemplate.postForEntity(createURLWithPort("/transactions"), transactionTwo, String.class);
        final ResponseEntity<Statistics> responseEntity = restTemplate.getForEntity(createURLWithPort("/statistics"), Statistics.class);

        assertThat(responseEntity.getStatusCode(), is(OK));
        assertThat(responseEntity.getBody(), is(expectedStatistic));
    }

    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }
}
