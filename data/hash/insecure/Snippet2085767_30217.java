MessageDigest md = null;
md = MessageDigest.getInstance("SHA");
md.update(pPassword.getBytes("UTF-8"));
byte raw[] = md.digest();
String hash = BASE64Encoder.encodeBuffer(raw);
