public String encrypt(String generatedKey)
    {
            try {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(generatedKey.getBytes("UTF-8"));
                byte digest[] = md.digest();
                return (new BASE64Encoder()).encode(digest);
            }
            catch (Exception e) {
                return null;
            }

    }
