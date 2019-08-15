byte[] bytes = new byte[32];
String message = "secret";
MessageDigest md = MessageDigest.getInstance("SHA-256");
bytes = md.digest(message.getBytes("UTF-8"));

JWSSigner signer = new MACSigner(bytes);
