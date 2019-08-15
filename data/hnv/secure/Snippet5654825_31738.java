import android.net.http.AndroidHttpClient;
import android.app.Application;
import android.util.Log;
import idatt.mobile.android.providers.DBLog;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;

import java.io.InputStream;
import java.security.KeyStore;

/*
To generate PKS:
1. Created cert in IIS7 and then exported as pfx. Follow instruction on SelfSSL: http://www.robbagby.com/iis/self-signed-certificates-on-iis-7-the-easy-way-and-the-most-effective-way/
1a. Download tool: http://cid-3c8d41bb553e84f5.skydrive.live.com/browse.aspx/SelfSSL
1b. Run: SelfSSL /N:CN=mydomainname /V:1000 /S:1 /P:8081
 I use port 8081 on my server
1c. Export from IIS manager to cert.pfx
2. Run command line in SSL to convert file into X.509:
openssl pkcs12 -in C:\cert.pfx -out C:\cert.cer -nodes
3. Edit file and delete all except -----BEGIN.... END CERTIFICATE----- IMPORTANT! It was working when I got proper (5) amount of dashes and put tags and data on separate lines
4. use keytool. C:\Java\JDK\bcprov.jar was downloaded separately
 C:\Users\Ivan>keytool -import -v -trustcacerts -alias key_alias -file C:\cert.cer -keystore C:\mystore.bks -storetype BKS -provider org.bouncycastle.jce.provider.BouncyCastleProvider -providerpath C:\Java\JDK\bcprov.jar -storepass 123456

*/

public class MyApplication extends Application
{
    private static final String LOG_TAG = "MyApplication";
    private AndroidHttpClient androidHttpClient;

    @Override
    public void onCreate()
    {
        super.onCreate();
        androidHttpClient = createAndroidHttpClient();
    }

    @Override
    public void onLowMemory()
    {
        super.onLowMemory();
        shutdownAndroidHttpClient();
    }

    @Override
    public void onTerminate()
    {
        super.onTerminate();
        shutdownAndroidHttpClient();
    }


    private AndroidHttpClient createAndroidHttpClient()
    {
        Log.d(LOG_TAG,"createAndroidHttpClient");

        AndroidHttpClient client = AndroidHttpClient.newInstance("Android");

        //This is optional call to inject custom BKS that was created from self-signed certificate
        client = addCustomCertificate(client);

        return client;
    }

    public AndroidHttpClient getAndroidHttpClient()
    {
        return androidHttpClient;
    }

    private void shutdownAndroidHttpClient()
    {
        if(androidHttpClient!=null && androidHttpClient.getConnectionManager()!=null)
        {
            androidHttpClient.getConnectionManager().shutdown();
        }
    }

    private AndroidHttpClient addCustomCertificate(AndroidHttpClient client)
    {
        SSLSocketFactory sf = SSLSocketFactory.getSocketFactory();

        try
        {
            InputStream in = getResources().openRawResource(R.raw.home_server);

            KeyStore trustStore = KeyStore.getInstance("BKS");

            trustStore.load(in, "123456".toCharArray());
            in.close();

            sf = new SSLSocketFactory(trustStore);
            sf.setHostnameVerifier(SSLSocketFactory.STRICT_HOSTNAME_VERIFIER);
        }
        catch (Exception t)
        {
            DBLog.InsertError(this, t);
        }

        //Lets register our custom factory here
        client.getConnectionManager().getSchemeRegistry().register(new Scheme("https", sf, 443));

        return client;
    }
}
