try {
        cipher = Cipher.getInstance("AES/CBC/PKCS5Padding", "BC");
        byte[] key = { 107, -39, 87, -65, -1, -28, -85, -94, 105, 76, -94,
                110, 48, 116, -115, 86 };
        byte[] vector = { -94, 112, -23, 93, -112, -58, 18, 78, 1, 69, -92,
                102, 33, -96, -94, 59 };
        SecretKey aesKey = new SecretKeySpec(key, "AES");
        byte[] message = { 32, -26, -72, 25, 63, 114, -58, -5, 4, 90, 54,
                88, -28, 3, -72, 25, -54, -60, 17, -53, -27, -91, 34, -101,
                -93, -3, -47, 47, -12, -35, -118, -122, -77, -7, -9, -123,
                7, -66, 10, -93, -29, 4, -60, -102, 16, -57, -118, 94 };

        IvParameterSpec aesVector = new IvParameterSpec(vector);
        cipher.init(Cipher.DECRYPT_MODE, aesKey, aesVector);
        byte[] wynik = cipher.doFinal(message);
        Log.d("Solution here", "Solution");
        for (byte i : wynik)
            Log.d("Solution", "" + i);
    } catch (Exception e) {
        Log.d("ERROR", "TU");
        e.printStackTrace();
    }
