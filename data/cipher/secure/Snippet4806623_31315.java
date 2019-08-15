MessageDigest sha1 = MessageDigest.getInstance("SHA1", BOUNCY_CASTLE_PROVIDER);
byte[] digest = sha1.digest(content);
DERObjectIdentifier sha1oid_ = new DERObjectIdentifier("1.3.14.3.2.26");

AlgorithmIdentifier sha1aid_ = new AlgorithmIdentifier(sha1oid_, null);
DigestInfo di = new DigestInfo(sha1aid_, digest);

byte[] plainSig = di.getDEREncoded();
Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding", BOUNCY_CASTLE_PROVIDER);
cipher.init(Cipher.ENCRYPT_MODE, privateKey);
byte[] signature = cipher.doFinal(plainSig);
