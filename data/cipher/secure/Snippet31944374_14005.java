public static void decrypt(String originalZipFileName, String newZipFileName, String privateKeyFileName) throws Exception {
    byte[] buffer = new byte[128];  

    ZipFile originalZipFile = new ZipFile(originalZipFileName); 
    ZipOutputStream newZipFile = new ZipOutputStream(new FileOutputStream(newZipFileName));

    Enumeration<? extends ZipEntry> zipEntries = originalZipFile.entries();

    String privateKey = getKeyString(privateKeyFileName);
    PrivateKey key = makePrivateKey(privateKey);

    Cipher cipher = Cipher.getInstance("RSA");

    cipher.init(Cipher.DECRYPT_MODE, key);


    while(zipEntries.hasMoreElements()){

        ZipEntry entry = zipEntries.nextElement();          

        ZipEntry copy = new ZipEntry(entry.getName());      
        newZipFile.putNextEntry(copy);


        InputStream inputEntry = originalZipFile.getInputStream(entry);


        while(inputEntry.read(buffer) != -1){
            newZipFile.write(cipher.doFinal(buffer));
        }

        newZipFile.closeEntry();
        inputEntry.close();
    }
    newZipFile.close();
    originalZipFile.close();
}
