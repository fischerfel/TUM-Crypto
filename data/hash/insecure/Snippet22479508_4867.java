package testproject;

import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.security.Security;
import javax.crypto.KeyAgreement;
import javax.crypto.spec.DHParameterSpec;

public class KeyGen {

  private static BigInteger g512 = new BigInteger("1234567890", 16);
  //generates a random, non-negative integer for Base

  private static BigInteger p512 = new BigInteger("1234567890", 16);
  //generates a random, non-negative integer for Prime

  public static void main(String[] args) throws Exception {
    Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
    DHParameterSpec dhParams = new DHParameterSpec(p512, g512);
    //Specify parameters to use for the algorithm
    KeyPairGenerator keyGen = KeyPairGenerator.getInstance("DH", "BC");
    //Define specific algorithm to use "diffie-hellman", with provider "bc"

    keyGen.initialize(dhParams, new SecureRandom());
    //initialize with parameters & secure random seed

    KeyAgreement aKeyAgree = KeyAgreement.getInstance("DH", "BC");
    //define algorithm for A's key agreement
    KeyPair aPair = keyGen.generateKeyPair();
    //generate keyPair for A

    KeyAgreement bKeyAgree = KeyAgreement.getInstance("DH", "BC");
    //define algorithm for B's key agreement
    KeyPair bPair = keyGen.generateKeyPair();
    //generate keyPair for B

    aKeyAgree.init(aPair.getPrivate());
    //initialize A's keyAgreement with A's private key
    bKeyAgree.init(bPair.getPrivate());
    //initialize B's keyAgreement with B's private key

    aKeyAgree.doPhase(bPair.getPublic(), true);
    //do last phase of A's keyAgreement with B's public key
    bKeyAgree.doPhase(aPair.getPublic(), true);
    //do last phase of B's keyAgreement with A's public key

    MessageDigest hash = MessageDigest.getInstance("SHA1", "BC");

    System.out.println(new String(hash.digest(aKeyAgree.generateSecret())));
    //generate secret key for A, hash it.
    System.out.println(new String(hash.digest(bKeyAgree.generateSecret())));
    //generate secret key for B, hash it.
  }
}
