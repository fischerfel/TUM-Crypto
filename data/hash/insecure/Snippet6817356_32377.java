private String calculatePasswordDigest(String nonce, String created, String password) {
        String encoded = null;
        try {
            String pass = hexEncode(nonce) + created + password;
            MessageDigest md = MessageDigest.getInstance( "SHA1" );
            md.update( pass.getBytes() );
            byte[] encodedPassword = md.digest();
            encoded = Base64.encodeBytes(encodedPassword);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(HeaderHandler.class.getName()).log(Level.SEVERE, null, ex);
        }

        return encoded;
    }

    private String hexEncode(String in) {
        StringBuilder sb = new StringBuilder("");
        for (int i = 0; i < (in.length() - 2) + 1; i = i + 2) {
            int c = Integer.parseInt(in.substring(i, i + 2), 16);
            char chr = (char) c;
            sb.append(chr);
        }
        return sb.toString();
    }
