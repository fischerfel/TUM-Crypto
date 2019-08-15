    public void send( OutputStream op, 
                        FileInputStream filetoprocess, 
                        long l) throws Throwable
{

    byte[] inputBytes = new byte[(int) l];
    int iRead = filetoprocess.read(inputBytes);
    if (iRead != l)
    {
        System.out.println("Read error.");
        return;
    }
    byte[] ivBytes = "1234567812345678".getBytes();
    DESKeySpec desKeySpec = new DESKeySpec(ivBytes);
    SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
    SecretKey sKey = keyFactory.generateSecret(desKeySpec);
    Cipher ecipher = Cipher.getInstance("DES");
    ecipher.init(Cipher.ENCRYPT_MODE, sKey);
    byte[] outputBytes = ecipher.doFinal(inputBytes);

    // Send the file's size, 4 bytes.
    // Use an 8 byte buffer to send big files > 2GB.
    byte[] fileSize = new byte[4];
    fileSize[0] = (byte) ((iRead & 0xff000000) >> 24);
    fileSize[1] = (byte) ((iRead & 0x00ff0000) >> 16);
    fileSize[2] = (byte) ((iRead & 0x0000ff00) >>  8);
    fileSize[3] = (byte)  (iRead & 0x000000ff);
    op.write(fileSize, 0, 4);

    // Now send the file's data
    op.write(outputBytes);
    op.flush();

    System.out.println("File sent");

}

public static void receive(
                    InputStream ip, 
                    File fname, 
                    PrintWriter output2) throws Throwable
{

    byte[] ivBytes = "1234567812345678".getBytes();

    DESKeySpec desKeySpec = new DESKeySpec(ivBytes);
    SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
    SecretKey sKey = keyFactory.generateSecret(desKeySpec);

    Cipher dcipher = Cipher.getInstance("DES");
    dcipher.init(Cipher.DECRYPT_MODE, sKey);

    ByteArrayOutputStream out = new ByteArrayOutputStream();

    // Receive the file's size first  
    byte[] bSize = new byte[4];
    ip.read(bSize, 0, 4);
    int fileSize=0;
    fileSize = (int) (bSize[0]) << 24 | (int) (bSize[1]) << 16 | 
               (int) (bSize[2]) << 8 | (int) (bSize[3]);

    // use a 4 or 8K buffer for better performance
    byte[] buffer = new byte[8*1024];
    int length;

    // Read up to the file size
    while (fileSize > 0)
    {
        if (fileSize > buffer.length) length=buffer.length;
        else length=fileSize;
        int iRead = ip.read(buffer, 0, length);
        if (iRead > 0) 
        {
            out.write(buffer, 0, iRead);
            fileSize -= iRead;              
        }
    }

    byte[] result = out.toByteArray();

    byte[] outputBytes = dcipher.doFinal(result);

    FileOutputStream outputStream = new FileOutputStream(fname);
    outputStream.write(outputBytes);
    outputStream.close();

    System.out.println("File received");

}
