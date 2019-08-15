MessageDigest md = null;
md = MessageDigest.getInstance(MESSAGE_DIGEST_ALGORITHM);
md.update(saltBytes);
md.update(passwordBytes);
byte[] digest = md.digest();
