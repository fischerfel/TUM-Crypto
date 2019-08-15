public class MainActivity extends Activity {
    private TextView textView;
    String response = "";
    String finalresponse="";


    /** Called when the activity is first created. */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.TextView01);
        System.setProperty("javax.net.ssl.trustStore","C:\\User\\*" );
        System.setProperty("javax.net.ssl.trustStorePassword", "" );
    }

    private class DownloadWebPageTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {



            TrustManager[] trustAllCerts = new TrustManager[] {
                new X509TrustManager() {
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }
                    public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {
                    }
                    public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {
                    }
                }
            };

            try {
                SSLContext sc = SSLContext.getInstance("SSL");
                sc.init(null, trustAllCerts, new java.security.SecureRandom());
                HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            } catch (Exception e) {
            }


            try {
                URL url = new URL("https://172.27.224.133");

                HttpsURLConnection con =(HttpsURLConnection)url.openConnection();

                con.setHostnameVerifier(new AllowAllHostnameVerifier());
                finalresponse=readStream(con.getInputStream());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return finalresponse;
        }

        private String readStream(InputStream in) {
            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new InputStreamReader(in));
                String line = "";
                while ((line = reader.readLine()) != null) {
                    response+=line;
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return response;
        } 


        @Override
        protected void onPostExecute(String result) {
            textView.setText(finalresponse);
        }
    }

    public void readWebpage(View view) {
        DownloadWebPageTask task = new DownloadWebPageTask();
        task.execute(new String[] { "https://172.27.224.133" });
    }
}
