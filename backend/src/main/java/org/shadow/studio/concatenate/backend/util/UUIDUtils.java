package org.shadow.studio.concatenate.backend.util;

import com.fasterxml.jackson.databind.json.JsonMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Base64;

import static com.fasterxml.jackson.module.kotlin.ExtensionsKt.jacksonObjectMapper;

public class UUIDUtils {


    public static String getUUIDByUsername(String username) {
        try {
            Map<String, String> json = getJsonFromUrl("https://api.mojang.com/users/profiles/minecraft/", username);
            return json.get("id");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public static String getFullUUID(String username) {
        String trimmedUUID = getUUIDByUsername(username);
        return trimmedUUID.substring(0, 8) + "-" +
                trimmedUUID.substring(8, 12) + "-" +
                trimmedUUID.substring(12, 16) + "-" +
                trimmedUUID.substring(16, 20) + "-" +
                trimmedUUID.substring(20);
    }
    public static Boolean getSkinByUUID(String Uuid) {
        try {
            Map<String, List> json = getJsonFromUrl("https://sessionserver.mojang.com/session/minecraft/profile/", Uuid);
            Map<String, String> properties = (Map<String, String>) json.get("properties").get(0);
            Map<String, Object> jsonSkin = JsonKt.parseJson(decodeFromBase64(properties.get("value")));
            String username = (String) jsonSkin.get("profileName");
            String urlSkin = jsonSkin.get("textures").toString().split("url=")[1];
            if (jsonSkin.get("textures").toString().split("url=").length > 2) {
                urlSkin = urlSkin.split("},")[0];
                String urlCape = jsonSkin.get("textures").toString().split("url=")[2].split("}}")[0];
                ExecutorUtils.downloadFromUrl(urlCape, username+"_cape.png");
            } else {
                urlSkin = urlSkin.split("}}")[0];
            }
            ExecutorUtils.downloadFromUrl(urlSkin, username+"_skin.png");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private static String decodeFromBase64(String encodeString) {
        byte[] decodedBytes = Base64.getDecoder().decode(encodeString);
        return new String(decodedBytes);
    }
    private static <T> Map<String, T> getJsonFromUrl(String urlstr, String arg) throws IOException {
        URL url = new URL(urlstr + arg);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String line;
        StringBuilder response = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();
        return (Map<String, T>) JsonKt.parseJson(response.toString());
    }
}
