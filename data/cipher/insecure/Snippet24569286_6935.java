String newPath = sdCardPath + "/" + dPdfName;
                File f1 = new File(newPath);
                if (!f1.exists())
                    try {
                        f1.createNewFile();
                    } catch (IOException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }

                try {
                    InputStream fis = new FileInputStream(f);
                    OutputStream fos = new FileOutputStream(f1);
                    String seed = "password";
                    byte[] rawKey = getRawKey(seed.getBytes());
                    SecretKeySpec skeySpec = new SecretKeySpec(rawKey,
                            "AES");
                    Cipher cipher = Cipher.getInstance("AES");
                    cipher.init(Cipher.DECRYPT_MODE, skeySpec);

                    fis = new CipherInputStream(fis, cipher);
                    int b;
                    byte[] data = new byte[4096];
                    while ((b = fis.read(data)) != -1) {
                        // fos.write(cipher.doFinal(data), 0, b);
                        fos.write(data, 0, b);
                    }
                    fos.flush();
                    fos.close();
                    fis.close();

                } catch (Exception e) {
                    // TODO: handle exceptionpri
                    e.printStackTrace();
                }
