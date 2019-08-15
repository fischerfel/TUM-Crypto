@SuppressLint("NewApi")
public class Connection extends IntentService{

private String tag = "Ciclo eventi";
private String user;
private String pass;

public Connection()
{
    super("Connection");
}

public void onCreate(){
    super.onCreate();
    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
    StrictMode.setThreadPolicy(policy);
}

public void onStart(Intent intent, int startId){

    Log.d(tag, "GetData");
    Bundle extras = intent.getExtras();
    user = (String) extras.get("User");
    pass = (String) extras.get("Password");
    Log.d(tag, user);
    Log.d(tag, pass);
    onHandleIntent(intent);
}

public int onStartCommand(Intent intent, int flags, int startId){
    onHandleIntent(intent);
    return START_NOT_STICKY;
}



@Override
public void onDestroy()
{
    Log.d(tag, "CONNECTION CLOSED");

}

@Override
protected void onHandleIntent(Intent intent) {
    Socket s=null;
    BufferedReader in=null;
    PrintWriter  writer=null;
    try {
        Log.d(tag, "Try to connect");
        s = getConnection("192.168.1.103", 5433);
        Log.d(tag, "Connection done");
        in = new BufferedReader(new InputStreamReader(s.getInputStream()));
        writer = new PrintWriter(s.getOutputStream(), true);
        writer.println(user);
        writer.println(pass);
        Log.d(tag, "I've send the credential");
        String resp = null;
        resp = in.readLine();
        Log.d(tag, "Receive the results");
        if(resp.equals("done")){
            Log.d(tag, "ACCEPT");
            /*Intent i=new Intent(this,SecondActivity.class);
            startActivity(i);*/
            onDestroy();
        }
        else{
            Log.d(tag, "Refused");
        }
    } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }


}

protected Socket getConnection(String ip, int port) throws IOException  {
    try {
        KeyStore trustStore = KeyStore.getInstance("BKS");
        InputStream trustStoreStream = getApplicationContext().getResources().openRawResource(R.raw.server);
        trustStore.load(trustStoreStream, "keypass".toCharArray());

        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init(trustStore);

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, trustManagerFactory.getTrustManagers(), null);
        SSLSocketFactory factory = sslContext.getSocketFactory();
        SSLSocket socket = (SSLSocket) factory.createSocket(ip, port);
        //socket.setEnabledCipherSuites(getCipherSuitesWhiteList(socket.getEnabledCipherSuites()));
        return socket;
    } catch (GeneralSecurityException e) {
        Log.e(this.getClass().toString(), "Exception while creating context: ", e);
        throw new IOException("Could not connect to SSL Server", e);
    }
}


    public static String[] getCipherSuitesWhiteList(String[] cipherSuites) {
        List<String> whiteList = new ArrayList<>();
        List<String> rejected = new ArrayList<>();
        for (String suite : cipherSuites) {
            String s = suite.toLowerCase();
            if (s.contains("anon") || //reject no anonymous
                    s.contains("export") || //reject no export
                    s.contains("null") || //reject no encryption
                    s.contains("md5") || //reject MD5 (weaknesses)
                    s.contains("_des") || //reject DES (key size too small)
                    s.contains("krb5") || //reject Kerberos: unlikely to be used
                    s.contains("ssl") || //reject ssl (only tls)
                    s.contains("empty")) {    //not sure what this one is
                rejected.add(suite);
            } else {
                whiteList.add(suite);
            }
        }
        return whiteList.toArray(new String[whiteList.size()]);
    }}
