url = "https://216.58.210.164/finance";
HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
conn.setHostnameVerifier(RelaxedSSLContext.allHostsValid); 
conn.connect();
int statusCode = conn.getResponseCode(); //200
// ERROR 'No subject alternative names matching IP address 216.58.210.164 found' without hostnameVerifier
