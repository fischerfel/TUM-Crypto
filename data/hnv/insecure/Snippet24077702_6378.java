public class MainActivity extends ActionBarActivity {

/* Called when the activity is first created. */

EditText iptxt;
Button sendbtn;
TextView logtxt;
EditText porttxt;
EditText cmdtxt;

// port to use
private String ip_address;
private int port = 1992;
private String cmdstr = null;
private SSLSocket socket = null;
private BufferedWriter out = null;
private BufferedReader in = null;
private final String TAG = "TAG";
private String keystorepass = "mypasswordisCOOL!";
private String keypassword = "mypasswordRULEZ@";

@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    iptxt = (EditText) findViewById(R.id.iptxt);
    porttxt = (EditText) findViewById(R.id.porttxt);
    cmdtxt = (EditText) findViewById(R.id.cmdtxt);

    logtxt = (TextView) findViewById(R.id.logtxt);

    sendbtn = (Button) findViewById(R.id.sendbtn);

    sendbtn.setClickable(true);
    sendbtn.setOnClickListener(new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            if (iptxt.getText().toString().equals(null) || porttxt.getText().toString().equals(null)) {
                Toast.makeText(v.getContext(), "Please enter an IP address or Port number", Toast.LENGTH_LONG).show();
            } else {
               cmdstr = cmdtxt.getText().toString();
               if (cmdstr == null) {
                   Toast.makeText(v.getContext(), "No command was entered", Toast.LENGTH_LONG).show();
               }

                Log.i(TAG, "makes it to here");

                port = Integer.parseInt(porttxt.getText().toString());
                ip_address = iptxt.getText().toString();

                try {

                    KeyStore ks = KeyStore.getInstance("BKS");
                    InputStream key = v.getResources().openRawResource(R.raw.mykeyandroid);
                    ks.load(key,keystorepass.toCharArray());
                    SSLSocketFactory socketFactory = new SSLSocketFactory(ks);
                    socketFactory.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
                    socket = (SSLSocket)
                            socketFactory.createSocket(new Socket(ip_address,port), ip_address, port, false);
                    socket.startHandshake();


                    out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                    in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    chat(cmdstr);

                    key.close();
                    in.close();
                    out.close();
                    socket.close();

                } catch (UnknownHostException e) {
                    Toast.makeText(v.getContext(), "Unknown host", Toast.LENGTH_SHORT).show();
                    Log.i(TAG, "Unknown host");
                    //System.exit(1);
                } catch (IOException e) {
                    Toast.makeText(v.getContext(), "No I/O", Toast.LENGTH_SHORT).show();
                    Log.i(TAG, "No I/O");
                    e.printStackTrace();
                    //System.exit(1);
                } catch (KeyStoreException e) {
                    Toast.makeText(v.getContext(), "Keystore ks error", Toast.LENGTH_SHORT).show();
                    Log.i(TAG, "Keystore ks error");
                    //System.exit(-1);
                } catch (NoSuchAlgorithmException e) {
                    Toast.makeText(v.getContext(), "No such algorithm for ks.load", Toast.LENGTH_SHORT).show();
                    Log.i(TAG, "No such algorithm for ks.load");
                    e.printStackTrace();
                    //System.exit(-1);
                } catch (CertificateException e) {
                    Toast.makeText(v.getContext(), "certificate missing", Toast.LENGTH_SHORT).show();
                    Log.i(TAG, "certificate missing");
                    e.printStackTrace();
                    //System.exit(-1);
                } catch (KeyManagementException e) {
                    Toast.makeText(v.getContext(), "KeyManagementException", Toast.LENGTH_SHORT).show();
                    Log.i(TAG, "key management exception");
                    e.printStackTrace();
                    //System.exit(-1);
                } catch (UnrecoverableKeyException e) {
                    Toast.makeText(v.getContext(), "UnrecoverableKeyException", Toast.LENGTH_SHORT).show();
                    Log.i(TAG,"unrecoverableKeyException");
                    e.printStackTrace();
                }


            }

        }
    });

}


public void chat(String temp){
    String message = temp;
    String line = "";
    // send id of the device to match with the image
    try {
        out.write(message);
        out.flush();
    } catch (IOException e2) {
        Log.i(TAG,"Read failed");
        System.exit(1);
    }
    // receive a ready command from the server
    try {
        line = in.readLine();
        logtxt.setText("SERVER SAID: " + line);

    } catch (IOException e1) {
        Log.i(TAG,"Read failed");
        System.exit(1);
    }
}
}
