public class frame1 {

    String filepath;

    public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IOException {
        JFrame.setDefaultLookAndFeelDecorated(true);
        JDialog.setDefaultLookAndFeelDecorated(true);
        JFrame frame = new JFrame("Test");
        frame.getContentPane().setLayout(new FlowLayout());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JButton button = new JButton("Encrypt");
        button.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent ae) {
            JFileChooser fileChooser = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter("TEXT FILES","txt", "text");
            fileChooser.setFileFilter(filter);
            int returnValue = fileChooser.showOpenDialog(null);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                  System.out.println(selectedFile.getAbsolutePath());
                  String filepath = selectedFile.getAbsolutePath(); 

            }
        frame.getContentPane().add(button);
        frame.pack();
        frame.setVisible(true);

      public void encrypt() throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IOException {
        KeyGenerator keygenerator = KeyGenerator.getInstance("AES");
        SecretKey myDesKey = keygenerator.generateKey();
        Cipher desalgCipher;
        desalgCipher = Cipher.getInstance("AES");
        desalgCipher.init(Cipher.ENCRYPT_MODE, myDesKey);

        Path path = Paths.get(filepath); 
        try(InputStream is = Files.newInputStream(path);       
        CipherInputStream cipherIS = new CipherInputStream(is, desalgCipher);  
        BufferedReader reader = new BufferedReader(new InputStreamReader(cipherIS));){  
            String line;
            while((line = reader.readLine()) != null){
                System.out.println(line);            
            }
        }
       }         
}
