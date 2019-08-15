static{
    //install ibm's provider
    java.security.Security.addProvider(new IBMJCE());
}

public byte[] encrypt(byte[] input)throws SecurityException{
    KeyGenerator kg = KeyGenerator.getInstance("DES");
    //call ibm's provider
    SecureRandom sr = SecureRandom.getInstance("IBMSecureRandom", new IBMJCE());
    sr.setSeed(str.getBytes());
    kg.init(sr);
    Key key = kg.generateKey();
    Cipher cipher = Cipher.getInstance("DES");
    cipher.init(1, key);
    byte[] ret = cipher.doFinal(input);
    return ret;
}
