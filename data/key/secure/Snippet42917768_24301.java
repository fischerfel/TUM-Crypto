 private Key generateKey() {
     Key secretKey = new SecretKeySpec(keyValue, ALGO);
     return secretKey;
}
