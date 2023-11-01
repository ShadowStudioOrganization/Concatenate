package org.shadow.studio.concatenate.backend.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

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
            return getFullUUID((String) json.get("id"));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public static String getFullUUID(String trimmedUUID) {
        return trimmedUUID.substring(0, 8) + "-" +
                trimmedUUID.substring(8, 12) + "-" +
                trimmedUUID.substring(12, 16) + "-" +
                trimmedUUID.substring(16, 20) + "-" +
                trimmedUUID.substring(20);
    }
}
