HostnameVerifier hostnameVerifier = org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;

DefaultHttpClient client = new DefaultHttpClient();

SchemeRegistry registry = new SchemeRegistry();
SSLSocketFactory socketFactory = SSLSocketFactory.getSocketFactory();
socketFactory.setHostnameVerifier((X509HostnameVerifier) hostnameVerifier);
registry.register(new Scheme("https", socketFactory, 443));
SingleClientConnManager mgr = new SingleClientConnManager(client.getParams(), registry);
DefaultHttpClient httpClient = new DefaultHttpClient(mgr, client.getParams());

HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);

try {

    final String url = "https://sso.three.co.uk/mylogin/?service=https%3A%2F%2Fwww.three.co.uk%2FThreePortal%2Fappmanager%2FThree%2FSelfcareUk%3F_pageLabel%3DP22200581501288714397101%26_nfpb%3Dtrue%26&amp;resource=portlet";
    HttpPost httpPost = new HttpPost(url);
    HttpResponse response = httpClient.execute(httpPost);

} catch(IOException e) {

    AlertDialog alertDialog = new AlertDialog.Builder(SslActivity.this).create();
    alertDialog.setTitle("About");
    alertDialog.setMessage(e.toString());
    alertDialog.setButton("Okay", new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {

        }
    });

    alertDialog.show();

}
