package test;

import java.io.*;
import java.util.*;
import java.net.*;
import javax.net.ssl.*;
import java.security.*;

public class UBConnection
{

    private List<String> cookies;
    private final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0";
    private int fileCounter = 0;

    public static void main(String[] args) throws Exception
    {
        System.setProperty("http.proxySet", "true");
        System.setProperty("http.proxyHost", "localhost");
        System.setProperty("http.proxyPort", "2121");
        UBConnection http = new UBConnection();
        http.login("gfhgfh", "fghfh");
    }

    public UBConnection() throws Exception
    {
        configSSL();
    }

    public static void configSSL() throws Exception
    {
        System.setProperty("https.protocols", "SSLv3");
        SSLContext ctx = SSLContext.getInstance("TLS");
        ctx.init(new KeyManager[0], new TrustManager[]
        {
            new DefaultTrustManager()
        }, new SecureRandom());
        SSLContext.setDefault(ctx);
        CookieHandler.setDefault(new CookieManager());
    }

    public void login(String username, String password) throws Exception
    {
        String loginUrl = "http://www.example.com/login";
        CookieHandler.setDefault(new CookieManager());
        //String page = new String(send(true, loginUrl, "", true));
        String postParams = getFormParams(username, password);
        byte[] b = send(true, loginUrl, postParams, false);
    }

    private byte[] send(boolean isGet, String url, String postParams, boolean isSecure) throws Exception
    {
        if (postParams == null)
        {
            postParams = "";
        }
        URL urlObj = new URL(url);
        HttpURLConnection conn;
        if (isSecure == true)
        {
            SSLSocketFactory sslsocketfactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
            conn = (HttpsURLConnection) urlObj.openConnection();
            ((HttpsURLConnection) conn).setSSLSocketFactory(sslsocketfactory);
        }
        else
        {
            conn = (HttpURLConnection) urlObj.openConnection();
        }
        if (isGet == true)
        {
            conn.setRequestMethod("GET");
        }
        else
        {
            conn.setRequestMethod("POST");
        }
        System.out.println("Aum: " + conn);
        conn.setUseCaches(false);
        conn.setRequestProperty("Host", urlObj.getHost());
        conn.setRequestProperty("User-Agent", USER_AGENT);
        conn.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        conn.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
        conn.setRequestProperty("Referer", url);
        if (cookies != null)
        {
            for (String cookie : this.cookies)
            {
                conn.addRequestProperty("Cookie", cookie);
            }
        }
        conn.setRequestProperty("Connection", "keep-alive");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        if (isGet == false)
        {
            conn.setRequestProperty("Content-Length", Integer.toString(postParams.length()));
        }

        conn.setDoOutput(true);
        conn.setDoInput(true);

        if ((isGet == false) && (postParams != null))
        {
            DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
            wr.writeBytes(postParams);
            wr.flush();
            wr.close();
        }
        int responseCode = conn.getResponseCode();
        System.out.println("\nSending " + (isGet == true ? "GET" : "POST") + " request to URL : " + url);
        if (isGet == false)
        {
            System.out.println("Post parameters : " + postParams);
        }
        System.out.println("Response Code : " + responseCode);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataInputStream din = new DataInputStream(conn.getInputStream());
        int i = 0;
        while (i != -1)
        {
            i = din.read();
            bos.write(i);
        }
        byte[] b = bos.toByteArray();
        putToFile(new String(b), "file" + fileCounter + ".htm");
        fileCounter++;
        return b;
    }

    public String getFormParams(String username, String password) throws UnsupportedEncodingException
    {
        return "auth=ldap&url=%5EU&user=" + username + "&pass=" + password + "&submit.x=66&submit.y=23";
    }

    public List<String> getCookies()
    {
        return cookies;
    }

    public void setCookies(List<String> cookies)
    {
        this.cookies = cookies;
    }

    public byte[] setPost(String url, String params, boolean https) throws Exception
    {
        return send(false, url, params, https);
    }

    public byte[] setGet(String url, String params, boolean https) throws Exception
    {
        return send(true, url, params, https);
    }

    private void putToFile(String data, String filename) throws Exception
    {
        System.out.println("Saving to file " + filename);
        FileWriter fw = new FileWriter(new File(filename));
        fw.write(data);
        fw.close();
    }
}
