public String getHash(final String appSecret , final String sessionToken)throws NoSuchAlgorithmException ,UnsupportedEncodingException{

        String input = sessionToken + appSecret;
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        digest.reset();

        byte[] byteData = digest.digest(input.getBytes("UTF-8"));
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < byteData.length; i++){
            sb.append(String.format("%02x", 0xFF & byteData[i]));
        }
        return sb.toString();

    }
