String password = "_sOme*ShaREd*SecreT";
byte[] salta = new byte[]{-84, -119, 25, 56, -100, 100, -120, -45, 84, 67, 96, 10, 24, 111, 112, -119, 3};
SecretKeyFactory factory = null;
try {
    factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
} catch (NoSuchAlgorithmException e2) {
    e2.printStackTrace();
}
KeySpec spec = new PBEKeySpec(password.toCharArray(), salta, 1024, 128);
SecretKey tmp = null;
try {
    tmp = factory.generateSecret(spec);
} catch (InvalidKeySpecException e2) {
    e2.printStackTrace();
}
SecretKeySpec secret = new SecretKeySpec(tmp.getEncoded(), "AES");
Log.i("The secret Key: " , Base64.encodeToString(secret.getEncoded(), 0 ));
