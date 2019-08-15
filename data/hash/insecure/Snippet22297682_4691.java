//Using the same DB store certificates

//Verify signatures using Bouncy Castle JCE Provider

//Hash data using SHA-1
byte[] data_ = request.getDataInUTF8().getBytes("UTF-8");
MessageDigest messageDigest_ = MessageDigest.getInstance("SHA-1", "BC");
byte[] hashed = messageDigest.digest(data);
//Signature
byte [] signature = new BASE64Decoder().decodeBuffer(request.getSignatureInBase64());
//Certificate
CertificateFactory certificateFactory = CertificateFactory.getInstance("X509", "BC");
byte[] buffer = new BASE64Decoder().decodeBuffer(certEncodeBase64);
ByteArrayInputStream byteArrayCertificateInputStream = new ByteArrayInputStream(buffer);
Certificate certificate = certificateFactory.generateCertificate(byteArrayCertificateInputStream);
//Verify using algorithm RSA
Signature verifyEngine = Signature.getInstance("RSA", "BC");
verifyEngine.initVerify(certificate);
verifyEngine.update(hashed);
result = verifyEngine.verify(signature);
