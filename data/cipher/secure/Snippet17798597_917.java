public byte[] cifra(Object msjO, InputStream fileNamePrivateKey) throws Exception {

    try {

        ObjectInputStream restore = new ObjectInputStream(fileNamePrivateKey);
        PublicKey llave = (PublicKey) restore.readObject();
        String msj = (String)msjO;          
        restore.close();
        if (llave != null) {

            // get an RSA cipher object and print the provider
            final Cipher cipher = Cipher.getInstance("RSA");
            // encrypt the plain text using the public key
            cipher.init(Cipher.ENCRYPT_MODE, llave);
            byte[] encrypted = cipher.doFinal(msj.getBytes());


            return encrypted;
        }
    } catch (Exception e) {
        e.printStackTrace();
    }

    return null;
}
