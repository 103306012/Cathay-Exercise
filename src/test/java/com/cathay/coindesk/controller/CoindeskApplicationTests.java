package com.cathay.coindesk.controller;

import com.cathay.coindesk.repository.Coin;
import com.cathay.coindesk.repository.CoinRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CoinControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private CoinRepository coinRepository;

    @BeforeEach
    void setUp() {
        coinRepository.deleteAll(); // 清理数据库，确保测试独立性
    }

    @Test
    void testAddCoin() {
        Map<String, String> request = new HashMap<>();
        request.put("code", "TWD");
        request.put("chineseName", "台幣");

        webTestClient.post()
                .uri("/coin")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.message").isEqualTo("Create Coin successful")
                .jsonPath("$.id").exists();

        assertThat(coinRepository.findAll()).hasSize(1);
    }

    @Test
    void testUpdateCoin() {
        Coin coin = new Coin();
        coin.setCode("BTC");
        coin.setChineseName("比特幣");
        Coin savedCoin = coinRepository.save(coin);

        Map<String, String> request = new HashMap<>();
        request.put("code", "TWD");
        request.put("chineseName", "台幣");

        webTestClient.patch()
                .uri("/coin/{id}", savedCoin.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.message").isEqualTo("Update Coin successful")
                .jsonPath("$.id").exists()
                .jsonPath("$.chineseName").isEqualTo("台幣")
                .jsonPath("$.code").isEqualTo("TWD");
        Coin updatedCoin = coinRepository.findById(savedCoin.getId()).get();
        assertThat(updatedCoin.getChineseName()).isEqualTo("台幣");
        assertThat(updatedCoin.getCode()).isEqualTo("TWD");
    }

    @Test
    void testGetCoins() {
        Coin coin1 = new Coin();
        coin1.setCode("BTC");
        coin1.setChineseName("比特幣");
        coinRepository.save(coin1);

        Coin coin2 = new Coin();
        coin2.setCode("ETH");
        coin2.setChineseName("以太幣");
        coinRepository.save(coin2);

        webTestClient.get()
                .uri("/coin")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.length()").isEqualTo(2)
                .jsonPath("$[0].code").isEqualTo("BTC")
                .jsonPath("$[1].code").isEqualTo("ETH");
    }

    @Test
    void testDeleteCoin() {
        Coin coin = new Coin();
        coin.setCode("BTC");
        coin.setChineseName("比特幣");
        Coin savedCoin = coinRepository.save(coin);

        webTestClient.delete()
                .uri("/coin/{id}", savedCoin.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.message").isEqualTo("Delete Coin successful");

        assertThat(coinRepository.findAll()).isEmpty();
    }

    @Test
    void testCoindesk(){
        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject("https://api.coindesk.com/v1/bpi/currentprice.json",String.class);
        assertThat(response).isNotBlank();

    }

    @Test
    void testGetNewCoinsDesk() {

       String responseBody =  webTestClient.get()
                .uri("/coin/coindesk")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .returnResult()
                .getResponseBody();
        assertThat(responseBody).isNotBlank();
    }
}
