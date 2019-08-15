package infra;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.*;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

public class Main {

    private static String companyName = "lolilol";

    private static String baseUrl = "https://" + companyName + ".inspirecloud.net";

    private static String loginApi =  "/api/publish/Users/Login";

    private static String loginUrl =  baseUrl + loginApi;

    private static String fetchBatchesApi = "/api/query/Messenger/ListBatchesQueryByUploadTime";

    private static String fetchBatchesUrl = baseUrl + fetchBatchesApi;

    private static String detailBatcheApi = "/api/query/Messenger/ReportQuery";

    private static String detailBatcheUrl = baseUrl + detailBatcheApi;

    private static Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("proxy.local.fr", 8080));

    private static int nbEssais = 4;

    private static String header = "nbClic;Email;Id;Name;Customer;CompanyName;LastUploadTime;Type;MessageType;EmailId;ToEmailAdr;SendindState;Delivered;DeliveryError;FirstView;LastView;ViewCount;Unsubscribe;ServiceUsed;CustomerId;UrlLandingPage;GMC1;GMC2;GMC3;GMC4;GMC5;\n";

     private static Logger logger = Logger.getLogger(Main.class);

    public static void main(String[] args) {


        logger.info("Début des logs");

        HttpsURLConnection loginCon = null;
        HttpsURLConnection fetchBatchCon = null;
        HttpsURLConnection detailBatchCon;

        FileOutputStream fos = null;
        Scanner ficIn = null;

        int retry;

        String email;
        String password;
        String type;
        String from;
        String to;


        String cookie = null;
        String lineIn;
        String[] params;
        ArrayList<String> messagesMail = new ArrayList<String>();


        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[] {
                new X509TrustManager() {
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return new X509Certificate[0];
                    }
                    public void checkClientTrusted(
                            java.security.cert.X509Certificate[] certs, String authType) {
                    }
                    public void checkServerTrusted(
                            java.security.cert.X509Certificate[] certs, String authType) {
                    }
                }
        };

        try
        {
            SSLContext sc = SSLContext.getInstance("SSL");

            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (GeneralSecurityException e) {
            logger.error("Problème sécurité");
        }
