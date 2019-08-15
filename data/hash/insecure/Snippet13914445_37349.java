import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Share {

  final byte[] bytes = new byte[100];
  final MessageDigest sharedDigest;
  final ExecutorService pool;
  int threads = 100;

  Share() throws NoSuchAlgorithmException {
    sharedDigest = MessageDigest.getInstance("MD5");
    pool = Executors.newFixedThreadPool(threads);
  }

  void go() {

    for (int i=0; i<threads; i++) {
      pool.execute(new Runnable() {
        public void run() {
          long start = System.currentTimeMillis();
          for (int i=0; i<1000000; i++) {
            /*
            synchronized (sharedDigest) {
              sharedDigest.reset();
              sharedDigest.update(bytes);
              sharedDigest.digest();
            }*/
            try {
              MessageDigest digest = MessageDigest.getInstance("MD5");
              digest.reset();
              digest.update(bytes);
              digest.digest();
            } catch (Exception ex) {
              ex.printStackTrace();
            }
          }
          long end = System.currentTimeMillis();
          System.out.println(end-start);
          pool.shutdown();
        }
      });
    }

  }

  public static void main(String[] args) throws Exception {
    Share share = new Share();
    share.go();
  }

}
