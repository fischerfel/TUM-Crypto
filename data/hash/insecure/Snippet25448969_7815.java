MessageDigest md = MessageDigest.getInstance("MD5");
byte[] hash = md.digest(message.getBytes("UTF-8"));

String base64 = Base64.encodeToString(hash, Base64.DEFAULT);
