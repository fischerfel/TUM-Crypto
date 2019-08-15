MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
byte[] hashedPassword = messageDigest.digest(userPassword.getBytes());
String hashedPasswordText = Base64.encodeToString(hashedPassword, Base64.DEFAULT).trim();
