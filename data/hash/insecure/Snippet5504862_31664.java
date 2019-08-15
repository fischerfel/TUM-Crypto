package org.sqlite;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MainDriver {


public static void main(String[] args) {
    new MainDriver();
}


public MainDriver() {

    //Buffers to read and write
    byte[] writeBuffer = new byte[10];
    byte[] readBuffer = null;
    for (int i = 1; i < 10; i++) {
        writeBuffer[i] = (byte)i;
    }

    //Database objects
    Connection conn = null;
    Statement stat = null;
    PreparedStatement prep = null;

    //Load the database driver
    try {
        System.loadLibrary("sqlite");
        Class.forName("org.sqlite.JDBC");
    } catch (Exception e) {
        System.err.println("Could not load sqlite library or instantiate the database driver.");
        System.err.println(e);
        e.printStackTrace();
        return;
    }

    //Open a connection to the database
    try {
        conn = DriverManager.getConnection("jdbc:sqlite:" + "file.db");
    } catch (SQLException e) {
        System.err.println("Could not open a connection to the database with name file.db");
        System.err.println(e);
        e.printStackTrace();
        return;
    }

    //Create a table
    try {
        stat = conn.createStatement();
        stat.execute("CREATE TABLE TEST (model BLOB NOT NULL)");
        stat.close();
    } catch (SQLException e) {
        System.err.println("The table could not be created.");
        System.err.println(e);
        e.printStackTrace();
        return;
    }

    //Write buffer into the database
    try {
        conn.setAutoCommit(false);
        prep = conn.prepareStatement("INSERT INTO TEST (model) VALUES(?)");
        prep.setBytes(1, writeBuffer);
        prep.addBatch();
        prep.executeBatch();
        conn.setAutoCommit(true);
        prep.close();
    } catch (SQLException e) {
        System.err.println("The buffer could not be written to the database.");
        System.err.println(e);
        e.printStackTrace();
        return;
    }

    //Read buffer from the database
    try {
        stat = conn.createStatement();
        ResultSet rs = stat.executeQuery("SELECT * FROM TEST");
        readBuffer = rs.getBytes(1);
        rs.close();
        stat.close();
    } catch (SQLException e) {
        System.err.println("The buffer could not be read");
        System.err.println(e);
        e.printStackTrace();
    }

    //Close the database
    try {
        conn.close();
    } catch (SQLException e) {
        System.err.println("Database could not be closed");
        System.err.println(e);
        e.printStackTrace();
    }

    //Print the buffers
    System.out.print("Write buffer = ");
    for (int i = 0; i < writeBuffer.length; i++) {
        System.out.print(writeBuffer[i]);
    }
    System.out.println();
    System.out.print("Read  buffer = ");
    for (int i = 0; i < readBuffer.length; i++) {
        System.out.print(readBuffer[i]);
    }
    System.out.println();

    //Check the md5sum
    try {
        java.security.MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
        byte[] md5sum = null;
        java.math.BigInteger bigInt = null;

        //Write buffer
        digest.reset();
        digest.update(writeBuffer);
        md5sum = digest.digest();
        bigInt = new java.math.BigInteger(1, md5sum);
        System.out.println("MD5 checksum of write buffer = " + bigInt.toString(16));

        //Read buffer
        digest.reset();
        digest.update(readBuffer);
        md5sum = digest.digest();
        bigInt = new java.math.BigInteger(1, md5sum);
        System.out.println("MD5 checksum of read  buffer = " + bigInt.toString(16));
    } catch (Exception e) {
        System.err.println("MD5 checksum not available");
        return;
    }
}
