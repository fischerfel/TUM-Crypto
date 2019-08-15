public static void sendPostReq() throws Exception{

    String grooveSharkjson = "{'method':'startSession','header':{'wsKey':'wskey'}}";

    String key = "secret"; // Your api key.
    String sig = SecurityHelper.getHmacMD5(grooveSharkjson, key);

    URL url = new URL("https://api.grooveshark.com/ws3.php?sig=" + sig);
    URLConnection connection = url.openConnection();
    connection.setDoInput(true);
    connection.setDoOutput(true);

    connection.connect();

    OutputStream os = connection.getOutputStream();
    PrintWriter pw = new PrintWriter(new OutputStreamWriter(os));
    pw.write(grooveSharkjson);
    pw.close();

    InputStream is = connection.getInputStream();
    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
    String line = null;
    StringBuffer sb = new StringBuffer();
    while ((line = reader.readLine()) != null) {
        sb.append(line);
    }
    is.close();
    String response = sb.toString();
    System.out.println(response);

}

public static String getHmacMD5(String payload, String secret) {
    String sEncodedString = null;
    try {
       SecretKeySpec key = new SecretKeySpec((secret).getBytes("UTF-8"), "HmacMD5");
       Mac mac = Mac.getInstance("HmacMD5");
       mac.init(key);

       byte[] bytes = mac.doFinal(payload.getBytes("UTF-8"));

       StringBuffer hash = new StringBuffer();

       for (int i=0; i<bytes.length; i++) {
          String hex = Integer.toHexString(0xFF &  bytes[i]);
          if (hex.length() == 1) {
              hash.append('0');
          }
              hash.append(hex);
          }
          sEncodedString = hash.toString();
       }
       catch (UnsupportedEncodingException e) {}
       catch(InvalidKeyException e){}
       catch (NoSuchAlgorithmException e) {}

       return sEncodedString ;
}
