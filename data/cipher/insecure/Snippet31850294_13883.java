String key = MD5.getMD5("K3b2mTr3g0s1_B-m");//MD5.getMD5(key) will return MD5 hash of key passed
    byte[] raw = key.getBytes();
    SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
    Cipher cipher = Cipher.getInstance("AES");
    cipher.init(Cipher.ENCRYPT_MODE, skeySpec);

        if (inputString != null) {
            byte[] encrypted = cipher.doFinal(inputString.getBytes());
            BASE64Encoder encoder = new BASE64Encoder();
            encryptedString = encoder.encode(encrypted);
            System.out.print("encrypted string :" + encryptedString);
        }
