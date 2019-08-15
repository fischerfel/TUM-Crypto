public class ScheduleDownloader extends AsyncTask<String, Void, Object> {
private static SSLContext sc;
URL url;
String nameOfFile = "";
private Activity act;

public ScheduleDownloader(Activity act, URL url, String nameOfFile) {
    this.act = act;
    this.url = url;
    this.nameOfFile = nameOfFile;
}

public void setURL(String url){
    try {
        this.url = new URL(url);
    }catch (MalformedURLException e){
        Log.d("urlError", e.toString());
    }
};
public void setNameOfFile(String nameOfFile) {
    this.nameOfFile = nameOfFile;
}

protected Object doInBackground(String... strings)  {
    Log.d("url", url.toString());
    try {
        downloadFile();
    }catch (Exception e){
        e.printStackTrace();
    }

    return null;
}
private String downloadFile() throws Exception {
        trustEveryone();
        URLConnection urlConnection =  url.openConnection();

        InputStream input = urlConnection.getInputStream();
        BufferedInputStream inStream = new BufferedInputStream(input, 1024 * 5);

        File file = new File(act.getDir("filesdir", Context.MODE_PRIVATE)+nameOfFile);
        if(file.exists()) file.delete();
        file.createNewFile();

        FileOutputStream output = new FileOutputStream(file);
        byte[] data = new byte[1024 * 5];
        int count = 0;
         while ( (count = inStream.read(data) ) != -1){
             Log.d("TAGin", count+"");
             output.write(data, 0, count);
         }
        Log.d("TAG", count+"");
        output.flush();
        output.close();
        input.close();

    return null;
}
private void trustEveryone() {
    TrustManager[] trustAllCerts = new TrustManager[]{
            new X509TrustManager() {
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {return null;}
                public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {};
                public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {};
            }
    };
    try {
        sc  = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, new java.security.SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
    } catch (Exception e) {
        e.printStackTrace();
    }
}}
