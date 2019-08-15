public void crypt() throws NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException, InvalidKeyException, IOException{
    Cipher cipher = Cipher.getInstance("AES");
    // Encrypt

    byte [] data = {90, 52, 50, 52, 48, 54, 54, 51, 52, 51, 50, 51, 49, 51, 54, 51};
    System.out.println(Arrays.toString(data));
    SecretKey originalKey = new SecretKeySpec(data, 0, data.length, "AES");
    System.out.println(Arrays.toString(originalKey.getEncoded())+data);
    cipher.init(Cipher.ENCRYPT_MODE, originalKey);
    System.out.println(Arrays.toString(originalKey.getEncoded())+data);
    String cleartextFile = this.lien;
    String ciphertextFile = this.lien;

    FileInputStream fis = new FileInputStream(cleartextFile);
    FileOutputStream fos = new FileOutputStream(ciphertextFile);
    CipherOutputStream cos = new CipherOutputStream(fos, cipher);

    int i;
    while ((i = fis.read()) != -1) {
        cos.write(i);
    }
    cos.close();
}

    // Decrypt
public void decrypt() throws NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException, InvalidKeyException, IOException{

        //csetCodeCrypt(result.getBytes(6));
        byte [] data = {90, 52, 50, 52, 48, 54, 54, 51, 52, 51, 50, 51, 49, 51, 54, 51};
    Cipher cipher = Cipher.getInstance("AES");
    SecretKey originalKey = new SecretKeySpec(data, 0, data.length, "AES");
    System.out.println(Arrays.toString(originalKey.getEncoded()));
    cipher.init(Cipher.DECRYPT_MODE, originalKey);
    System.out.println(Arrays.toString(originalKey.getEncoded()));
    String cleartextFile = this.lien;
    String ciphertextFile = this.lien;

    FileInputStream fis = new FileInputStream(ciphertextFile);
    FileOutputStream fos = new FileOutputStream(cleartextFile);
    CipherOutputStream cos = new CipherOutputStream(fos, cipher);

    int i;
    while ((i = fis.read()) != -1) {
        cos.write(i);
    }
    cos.close();
}

}
