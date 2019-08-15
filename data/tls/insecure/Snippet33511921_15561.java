public class Sellado implements TSAClient{

//Code  

protected byte[] getTSAResponse(byte[] requestBytes)
     throws IOException, NoSuchAlgorithmException, UnrecoverableKeyException, KeyStoreException, KeyManagementException
  {

 //Preparing SSL Context

KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
kmf.init(EmpadronamientoI.ks, "pass".toCharArray());

TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
tmf.init(EmpadronamientoI.ks);

SSLContext ctx = SSLContext.getInstance("TLS");
ctx.init(kmf.getKeyManagers(),tmf.getTrustManagers(), new SecureRandom());
SSLSocketFactory sf = ctx.getSocketFactory();

URL url = new URL(this.tsaURL);
HttpsURLConnection tsaconn = (HttpsURLConnection)url.openConnection();
tsaconn.setSSLSocketFactory(sf);
tsaconn.setConnectTimeout(0);

tsaconn.setDoInput(true);
tsaconn.setDoOutput(true);
tsaconn.setUseCaches(false);
tsaconn.setRequestProperty("Content-Type", "application/timestamp-query");
tsaconn.setRequestProperty("Content-Transfer-Encoding", "binary");

try{
    tsaconn.connect();
        }
catch (IOException ioe) 
    {
           throw new IOException(MessageLocalization.getComposedMessage("failed.to.get.tsa.response.from.1", new Object[] { this.tsaURL }));
         }


 OutputStream out = tsaconn.getOutputStream();
 out.write(requestBytes);
 out.close();


 InputStream inp = tsaconn.getInputStream();
 ByteArrayOutputStream baos = new ByteArrayOutputStream();
 byte[] buffer = new byte['?'];
 int bytesRead = 0;
 while ((bytesRead = inp.read(buffer, 0, buffer.length)) >= 0) {
  baos.write(buffer, 0, bytesRead);
    }

 byte[] respBytes = baos.toByteArray();

String encoding = tsaconn.getContentEncoding();
if ((encoding != null) && (encoding.equalsIgnoreCase("base64"))) {
   respBytes = Base64.decode(new String(respBytes));
 }

return respBytes;
   }
    //Code
    }
