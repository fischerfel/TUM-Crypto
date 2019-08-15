public void hash() throws NoSuchAlgorithmException, UnsupportedEncodingException{

        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(fixturesFeedURL.getBytes("UTF-8"));
        byte[] digest = md.digest();
        String strhash = new String(digest);
        Log.v("myApp", strhash);
    }   
