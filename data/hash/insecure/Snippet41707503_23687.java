public static String sha1Convert(String password) {
            try {
                MessageDigest crypt = MessageDigest.getInstance("SHA-1");
                crypt.reset();
                crypt.update(password.getBytes("UTF-8"));
                return byteToHex(crypt.digest());
            } catch (NoSuchAlgorithmException | UnsupportedEncodingException ex) {
                Logger.getLogger(UserLoginManaged.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
