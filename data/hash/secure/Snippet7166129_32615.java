            String password="asdf"
            MessageDigest digest=null;
    try {
        digest = MessageDigest.getInstance("SHA-256");
    } catch (NoSuchAlgorithmException e1) {
        // TODO Auto-generated catch block
        e1.printStackTrace();
    }
       digest.reset();
       try {
        Log.i("Eamorr",digest.digest(password.getBytes("UTF-8")).toString());
    } catch (UnsupportedEncodingException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
