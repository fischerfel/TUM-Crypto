    try {
            String text = "Hello World";
            MessageDigest msg = MessageDigest.getInstance("MD5");
            msg.update(text.getBytes(), 0, text.length());
            String digest1 = new BigInteger(1, msg.digest()).toString(16);
            System.out.println("MD5: " + digest1.length());
            System.out.println("MD5: " + digest1);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(AndroidActivationView.class.getName()).log(Level.SEVERE, null, ex);
        }
