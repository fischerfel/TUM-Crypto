public static void main(String args[]){ 
     String message = "<abc>ABCDEFG</abc>"; 
     String key = "key"; 
     byte[] b = encrypt(message.getBytes(), key.getBytes());
}

public byte[] encrypt(byte encrypt[], byte en_key[]) { 
     try { 
           SecretKeySpec key = new SecretKeySpec(en_key, "Blowfish"); 
           Cipher cipher = Cipher.getInstance("Blowfish/ECB/NoPadding"); 
           cipher.init(Cipher.ENCRYPT_MODE, en_key); 
           return cipher.doFinal(encrypt); 
     } catch (Exception e) { 
           e.printStackTrace();
           return null; 
     }
} 
