try
{   
    int cipherMode = Cipher.ENCRYPT_MODE;

    SecretKeySpec secretKey = ...; // generated previously using KeyGenerator       

    byte[] nonceAndCounter = new byte[16];
    byte[] nonceBytes      = ...; // generated previously using SecureRandom's nextBytes(8);

    // use first 8 bytes as nonce
    Arrays.fill(nonceAndCounter, (byte) 0);
    System.arraycopy(nonceBytes, 0, nonceAndCounter, 0, 8);

    IvParameterSpec ivSpec = new IvParameterSpec(nonceAndCounter);
    Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding");

    cipher.init(cipherMode, secretKey, ivSpec);

    File inFile  = new File(...);
    File outFile = new File(...);

    long bytesRead = 0;

    try (FileInputStream is = new FileInputStream(inFile);
         FileOutputStream os = new FileOutputStream(outFile))
    {       
        byte[] inBuf       = new byte[512 * 1024];
        byte[] outBuf      = new byte[512 * 1024];
        int    readLen     = 0;

        ByteBuffer byteBuffer = ByteBuffer.allocate(8);
        byteBuffer.putLong(bytesRead);

        while ((readLen = is.read(inBuf)) != -1)
        {
            bytesRead += readLen;

            cipher.update(inBuf, 0, readLen, outBuf, 0);

            os.write(outBuf);
        }

        cipher.doFinal(outBuf, 0);
        os.write(outBuf);
        is.close();
        os.close();
    }
    catch (Exception e) {
        System.out.printf("Exception for file: %s\n", e);
    }
} 
catch (Exception e) { 
    System.out.printf("Exception: %s\n", e);
}
