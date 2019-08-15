byte[] message = new byte[] { (byte) 0xbe, (byte) 0xef, (byte) 0xef };
Cipher cipher = Cipher.getInstance("RSA");

KeyFactory keyFactory = KeyFactory.getInstance("RSA");

String mod = "B390F7412F2554387597814A25BC11BFFD95DB2D1456F1B66CDF52BCC1D20C7FF24F3CCE7B2D66E143213F64247454782A377C79C74477A28AF6C317BE68BC6E8FF001D375F9363B5A7161C2DFBC2ED0850697A54421552C6288996AC61AF5A9F7DE218ABBC75A145F891266615EB81D11A22B7260F7608083B373BA4BC0756B";
String exp = "010001";

RSAPublicKeySpec pubKeySpec = new RSAPublicKeySpec(new BigInteger(Hex.fromString(mod)), new BigInteger(Hex.fromString(exp)));
RSAPublicKey pubKey = (RSAPublicKey) keyFactory.generatePublic(pubKeySpec);

cipher.init(Cipher.ENCRYPT_MODE, pubKey);

byte[] cipherText = cipher.doFinal(message);
System.out.println("cipher: " + new String(cipherText));
