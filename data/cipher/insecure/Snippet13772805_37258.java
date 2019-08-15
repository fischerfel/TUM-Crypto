package com.jeffterrace.appenginetest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.channels.Channels;
import java.security.InvalidKeyException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.files.AppEngineFile;
import com.google.appengine.api.files.FileReadChannel;
import com.google.appengine.api.files.FileService;
import com.google.appengine.api.files.FileServiceFactory;
import com.google.appengine.api.files.FileWriteChannel;
import com.google.appengine.api.files.GSFileOptions.GSFileOptionsBuilder;

/**
 * Sample AppEngine Java class that reads and writes an encrypted, zipped file.
 */
public class CloudStorageZipCrypto extends HttpServlet {

  // Google Cloud Storage bucket name.
  private final String bucket = "jefftest1";
  // File name within the bucket.
  private final String filename = "test1.zip";
  // Text file within the zip file.
  private final String textFilename = "test1.txt.crypt";
  // The full Cloud Storage path for accessing the file from App Engine.
  private final String gsPath = "/gs/" + bucket + "/" + filename;

  // App Engine file service instance.
  private FileService fileService = FileServiceFactory.getFileService();

  // Secret key for DES encryption (only first 8 bytes are used).
  private final String secretKey = "thereisnospoon";

  @Override
  public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    resp.setContentType("text/plain");
    resp.getWriter().println("CloudStorageZipCrypto GET starting.");

    resp.getWriter().println("Creating file...");

    // Create the GCS file and open it for writing.
    GSFileOptionsBuilder optionsBuilder = new GSFileOptionsBuilder()
      .setBucket(this.bucket)
      .setKey(this.filename)
      .setAcl("public-read");
    AppEngineFile gcsFile = this.fileService.createNewGSFile(optionsBuilder.build());

    resp.getWriter().println("File created.");

    resp.getWriter().println("Writing to file: " + gcsFile.getFullPath());

    // Open a write channel to the GCS object.
    FileWriteChannel writeChannel = this.fileService.openWriteChannel(gcsFile, true);

    // Initialize what we need for encryption.
    DESKeySpec dks;
    SecretKeyFactory skf;
    SecretKey desKey;
    Cipher cipher;
    try {
      dks = new DESKeySpec(this.secretKey.getBytes());
      skf = SecretKeyFactory.getInstance("DES");
      desKey = skf.generateSecret(dks);
      cipher = Cipher.getInstance("DES");
      cipher.init(Cipher.ENCRYPT_MODE, desKey);
    } catch (Exception e) {
      resp.getWriter().println("Error initializing crypto: " + e);
      return;
    }

    // Create a zip file output stream and add a text file to it.
    ZipOutputStream zos = new ZipOutputStream(Channels.newOutputStream(writeChannel));
    zos.putNextEntry(new ZipEntry(textFilename));

    // Write some text to the file, through a cipher output stream so it gets encrypted.
    CipherOutputStream cos = new CipherOutputStream(zos, cipher);
    PrintWriter out = new PrintWriter(cos);
    out.println("The woods are lovely and deep.");
    out.println("But I have promises too keep.");

    // Close all the things
    out.close();
    cos.close();
    zos.close();
    writeChannel.closeFinally();

    resp.getWriter().println("Wrote to, closed, and finalized the file.");

    // Open the zip file back up from GCS
    gcsFile = new AppEngineFile(this.gsPath);
    FileReadChannel readChannel = fileService.openReadChannel(gcsFile, false);
    ZipInputStream zis = new ZipInputStream(Channels.newInputStream(readChannel));

    // Read in the first file as UTF-8.
    zis.getNextEntry();
    try {
      cipher.init(Cipher.DECRYPT_MODE, desKey);
    } catch (InvalidKeyException e) {
      resp.getWriter().println("Error initializing crypto: " + e);
      return;
    }
    CipherInputStream cis = new CipherInputStream(zis, cipher);
    BufferedReader reader = new BufferedReader(new InputStreamReader(cis, "UTF-8"));
    resp.getWriter().println("Reading file contents...");
    String line;
    while ((line = reader.readLine()) != null) {
      resp.getWriter().println("READ: " + line);
    }

    // Close all the things.
    reader.close();
    cis.close();
    zis.close();
    readChannel.close();
  }
}
