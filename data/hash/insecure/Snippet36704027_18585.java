   try {
                MessageDigest md = MessageDigest.getInstance("MD5"); // get the
                                                                        // instance
                                                                        // of md5
            md.update(bytes); // get the digest updated
            byte[] b = md.digest(); // calculate the final value
            int i;
            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            message = buf.toString(); // output as strings

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace(); // when certain algorithm is down, output the
                                    // abnormal condition
        }
        return message;
    }
