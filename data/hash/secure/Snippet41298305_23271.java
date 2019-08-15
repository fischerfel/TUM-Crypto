// signed message --> hash of concat
MessageDigest digest = MessageDigest.getInstance("SHA-256");
digest.update( concat.getBytes() );
byte[] message = digest.digest();
System.out.println("message length "+message.length); // --> 32


// signature belonging to the message --> checkValue
System.out.println("check value length " +checkValue.length()); // --> 512
byte[] sigBytes = checkValue.getBytes();
System.out.println("check value bytes "+sigBytes.length); // --> 512

// public certificate of the CA 
File file3 = new File(certificatePath); 
byte[] encCertRSA = new byte[(int) file3.length()];
FileInputStream fis3 = new FileInputStream(file3);
fis3.read(encCertRSA);
fis3.close();
InputStream is = new ByteArrayInputStream( encCertRSA );

CertificateFactory f = CertificateFactory.getInstance("X.509");
X509Certificate certRSA = (X509Certificate)f.generateCertificate(is);
certRSA.checkValidity();
PublicKey pubKeyRSA = certRSA.getPublicKey();

Signature sig = Signature.getInstance("SHA256withRSA");
sig.initVerify(pubKeyRSA);

// supply the Signature object with the data for which a signature was generated --> hash of concat
sig.update(message);

boolean isValid = sig.verify( sigBytes );

System.out.println("The signature of the email verifies: " + isValid);
