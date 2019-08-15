import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
public class ExcelReader {

     @SuppressWarnings("unchecked")
        public static void main(String[] args) throws Exception {
            //
            // An excel file name. You can create a file name with a full 
            // path information.
            //
            String filename = "E:\\Registration Details.xlsx";

            //
            // Create an ArrayList to store the data read from excel sheet.
            //
            List sheetData = new ArrayList();

            FileInputStream fis = null;
            try {
                //
                // Create a FileInputStream that will be use to read the 
                // excel file.
                //
                fis = new FileInputStream(filename);

                //
                // Create an excel workbook from the file system.
                //
                XSSFWorkbook  workbook = new XSSFWorkbook(fis);
                //
                // Get the first sheet on the workbook.
                //
                XSSFSheet  sheet = workbook.getSheetAt(0);

                //
                // When we have a sheet object in hand we can iterator on 
                // each sheet's rows and on each row's cells. We store the 
                // data read on an ArrayList so that we can printed the 
                // content of the excel to the console.
                //
                Iterator rows = sheet.rowIterator();
                while (rows.hasNext()) {
                    XSSFRow row = (XSSFRow) rows.next();
                    Iterator cells = row.cellIterator();

                    List data = new ArrayList();
                    while (cells.hasNext()) {
                        XSSFCell cell = (XSSFCell) cells.next();
                        data.add(cell);
                    }

                    sheetData.add(data);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (fis != null) {
                    fis.close();
                }
            }

            showExcelData(sheetData);
        }

        private static void showExcelData(List sheetData) {
            //
            // Iterates the data and print it out to the console.
            //
            ArrayList xlsDataList = new ArrayList();
            for (int i = 0; i < sheetData.size(); i++) {
                List list = (List) sheetData.get(i);
                for (int j = 0; j < list.size(); j++) {
                    XSSFCell cell = (XSSFCell) list.get(j);
                    if(cell.getCellType()==cell.CELL_TYPE_STRING){
                        xlsDataList.add(cell.getRichStringCellValue().getString());
                   // System.out.print(
                          //  cell.getRichStringCellValue().getString());
                    }else  if(cell.getCellType()==cell.CELL_TYPE_NUMERIC){
                        xlsDataList.add(cell.getNumericCellValue());
                        //System.out.print(
                              //  cell.getNumericCellValue());
                        }
                    if (j < list.size() - 1) {
                    //    System.out.print(", ");
                    }
                }
              //  System.out.println("");
            }
            if(null!=xlsDataList && xlsDataList.size()>0){
                authenticatenewPostUrl(xlsDataList);
            }
            for(int i=0;i<xlsDataList.size();i++){
                System.out.println(xlsDataList.get(i));
            }
        }


        public static void authenticatenewPostUrl(ArrayList xlsDataList) {

            HostnameVerifier hv = new HostnameVerifier() {

                @Override
                public boolean verify(String urlHostName, SSLSession session) {
                    System.out.println("Warning: URL Host: " + urlHostName
                            + " vs. " + session.getPeerHost());
                    return true;
                }
            };
            // Now you are telling the JRE to trust any https server.
            // If you know the URL that you are connecting to then this should
            // not be a problem
            try {
                trustAllHttpsCertificates();
            } catch (Exception e) {
                System.out.println("Trustall" + e.getStackTrace());
            }
            HttpsURLConnection.setDefaultHostnameVerifier(hv);
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            try {

                URL url = new URL("https://www.walmart.com/cservice/ya_index.gsp");
                 String line;

                    StringBuilder string=new StringBuilder();
                    HttpsURLConnection  ucc = (HttpsURLConnection) url.openConnection();
                    InputStream content = (InputStream) ucc.getInputStream();
                    BufferedReader in = new BufferedReader(new InputStreamReader(content));
                    while ((line = in.readLine()) != null) {
                     string.append(line);

                    }
                 Document document=Jsoup.parse(string.toString());
                    Elements elements=document.getElementsByTag("a");//("mainSpriteSliderBTN wmBTN_blue25 continue");
                    int count=0;
                    //String registrationURL="";
                    String herfURL="";
                    for(Element elem:elements)
                    {
                        Elements e=elem.getElementsByAttributeValueMatching("class", "mainSpriteSliderBTN wmBTN_blue25 continue");
                        if(e!=null && !e.toString().isEmpty())
                        {
                          System.out.println(e.get(0).toString()+"\n"+count++);
                          Element myElem=e.get(0);
                          System.out.println(myElem.attr("href"));
                          herfURL=myElem.attr("href");
                        }
                    }
                    URL registrationURL = new URL("https://www.walmart.com"+herfURL);

                //String credentials = "ptt" + ":" + "ptt123";
                String postData = "firstName="+xlsDataList.get(0)+"&lastName="+xlsDataList.get(1)+"&userName="+xlsDataList.get(2)+"&userNameConfirm="+xlsDataList.get(3)+"&pwd="+xlsDataList.get(5)+"&pwdConfirm="+xlsDataList.get(6);
               // String encoding = Base64Converter.encode(credentials.getBytes("UTF-8"));
                HttpsURLConnection  uc = (HttpsURLConnection) registrationURL.openConnection();
                uc.setDoInput(true);
                uc.setDoOutput(true);
              //  uc.setRequestProperty("Authorization", String.format("Basic %s", encoding));
                uc.setRequestMethod("POST");
                uc.setRequestProperty("Accept", "*/*");
                uc.setRequestProperty("Content-Length", Integer.toString(postData.getBytes().length));
                uc.setRequestProperty("Content-Type", "text/html; charset=utf-8");

                OutputStreamWriter outputWriter = new OutputStreamWriter(uc.getOutputStream());
                outputWriter.write(postData);
                outputWriter.flush();
                outputWriter.close();
                System.out.println(uc.getResponseCode()+":::"+uc.getResponseMessage());

            } catch (MalformedURLException e) {
                e.printStackTrace();
                pw.println("Invalid URL");
            } catch (IOException e) {
                e.printStackTrace();
                pw.println("Error reading URL");
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println(sw.toString());
        }

        public static class TempTrustedManager implements
        javax.net.ssl.TrustManager, javax.net.ssl.X509TrustManager {
    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
        return null;
    }

    public boolean isServerTrusted(
            java.security.cert.X509Certificate[] certs) {
        return true;
    }

    public boolean isClientTrusted(
            java.security.cert.X509Certificate[] certs) {
        return true;
    }

    public void checkServerTrusted(
            java.security.cert.X509Certificate[] certs, String authType)
            throws java.security.cert.CertificateException {
        return;
    }

    public void checkClientTrusted(
            java.security.cert.X509Certificate[] certs, String authType)
            throws java.security.cert.CertificateException {
        return;
    }
}

private static void trustAllHttpsCertificates() throws Exception {

    // Create a trust manager that does not validate certificate chains:

    javax.net.ssl.TrustManager[] trustAllCerts =

    new javax.net.ssl.TrustManager[1];

    javax.net.ssl.TrustManager tm = new TempTrustedManager();

    trustAllCerts[0] = tm;

    javax.net.ssl.SSLContext sc =

    javax.net.ssl.SSLContext.getInstance("SSL");

    sc.init(null, trustAllCerts, null);

    javax.net.ssl.HttpsURLConnection.setDefaultSSLSocketFactory(

    sc.getSocketFactory());

}

}
