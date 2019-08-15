import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.SocketAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.List;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import com.btr.proxy.search.ProxySearch;
import com.btr.proxy.search.ProxySearch.Strategy;
import com.btr.proxy.util.PlatformUtil;
import com.btr.proxy.util.PlatformUtil.Platform;
import com.sun.deploy.net.proxy.BrowserProxyInfo;
import com.sun.deploy.net.proxy.DummyAutoProxyHandler;
import com.sun.deploy.net.proxy.ProxyConfigException;
import com.sun.deploy.net.proxy.ProxyInfo;
import com.sun.deploy.net.proxy.ProxyType;

public class ProxyPacManager {
public static void main(String args[]) throws Exception{
    getProxy();
}

public static Proxy getProxy(){
    String almProtocol = Constants.getPropereties().getProperty("dashboard.alm.protocol");
    String almHost = Constants.getPropereties().getProperty("dashboard.alm.host");
    String almPort = Constants.getPropereties().getProperty("dashboard.alm.port");
    String urlStr = almProtocol+almHost+":"+almPort;
    Proxy proxy = null;
    List<Proxy> proxyList = null;
    String successMsg = "Proxy not found.";
    try{
        System.out.println("Trying to connect through Proxy Vole plugin.");
        proxyList = getSSLCertificateAutoProxy(urlStr);
        proxy = getProxyTested(proxyList, urlStr);
        successMsg="Successfully connected through Proxy Vole plugin.";
    } catch(Exception ex){
        System.out.println("Proxy Vole plugin didn't work."+ex.getMessage());
        try{
            System.out.println("Trying to connect through java.net.useSystemProxies Proxy.");
            proxyList = getSSLCertificateSysProxy(urlStr);
            proxy = getProxyTested(proxyList, urlStr);
            successMsg="Successfully connected through java.net.useSystemProxies Proxy.";
        } catch(Exception ex1){
            System.out.println("java.net.useSystemProxies didn't work."+ex1.getMessage());
            try{
                /*System.out.println("Trying to connect through PAC Proxy.");
                proxyList = getSSLCertificatePACProxy(urlStr);
                proxy = getProxyTested(proxyList, urlStr);
                successMsg="Successfully connected through PAC Proxy.";*/
                throw new Exception("Bypass PAC Proxy for testing.");
            }catch(Exception ex2){
                System.out.println("PAC Proxy read didn't work."+ex2.getMessage());
                try{
                    System.out.println("Trying to connect through Constant Proxy.");
                    proxyList = getSSLCertificateConstantProxy();
                    proxy = getProxyTested(proxyList, urlStr);
                    successMsg="Successfully connected through Constant Proxy.";
                }catch(Exception ex3){
                    System.out.println("Constant Proxy read didn't work."+ex3.getMessage());
                    proxyList = new ArrayList<Proxy>();
                    proxyList.add(Proxy.NO_PROXY);
                    proxy = getProxyTested(proxyList, urlStr);
                    successMsg = "Connected with NO_PROXY";
                }
            }
        }
    }
    System.out.println(successMsg);
    return proxy;
}

private static Proxy getProxyTested(List<Proxy> proxyList, String urlStr){
    if (proxyList != null && !proxyList.isEmpty()) { 
         for (Proxy proxy : proxyList) { 
             SocketAddress address = proxy.address(); 
             if (address instanceof InetSocketAddress) {
                 System.out.println("Trying to connect through proxy: "+((InetSocketAddress) address).getHostName()+":"+((InetSocketAddress) address).getPort());
                try {
                    URLConnection connection = new URL(urlStr).openConnection(proxy);
                    connection.connect();               
                    System.out.println("Connected through proxy: "+((InetSocketAddress) address).getHostName()+":"+((InetSocketAddress) address).getPort());
                    return proxy; 
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }                                  
             } 
         } 
    }
    return null;
}

private static List<Proxy> getSSLCertificateConstantProxy() throws Exception{
    setCertificate();
    List<Proxy> proxyList = new ArrayList<Proxy>();
    String proxyHost = Constants.getPropereties().getProperty("dashboard.alm.proxy.host");
    InetAddress hostIp = InetAddress.getByName(proxyHost);
    int proxyPort = Integer.parseInt(Constants.getPropereties().getProperty("dashboard.alm.proxy.port"));

    //Create your proxy and setup authentication for it.
    Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(hostIp.getHostAddress(), proxyPort));                        
    //Setup authentication for your proxy.
    /*Authenticator.setDefault(new Authenticator() {

          @Override
          protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication("<user>", "<password>".toCharArray());
          }
    });*/

    proxyList.add(proxy);
    return proxyList; 
}

private static void setCertificate() throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException, KeyManagementException{
    //First, load the key store file
    String jksFile = Constants.getPropereties().getProperty("dashboard.alm.certificate");
    InputStream trustStream = new FileInputStream(jksFile); 
    String jksPass = Constants.getPropereties().getProperty("dashboard.alm.certificate.pass");
    char[] trustPassword = jksPass.toCharArray();

    //Initialize a KeyStore
    KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
    trustStore.load(trustStream, trustPassword);

    //Initialize TrustManager objects.
    TrustManagerFactory trustFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
    trustFactory.init(trustStore);
    TrustManager[] trustManagers = trustFactory.getTrustManagers();

    //Create a new SSLContext, load the TrustManager objects into it and set it as default.
    SSLContext sslContext = SSLContext.getInstance("SSL");
    sslContext.init(null, trustManagers, null);
    SSLContext.setDefault(sslContext);  
}
private static ProxyInfo[] getProxyInfo(String urlStr) throws ProxyConfigException, MalformedURLException{      
    String proxypac = Constants.getPropereties().getProperty("dashboard.alm.proxy.pac");        
    BrowserProxyInfo b = new BrowserProxyInfo();
    /*WDefaultBrowserProxyConfig wd = new WDefaultBrowserProxyConfig();
    BrowserProxyInfo b = wd.getBrowserProxyInfo();        */
    b.setType(ProxyType.AUTO);
    b.setAutoConfigURL(proxypac);       
    DummyAutoProxyHandler handler = new DummyAutoProxyHandler();
    handler.init(b);

    URL url = new URL(urlStr);
    ProxyInfo[] ps = handler.getProxyInfo(url);     

    return ps;
}

public static List<Proxy> getSSLCertificateAutoProxy(String urlStr) throws Exception{           
    setCertificate();
    /*ProxySearch proxySearch = ProxySearch.getDefaultProxySearch();*/
    ProxySearch proxySearch = new ProxySearch();
    proxySearch.setPacCacheSettings(32, 1000*60*5);
    if (PlatformUtil.getCurrentPlattform() == Platform.WIN) { 
        proxySearch.addStrategy(Strategy.IE); 
        proxySearch.addStrategy(Strategy.FIREFOX); 
        proxySearch.addStrategy(Strategy.JAVA); 
    } else if (PlatformUtil.getCurrentPlattform() == Platform.LINUX) { 
        proxySearch.addStrategy(Strategy.GNOME); 
        proxySearch.addStrategy(Strategy.KDE); 
        proxySearch.addStrategy(Strategy.FIREFOX); 
    } else { 
        proxySearch.addStrategy(Strategy.OS_DEFAULT); 
    }       


    ProxySelector proxySelector = proxySearch.getProxySelector();
    /*BufferedProxySelector cachedSelector = new BufferedProxySelector(32, 1000*60*5, proxySelector);*/


    ProxySelector.setDefault(proxySelector); 
    //ProxySelector.setDefault(cachedSelector);
    URI home = URI.create(urlStr);  
    //List<Proxy> proxyList = cachedSelector.select(home); 
    List<Proxy> proxyList = proxySelector.select(home);
    return proxyList;
}

public static List<Proxy> getSSLCertificatePACProxy(String urlStr) throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException, KeyManagementException, ProxyConfigException{
    List<Proxy> proxyList = new ArrayList<Proxy>();
    setCertificate();       
    ProxyInfo[] ps = getProxyInfo(urlStr);      
    for(ProxyInfo p: ps){

        String proxyHost = p.getProxy();
        int proxyPort = p.getPort(); 

        //Create your proxy and setup authentication for it.
        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHost, proxyPort));                      
        //Setup authentication for your proxy.
        /*Authenticator.setDefault(new Authenticator() {

              @Override
              protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("<user>", "<password>".toCharArray());
              }
        });*/
        proxyList.add(proxy);

    }
    return proxyList;

}


public static List<Proxy> getSSLCertificateSysProxy(String urlStr) throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException, KeyManagementException, ProxyConfigException, URISyntaxException{
    setCertificate();       
    System.setProperty("java.net.useSystemProxies","true");
    List<Proxy> proxyList = ProxySelector.getDefault().select(new URI(urlStr));

    return proxyList;

}

}
