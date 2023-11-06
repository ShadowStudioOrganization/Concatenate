package org.shadow.studio.concatenate.backend.util;

import java.awt.*;
import java.io.*;
import java.net.*;

public class LoginVerifyUtils {
    private static HttpURLConnection connection = null;
    private static URL url = null;
    private static URI uri = null;

    public void setUrl(String url1) throws MalformedURLException {
        url = new URL(url1);
    }
    public void setUri(String uri1) throws URISyntaxException {
        uri = new URI(uri1);
    }
    public void getConnection() throws IOException {
        connection = (url==null)?null:(HttpURLConnection) url.openConnection();
    }
    public void closeConnection() {
        connection.disconnect();
    }
    public void setConRequestProperty(String key, String value) {
        connection.setRequestProperty(key,value);
    }

    public boolean Oauth() throws URISyntaxException {
        this.setUri("https://login.live.com/oauth20_authorize.srf" +
                "?client_id=00000000402b5328" +
                "&response_type=code" +
                "&scope=service%3A%3Auser.auth.xboxlive.com%3A%3AMBI_SSL" +
                "&redirect_uri=https%3A%2F%2Flogin.live.com%2Foauth20_desktop.srf");
        if (Desktop.isDesktopSupported()) {
            Desktop desktop = Desktop.getDesktop();

            if (desktop.isSupported(Desktop.Action.BROWSE)) {
                try {
                    desktop.browse(uri);
                    return true;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }


    /**
     * doGet
     * @return 响应数据
     */
    public String doGet() {
        InputStream is=null;
        BufferedReader br = null;
        StringBuffer result=new StringBuffer();
        try {
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(15000);
            connection.setReadTimeout(15000);
            connection.connect();
            // get response
            if(connection.getResponseCode()==200){
                is=connection.getInputStream();
                if(is!=null){
                    br=new BufferedReader(new InputStreamReader(is,"UTF-8"));
                    String temp = null;
                    while ((temp=br.readLine())!=null){
                        result.append(temp);
                    }
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(br!=null){
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(is!=null){
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result.toString();
    }

    /**
     * doPost
     * @param param 请求参数
     * @return 响应数据
     */
    public String doPost(String param) {
        StringBuffer result=new StringBuffer();
        OutputStream os=null;
        InputStream is=null;
        BufferedReader br=null;
        try {
            connection.setRequestMethod("POST");
            connection.setConnectTimeout(15000);
            connection.setReadTimeout(15000);
            connection.setDoOutput(true);
            connection.setDoInput(true);
            // set args and type
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            // send param
            DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
            wr.writeBytes(param);
            wr.close();
            // get response
            System.out.println(connection.getResponseCode());
            if(connection.getResponseCode()==200){
                is=connection.getInputStream();
                if(is!=null){
                    br=new BufferedReader(new InputStreamReader(is,"UTF-8"));
                    String temp=null;
                    if((temp=br.readLine())!=null){
                        result.append(temp);
                    }
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(br!=null){
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(os!=null){
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(is!=null){
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result.toString();

    }
}
