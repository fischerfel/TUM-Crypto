    import java.io.*;
    import java.net.*;
    import java.security.MessageDigest;
    import java.security.NoSuchAlgorithmException;
    import java.util.*;

    public class EchoServer2 {

        // numer hosta
        private static int liczba = 1;
        // zadeklaruj gĹ‚Ăłwne gniazdo serwera
        private static ServerSocket echoServer = null;
        // gniazdo do poĹ‚Ä…czeĹ„ z klientem
        private static Socket clientSocket = null;
        // strumieĹ„ do zapisu do gniazda
        private static PrintWriter out = null;
        // strumieĹ„ do odczytu z gniazda
        private static BufferedReader in = null;

        private static BufferedReader NazwaPliku;

        public static void main(String args[]) throws IOException, NoSuchAlgorithmException {

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
                    listaPlikow.append(" ");
                    listaPlikow.append(md5(file));
                    listaPlikow.append(" ");

                }
            }

            // prĂłba otwarcia gniazda na porcie 9999
            try {
                System.out.println("PrĂłba utworzenia gniazda gĂłwnego serwera");
                echoServer = new ServerSocket(16000);
                System.out.println("Gniazdo utworzone");
            } catch (IOException e) {
                System.out.println(e);
                System.exit(1);
            }

            // oczekiwanie na klienta, a po podĹ‚Ä…czeniu utworzenie strumieni i
            // komunikacja
            try {
                System.out.println("Czekam na accept");
                clientSocket = echoServer.accept();
                System.out.println("Klient siÄ™ poĹ‚Ä…czyĹ‚:");
                InetAddress address = clientSocket.getInetAddress();
                int port = clientSocket.getPort();
                System.out.println("z adresu " + address.toString() + ":" + port);
                // utwĂłrz nowy strumieĹ„ do odczytu
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                // utwĂłrz nowy strumieĹ„ do zapisu
                out = new PrintWriter(clientSocket.getOutputStream(), true);

                // wyslanie listy plikow do klienta
                // out.println(listaPlikow);

                // wyswietlnie listy plikow od klienta

                // bufor na dane od klienta
                BufferedReader instr = new BufferedReader(new InputStreamReader(System.in));

                String zmienna = null;
                String check;

                // odbieranie danych od innego hosta
                int liczy = 0;
                while (true) {

                    System.out.println("instrukcje");
                    System.out.println(zmienna);
                    while ((zmienna = in.readLine()) != null) {
                        check = zmienna.toString();

                        switch (check) {
                        case ("lista"):
                            while ((zmienna = in.readLine()) != null) {
                                System.out.println(zmienna);
                                System.out.println("instrukcje");

                                break;
                            }
                            break;
                        case ("PULL"):
                            // if(check.equals("PULL")){
                            System.out.println("proba wyslania pliku ");
                            while ((zmienna = in.readLine()) != null) {
                                System.out.println("wysylanie pliku: " + zmienna);
                                check = zmienna.toString();
                                PUSHfile(check);
                                System.out.println("instrukcje");
                                System.out.println(zmienna);
                                break;
                            }
                            break;
                        // }
                        // (check.equals("PUSH")){
                        case ("PUSH"):
                            System.out.println("proba odebrania pliku ");
                            while ((zmienna = in.readLine()) != null) {
                                System.out.println("odbieranie pliku o nazwie: " + zmienna);
                                check = zmienna.toString();
                                PULLfile(check);
                                System.out.println("instrukcje");
                                System.out.println(zmienna);

                                break;
                            }
                            break;

                        case ("ZAKONCZ"):
                            // zamknij wszystko
                            in.close();
                            out.close();
                            clientSocket.close();
                            echoServer.close();

                        }

                    }

                }
            } catch (IOException e) {
                System.out.println(e);
            }
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

        public static void PUSHfile(String filename) throws IOException {
            OutputStream out3 = null;
            DataInputStream in3 = null;

            try {
                File file = new File("C:\\TORrent_" + liczba + "/" + filename);

                int dlugosc = (int) file.length();
                // System.out.print(dlugosc + " "+ filename);
                BufferedInputStream plik = new BufferedInputStream(new FileInputStream(file));
                // ilosc bitow rowna wielkosci pliku
                byte[] bity = new byte[dlugosc];
                in3 = new DataInputStream(plik);
                // wyslanie pliku i jego wielkosci
                out3 = clientSocket.getOutputStream();
                int licznik = 0;

                while ((dlugosc = in3.read(bity)) > 0) {
                    out3.write(bity, 0, dlugosc);
                    licznik += dlugosc;

                }
                out3.close();
                in3.close();
                System.out.println("dlugosc w bitach " + licznik);

            } catch (FileNotFoundException ex) {
                System.out.println("File not found. ");

            }
        }

        public static void PULLfile(String filename) throws IOException {
            out.println(filename);
            // System.out.println(filename);
            OutputStream out2 = null;
            InputStream in2 = null;
            File file = new File("C:\\TORrent_" + liczba + "/" + filename);

            try {
                in2 = clientSocket.getInputStream();

                out2 = new FileOutputStream(file);
                // DataInputStream dane =(DataInputStream) in;
                BufferedInputStream plik = new BufferedInputStream(new FileInputStream(file));
                byte[] bity = new byte[16 * 1024];

                // int wielkosc = dane.readInt();
                int licznik;
                int dlugosc = 0;

                while ((licznik = in2.read(bity)) > 0) {
                    out2.write(bity, 0, licznik);
                    dlugosc += licznik;
                }

                System.out.println("dlugosc w bitach " + dlugosc);
                out2.close();
                in2.close();

            } catch (FileNotFoundException ex) {
                System.out.println("File not found. ");
            }

        }

    }
