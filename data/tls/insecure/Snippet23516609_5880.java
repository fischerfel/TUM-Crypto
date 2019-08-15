 public static void main(String arg[]){

 try {
 KeyStore keyStore = KeyStore.getInstance("jks");

 InputStream inputStream;
 try {
 inputStream = new FileInputStream(ITestEncryptionConstants.SSLKEYSTORE);
 keyStore.load(inputStream,
 ITestEncryptionConstants.SSLPASSWORD.toCharArray());
 TrustManagerFactory trustManagerFactory =
 TrustManagerFactory.getInstance("PKIX", "SunJSSE");
 trustManagerFactory.init(keyStore);

 X509TrustManager x509TrustManager = null;
 for (TrustManager trustManager : trustManagerFactory.getTrustManagers())
 {
 if (trustManager instanceof X509TrustManager) {
 x509TrustManager = (X509TrustManager) trustManager;
 break;
 }
 }

 KeyManagerFactory keyManagerFactory =
 KeyManagerFactory.getInstance("SunX509", "SunJSSE");
 keyManagerFactory.init(keyStore,
 ITestEncryptionConstants.SSLPASSWORD.toCharArray());

 X509KeyManager x509KeyManager = null;
 for (KeyManager keyManager : keyManagerFactory.getKeyManagers()) {
 if (keyManager instanceof X509KeyManager) {
 x509KeyManager = (X509KeyManager) keyManager;
 break;
 }
 }

 SSLContext sslContext = SSLContext.getInstance("TLS");

 sslContext.init(new KeyManager[]{x509KeyManager},
 new TrustManager[]{x509TrustManager}, null);

 SSLServerSocketFactory factory =
 sslContext.getServerSocketFactory();//(SSLServerSocketFactory )
 SSLServerSocketFactory .getDefault();

 SSLServerSocket sslSock =
 (SSLServerSocket)factory.createServerSocket(8096);
 SSLSocket c = (SSLSocket)sslSock.accept();
 PrintWriter out =new PrintWriter(c.getOutputStream(),true);
 InputStream in = c.getInputStream();
in.close();
 out.close();


 } catch (FileNotFoundException e) {
 // TODO Auto-generated catch block
 e.printStackTrace();
 } catch (NoSuchAlgorithmException e) {
 // TODO Auto-generated catch block
 e.printStackTrace();
 } catch (CertificateException e) {
 // TODO Auto-generated catch block
 e.printStackTrace();
 } catch (IOException e) {
 // TODO Auto-generated catch block
 e.printStackTrace();
 } catch (NoSuchProviderException e) {
 // TODO Auto-generated catch block
 e.printStackTrace();
 } catch (UnrecoverableKeyException e) {
 // TODO Auto-generated catch block
 e.printStackTrace();
 } catch (KeyManagementException e) {
 // TODO Auto-generated catch block
 e.printStackTrace();
 }

 } catch (KeyStoreException e1) {
 // TODO Auto-generated catch block
 e1.printStackTrace();
 }



 }
