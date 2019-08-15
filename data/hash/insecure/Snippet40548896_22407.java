import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class CompareTwoFiles {
    static int count1 = 0 ;
    static int count2 = 0 ;

    static String arrayLines1[] = new String[countLines("\\Files_Comparison\\File11.txt")];
    static String arrayLines2[] = new String[countLines("\\Files_Comparison\\File12.txt")];

    public static void main(String args[]) throws Throwable{  
        long lStartTime = new Date().getTime();
        System.out.println("File1 count=" + countLines("\\Files_Comparison\\File11.txt"));
        System.out.println("File2 count=" + countLines("\\Files_Comparison\\File12.txt"));
        MessageDigest md_1 = MessageDigest.getInstance("MD5");
        MessageDigest md_2 = MessageDigest.getInstance("MD5");
        InputStream is_1 = new FileInputStream("\\Files_Comparison\\File11.txt");
        InputStream is_2 = new FileInputStream("\\Files_Comparison\\File12.txt");
        try {
          is_1 = new DigestInputStream(is_1, md_1);
          is_2 = new DigestInputStream(is_2, md_2);
        }
        finally {
          is_1.close();
          is_2.close();
        }
        byte[] digest_1 = md_1.digest();
        byte[] digest_2 = md_2.digest();
        System.out.println(digest_1);
        System.out.println(digest_2);
        if (digest_1.equals(digest_2)) {
            System.out.println("File Comparison Process Completed");
            System.out.println("Both files are same");
        }
        else 
        {
        findDifference("\\Files_Comparison\\File11.txt","\\Files_Comparison\\File12.txt");
        displayRecords();
        System.out.println("File Comparison Process Completed");
        }
        long lEndTime = new Date().getTime();
        long difference = lEndTime - lStartTime; 
        String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(difference),
                TimeUnit.MILLISECONDS.toMinutes(difference) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(difference)),
                TimeUnit.MILLISECONDS.toSeconds(difference) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(difference)));
        System.out.println(hms);
    }

    public static int countLines(String File){

        int lineCount = 0;
        try {
           BufferedReader br = new BufferedReader(new FileReader(File));
           while ((br.readLine()) != null) {
               lineCount++;
           }

       } catch (FileNotFoundException e) {
           e.printStackTrace();
       } catch (IOException e) {
           e.printStackTrace();
       }
           return lineCount;
    }

    public static void findDifference(String File1, String File2){
        String contents1 = null;  
        String contents2 = null; 
        try  
        {  
            FileReader file1 = new FileReader(File1);  
            FileReader file2 = new FileReader(File2);
            BufferedReader buf1 = new BufferedReader(file1); 
            BufferedReader buf2 = new BufferedReader(file2);

           while ((contents1 = buf1.readLine()) != null)  
            {  
               arrayLines1[count1] = contents1 ;
               count1++;
            }  

           while ((contents2 = buf2.readLine()) != null)  
            {  
               arrayLines2[count2] = contents2 ;
               count2++;
            }
       }catch (Exception e){
           e.printStackTrace();
       }
}





    public static void displayRecords() { 

        for (int i = 0 ; i < arrayLines1.length && i < arrayLines2.length; i++) 
        {    
            String a = arrayLines1[i];  
            String b = arrayLines2[i];  

            if(!a.contains(b)) {  
                   System.out.println(a);  
            }  
        }
    }

}
