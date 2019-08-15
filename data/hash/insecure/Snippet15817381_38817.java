public byte[] getCheckSumFromFile(InputStream inputStream)
    {
        MessageDigest md = null;

        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }

        InputStream is = null;

        try {
          is = new DigestInputStream(inputStream, md);
        }
        finally {
              try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        return md.digest();
    }
