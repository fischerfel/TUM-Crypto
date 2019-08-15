// decode public key
pubk = KeyFactory.getInstance("RSA").generatePublic(
    new X509EncodedKeySpec(X)
);
// decode symmetric key
cipher = Cipher.getInstance("RSA");
cipher.init(Cipher.UNWRAP_MODE, pubk);
skey = (SecretKey)cipher.unwrap(key1, "AES", Cipher.SECRET_KEY);
