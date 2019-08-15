MessageDigest messageDigest = MessageDigest.getInstance("MD5");
byte[] passwordMD5Digest = messageDigest.digest(passwordString.getBytes("UTF-8"));
