package com.bcb.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class UrlConnUtil {

    public final static String getResult(String urlstr) {
        StringBuffer sb = new StringBuffer();
        InputStream in = null;
        String result11 = "";
        try {
            URL url = new URL(urlstr);
            in = url.openStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in, "utf-8"));
            String inputLine = "";
            while ((inputLine = reader.readLine()) != null) {
                sb.append(inputLine);
            }
            result11 = sb.toString();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
            } catch (Exception var13) {
                var13.printStackTrace();
            }
        }
        return result11;
    }
}
