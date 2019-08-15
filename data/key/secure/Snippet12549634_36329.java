public static void signRequest(HttpURLConnection request, String account, String key) throws Exception
{
    SimpleDateFormat fmt = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss");
    fmt.setTimeZone(TimeZone.getTimeZone("GMT"));
    String date = fmt.format(Calendar.getInstance().getTime()) + " GMT";

    StringBuilder sb = new StringBuilder();
    sb.append("GET\n"); // method
    sb.append('\n'); // md5 (optional)
    sb.append('\n'); // content type
    sb.append('\n'); // legacy date
    sb.append("x-ms-date:" + date + '\n'); // headers
    sb.append(request.getURL().getPath() + "/tweet/?comp=list"); // resource TODO: "?comp=..." if present

    System.out.println(sb.toString());
    Mac mac = Mac.getInstance("HmacSHA256");
    mac.init(new SecretKeySpec(Base64.decode(key), "HmacSHA256"));
    String authKey = new String(Base64.encode(mac.doFinal(sb.toString().getBytes("UTF-8"))));
    String auth = "SharedKey " + account + ":" + authKey;
    request.setRequestProperty("x-ms-date", date);
    request.setRequestProperty("Authorization", auth);
    request.setRequestMethod("GET");
    System.out.println(auth);
}



public static void main(String args[]) throws Exception
{
     String account = "devstoreaccount1";
     String key = "Eby8vdM02xNOcqFlqUwJPLlmEtlCDXJ1OUzFT50uSRZ6IFsuFq2UVErCz4I6tq/K1SZFPTOtr/KBHBeksoGMGw==";
     HttpURLConnection connection = (HttpURLConnection) (new URL("http://localhost:10000/devstoreaccount1")).openConnection();
     signRequest(connection, account, key);
     connection.connect();
     System.out.println(connection.getResponseMessage());
 }
