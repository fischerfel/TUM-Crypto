// *****************************************
    package pl.scriptease.studia.prir;

import java.security.MessageDigest;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class BruteForce extends Thread {

    public static Object lock = new Object();
    public static GUI gui;
    static String sha1 = "";
    static int length = 1;
    static int numberOfThreads = 1;
    private static int nextThreadId = 1;
    static String textAreaBuffer = "";
    static String password = "";
    static ExecutorService executor;
    static Set<Thread> threadSet;
    volatile boolean running = false;
    boolean foundPassword = false;
    int threadId = 0;
    long startTime;
    long endTime;
    long elapsedTime;
    String start;
    String temporary;
    String end;
    static char[] searcharea = "0123456789abcdefghijklmnopqrstuwxyz".toCharArray();

    void loop() {
    temporary = start;

    while (running) {

        // print(this + " checking: " + temp);
        if (check(temporary)) {
        foundPassword = true;
        password = temporary;
        gui.passwordFound(password);
        print("password was found: '" + temporary + "'");
        this.interrupt();
        shutdown();
        break;
        }

        temporary = search(temporary);

        if (interrupted()) {
        break;
        }

        if (temporary.length() > length) {
        break;
        }

        if (temporary.length() >= length && temporary.startsWith(end) && end.charAt(0) != last()) {
        break;
        }
    }

    running = false;
    }

    public String search(String temp) {

    String sample = "";
    int length = temp.length();
    char[] a = temp.toCharArray();

    for (int i = length - 1; i >= 0; i--) {

        if (i == 0) {
        if (a[i] == end.charAt(0)) {
            a[i] = start.charAt(0);
            sample = new String(a) + first();
            break;
        }
        }

        a[i] = next(a[i]);  // next character to match with original password 

        if (a[i] != first()) {
        sample = new String(a);
        break;
        } else {
        if (i == 0) {
            sample = new String(a) + first();
        }
        }
    }

    return sample;
    }

    private static char next(char ch) {
    if (ch == searcharea[searcharea.length - 1]) {
        return searcharea[0];
    } else {
        return searcharea[indexOf(ch) + 1];
    }
    }

    static boolean check(String s) {
    return toSha1(s).equals(sha1);
    }   // compare SHA codes 

    static char first() {
    return searcharea[0];
    }

    static char last() {
    return searcharea[searcharea.length - 1];
    }

    static int indexOf(char c) {
    for (int i = 0; i < searcharea.length; ++i) {
        if (searcharea[i] == c) {
        return i;
        }
    }
    return -1;
    }

    BruteForce(String start, String end) {
    this.start = start;
    this.end = end;
    threadId = nextThreadId;
    ++nextThreadId;
    }   // Gives Id to the threads that we later will see on the log window 

    public void run() {
    print("started on interval [" + start + ", " + end + "]");
    startTime = System.currentTimeMillis();
    running = true;
    loop();
    endTime = System.currentTimeMillis();
    elapsedTime = endTime - startTime;
    print(String.format("ended with time:  %.2f sec", elapsedTime / 1e3));
    }

    public String toString() {
    return "[T" + threadId + "]";   
    }   // return name of the thread that do the job in the log window

    public static String toSha1(String input) {
    try {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        return byteArrayToHexString(md.digest(input.getBytes("UTF-8")));
    } // convert password that you write down to the SHA code 
    catch (Exception e) {
        return null;
    }
    }

    private static String byteArrayToHexString(byte[] b) {
    String result = "";
    for (int i = 0; i < b.length; i++) {
        result += Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1);
    }
    return result;
    }

    public static void decrypt() {

    threadSet = new HashSet<Thread>();

    int searchAreaLength = searcharea.length;
    int interval = (int) Math.round(searchAreaLength / numberOfThreads);    // the works divided between thread equally 

    executor = Executors.newFixedThreadPool(numberOfThreads);
//  executor = Executors.newCachedThreadPool(); 
    for (int i = 0; i < numberOfThreads; ++i) {

        int start = i * interval;
        int end = (i + 1) * interval;

        if (end >= searchAreaLength) {
        end = searchAreaLength - 1;
        }

        Thread worker = new BruteForce(searcharea[start] + "", searcharea[end] + "");
        executor.execute(worker);
        threadSet.add(worker);
    }
    }

    public static void shutdown() {
    try {
        executor.shutdown();
        nextThreadId = 1;
        sleep(500);
        executor.shutdownNow();

    } catch (NullPointerException e) {

    } catch (InterruptedException e) {
    }
    }

    private void print(String text) {
    String time = String.format("%tT: ", Calendar.getInstance());
    synchronized (lock) {
        textAreaBuffer += (time + this + " : " + text + "\n");
    }
    }

    public static String pullTextAreaBuffer() {
    synchronized (lock) {
        String ret = textAreaBuffer;
        textAreaBuffer = "";
        return ret;
    }
    }
}
