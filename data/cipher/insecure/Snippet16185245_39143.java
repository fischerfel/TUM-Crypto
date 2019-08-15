public static void encryptOrDecrypt(int mode, OutputStream os, InputStream is, String key) throws Throwable {

    IvParameterSpec l_ivps;
    l_ivps = new IvParameterSpec(IV, 0, IV.length);

    DESKeySpec dks = new DESKeySpec(key.getBytes());
    SecretKeyFactory skf = SecretKeyFactory.getInstance("DES");
    SecretKey desKey = skf.generateSecret(dks);
    Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding"); 

    if (mode == Cipher.ENCRYPT_MODE) {
        cipher.init(Cipher.ENCRYPT_MODE, desKey,l_ivps);    
        CipherInputStream cis = new CipherInputStream(is, cipher);
        doCopy(cis, os);
    } else if (mode == Cipher.DECRYPT_MODE) {
        cipher.init(Cipher.DECRYPT_MODE, desKey,l_ivps);            
        CipherInputStream cis = new CipherInputStream(is, cipher);                  
        doCopy(cis, os);
        System.out.println("Decrypted");
    }
}

public static void doCopy(InputStream is, OutputStream os) throws IOException {
    byte[] bytes = new byte[64];
    int numBytes;
    System.out.println("doCopy Step1");
    System.out.println("is: "+is.read(bytes));
    while ((numBytes = is.read(bytes)) != -1) {
        os.write(bytes, 0, numBytes);
        System.out.println("doCopy Step2");
    }
    os.flush();
    os.close();
    is.close();
}

public static void writeFile(InputStream in){
    try {
        String strContent;          
        BufferedReader bReader = new BufferedReader(new InputStreamReader(in));
        StringBuffer sbfFileContents = new StringBuffer();
        String line = null;

        while( (line = bReader.readLine()) != null){
            sbfFileContents.append(line);
        }
        System.out.println("File:"+sbfFileContents);            
    } catch (FileNotFoundException e) {
        e.printStackTrace();
    } catch (IOException ioe){

    }
}
