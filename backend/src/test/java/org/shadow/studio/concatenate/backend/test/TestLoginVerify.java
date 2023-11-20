package org.shadow.studio.concatenate.backend.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.shadow.studio.concatenate.backend.login.MicrosoftKt;
import org.shadow.studio.concatenate.backend.util.JsonKt;
import org.shadow.studio.concatenate.backend.util.LoginVerifyUtils;

import java.io.IOException;
import java.net.URISyntaxException;
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

        // get access_token(micorosoft) by code
        login.setUrl("https://login.live.com/oauth20_token.srf");
        login.getConnection();
        login.setConRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        String arg1 = "client_id=00000000402b5328" +
                "&code=" + authcode +
                "&grant_type=authorization_code" +
                "&redirect_uri=https://login.live.com/oauth20_desktop.srf" +
                "&scope=service::user.auth.xboxlive.com::MBI_SSL ";
        Map<String, Object> responseTokenJson = JsonKt.parseJsonToMap(login.doPost(arg1));
        String accessToken = (String) responseTokenJson.get("access_token");
        String refreshToken = (String) responseTokenJson.get("refresh_token");
        MicrosoftKt.getAccessToken(accessToken);
    }
}
