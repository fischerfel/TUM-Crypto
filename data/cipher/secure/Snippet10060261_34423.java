Security.insertProviderAt(new FooBarProvider(), 1);
Cipher cip = Cipher.getInstance("AES/CBC/PKCS5Padding");
System.out.println(cip.getProvider()); //prints "SunJCE version 1.7"
