public class file_updated {
public static Map extra = new HashMap();
public static void main(String[] args) throws Exception {
    File branches = null;
    List map_list = new ArrayList();
    Map get_val=new HashMap();
    List add_apk = new ArrayList();
    File f2 = new File("C:\\Users\\rishii\\Desktop\\new_creation");

    int count=0;
    for (File file : f2.listFiles()) {
        branches=getFilesRecursive(file);
        add_apk.add(branches);
        count=count+1;
    }

            check_sum(add_apk);




}

public  static void check_sum(List file){
 try {

     MessageDigest Digest = MessageDigest.getInstance("MD5");
     Iterator it2 = file.iterator();
     int count=0;
     while(it2.hasNext())

     {
         System.out.println(count=count+1);
         System.out.println(it2.next().toString());
         Digest.update(it2.next().toString().getBytes());
     }

 }
 catch (Exception ex)
 {
     ex.printStackTrace();
 }
 }


public static   File getFilesRecursive(File file)
{

    if(file.isDirectory())
    {
        for(File file1:file.listFiles())
        {

            return file1;
        }

    }

    return  file;
}
