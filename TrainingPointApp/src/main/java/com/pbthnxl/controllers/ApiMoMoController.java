//
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.entity.StringEntity;
//import org.apache.http.impl.client.CloseableHttpClient;
//import org.apache.http.impl.client.HttpClients;
//import org.apache.http.util.EntityUtils;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import java.io.UnsupportedEncodingException;
//
//import javax.crypto.Mac;
//import javax.crypto.spec.SecretKeySpec;
//import java.nio.charset.StandardCharsets;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.UUID;
//import java.security.*;
//import java.util.Formatter;
//import com.google.gson.Gson;
//import com.google.gson.GsonBuilder;
//import com.pbthnxl.momo.HttpResponse;
//import com.pbthnxl.momo.PaymentRequest;
//import com.pbthnxl.momo.PaymentResponse;
//import okhttp3.Request;
//
//@RestController
//@RequestMapping("/api")
//public class ApiMoMoController {
//
//    private String momoCreateUrl = "https://test-payment.momo.vn/v2/gateway/api/create";
//    @Value("${SERVER_URL}")
//    private String serverUrl;
//
//    private static final String PARTNER_CODE = "MOMO";
//    private static final String ACCESS_KEY = "F8BBA842ECF85";
//    private static final String SECRET_KEY = "K951B6PE1waDMi640xX08PD3vg6EkVlz";
//
//    @PostMapping("/momo-pay/")
//    public ResponseEntity<Map<String, Object>> momoPay(@RequestBody Map<String, Object> request) throws Exception {
//        String requestId = UUID.randomUUID().toString();
//        String amount = "40000";
//        String orderId = UUID.randomUUID().toString();
//        String orderInfo = "pay with MoMo";
//        String requestType = "captureWallet";
//        String extraData = "";
//        String redirectUrl = serverUrl;
//        String ipnUrl = serverUrl + "/api/momo-pay/ipn";
//
//        String rawSignature = String.format(
//                "accessKey=%s&amount=%s&extraData=%s&ipnUrl=%s&orderId=%s&orderInfo=%s&partnerCode=%s&redirectUrl=%s&requestId=%s&requestType=%s",
//                ACCESS_KEY, amount, extraData, ipnUrl, orderId, orderInfo, PARTNER_CODE, redirectUrl, requestId, requestType
//        );
//
//        String signature = hmacSHA256(rawSignature, SECRET_KEY);
//
//        Map<String, String> data = new HashMap<>();
//        data.put("partnerCode", PARTNER_CODE);
//        data.put("partnerName", "Vé Máy Bay Giá Rẻ");
//        data.put("requestId", requestId);
//        data.put("amount", amount);
//        data.put("orderId", orderId);
//        data.put("orderInfo", orderInfo);
//        data.put("redirectUrl", redirectUrl);
//        data.put("ipnUrl", ipnUrl);
//        data.put("lang", "vi");
//        data.put("extraData", extraData);
//        data.put("requestType", requestType);
//        data.put("signature", signature);
//        
//        System.out.println(data);
//
//        String jsonResponse = postRequest(momoCreateUrl, data);
//        Map<String, Object> responseData = new ObjectMapper().readValue(jsonResponse, Map.class);
//
//        if (responseData != null && responseData.get("payUrl") != null) {
//            Map<String, Object> responseMap = new HashMap<>();
//            responseMap.put("ok", "200");
//            responseMap.put("payUrl", responseData.get("payUrl"));
//            responseMap.put("orderId", responseData.get("orderId"));
//            return new ResponseEntity<>(responseMap, HttpStatus.OK);
//        } else {
//            Map<String, Object> errorMap = new HashMap<>();
//            errorMap.put("error", data);
//            return new ResponseEntity<>(errorMap, HttpStatus.BAD_REQUEST);
//        }
//    }
//
//    private String postRequest(String url, Map<String, String> data) throws Exception {
//        CloseableHttpClient httpClient = HttpClients.createDefault();
//        HttpPost post = new HttpPost(url);
//        post.setHeader("Content-Type", "application/json");
//        String json = new ObjectMapper().writeValueAsString(data);
//        StringEntity entity = new StringEntity(json);
//        post.setEntity(entity);
//        return EntityUtils.toString(httpClient.execute(post).getEntity(), StandardCharsets.UTF_8);
//    }
//
//    private String hmacSHA256(String data, String secret) throws Exception {
//        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
//        SecretKeySpec secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
//        sha256_HMAC.init(secretKey);
//        return bytesToHex(sha256_HMAC.doFinal(data.getBytes(StandardCharsets.UTF_8)));
//    }
//
//    private String bytesToHex(byte[] bytes) {
//        StringBuilder hexString = new StringBuilder();
//        for (byte b : bytes) {
//            String hex = Integer.toHexString(0xff & b);
//            if (hex.length() == 1) hexString.append('0');
//            hexString.append(hex);
//        }
//        return hexString.toString();
//    }
//}
