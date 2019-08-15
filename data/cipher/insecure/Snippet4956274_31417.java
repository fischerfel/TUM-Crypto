public String Encryption(String toEncrypt) throws Exception
{
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        EditText et = (EditText) findViewById(R.id.entry);
        byte[] input = toEncrypt.getBytes();
        byte[] keyBytes = "hello".getBytes();

        //et.setText("in encryption");
        SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS7Padding", "BC");

        //et.setText("in encryption");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        et.setText("in encryption");
        byte[] cipherText = new byte[cipher.getOutputSize(input.length)];
        int ctLength = cipher.update(input, 0, input.length, cipherText, 0);
        ctLength += cipher.doFinal(cipherText, ctLength);
        //et.setText("in encryption");
        //return "abc";
        return cipherText.toString();
}
