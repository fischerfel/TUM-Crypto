public class LoginScreen extends AppCompatActivity {

...
...

  public LoginScreen() {

    try {
        inStream = this.getApplicationContext().getResources().openRawResource(R.raw.mytruststore);

        KeyStore ks = KeyStore.getInstance("BKS");
        ks.load(inStream, "bks*password".toCharArray());
        inStream.close();

        KeyManagerFactory kmf = KeyManagerFactory.getInstance("X509");
        kmf.init(ks, "bks*password".toCharArray());

        TrustManagerFactory tmf = TrustManagerFactory.getInstance("X509");
        tmf.init(ks);

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(kmf.getKeyManagers(),tmf.getTrustManagers(), null);
        SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
        hurlStack = new HurlStack(null, sslSocketFactory);
    } catch (Exception e){
        Log.d("Exception:",e.toString());
    }

  }

...
...

}
