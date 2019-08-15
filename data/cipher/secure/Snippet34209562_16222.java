public static void main(String[] args) {
    String text = "base64encodedstring";
    try {
        Security.addProvider(new BouncyCastleProvider());
        decode(text);
        PublicKey pubKey=getKey(text);
        byte[] input = "plaintext".getBytes();
        Cipher cipher = Cipher.getInstance("RSA/None/PKCS1Padding", "BC");
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);
        byte[] cipherText = cipher.doFinal(input);
        System.out.println("cipher: " + new String(cipherText));

    } catch (Exception e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
}
