public class StrongAES {

    byte[] input;
    byte[] cipher_Text;
    SecretKeySpec Key;
    int ctLength;

    public byte[] encrypt_Data(String data){
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        byte[] input = data.getBytes();
        this.input = input;
        byte[] keyBytes = new byte[] {'T', 'h', 'i', 's', 'I', 's', 'A', 'S', 'e', 'c', 'r', 'e', 't', 'K', 'e', 'y'};

        SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");
        this.Key = key;
        byte[] cipherText = null;
        Cipher cipher;
        try {
            cipher = Cipher.getInstance("AES/ECB/PKCS7Padding", "BC");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            cipherText = new byte[cipher.getOutputSize(input.length)];
            int ctLength = cipher.update(input, 0, input.length, cipherText, 0);
            ctLength += cipher.doFinal(cipherText, ctLength);
            this.cipher_Text = cipherText;
            this.ctLength = ctLength;
        ....    
        ....
        return cipherText;
    }
    public int count_Cipher(){
        return this.ctLength;   
    }
