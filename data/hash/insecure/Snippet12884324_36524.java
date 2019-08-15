        public class Md5tester {
private String licenseMd5 = "?jZ2$??f???%?";

public Md5tester(){
    System.out.println(isLicensed());
}
public static void main(String[] args){
    new Md5tester();
}
public boolean isLicensed(){
    File f = new File("C:\\Some\\Random\\Path\\toHash.txt");
    if (!f.exists()) {
        return false;
    }
    try {
        BufferedReader read = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
        //get line from txt
        String line = read.readLine();
        //output what line is
        System.out.println("Line read: " + line);
        //get utf-8 bytes from line
        byte[] lineBytes = line.getBytes("UTF-8");
        //declare messagedigest for hashing
        MessageDigest md = MessageDigest.getInstance("MD5");
        //hash the bytes of the line read
        String hashed = new String(md.digest(lineBytes), "UTF-8");
        System.out.println("Hashed as string: " + hashed);
        System.out.println("LicenseMd5: " + licenseMd5);
        System.out.println("Hashed as bytes: " + hashed.getBytes("UTF-8"));
        System.out.println("LicenseMd5 as bytes: " + licenseMd5.getBytes("UTF-8"));
        if (hashed.equalsIgnoreCase(licenseMd5)){
            return true;
        }
        else{
            return false;
        }
    } catch (FileNotFoundException e) {
        return false;
    } catch (IOException e) {           
        return false;
    } catch (NoSuchAlgorithmException e) {  
        return false;
    }
} 

}
