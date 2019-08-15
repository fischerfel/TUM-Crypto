 private static void checkSumGen() throws IOException NoSuchAlgorithmException
    {

    File plFolder = new File(".\\Plugins");
    File[] listOfFiles = plFolder.listFiles();
    List<String> listClone = new ArrayList<String>();
    for (int i = 0; i < listOfFiles.length; i++)
        {
            File file = listOfFiles[i];
            String fileloc = file.getAbsolutePath();
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            FileInputStream fis = new FileInputStream(fileloc);

            byte[] dataBytes = new byte[1024];

            int nread = 0;
            while ((nread = fis.read(dataBytes)) != -1)
            {
                md.update(dataBytes, 0, nread);
            } ;
            byte[] mdbytes = md.digest();

            // convert the byte to hex format method 1
            StringBuffer sb = new StringBuffer();
            for (int i1 = 0; i1 < mdbytes.length; i++)
                {
                    sb.append(Integer.toString((mdbytes[i1] & 0xff) + 0x100, 16).substring(1)); 
                }

            System.out.println("Hex format : " + sb.toString());

            // convert the byte to hex format method 2
            StringBuffer hexString = new StringBuffer();
            for (int i2 = 0; i2 < mdbytes.length; i++)
                {
                    hexString.append(Integer.toHexString(0xFF & mdbytes[i2]));
                }
            System.out.println("Hex format : " + hexString.toString());
        }

}
