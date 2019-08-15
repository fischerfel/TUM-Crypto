//This is the wrong initialization
Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding");

//This is the right initialization
Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding","SunJCE");
