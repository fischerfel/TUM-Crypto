StringBuilder salt=new StringBuilder();
salt.append("MySuperSecretSalt");
MessageDigest md = MessageDigest.getInstance("SHA-256");
String text = "This is text to hash";
salt.append(text);    
md.update(salt.toString().getBytes("UTF-8")); // Change this to "UTF-16" if needed
byte[] digest = md.digest();
