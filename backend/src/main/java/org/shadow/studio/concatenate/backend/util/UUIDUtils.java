package org.shadow.studio.concatenate.backend.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.Base64;

import org.shadow.studio.concatenate.backend.util.ExecutorUtils;

public class UUIDUtils {
    public static String getUUIDByUsername(String username) {
        try {
            URL url = new URL("https://api.mojang.com/users/profiles/minecraft/" + username);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            StringBuilder response = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();
            Map<String, Object> json = JsonKt.parseJson(response.toString());
            return (String) json.get("id");
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
            URL url = new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + Uuid);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            StringBuilder response = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();
            Map<String, Object> json = JsonKt.parseJson(response.toString());
            ArrayList properties = (ArrayList) json.get("properties");
            String urlSkinCape = decodeFromBase64(properties.get(0).toString().split("value=")[1].split("}")[0]);
            Map<String, Object> jsonSkin = JsonKt.parseJson(urlSkinCape);
            String username = (String) jsonSkin.get("profileName");
            if (jsonSkin.get("textures").toString().split("url=").length > 2) {
                String urlSkin = jsonSkin.get("textures").toString().split("url=")[1].split("},")[0];
                ExecutorUtils.downloadFromUrl(urlSkin, username+"_skin.png");
                String urlCape = jsonSkin.get("textures").toString().split("url=")[2].split("}}")[0];
                ExecutorUtils.downloadFromUrl(urlCape, username+"_cape.png");
            } else {
                String urlSkin = jsonSkin.get("textures").toString().split("url=")[1].split("}}")[0];
                ExecutorUtils.downloadFromUrl(urlSkin, username+"_skin.png");
            }
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
}
