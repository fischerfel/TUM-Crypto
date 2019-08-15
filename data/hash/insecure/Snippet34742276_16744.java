String userId; 
String applicationKey; // E.g. "196087a1-e815-4bc4-8984-60d8d8a43f1d";
String applicationSecret; // E.g. "oYdgGRXoxEuJhGDY2KQ/HQ==";
long sequence; // fetch and increment last used sequence
String toSign = userId + applicationKey + sequence + applicationSecret;
MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
byte[] hash = messageDigest.digest(toSign.getBytes("UTF-8"));
String signature = Base64.encodeBase64String(hash).trim();
