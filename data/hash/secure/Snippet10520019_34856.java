String tag = "...";
MessageDigest md = MessageDigest.getInstance("SHA-256");
md.update(text.getBytes("UTF-8"));
byte[] digest = md.digest();
