import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Reader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class Crypto {

    public static void main(String[] args) {
        new Crypto();
    }

    public Crypto() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
                }

                JFrame frame = new JFrame("Testing");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setLayout(new GridBagLayout());
                GridBagConstraints gbc = new GridBagConstraints();
                gbc.gridx = 0;
                gbc.gridy = 0;
                gbc.weighty = 1;
                gbc.weightx = 1;
                gbc.fill = GridBagConstraints.BOTH;

                final FilePane sourcePane = new FilePane(true);
                final FilePane encryptPane = new FilePane(false);

                frame.add(sourcePane, gbc);
                gbc.gridx = 2;
                frame.add(encryptPane, gbc);

                JButton encrypt = new JButton("Encrypt >>");
                JPanel panel = new JPanel(new GridBagLayout());
                panel.add(encrypt);

                encrypt.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        File source = sourcePane.getFile();
                        try (BufferedReader br = new BufferedReader(new FileReader(source))) {
                            char[] buffer = new char[1024];
                            StringBuilder sb = new StringBuilder(1024);
                            int bytesRead = -1;
                            while ((bytesRead = br.read(buffer)) != -1) {
                                sb.append(buffer, 0, bytesRead);
                            }
                            String encrypted = encryptionMD5(sb.toString());

                            File enrypt = new File(source.getPath() + ".enrypted");
                            try (BufferedWriter bw = new BufferedWriter(new FileWriter(enrypt))) {
                                bw.write(encrypted);
                            } catch (Exception exp) {
                                exp.printStackTrace();
                            }

                            encryptPane.setFile(enrypt);

                        } catch (Exception exp) {
                            exp.printStackTrace();
                        }
                    }
                });

                gbc.gridx = 1;
                gbc.weighty = 1;
                gbc.weightx = 0;
                gbc.fill = GridBagConstraints.VERTICAL;
                frame.add(panel, gbc);

                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });
    }

    public static String encryptionMD5(String token) {
        char[] hex = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            MessageDigest msgd = MessageDigest.getInstance("MD5");
            byte[] bytes = msgd.digest(token.getBytes());
            StringBuilder strbMD5 = new StringBuilder(2 * bytes.length);
            for (int i = 0; i < bytes.length; i++) {
                int low = (int) (bytes[i] & 0x0f);
                int high = (int) ((bytes[i] & 0xf0) >> 4);
                strbMD5.append(hex[high]);
                strbMD5.append(hex[low]);
            }
            return strbMD5.toString();
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }

    public class FilePane extends JPanel {

        private JTextField field;
        private JButton browse;
        private JTextArea content;

        private File file;

        public FilePane(boolean canOpen) {
            setLayout(new BorderLayout());

            field = new JTextField();
            field.setEditable(false);

            content = new JTextArea(20, 20);
//            content.setLineWrap(true);
//            content.setWrapStyleWord(true);
            content.setEditable(false);

            add(new JScrollPane(content));

            JPanel header = new JPanel(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.weightx = 1;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            header.add(field, gbc);
            gbc.gridx = 1;
            gbc.weightx = 0;
            gbc.fill = GridBagConstraints.NONE;
            if (canOpen) {
                browse = new JButton("...");
                browse.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        JFileChooser chooser = new JFileChooser();
                        chooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
                        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                        switch (chooser.showOpenDialog(FilePane.this)) {
                            case JFileChooser.APPROVE_OPTION:
                                setFile(chooser.getSelectedFile());
                                break;
                        }
                    }
                });
                header.add(browse, gbc);
            }

            add(header, BorderLayout.NORTH);

        }

        public File getFile() {
            return file;
        }

        public void setFile(File f) {
            file = f;
            field.setText(file.getPath());
            if (file != null) {
                try (Reader r = new FileReader(file)) {
                    content.read(r, file);
                } catch (Exception exp) {
                }
            } else {
                content.setText(null);
            }
            content.setCaretPosition(0);
        }

    }

}
