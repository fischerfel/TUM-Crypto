private void IssuingTickets() throws Exception{

        String socketUsername = reader.readLine();//rcv username
        String socketPassword = reader.readLine();//rcv password
        writer.println("Lemme Check!");  


        Properties prop = new Properties();
        prop.load(new FileInputStream("input"));
        String fileUsername = prop.getProperty("username");
        String filePassword = null;
        if (prop.getProperty("password") != null) {
            filePassword = prop.getProperty("password");}
        if (socketPassword.equals(filePassword)){ 
            String sessionKeyBobKdc = new Scanner(new File("sBOBandKDC")).useDelimiter("\\Z").next();
            byte[] ClientTicket = encrypt(sessionKeyBobKdc, filePassword);
            System.out.println("clietn ticket =   " + ClientTicket+"   ArraytoString   " + Arrays.toString(ClientTicket));
            writer.println(ClientTicket);
            String KDCkey = new Scanner(new File("KDCkey")).useDelimiter("\\Z").next();
            String UnEncTGT= sessionKeyBobKdc.concat(socketUsername);
            byte[] TGT = encrypt(UnEncTGT, KDCkey);
            writer.println(TGT);

            }else
                {writer.println("Please try again later!");}

            }    


    public static byte[] encrypt(String plainText1, String encryptionKey) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding", "SunJCE");
        SecretKeySpec key = new SecretKeySpec(encryptionKey.getBytes("UTF-8"), "AES");
        cipher.init(Cipher.ENCRYPT_MODE, key,new IvParameterSpec(IV.getBytes("UTF-8")));
        return cipher.doFinal(plainText1.getBytes("UTF-8"));
            }



    public static String decrypt(byte[] cipherText, String encryptionKey) throws Exception{
        Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding", "SunJCE");
        SecretKeySpec key = new SecretKeySpec(encryptionKey.getBytes("UTF-8"), "AES");
        cipher.init(Cipher.DECRYPT_MODE, key,new IvParameterSpec(IV.getBytes("UTF-8")));
        return new String(cipher.doFinal(cipherText),"UTF-8");
            }
}
