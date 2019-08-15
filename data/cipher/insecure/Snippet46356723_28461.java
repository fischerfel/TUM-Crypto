     final String ALGORITHM = "blowfish";
     String keyString = "DesireSecretKey";

    private void encrypt(String file) throws Exception {

        File extStore = Environment.getExternalStorageDirectory();
        File inputFile = new File(file);
        File encryptedFile = new 
        File(extStore+"/Movies/encryptAudio.amr");
        doCrypto(Cipher.ENCRYPT_MODE, inputFile, encryptedFile);
     }



     private  void doCrypto(int cipherMode, File inputFile,
                                 File outputFile) throws Exception {

        Key secretKey = new 
        SecretKeySpec(keyString.getBytes(),ALGORITHM);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(cipherMode, secretKey);

        FileInputStream inputStream = new FileInputStream(inputFile);
        byte[] inputBytes = new byte[(int) inputFile.length()];
        inputStream.read(inputBytes);

        byte[] outputBytes = cipher.doFinal(inputBytes);

        FileOutputStream outputStream = new 
        FileOutputStream(outputFile);
        outputStream.write(outputBytes);

        inputStream.close();
        outputStream.close();

    }
