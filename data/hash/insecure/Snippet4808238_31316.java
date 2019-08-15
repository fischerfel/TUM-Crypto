    private static byte[] SHA1(final String in)
            throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        md.update(in.getBytes("iso-8859-1"), 0, in.length());
        return md.digest();
    }

    public static String decryptSHA1(String key, final String start) {
        final String delim = "a";
        if (start == null)
            return null;
        byte[] hashedkey;
        byte[] password;
        int i;
        try {
            hashedkey = SHA1(key);
        } catch (final NoSuchAlgorithmException e) {
            e.printStackTrace();
            return start;
        } catch (final UnsupportedEncodingException e) {
            e.printStackTrace();
            return start;
        }
        final String[] temp = start.split(delim);
        password = new byte[temp.length];
        for (i = 0; i < hashedkey.length; i++) {
            final int temp2 = Integer.parseInt(temp[i]);
            if (hashedkey[i] == temp2) {
                break;
            } else {
                password[i] = (byte) (temp2 - hashedkey[i]);
            }
        }
        return new String(password, 0, i);
    }
