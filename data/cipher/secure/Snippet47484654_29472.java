{
        byte[] data = Base64.decode(s1.getBytes(),Base64.DEFAULT);

        Log.e(TAG, "RSADecrypt1: byte "+encryptedByte );
        cipher1 = Cipher.getInstance("RSA");
        cipher1.init(Cipher.DECRYPT_MODE, privateKey);
        decryptedBytes = cipher1.doFinal(data);

        decrypted = new String(decryptedBytes);
        System.out.println("DDecrypted?????" + decrypted);
//        String encodedString = Base64.decode(decryptedBytes, Base64.NO_WRAP);
//        Log.e(TAG, "RSADecrypt1: encodedString  "+encodedString );

        System.out.println("DDecrypted????? dejdejednjdnn   " + Base64.encodeToString(decryptedBytes,0,decryptedBytes.length,Base64.DEFAULT));

        Log.e(TAG, "RSADecrypt1: woww "+Base64.encodeToString(decryptedBytes, Base64.DEFAULT) );
        return decrypted;
    }
