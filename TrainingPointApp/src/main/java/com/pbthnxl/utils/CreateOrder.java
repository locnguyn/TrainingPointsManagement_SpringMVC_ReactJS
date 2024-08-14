package com.pbthnxl.utils;

import static com.pbthnxl.utils.Helpers.getAppTransId;

import org.json.JSONObject;

import java.util.Date;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class CreateOrder {

    private final RestTemplate restTemplate;

    public CreateOrder(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    private class CreateOrderData {

        String AppId;
        String AppUser;
        String AppTime;
        String Amount;
        String AppTransId;
        String EmbedData;
        String Items;
        String BankCode;
        String Description;
        String Mac;

        private CreateOrderData(String amount) throws Exception {
            long appTime = new Date().getTime();
            AppId = String.valueOf(2553);
            AppUser = "Điểm rèn luyện Lộc Hiếu";
            AppTime = String.valueOf(appTime);
            Amount = amount;
            AppTransId = Helpers.getAppTransId();
            EmbedData = "{}";
            Items = "[]";
            BankCode = "zalopayapp";
            Description = "Thanh toán dịch vụ trên Điểm rèn luyện Lộc Hiếu - #" + getAppTransId();
            String inputHMac = String.format("%s|%s|%s|%s|%s|%s|%s",
                    this.AppId,
                    this.AppTransId,
                    this.AppUser,
                    this.Amount,
                    this.AppTime,
                    this.EmbedData,
                    this.Items);

            Mac = Helpers.getMac("PcY4iZIKFCIdgZvA6ueMcMHHUbRLYjPL", inputHMac);
        }
    }

    public JSONObject createOrder(String amount) throws Exception {
        // Initialize the order data
        CreateOrderData input = new CreateOrderData(amount);

        // Convert CreateOrderData to a Map or JSON
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        JSONObject requestBody = new JSONObject();
        requestBody.put("appid", input.AppId);
        requestBody.put("appuser", input.AppUser);
        requestBody.put("apptime", input.AppTime);
        requestBody.put("amount", input.Amount);
        requestBody.put("apptransid", input.AppTransId);
        requestBody.put("embeddata", input.EmbedData);
        requestBody.put("item", input.Items);
        requestBody.put("bankcode", input.BankCode);
        requestBody.put("description", input.Description);
        requestBody.put("mac", input.Mac);

        String url = "https://sb-openapi.zalopay.vn/v2/create";
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httppost = new HttpPost(url);

//        List<NameValuePair> params = new ArrayList<NameValuePair>(2);
//        params.add(new BasicNameValuePair("param-1", "12345"));
//        params.add(new BasicNameValuePair("param-2", "Hello!"));
        httppost.setEntity((HttpEntity) requestBody);

        HttpResponse response = httpclient.execute(httppost);
        HttpEntity entity = response.getEntity();

        System.err.println(entity.toString() + "________________________________");

        // Convert the response to JSONObject
        return new JSONObject(response.getEntity());
    }
}
