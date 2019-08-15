public static String buildIdentity(String identity) {
        try {
            return URLEncoder.encode(toSHA1(identity.getBytes())).toLowerCase();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String toSHA1(byte[] convertme) throws UnsupportedEncodingException{
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-1");
            md.update(convertme);
            byte[] res = md.digest();
            return new String(res);
        }
        catch(NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }


System.out.println(Utils.buildIdentity("asfasfasdf"));
