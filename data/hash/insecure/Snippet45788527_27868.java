public class Test2 {
    public static void main(String[] args) {

        try {
            String uniqueToken = JwtTokenUtil.generateToken(USER_AGENT);
            RSA1 rsa = new RSA1();
            Map<String,String> map = rsa.getValue();
            String singnatureValue = map.get("signature");
            String digestValue = map.get("digest");
            String httpsURL = "https://<https url>.svc/Https";
            DefaultHttpClient httpClient = new DefaultHttpClient();

            try {

String CA_FILE = "C:\\Users\\Administrator\\Desktop\\certificate file.cer";

FileInputStream fis = new FileInputStream(CA_FILE);
X509Certificate ca = (X509Certificate) CertificateFactory.getInstance(
        "X.509").generateCertificate(new BufferedInputStream(fis));

KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
ks.load(null, null);
ks.setCertificateEntry(Integer.toString(1), ca);

TrustManagerFactory tmf = TrustManagerFactory
        .getInstance(TrustManagerFactory.getDefaultAlgorithm());
tmf.init(ks);

SSLContext ctx = SSLContext.getInstance("tls");
TrustManager[] trustManagers = tmf.getTrustManagers();

KeyManager[] keyManagers = getKeyManagers("pkcs12", new FileInputStream(new File("C:\\Users\\Administrator\\Desktop\\pfxfile.pfx")), "password");
ctx.init(keyManagers, trustManagers, new SecureRandom());
SSLSocketFactory factory = new SSLSocketFactory(ctx, new StrictHostnameVerifier());

ClientConnectionManager manager = httpClient.getConnectionManager();
manager.getSchemeRegistry().register(new Scheme("https", 443, factory));

InputStream pfxInputStream = new FileInputStream("C:\\Users\\Administrator\\Desktop\\dvs\\pfxfile.pfx");
KeyStore ksa = KeyStore.getInstance("PKCS12");
ksa.load(pfxInputStream, "AztGEx4NpTScKFFs".toCharArray());
Enumeration<String> aliases = ksa.aliases();
String aliaz = "";
while (aliases.hasMoreElements()) {
    aliaz = aliases.nextElement();
    if (ksa.isKeyEntry(aliaz)) {
        break;
    }
}
MessageDigest md = MessageDigest.getInstance("SHA-1");
md.update("X.509".getBytes());
byte[] digestd =  md.digest();

X509Certificate certificate = (X509Certificate) ksa.getCertificate(aliaz);
Base64 base64 = new Base64();
String tokena = base64.encodeToString(certificate.getEncoded());
String signature = base64.encodeToString(certificate.getSignature());
String digest = base64.encodeToString(digestd);

URL obj = new URL(httpsURL);
HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

con.setRequestMethod("POST");
con.setRequestProperty("User-Agent", USER_AGENT);
con.setRequestProperty("Content-Type", "application/soap+xml; charset=utf-8");
con.setRequestProperty("Host", "endpoint url");
con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
con.setRequestProperty("Accept-Encoding", "gzip, deflate");
con.setSSLSocketFactory(ctx.getSocketFactory());

String urlParameters = "<s:Envelope xmlns:s=\"http://www.w3.org/2003/05/soap-envelope\" xmlns:a=\"http://www.w3.org/2005/08/addressing\" xmlns:u=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd\">\n"
        + " <s:Header>\n"
        + " <a:Action s:mustUnderstand=\"1\">http://Contract/Service/Manager/Verification</a:Action>\n"
        + " <a:MessageID>urn:uuid:cc74abd8aafsa8f</a:MessageID>\n"
        + " <ActivityId CorrelationId=\"c4bsdde6af1d5\" xmlns=\"http://schemas.microsoft.com/2004/09/ServiceModel/Diagnostics\">8338-38a9a19bd371</ActivityId>\n"
        + " <a:ReplyTo>\n"
        + " <a:Address>http://www.w3.org/2005/08/addressing/anonymous</a:Address>\n"
        + " </a:ReplyTo>\n"
        + " <a:To s:mustUnderstand=\"1\" u:Id=\"_1\">https://URL/Bus/VerificationServiceBus.svc/Https</a:To>\n"
        + " <o:Security s:mustUnderstand=\"1\" xmlns:o=\"http://url.xsd\">\n"
        + " <u:Timestamp u:Id=\"_0\">\n"
        + " <u:Created>" + nowUTCg + "</u:Created>\n"
        + " <u:Expires>" + nowUTCg5 + "</u:Expires>\n"
        + " </u:Timestamp>\n"
        + " <o:BinarySecurityToken EncodingType=\"http://wss-soap-message-security-1.0#Base64Binary\" \n"
        + " ValueType=\"http://url-token-profile-1.0#X\" \n"
        + " u:Id=\"urn:uuid:5a23-4a786f0da8c-10\">" + tokena + "</o:BinarySecurityToken>\n"
        + " <Signature xmlns=\"http://www.w3.org/2000/09/xmldsig#\">\n"
        + " <SignedInfo>\n"
        + " <CanonicalizationMethod Algorithm=\"http://www.w3.org/2001/10/xml-exc-c14n#\"></CanonicalizationMethod>\n"
        + " <SignatureMethod Algorithm=\"http://www.w3.org/2000/09/xmldsig#rsa-sha1\"></SignatureMethod>\n"
        + " <Reference URI=\"#_0\">\n"
        + " <Transforms>\n"
        + " <Transform Algorithm=\"http://www.w3.org/2001/10/xml-exc-c14n#\"></Transform>\n"
        + " </Transforms>\n"
        + " <DigestMethod Algorithm=\"http://www.w3.org/2000/09/xmldsig#sha1\"></DigestMethod>\n"
        + " <DigestValue></DigestValue>\n"
        + " </Reference>\n"
        + " <Reference URI=\"#_1\">\n"
        + " <Transforms>\n"
        + " <Transform Algorithm=\"http://www.w3.org/2001/10/xml-exc-c14n#\"></Transform>\n"
        + " </Transforms>\n"
        + " <DigestMethod Algorithm=\"http://www.w3.org/2000/09/xmldsig#sha1\"></DigestMethod>\n"
        + " <DigestValue>"+digestValue+"</DigestValue>\n"
        + " </Reference>\n"
        + " </SignedInfo>\n"
        + " <SignatureValue>"+singnatureValue+"</SignatureValue>\n"
        + " <KeyInfo>\n"
        + " <o:SecurityTokenReference>\n"
        + " <o:Reference ValueType=\"http://url-token-profile-1.0#X\" URI=\"#urn:uuid:5a23-4a786f0da8c-10\"></o:Reference>\n"
        + " </o:SecurityTokenReference>\n"
        + " </KeyInfo>\n"
        + " </Signature>\n"
        + " </o:Security>\n"
        + " </s:Header>\n"
        + " <s:Body>\n"
        + " <VerifyDocument xmlns=\"http://Contract/Service/Manager\">\n"
        + " <request i:type=\"b:RequestName\" xmlns:b=\"http://Contract/Data/Manager\" xmlns:i=\"http://www.w3.org/2001/XMLSchema-instance\">\n"
        + " <b:Fullname i:nil=\"true\"></b:Fullname>\n"
        + " </request>\n"
        + " </VerifyDocument>\n"
        + " </s:Body>\n"
        + " </s:Envelope>";

con.setDoInput(true);
con.setDoOutput(true);
DataOutputStream wr = new DataOutputStream(con.getOutputStream());
wr.writeBytes(urlParameters);
wr.flush();
wr.close();

int responseCode = con.getResponseCode();
BufferedReader innn = new BufferedReader(new InputStreamReader(con.getErrorStream()));
String line = "";
while ((line = innn.readLine()) != null) {
    System.out.println("Error Stream=>" + line);
}

            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (IOException ex) {
            Logger.getLogger(Test2.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(Test2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    protected static KeyManager[] getKeyManagers(String keyStoreType, InputStream keyStoreFile, String keyStorePassword) throws Exception {
        KeyStore keyStore = KeyStore.getInstance(keyStoreType);
        keyStore.load(keyStoreFile, keyStorePassword.toCharArray());
        KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        kmf.init(keyStore, keyStorePassword.toCharArray());
        return kmf.getKeyManagers();
    }

    protected static TrustManager[] getTrustManagers(String trustStoreType, InputStream trustStoreFile, String trustStorePassword) throws Exception {
        KeyStore trustStore = KeyStore.getInstance(trustStoreType);
        trustStore.load(trustStoreFile, trustStorePassword.toCharArray());
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(trustStore);
        return tmf.getTrustManagers();
    }
}
