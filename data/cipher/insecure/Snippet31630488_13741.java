Cipher aesCipher = Cipher.getInstance("AES");
aesCipher.init(Cipher.ENCRYPT_MODE, aesSecretKey);
String inputText1 = JOptionPane.showInputDialog("Enter a secret message: ");
byte[] encrypt = aesCipher.doFinal(inputText1.getBytes());
