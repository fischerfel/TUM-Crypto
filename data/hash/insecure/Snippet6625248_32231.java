System.out.println(streamConvertor.getHash("01.02.1980", Boolean.TRUE));
System.out.println("");
System.out.println(streamConvertor.getHash("01.02.1980", Boolean.FALSE));

public String getMD5(String input, Boolean is_date) throws ParseException {
    try {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] mdinput;
        Boolean pr;
        if (input.equals("01.02.1980")) {
            pr = true;
        } else {
            pr = false;
        }

        if (is_date == Boolean.TRUE) {
            SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");
            Date day = df.parse(input);  
            mdinput = df.format(day).getBytes();
        } else {
            mdinput = input.getBytes();
        }
        if (pr == Boolean.TRUE) {
            System.out.println("Is date " + is_date + " - " + mdinput);
        }
        byte[] messageDigest = md.digest(mdinput);
        if (pr == Boolean.TRUE) {
            System.out.println("message - " + messageDigest);
        }
        BigInteger number = new BigInteger(1, messageDigest);
        if (pr == Boolean.TRUE) {
            System.out.println("Number - " + number);
        }
        String hashtext = number.toString(16);
        // Now we need to zero pad it if you actually want the full 32 chars.
        while (hashtext.length() < 32) {
            hashtext = "0" + hashtext;
        }
        if (pr == Boolean.TRUE) {
            System.out.println("hashtext - " + hashtext);
        }
        return hashtext;
    }
    catch (NoSuchAlgorithmException e) {
        throw new RuntimeException(e);
    }
}
