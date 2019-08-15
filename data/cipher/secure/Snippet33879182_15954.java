Security.addProvider(new com.sun.crypto.provider.SunJCE());
..

cipher = Cipher.getInstance("AES/ECB/ISO10126Padding");
