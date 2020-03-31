package com.pingxx.example;

import com.pingplusplus.Pingpp;
import com.pingplusplus.model.Charge;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * Created by Afon on 16/4/26.
 */
public class Main {

    /**
     * Pingpp 管理平台对应的 API Key，api_key 获取方式：登录 [Dashboard](https://dashboard.pingxx.com)->点击管理平台右上角公司名称->开发信息-> Secret Key
     */
    private final static String apiKey = "sk_test_C8Wvj9Py1GKC18KqjTf9i5W5";

    /**
     * Pingpp 管理平台对应的应用 ID，app_id 获取方式：登录 [Dashboard](https://dashboard.pingxx.com)->点击你创建的应用->应用首页->应用 ID(App ID)
     */
    private final static String appId = "app_40ir9Oi5e5OO0O0W";

    /**
   * 设置请求签名密钥，密钥对需要你自己用 openssl 工具生成，如何生成可以参考帮助中心：https://help.pingxx.com/article/123161；
   * 生成密钥后，需要在代码中设置请求签名的私钥(rsa_private_key.pem)；
   * 然后登录 [Dashboard](https://dashboard.pingxx.com)->点击右上角公司名称->开发信息->商户公钥（用于商户身份验证）
   * 将你的公钥复制粘贴进去并且保存->先启用 Test 模式进行测试->测试通过后启用 Live 模式
   */

    // 你生成的私钥路径
    private final static String privateKeyFilePath = "-----BEGIN RSA PRIVATE KEY-----\n" +
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

    protected static String projectDir;

    public static void main(String[] args) throws Exception {
        projectDir = System.getProperty("user.dir") + "/example/";

        // 设置 API Key
        Pingpp.apiKey = apiKey;

        Pingpp.appId = appId;

        // 设置私钥路径，用于请求签名
//        Pingpp.privateKeyPath = privateKeyFilePath;

        /**
         * 或者直接设置私钥内容
         Pingpp.privateKey = "-----BEGIN RSA PRIVATE KEY-----\n" +
         "... 私钥内容字符串 ...\n" +
         "-----END RSA PRIVATE KEY-----\n";
         */
        Pingpp.privateKey = "-----BEGIN RSA PRIVATE KEY-----\n" +
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


        ChargeExample example = new ChargeExample(appId);
        Charge charge = example.createCharge();
        System.out.println();

//        // Refund 示例
//        RefundExample.runDemos();
//
//        // RedEnvelope 示例
//        RedEnvelopeExample.runDemos(appId);
//
//        // Transfer 示例
//        TransferExample.runDemos(appId);
//
//        // Event 示例
//        EventExample.runDemos();
//
//        // Webhooks 验证示例
//        WebhooksVerifyExample.runDemos();
//
//        // 微信公众号 openid 相关示例
//        WxPubOAuthExample.runDemos(appId);
//
//        // 身份证银行卡信息认证接口
//        // 请使用 live key 调用该接口
//        // IdentificationExample.runDemos(appId);
//
//        // 批量付款示例
//        BatchTransferExample.runDemos(appId);
//
//        // 报关
//        // 请使用 live key 调用该接口
//        CustomsExample.runDemos(appId);
    }

    private static SecureRandom random = new SecureRandom();

    public static String randomString(int length) {
        String str = new BigInteger(130, random).toString(32);
        return str.substring(0, length);
    }

    public static int currentTimeSeconds() {
        return (int)(System.currentTimeMillis() / 1000);
    }
}
