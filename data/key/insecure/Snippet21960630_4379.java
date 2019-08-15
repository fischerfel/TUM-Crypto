private static byte[] asegurarCapacidad(byte[] inicial, int tamano){

    if(inicial.length<tamano){

        return Arrays.copyOf(inicial, tamano);          
    }

    return inicial;
}    

   private static String leer(Socket s){

    byte[] buffer=new byte[4092];
    byte[] bufferFinal = new byte[8092];
    int leido=0;
    int posicion=0;
    String salida = null;

    try {       
        SecretKey key = new SecretKeySpec("1212121212121212".getBytes("UTF-8"),"AES");
        DataInputStream dis=new DataInputStream(s.getInputStream());
        while((leido=dis.read(buffer))>0){
            bufferFinal=asegurarCapacidad(bufferFinal,posicion+leido);
            System.arraycopy(buffer, 0, bufferFinal, posicion, leido);  
            posicion+=leido;
        }
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, key,iv);
        salida=decodificar(cipher.doFinal(bufferFinal));

    } catch (Throwable e) {
        System.out.println("CLIENTE: ERROR AL LEER: "+e);
        e.printStackTrace();
    }

    return salida;
}
