DHParameterSpec dhSkipParamSpec = new DHParameterSpec(skip1024Modulus, skip1024Base);
KeyPairGenerator aliceKpairGen = KeyPairGenerator.getInstance("DH", "BC");
aliceKpairGen.initialize(dhSkipParamSpec);
KeyPair aliceKpair = aliceKpairGen.generateKeyPair();
byte[] alicePubKeyEnc = aliceKpair.getPublic().getEncoded();

aliceKeyAgree = KeyAgreement.getInstance("DH", "BC");
aliceKeyAgree.init(aliceKpair.getPrivate());


//... obtaining Bob's Public Key
aliceKeyFac = KeyFactory.getInstance("DH", "BC");
X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(bobPubKeyEnc);
bobPubKey = aliceKeyFac.generatePublic(x509KeySpec);

aliceKeyAgree.doPhase(bobPubKey, true);
SecretKey aliceAesKey = aliceKeyAgree.generateSecret("AES");

Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding", "BC");
cipher.init(Cipher.ENCRYPT_MODE, aliceAesKey);
byte[] cipherText = cipher.doFinal(plaintext.getBytes());
