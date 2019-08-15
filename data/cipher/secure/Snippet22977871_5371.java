                        file = x;
                        FileInputStream fis = new FileInputStream(file.getAbsolutePath());

                        file = new File(file.getAbsolutePath().substring(0,
                                file.getAbsolutePath().length() - 4));

                        FileOutputStream fos = new FileOutputStream(file);

                        byte k[] = Hash.MD5(password).getBytes("UTF-8");
                        SecretKeySpec key = new SecretKeySpec(k, "AES");

                        Cipher cipher = Cipher.getInstance(algorithm);

                        byte[] iv = batchIV;
                        IvParameterSpec ivSpec = new IvParameterSpec(iv);

                        cipher.init(Cipher.DECRYPT_MODE, key, ivSpec);
                        CipherInputStream cin = new CipherInputStream(fis,
                                cipher);

                        byte[] buffer = new byte[1024];
                        int read = 0;

                        while ((read = cin.read(buffer)) != -1) {
                            fos.write(buffer, 0, read);
                        }

                        fos.flush();
                        fos.close();
                        cin.close(); 
