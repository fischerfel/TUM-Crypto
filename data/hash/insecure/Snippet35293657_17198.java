public class BruteWorker implements Runnable {
    private static final char[] alphabet = "eaistnrulodmpcvqgbfjhzxykw0123456789!@#$%&*".toCharArray();
    private static final int maxLength = 8;
    private static Map<String, String> hashes;
    private static MessageDigest md ;
    private char[] partition;
    private long nbTries = 0;

    BruteWorker(char[] partition, Map<String, String> hashes) {
        this.partition = partition;
        this.hashes = hashes;
    }

    public void run() {
        System.out.println("New thread (id = "+ Thread.currentThread().getId() +")");
        try {
            md = MessageDigest.getInstance("MD5");
            for(char c : this.partition){
                stringPossibilities(String.valueOf(c), maxLength);
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        System.out.println("End of thread (id = "+ Thread.currentThread().getId() +")");
    }

    //Recursive class
    private void stringPossibilities(String prefix, int length) {
        nbTries++;
        if((nbTries % 10000000) == 0){
            System.out.println(nbTries + " tries on thread id = "+ Thread.currentThread().getId());
        }
        md.update(prefix.getBytes());
        byte[] bytes = md.digest();
        String md5 = getMd5Hash(prefix);

        if (hashes.containsKey(md5)){
            System.out.println(prefix + " = " + md5);
        }


        if (prefix.length() < length){
            for (int i = 0; i < alphabet.length; i++){
                char c = alphabet[i];
                stringPossibilities(prefix + c, length);
            }
        }
    }

    private String getMd5Hash(String prefix){
        md.update(prefix.getBytes());
        byte[] bytes = md.digest();
        StringBuilder md5 = new StringBuilder();
        for(int j =0; j < bytes.length; j++){
            md5.append(Integer.toString((bytes[j] & 0xff) + 0x100, 16).substring(1));
        }
        return md5.toString();
    }

}
