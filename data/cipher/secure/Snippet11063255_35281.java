public void decryptFile(String fileName,PrivateKey privateKey) throws Exception {
    Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
    cipher.init(Cipher.DECRYPT_MODE, privateKey);
    FileInputStream fis = new FileInputStream(fileName);
    File file=new File("decryptedfile.xml");
    if(file.exists()) {
        file.delete();
    }
            FileOutputStream fos = new FileOutputStream("decryptedfile.xml");
    CipherInputStream cis = new CipherInputStream(fis, cipher);
    int i;
    byte[] block = new byte[32];
    //System.out.println("Read : "+cis.read(block));
    while ((i = cis.read(block)) != -1) {
        System.out.println(String.valueOf(i));
        fos.write(block, 0, i);
    }
    fos.close();
}
