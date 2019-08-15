JFileChooser chooser = new JFileChooser();
    chooser.setMultiSelectionEnabled(true);

    Component frame = null;

    chooser.showOpenDialog(frame);
    File files = chooser.getSelectedFile();

    chooser.showOpenDialog(frame);
    File file2 = chooser.getSelectedFile();


    boolean work = files.toString().equals(file2.toString());

    if (work) {
        System.out.print("Yes the String");
    } else {
        System.out.print("No The String " + files.toString() + " " + file2.toString());
    }


    MessageDigest md = MessageDigest.getInstance("MD5");

    FileInputStream fis = new FileInputStream(files.toString());
    FileInputStream fis2 = new FileInputStream(file2.toString());

    String md5 = org.apache.commons.codec.digest.DigestUtils.md5Hex(fis);
    String  md52 = org.apache.commons.codec.digest.DigestUtils.md5Hex(fis2);

    boolean itWork = md5.equals(md52);
    if (itWork) {
        System.out.print("Yes the Hash"+ md5 + " " + md52);
    } else {
        System.out.print("No the Hash " + md5 + " " + md52);
    }

}

No The String C:\Users\Chris Deisr\Desktop\y8hu.txt C:\Users\Chris Deisr\Desktop\jhj.txtYes the Hashd41d8cd98f00b204e9800998ecf8427e d41d8cd98f00b204e9800998ecf8427e
