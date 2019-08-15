ECPublicKey key = (ECPublicKey) certificate.getPublicKey();
byte[] keyBytes = key.getEncoded(); 

MessageDigest messageDigest = MessageDigest.getInstance("SHA256", PROVIDER_NAME);
byte[] keyHash = messageDigest.digest(keyBytes);    

String base64keyBytes = new String(Base64.encode(keyBytes));
//base64keyBytes == MFkwEwYHKoZIzj0CAQYIKoZIzj0DAQcDQgAE+4wQWWRnPqGlsncZX17t0CfLOl6u
// 68aXUsqnzlIcpCdDukHhxibd2MjHPFGpnK3ZKdHxIFh+NBQvGssM5ncm1g==
// line break added for readability
