/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pbthnxl.controllers;



/**
 *
 * @author DELL
 */
import com.pbthnxl.services.VNPayService;
import com.pbthnxl.utils.PaypalIntent;
import com.pbthnxl.utils.PaypalUserAction;
import com.pbthnxl.utils.PaypalPaySource;
import com.pbthnxl.vn.zalopay.crypto.HMACUtil;
import java.text.SimpleDateFormat;
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
import java.util.Base64;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import org.apache.http.client.methods.HttpHead;
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
    
    private Logger logger = Logger.getLogger(this.getClass().getName());
    private String key2 = "eG4r0GcoNtRGbO8";
    private Mac HmacSHA256;
    private static Map<String, String> config = new HashMap<String, String>() {
        {
            put("app_id", "2553");
            put("key1", "PcY4iZIKFCIdgZvA6ueMcMHHUbRLYjPL");
            put("key2", "kLtgPl8HHhfvMuDHPwKfgfsY4Ydm9eIz");
            put("endpoint", "https://sb-openapi.zalopay.vn/v2/create");
        }
    };
    private static final String CLIENT_ID = "AThRltxPxUWCRRenurymjrI2hc4hIY6dc6nh4sVAXgc8D6rLz_Gjyn1QZ8VQVyqDMwLhoD4elut5Uuwu";
    private static final String CLIENT_SECRET = "EKiVQSnvUCsUZ_CKqyzV4DQQ-ccV3y5asgf_snM-dr_Q5SuPoJtZ5uI8lq6Rs9geMQCBXRI9i_DZu3oI";

    public static String getCurrentTimeString(String format) {
        Calendar cal = new GregorianCalendar(TimeZone.getTimeZone("GMT+7"));
        SimpleDateFormat fmt = new SimpleDateFormat(format);
        fmt.setCalendar(cal);
        return fmt.format(cal.getTimeInMillis());
    }
    
    public void CallbackController() throws Exception {
        HmacSHA256 = Mac.getInstance("HmacSHA256");
        HmacSHA256.init(new SecretKeySpec(key2.getBytes(), "HmacSHA256"));
    }

    @PostMapping(value = "/zalopay/create/", consumes = {
        MediaType.APPLICATION_JSON_VALUE
    })
    @CrossOrigin
    public ResponseEntity<String> createOrder(@RequestParam Map<String, String> params) {
        Random rand = new Random();
        int random_id = rand.nextInt(1000000);
        final Map embed_data = new HashMap() {
            {
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
                put("app_id", config.get("app_id"));
                put("app_trans_id", getCurrentTimeString("yyMMdd") + "_" + random_id); // translation missing: vi.docs.shared.sample_code.comments.app_trans_id
                put("app_time", System.currentTimeMillis()); // miliseconds
                put("app_user", "user123");
                put("amount", 50000);
                put("description", "Lazada - Payment for the order #" + random_id);
                put("bank_code", "zalopayapp");
                put("item", new JSONObject(item).toString());
                put("embed_data", new JSONObject(embed_data).toString());
            }
        };

        // app_id +”|”+ app_trans_id +”|”+ appuser +”|”+ amount +"|" + app_time +”|”+ embed_data +"|" +item
        String data = order.get("app_id") + "|" + order.get("app_trans_id") + "|" + order.get("app_user") + "|" + order.get("amount")
                + "|" + order.get("app_time") + "|" + order.get("embed_data") + "|" + order.get("item");
        order.put("mac", HMACUtil.HMacHexStringEncode(HMACUtil.HMACSHA256, config.get("key1"), data));

        return new ResponseEntity<>(null, HttpStatus.CREATED);
    }
    
    @PostMapping("/submitOrder")
    public ResponseEntity<String> submitOrder(@RequestParam("amount") int orderTotal,
                            @RequestParam("orderInfo") String orderInfo,
                            HttpServletRequest request){
        String vnpayUrl = vnPayService.createOrder(orderTotal, orderInfo, "http://localhost:3000/stats");
        return new ResponseEntity<>(vnpayUrl, HttpStatus.OK);
    }

    @GetMapping("/vnpay-payment")
    public String GetMapping(HttpServletRequest request, Model model){
        int paymentStatus =vnPayService.orderReturn(request);

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

    @CrossOrigin
    @PostMapping("/paypal/create/")
    public ResponseEntity<String> paypalCcreateOrder(@RequestParam Map<String, String> params) {
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
    
    @CrossOrigin
    @PostMapping("/paypal/capture/{orderId}")
    public ResponseEntity<String> paypalCaptureOrder(@RequestParam Map<String, String> params, @PathVariable(value = "orderId") String orderId ){
        if(orderId != null && params.containsKey("access_token")){
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
    
    private String encodeCredentials(String clientId, String clientSecret) {
        String credentials = clientId + ":" + clientSecret;
        return Base64.getEncoder().encodeToString(credentials.getBytes());
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
}
