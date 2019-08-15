    private static SOAPMessage createSOAPRequest() throws Exception 
    {
        String password = "FakePassword";

        String nonce = generateNonce(); 
        System.out.println("Nonce = " + nonce);

        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        dateFormatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date today = Calendar.getInstance().getTime();
        String created = dateFormatter.format(today);
        System.out.println("Created = " + created);

        String passwordDigest = buildPasswordDigest(nonce, created, password);
        System.out.println("Password Digest = " + passwordDigest);
    }

    private static String buildPasswordDigest(String nonce, String created, String password) throws NoSuchAlgorithmException, UnsupportedEncodingException
    {
        MessageDigest sha1;
        String passwordDigest = null;

        try
        {
            sha1 = MessageDigest.getInstance("SHA-1");
            sha1.update(Base64.decodeBase64(nonce));
            sha1.update(created.getBytes("UTF-8"));
            passwordDigest = new String(Base64.encodeBase64(sha1.digest(password.getBytes("UTF-8"))));
            sha1.reset();
        }
        catch (NoSuchAlgorithmException e) 
        {
            e.printStackTrace();
        }

        return passwordDigest;
    }

    private static String generateNonce() throws NoSuchAlgorithmException, NoSuchProviderException, UnsupportedEncodingException
    {
        String dateTimeString = Long.toString(new Date().getTime());
        byte[] nonceByte = dateTimeString.getBytes();
        return Base64.encodeBase64String(nonceByte);
    }
