package com.cathay.coindesk.controller;

import com.cathay.coindesk.repository.CoinRepository;
import com.cathay.coindesk.usecase.createcoin.*;
import com.cathay.coindesk.usecase.updatecoin.*;
import com.ibm.icu.util.Currency;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/coin")
public class CoinController {

    @Autowired
    private CreateCoinInteractor createCoinInteractor;

    @Autowired
    private UpdateCoinInteractor updateCoinInteractor;

    @Autowired
    private CoinRepository coinRepository;

    private String getCurrencyChineseName(String currencyCode) {
        Locale locale = new Locale("zh_TW");
        Currency currency = Currency.getInstance(currencyCode);
        return currency.getDisplayName(locale);
    }

    @GetMapping(path = "coindesk",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CoindeskResponseBean> getCoinDesks() {
        RestTemplate restTemplate = new RestTemplate();
        GetCoinDeskResponse coindesk = restTemplate.getForObject("https://api.coindesk.com/v1/bpi/currentprice.json", GetCoinDeskResponse.class);
        CoindeskResponseBean responseBean = new CoindeskResponseBean();


        if(coindesk!=null) {
            if(coindesk.getTime()!=null) {
                DateTimeFormatter inputFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
                DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                ZonedDateTime parsedTime = ZonedDateTime.parse(coindesk.getTime().getUpdatedISO(), inputFormatter);

                String updateTime = parsedTime.format(outputFormatter);
                responseBean.setUpdatedTime(updateTime);

            }
            responseBean.setCoinBeanList(new ArrayList<>());
            for(Map.Entry<String, GetCoinDeskResponse.Currency> entry : coindesk.getBpi().entrySet()) {
                CoindeskCoinBean bean = new CoindeskCoinBean();
                bean.setCode(entry.getKey());
                bean.setRateFloat(entry.getValue().getRateFloat());
                bean.setChineseName(getCurrencyChineseName(entry.getKey()));
                responseBean.getCoinBeanList().add(bean);
            }
        }

        return ResponseEntity.ok(responseBean);
    }
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<GetCoinResponseBean>> getCoins() {

        return ResponseEntity.ok(coinRepository.findAll().stream().map(coin -> {
            GetCoinResponseBean response = new GetCoinResponseBean();
            response.setId(coin.getId());
            response.setCode(coin.getCode());
            response.setChineseName(coin.getChineseName());
            return response;
        }).collect(Collectors.toList()));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CreateCoinResponseBean> addCoin(@Valid @RequestBody CreateCoinRequestBean coinRequestBean) {
        CreateCoinInput input = new CreateCoinInput();
        input.setChineseName(coinRequestBean.getChineseName());
        input.setCode(coinRequestBean.getCode());
        CreateCoinOutput output = new CreateCoinOutput();

        CreateCoinResponseBean responseBean = new CreateCoinResponseBean();
        try {
            createCoinInteractor.execute(input, output);
            responseBean.setId(output.getId());
            responseBean.setMessage("Create Coin successful");
        } catch (CreateInvalidCoinException e) {
            responseBean.setMessage("Invalid code");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBean);
        } catch (CreateDuplicateCoinException e) {
            responseBean.setMessage("Coin duplicate");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(responseBean);
        } catch (CreateCoinException e) {
            return ResponseEntity.internalServerError().build();

        }
        return ResponseEntity.status(HttpStatus.CREATED).body(responseBean);
    }

    @PatchMapping(path = "/{id}",consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UpdateCoinResponseBean> updateCoin(@PathVariable Long id,@Valid @RequestBody UpdateCoinRequestBean coinRequestBean) {
        UpdateCoinInput input = new UpdateCoinInput();
        input.setId(id);
        input.setChineseName(coinRequestBean.getChineseName());
        input.setCode(coinRequestBean.getCode());

        UpdateCoinOutput output = new UpdateCoinOutput();

        UpdateCoinResponseBean responseBean = new UpdateCoinResponseBean();
        try {
            updateCoinInteractor.execute(input, output);
            responseBean.setId(output.getId());
            responseBean.setChineseName(output.getChineseName());
            responseBean.setCode(output.getCode());
            responseBean.setMessage("Update Coin successful");
        } catch (UpdateInvalidCoinException e) {
            responseBean.setMessage("Invalid code");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBean);
        } catch (UpdateDuplicateCoinException e) {
            responseBean.setMessage("Coin duplicate");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(responseBean);
        } catch (UpdateCoinException e) {
            return ResponseEntity.internalServerError().build();

        }
        return ResponseEntity.ok(responseBean);
    }

    @DeleteMapping(path = "/{id}" ,produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DeleteCoinResponseBean> deleteCoin(@PathVariable Long id) {

        coinRepository.deleteById(id);
        DeleteCoinResponseBean responseBean = new DeleteCoinResponseBean();
        responseBean.setMessage("Delete Coin successful");
        return ResponseEntity.ok(responseBean);
    }
}
