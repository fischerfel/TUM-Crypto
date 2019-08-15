    String getMD5(String data){
        MessageDigest m = MessageDigest.getInstance("MD5");
        m.reset();
        m.update(data.getBytes());
        return new BigInteger(1, m.digest()).toString(16);
    }
