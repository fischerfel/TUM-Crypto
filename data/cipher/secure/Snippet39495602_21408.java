byte[] encryptData(byte[] bytes) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, UnsupportedEncodingException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        byte[] modulusBytes = Base64.decode("rSE5aIjN9rjXrXreZWRMrE/rW1oEWMu1gEhJfcC3ZOxnTuZFRxyws9KAH6Fto2HLGnJeiFaNJSb4dxr10fldsQwCw/0VTSCXrkAviILBjC/lIFx6oqZ3Ivu+bBGjEY0F5Y4mEFzmfou5L10ydXmPECMKB+ezDhPjvX+FXo5uO7gtHv0MOefpkCaeL+WrC0ETwJP6/EIPF4sa+uBHWSC601y8mzQk/t42WI7JsRDI1usQ/MH8g0HT3JgnMffWPm0nfAIvPY575sfvZ1IEQGX4zvU/Fuo7CeOi1jsT3nTk22Sp1a9j0VAu8sBxtfWpVwe2v8m8xa0nUxiJEuRBGFXOU9yS9gyMvZZDbSAMuQOVonz6cJT54KcaT3XoxdMf0N6WKPd3iZiC8I5jTpFtPgd8sOzMucBUbecIoW3/Kx9H0T8Xoc7+bHzcBm5OQ63NQmpmQ8fguw018YBVbe1mfrZrkkl86gJSaknctfoBmvdP4pt74S0Uy5TH54Hit5kL6qkdmWgREwgNhjeHnc3UgvRlUlBIlJqtv90+Z9boe2xyoHad8fWqAwooaMmOhNlQY985y2CEo5NgT8RZogRzhu59pQurNu5wfA0zsO1+c+IdyfKBN3kpy82SMbwyeSym0IQIjYt1x+TnlTk05hahrantejmBBcuWsVJUb/0TyfU84TM="
                .getBytes("UTF-8"), Base64.DEFAULT);
        byte[] exponentBytes = Base64.decode("AQAB".getBytes("UTF-8"), Base64.DEFAULT);

        BigInteger modules = new BigInteger(1, modulusBytes);
        BigInteger exponent = new BigInteger(1, exponentBytes);

        int keysize = 4096 / 8;
        int num1 = keysize - 42;
        int length = bytes.length;
        int num2 = length / num1;

        KeyFactory factory = KeyFactory.getInstance("RSA");
        Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");
        RSAPublicKeySpec pubSpec = new RSAPublicKeySpec(modules, exponent);
        PublicKey pubKey = factory.generatePublic(pubSpec);
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i <= num2; i++) {
            byte[] rgb = new byte[length - num1 * i > num1 ? num1 : length - num1 * i];
            System.arraycopy(bytes, num1 * i, rgb, 0, rgb.length);
            byte[] inArray = cipher.doFinal(rgb);
            stringBuilder.append(Base64.encode(inArray,Base64.DEFAULT).toString());
        }

        return Base64.encode(cipher.doFinal(bytes), Base64.DEFAULT);
    }
