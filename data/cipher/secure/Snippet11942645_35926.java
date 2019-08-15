private static final ThreadLocal<Cipher> localDigest = new ThreadLocal<Cipher>() {
    @Override
    protected Cipher initialValue() {
        try {
            return Cipher.getInstance("RSA");
        } catch (Exception e) {
            // ugly but necessary
            throw new RuntimeException(e);
        }
    }
};

public static void main(String[] args) {
    new Thread(new MyRunnable()).start();
    new Thread(new MyRunnable()).start();
}

private static class MyRunnable implements Runnable {
    @Override
    public void run() {
        Cipher cipher = localDigest.get();
        System.out.println("Got digest " + System.identityHashCode(cipher));
        ...
    }
}
