//1 Entry point for thread with run method
public void run() {
    System.out.println("Hashmaker():/run(). " + threadName + " Running "  );
    try {
        String original = in;
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(original.getBytes());
        byte[] digest = md.digest();
        StringBuffer sb = new StringBuffer();
        for (byte b : digest) {
            sb.append(String.format("%02x", b & 0xff));
            System.out.println("Hashmaker:/run(). original: " + original);
            System.out.println("Hashmaker:/run(). digested(hex):" + sb.toString());
            //src http://www.avajava.com/tutorials/lessons/how-do-i-generate-an-md5-digest-for-a-string.html
        }
            // Let the thread sleep for a while.
            //Thread.sleep(7);
            System.out.println("Hashmaker:/run(). Thread: " + threadName + " recieved a clear password = " + in);
            //Save the hash
            hash=sb.toString();
    }
    /*catch (InterruptedException e) {
        System.out.println("Hashmaker:/run(). Thread " +  threadName + " interrupted.");
    } */
    catch (NoSuchAlgorithmException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
    System.out.println("Hashmaker:/run(). Thread " +  threadName + " exiting.");
}
