TrustManager[] trustAllCerts = new TrustManager[] { 
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
} catch (GeneralSecurityException e) {

}


String url = "https://mycompany.com/gerrit/p/my-services.git";
String username = "username";
String password = "password";
String checkoutLocation = "/Users/testing";
String branch = "my-services-sample-test";

UsernamePasswordCredentialsProvider userCredential = new UsernamePasswordCredentialsProvider(username, password);
Git r = Git.cloneRepository().setDirectory(new File(checkoutLocation + "/clonned"))
.setCredentialsProvider(userCredential)
.setURI(url)
.setProgressMonitor(new TextProgressMonitor())
.setBranch(branch)
.call();
System.out.println("Clonning @ " + checkoutLocation);
r.getRepository().close();  
