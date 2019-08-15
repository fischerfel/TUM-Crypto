    Button sendButton;
    EditText textSend;
    private String ip_address = "192.168.10.103";
    private int port = 5000;


    private SSLSocket socket = null;
    private BufferedWriter out = null;
    private BufferedReader in = null;
    private final String TAG = "TAG";
    private char keystorepass[] = "......".toCharArray();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sendButton = (Button) findViewById(R.id.send);
        textSend = (EditText) findViewById(R.id.textsend);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String send = textSend.getText().toString();
                if(send.isEmpty()){
                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainActivity.this);
                    dialogBuilder.setMessage("Enter Text!");
                    dialogBuilder.setTitle("No TEXT");
                    dialogBuilder.setPositiveButton("OK...", null);
                    dialogBuilder.show();
                }else{
                    try {
                        KeyStore ks = KeyStore.getInstance("BKS");
                        InputStream keyin = getApplicationContext().getResources().openRawResource(R.raw.androidnewkey);
                        ks.load(keyin, keystorepass);
                        Log.i(TAG,"2");
                        SSLSocketFactory socketFactory = new SSLSocketFactory(ks);
                        socketFactory.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
                        socket = (SSLSocket)
                                socketFactory.createSocket(new Socket(ip_address,port), ip_address, port, false);
                        socket.startHandshake();
                        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                        chat(send);

                    }catch (KeyStoreException e){
                        Log.i(TAG,"KeyStor");
                    }catch (IOException r ){
                        Log.i(TAG,"IO");
                    }catch(NoSuchAlgorithmException r ){
                        Log.i(TAG,"No all");
                    }catch (CertificateException u){
                        Log.i(TAG,"CertEx");
                    }catch(KeyManagementException r){
                        Log.i(TAG,"eyEx");
                    }catch(UnrecoverableKeyException e){
                        Log.i(TAG,"UnrectEx");
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
