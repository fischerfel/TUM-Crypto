String asymPadding = "RSA2048/ECB/OAEPWithSHA256AndMGF1Padding";
String secKeyEncoded = getSymmetricKey(secKey);
KeyPair keyPair = getKeyPair(SELF4);

if (asymmPadding.contains(RSA2048)) {
    asymmPadding = RSA.concat(asymmPadding.substring(asymmPadding.indexOf("/")));
}

Cipher cipher = Cipher.getInstance(asymPadding);
