package com.xxx.xxx.x.xx;

import java.io.IOException;
import java.net.CookieManager;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import net.customware.gwt.dispatch.client.standard.StandardDispatchService;
import net.customware.gwt.dispatch.shared.DispatchException;

import com.gdevelop.gwt.syncrpc.LoginUtils;
import com.gdevelop.gwt.syncrpc.ProxySettings;
import com.gdevelop.gwt.syncrpc.SyncProxy;

public class TestRemoteExecuteAction {

            static Logger logger = Logger.getLogger(TestRemoteExecuteAction.class.getName());
              public static void main(String[] arg) {

                  SyncProxy.setLoggingLevel(Level.ALL);

                try {

                      // Create a trust manager that does not validate certificate chains
                    TrustManager[] trustAllCerts = new TrustManager[] {new X509TrustManager() {
                            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                                return null;
                            }
                            public void checkClientTrusted(X509Certificate[] certs, String authType) {
                            }
                            public void checkServerTrusted(X509Certificate[] certs, String authType) {
                            }
                        }
                    };

                    // Install the all-trusting trust manager
                    SSLContext sc = SSLContext.getInstance("SSL");
                    sc.init(null, trustAllCerts, new java.security.SecureRandom());
                    HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

                    // Create all-trusting host name verifier
                    HostnameVerifier allHostsValid = new HostnameVerifier() {
                        public boolean verify(String hostname, SSLSession session) {
                            return true;
                        }
                    };

                    // Install the all-trusting host verifier
                    HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);

                    CookieManager cookiemanager = LoginUtils.loginFormBasedJ2EE("https:XXXXX", "XXXX", "XXXX");

                    SyncProxy.setBaseURL("https://XXXXXX");

                    StandardDispatchService rpcService =  SyncProxy.createProxy(StandardDispatchService.class,
                            new ProxySettings().setCookieManager(cookiemanager));

                    System.out.println(cookiemanager.getCookieStore().getCookies().get(0));
                    String JSESSIONID = cookiemanager.getCookieStore().getCookies().get(0).getValue();

                    rpcService.execute(new XXXXXAction("XXX"));



                } catch (NoSuchAlgorithmException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (KeyManagementException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (URISyntaxException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (DispatchException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } 

              }
}
