TrustManager[] trustAllCerts = new TrustManager[] { 
    new X509TrustManager() {     
        public java.security.cert.X509Certificate[] getAcceptedIssuers() { 
            return null;
        } 
        public void checkClientTrusted( 
            java.security.cert.X509Certificate[] certificates, String authType) {
            } 
        public void checkServerTrusted( 
            java.security.cert.X509Certificate[] certificates, String authType) {
        }
    } 
}; 

try {
    String connectionString = "ssl://ipaddress:port"
    ActiveMQSslConnectionFactory factory = new  ActiveMQSslConnectionFactory(connectionString);
factory.setKeyAndTrustManagers(null, trustAllCerts, new SecureRandom());
    Connection connection = factory.createConnection(user,password);
    connection.start(); 

} catch (Exception e) {
} 
