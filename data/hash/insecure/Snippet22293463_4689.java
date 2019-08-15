String stringThatNeedsToBeEncrpyted = "PutYourURL"; 
        MessageDigest mdEnc = null;
        try {
            mdEnc = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } // Encryption algorithm
        mdEnc.update(stringThatNeedsToBeEncrpyted.getBytes(), 0, stringThatNeedsToBeEncrpyted.length());
        String md5 = new BigInteger(1, mdEnc.digest()).toString(16); 
        System.out.println(md5); 
