btnGenKey.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ECNamedCurveParameterSpec spec = ECNamedCurveTable.getParameterSpec("prime256v1");
            try {
                KeyPairGenerator g = KeyPairGenerator.getInstance("ECDSA","SC");
                g.initialize(spec, new SecureRandom());
                KeyPair keyPair = g.generateKeyPair();
                privateKey = keyPair.getPrivate();
                publicKey = keyPair.getPublic();
                Toast.makeText(MainActivity.this, "GEN KEY SUCCESS!!", Toast.LENGTH_SHORT).show();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    });
    btnEncrypt.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String origin = txtOrigin.getText().toString();
            try {
                Cipher c = Cipher.getInstance("ECIES","SC");
                c.init(Cipher.ENCRYPT_MODE,publicKey);
                encodeBytes = c.doFinal(origin.getBytes());
                txtEncrypt.setText(Base64.encodeToString(encodeBytes,Base64.DEFAULT));
                Toast.makeText(MainActivity.this, "ENCRYPT SUCCESS!!", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    });
    btnDecrypt.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            byte[] decodeBytes = null;
            try
            {
                Cipher c = Cipher.getInstance("ECIES","SC");
                c.init(Cipher.DECRYPT_MODE,privateKey);
                decodeBytes = c.doFinal(encodeBytes);
                String deCrypt = new String(decodeBytes,"UTF-8");
                txtDecrypt.setText(deCrypt);
                Toast.makeText(MainActivity.this, "DECRYPT SUCCESS!!", Toast.LENGTH_SHORT).show();
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }
    });
