if(dflag == 1) {
     //AES-128 bit key initialization.
     System.out.println("File completely received");
     byte[] keyvalue = "AES128PeerBuLLet".getBytes();
     Key key = new SecretKeySpec(keyvalue, "AES");

     //Initialization Vector initialized
     IvParameterSpec ivParameterSpec = null;

     //Cipher Initialization.
     Cipher decCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
      try {
           decCipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));
     } catch (InvalidAlgorithmParameterException ex) {
                        Logger.getLogger(PeersController.class.getName()).log(Level.SEVERE, null, ex);
      }
      System.out.println(decCipher.getProvider().getInfo());

      //Decryption Mechanism.
      try (FileOutputStream stream = new FileOutputStream(decrypted)) {
             try (FileInputStream fis = new FileInputStream(encrypted)) {
                    try (CipherInputStream cis = new CipherInputStream(fis, decCipher)) {
                           int read, i = 0;
                           byte[] buffer = new byte[(1024 * 1024) + 16];
                           while ((read = cis.read(buffer)) != -1) {
                                    stream.write(buffer, 0, read);
                                    i = i + read;
                                    double d = (double) i / len;
                                    double progress = new BigDecimal(d).setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue();
                                    Platform.runLater(() -> {
                                        pBar.setProgress(progress);
                                        progressText.setText("Decrypting..");
                                    });
                                }
                            } catch (Exception e) {
                                System.out.println(e.getMessage());
                            }
                        }
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
        }
