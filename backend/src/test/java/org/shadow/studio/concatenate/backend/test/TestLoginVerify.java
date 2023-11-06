package org.shadow.studio.concatenate.backend.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.shadow.studio.concatenate.backend.util.JsonKt;
import org.shadow.studio.concatenate.backend.util.LoginVerifyUtils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class TestLoginVerify {
    @Test
    public void login() throws IOException, URISyntaxException {
        LoginVerifyUtils login = new LoginVerifyUtils();
        // get code from Oauth
        Assertions.assertTrue(login.Oauth());
        Scanner scanner = new Scanner(System.in);
        System.out.println("输入authcode:");
        String authcode = scanner.nextLine();
//        System.out.println(authcode);

        // get access_token(micorosoft) by code
        login.setUrl("https://login.live.com/oauth20_token.srf");
        login.getConnection();
        String arg1 = "client_id=00000000402b5328" +
                "&code=" + authcode +
                "&grant_type=authorization_code" +
                "&redirect_uri=https://login.live.com/oauth20_desktop.srf" +
                "&scope=service::user.auth.xboxlive.com::MBI_SSL ";
        Map<String, Object> responseTokenJson = JsonKt.parseJson(login.doPost(arg1));
        String accessToken = (String) responseTokenJson.get("access_token");
        String refreshToken = (String) responseTokenJson.get("refresh_token");

        // get token(Xbox) by access_token(micorosoft)
//        String accessToken = "EwAYA+pvBAAUKods63Ys1fGlwiccIFJ+qE1hANsAAXDxWzRCZrGcWuF68UGr/J9Rm+/Wb22F3FYLud7vUycx2HHi1VV7JsWwqJ/lNYSl7GaA0qFpBmxCDInzb1FkTh7agxAYvp0BySwd1Khcql34WgImATdkF1TrHvxYPBHsWTuhthYbpDkzzsmTqCIm4Al9dgDxZ+F+76fjbU1PCKU0Tvy3Ik82dEGrzvEbvx1ktx4fZwSFtZL3LTW+bzWJrUovmEFWDa7O9EIviFqS6CHPZI8ca26hj3m0xrwDUc4ZI4nwA/ltOE+AGXwI1OjCX8jnluQZh3N284P5dU4IrOZN2k9KSky3MfpeDzivLF4dDYMkvhnVb+cIN3F2/yBos3kDZgAACCmPI906WY1G6AFOxgnJgyRJ6bhu+IJV/qpXYSQlIecnf63AARR6JGiA92L4rVExDbeisR021L01c1hFn/jdpmFiGliNmxrQ328INiakDDtPP+ri3GhzEpjGs3uBeXLOxJ/t+iSULDdXUdp6ckPSTxYRdfARXeMefxkGh6J87XO8yEVUVdieP6DpGMSEknFTdwUVLgZOj+84rQQz4HqYIckQZ/1A7Ax08eRGlrlcmkzuHBl/5t3JZR/tiNXvX0xVBOqdaVP+x29tghRo+kDsp4fOXzIqtHwxNz6KdWErS2avvqklWOafs6sLJn6TpM9U5K/KJOCZ3wesI2/WcKoVJCHulORLD6EmD9/F9Tn1Rt06S8raVoLwCiMSCM1O7OVY7rtkHQWkViQ2HZ8frRKc0G5IpxgBAhR1OeMrRFGHc6m/0SY51Ds69nKMo6MdaYKdMBY/wgxoOcSiqN8/3DXP/mf6rqGGxtaiTKLX5wAburBqjWmDr0Qu7EIIZy9d0+oIhI/QpJdWamr+G/OxhMfvEhl4Nu7RGEaWq4Q9gmMRD0LW1v2zHa3v3ASHcy/JVH522qboUCnhbEX2uyxLzZOrSS0baVmH0t4pikZ8FFsHrenWX1NvIyGn8sqdh/PXsM1QiM2zK5ExWZBrnQp+YO3wXnzxwhsC";
        login.setUrl("https://user.auth.xboxlive.com/user/authenticate");
        login.getConnection();
        String arg2 = String.format( """
                {
                    "Properties": {
                        "AuthMethod": "RPS",
                        "SiteName": "user.auth.xboxlive.com",
                        "RpsTicket": "%s"
                    },
                    "RelyingParty": "http://auth.xboxlive.com",
                    "TokenType": "JWT"
                 }""", accessToken);
        Map<String, Object> xBoxResponseTokenJson = JsonKt.parseJson(login.doPost(arg2));
        String xBoxToken = (String) xBoxResponseTokenJson.get("Token");
        String uhs = xBoxResponseTokenJson.get("DisplayClaims").toString().split("uhs=")[1].split("}]}")[0];
//        System.out.println("Token:" + xBoxToken);
//        System.out.println("uhs:" + uhs);

        // get token(XSTS) by token(Xbox)
        login.setUrl("https://xsts.auth.xboxlive.com/xsts/authorize");
        login.getConnection();
        String arg3 = String.format("""
                {
                                    "Properties": {
                                        "SandboxId": "RETAIL",
                                        "UserTokens": [
                                            "%s"
                                        ]
                                    },
                                    "RelyingParty": "rp://api.minecraftservices.com/",
                                    "TokenType": "JWT"
                                 }
                """, xBoxToken);
        Map<String, Object> XSTSResponseTokenJson = JsonKt.parseJson(login.doPost(arg3));
        System.out.println(XSTSResponseTokenJson);
        String XSTSToken = (String) XSTSResponseTokenJson.get("Token");
        String uhs2 = XSTSResponseTokenJson.get("DisplayClaims").toString().split("uhs=")[1].split("}]}")[0];
//        System.out.println(XSTSToken);
//        System.out.println("uhs2:" + uhs2);

        // get access_token(minecraft) by token(XSTS) and uhs
        login.setUrl("https://api.minecraftservices.com/authentication/login_with_xbox");
        login.getConnection();
        String identityToken =  "XBL3.0 x=" + uhs + ";"+XSTSToken;
        String arg4 = String.format("""
                {
                    "identityToken": "%s"
                 }
                """, identityToken);
        System.out.println(arg4);
        Map<String, Object> GameResponseTokenJson = JsonKt.parseJson(login.doPost(arg4));
        String gameAceeseToken = (String) GameResponseTokenJson.get("access_token");
        System.out.println("acces_token(minecraft):\n" + gameAceeseToken);
        login.closeConnection();
    }
}
