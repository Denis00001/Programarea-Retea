package com.company;
import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import okhttp3.*;

class Main {
    static final String url = "https://animego.org/";
    static final Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("117.1.16.131", 8080));
    final static String[] str = new String[55];
    final static String[] str2 = new String[55];
    final static String[] str3 = new String[55];

    public static void main(String[] args)  {

        new Thread(new HeadThread()).start();
        new Thread(new OptionsThread()).start();
        new Thread(new GetThread()).start();
        new Thread(new CookieThreadGet()).start();
        new Thread(new PostThread()).start();
    }


    private static void sendGet() throws Exception {

        HttpsURLConnection httpClient = (HttpsURLConnection) new URL(url).openConnection(proxy);
        httpClient.setRequestMethod("GET");
        //add request header
        httpClient.setRequestProperty("User-Agent", "Mozilla/5.0");
        httpClient.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
        httpClient.setRequestProperty("Connection", "close");
        int responseCode = httpClient.getResponseCode();

        System.out.println("\nSending 'GET' request to URL : " + url);
        System.out.println("Response Code : " + responseCode);

        try (BufferedReader in = new BufferedReader(
                new InputStreamReader(httpClient.getInputStream()))) {

            StringBuilder response = new StringBuilder();
            String line;

            while ((line = in.readLine()) != null) {
                response.append(line);
            }
            in.close();
            //print result
            String s = response.toString();
            Pattern pattern = Pattern.compile("[ ]{40}[А-ЯЁ]+[а-яёAйА -]*");
            Matcher matcher = pattern.matcher(s);
            int i=0;
            while (matcher.find()) {
                str[i] = s.substring(matcher.start(), matcher.end());
                str2[i] = str[i].replaceAll("[ ]{40}", "");
                StringBuilder st = new StringBuilder();
                str3[i] = str2[i].replaceAll("(^(Выберите|ТВ)[а-я ]*)", "");
                i++;
            }
        }
        System.out.println("Найденные на сайте жанры Аниме:");
        for(int i=7;i<str3.length-10;i++){
            System.out.println(str3[i]);
        }
    }

    private static void sendPost() throws Exception {

        HttpsURLConnection httpClient = (HttpsURLConnection) new URL(url).openConnection(proxy);
        //add reuqest header
        httpClient.setRequestMethod("POST");
        httpClient.setRequestProperty("User-Agent", "Mozilla/5.0");
        httpClient.setRequestProperty("Content-Type", "text/html;charset=utf-8");
        httpClient.setRequestProperty("Content-Length","35");
        String urlParameters = "__tn__=%2CdC-R-R&eid=ARA3Cq7oApR5HMeo361GVausjDlCe4yoHbUMEVsxBJHD-RZ-MRj7HJIxXdVabrrVe0k2OdPaYrXloDV1&hc_ref=ARTQFsh4_Vy6fBwKWNdwaHSY8bF1p-hZU9comRYlFOmBoWg4v3AMgkGxAXDL9wrhWQI&fref=nf";

        // Send post request
        httpClient.setDoOutput(true);
        try (DataOutputStream wr = new DataOutputStream(httpClient.getOutputStream())) {
            wr.writeBytes(urlParameters);
            wr.flush();
        }

        int responseCode = httpClient.getResponseCode();
        System.out.println("\nSending 'POST' request to URL : " + url);
        System.out.println("Post parameters : " + urlParameters);
        System.out.println("Response Code : " + responseCode);

        try (BufferedReader in = new BufferedReader(
                new InputStreamReader(httpClient.getInputStream()))) {

            String line;
            StringBuilder response = new StringBuilder();

            while ((line = in.readLine()) != null) {
                response.append(line);
            }
            in.close();
            //print result
            System.out.println(response.toString());

        }

    }

    private static void CookieGet() throws IOException {

        OkHttpClient client = new OkHttpClient.Builder()
                .proxy(proxy)
                .build();
        Request request = new Request.Builder()
                .url("http://elearning.utm.md/moodle/user/profile.php?id=38984")
                .get()
                .addHeader("host", "elearning.utm.md")
                .addHeader("connection", "keep-alive")
                .addHeader("upgrade-insecure-requests", "1")
                .addHeader("dnt", "1")
                .addHeader("user-agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.129 Safari/537.36")
                .addHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
                .addHeader("sec-fetch-site", "same-origin")
                .addHeader("sec-fetch-mode", "navigate")
                .addHeader("sec-fetch-user", "?1")
                .addHeader("sec-fetch-dest", "document")
                .addHeader("referer", "http://elearning.utm.md/moodle/")
                .addHeader("accept-encoding", "gzip, deflate, br")
                .addHeader("accept-language", "ru-RU,ru;q=0.9,en-US;q=0.8,en;q=0.7")
                .addHeader("cookie", "Moodl")
                .build();

        Response response = client.newCall(request).execute();
        System.out.println("\n");
        System.out.println(response.body().string());
}


    private static void sendHead() throws Exception {

        HttpsURLConnection httpClient = (HttpsURLConnection) new URL(url).openConnection(proxy);

        //add reuqest header
        httpClient.setRequestMethod("HEAD");

        int responseCode = httpClient.getResponseCode();
        System.out.println("\nSending 'HEAD' request to URL : " + url);
        System.out.println("Response Code : " + responseCode);
        //httpClient.getInputStream().close();


        Map<String, List<String>> map = httpClient.getHeaderFields();
        for (Map.Entry<String, List<String>> entry : map.entrySet()) {
            System.out.println(entry.getKey() +
                    " : " + entry.getValue());
        }

    }

    private static void sendOptions() throws Exception {

            String url = "http://unite.md/images/Declaratia%20privind%20Politica%20in%20domeniul%20calitatii_ed.7.pdf";
            //String url = "http://ummaadmin.workapp.kz/index.php/rest/user/signup";
            HttpURLConnection httpClient = (HttpURLConnection) new URL(url).openConnection(proxy);

            httpClient.setRequestMethod("OPTIONS");
            int responseCode = httpClient.getResponseCode();
            System.out.println("\nSending 'OPTIONS' request to URL : " + url);
            System.out.println("Response Code : " + responseCode);
            //   httpClient.getInputStream().close();
            System.out.println("Allow: " + httpClient.getHeaderField("Allow"));
 /*       Map<String, List<String>> map = httpClient.getHeaderFields();
        for (Map.Entry<String, List<String>> entry : map.entrySet()) {
            System.out.println("Key : " + entry.getKey() +
                    " ,Value : " + entry.getValue());
        }
 */
    }

    static class GetThread implements Runnable {
        public void run() {
            //логика, которая одновременно доступна только для одного потока
            synchronized (proxy){
                try {
                    sendGet();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    static class PostThread implements Runnable {

        public void run() {
            //логика, которая одновременно доступна только для одного потока
            synchronized (proxy){
                try {
                    sendPost();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    static class OptionsThread implements Runnable {

        public void run() {
            //логика, которая одновременно доступна только для одного потока
            synchronized (proxy){
                try {
                    sendOptions();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    static class CookieThreadGet implements Runnable {

        public void run() {
            //логика, которая одновременно доступна только для одного потока
            synchronized (proxy){
                try {
                    CookieGet();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    static class HeadThread implements Runnable {
        public void run() {
            //логика, которая одновременно доступна только для одного потока
            synchronized (proxy){
                try {
                    sendHead();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}