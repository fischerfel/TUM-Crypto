MessageDigest digest = MessageDigest.getInstance("SHA-256");
byte[] hash = digest.digest(message.getBytes(StandardCharsets.UTF_8));
 for(byte c : hash) {
    if( String.format("%02X ",c) == "69" || String.format("%02X ",c) == "CB" ) {
        ...
    }
     System.out.println(String.format("%02X ",c));

 }    
