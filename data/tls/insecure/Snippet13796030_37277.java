    >     Try using TrustManagerManipulator Class:




              try {
                                TrustManagerManipulator.allowAllSSL();
                                try {
                                    SoapObject request = new SoapObject(Data.NAMESPACE,
                                            methodName);

                                    SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                                            SoapEnvelope.VER11);

                                    if (methodName.equals(Data.DOPAYMENT))

                                    {
                                        request.addProperty("firstname", param1);
                                        request.addProperty("lastName", param2);

                envelope.dotNet = true;
                                envelope.setOutputSoapObject(request);
                                envelope.env = "http://schemas.xmlsoap.org/soap/envelope/";
                                TrustManagerManipulator.allowAllSSL();
                                System.setProperty("http.keepAlive", "false");

                                HttpTransportSE androidHttpTransport = new HttpTransportSE(
                                        Data.URL_BASE);
                                androidHttpTransport
                                        .setXmlVersionTag("<?xml version=\"1.0\" encoding=\"utf-8\"?>");

                                try {
                                    androidHttpTransport.call(Data.SOAP_ACTION,
                                            envelope);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Toast.makeText(context, "Network failed!",
                                            Toast.LENGTH_LONG).show();
                                    dismissDialog();
                                }
                                SoapObject resultsRequestSOAP = (SoapObject) envelope.bodyIn;
                                String responseLocal = resultsRequestSOAP.toString();
                                responseXML = (String) resultsRequestSOAP
                                        .getProperty(0);
                                Log.v("responseXML0::", "" + responseXML);

                                Log.v("responseSTR", "" + responseLocal);




///////////////////////////////////

> TrustManagerManipulator Class


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

public class TrustManagerManipulator implements X509TrustManager {


    private static TrustManager[] trustManagers;
    private static final X509Certificate[] acceptedIssuers = new X509Certificate[] {};

    public boolean isClientTrusted(X509Certificate[] chain) {
        return true;
    }

    public boolean isServerTrusted(X509Certificate[] chain) {
        return true;
    }

    public static void allowAllSSL() 
    {

        HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        });
        SSLContext context = null;
        if (trustManagers == null) {
            trustManagers = new TrustManager[] { new TrustManagerManipulator() };
        }
        try {
            context = SSLContext.getInstance("TLS");
            context.init(null, trustManagers, new SecureRandom());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        HttpsURLConnection.setDefaultSSLSocketFactory(context
                .getSocketFactory());
    }

    public void checkClientTrusted(X509Certificate[] chain, String authType)
            throws CertificateException {
    }

    public void checkServerTrusted(X509Certificate[] chain, String authType)
            throws CertificateException {
    }

    public X509Certificate[] getAcceptedIssuers() {
        return acceptedIssuers;
    }
}
