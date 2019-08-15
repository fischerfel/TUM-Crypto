public static void main(String[] args)throws Exception
    {
        String password = "T100375|Z123456|3.00|999999999|https://c.ap2.visual.force.com/apex/ThankYou|45454545|test_1.00_0.00~test_1.00_0.00~test_1.00_0.00|prasad.vandavasi@techprocess.co.in|7506384658|11740|Prasad Vandavasi|7895455555|Y|F|40000000.00|6274148983DTKQQK";

        MessageDigest md = MessageDigest.getInstance("SHA-512");
        md.update(password.getBytes());

        byte byteData[] = md.digest();

        //convert the byte to hex format method 1
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < byteData.length; i++) {
         sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        }

        System.out.println("Hex format : " + sb.toString());

        //convert the byte to hex format method 2
        StringBuffer hexString = new StringBuffer();
        for (int i=0;i<byteData.length;i++) {
            String hex=Integer.toHexString(0xff & byteData[i]);
            if(hex.length()==1) hexString.append('0');
            hexString.append(hex);
        }
        System.out.println("Hex format : " + hexString.toString());
    }
