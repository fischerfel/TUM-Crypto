String urlParameters="dateTime=" + URLEncoder.encode(dateTime,"UTF-8")+
    "&mobileNum="+URLEncoder.encode(mobileNum,"UTF-8");

URL url = new URL(myurl);
HttpsURLConnection conn;
conn=(HttpsURLConnection)url.openConnection();

// Create the SSL connection
SSLContext sc;
sc = SSLContext.getInstance("TLS");
sc.init(null, null, new java.security.SecureRandom());
conn.setSSLSocketFactory(sc.getSocketFactory());
conn.setConnectTimeout(HTTP_CONNECT_TIME_OUT);
conn.setReadTimeout(HTTP_READ_TIME_OUT);

//set the output to true, indicating you are outputting(uploading) POST data
conn.setDoOutput(true);
//once you set the output to true, you don't really need to set the request method to post, but I'm doing it anyway
conn.setRequestMethod("POST");
conn.setFixedLengthStreamingMode(urlParameters.getBytes().length);
conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

PrintWriter out = new PrintWriter(conn.getOutputStream());
out.print(urlParameters);
out.close();

InputStream is = conn.getInputStream();
BufferedReader in = new BufferedReader(new InputStreamReader(is));
String inputLine;
while ((inputLine = in.readLine()) != null) {
  response += inputLine;            
}                   
