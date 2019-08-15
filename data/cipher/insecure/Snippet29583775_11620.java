static Cipher cipher;

static final int[] ENC_MODE = {Cipher.ENCRYPT_MODE, Cipher.DECRYPT_MODE, Cipher.ENCRYPT_MODE};

public static void main(String[] args) throws Exception {

    String text = "this is my text";
    cipher = Cipher.getInstance("DES");
    SecretKey key1 = KeyGenerator.getInstance("DES").generateKey();
    SecretKey key2 = KeyGenerator.getInstance("DES").generateKey();

    String cipherText = enc(text, key1, key2);



}

private static String enc(String plainText, SecretKey key1, SecretKey key2) throws Exception{
    byte[] textBytes = null;
    String encText = plainText;

    for(int i=0; i<3; i++){ 
        if (ENC_MODE[i] == Cipher.ENCRYPT_MODE){
            cipher.init(ENC_MODE[i], key1);
            textBytes = encText.getBytes("UTF8");
            textBytes = cipher.doFinal(textBytes);
            encText = Base64.getEncoder().encodeToString(textBytes);

        }else if(ENC_MODE[i] == Cipher.DECRYPT_MODE){
            cipher.init(ENC_MODE[i], key2);
            textBytes = cipher.doFinal(textBytes);  //Error Line
            encText = new String(textBytes, "UTF8");
        }

        System.out.println("loop= " + i +" "+encText);
    }
    return encText;
}
