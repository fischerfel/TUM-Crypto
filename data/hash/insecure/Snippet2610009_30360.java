public static String encriptSHA1(String password){
        String hash = "";

        try {
            MessageDigest md;
            byte[] buffer, digest;

            buffer = password.getBytes();
            md = MessageDigest.getInstance("SHA1");

            md.update(buffer);
            digest = md.digest();

            for(byte aux : digest) {
                int b = aux & 0xff;
                if (Integer.toHexString(b).length() == 1) hash += "0";
                hash += Integer.toHexString(b);
            }
        } catch (NoSuchAlgorithmException e) {
        }

        return hash;
    }
