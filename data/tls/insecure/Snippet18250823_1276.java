public class MyWebViewClient extends WebViewClient {
    private String loginCookie;
    private Context mContext;
    private WebView mWebView;

    public MyWebViewClient(Context context, WebView webview) {
        super();

        mContext = context;
        mWebView = webview;
    }

    @Override
    public void onPageStarted( WebView view, String url, Bitmap favicon ) {
    }

    @Override
    public void onPageFinished( WebView view, String url ) {
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setCookie(url, loginCookie);
    }

    @Override
    public void onReceivedError( WebView view, int errorCode, String description, String failingUrl ) {
        Toast.makeText(view.getContext(), "ƒy�[ƒW“Ç‚�?�ž‚�?ƒGƒ‰�[", Toast.LENGTH_LONG).show();
        System.err.println(errorCode + " - " + description + "-" + failingUrl); 


    }



    @Override
    public void onLoadResource( WebView view, String url ){
        CookieManager cookieManager = CookieManager.getInstance();
        loginCookie = cookieManager.getCookie(url);
    }

    @Override
    public boolean shouldOverrideUrlLoading( WebView view, String url ) {


        if (url.contains(".pdf" )){
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
               .detectNetwork() // or .detectAll() for all detectable problems
               .penaltyDialog()  //show a dialog
               .permitNetwork() //permit Network access 
               .build());
            Toast.makeText(view.getContext(), "pdf", Toast.LENGTH_LONG).show();
            String savedFile=Download(url);


        }
        return false;
    }

    public String Download(String Url)
    {

     String filepath=null;
     try 
     {
      //set the download URL, a url that points to a file on the internet
      //this is the file to be downloaded
         final HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
             public boolean verify1(String hostname, SSLSession session) {
                 return true;
             }

            @Override
            public boolean verify(String arg0, SSLSession arg1) {
                // TODO Auto-generated method stub
                return false;
            }
      };
         HttpURLConnection conn = null;
            URL url = new URL(Url);
            if (url.getProtocol().toLowerCase().equals("https")) {
                trustAllHosts();
                HttpsURLConnection https = (HttpsURLConnection) url.openConnection();
                https.setHostnameVerifier(DO_NOT_VERIFY);
                conn = https;
            } else {
                conn = (HttpURLConnection) url.openConnection();
            }

      //set up some things on the connection
      conn.setRequestMethod("GET");
      conn.setDoOutput(true); 
      //and connect!
      conn.connect();
      //set the path where we want to save the file
      //in this case, going to save it on the root directory of the
      //sd card.
      File SDCardRoot = Environment.getExternalStorageDirectory();
      //create a new file, specifying the path, and the filename
      //which we want to save the file as.

      String filename= "sample";   // you can download to any type of file ex:.jpeg (image) ,.txt(text file),.mp3 (audio file)
      Log.i("Local filename:",""+filename);
      File file = new File(SDCardRoot,filename);
      if(file.createNewFile())
      {
       file.createNewFile();
      }

      //this will be used to write the downloaded data into the file we created
      FileOutputStream fileOutput = new FileOutputStream(file);


      //this will be used in reading the data from the internet
      InputStream inputStream = conn.getInputStream();


      //this is the total size of the file
      int totalSize = conn.getContentLength();
      //variable to store total downloaded bytes
      int downloadedSize = 0;


      //create a buffer...
      byte[] buffer = new byte[4 * 1024];
      int bufferLength = 0; //used to store a temporary size of the buffer


      //now, read through the input buffer and write the contents to the file
      while ( (bufferLength = inputStream.read(buffer)) > 0 )
      {
       //add the data in the buffer to the file in the file output stream (the file on the sd card
       fileOutput.write(buffer, 0, bufferLength);
       //add up the size so we know how much is downloaded
       downloadedSize += bufferLength;
       //this is where you would do something to report the prgress, like this maybe
       Log.i("Progress:","downloadedSize:"+downloadedSize+"totalSize:"+ totalSize) ;


      }
      //close the output stream when done
      fileOutput.close();
      if(downloadedSize==totalSize)   filepath=file.getPath();

      //catch some possible errors...
     } catch (MalformedURLException e) {
      e.printStackTrace();
     } catch (IOException e) {
      filepath=null;
      e.printStackTrace();
     }
     **Log.i("filepath:is this error"," "+filepath) ;**


     return filepath;


    }







     private static void trustAllHosts() {
         // Create a trust manager that does not validate certificate chains
         TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
                 public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                         return new java.security.cert.X509Certificate[] {};
                 }

                 public void checkClientTrusted(X509Certificate[] chain,
                                 String authType) throws CertificateException {
                 }

                 public void checkServerTrusted(X509Certificate[] chain,
                                 String authType) throws CertificateException {
                 }
         } };

         // Install the all-trusting trust manager
         try {
                 SSLContext sc = SSLContext.getInstance("TLS");
                 sc.init(null, trustAllCerts, new java.security.SecureRandom());
                 HttpsURLConnection
                                 .setDefaultSSLSocketFactory(sc.getSocketFactory());
         } catch (Exception e) {
                 e.printStackTrace();
         }
 }

        @Override
    public void onReceivedSslError( WebView view, SslErrorHandler handler, SslError error ) {
        handler.proceed();
    }

    @Override
    public void onReceivedHttpAuthRequest( WebView view, final HttpAuthHandler handler, final String host, final String realm ){
        // ˆø��?host‚É‚�?setHttpAuthUsernamePassword‚Ì‘æ1ˆø��?‚Å��?’è‚µ‚½•¶Žš—ñ‚ª“ü‚�?‚Ä‚«‚Ü‚·�B
        // Android 4.*�i‚à‚µ‚©‚·‚é‚Æ3.*‚©‚ç�j‚Å‚�?‚È‚º‚©ˆø��?host‚É�ŸŽè‚Éƒ|�[ƒg�?Ô�†‚ª’Ç‹L‚³‚ê‚Ä‚µ‚Ü‚¢‚Ü‚·�B
        // ‹ï‘Ì“I‚É‚�?�u:80�v‚ª’Ç‹L‚³‚ê‚Ä‚µ‚Ü‚¢‚Ü‚·�B

        String userName = null;
        String userPass = null;

        if (handler.useHttpAuthUsernamePassword() && view != null) {
            String[] haup = view.getHttpAuthUsernamePassword(host, realm);
            if (haup != null && haup.length == 2) {
                userName = haup[0];
                userPass = haup[1];
            }
        }

        if (userName != null && userPass != null) {
            handler.proceed(userName, userPass);
        }
        else {
            showHttpAuthDialog(handler, host, realm, null, null, null);
        }
    }

    private void showHttpAuthDialog( final HttpAuthHandler handler, final String host, final String realm, final String title, final String name, final String password ) {
        LinearLayout llayout = new LinearLayout((Activity)mContext);
        final TextView textview1 = new TextView((Activity)mContext);
        final EditText edittext1 = new EditText((Activity)mContext);
        final TextView textview2 = new TextView((Activity)mContext);
        final EditText edittext2 = new EditText((Activity)mContext);
        llayout.setOrientation(LinearLayout.VERTICAL);
        textview1.setText("username:");
        textview2.setText("password:");
        llayout.addView(textview1);
        llayout.addView(edittext1);
        llayout.addView(textview2);
        llayout.addView(edittext2);

        final Builder mHttpAuthDialog = new AlertDialog.Builder((Activity)mContext);
        mHttpAuthDialog.setTitle("Enter your login details")
            .setView(llayout)
            .setCancelable(false)
            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    EditText etUserName = edittext1;
                    String userName = etUserName.getText().toString();
                    EditText etUserPass = edittext2;
                    String userPass = etUserPass.getText().toString();

                    mWebView.setHttpAuthUsernamePassword(host, realm, name, password);

                    handler.proceed(userName, userPass);
                }
            })
            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    handler.cancel();
                }
            })
            .create().show();
    }

}
