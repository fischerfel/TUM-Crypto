    Button send;
    EditText textSend;
    private String ip_address = "192.168.10.103";
    private int port = 5000;
    private SSLSocket socket = null;
    private BufferedWriter out = null;
    private BufferedReader in = null;
    private final String TAG = "TAG";
    private char keystorepass[] = "....".toCharArray();
    private char keypassword[] = "....".toCharArray();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_screen);
        send = (Button) findViewById(R.id.send);
        textSend = (EditText) findViewById(R.id.textsend);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String send = textSend.getText().toString();
                if(send.isEmpty()){
                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(sendScreen.this);
                    dialogBuilder.setMessage("Enter Text!");
                    dialogBuilder.setTitle("No TEXT");
                    dialogBuilder.setPositiveButton("OK...", null);
                    dialogBuilder.show();
                }else{
                    Log.i(TAG,"makes it to here");
                    try{

                        KeyStore ks = KeyStore.getInstance("BKS");
                        InputStream keyin = v.getResources().openRawResource(R.raw.androidKey);
                        ks.load(keyin,keystorepass);
                        SSLSocketFactory socketFactory = new SSLSocketFactory(ks);
                        socketFactory.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

                        socket = (SSLSocket)
                                socketFactory.createSocket(new Socket(ip_address,port), ip_address, port, false);
                        socket.startHandshake();

                        printServerCertificate(socket);
                        printSocketInfo(socket);

                        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        chat(send);
                    } catch (UnknownHostException e) {
                        Toast.makeText(v.getContext(), "Unknown host", Toast.LENGTH_SHORT).show();
                        Log.i(TAG,"Unknown host");
                        //System.exit(1);
                    } catch  (IOException e) {
                        Toast.makeText(v.getContext(), "No I/O", Toast.LENGTH_SHORT).show();
                        Log.i(TAG,"No I/O");
                        e.printStackTrace();
                        //System.exit(1);
                    } catch (KeyStoreException e) {
                        Toast.makeText(v.getContext(), "Keystore ks error", Toast.LENGTH_SHORT).show();
                        Log.i(TAG,"Keystore ks error");
                        //System.exit(-1);
                    } catch (NoSuchAlgorithmException e) {
                        Toast.makeText(v.getContext(), "No such algorithm for ks.load", Toast.LENGTH_SHORT).show();
                        Log.i(TAG,"No such algorithm for ks.load");
                        e.printStackTrace();
                        //System.exit(-1);
                    } catch (CertificateException e) {
                        Toast.makeText(v.getContext(), "certificate missing", Toast.LENGTH_SHORT).show();
                        Log.i(TAG,"certificate missing");
                        e.printStackTrace();
                        //System.exit(-1);
                    } catch (UnrecoverableKeyException e) {
                        Toast.makeText(v.getContext(), "UnrecoverableKeyException", Toast.LENGTH_SHORT).show();
                        Log.i(TAG,"unrecoverableKeyException");
                        e.printStackTrace();
                        //System.exit(-1);
                    } catch (KeyManagementException e) {
                        Toast.makeText(v.getContext(), "KeyManagementException", Toast.LENGTH_SHORT).show();
                        Log.i(TAG,"key management exception");
                        e.printStackTrace();
                        //System.exit(-1);
                    }
                }
            }
        });


    }
    private void printServerCertificate(SSLSocket socket) {
        try {
            Certificate[] serverCerts =
                    socket.getSession().getPeerCertificates();
            for (int i = 0; i < serverCerts.length; i++) {
                Certificate myCert = serverCerts[i];
                Log.i(TAG,"====Certificate:" + (i+1) + "====");
                Log.i(TAG,"-Public Key-\n" + myCert.getPublicKey());
                Log.i(TAG,"-Certificate Type-\n " + myCert.getType());

                System.out.println();
            }
        } catch (SSLPeerUnverifiedException e) {
            Log.i(TAG,"Could not verify peer");
            e.printStackTrace();
            System.exit(-1);
        }
    }
    private void printSocketInfo(SSLSocket s) {
        Log.i(TAG,"Socket class: "+s.getClass());
        Log.i(TAG,"   Remote address = "
                +s.getInetAddress().toString());
        Log.i(TAG,"   Remote port = "+s.getPort());
        Log.i(TAG,"   Local socket address = "
                +s.getLocalSocketAddress().toString());
        Log.i(TAG,"   Local address = "
                +s.getLocalAddress().toString());
        Log.i(TAG,"   Local port = "+s.getLocalPort());
        Log.i(TAG,"   Need client authentication = "
                +s.getNeedClientAuth());
        SSLSession ss = s.getSession();
        Log.i(TAG,"   Cipher suite = "+ss.getCipherSuite());
        Log.i(TAG,"   Protocol = "+ss.getProtocol());
    }

    public void chat(String temp){
        String message = temp;
        String line = "";
        // send id of the device to match with the image
        try {
            out.write(message+"\n");
            out.flush();
        } catch (IOException e2) {
            Log.i(TAG,"Read failed");
            System.exit(1);
        }
        // receive a ready command from the server
//        try {
//            line = in.readLine();
//            mResponse.setText("SERVER SAID: "+line);
//            //Log.i(TAG,line);
//        } catch (IOException e1) {
//            Log.i(TAG,"Read failed");
//            System.exit(1);
//        }
    }
