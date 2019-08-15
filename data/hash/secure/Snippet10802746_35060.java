    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
        NoSuchAlgorithmException noSuchAlgorithmException = null;
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");

        } catch (NoSuchAlgorithmException ex) {
            noSuchAlgorithmException = ex;
        }
        if (noSuchAlgorithmException != null) {
            System.out.println(noSuchAlgorithmException.toString());
        }
        else {
            UnsupportedEncodingException unsupportedEncodingException = null;
            byte[] hash = null;
            char[] password = jPasswordField1.getPassword();
            StringBuffer stringBuffer = new StringBuffer();
            for (char c : password) {
                if (c > 0 && c < 16) {
                    stringBuffer.append("0");
                }
                stringBuffer.append(Integer.toHexString(c & 0xff));
            }
            String passwordString = stringBuffer.toString();
            try {
                hash = messageDigest.digest(passwordString.getBytes("UTF-8"));
            } catch (UnsupportedEncodingException ex) {
                unsupportedEncodingException = ex;
            }
            if (unsupportedEncodingException != null) {
                System.out.println(unsupportedEncodingException.toString());
            }
            else {
                stringBuffer = new StringBuffer();
                for (byte b : hash) {
                    stringBuffer.append(String.format("%02x", b));
                }
                String passwordHashed = stringBuffer.toString();
                System.out.println(passwordHashed);
            }
        }
