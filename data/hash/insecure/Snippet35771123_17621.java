@Override
public void generatingPdf(int buergelAgencyNumber, int inquiryNumber,
        int reportNumber) {
    PDFReportRetrieval port = buergelServices.getPDFRetrievalPort();

    PdfResponse ss = null;
    try {
        ss = port.getReport(buergelAgencyNumber, inquiryNumber,
                reportNumber);
        System.out.println("jest ok");
    } catch (pl.done.platform.client.germany.de.buergel.rcs.ws.b2b.v1.report.generated.BuergelException e2) {
        // TODO Auto-generated catch block
        e2.printStackTrace();
    }

    DataHandler handler = ss.getPdf();

    InputStream is = null;
    try {
        is = handler.getInputStream();

    } catch (IOException e1) {
        // TODO Auto-generated catch block
        e1.printStackTrace();
    }

    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    // get current date time with Date()
    Date date = new Date();
    LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault())
            .toLocalDate();
    int year = localDate.getYear();
    int month = localDate.getMonthValue();
    int day = localDate.getDayOfMonth();

    System.out.println(dateFormat.format(date));
    System.out.println("year " + year);
    System.out.println("month " + month);
    System.out.println("day " + day);

    String address=entityService.getPathAddress();
    File filed = new File(address + year + "\\" + month + "\\" + day);
    if (!filed.exists()) {
        if (filed.mkdirs()) {
            System.out.println("directory is created");
        }
    } else {
        System.out.println("directory exist");
    }

    String uuid = UUID.randomUUID().toString();
    System.out.println("uuid = " + uuid);

    String hashmd5 = getHashMD5("" + date + uuid);

    System.out.println("hashmd5 " + hashmd5);

    OutputStream os = null;
    try {
        os = new FileOutputStream(new File(address + year + "\\"
                + month + "\\" + day + "\\" + hashmd5 + ".pdf"));


        //os = new FileOutputStream(new File(
        //      "C:\\WildFly\\wildfly-9.0.2.Final\\wildfly-9.0.2.Final\\welcome-content\\"+hashmd5+".txt"));        

    } catch (FileNotFoundException e1) {
        // TODO Auto-generated catch block
        e1.printStackTrace();
    }

    byte[] buffer = new byte[16384];
    int bytesRead = 0;
    try {

        is.reset();
        while ((bytesRead = is.read(buffer)) != -1) {
            os.write(buffer, 0, bytesRead);
            System.out.print("sss");
        }
        os.flush();
        is.close();
        System.out.print("www");

    } catch (IOException e1) {
        // TODO Auto-generated catch block
        e1.printStackTrace();
    }






}

public String getHashMD5(String string) {
    try {
        MessageDigest md = MessageDigest.getInstance("MD5");
        BigInteger bi = new BigInteger(1, md.digest(string.getBytes()));
        return bi.toString(16);
    } catch (NoSuchAlgorithmException ex) {
        // Logger.getLogger(MD5Utils.class
        // .getName()).log(Level.SEVERE, null, ex);

        return "";
    }
}
