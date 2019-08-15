public static void main(String[] args) throws Exception {
                            byte[] keyBytes = MessageDigest.getInstance("MD5").digest(
                                                            "som3C0o7p@s5".getBytes());
                            SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");                       
                            Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
                            IvParameterSpec ivSpec = new IvParameterSpec(new byte[96]);
                            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding", "BC");
                            byte[] block = new byte[96];
                            int i;
                            long st, et;

                            cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);

                            BufferedInputStream bIn = new BufferedInputStream(
                                                            new ProgressMonitorInputStream(null, "Encrypting ...",
                                                                                            new FileInputStream("input.txt")));
                            CipherInputStream cIn = new CipherInputStream(bIn, cipher);
                            BufferedOutputStream bOut = new BufferedOutputStream(
                                                            new FileOutputStream("output.txt"));

                            int ch;
                            while ((i = cIn.read(block)) != -1) {
                                            bOut.write(block, 0, i);
                            }
                            cIn.close();
                            bOut.close();

                            Thread.sleep(5000);


                cipher.init(Cipher.DECRYPT_MODE, key, ivSpec);
    InputStream is = new FileInputStream("output.txt");
    OutputStream os = new FileOutputStream("output2.txt");
    os = new CipherOutputStream(os, cipher);
    while ((i = is.read(block)) != -1)
    {
        os.write(block, 0, i);
    }
    is.close();
    os.close();
            }
