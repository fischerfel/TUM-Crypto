private void executeDecryption() {
    encryptMessageStr = messageEncryptTA.getText();
    algorithmType = decryptAlgorithmTypeCB.getSelectionModel().getSelectedItem();
    keyDecrypt = keyTF.getText();

    if (!messageEncryptedTA.getText().isEmpty()) {

        try {
            //Using decryptText()
            String textDecipher = decryptText(encryptedMessageStr, keyDecrypt);
            messageEncryptedTA.setText(textDecipher);

        } catch (Exception ex) {
            MessageBox.display("Error!", "Could not decrypt message! Please try again later");
            //System.out.println(ex.getMessage());
        }

    } else {
        MessageBox.display("Warning!", "Please make sure message field is not empty!");
    }
}

public static String decryptText(String encryptedMessageStr, String secretKey) {
    try {
        Key k = new SecretKeySpec(Base64.getDecoder().decode(secretKey), "AES");
        Cipher cipherAES = Cipher.getInstance("AES");
        cipherAES.init(Cipher.DECRYPT_MODE, k);

        byte[] decodedValue = Base64.getDecoder().decode(encryptedMessageStr);
        byte[] cipherTextBytes = cipherAES.doFinal(decodedValue);
        String decryptedValue = new String(cipherTextBytes);
        return decryptedValue;

    } catch (Exception ex) {
        ex.printStackTrace();
    }

    return "Error";

}
