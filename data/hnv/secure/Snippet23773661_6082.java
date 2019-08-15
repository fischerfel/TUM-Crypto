public static void main (String[] args) throws Exception {
    org.apache.http.conn.ssl.SSLSocketFactory sf = org.apache.http.conn.ssl.SSLSocketFactory.getSocketFactory();
    sf.setHostnameVerifier(new MyHostnameVerifier());
    org.apache.http.conn.scheme.Scheme sch = new Scheme("https", 443, sf);

    org.apache.http.client.HttpClient client = new DefaultHttpClient();
    client.getConnectionManager().getSchemeRegistry().register(sch);
    org.apache.http.client.methods.HttpPost post = new HttpPost("https://www.rideforrainbows.org/");
    org.apache.http.HttpResponse response = client.execute(post);
    java.io.InputStream is = response.getEntity().getContent();
    java.io.BufferedReader rd = new java.io.BufferedReader(new java.io.InputStreamReader(is));
    String line;
    while ((line = rd.readLine()) != null) { 
        System.out.println(line);
    }
}
