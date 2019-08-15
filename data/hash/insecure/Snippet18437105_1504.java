package com.example.hcpandroid;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.math.BigInteger;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import net.sf.json.JSON;
import net.sf.json.JSONSerializer;
import net.sf.json.xml.XMLSerializer;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONValue;
import sun.misc.BASE64Encoder;

/**
 * @author sajithru
 */
public class SSLAuthenticate {

    private String username;
    private String password;
    private DefaultHttpClient httpClient;
    private String cookie;

    public SSLAuthenticate(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public SSLAuthenticate() {
    }

    public DefaultHttpClient getHttpClient() {
        return httpClient;
    }

    public String getCookie() {
        return cookie;
    }

    public String getUsername() {
        return username;
    }

    public int authenticate() {

        HttpResponse responce = null;
        try {
            //Authenticate SSL Certification
            TrustStrategy easyStrategy = new TrustStrategy() {
                @Override
                public boolean isTrusted(X509Certificate[] chain, String authType)
                        throws CertificateException {
                    // eh, why not?
                    return true;
                }
            };

            SchemeRegistry schemeRegistry = new SchemeRegistry();
            SSLContext sslcontext = SSLContext.getInstance("TLS");
            sslcontext.init(null, null, null);
            SSLSocketFactory ssf = new SSLSocketFactory((KeyStore) easyStrategy);
            ssf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

            Scheme httpsScheme = new Scheme("https", ssf, 443);
            schemeRegistry.register(httpsScheme);

            TrustManager trustMgr = new TrustManager() {
            };
            sslcontext.init(null, new TrustManager[]{trustMgr}, null);

            HttpParams params = new BasicHttpParams();
            ClientConnectionManager connMgr = new ThreadSafeClientConnManager(params, schemeRegistry);
            httpClient = new DefaultHttpClient(connMgr, params);

            //Encode username into BASE64 encode format
            BASE64Encoder base64Encoder = new BASE64Encoder();
            String uname64 = base64Encoder.encode(username.getBytes());

            //Encode password into MD5 Hash encode format
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(password.getBytes(), 0, password.length());
            String md5Password = new BigInteger(1, messageDigest.digest()).toString(16);

            //Set HTTPS request header- Authentication
            cookie = "hcp-ns-auth=" + uname64 + ":" + md5Password;
            System.out.println("Username: " + username + " Password: " + password);
            System.out.println(cookie);

            responce = adminAuth(cookie, httpClient);       

        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(SSLAuthenticate.class.getName()).log(Level.SEVERE, null, ex);
        } catch (KeyManagementException ex) {
            Logger.getLogger(SSLAuthenticate.class.getName()).log(Level.SEVERE, null, ex);
        } catch (KeyStoreException ex) {
            Logger.getLogger(SSLAuthenticate.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnrecoverableKeyException ex) {
            Logger.getLogger(SSLAuthenticate.class.getName()).log(Level.SEVERE, null, ex);
        }

        int responceCode = responce.getStatusLine().getStatusCode();

        return responceCode;
    }

    private HttpResponse adminAuth(String cookie, HttpClient hClient) {

        HttpResponse response = null;
        try {
            //Creating HTTP Post object
            String url = "https://millennium-test.hcp.millenniumit.com/query";
            //String url = "https://hitachi.hcp1.hdspoc.com/query";
            HttpPost httpPost = new HttpPost(url);
            httpPost.getParams().setParameter(ClientPNames.COOKIE_POLICY, CookiePolicy.BROWSER_COMPATIBILITY);
            //Setting HTTP Post headers for Authentication, Request type, Respond type and Encode type
            httpPost.addHeader("Cookie", cookie);
            httpPost.addHeader("Content-Type", "application/xml");
            httpPost.addHeader("Accept", "application/json");
            //httpPost.addHeader("Content-Encoding", "gzip");
            //httpPost.addHeader("Accept-Encoding", "gzip");

            Map<String, String> obj = new LinkedHashMap<String, String>();
            obj.put("query", "+(namespace:\"data-set1.Millennium-Test\")");
            obj.put("contentProperties", "false");
            obj.put("objectProperties", "shred,retention");
            obj.put("sort", "changeTimeMilliseconds+asc");
            String jsonText = "{\"object\" :" + JSONValue.toJSONString(obj) + "}";
            System.out.println(jsonText);

            XMLSerializer serializer = new XMLSerializer();
            JSON json = JSONSerializer.toJSON(jsonText);
            serializer.setRootName("queryRequest");
            serializer.setTypeHintsEnabled(false);
            String xml = serializer.write(json);

            xml = xml.replaceAll("\\<\\?xml(.+?)\\?\\>", "").trim();
            System.out.println(xml);

            StringEntity stringEntity = new StringEntity(xml, HTTP.UTF_8);
            httpPost.setEntity(stringEntity);
            response = hClient.execute(httpPost);
            System.out.println(response.toString());

            String sJson = EntityUtils.toString(response.getEntity());
            System.out.println(sJson);

            HCP_Logger myLogger = new HCP_Logger();
            myLogger.writeToLog(username, xml, response.toString());

        } catch (IOException ex) {
            Logger.getLogger(SSLAuthenticate.class.getName()).log(Level.SEVERE, null, ex);
        }

        return response;
    }

    private HttpResponse tenantAuth(String cookie, HttpClient hClient) {

        HttpResponse response = null;
        try {
            //Creating HTTP Post object
            String url = "sample url";
            //String url = "sample url";
            HttpPost httpPost = new HttpPost(url);
            httpPost.getParams().setParameter(ClientPNames.COOKIE_POLICY, CookiePolicy.BROWSER_COMPATIBILITY);
            //Setting HTTP Post headers for Authentication, Request type, Respond type and Encode type
            httpPost.addHeader("Cookie", cookie);
            httpPost.addHeader("Content-Type", "application/xml");
            httpPost.addHeader("Accept", "application/json");
            //httpPost.addHeader("Content-Encoding", "gzip");
            //httpPost.addHeader("Accept-Encoding", "gzip");

            Map obj = new LinkedHashMap();
            obj.put("query", "+(namespace:\"sample")");
            obj.put("contentProperties", "false");
            obj.put("objectProperties", "shred,retention");
            obj.put("sort", "changeTimeMilliseconds+asc");
            String jsonText = "{\"object\" :" + JSONValue.toJSONString(obj) + "}";
            //System.out.println(jsonText);

            XMLSerializer serializer = new XMLSerializer();
            JSON json = JSONSerializer.toJSON(jsonText);
            serializer.setRootName("queryRequest");
            serializer.setTypeHintsEnabled(false);
            String xml = serializer.write(json);

            xml = xml.replaceAll("\\<\\?xml(.+?)\\?\\>", "").trim();
            //System.out.println(xml);

            //String xmll = "<queryRequest><object><query>namespace:\"data-set1.Millennium-Test\"</query><objectProperties>shred,retention</objectProperties><sort>changeTimeMilliseconds+asc</sort></object></queryRequest>";
            //System.out.println(xmll); 

            StringEntity stringEntity = new StringEntity(xml, HTTP.UTF_8);
            httpPost.setEntity(stringEntity);

            response = hClient.execute(httpPost);
            //System.out.println(response.toString());

//            String sJson = EntityUtils.toString(response.getEntity());
            //System.out.println(sJson);

//            HCP_Logger myLogger = new HCP_Logger();
//            myLogger.writeToLog(username, xml, response.toString());

        } catch (IOException ex) {
            Logger.getLogger(SSLAuthenticate.class.getName()).log(Level.SEVERE, null, ex);
        }

        return response;
    }
}
