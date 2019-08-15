byte[] bytes = text.getBytes( "UTF-8" );
MessageDigest messageDigest = MessageDigest.getInstance( "SHA-256" );
byte[] hash = messageDigest.digest( bytes );
String result = Base64.getEncoder().encodeToString(hash);
