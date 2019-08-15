AlgorithmParameterGenerator paramGen = AlgorithmParameterGenerator.getInstance("DH");
paramGen.init(512); // number of bits
AlgorithmParameters params = paramGen.generateParameters();
DHParameterSpec dhSpec = (DHParameterSpec)params.getParameterSpec(DHParameterSpec.class); 

BigInteger p512 = dhSpec.getP();
BigInteger g512 = dhSpec.getG();

//A  
KeyPairGenerator akpg = KeyPairGenerator.getInstance("DiffieHellman");

DHParameterSpec param = new DHParameterSpec(p512, g512);
System.out.println("Prime: " + p512);
System.out.println("Base: " + g512);
akpg.initialize(param);
KeyPair kp = akpg.generateKeyPair();

//B
KeyPairGenerator bkpg = KeyPairGenerator.getInstance("DiffieHellman");

DHParameterSpec param2 = new DHParameterSpec(p512, g512);
System.out.println("Prime: " + p512);
System.out.println("Base: " + g512);
bkpg.initialize(param2);
KeyPair kp2 = bkpg.generateKeyPair();


KeyAgreement aKeyAgree = KeyAgreement.getInstance("DiffieHellman");
KeyAgreement bKeyAgree = KeyAgreement.getInstance("DiffieHellman");

aKeyAgree.init(kp.getPrivate());
bKeyAgree.init(kp2.getPrivate());

aKeyAgree.doPhase(kp2.getPublic(), true);
bKeyAgree.doPhase(kp.getPublic(), true);

//System.out.println("Alice Secret Key: " + aKeyAgree.generateSecret());
//System.out.println("Bob's Secret Key: " + bKeyAgree.generateSecret());

MessageDigest hash = MessageDigest.getInstance("SHA-256");

byte[] ASharedSecret = hash.digest(aKeyAgree.generateSecret());
byte[] BSharedSecret = hash.digest(bKeyAgree.generateSecret());

System.out.println("Alice's Shared Secret: " + ASharedSecret.toString());
System.out.println("Bob's Shared Secret: " + BSharedSecret.toString());
