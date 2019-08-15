MessageDigest md = MessageDigest.getInstance("MD5");
byte[] arr = md.digest(bytesOfMessage);
return Base64.getEncoder().encodeToString(arr);
