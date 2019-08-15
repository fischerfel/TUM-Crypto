    SecretKeySpec   incorrectKey = new SecretKeySpec(keyBytes, "AES");
    IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);
    Cipher          cipher = Cipher.getInstance("AES/GCM/NoPadding", "BC");
    byte[] block = new byte[1048576];
    int i;

    cipher.init(Cipher.DECRYPT_MODE, incorrectKey, ivSpec);

    BufferedInputStream fis=new BufferedInputStream(new ProgressMonitorInputStream(null,"Decrypting ...",new FileInputStream("file.enc")));
    BufferedOutputStream ro=new BufferedOutputStream(new FileOutputStream("file_org"));        
    CipherOutputStream dcOut = new CipherOutputStream(ro, cipher);

    while ((i = fis.read(block)) != -1) {
        dcOut.write(block, 0, i);
    }

    dcOut.close();
    fis.close();
