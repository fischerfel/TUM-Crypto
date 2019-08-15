public void login() {
        LoginData loginData = this.fragmentBox.getUpdatedLoginData();

        String finishString = new String();
        try {
            Cipher cipher = Cipher.getInstance("AES");
            byte[] output = null;

            SecretKeySpec secretKey = new SecretKeySpec(
                    Globals.ENCRYPTPW.getBytes(), "AES");

            System.out.println(secretKey.getEncoded().toString() +  "----" + Globals.ENCRYPTPW);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);

            output = cipher.doFinal(loginData.getPassword().getBytes());

            byte[] encryptedUsernameString = Base64.encode(output, 0);
            finishString = new String(encryptedUsernameString, "UTF-8");

        } catch (InvalidKeyException e) {
            e.getStackTrace();
            Log.v("Fehler im Code","");
        } catch(Exception e){}


        System.out.println("Code: " + finishString);
}
