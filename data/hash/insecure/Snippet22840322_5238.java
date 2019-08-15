PrivateKey privatekey = (PrivateKey) keyStore.getKey(alias, null);
...
MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
byte[] data_to_sign = sha1.digest(bdataToSign);
Signature sig = Signature.getInstance("NONEwithRSA", "SunMSCAPI");
sig.initSign(privatekey);
sig.update(data_to_sign);
byte[] bSignedData_JAVASHA1_CAPIRSA = sig.sign();
...
