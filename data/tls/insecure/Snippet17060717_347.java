// Create a trust manager that does not validate certificate chains
TrustManager[] trustAllCerts = new TrustManager[]{
        new X509TrustManager() {
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null;
            }
            public void checkClientTrusted(
                    java.security.cert.X509Certificate[] certs, String authType) {
            }
            public void checkServerTrusted(
                    java.security.cert.X509Certificate[] certs, String authType) {
            }
        }
};

// Install the all-trusting trust manager
try {
    SSLContext sc = SSLContext.getInstance("SSL");
    sc.init(null, trustAllCerts, new java.security.SecureRandom());
    HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
} catch (Exception e) {
}


String httpsURL = "https://requestb.in/191g0961";

String query = "email="+URLEncoder.encode("abc@xyz.com","UTF-8"); 
query += "&";
query += "password="+URLEncoder.encode("abcd","UTF-8") ;

URL myurl = new URL(httpsURL);
HttpsURLConnection con = (HttpsURLConnection)myurl.openConnection();
con.setRequestMethod("POST");

con.setRequestProperty("Content-length", String.valueOf(query.length())); 
con.setRequestProperty("Content-Type","application/x-www-form-urlencoded"); 
con.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0;Windows98;DigExt)"); 
con.setDoOutput(true); 
con.setDoInput(true); 

DataOutputStream output = new DataOutputStream(con.getOutputStream());  

output.writeBytes(query);

output.close();

DataInputStream input = new DataInputStream(con.getInputStream()); 


for( int c = input.read(); c != -1; c = input.read() ) 
    System.out.print( (char)c ); 
input.close(); 

System.out.println("Resp Code:"+con .getResponseCode()); 
System.out.println("Resp Message:"+ con .getResponseMessage());
