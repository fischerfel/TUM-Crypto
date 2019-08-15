private void btnEncryptActionPerformed(java.awt.event.ActionEvent evt) {                                           
        try
        {
            Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
            input = txtPlain.getText().getBytes();
            SecretKeySpec key = new SecretKeySpec(keyBytes, "DES"); 
            IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);
            cipher = Cipher.getInstance("DES/CTR/NoPadding", "BC");
            cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);
            cipherText = new byte[cipher.getOutputSize(input.length)];
            ctLength = cipher.update(input, 0, input.length, cipherText, 0);
            ctLength += cipher.doFinal(cipherText, ctLength);
            txtEncrypt.setText(new String(cipherText));
            System.out.println("cipher: " + new String(cipherText));
        }
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(null, e);
        }
    } 
