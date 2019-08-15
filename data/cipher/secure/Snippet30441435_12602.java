KeyFactory keyFactory = KeyFactory.getInstance("RSA");
RSAPublicKeySpec pubKeySpec = new RSAPublicKeySpec(new BigInteger(modulusBytes), new BigInteger(exponentBytes));
RSAPublicKey publicKey = (RSAPublicKey) keyFactory.generatePublic(pubKeySpec);

byte[] decrypted = null;
try {
    // get an RSA cipher object and print the provider
    final Cipher cipher = Cipher.getInstance("RSA/None/NoPadding");

    // decrypt the text using the public key
    cipher.init(Cipher.DECRYPT_MODE, publicKey);
    decrypted = cipher.doFinal(area_fissa_byte);

} catch (Exception ex) {
    ex.printStackTrace();
    Log.d("error","error");
}
