public class Test extends JPanel implements ActionListener,
        PropertyChangeListener {

    private JProgressBar progressBar;
    private JButton startButton;
    private JTextArea encryptedOutput;
    private JTextArea decryptedOutput;
    private Task task;

    Key aesKey;
    Cipher cipher;
    String key = "Code1234Code1234"; // 128 bit key

    class Task extends SwingWorker<Void, Void> {
        @Override
        public Void doInBackground() {
            int progress = 0;
            // Initialize progress property.
            setProgress(0);

            try {

                JFileChooser fileopen = new JFileChooser();
                FileFilter filter = new FileNameExtensionFilter("txt files",
                        "txt");
                fileopen.addChoosableFileFilter(filter);

                File file = null;
                String fileName = "";
                int ret = fileopen.showDialog(null, "Open file");

                if (ret == JFileChooser.APPROVE_OPTION) {
                    file = fileopen.getSelectedFile();
                    fileName = file.getName();

                    encryptedOutput.setText(String.format(
                            "Encrypted Output :  %s\t\n\n", fileName));
                    decryptedOutput.setText(String.format(
                            "Decrypted Output  : %s\t\n\n", fileName));
                    BufferedReader in = new BufferedReader(new FileReader(file));
                    String line = in.readLine();
                    long readLength = 0;
                    long totalLength = file.length();
                    double lengthPerPercent = 100.0 / totalLength;
                    // added dummy delay because encryption takes very less time
                    // in MVCE
                    Thread.sleep(1000);
                    while (line != null) {
                        try {

                            Thread.sleep(100);
                            // encrypt the text
                            cipher.init(Cipher.ENCRYPT_MODE, aesKey);
                            byte[] encrypted = cipher.doFinal(line.getBytes());

                            encryptedOutput.append(String.format("%s\n",
                                    new String(encrypted)));

                            // decrypt the text
                            cipher.init(Cipher.DECRYPT_MODE, aesKey);
                            String decrypted = new String(
                                    cipher.doFinal(encrypted));
                            decryptedOutput.append(String.format("%s\n",
                                    decrypted));

                            // calculate progress.
                            readLength += line.length();
                            progress = (int) (lengthPerPercent * readLength);
                            setProgress(Math.min(progress, 100));
                            line = in.readLine();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        /*
         * Executed in event dispatching thread
         */
        @Override
        public void done() {
            setProgress(100);
            Toolkit.getDefaultToolkit().beep();
            startButton.setEnabled(true);

            // turn off the wait cursor
            setCursor(null);
        }
    }

    public Test() {

        super(new BorderLayout());

        // Create key and cipher
        aesKey = new SecretKeySpec(key.getBytes(), "AES");
        try {
            cipher = Cipher.getInstance("AES");
        } catch (Exception e) {
            e.printStackTrace();
        }

        progressBar = new JProgressBar(0, 100);
        progressBar.setValue(0);
        progressBar.setStringPainted(true);

        encryptedOutput = new JTextArea(20, 50);
        encryptedOutput.setMargin(new Insets(5, 5, 5, 5));
        encryptedOutput.setEditable(false);
        encryptedOutput.setLineWrap(true);

        decryptedOutput = new JTextArea(20, 50);
        decryptedOutput.setMargin(new Insets(5, 5, 5, 5));
        decryptedOutput.setEditable(false);
        decryptedOutput.setLineWrap(true);

        startButton = new JButton("Start Encoding");
        startButton.setActionCommand("start");
        startButton.addActionListener(this);

        JPanel panel = new JPanel();
        add(panel, BorderLayout.PAGE_START);
        panel.add(progressBar, BorderLayout.LINE_START);
        add(new JScrollPane(encryptedOutput), BorderLayout.LINE_START);
        add(new JScrollPane(decryptedOutput), BorderLayout.LINE_END);
        panel.add(startButton, BorderLayout.PAGE_END);
        setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

    }

    public void actionPerformed(ActionEvent evt) {
        startButton.setEnabled(false);
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        task = new Task();
        task.addPropertyChangeListener(this);
        task.execute();
    }

    /**
     * Invoked when task's progress property changes.
     */
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("progress")) {
            int progress = (Integer) evt.getNewValue();
            progressBar.setValue(progress);
            encryptedOutput.append("\n\n");
        }
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JComponent newContentPane = new Test();
        newContentPane.setOpaque(true);
        frame.setContentPane(newContentPane);
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
}
