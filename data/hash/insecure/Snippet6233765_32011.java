    public byte[] getHandshake (String firstKey, String secondKey, byte[] last8)
    {
        byte[] toReturn = null;
        //Strip out numbers
        int firstNum = Integer.parseInt(firstKey.replaceAll("\\D", ""));
        int secondNum = Integer.parseInt(secondKey.replaceAll("\\D", ""));

        //Count spaces
        int firstDiv = firstKey.replaceAll("\\S", "").length();
        int secondDiv = secondKey.replaceAll("\\S", "").length();

        //Do the division
        int firstShake = firstNum / firstDiv;
        int secondShake = secondNum / secondDiv;

        //Prepare 128 bit byte array
        byte[] toMD5 = new byte[16];
        byte[] firstByte = ByteBuffer.allocate(4).putInt(firstShake).array();
        byte[] secondByte = ByteBuffer.allocate(4).putInt(secondShake).array();

        //Copy the bytes of the numbers you made into your md5 byte array
        System.arraycopy(firstByte, 0, toMD5, 0, 4);
        System.arraycopy(secondByte, 0, toMD5, 4, 4);
        System.arraycopy(last8, 0, toMD5, 8, 8);
        try
        {
            //MD5 everything together
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            toReturn = md5.digest(toMD5);
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }

        return toReturn;
}
