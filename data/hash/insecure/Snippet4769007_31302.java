MessageDigest md = null;
md = MessageDigest.getInstance("SHA");
md.update(plaintext.getBytes("UTF-8"));
byte raw[] = md.digest();
hash = new Base64().encodeToString(raw).replaceAll("\n", "").replaceAll("\r", "");
