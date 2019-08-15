MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
byte[] hash = messageDigest.digest(toSign.getBytes("UTF-8"));
