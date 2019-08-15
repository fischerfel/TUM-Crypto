public void RSADecrypt(String inFileName, String outFileName) {
        try {
            /* Get the encrypted message from file. */
            FileInputStream cipherfile = new FileInputStream(inFileName);

            byte[] ciphertext = new byte[cipherfile.available()];
            cipherfile.read(ciphertext);
            cipherfile.close();         
            PrivateKey privatekey =readPrivateKeyFromFile("D:\\Private.key");

            /* Create cipher for decryption. */
            Cipher decrypt_cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            decrypt_cipher.init(Cipher.DECRYPT_MODE, privatekey);
            FileOutputStream plainfile = new FileOutputStream(outFileName);
            int n = ciphertext.length / 128;
            System.out.println("len: " + n);
            byte[] data1 = new byte[128];
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < 128; j++) {
                    data1[j] = ciphertext[128 * i + j];
                }
                byte[] descryptedData = decrypt_cipher.doFinal(data1);
                plainfile.write(descryptedData);

            }

            plainfile.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
public PrivateKey readPrivateKeyFromFile(String fileName)
            throws IOException {
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        try {
            fis = new FileInputStream(new File(fileName));
            ois = new ObjectInputStream(fis);

            BigInteger modulus = (BigInteger) ois.readObject();
            BigInteger exponent = (BigInteger) ois.readObject();

            // Get Private Key
            RSAPrivateKeySpec rsaPrivateKeySpec = new RSAPrivateKeySpec(
                    modulus, exponent);
            KeyFactory fact = KeyFactory.getInstance("RSA");
            PrivateKey privateKey = fact.generatePrivate(rsaPrivateKeySpec);
            System.out.println("get key ok: " + privateKey.toString());
            return privateKey;

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (ois != null) {
                ois.close();
                if (fis != null) {
                    fis.close();
                }
            }
        }
        return null;
    }
