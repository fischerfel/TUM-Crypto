public byte[] sumCalc (){ 
    String key = "anyKey";
    byte[] hashedKey = null;
    try {
        byte [] byteKey = key.getBytes("UTF-8");
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        hashedKey = md.digest(byteKey);
    }catch (Exception ex){
        System.err.println("Error generant clau" + ex);  
    }
    return hashedKey;
}
