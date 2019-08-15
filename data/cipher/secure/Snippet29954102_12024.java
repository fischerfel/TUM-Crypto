        protected void onPostExecute(String file_url) {

            pDialog.dismiss(); 
            String privKeyPEM = sPK.replace("-----BEGIN PRIVATE KEY-----\r\n", "");
            privKeyPEM = privKeyPEM.replace("-----END PRIVATE KEY-----", "");
            byte[] b = Base64.decode(privKeyPEM,Base64.DEFAULT);

            KeyFactory keyFactory = null;
            try {
                keyFactory = KeyFactory.getInstance("RSA");
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }

            EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(b); //This decodes properly without any exceptions.
            PrivateKey privateKey2 = null;
            try {
                privateKey2 = keyFactory.generatePrivate(privateKeySpec);
            } catch (InvalidKeySpecException e) {
                e.printStackTrace();
            } 
            byte[] decryptedData = null;
            Cipher cipher = null;
            try {
                cipher = Cipher.getInstance("RSA");
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (NoSuchPaddingException e) {
                e.printStackTrace();
            } 
            try {
                cipher.init(Cipher.DECRYPT_MODE,privateKey2);
            } catch (InvalidKeyException e) {
                e.printStackTrace();
            }
            byte[] sD = Base64.decode(sData, Base64.DEFAULT);// Here I try to get the encrypted string retrieved from server into a byte[].
            try {
                decryptedData = cipher.doFinal(sD); // no errors, but I get the incorrect unencrypted string.
            } catch (IllegalBlockSizeException e) {
                e.printStackTrace();
            } catch (BadPaddingException e) {
                e.printStackTrace();
            }
            if (decryptedData != null){
                String decrypted = new String(decryptedData);
                //decryptedData = Base64.encode(decryptedData,Base64.DEFAULT);
                Toast.makeText(LicenseActivity.this, decrypted, Toast.LENGTH_LONG).show();
            }

        }
}
