 package ca.company.project;

    import org.apache.cordova.CordovaActivity;

    import android.app.Activity;
    import android.app.AlertDialog;
    import android.content.Context;
    import android.content.DialogInterface;
    import android.content.DialogInterface.OnClickListener;
    import android.content.pm.ActivityInfo;
    import android.content.res.Configuration;
    import android.os.Bundle;
    import android.util.DisplayMetrics;
    import android.view.WindowManager.LayoutParams;
    import android.content.pm.PackageManager;
    import com.adobe.mobile.*;

    import com.worklight.androidgap.api.WL;
    import com.worklight.androidgap.api.WLInitWebFrameworkListener;
    import com.worklight.androidgap.api.WLInitWebFrameworkResult;
    import com.worklight.wlclient.api.WLClient;


    import javax.net.ssl.SSLSocketFactory;

    import java.security.KeyManagementException;
    import java.security.NoSuchAlgorithmException;
    import java.security.SecureRandom;
    import java.security.cert.CertificateException;
    import java.security.cert.X509Certificate;

    import javax.net.ssl.HostnameVerifier;
    import javax.net.ssl.HttpsURLConnection;
    import javax.net.ssl.SSLContext;
    import javax.net.ssl.SSLSession;
    import javax.net.ssl.TrustManager;
    import javax.net.ssl.X509TrustManager;


    import android.os.*;

    public class MobileBanking extends CordovaActivity implements WLInitWebFrameworkListener {

        @Override
        public void onCreate(Bundle savedInstanceState){
            super.onCreate(savedInstanceState);


            /*getWindow().setFlags(LayoutParams.FLAG_SECURE,
                    LayoutParams.FLAG_SECURE);*/

            WL.createInstance(this);


    try{
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, null, null);
        SSLSocketFactory noSSLv3Factory = null;
        if (android.os.Build.VERSION.SDK_INT <= android.os.Build.VERSION_CODES.KITKAT) {
            noSSLv3Factory = new TLSSocketFactory();
        } else {
            noSSLv3Factory = sslContext.getSocketFactory();
        }
        HttpsURLConnection.setDefaultSSLSocketFactory(noSSLv3Factory);

    }  catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
    } catch (KeyManagementException e) {
        e.printStackTrace();
    }




            WL.getInstance().showSplashScreen(this);

            WL.getInstance().initializeWebFramework(getApplicationContext(), this);

            if(isTabletDevice(this)){
                //Tablet
    //          System.out.println("isTablet oncreate");
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
            } else {
                //Phone
                 setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }
            // Allow the SDK access to the application context
            Config.setContext(this.getApplicationContext());
        }
