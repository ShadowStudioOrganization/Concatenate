package org.shadow.studio.concatenate.backend.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.shadow.studio.concatenate.backend.util.JsonKt;
import org.shadow.studio.concatenate.backend.util.LoginVerifyUtils;

import java.io.IOException;
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
        login.setUrl("https://login.live.com/oauth20_token.srf");
        login.getConnection();
        Map<String, Object> data = new HashMap<>();
        data.put("client_id", "00000000402b5328");
        data.put("code", authcode);
        data.put("grant_type", "authorization_code");
        data.put("redirect_uri", "https://login.live.com/oauth20_desktop.srf");
        data.put("scope", "service::user.auth.xboxlive.com::MBI_SSL");
        System.out.println(login.doPost(JsonKt.parseJsonStr(data)));
        login.closeConnection();
    }
}
