        try {
        String dataAlgorithm =  JCEMapper.translateURItoJCEID(tmp);
        decryptor = Cipher.getInstance(dataAlgorithm);

        //decryptor = Cipher.getInstance("DESede/CBC/ISO10126Padding");

        int ivLen = decryptor.getBlockSize();
        byte[] ivBytes = null; // = new byte[ivLen];

        if (dataAlgorithm.matches(".*[gG][cC][mM].*$")) { // TK 03/09/2015 - probably needs more places for decrypting body stuff
          ivLen = 12; // 12 for GCM - also see wss4j-2.1.2/ws-security-common/src/main/java/org/apache/wss4j/common/util/AttachmentUtils.java
          ivBytes = new byte[ivLen];
          System.arraycopy(cipherInput, 0, ivBytes, 0, ivLen);
          GCMParameterSpec iv = new GCMParameterSpec(16 * Byte.SIZE, ivBytes);
          decryptor.init(Cipher.DECRYPT_MODE, symmetricKey, iv);
        }
        else {
          ivBytes = new byte[ivLen];
          System.arraycopy(cipherInput, 0, ivBytes, 0, ivLen);
          IvParameterSpec iv = new IvParameterSpec(ivBytes);
          decryptor.init(Cipher.DECRYPT_MODE, symmetricKey, iv);
        }

        cipherOutput = decryptor.doFinal(cipherInput, ivLen, cipherInput.length-ivLen);
    } catch (Exception e) {
        log.log(Level.SEVERE, "WSS1232.failedto.decrypt.attachment", e);
        throw new XWSSecurityException(e);
    }
