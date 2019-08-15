public static void main(String[] args) throws IOException, SQLException{
    FileReader readFile = null;
    BufferedReader br = null;

    String timeStamp = null;
    String visitorID = null;;
    String visitorName = null;
    String emailAdd;
    String encryptedEmail = null;
    String countryCode = null;
    String countryName = null;
    String region;
    String timeStampConvo = null;
    String convo = null;
    String nameOfConvo = null;
    String visitorConvo = null;
    String staffConvo = null;
    String staffName = null;



    /////////////////////////////// MYSQL //////////////////////////////////////////    
    String url = "jdbc:mysql://localhost:3306/webchat_fypj?autoReconnect=true&useSSL=false";
    String username = "root";
    String password = "mavis";


    try {
        /////////////////////// READ FROM THE TEXTFILE ////////////////////////////
        String Path = new File("").getAbsolutePath();
        readFile = new FileReader(Path+"/src/data/2017-01-16tiff1.text");
        br = new BufferedReader(readFile);
        String str;
        Pattern p = Pattern.compile("Email: "+"([\\w\\-]([\\-\\.\\w])+[\\w\\-]+@([\\w\\-]+\\.)+[A-Za-z]{2,4})");      
        Pattern timing = Pattern.compile("(" + "(\\d{4})" + "-" + "(\\d{2})" + "-" + "(\\d{2})" + " " + "(\\d{2})"+ ":" + "(\\d{2})" + ":" + "(\\d{2})" + ")");


        ////////////////////// GET CONNECTION TO DATABASE /////////////////////////
        Connection myConn = DriverManager.getConnection(url, username, password);
        Statement myStat = (Statement) myConn.createStatement();        
        ///////////////////// GET VALUES FROM TEXTFILE////////////////////////////
        while ((str = br.readLine()) != null) {


            if(str.toLowerCase().contains("timestamp"))
            {
                timeStamp = StringUtils.substringAfter(str, "Timestamp: ");
                System.out.println("\n" + "\n" + "Timestamp - " + timeStamp);
            }
            if(str.toLowerCase().contains("visitor id"))
            {
                visitorID = StringUtils.substringAfter(str, "Visitor ID: ");
                System.out.println("Visitor ID - " + visitorID);
            }
            if(str.toLowerCase().contains("visitor name"))
            {
                visitorName = StringUtils.substringAfter(str, "Visitor Name: ");
                //System.out.println("Visitor Name - " + visitorName);
            }

            /////////////////////////// FOR EMAIL /////////////////////////
            Matcher m = p.matcher(str);
            if(m.find())
            {
                emailAdd = m.group(1);
                try 
                {
                    byte[] enEmail = emailAdd.getBytes();

                    Cipher c = Cipher.getInstance("DES");
                    KeyGenerator kg = KeyGenerator.getInstance("DES");
                    SecretKey sk = kg.generateKey();

                    c.init(Cipher.ENCRYPT_MODE, sk);
                    byte encryptEmail[] = c.doFinal(enEmail);

                    encryptedEmail = new String(encryptEmail);
                    if(str.toLowerCase().contains("Visitor Email"))
                    {
                        //System.out.println("Email - " + encryptedEmail.replace("'", "\\'"));
                    }
                    else {
                    //  System.out.println("Email - blank");
                    }
                }
                catch(Exception e){
                    System.out.println(e);
                }

            }
            if(str.toLowerCase().contains("country code"))
            {
                countryCode = StringUtils.substringAfter(str, "Country Code: ");
                System.out.println("Country Code - " + countryCode);
            }
            if(str.toLowerCase().contains("country name"))
            {
                countryName = StringUtils.substringAfter(str, "Country Name: ");
                System.out.println("Country Name - " + countryName);
            }


            Matcher timematch = timing.matcher(str);
            if(timematch.find())
            {
                timeStampConvo = timematch.group(1);
                //System.out.println("Mini Timestamp - " + timeStampConvo);

                convo = StringUtils.substringAfter(str, timeStampConvo + ") ");
                //System.out.println("convo - " + convo);
                nameOfConvo = StringUtils.substringBefore(convo, ":");
                //System.out.println(nameOfConvo + " ");


                ///////////////////////DATABASE FOR CONVERSATION/////////////////////////////////////////////////
                if(!(nameOfConvo.equals(visitorName))){
                    staffConvo = StringUtils.substringAfter(convo, ": ");
                //  System.out.println("Staff - " + staffConvo);
                    staffName = StringUtils.substringBefore(convo, ": ");
                //  System.out.println("Staff - " + staffName);
                    String staffSql = "INSERT INTO conversation" + "(speaker,convoTimestamp,convo)" + "VALUES ('"+ staffName+"','"+timeStampConvo+"','"+ staffConvo.replace("'", "\\'")+"')";
                    myStat.executeUpdate(staffSql);
                } 
                    else {
                        visitorConvo = StringUtils.substringAfter(convo, ": ");
                    //  System.out.println("Visitor - " + visitorConvo);
                        String visitorSql = "INSERT INTO conversation" + "(speaker,convoTimestamp,convo)" + "VALUES ('"+ visitorName+"','"+timeStampConvo+"','"+ visitorConvo.replace("'", "\\'")+"')";
                        myStat.executeUpdate(visitorSql);
                }

                }

        }
            ///////////////////////////// DATABASE /////////////////////////////////////////            
        String countryCodeSql = "INSERT INTO session" + "(sessionTimestamp,countryCode,countryName,visitorID)" + "VALUES ('"+ timeStamp+"','"+countryCode+"','"+ countryName+"','" + visitorID+"')";
        myStat.executeUpdate(countryCodeSql);

            if (visitorID != null && encryptedEmail !=null){
            String visitorSql = "INSERT IGNORE INTO visitor" + "(visitorID,visitorName,visitorEmail)" + "VALUES ('" + visitorID+"','" + visitorName+ "','" + encryptedEmail.replace("'", "\\'")+"')";
            myStat.executeUpdate(visitorSql);
            }




    }
    catch (SQLException e) {
        System.out.println(e.getMessage());
    }


    finally {

        try {
            readFile.close();
            br.close();
        }

        catch (IOException x) {
            x.printStackTrace();
        }


    }
}
