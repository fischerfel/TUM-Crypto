btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String plainTextPassword=editText1.getText().toString();
                String encrypted = "";
                try{
                    DESKeySpec keySpec = new DESKeySpec("qwertykey".getBytes());
                    SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
                    SecretKey key = keyFactory.generateSecret(keySpec);
                    byte[] cleartext = plainTextPassword.getBytes();

                    Cipher cipher = Cipher.getInstance("DES/CBC/ZeroBytePadding", "BC");; // cipher is not thread safe
                    cipher.init(Cipher.ENCRYPT_MODE, key);
                    encrypted = Base64.encodeToString(cipher.doFinal(cleartext),Base64.DEFAULT);

              } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (InvalidKeyException e) {
                    e.printStackTrace();
                } catch (NoSuchPaddingException e) {
                    e.printStackTrace();
                } catch (BadPaddingException e) {
                    e.printStackTrace();
                } catch (InvalidKeySpecException e) {
                    e.printStackTrace();
                } catch (IllegalBlockSizeException e) {
                    e.printStackTrace();
                } catch (NoSuchProviderException e) {
                    e.printStackTrace();
                }
                textView4.setText(encrypted.toString());

            }});
