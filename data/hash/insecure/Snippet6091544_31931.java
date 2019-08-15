public static void generateChecksums(String strInputFile, String strCSVFile) {
    ArrayList<String[]> outputList = new ArrayList<String[]>();
    try {
        MessageDigest m = MessageDigest.getInstance("MD5");
        File aFile = new File(strInputFile);
        InputStream is = new FileInputStream(aFile);

        System.out.println(Calendar.getInstance().getTime().toString() + 
                    " Processing Checksum: " + strInputFile);

        double dLength = aFile.length();
        try {
            is = new DigestInputStream(is, m);
            // read stream to EOF as normal...
            int nTmp;
            double dCount = 0;
            String returned_content="";
            while ((nTmp = is.read()) != -1) {
                dCount++;
                if (dCount % 600000000 == 0) {
                    System.out.println(". ");
                } else if (dCount % 20000000 == 0) {
                    System.out.print(". ");
                }
            }
            System.out.println();
        } finally {
            is.close();
        }
        byte[] digest = m.digest();
        m.reset();
        BigInteger bigInt = new BigInteger(1,digest);
        String hashtext = bigInt.toString(16);
        // Now we need to zero pad it if you actually / want the full 32 chars.
        while(hashtext.length() < 32 ){
            hashtext = "0" + hashtext;
        }
        String[] arrayTmp = new String[2];
        arrayTmp[0] = aFile.getName();
        arrayTmp[1] = hashtext;
        outputList.add(arrayTmp);
        System.out.println("Hash Code: " + hashtext);
        UtilityFunctions.createCSV(outputList, strCSVFile, true);
    } catch (NoSuchAlgorithmException nsae) {
        System.out.println(nsae.getMessage());
    } catch (FileNotFoundException fnfe) {
        System.out.println(fnfe.getMessage());
    } catch (IOException ioe) {
        System.out.println(ioe.getMessage());
    }
}
