import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class hash extends Thread {

    private Thread t = null;
    private MessageDigest md = null;
    private StringBuffer sb = null;
    private String message = null;
    private ArrayList<String> list = null;
    private int count = 0;

    public hash(ArrayList<String> list) {
        this.list = list;
    }

    public final void mdstart() {

        for(String item:list){
            this.message=item;
            if(t==null){
                t = new Thread(this);
                t.start();
            }
            count++;
        }
        System.out.println("end: "+this.count);

    }

    @Override
    public final void run() {
        System.out.println("run: "+this.count);
        try {
            md = MessageDigest.getInstance("MD5");
            md.update(this.message.getBytes());

            byte[] digest = md.digest();
            sb = new StringBuffer();
            for (byte hash : digest) {
                sb.append(String.format("%02x", hash));
            }
            System.out.println(this.message + " : " + sb.toString());
        } catch (NoSuchAlgorithmException ex) {
            System.out.println("no such algorithm exception : md5");
            System.exit(1);
        }
    }

    public static void main(String args[]) {

        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add("message" + i);
        }
        new hash(list).mdstart();

    }

}
