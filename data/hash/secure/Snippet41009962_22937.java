        String resultString         =   "";
        try {
            byte[] buffer           =   password.getBytes();
            MessageDigest md        =   MessageDigest.getInstance("SHA-512");
            md.update(buffer);
            byte[] digest           =   md.digest();

            for(int i = 0 ; i < digest.length ; i++) {
                int b               =   digest[i] & 0xff;
                if(Integer.toHexString(b).length() == 1)
                    resultString    =   resultString + "0";
                resultString        =   resultString + Integer.toHexString(b);
            }
        } catch(NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
