public byte[] digestAuthKey(String login, Date d) {
   MessageDigest md = MessageDigest.getInstance("MD5");
   String key = login + SALT + d.getTime();
   return md.digest(key.getBytes());
}
