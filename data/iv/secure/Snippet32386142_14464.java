          try {
        String dataAlgorithm =  JCEMapper.translateURItoJCEID(tmp);
        decryptor = Cipher.getInstance(dataAlgorithm);

        //decryptor = Cipher.getInstance("DESede/CBC/ISO10126Padding");

        int ivLen = decryptor.getBlockSize();
        byte[] ivBytes = new byte[ivLen];

        System.arraycopy(cipherInput, 0, ivBytes, 0, ivLen);
        if (dataAlgorithm.matches(".*[gG][cC][mM].*$")) { // TK 03/09/2015 - probably needs more places for decrypting body stuff
          GCMParameterSpec iv = new GCMParameterSpec(ivLen * Byte.SIZE, ivBytes);
          decryptor.init(Cipher.DECRYPT_MODE, symmetricKey, iv);
        }
        else {
          IvParameterSpec iv = new IvParameterSpec(ivBytes);
          decryptor.init(Cipher.DECRYPT_MODE, symmetricKey, iv); <===== old line 761
        }

        cipherOutput = decryptor.doFinal(cipherInput, ivLen, cipherInput.length-ivLen);
      } catch (Exception e) {
        log.log(Level.SEVERE, "WSS1232.failedto.decrypt.attachment", e);
        throw new XWSSecurityException(e);
      }
