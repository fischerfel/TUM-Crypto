String stQuery="from FTPConfig where sstatus='"+Enumerations.MasterStatus_Add+"'";
List<FTPConfig> lstftp=getHibernateTemplate().find(stQuery);

FTPClient ftp=new FTPClient();

ftp.connect(lstftp.get(3).getshost(),lstftp.get(3).getNport());
boolean ftpFile=ftp.login(lstftp.get(3).getsusername(), lstftp.get(3).getspassword());

ftp.setFileType(FTP.BINARY_FILE_TYPE);
ftp.enterLocalPassiveMode();


if(lstDBfile.size()>0)
{
    String filename =nFileImageID+lstDBfile.get(0).getsFileName(); 

    String absolutePath1 = new File("").getAbsolutePath() + Enumerations.UPLOAD_PATH;
    String uniquefilename =  lstDBfile.get(0).getsFileName();
    String st1 = absolutePath1 + uniquefilename;
    String st2 = absolutePath1 + filename;

    logger.info("**********FTP Storage st1*************"+st1);

    logger.info("**********FTP Storage filename *************"+filename);


    stResult = uniquefilename;
    File file = new File(st1);

    String password = "javapapers";
    PBEKeySpec pbeKeySpec = new PBEKeySpec(password.toCharArray());
    SecretKeyFactory secretKeyFactory = SecretKeyFactory
            .getInstance("PBEWithMD5AndTripleDES");
    SecretKey secretKey = secretKeyFactory.generateSecret(pbeKeySpec);

    InputStream inputStream = ftp.retrieveFileStream(filename);

    OutputStream oufil= new FileOutputStream(st2);
    int c=0;
    while((c=inputStream.read())!=-1)
    {
        oufil.write(c);
    }
    oufil.close();
    ByteArrayInputStream filein = new ByteArrayInputStream(oufil.toString().getBytes());


    byte[] salt = new byte[8];
    filein.read(salt);

    PBEParameterSpec pbeParameterSpec = new PBEParameterSpec(salt, 100);

    Cipher cipher = Cipher.getInstance("PBEWithMD5AndTripleDES");


    cipher.init(Cipher.DECRYPT_MODE, secretKey, pbeParameterSpec);

    OutputStream outputStream2 = new FileOutputStream(file);
    long start = System.currentTimeMillis();

    byte[] bytesArray = new byte[64];
    int bytesRead = -1;
    while ((bytesRead = filein.read(bytesArray)) != -1) {

        byte[] output = cipher.update(bytesArray, 0, bytesRead);
        if (output != null)
            outputStream2.write(output);
        outputStream2.write(output);
    }
    byte[] output = cipher.doFinal(); 

    if (output != null)
        outputStream2.write(output);




    boolean download  = ftp.completePendingCommand();
    if (download)
    {  
        System.out.println("File downloaded successfully !");  
        logger.info("file downloaded successfully with "
                + (System.currentTimeMillis() - start) + "ms");
    } 
    else 
    {  
        System.out.println("Error in downloading file !");  
    }  
    outputStream2.flush();
    outputStream2.close();
    inputStream.close();
