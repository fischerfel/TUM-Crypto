    import java.util.*;
    import java.util.stream.Stream;
    import java.net.*;
    import java.nio.file.Files;
    import java.nio.file.Path;
    import java.nio.file.Paths;
    import java.security.DigestInputStream;
    import java.security.MessageDigest;
    import java.security.NoSuchAlgorithmException;
    import java.io.*;

    public class EchoClient2 {
        // numer hosta
        private static int liczba = 2;
        // gniazdo do poĹ‚Ä…czenia z serwerem
        private static Socket echoSocket = null;
        // strumieĹ„ do zapisu do serwera
        private static PrintWriter out = null;
        // strumieĹ„ do odczytu z serwera
        private static BufferedReader in = null;

        private static BufferedReader NazwaPliku = null;

        public static void main(String[] args) throws IOException, NoSuchAlgorithmException {

            File folder = new File("C:\\TORrent_" + liczba);
            try {
                if (folder.mkdir()) {
                    System.out.println("Tworze folder");
                } else {
                    System.out.println("folder juz ustnieje");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            // lista plikow i md5
            StringBuffer listaPlikow = new StringBuffer();
            File[] listOfFiles = folder.listFiles();

            for (File file : listOfFiles) {
                if (file.isFile()) {
                    System.out.println(file.getName());
                    System.out.println(md5(file));
                    listaPlikow.append(file.getName());
                    listaPlikow.append("  ");
                    listaPlikow.append(md5(file));
                    listaPlikow.append("  ");

                }
            }

            // nazwa serwera

            String hostname = "localhost";
            if (args.length > 0)
                hostname = args[0];

            try {
                System.out.println("prĂłba utworzenia gniazda");
                echoSocket = new Socket(hostname, 16000);
                System.out.println("prĂłba utworzenia strumienia wyjĹ›ciowego");
                out = new PrintWriter(echoSocket.getOutputStream(), true);
                System.out.println("prĂłba utworzenia strumienia wejĹ›ciowego");
                in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
            } catch (UnknownHostException e) {
                System.err.println("Nieznany host: " + hostname + ".");
                System.exit(1);
            } catch (IOException e) {
                System.err.println("BĹ‚Ä…d poĹ‚Ä…czenia z " + hostname + ".");
                System.exit(1);
            }

            // wyslanie listy plikow do serwera
            // out.println(listaPlikow);

            // wyswietlanie listy plikow od serwera

            BufferedReader instr = new BufferedReader(new InputStreamReader(System.in));
            String zmienna;
            String check;

            // wysylanie danych do innego hosta

            while (true) {

                System.out.println("Wybierz instrukcje");
                System.out.println("lista-wyslanie listy plikow ,PULL(pobranie pliku), PUSH(wyslanie pliku,ZAKONCZ");
                while ((zmienna = instr.readLine()) != null) {
                    check = zmienna.toString();
                    String nazwaPliku = null;

                    switch (check) {
                    case ("lista"):
                        out.println("lista");
                        out.println(listaPlikow);
                        System.out.println("Wybierz instrukcje");
                        System.out
                                .println("lista-wyslanie listy plikow ,PULL(pobranie pliku), PUSH(wyslanie pliku,ZAKONCZ");

                        break;
                    case ("PULL"):
                        System.out.println("pobieranie pliku: ");
                        // out.println("PULL");
                        while ((zmienna = instr.readLine()) != null) {
                            nazwaPliku = zmienna.toString();
                            System.out.println("nazwa pliku do pobrania: " + nazwaPliku);
                            PULLfile(nazwaPliku);
                            System.out.println("Wybierz instrukcje");
                            System.out.println(
                                    "lista-wyslanie listy plikow ,PULL(pobranie pliku), PUSH(wyslanie pliku,ZAKONCZ");

                            break;
                        }
                        break;
                    case ("PUSH"):
                        System.out.println("wprowadz nazwe pliku: ");
                        // out.println("PUSH");
                        while ((zmienna = instr.readLine()) != null) {
                            nazwaPliku = zmienna.toString();
                            System.out.println("wysylanie pliku: " + nazwaPliku);
                            PUSHfile(nazwaPliku);
                            System.out.println("Wybierz instrukcje");
                            System.out.println(
                                    "lista-wyslanie listy plikow ,PULL(pobranie pliku), PUSH(wyslanie pliku,ZAKONCZ");

                            break;
                        }
                        break;

                    case ("ZAKONCZ"):
                        out.println("ZAKONCZ");
                        out.close();
                        in.close();
                        instr.close();
                        // stdIn.close();
                        echoSocket.close();
                        // break;

                    }

                }
            }

            // zakoĹ„czenie pracy - pozamykaj strumienie i gniazda

        }

        public static String md5(File plik) throws NoSuchAlgorithmException, IOException {
            MessageDigest md = MessageDigest.getInstance("MD5");
            FileInputStream fis = new FileInputStream(plik);

            byte[] dataBytes = new byte[1024];

            int nread = 0;
            while ((nread = fis.read(dataBytes)) != -1) {
                md.update(dataBytes, 0, nread);
            }
            ;
            byte[] mdbytes = md.digest();

            // convert the byte to hex format method 1
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < mdbytes.length; i++) {
                sb.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16).substring(1));
            }

            // System.out.println("Digest(in hex format):: " + sb.toString());
            String hexy = sb.toString();
            return hexy;
        }

        public static void PULLfile(String filename) throws IOException {
            out.println("PULL");
            out.println(filename);
            // System.out.println(filename);
            OutputStream out = null;
            InputStream in = null;
            File file = new File("C:\\TORrent_" + liczba + "/" + filename);

            try {
                in = echoSocket.getInputStream();

                out = new FileOutputStream(file);
                DataInputStream dane = new DataInputStream(in);
                BufferedInputStream plik = new BufferedInputStream(new FileInputStream(file));
                byte[] bity = new byte[1024];

                int licznik;
                int dlugosc = 0;
                while ((licznik = in.read(bity)) > 0) {
                    out.write(bity, 0, licznik);
                    dlugosc += licznik;
                }
                out.close();
                in.close();
                System.out.println("dlugosc w bitach " + dlugosc);

            } catch (FileNotFoundException ex) {
                System.out.println("File not found. ");
            }

        }

        public static void PUSHfile(String filename) throws IOException {
            out.println("PUSH");
            out.println(filename);
            DataInputStream in2 = null;
            OutputStream out2 = null;

            try {
                File file = new File("C:\\TORrent_" + liczba + "/" + filename);

                int dlugosc = (int) file.length();
                // System.out.print(dlugosc + " "+ filename);
                BufferedInputStream plik = new BufferedInputStream(new FileInputStream(file));
                // ilosc bitow rowna wielkosci pliku
                byte[] bity = new byte[dlugosc];
                in2 = new DataInputStream(plik);
                // wyslanie pliku i jego wielkosci
                out2 = echoSocket.getOutputStream();

                int dlug = 0;

                while ((dlugosc = in2.read(bity)) > 0) {
                    out2.write(bity, 0, dlugosc);
                    dlug += dlugosc;

                }
                out2.close();
                in2.close();
                System.out.println("dlugosc w bitach " + dlug);

            } catch (FileNotFoundException ex) {
                System.out.println("File not found. ");
            }

        }

    }
