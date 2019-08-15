String s= EnumerationsQms.ReturnStatus.SUCCESS.getreturnstatus();
int read;

FTPConfig objFTP = (FTPConfig)getHibernateTemplate().find(" from FTPConfig where sstatus='A'").get(3);

FTPClient ftpclient = new FTPClient();

ftpclient.connect(objFTP.getshost(),objFTP.getNport());

logger.info("objFTP.getsip()"+objFTP.getsip());


boolean strue = ftpclient.login(objFTP.getsusername(),objFTP.getspassword());

logger.info("objFTP.getsusername()"+objFTP.getsusername());

logger.info("objFTP.getspassword()"+objFTP.getspassword());
ftpclient.enterLocalPassiveMode();

ftpclient.setFileType(FTP.BINARY_FILE_TYPE);

if(strue)
{
    String st =  GeneralFunctionDAOImpl.getAbsolutePath() + objDbFileStorage.getsFileName();

    logger.info("File Name--->"+st);

    File firstLocalFile = new File(st);           
    String uniquefilename =objDbFileStorage.getnFileImageId() + objDbFileStorage.getsFileName();
    logger.info("uniquefilename"+uniquefilename);

    InputStream inputStream = new FileInputStream(st);
    logger.info("st"+st);

    OutputStream outputStream = ftpclient.storeFileStream(uniquefilename);

    String password = "javapapers";
       PBEKeySpec keySpec = new PBEKeySpec(password.toCharArray());
       SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBEWithMD5AndTripleDES");
       SecretKey passwordKey = keyFactory.generateSecret(keySpec);

    byte[] salt = new byte[8];
    Random rnd = new Random();
    rnd.nextBytes(salt);
    int iterations = 100;

    PBEParameterSpec parameterSpec = new PBEParameterSpec(salt, iterations);

    Cipher cipher = Cipher.getInstance("PBEWithMD5AndTripleDES");
    cipher.init(Cipher.ENCRYPT_MODE, passwordKey, parameterSpec);
    outputStream.write(salt);

    byte[] input = new byte[64];

    while ((read = inputStream.read(input)) != -1)

    {                   
        byte[] output = cipher.update(input, 0, read);
        if (output != null)
            outputStream.write(output); 

    }   

    byte[] output = cipher.doFinal();
    if (output != null)
        outputStream.write(output);

    inputStream.close();
    outputStream.flush();
    outputStream.close();

    if(ftpclient.isConnected()){
        ftpclient.logout();
        ftpclient.disconnect();
    }
}
return s;
