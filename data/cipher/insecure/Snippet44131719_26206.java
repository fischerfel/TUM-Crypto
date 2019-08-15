 if (!isEncrypted) {
                        FileInputStream fis = new FileInputStream(path);
                        // This stream write the encrypted text. This stream will be wrapped by another stream.
                        FileOutputStream fos = new FileOutputStream(path + ".abcd");

                        // Length is 16 byte
                        SecretKeySpec sks = new SecretKeySpec("abcdefghijklmnop".getBytes(), "AES");
                        // Create cipher
                        Cipher cipher = Cipher.getInstance("AES");
                        cipher.init(Cipher.ENCRYPT_MODE, sks);
                        // Wrap the output stream
                        CipherOutputStream cos = new CipherOutputStream(fos, cipher);
                        // Write bytes
                        int b;
                        byte[] d = new byte[1024];
                        try {
                            while ((b = fis.read(d)) != -1) {
                                cos.write(d, 0, b);
                            }

                            // Flush and close streams.
                            cos.flush();
                            cos.close();
                            fis.close();
                            new File(path).deleteOnExit();
                            isEncrypted = true;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    btnEncrypt.setText("Decrypt Path");
                                    deleteMyFile(path);
                                }
                            });
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    } else {
                        FileInputStream fis = new FileInputStream(path + ".abcd");

                        FileOutputStream fos = new FileOutputStream(path);
                        SecretKeySpec sks = new SecretKeySpec("abcdefghijklmnop".getBytes(), "AES");
                        Cipher cipher = Cipher.getInstance("AES");
                        cipher.init(Cipher.DECRYPT_MODE, sks);
                        CipherInputStream cis = new CipherInputStream(fis, cipher);
                        int b;
                        byte[] d = new byte[1024];


                        while ((b = cis.read(d)) != -1) {
                            fos.write(d, 0, b);
                        }
                        fos.flush();
                        fos.close();
                        cis.close();
                        isEncrypted = false;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                deleteMyFile(path + ".abcd");
                                btnEncrypt.setText("Encrypt Path");
                            }
                        });

                    }
                } catch (final Exception e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.setMessage(e.getMessage());
                        }
                    });
