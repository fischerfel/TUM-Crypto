byte[] data = "test".getBytes("UTF8");
KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
kpg.initialize(512);
KeyPair keyPair = kpg.genKeyPair();

byte[] pk = keyPair.getPublic().getEncoded();
X509EncodedKeySpec spec = new X509EncodedKeySpec(pk);
KeyFactory keyFactory = KeyFactory.getInstance("RSA");
PublicKey pubKey = keyFactory.generatePublic(spec);
Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
cipher.init(Cipher.ENCRYPT_MODE, pubKey);
byte[] encrypted = cipher.doFinal(data);

byte[] priv = keyPair.getPrivate().getEncoded();
PKCS8EncodedKeySpec spec2 = new PKCS8EncodedKeySpec(priv);
PrivateKey privKey = keyFactory.generatePrivate(spec2);
cipher.init(Cipher.DECRYPT_MODE, privKey);
byte[] plain = cipher.doFinal(encrypted);

System.out.println(new String(plain, "UTF8")); //=> "test"
