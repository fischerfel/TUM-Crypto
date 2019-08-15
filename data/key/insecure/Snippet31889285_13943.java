String apikey = "xxx";
String apisecret = "xxx";
String nonce = String.valueOf(System.currentTimeMillis());
String uri = "https://bittrex.com/api/v1.1/market/getopenorders?apikey=" + apikey + "&nonce=" + nonce;

Mac mac = Mac.getInstance("HmacSHA512");
SecretKeySpec secret = new SecretKeySpec(apisecret.getBytes(),"HmacSHA512");
mac.init(secret);
byte[] digest = mac.doFinal(uri.getBytes());
String sign = new String(digest);

HttpURLConnection con = (HttpURLConnection) new URL(uri).openConnection();
con.setRequestProperty("apisign", sign); // << very confused
con.setRequestMethod("GET");
con.connect();

con.getInputStream(); // << Exception is thrown
