public static String createOrderNumber(Date orderDate) throws NoSuchAlgorithmException {
        DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        String datestring = df.format(orderDate).toString();
        System.out.println("datestring="+datestring);
        System.out.println("datestring size="+datestring.length());
        String hash = makeHashString(datestring);//creates SHA1 hash of 16 digits
        System.out.println("hash="+hash);
        System.out.println("hash size="+hash.length());
        int datestringlen = datestring.length();
        String ordernum = datestring+hash.substring(datestringlen,datestringlen+5);
        System.out.println("ordernum size="+ordernum.length());
        return ordernum;
    }

    private static String makeHashString(String plain) throws NoSuchAlgorithmException {
        final int MD_PASSWORD_LENGTH = 16;
        final String HASH_ALGORITHM = "SHA1";
        String hash = null;
         try {
                MessageDigest md = MessageDigest.getInstance(HASH_ALGORITHM);
                md.update(plain.getBytes());
                BigInteger hashint = new BigInteger(1, md.digest());
                hash = hashint.toString(MD_PASSWORD_LENGTH);
            } catch (NoSuchAlgorithmException nsae) {
                throw(nsae);
            }
        return hash;
    }
