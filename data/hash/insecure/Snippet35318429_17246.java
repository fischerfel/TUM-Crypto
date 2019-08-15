public void run() {
    System.out.println("New thread (id = "+ Thread.currentThread().getId() +")");
    try {
        md = MessageDigest.getInstance("MD5");
        for(char c : this.partition){
            stringPossibilities(String.valueOf(c), maxLength);
        }
    } catch (Exception e) {
        e.printStackTrace();
    } finally {
        System.out.println("End of thread (id = "+ Thread.currentThread().getId() +")");
    }
}
