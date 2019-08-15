public static void cryptoFunction() throws Exception
        {
            KeyStore store = KeyStore.getInstance("Windows-MY", "SunMSCAPI");
            store.load(null);
            String alias = "alias";
            Certificate cert = store.getCertificate(alias);
            PublicKey pubKey = (PublicKey) cert.getPublicKey();
            PrivateKey privKey = (PrivateKey) store.getKey(alias, "123456".toCharArray());
            Cipher ecipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            Cipher dcipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");

            ecipher.init(Cipher.ENCRYPT_MODE, pubKey);

            File userDir = new File("C:\\TestCryptoFiles");
            userDir.mkdir();

            File tmpdestFile = new File(userDir, "outFile.txt");
            File sourceFile = new File(userDir, "InFile.txt");

            int cipherMode = Cipher.ENCRYPT_MODE; //Cipher.DECRYPT_MODE

            byte[] buf = cipherMode == Cipher.ENCRYPT_MODE ? new byte[100]: new byte[128];
            int bufl;

            FileOutputStream outputWriter = new FileOutputStream(tmpdestFile);
            FileInputStream inputReader = new FileInputStream(sourceFile);         
            if(cipherMode == Cipher.ENCRYPT_MODE){
                while ((bufl = inputReader.read(buf)) != -1) {
                    byte[] encText = null;
                    encText = ecipher.doFinal(copyBytes(buf, bufl));
                    System.out.println(new String(encText));
                //  encText = dcipher.doFinal(encText);  // works well...
                    outputWriter.write(encText);
                }
            }else{
                while ((bufl = inputReader.read(buf)) != -1) {
                    byte[] encText = null;
                    encText = dcipher.doFinal(copyBytes(buf, bufl)); // throws exception Bad data...
                    System.out.println(new String(encText));
                    outputWriter.write(encText);
                }
            }
        }
     public static byte[] copyBytes(byte[] arr, int length) {
            byte[] newArr = null;
            if (arr.length == length)
                newArr = arr;
            else {
                newArr = new byte[length];
                for (int i = 0; i < length; i++) {
                    newArr[i] = (byte) arr[i];
                }
            }
            return newArr;
        }
