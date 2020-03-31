package com.pingxx.example.utils;

import com.pingplusplus.Pingpp;
import com.pingplusplus.exception.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
public class ChargeUtils {

    public static void main(String[] args) {
        com.pingplusplus.model.Charge charge=null;
        //Test key
        Pingpp.apiKey = "sk_test_C8Wvj9Py1GKC18KqjTf9i5W5";

        //APP ID
        Pingpp.appId="app_40ir9Oi5e5OO0O0W";

        //私钥 用你自己的私钥
        Pingpp.privateKey="-----BEGIN RSA PRIVATE KEY-----\n" +
                "MIICXgIBAAKBgQDJ7LpSPg8cszQo1ZoSox7G1woqMnHcn1teYJnSvF7+9PAcPWX9\n" +
                "TMyhQQQTaSkehYtbCpdyN40GEyGxHPnJw0L5MSRRtZVbC8SHFS/N7keC0Auu1M5b\n" +
                "fH+VcAjOU02NRJLhlVGEaZcFvT/M4W73oM9RX+PrG0DNBSYO33sm1GAx4QIDAQAB\n" +
                "AoGBAKIvWQw7ummj6m4t+t2jVVICYLC56ch52qp4TrT8BhUkaUNVcXi9WdLfZMWu\n" +
                "fo98Vg0e+buMsOHDNhmLG3qaMkyEYXivNLLcnnRXAf5blNDHfpiN1TSxMQkzt5Va\n" +
                "KvhioEpuhrCjYr9RJOMgqzdNse4ItrsVRWHY0xoIZr/navNRAkEA9qA49EF6NAEL\n" +
                "VjWURSU2y6RFRfyqDzYsRdr0whfXyBcAlfjzEkYzuJphgFtLtl4/RkUmQA/Vfcdq\n" +
                "wynfzoF4xQJBANGZiylO7VcKNLXO+Bm0SrjYfm8bSrsxiixb4Xxkq9gid3HpFJAK\n" +
                "HnSUR6GSi7sWIgY4uRjCMdMHTS5o+tBhDm0CQQDydrznrkPlQq2RmOVnQVnoxVxx\n" +
                "nSDYCatnFgeRln1HMw4ZY4IdkjUhJW38EkWSSoAspqkfTDHZEaftrYph7Ln9AkBl\n" +
                "63/b6mFGvdO7xJSoCx96muuAI4lHVWOXBmazElDtnHTwkyJImGMO1TuvuY7wvmZ1\n" +
                "GAgJngSUVSyWsL2lrkxRAkEAwatSheirvr+3/s8Pqik823VROot/LiVK6hWYscHW\n" +
                "DTVw5yJcFz2YXRrXFcQGGKm/A0unIyBzccbe8A3+YJFmwA==\n" +
                "-----END RSA PRIVATE KEY-----";
        Map<String, Object> chargeParams = new HashMap<String, Object>();
        String orderNo = new Date().getTime()+"";
        chargeParams.put("order_no",orderNo);
        chargeParams.put("amount",300);
        chargeParams.put("channel","alipay");
        chargeParams.put("currency","cny");
        chargeParams.put("client_ip","192.168.31.130");
        chargeParams.put("subject","冲金币");
        chargeParams.put("body","名片赞");
        Map<String, String> app = new HashMap<String, String>();
        app.put("id","app_CC8ej9iPWfPCHKe9");
        chargeParams.put("app",app);
        try {
            charge= com.pingplusplus.model.Charge.create("app_40ir9Oi5e5OO0O0W",chargeParams);
            String chargeString = charge.toString();
            System.out.println(chargeString);
        } catch (AuthenticationException e) {
            e.printStackTrace();
        } catch (InvalidRequestException e) {
            e.printStackTrace();
        } catch (APIConnectionException e) {
            e.printStackTrace();
        } catch (APIException e) {
            e.printStackTrace();
        } catch (ChannelException e) {
            e.printStackTrace();
        } catch (RateLimitException e) {
            e.printStackTrace();
        }

    }


}
