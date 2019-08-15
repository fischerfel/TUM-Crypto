public static byte[] RC4(byte[] x,byte[] keyBytes)   
{   
    byte[] e = null; 

    try   
    {   
        SecureRandom sr = new SecureRandom(keyBytes);
        KeyGenerator kg = KeyGenerator.getInstance(algorithm);
        kg.init(sr);
        SecretKey sk = kg.generateKey();  
        Cipher enCipher = Cipher.getInstance("RC4");   
        enCipher.init(Cipher.ENCRYPT_MODE,sk);   
        e = enCipher.doFinal(plaintext);              
    }   
    catch(Exception ex)   
    {   
        ex.printStackTrace();   
    }   
    return e;   
} 
