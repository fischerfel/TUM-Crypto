try {
        BufferedReader inStream = new BufferedReader (new FileReader(cryptoFile));

        int k = 0;

        fileContents.add(inStream.readLine());

        while(fileContents.get(k) != null) {
            k++;
            fileContents.add(inStream.readLine());
        }

        inStream.close();

        try {

            PrivateKey privateKey = kp.getPrivate();
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);

            int j = 0;

            while(fileContents.get(j) != null) {

                String text = fileContents.get(j);

                try {                        
                    x = Hex.decodeHex(text.toCharArray());
                    y = cipher.doFinal(x);
                } catch (DecoderException ex) {
                    Logger.getLogger(Crypto.class.getName()).log(Level.SEVERE, null, ex);
                }

                try (PrintWriter file = new PrintWriter(
                        new BufferedWriter(
                        new FileWriter("DecryptedFile.txt", true)))) {
                    file.println(y);
                } catch (IOException e) {
                    System.err.println("IOERROR: " + e.getMessage() + "\n");
                }

                j++;
            }

        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException ex) {
            Logger.getLogger(Crypto.class.getName()).log(Level.SEVERE, null, ex);
        }

    } catch (FileNotFoundException e) {
        System.err.println("IOERROR: File NOT Found: " + cryptoFile + "\n");
    } catch ( IOException e ) {
        System.err.println("IOERROR: " + e.getMessage() + "\n");
    } finally {
        messagePane.setText(messagePane.getText() + "\n\n"
                + cryptoFile + "is done being decrypted.");
        messagePane.setText(messagePane.getText() + "\n"
                + "Decrypted file saved to \'DecryptedFile.txt\'.");

        cryptoFile = "";
        pathTextField.setText(cryptoFile);
        encryptButton.setEnabled(false);
        decryptButton.setEnabled(false);

    }
