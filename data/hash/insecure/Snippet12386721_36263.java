    String encryptedString = null;
            byte[] bytesToBeEncrypted;
            try {
                // convert string to bytes using a encoding scheme  
                bytesToBeEncrypted = ren_pass.getBytes("UTF-8");
                MessageDigest md = MessageDigest.getInstance("MD5");
                byte[] theDigest = md.digest(bytesToBeEncrypted);
                // convert each byte to a hexadecimal digit  
                Formatter formatter = new Formatter();

                for (int i = 0; i <= theDigest.length; i++) {
                    byte b = theDigest[i];                          
                    //for (byte b : theDigest) {  

     formatter.format("%02x", b); // error on this statement b cannot find symbol                       
                }
                encryptedString = formatter.toString().toLowerCase();
                System.out.print(encryptedString);

            } catch (UnsupportedEncodingException eq) {
                eq.printStackTrace();
            } catch (NoSuchAlgorithmException ew) {
                ew.printStackTrace();
            }
