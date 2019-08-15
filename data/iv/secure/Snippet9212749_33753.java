public void encrypt(String plaintext, String IV, String tripleDesKey){

try{

     SecretKey keySpec = new SecretKeySpec(tripleDesKey.getBytes("US-ASCII"),"DESede");

    IvParameterSpec iv = new IvParameterSpec(IV.getBytes("US-ASCII"));

    Cipher e_cipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
    e_cipher.init(Cipher.ENCRYPT_MODE, keySpec, iv);

    byte [] cipherText = e_cipher.doFinal(plaintext.trim().getBytes("US-ASCII"));

        System.out.println("Ciphertext: " + asHex(cipherText));
}
catch(Exception exc){
 ex.printStackTrace();
}
}
