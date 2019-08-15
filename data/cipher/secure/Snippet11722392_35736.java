public static void encryptDecryptFile(String srcFileName,
            String destFileName, Key key, int cipherMode) throws Exception {
        OutputStream outputWriter = null;
        InputStream inputReader = null;     
        try {                   
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");         
            byte[] buf = cipherMode == Cipher.ENCRYPT_MODE ? new byte[100]
                    : new byte[128];
            int bufl;
            cipher.init(cipherMode, key);           
            outputWriter = new FileOutputStream(destFileName);
            inputReader = new FileInputStream(srcFileName);
            while ((bufl = inputReader.read(buf)) != -1) {          
                byte[] encText = null;
                if (cipherMode == Cipher.ENCRYPT_MODE)
                    encText = encrypt(copyBytes(buf, bufl), (PublicKey) key);
                else
                    encText = decrypt(copyBytes(buf, bufl), (PrivateKey) key);              
                outputWriter.write(encText);
            }           
        } catch (Exception e) {e.printStackTrace();
            throw e;
        } finally {
            try {
                if (outputWriter != null)
                    outputWriter.close();
                if (inputReader != null)
                    inputReader.close();
            } catch (Exception e) {
            }
        }
    }
