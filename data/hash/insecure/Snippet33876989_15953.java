    import org.apache.commons.io.input.TeeInputStream;

    import java.io.*;
    import java.security.MessageDigest;
    import java.util.Arrays;
    import java.util.concurrent.ExecutorService;
    import java.util.concurrent.Executors;
    import java.util.concurrent.Future;

    public class Test1 {

        private static final ExecutorService executor = Executors.newFixedThreadPool(2);

        public static boolean compareStreams(InputStream is1, InputStream is2) throws Exception {
            // create pipe that will copy data from is1 to pipe accessible by pis1
            final PipedOutputStream pos1 = new PipedOutputStream();
            final PipedInputStream pis1 = new PipedInputStream(pos1, 1024);
            final TeeInputStream tee1 = new TeeInputStream(is1, pos1, true);

            // create pipe that will copy data from is2 to pipe accessible by pis2
            final PipedOutputStream pos2 = new PipedOutputStream();
            final PipedInputStream pis2 = new PipedInputStream(pos2, 1024);
            final TeeInputStream tee2 = new TeeInputStream(is2, pos2, true);

            class Comparator implements Runnable {
                private final InputStream is;
                final MessageDigest md = MessageDigest.getInstance("MD5");

                public Comparator(InputStream is) throws Exception {
                    this.is = is;
                }

                @Override
                public void run() {
                    byte[] arr = new byte[1024];
                    int read = 0;
                    try {
                        while ((read = is.read(arr)) >= 0) {
                            md.update(arr, 0, read);
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            Comparator comparatorIs1 = new Comparator(pis1);
            Future<?> f1 = executor.submit(comparatorIs1);
            Comparator comparatorIs2 = new Comparator(pis2);
            Future<?> f2 = executor.submit(comparatorIs2);

            Reader r1 = new InputStreamReader(is1);
            Reader r2 = new InputStreamReader(is2);

            char[] c1 = new char[1024];
            char[] c2 = new char[1024];

            int read1 = 0;
            int read2 = 0;

            boolean supposeEquals = true;

            while (supposeEquals) {
                read1 = r1.read(c1);
                read2 = r2.read(c2);
                if (read1 != read2 || (read1 < 0 && read2 < 0)) {
                    break;
                }
                for (int i = 0; i < read1; i++) {
                    if (c1[i] != c2[i]) {
                        supposeEquals = false;
                        break;
                    }
                }
            }

            f1.cancel(true);
            f2.cancel(true);

            return read1 == read2 && supposeEquals && Arrays.equals(comparatorIs1.md.digest(), comparatorIs2.md.digest());
        }

        public static void main(String[] args) throws Exception {
            System.out.println("Comparison result : " + compareStreams(new ByteArrayInputStream("test string here".getBytes()), new ByteArrayInputStream("test string here".getBytes())));
            System.out.println("Comparison result : " + compareStreams(new ByteArrayInputStream("test string test".getBytes()), new ByteArrayInputStream("test string here".getBytes())));
            System.out.println("Comparison result : " + compareStreams(new ByteArrayInputStream("test".getBytes()), new ByteArrayInputStream("test string here".getBytes())));
        }

    }
