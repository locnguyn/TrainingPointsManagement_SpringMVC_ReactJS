/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pbthnxl.controllers;

/**
 *
 * @author DELL
 */
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pbthnxl.services.VNPayService;
import com.pbthnxl.utils.PaypalIntent;
import com.pbthnxl.utils.PaypalUserAction;
import com.pbthnxl.utils.PaypalPaySource;
import com.pbthnxl.vn.zalopay.crypto.HMACUtil;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.TimeZone;
import java.util.logging.Logger;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import org.cloudinary.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import java.util.List;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 *
 * @author hieu
 */
@CrossOrigin
@RestController
@RequestMapping("/api/payment")
@PropertySource("classpath:configs.properties")
public class ApiPaymentController {

    @Autowired
    private Environment env;

    @Autowired
    private VNPayService vnPayService;

    private static final String ACCESS_KEY = "F8BBA842ECF85";
    private static final String SECRET_KEY = "K951B6PE1waDMi640xX08PD3vg6EkVlz";

    //ZALOPAY Version 1
    @CrossOrigin
    @PostMapping(value = "/zalopay/create/", consumes = {
        MediaType.APPLICATION_JSON_VALUE
    })
    public ResponseEntity<String> zalopayCreateOrder(@RequestBody Map<String, String> params) throws UnsupportedEncodingException, IOException {
        if (params.containsKey("amount")) {
            Random rand = new Random();
            int random_id = rand.nextInt(1000000);
            String return_url = env.getProperty("base_url") + env.getProperty("zalopay.return_url");
            final Map embeddata = new HashMap() {
                {
                    put("redirecturl", return_url);
                }
            };

            final Map[] item = {
                new HashMap() {
                    {

                    }
                }
            };

            Map<String, Object> order = new HashMap<String, Object>() {
                {
                    put("appid", env.getProperty("zalopay.appId"));
                    put("apptransid", getCurrentTimeString("yyMMdd") + "_" + random_id); // mã giao dich có định dạng yyMMdd_xxxx
                    put("apptime", System.currentTimeMillis()); // miliseconds
                    put("appuser", "demo");
                    put("amount", params.get("amount"));
                    put("description", "Export PDF Fee - " + params.get("amount"));
                    put("bankcode", "zalopayapp");
                    put("item", new JSONObject(item).toString());
                    put("embeddata", new JSONObject(embeddata).toString());
                }
            };

            // appid +”|”+ apptransid +”|”+ appuser +”|”+ amount +"|" + apptime +”|”+ embeddata +"|" +item
            String data = order.get("appid") + "|" + order.get("apptransid") + "|" + order.get("appuser") + "|" + order.get("amount")
                    + "|" + order.get("apptime") + "|" + order.get("embeddata") + "|" + order.get("item");
            order.put("mac", HMACUtil.HMacHexStringEncode(HMACUtil.HMACSHA256, env.getProperty("zalopay.key1"), data));

            CloseableHttpClient client = HttpClients.createDefault();
            HttpPost post = new HttpPost(env.getProperty("zalopay.create_order"));

            List<NameValuePair> param = new ArrayList<>();
            for (Map.Entry<String, Object> e : order.entrySet()) {
                param.add(new BasicNameValuePair(e.getKey(), e.getValue().toString()));
            }

            // Content-Type: application/x-www-form-urlencoded
            post.setEntity(new UrlEncodedFormEntity(param));

            CloseableHttpResponse res = client.execute(post);
            BufferedReader rd = new BufferedReader(new InputStreamReader(res.getEntity().getContent()));
            StringBuilder resultJsonStr = new StringBuilder();
            String line;

            while ((line = rd.readLine()) != null) {
                resultJsonStr.append(line);
            }

            JSONObject result = new JSONObject(resultJsonStr.toString());

            System.out.println(result);

            return new ResponseEntity<>(result.toString(), HttpStatus.CREATED);

//            RestTemplate restTemplate = new RestTemplate();
//
//            HttpHeaders headers = new HttpHeaders();
//            headers.setContentType(MediaType.APPLICATION_JSON);
//            
//            ObjectMapper objectMapper = new ObjectMapper();
//            String orderJson = objectMapper.writeValueAsString(order);
//
//            HttpEntity<String> request = new HttpEntity<>(orderJson, headers);
//            ResponseEntity<String> response = restTemplate.postForEntity(env.getProperty("zalopay.create_order"), request, String.class);
//
//            return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
        }

        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }

    //    @PostMapping("/zalopay/callback/")
//    @CrossOrigin
//    public ResponseEntity<String> calllback(@RequestBody String jsonStr) {
//        JSONObject result = new JSONObject();
//
//        try {
//            JSONObject cbdata = new JSONObject(jsonStr);
//            String dataStr = cbdata.getString("data");
//            String reqMac = cbdata.getString("mac");
//
//            byte[] hashBytes = HmacSHA256.doFinal(dataStr.getBytes());
//            String mac = DatatypeConverter.printHexBinary(hashBytes).toLowerCase();
//
//            // kiểm tra callback hợp lệ (đến từ ZaloPay server)
//            if (!reqMac.equals(mac)) {
//                // callback không hợp lệ
//                result.put("return_code", -1);
//                result.put("return_message", "mac not equal");
//            } else {
//                // thanh toán thành công
//                // merchant cập nhật trạng thái cho đơn hàng
//                JSONObject data = new JSONObject(dataStr);
//                logger.info("update order's status = success where app_trans_id = " + data.getString("app_trans_id"));
//
//                result.put("return_code", 1);
//                result.put("return_message", "success");
//            }
//        } catch (Exception ex) {
//            result.put("return_code", 0); // ZaloPay server sẽ callback lại (tối đa 3 lần)
//            result.put("return_message", ex.getMessage());
//        }
//
//        // thông báo kết quả cho ZaloPay server
//        return new ResponseEntity<>(result.toString(), HttpStatus.CREATED);
//    }
    //VNPAY
    @PostMapping("/submitOrder")
    public ResponseEntity<String> submitOrder(@RequestParam("amount") int orderTotal,
            @RequestParam("orderInfo") String orderInfo,
            HttpServletRequest request) {
        String return_url = env.getProperty("base_url") + env.getProperty("zalopay.return_url");
        String vnpayUrl = vnPayService.createOrder(orderTotal, orderInfo, return_url);
        return new ResponseEntity<>(vnpayUrl, HttpStatus.OK);
    }

    @GetMapping("/vnpay-payment")
    public String GetMapping(HttpServletRequest request, Model model) {
        int paymentStatus = vnPayService.orderReturn(request);

        String orderInfo = request.getParameter("vnp_OrderInfo");
        String paymentTime = request.getParameter("vnp_PayDate");
        String transactionId = request.getParameter("vnp_TransactionNo");
        String totalPrice = request.getParameter("vnp_Amount");

        model.addAttribute("orderId", orderInfo);
        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("paymentTime", paymentTime);
        model.addAttribute("transactionId", transactionId);

        return paymentStatus == 1 ? "ordersuccess" : "orderfail";
    }

    //PAYPAL
    @PostMapping("/paypal/create/")
    public ResponseEntity<String> paypalCreateOrder(@RequestParam Map<String, String> params) {
        if (params.containsKey("amount") && params.containsKey("access_token")) {
//            String url = "https://api-m.sandbox.paypal.com/v2/checkout/orders";
            String return_url = env.getProperty("paypal.return_url");
            String cancel_url = env.getProperty("paypal.cancel_url");
            String currencyCode = "USD";
            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("PayPal-Request-Id", generateRandomRequestId());
            headers.set("Authorization", "Bearer " + params.get("access_token"));

            String requestBody = buildRequestBody(PaypalIntent.CAPTURE, currencyCode, params.get("amount"), PaypalPaySource.paypal, PaypalUserAction.PAY_NOW, return_url, cancel_url);
//            String requestBody = "{ \"intent\": \"CAPTURE\", \"purchase_units\": [ { \"amount\": { \"currency_code\": \"USD\", \"value\": \"100.00\" } } ], \"payment_source\": { \"paypal\": { \"experience_context\": { \"payment_method_preference\": \"IMMEDIATE_PAYMENT_REQUIRED\", \"brand_name\": \"EXAMPLE INC\", \"locale\": \"en-US\", \"landing_page\": \"LOGIN\", \"shipping_preference\": \"NO_SHIPPING\", \"user_action\": \"PAY_NOW\", \"return_url\": \"http://localhost:3000/\", \"cancel_url\": \"https://example.com/cancelUrl\" } } } }";

            System.out.print(requestBody);

            HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

            ResponseEntity<String> response = restTemplate.postForEntity(env.getProperty("paypal.create_order"), request, String.class);

            return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
        }

        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/paypal/capture/{orderId}")
    public ResponseEntity<String> paypalCaptureOrder(@RequestParam Map<String, String> params, @PathVariable(value = "orderId") String orderId) {
        if (orderId != null && params.containsKey("access_token")) {
            String url = env.getProperty("paypal.capture_order");
            String finalUrl = url.replace("{id}", orderId);

            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer " + params.get("access_token"));

            StringBuilder requestBody = new StringBuilder();

            HttpEntity<String> request = new HttpEntity<>(null, headers);
            ResponseEntity<String> response = restTemplate.exchange(finalUrl, HttpMethod.POST, request, String.class);

            System.out.println("Capture");

            return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
        }

        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }

    private String buildRequestBody(PaypalIntent intent, String currencyCode, String value, PaypalPaySource paySource, PaypalUserAction action, String returnUrl, String cancelUrl) {
        StringBuilder requestBody = new StringBuilder();

        System.out.println(paySource);

        requestBody.append("{ \"intent\": \"").append(intent).append("\", ");
        requestBody.append("\"purchase_units\": [ { ");
        requestBody.append("\"amount\": { \"currency_code\": \"").append(currencyCode).append("\", \"value\": \"").append(value).append("\" } } ], ");
        requestBody.append("\"payment_source\": { \"").append(paySource).append("\": { \"experience_context\": { ");
        requestBody.append("\"payment_method_preference\": \"IMMEDIATE_PAYMENT_REQUIRED\", ");
        requestBody.append("\"brand_name\": \"Hệ Thống Điểm Rèn Luyện Lộc Hiếu\", \"locale\": \"en-US\", \"landing_page\": \"NO_PREFERENCE\", ");
        requestBody.append("\"shipping_preference\": \"NO_SHIPPING\", \"user_action\": \"").append(action).append("\", ");
        requestBody.append("\"return_url\": \"").append(returnUrl).append("\", ");
        requestBody.append("\"cancel_url\": \"").append(cancelUrl).append("\" } } } }");

        return requestBody.toString();
    }

    private String generateRandomRequestId() {
        return UUID.randomUUID().toString();
    }

    public static String getCurrentTimeString(String format) {
        Calendar cal = new GregorianCalendar(TimeZone.getTimeZone("GMT+7"));
        SimpleDateFormat fmt = new SimpleDateFormat(format);
        fmt.setCalendar(cal);
        return fmt.format(cal.getTimeInMillis());
    }

//    public void CallbackController() throws Exception {
//        HmacSHA256 = Mac.getInstance("HmacSHA256");
//        HmacSHA256.init(new SecretKeySpec(key2.getBytes(), "HmacSHA256"));
//    }
}
