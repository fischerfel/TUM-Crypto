public class ClientHandler implements Runnable {
Socket msock;
DataInputStream dis;
DataOutputStream dos;
String ip;
private miniClientHandler(Socket msock) {
            this.msock = msock;
        }
@Override
public void run() {
    try {
        dis = new DataInputStream(msock.getInputStream());
        dos = new DataOutputStream(msock.getOutputStream());

        ip = msock.getInetAddress().getHostAddress();

        String msg = dis.readLine();
        System.out.println(msg);

        if (msg.equals("Download")) {

                    String file2dl = dis.readLine(); //2
                    File file = new File(sharedDirectory.toString() + "\\" + file2dl);
                    dos.writeLong(file.length()); //3+

                    //AES-128 bit key initialization.
                    byte[] keyvalue = "AES128BitPasswd".getBytes();
                    SecretKey key = new SecretKeySpec(keyvalue, "AES");

                    //Initialize the Cipher.
                    Cipher encCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
                    encCipher.init(Cipher.ENCRYPT_MODE, key);

                    //Get the IV from cipher.
                    IvParameterSpec spec = null;
                    try {
                        spec = encCipher.getParameters().getParameterSpec(IvParameterSpec.class);
                    } catch (InvalidParameterSpecException ex) {
                        Logger.getLogger(PeersController.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    byte[] iv = spec.getIV();

                    dos.write(iv, 0, iv.length); //4+

                    //Encryption Mechanism.
                    try (FileInputStream fis = new FileInputStream(file)) {
                        try (CipherOutputStream cos = new CipherOutputStream(dos, encCipher)) {
                            int read;
                            byte[] buffer = new byte[1024 * 1024];
                            while ((read = fis.read(buffer)) != -1) {
                                cos.write(buffer, 0, read); //5+ due to dos as underlying parameter of cos.
                            }
                        }
                    }

                    String message = dis.readLine();
                    System.out.println(message);
                    if (message.equals("Fetching Done")) {
                        System.out.println("Fetching Done!");
                    } else if (message.equals("Fetching Drop")) {
                        System.out.println("Fetching Denied!");
                    }
                }
            } catch (IOException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException e) {
                System.out.println(e.getMessage());
            }
        }

    }
