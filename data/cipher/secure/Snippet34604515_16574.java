File secKey = new File(ROOT_PATH+"pkcs8_key.key");
byte[] privateKeyB = readByte(secKey);
PKCS8EncodedKeySpec privateSpec = new PKCS8EncodedKeySpec(privateKeyB);

KeyFactory privateFactory = KeyFactory.getInstance("ec",provider);
PrivateKey privateKey = privateFactory.generatePrivate(privateSpec);
Cipher privateCipher = Cipher.getInstance("ECIES/NONE/Nopadding", provider);
privateCipher.init(Cipher.DECRYPT_MODE, privateKey);
byte[] encryptedFromFile = readByte(new File(ROOT_PATH+"test/real/testout.dat"));
byte[] decrypted = privateCipher.doFinal(encryptedFromFile);

FileUtils.writeByteArrayToFile(new File(ROOT_PATH+"test/real/testout_de.txt"), decrypted);`
