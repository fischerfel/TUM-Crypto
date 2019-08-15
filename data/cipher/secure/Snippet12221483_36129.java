    SecretKeySpec   key = new SecretKeySpec(keyBytes, "AES");
    IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);
    Cipher          cipher = Cipher.getInstance("AES/CBC/PKCS5Padding", "BC");
    byte[] block = new byte[8];
    int i;

    cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);

    BufferedInputStream bIn=new BufferedInputStream(new ProgressMonitorInputStream(null,"Encrypting ...",new FileInputStream("input")));
    CipherInputStream       cIn = new CipherInputStream(bIn, cipher);
    BufferedOutputStream bOut=new BufferedOutputStream(new FileOutputStream("output.enc"));

    int ch;
    while ((i = cIn.read(block)) != -1) {
        bOut.write(block, 0, i);
    }
    cIn.close();
    bOut.close();
