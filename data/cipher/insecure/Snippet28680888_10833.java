private void encrypt() {
    if (this.sks == null){
        // make toast and tell user to generate the key
    } else {
        try {
            Cipher c = Cipher.getInstance("AES");
            // use the class key spec to encrypt
            c.init(Cipher.ENCRYPT_MODE, this.sks);
            encodedBytes = c.doFinal(getData_str.getBytes());
            String encoded = Base64
                    .encodeToString(encodedBytes, Base64.DEFAULT);
            System.out.println(" " + encoded);
        } catch (Exception e) {
            Log.e(TAG, "AES encryption error");
        }
    }
}
