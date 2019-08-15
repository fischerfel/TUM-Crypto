MessageDigest md = MessageDigest.getInstance("SHA-256");
byte[] hash = md.digest(password.getBytes("UTF-8"));
