enter code here private static Key generateKey() throws Exception 
{
        Key key = new SecretKeySpec(keyValue, algorithm);
        return key;
}

// performs encryption & decryption 
public static void main(String[] args) throws Exception 
{
        String plainText = "1234";
        String encryptedText = LoginAuthentication.encrypt(plainText);
        String decryptedText = LoginAuthentication.decrypt(encryptedText);

        System.out.println("Plain Text : " + plainText);
        System.out.println("Encrypted Text : " + encryptedText);
        System.out.println("Decrypted Text : " + decryptedText);
}
