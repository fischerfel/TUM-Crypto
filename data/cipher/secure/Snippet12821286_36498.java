   //**The main function:**

File inputfile = new File(fileToEncrypt); // I receive fileToEncrypt for the function.

try {
    inputbytes = readFromFile(inputfile);
} 

catch (IOException e) {
    Toast.makeText(this, "ERROR READING", Toast.LENGTH_LONG).show();
}

byte[] outputbytes = rsaEncrypt(inputbytes, raiz.getAbsolutePath()+"/PublicKeyFile.key");
byte[] inputbytes = rsaDecrypt(outputbytes, raiz.getAbsolutePath()+"/Privada.key");


    //**Used functions**

    public byte[] rsaEncrypt(byte[] src, String direccion) {
    try {
        File archivo_llave_publica = new File(direccion);
        byte[] bytes_llave = leer(archivo_llave_publica);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");          
        EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(bytes_llave);
        PublicKey pubKey = keyFactory.generatePublic(publicKeySpec);
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);
        byte[] cipherData = cipher.doFinal(src);
        return cipherData;
    } catch (Exception e) {
        Toast.makeText(this, "Error encriptando!!", Toast.LENGTH_LONG).show();
    }
    Toast.makeText(this, "ERROR ENCRYPT!", Toast.LENGTH_LONG).show();
    return null;
}

public byte[] rsaDecrypt(byte[] src, String direccion) {
    try {
        File archivo_llave_privada = new File(direccion);
        byte[] bytes_llave = leer(archivo_llave_privada);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");          
        EncodedKeySpec privateKeySpec = new X509EncodedKeySpec(bytes_llave);
        PrivateKey priKey = keyFactory.generatePrivate(privateKeySpec);
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, priKey);
        byte[] cipherData = cipher.doFinal(src);
        return cipherData;
    } catch (Exception e) {
        Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
    }
    return null;
}

public byte[] readFromFile(File archivo) throws IOException{           
    FileInputStream lector = new FileInputStream(archivo);
    long length = archivo.length();
    byte[] input = new byte[(int) length];
    for (int i=0; i<(int)length; i++)
    {
        input[i]=(byte) lector.read();
    }
    lector.close();
        return input;
 }
