public class StrongAES {
            public void encrypt_Data(){

        }

    public String decrypt_Data(byte[] cipherText, int ctLength) throws InvalidAlgorithmParameterException{

        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());

        byte[] keyBytes = new byte[] {'T', 'h', 'i', 's', 'I', 's', 'A', 'S', 'e', 'c', 'r', 'e', 't', 'K', 'e', 'y'};
        SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");
        System.out.println("byte length - "+cipherText.length);
        Cipher cipher;
        byte[] plainText = null;
        try {
            cipher = Cipher.getInstance("AES/ECB/PKCS7Padding","BC");
            cipher.init(Cipher.DECRYPT_MODE, key);

            //decrypt
            plainText = new byte[cipher.getOutputSize(ctLength)];
            System.out.println("cipherText - "+cipherText+" ctLength - "+ctLength+" plainText - "+plainText);
            int ptLength = cipher.update(cipherText, 0, ctLength, plainText,0);
            System.out.println("ptLength - "+ptLength);

            ptLength += cipher.doFinal(plainText, ptLength);

            return new String(plainText);
