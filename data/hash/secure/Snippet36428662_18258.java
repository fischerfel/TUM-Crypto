 public static  String generateHashString(String data)
    {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] dataInBytes = data.getBytes(StandardCharsets.UTF_8);
            md.update(dataInBytes);
            byte[] mdbytes = md.digest();
            StringBuffer hexString = new StringBuffer();
            for (int i=0;i<mdbytes.length;i++) {
                hexString.append(Integer.toHexString(0xFF & mdbytes[i]));
            }

            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return null;
    }
