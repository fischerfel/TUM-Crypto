package com.maypackage.services;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.MarshalBase64;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpsTransportSE;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.edusunsoft.mypackage.R;
import com.mypackage.Model.Property_vo;
import com.maypackage.util.Utility;

public class SoapParser {
String c_url = "";
private ArrayList<Property_vo> c_args;
public static String c_method = "";

String namespace = "http://tempuri.org/";
private Dialog mPoweroffDialog;

Context context;
public SoapParser(ArrayList<Property_vo> args, String method, String url) {
    c_method = method;
    c_url = url;
    c_args = args;
    Log.v("url", url);
    Log.v("methodname", method);
    //Log.v("param", args.toString());
}

public SoapParser() {
}

public String buildData(Context context) {
    SoapObject request = new SoapObject(namespace,  c_method.toString());
    for (int i = 0; i < c_args.size(); i++) {

        request.addProperty(c_args.get(i).getName(), c_args.get(i)
                .getValue());
        Log.v("param",c_args.get(i).getName()+"="+ c_args.get(i)
                .getValue());
    }
    String res = getResponse(request,context);
    System.out.println(res);
    return res;

}
public String buildData(byte[] byteArray ,Context context) {
    this.context=context;
    SoapObject request = new SoapObject(  namespace,  c_method.toString() );

    for (int i = 0; i < c_args.size(); i++) {
        request.addProperty(c_args.get(i).getName(), c_args.get(i)
                .getValue());
    }

    request.addProperty("SignatureFileData", byteArray);
    String res = getResponse(request,this.context);
    System.out.println(res);
    return res;
}

public String getResponse(SoapObject request, final Context context) {
    String responce = "";
    try {
        if (Utility.isNetworkAvailable(context)) {
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);

            String surl = c_url+"?op=" + c_method.toString();
            new MarshalBase64().register(envelope);

            allowAllSSL();
            HttpsTransportSE androidHttpTransport = new HttpsTransportSE(c_url.split("/")[2].toString(),443, "/Services/"+ c_url.split("/")[c_url.split("/").length-1].toString() +   "?op=" + c_method.toString(), 30000);

            androidHttpTransport.call(namespace + c_method.toString(), envelope);
            SoapPrimitive  result = (SoapPrimitive) envelope.getResponse();
            responce = result.toString();
        }
        else
        {
            ((Activity)context).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    mPoweroffDialog = new Dialog(context);
                    mPoweroffDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    mPoweroffDialog.getWindow().setFlags(
                            WindowManager.LayoutParams.FLAG_FULLSCREEN,
                            WindowManager.LayoutParams.FLAG_FULLSCREEN);
                    mPoweroffDialog.getWindow().setBackgroundDrawableResource(
                            android.R.color.transparent);
                    mPoweroffDialog.setContentView(R.layout.dialog_logout_password);
                    mPoweroffDialog.setCancelable(false);
                    mPoweroffDialog.show();

                    LinearLayout ll_submit = (LinearLayout) mPoweroffDialog
                            .findViewById(R.id.ll_submit);

                    TextView tv_say_something = (TextView)mPoweroffDialog.findViewById(R.id.tv_say_something);
                    tv_say_something.setText(context.getResources().getString(R.string.PleaseCheckyourinternetconnection));

                    TextView tv_header = (TextView)mPoweroffDialog.findViewById(R.id.tv_header);
                    tv_header.setText(context.getResources().getString(R.string.NoNetwork));

                    ll_submit.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // TODO Auto-generated method stub
                            mPoweroffDialog.dismiss();
                        }
                    });

                    ImageView img_close = (ImageView) mPoweroffDialog
                            .findViewById(R.id.img_close);
                    img_close.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // TODO Auto-generated method stub
                            mPoweroffDialog.dismiss();
                        }
                    });
                }
            });
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return responce;
}

private static TrustManager[] trustManagers;

public static class _FakeX509TrustManager implements
javax.net.ssl.X509TrustManager {
    private static final X509Certificate[] _AcceptedIssuers = new X509Certificate[] {};

    public void checkClientTrusted(X509Certificate[] arg0, String arg1)
            throws CertificateException {
    }

    public void checkServerTrusted(X509Certificate[] arg0, String arg1)
            throws CertificateException {
    }

    public boolean isClientTrusted(X509Certificate[] chain) {
        return (true);
    }

    public boolean isServerTrusted(X509Certificate[] chain) {
        return (true);
    }

    public X509Certificate[] getAcceptedIssuers() {
        return (_AcceptedIssuers);
    }
}

public static void allowAllSSL() {

    javax.net.ssl.HttpsURLConnection
    .setDefaultHostnameVerifier(new HostnameVerifier() {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    });

    javax.net.ssl.SSLContext context = null;

    if (trustManagers == null) {
        trustManagers = new javax.net.ssl.TrustManager[] { new _FakeX509TrustManager() };
    }
    try {
        context = javax.net.ssl.SSLContext.getInstance("TLS");
        context.init(null, trustManagers, new SecureRandom());
    } catch (NoSuchAlgorithmException e) {
        Log.e("allowAllSSL", e.toString());
    } catch (KeyManagementException e) {
        Log.e("allowAllSSL", e.toString());
    }
    javax.net.ssl.HttpsURLConnection.setDefaultSSLSocketFactory(context
            .getSocketFactory());
}
}
