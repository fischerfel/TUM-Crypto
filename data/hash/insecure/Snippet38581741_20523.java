public class batchrun {
public static String md5gen(String a) throws NoSuchAlgorithmException
{
    MessageDigest m= MessageDigest.getInstance("MD5");
    m.reset();
    m.update(a.getBytes());
    byte[] digest=m.digest();
    BigInteger bigInt = new BigInteger(1,digest);
    String hashtext = bigInt.toString(16);
    while(hashtext.length() < 32 ){
      hashtext = "0"+hashtext;
    }
    return hashtext;
}
private static String getInputAsString(InputStream is)
{
   try(java.util.Scanner s = new java.util.Scanner(is)) 
   { 
       return s.useDelimiter("\\A").hasNext() ? s.next() : ""; 
   }
}
public static void main(String[] args) throws InterruptedException {
    try {

        guibuilder.main(args);  
        guibuilder gb=new guibuilder();
        String fg=guibuilder.antd;
        String arg1=gb.arg;
        String userinp1=gb.userinp;

        System.out.println("FG="+fg+" arg1="+arg1+" userinp="+userinp1);


  Process pan =  Runtime.getRuntime().exec(new String[]    {"C:\\test1.bat",arg1,fg});

        pan.waitFor();



            String extra="\\";
            extra+=userinp1;
            String patha=fg+extra;
            ProcessBuilder pb = new     ProcessBuilder("adb","shell","getprop","ro.csc.sales_code");
            Process p=pb.start();
            p.waitFor();
            String stdout = getInputAsString(p.getInputStream());
            String newstring=stdout.substring(0,3);;
            String fn=fg+"\\"+newstring+".txt";
            ZipFile zipFile = new ZipFile(patha);
            Enumeration<?> enu = zipFile.entries();
            int flag=0;
            String so="so";
            File file = new File(fn); 
        FileOutputStream fos = new FileOutputStream(file);
            PrintStream ps = new PrintStream(fos);

            System.setOut(ps);

            while (enu.hasMoreElements()) {
                ZipEntry zipEntry = (ZipEntry) enu.nextElement();
                String name = zipEntry.getName();
                long size= zipEntry.getSize();
                String extension= name.substring(name.lastIndexOf(".")+1,name.length());

                if(extension.equals(so))
                {
                    String plaintext=name+size;
                    String md5result=md5gen(plaintext);
                    System.out.println(name+"   "+size+"   "+md5result);
                    ++flag;

                }

            }
            if(flag==0)
                System.out.println("fail");


}catch (IOException ex){
    System.out.println(ex.getMessage());
} catch (NoSuchAlgorithmException e) {
    // TODO Auto-generated catch block
    e.printStackTrace();
}
    }

}
