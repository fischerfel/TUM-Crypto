try{ 
    JSONObject jobj=new JSONObject();

    jobj.put("UserName","demo1");
    jobj.put("Password","demo1");
    Log.e("json string :",jobj.toString());

    byte[] postData = jobj.toString().getBytes();

    URL url = new URL("https://services.myovs.com/TokenService/Token");

    SSLContext sc = SSLContext.getInstance("SSL");
    sc.init(null, trustAllCerts, new java.security.SecureRandom());
    HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

    HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();

    conn.setDoOutput(true);
    conn.setDoInput(true);
    conn.setUseCaches(false);

    conn.setRequestMethod("POST");
    conn.setRequestProperty("Content-Type", "application/json");

    OutputStream out = conn.getOutputStream();

    out.write(postData);
    out.close();

    String temp = ((HttpsURLConnection)conn).getResponseMessage();
    Log.e("response  :",temp);
} catch(Exception ex) {
    ex.printStackTrace();   
}
