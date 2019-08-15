public static void fileEncripWrapped(File in, File out, PublicKey pub, byte [] key) {  

    try {
        SecretKeySpec keySpec = new SecretKeySpec(key, "AES");

        //Encrypting wrapped key      
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.WRAP_MODE, pub);
        byte[] encKey = cipher.wrap(keySpec);

        FileOutputStream osAppend  = new FileOutputStream(out);

        osAppend.write(encKey);
        osAppend.close();


        // Crypting the file 
         cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
        cipher.init(Cipher.ENCRYPT_MODE, keySpec);

        FileInputStream is = new FileInputStream(in);
        CipherOutputStream os = new CipherOutputStream(new FileOutputStream(out, true), cipher);

        copy(is, os);

        is.close();
        os.close();

    } catch (Exception ex) {
        System.err.println("Ha succeït un error xifrant: " + ex);
    }
}
