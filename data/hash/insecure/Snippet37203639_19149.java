try {
      MessageDigest md = MessageDigest.getInstance("SHA-1");
      md.update(password.getBytes());
      BigInteger hash = new BigInteger(1, md.digest());
      hashword = hash.toString(16);
} catch (NoSuchAlgorithmException ex) {
      /* error handling */
}
return hashword;
