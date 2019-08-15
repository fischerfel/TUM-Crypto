private String AESDecrypt(byte[] keyBytes, byte[] IVBytes, byte[] input)
            throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(keyBytes, "AES"),
                new IvParameterSpec(IVBytes));

        // decrypt
        byte[] decrptedBytes = new byte[input.length];
        cipher.update(input, 0, input.length, decrptedBytes, 0);
        cipher.doFinal(decrptedBytes, 0);


        Log.i("demo", "Unencrypted MessageBytes: " + showByteArray(decrptedBytes));

        // find length of value
        int end = decrptedBytes.length;
        for (int i = 0; i < decrptedBytes.length; i++) {
            // Log.i("demo", "value: " + decrptedBytes[i]);
            if (decrptedBytes[i] == 0) {
                end = i;
                break;
            }
        }
        // Log.i("demo", "length: " + end);

        // make string in ascii
        byte[] value = Arrays.copyOf(decrptedBytes, end);
        String message = new String(value, "US-ASCII");

        return message;
    }
