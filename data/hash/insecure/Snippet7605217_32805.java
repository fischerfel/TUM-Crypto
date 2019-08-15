MessageDigest md = MessageDigest.getInstance("SHA-1");
byte[] passbyte;
passbyte = "abcdef12".getBytes("UTF-8");
passbyte = md.digest(passbyte);
