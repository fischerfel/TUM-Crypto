package data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManagerFactory;

import org.xmlpull.v1.XmlPullParserException;
import framework.QoowayActivity;
import android.os.AsyncTask;
import android.util.Log;

public class HttpRequestTask extends AsyncTask<String, Void, String> {

private QoowayActivity activity;
private Boolean Xml = false;
private EnumData.request_mode rm;
private status stat= status.notLoggedIn;
private String loginToken = "QoowayMember";
private DataStorageManager dataStorageManager = DataStorageManager.getSingletonInstance();
private String dataType;

public HttpRequestTask(QoowayActivity activity, EnumData.request_mode rm ) {
    this.activity = activity;
    this.rm = rm;
    this.dataType =dataStorageManager.getTempKey();

}


//
public HttpRequestTask(QoowayActivity activity, Boolean xml) {

    this.activity = activity;
    this.Xml = xml;

}

  @Override
    protected void onPreExecute() {
      super.onPreExecute();

        DataStorageManager.getSingletonInstance().incrementAsyncTask();
      /*
      if(!DataStorageManager.getSingletonInstance().checkInActivity) {
          DataStorageManager.getSingletonInstance().incrementAsyncTask();
      }
      */
    }

@SuppressWarnings("finally")
@Override
protected String doInBackground(String... urls) {

    // params comes from the execute() call: params[0] is the url.
    String result = null;

    try {

        result = loadResult(urls[0]);
    } catch (Exception e) {
        e.printStackTrace();
    }
    return result;

}

// onPostExecute displays the results of the AsyncTask.
@Override
protected void onPostExecute(String result) {
    DataStorageManager dataStorageManager = DataStorageManager.getSingletonInstance();
    dataStorageManager.decrementAsyncTask();
    /*
    if(!dataStorageManager.checkInActivity) {
        dataStorageManager.decrementAsyncTask();
    }
    */
    if(this.rm == EnumData.request_mode.LogIN )
    {
        if (this.stat== status.loggedIn) {
            dataStorageManager.loggedIn = true;
        } else {
            dataStorageManager.loggedIn = false;
        }
    }
      super.onPostExecute(result);

    //activity.cancelProgressDialog();
    //REMOVE

}


@SuppressWarnings("unchecked")
private String loadResult(String urlString) throws XmlPullParserException,
        IOException {
    InputStream stream = null;
    String result = null;
    try {
        stream = downloadUrl(urlString);
        result = inputStreamToString(stream);
        // Makes sure that the InputStream is closed after the app is
        // finished using it.
    } catch (Exception e1) {
        // TODO Auto-generated catch block
        e1.printStackTrace();
    }
    if (stream != null) {
        stream.close();
    }
    return result;

}

private InputStream downloadUrl(String urlString) throws IOException,
        KeyManagementException, NoSuchAlgorithmException,
        KeyStoreException, CertificateException {

    DataStorageManager dataStorageManager = DataStorageManager.getSingletonInstance();
    String userName = dataStorageManager.userName;
    String passWord = dataStorageManager.password;
    HttpURLConnection conn = null;
    if (urlString.startsWith("https")) {
        try {
            conn = (HttpURLConnection) httpsConnection(urlString);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    } else {
        URL url = new URL(urlString);
        conn = (HttpURLConnection) url.openConnection();
    }
    conn.setReadTimeout(10000 /* milliseconds */);
    conn.setConnectTimeout(15000 /* milliseconds */);
    conn.setRequestMethod("GET");
    conn.setDoInput(true);
    if (Xml) {
        conn.setRequestProperty("Accept", "text/xml");
    }

    if(dataStorageManager.loggedIn) {
        conn.setRequestProperty(
                "Authorization", dataStorageManager.loginToken);    
    } else {
    conn.setRequestProperty(
            "Authorization", loginToken);
    }   
        /*  "Basic "
                    +   Base64.encodeToString(
                            ((userName + ":" + passWord).getBytes()),
                            Base64.NO_WRAP)); */

    // Starts the query
    conn.connect();

    int code = conn.getResponseCode();
    String message = conn.getResponseMessage();
    dataStorageManager.lastCode = code;
    if(!message.isEmpty()){
        dataStorageManager.lastMessage = message;
    }

    if(code < 200 || code > 299) {
        activity.showDialog();
    }

    Log.i("WebApi", ""+code);
    //activity.showDialog();


    String temp =conn.getResponseMessage();
    if(conn.getResponseMessage().equals("OK") && this.rm == EnumData.request_mode.LogIN)
    {
        this.stat = status.loggedIn;
    }
    return conn.getInputStream();
}

private HttpURLConnection httpsConnection(String urlString)
        throws NoSuchAlgorithmException, KeyManagementException,
        KeyStoreException, IOException, CertificateException {
    HostnameVerifier hostnameVerifier = new HostnameVerifier() {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            HostnameVerifier hv = HttpsURLConnection
                    .getDefaultHostnameVerifier();
            return hv.verify(HttpRequestTask.this.getHostName(), session);
        }
    };
    // Get an instance of the Bouncy Castle KeyStore format
    KeyStore trusted = KeyStore.getInstance("BKS");
    // Get the raw resource, which contains the keystore with
    // your trusted certificates (root and any intermediate certs)
    InputStream in = activity.getApplicationContext().getResources()
            .openRawResource(this.getKeyStore());  // took away 2
    try {
        // Initialize the keystore with the provided trusted
        // certificates
        // Also provide the password of the keystore
        trusted.load(in, this.getKeyStorePassWord().toCharArray());
    } finally {
        in.close();
    }
    String algorithm = TrustManagerFactory.getDefaultAlgorithm();
    TrustManagerFactory tmf = TrustManagerFactory.getInstance(algorithm);
    tmf.init(trusted);

    SSLContext context = SSLContext.getInstance("TLS");
    context.init(null, tmf.getTrustManagers(), null);

    URL url = new URL(urlString);
    HttpsURLConnection urlConnection = (HttpsURLConnection) url
            .openConnection();
    urlConnection.setSSLSocketFactory(context.getSocketFactory());
    urlConnection.setHostnameVerifier(hostnameVerifier);
    return urlConnection;
}

private String inputStreamToString(InputStream is) throws IOException {
    String line = "";
    StringBuilder total = new StringBuilder();

    // Wrap a BufferedReader around the InputStream
    BufferedReader rd = new BufferedReader(new InputStreamReader(is));

    // Read response until the end
    while ((line = rd.readLine()) != null) {
        total.append(line);
    }

    // Return full string
    return total.toString();
}

private enum status
{
    loggedIn , notLoggedIn
}

 private int getKeyStore()
 {
     return DataStorageManager.getSingletonInstance().getApiKeyStore();
 }

 private String getHostName()
 {
     return DataStorageManager.getSingletonInstance().getApiHostNameVerfier();
 }

 private String getKeyStorePassWord()
 {
     return DataStorageManager.getSingletonInstance().getKeyStorePassword();
 }
