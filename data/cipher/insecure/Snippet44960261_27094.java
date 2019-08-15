           try {                    
                InputStream tis = getApplicationContext().getResources().openRawResource(R.raw.devdb_enc);
                FileOutputStream fos = new FileOutputStream(new File("file_to_be_decrypted"));

                SecretKeySpec sks = new SecretKeySpec("MyPasswordDiff".getBytes(), "AES");
                Cipher cipher = Cipher.getInstance("AES");
                cipher.init(Cipher.DECRYPT_MODE, sks);
                CipherInputStream cis = new CipherInputStream(tis, cipher);
                int b;
                byte[] d = new byte[8];
                while ((b = cis.read(d)) != -1) {
                    fos.write(d, 0, b);
                }
                fos.flush();
                fos.close();
                cis.close();
                isDecryptSucsess = true;


            } catch (NoSuchAlgorithmException e) {
                Utils.showLogger(e);
            } catch (NoSuchPaddingException e) {
                Utils.showLogger(e);
            } catch (InvalidKeyException e) {
                Utils.showLogger(e);
            } catch (IOException e) {
                Utils.showLogger(e);
            }catch (Exception e) {
                Utils.showLogger(e);
            }
