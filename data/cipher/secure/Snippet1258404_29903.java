byte[] certContents=null;
byte[] signature=null;
String contents = "abc";

// load cert
CertificateFactory factory = CertificateFactory.getInstance("X.509");
X509Certificate cert = (X509Certificate) factory.generateCertificate(new ByteArrayInputStream(certContents));

// grab public key
RSAPublicKey publicKey = (RSAPublicKey)cert.getPublicKey();

// get sha1 hash for contents        
Mac mac = Mac.getInstance("HmacSHA1");
mac.update(contents.getBytes());                
byte[] hash = mac.doFinal();

// get cipher
Cipher cipher = Cipher.getInstance("RSA");
cipher.init(Cipher.DECRYPT_MODE, publicKey);

// verify signature of contents matches signature passed to method somehow (and this is where I'm stuck)
