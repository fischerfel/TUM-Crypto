 import java.io.BufferedWriter;
 import java.io.ByteArrayInputStream;
 import java.io.File;
 import java.io.FileInputStream;
 import java.io.FileNotFoundException;
 import java.io.FileWriter;
 import java.io.IOException;
 import java.io.InputStream;
 import java.io.SequenceInputStream;
 import java.security.DigestInputStream;
 import java.security.MessageDigest;
 import java.security.NoSuchAlgorithmException;
 import java.text.SimpleDateFormat;
 import java.util.ArrayList;
 import java.util.Arrays;
 import java.util.Collections;
 import java.util.Date;
 import java.util.List;

 public class RestartTest {
  StringBuilder sb;
  String dtf = "============================";
  String hexRes2 = "";
  int i1 = 0;
  int i2 = 0;
  /**
  * @param args the command line arguments
  */
  public static void main(String[] args) throws InterruptedException,   IOException, NoSuchAlgorithmException {
    // TODO code application logic here
    new RestartTest().startApp();
  }

public void startApp() throws InterruptedException, IOException, NoSuchAlgorithmException {
  TaskStart startTask = new TaskStart();
  startTask.startCalc();
}

class TaskStart {
 public void startCalc() throws InterruptedException, IOException, NoSuchAlgorithmException {
  while(!Thread.currentThread().isInterrupted()) {
   i1 = (int) (Math.random() * 1000);
   System.out.println("Value 1: " + i1);
   new TaskStart2().startCalc2();
   new TaskStartPart().calculHash();
   dateiSpeichern(i1,i2,"");
  }
 }
}

class TaskStart2 {
 public void startCalc2() throws InterruptedException, IOException {
  i2 = (int) (Math.random() * 1000);
  System.out.println("Value 2: " + i2);
  dateiSpeichern(i1,i2,"");
 }
}

class TaskStartPart {
 public void calculHash() throws InterruptedException, IOException, NoSuchAlgorithmException {
  try {   
  DigestInputStream digestInputStream=null ;

  MessageDigest messageDigest=MessageDigest.getInstance("SHA-512") ;
  digestInputStream=new DigestInputStream(new TaskPart(new  File("C:\\Users\\win7p\\Documents/t")),messageDigest) ;
  //System.out.println("Path  :" + direc.toString()) ;
  while(digestInputStream.read()>=0) ;
  //System.out.print("\nsha-512 sum=") ;
   for(byte b: messageDigest.digest()) {
    hexRes2 += String.format("%02x",b);
   } sb = new StringBuilder(hexRes2);  
   dateiSpeichern(0,0,sb.substring(hexRes2.length() - 128,hexRes2.length())); System.out.println(sb.substring(hexRes2.length() - 128,hexRes2.length()));
  digestInputStream.close();
  } catch (IOException ex ) {ex.printStackTrace();}       
 }
}   

  class TaskPart extends InputStream {  
    private File mFile ;
    private List<File> mFiles ;
    private InputStream mInputStream ;

    public TaskPart(File file) throws FileNotFoundException {
     mFile=file ;
     if(file.isDirectory()) {
     mFiles=new ArrayList<File>(Arrays.asList(file.listFiles())) ;
     Collections.sort(mFiles) ;
     mInputStream=nextInputStream() ;
    } else {
       mFiles=new ArrayList<File>() ;
       mInputStream=new FileInputStream(file) ;
     }
}

@Override
public int read() throws IOException {
 int result=mInputStream==null?-1:mInputStream.read() ;
 if(result<0 && (mInputStream=nextInputStream())!=null)
 return read() ;
 else return result ;
}

protected String getRelativePath(File file) {
 return file.getAbsolutePath().substring(mFile.getAbsolutePath().length()) ;
}

protected InputStream nextInputStream() throws FileNotFoundException {
 if(!mFiles.isEmpty()) {
  File nextFile=mFiles.remove(0) ;
  return new SequenceInputStream(
  new ByteArrayInputStream(getRelativePath(nextFile).getBytes()),
  new TaskPart(nextFile)) ;
 }
 else return null ;
    }
  }

private void dateiSpeichern(int i1, int i2, String hexR) throws InterruptedException, IOException {
 try { 
  String tF = new SimpleDateFormat("dd-MM-yyyy HH-mm-ss").format(new  Date().getTime());
  try (BufferedWriter writer = new BufferedWriter(new FileWriter("C:\\Users\\win7p\\Documents/hashLog.txt", true))) {
   writer.append(tF);
   writer.newLine();
   writer.append(dtf);
   writer.newLine();
   writer.append("Hash Value: ");
   //If(hexR.length() == alHash.get(0))
   //alHash.add(hexR);
   writer.append(hexR);
   writer.newLine();
   writer.append("-----");
   writer.append("Value 1:");
   String si1  = Integer.toString(i1);
   writer.append(si1);
   writer.newLine();
   writer.append("*****");
   writer.append("Value 2:");
   String si2  = Integer.toString(i2);
   writer.append(si2);
   writer.newLine();
   writer.flush();
   writer.close();
  }
 } catch(IOException ex) {System.out.print("konnte Datei nicht speichern");}
   catch(NullPointerException nex) {System.out.println("no Log-File, try again...");}
}    }
