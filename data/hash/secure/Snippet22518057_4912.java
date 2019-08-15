public static String hashPassword(String password, String salt) throws Exception {
        String result = password;
        String appendedSalt = new StringBuilder().append('{').append(salt).append('}').toString();
        String appendedSalt2 = new StringBuilder().append(password).append('{').append(salt).append('}').toString();

        if(password != null) {
            //Security.addProvider(new BouncyCastleProvider());

            MessageDigest mda = MessageDigest.getInstance("SHA-512");
            byte[] pwdBytes = password.getBytes("UTF-8");
            byte[] saltBytes = appendedSalt.getBytes("UTF-8");
            byte[] saltBytes2 = appendedSalt2.getBytes("UTF-8");
            byte[] digesta = encode(mda, pwdBytes, saltBytes);

            //result = new String(digesta);
           System.out.println("first hash: " + new String(Base64.encode(digesta),"UTF-8"));
                for (int i = 1; i < ROUNDS; i++) {

                    digesta = encode(mda, digesta, saltBytes2);
                }
                System.out.println("last hash: " + new String(Base64.encode(digesta),"UTF-8"));

                result = new String(Base64.encode(digesta));
        }
        return result;
    }

private static byte[] encode(MessageDigest mda, byte[] pwdBytes,
            byte[] saltBytes) {
        mda.update(pwdBytes);
        byte [] digesta = mda.digest(saltBytes);
        return digesta;
    }
