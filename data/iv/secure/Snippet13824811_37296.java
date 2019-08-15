package gui;
    import java.awt.BorderLayout;
    import java.awt.event.ActionEvent;
    import java.awt.event.ActionListener;
    import java.awt.event.MouseAdapter;
    import java.awt.event.MouseEvent;
    import java.io.File;
    import java.io.FileInputStream;
    import java.io.FileOutputStream;
    import java.io.IOException;
    import java.io.InputStream;
    import java.io.OutputStream;
    import java.security.AlgorithmParameters;
    import java.security.spec.KeySpec;

    import javax.crypto.Cipher;
    import javax.crypto.CipherInputStream;
    import javax.crypto.CipherOutputStream;
    import javax.crypto.SecretKey;
    import javax.crypto.SecretKeyFactory;
    import javax.crypto.spec.IvParameterSpec;
    import javax.crypto.spec.PBEKeySpec;
    import javax.crypto.spec.SecretKeySpec;
    import javax.swing.ImageIcon;
    import javax.swing.JFileChooser;
    import javax.swing.JMenuItem;
    import javax.swing.JOptionPane;
    import javax.swing.JPanel;
    import javax.swing.JPopupMenu;
    import javax.swing.JScrollPane;
    import javax.swing.JTable;
    import javax.swing.table.DefaultTableModel;

    public class FileTable extends JPanel {

        /**
         * 
         */
        private static final long serialVersionUID = 1L;
        private JTable table;
        private DefaultTableModel tableModel = new DefaultTableModel(new String[] {"File", "Size", "Status" }, 0);
        private File dir;
        private File temp;
        private JPopupMenu popup;
        private String key;
        private PasswordStorage passwordStorage;
        private JFileChooser fileChooser;
        private static String salt = "loquetdeliciouslysalty";
        private static byte[] IV;

        public FileTable() {

            // Set Layout Manager
            setLayout(new BorderLayout());

            // Create Swing Components
            table = new JTable();
            table.setModel(tableModel);
            table.setDropTarget(new TableDnD(table));
            table.setShowGrid(false);
            table.setFillsViewportHeight(true);
            table.getColumnModel().getColumn(0).setPreferredWidth(250);

            passwordStorage = new PasswordStorage();
            fileChooser = new JFileChooser();
            popup = new JPopupMenu();

            JMenuItem removeItem = new JMenuItem("Remove");
            removeItem.setIcon(new ImageIcon("removeMenu.png"));
            popup.add(removeItem);

            // Add Components to pane
            add(new JScrollPane(table), BorderLayout.CENTER);

            table.addMouseListener(new MouseAdapter() {
                public void mousePressed(MouseEvent e) {
                    int row = table.rowAtPoint(e.getPoint());
                    table.getSelectionModel().setSelectionInterval(row, row);

                    if(e.getButton() == MouseEvent.BUTTON3) {
                        popup.show(table, e.getX(), e.getY());
                    }
                }
            });

            removeItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent arg0) {
                    int row = table.getSelectedRow();
                    if(row > -1) {
                        tableModel.removeRow(table.getSelectedRow());
                    }
                }
            });
        }

        public boolean isTableEmpty() {

            if(tableModel.getRowCount() == 0) {
                return true;
            }
            else {
                return false;
            }
        }

        public void addFile(File file) {
            tableModel.addRow(new Object[]{file, file.length() + " kb","Not Processed"});
        }

        public void removeFile() {
            int[] rows = table.getSelectedRows();

            for(int i = 0; i < rows.length; i++) {
                tableModel.removeRow(rows[i]-i);
            }
        }

        public void clearTable()
        {
            int rowCount = tableModel.getRowCount();

            for(int i = 0; i < rowCount; i++) {
                tableModel.removeRow(0);
            }

            table.removeAll();
        }

        public void encrypt() {

            if(!isTableEmpty()) {
                try {
                    do {
                        key = JOptionPane.showInputDialog(this, 
                                "Enter password", "Password", 
                                JOptionPane.OK_OPTION|JOptionPane.PLAIN_MESSAGE);   // key needs to be at least 8 characters for DES

                        if(key == null) break;
                    } while(key.length() < 8);

                    // If OK and length of password >= 8 encrypt files
                    if(key != null) {

                        // Store password
                        passwordStorage.write(key);

                        // Custom Folder for encrypted files
                        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

                        if(fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                            dir = fileChooser.getSelectedFile();
                        }

                        else {
                            // Default Folder for decrypted files
                            dir = new File("encrypted"); 
                            dir.mkdir();
                        }

                        for(int i = 0; i < tableModel.getRowCount(); i++) {
                            temp = (File) tableModel.getValueAt(i, 0);

                            FileInputStream fis2 = new FileInputStream(temp);
                            FileOutputStream fos2 = new FileOutputStream(dir + "\\" + (temp.getName()));

                            encrypt(key, fis2, fos2);
                        }

                        for(int i = 0; i < tableModel.getRowCount(); i++) {
                            File toDelete = (File) tableModel.getValueAt(i, 0);
                            toDelete.delete();
                        }

                        // Encryption complete message
                        JOptionPane.showMessageDialog(this, "Files encrypted succesfully!");

                        // CLEAR LIST
                        table.removeAll();
                        clearTable();
                    }
                } catch (Throwable te) { te.printStackTrace(); }
            }
        }

        public void decrypt() {

            if(!isTableEmpty()) {
                try {

                    key = JOptionPane.showInputDialog(this, 
                            "Enter password", "Password", 
                            JOptionPane.OK_OPTION|JOptionPane.PLAIN_MESSAGE);

                    while(!passwordStorage.isPassword(key)) {
                        key = JOptionPane.showInputDialog(this, 
                                "Enter password", "Wrong Password!", 
                                JOptionPane.OK_OPTION|JOptionPane.ERROR_MESSAGE);   
                    }

                    // If OK and length of password >= 8 decrypt files
                    if(key != null) {

                        // Custom Folder for decrypted files
                        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

                        if(fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                            dir = fileChooser.getSelectedFile();
                        }

                        else {
                            // Default Folder for decrypted files
                            dir = new File("decrypted"); 
                            dir.mkdir();
                        }

                        for(int i = 0; i < tableModel.getRowCount(); i++) {
                            temp = (File) tableModel.getValueAt(i, 0);

                            FileInputStream fis2 = new FileInputStream(temp);
                            FileOutputStream fos2 = new FileOutputStream(dir + "\\" + (temp.getName()));

                            decrypt(key, fis2, fos2);
                        }

                        for(int i = 0; i < tableModel.getRowCount(); i++) {
                            File toDelete = (File) tableModel.getValueAt(i, 0);
                            toDelete.delete();
                        }

                        // Encryption complete message
                        JOptionPane.showMessageDialog(this, "Files decrypted succesfully!");

                        // CLEAR LIST
                        table.removeAll();
                        clearTable();   
                    }
                } catch (Throwable te) { te.printStackTrace(); }
            }
        }


        /*************************************************************************************************************
         *      ENCRYPTION METHODS ***********************************************************************************
         *************************************************************************************************************/
        public static void encrypt(String key, InputStream is, OutputStream os) throws Throwable {
            encryptOrDecrypt(key, Cipher.ENCRYPT_MODE, is, os);
        }

        public static void decrypt(String key, InputStream is, OutputStream os) throws Throwable {
            encryptOrDecrypt(key, Cipher.DECRYPT_MODE, is, os);
        }

        public static void encryptOrDecrypt(String key, int mode, InputStream is, OutputStream os) throws Throwable {

            char[] kgen = key.toCharArray();
            byte[] ksalt = salt.getBytes();

            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            KeySpec spec = new PBEKeySpec(kgen, ksalt, 65536, 256);
            SecretKey tmp = factory.generateSecret(spec);
            SecretKey aesKey = new SecretKeySpec(tmp.getEncoded(), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");


            /*DESKeySpec dks = new DESKeySpec((salt + key).getBytes());
            SecretKeyFactory skf = SecretKeyFactory.getInstance("DES");
            SecretKey desKey = skf.generateSecret(dks);
            Cipher cipher = Cipher.getInstance("DES"); // DES/ECB/PKCS5Padding for SunJCE*/

            if (mode == Cipher.ENCRYPT_MODE) {
                cipher.init(Cipher.ENCRYPT_MODE, aesKey);
                CipherInputStream cis = new CipherInputStream(is, cipher);
                AlgorithmParameters params = cipher.getParameters();
                byte[] iv = params.getParameterSpec(IvParameterSpec.class).getIV();
                saveIv(iv);
                doCopy(cis, os);
            } else if (mode == Cipher.DECRYPT_MODE) {
                cipher.init(Cipher.DECRYPT_MODE, aesKey, new IvParameterSpec(getIv()));
                CipherOutputStream cos = new CipherOutputStream(os, cipher);
                doCopy(is, cos);
            }
        }

        public static void saveIv(byte[] iv) {
            IV = iv;
        }

        public static byte[] getIv() {
            return IV;
        }

        public static void doCopy(InputStream is, OutputStream os) throws IOException {

            byte[] bytes = new byte[1024];
            int numBytes;

            while ((numBytes = is.read(bytes)) > 0) {
                os.write(bytes, 0, numBytes);
            }

            os.flush();
            os.close();
            is.close();
        }

    }
