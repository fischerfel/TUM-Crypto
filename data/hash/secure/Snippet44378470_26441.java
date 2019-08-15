String hashWith256(String textToHash){
MessageDigest digest = MessageDigest.getInstance("SHA-256");
byte[] byteOfTextToHash=textToHash.getBytes("UTF-8");
byte[] hashedByetArray = digest.digest(byteOfTextToHash);
String encoded = Base64.encode(hashedByetArray );
return encoded;
}
