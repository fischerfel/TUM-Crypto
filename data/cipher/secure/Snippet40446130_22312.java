package Encriptacion;
import javax.crypto.*;
import java.io.*;
import java.nio.*;
import java.nio.channels.*;
import javax.crypto.spec.*;
public class EncriptadorAES {
    private SecretKey CLAVESECRETA=null;
    private final int AES_KEYLENGTH = 128;
    private IvParameterSpec IV=null;
    public EncriptadorAES() throws Exception{
        //generarIV();
        if(new File("initvectoraes.iv").exists()){
            this.IV=new IvParameterSpec(obtenerIV());
        }
    }
    public void setCLAVESECRETA(String clave){
        this.CLAVESECRETA=generarClaveSecreta(clave);
    }
    public void guardarClave(String clave,String ruta)throws Exception{
        try{
            byte[]bytesClave=generarClaveSecreta(clave).getEncoded();
            FileChannel canalSalida=new RandomAccessFile(new File(ruta), "rw").getChannel();
            ByteBuffer bufferSalida=ByteBuffer.wrap(bytesClave);
            canalSalida.write(bufferSalida);
            canalSalida.close();
        }catch(Exception ex){
            throw new Exception("No se pudo guardar la clave\n"+ex);
        }
    }
    public SecretKey cargarClave(String ruta)throws Exception{
        try{
            File archivo=new File(ruta);
            byte[]bytesClave=new byte[(int)archivo.length()];
            FileChannel canalEntrada=new RandomAccessFile(archivo, "r").getChannel();
            ByteBuffer bufferEntrada=ByteBuffer.allocate(bytesClave.length);
            canalEntrada.read(bufferEntrada);
            bufferEntrada.flip();
            bufferEntrada.get(bytesClave);
            canalEntrada.close();
            return new SecretKeySpec(bytesClave, "AES");
        }catch(Exception ex){
            throw new Exception("No se pudo cargar la clave secreta\n"+ex);
        }
    }
    public void encriptarArchivo(String ruta,SecretKey clave) throws Exception{
        File archivo=null;
        try {
            archivo=new File(ruta);
            if(archivo.isFile()&&archivo.exists()&&archivo.length()<=700248752){
                //LECTURA
                byte[] bytesArchivo=new byte[(int)archivo.length()];
                int tamañoBloque=AES_KEYLENGTH/8;
                int numBloques=((int)archivo.length()%tamañoBloque==0)?(int)archivo.length()/tamañoBloque:((int)archivo.length()/tamañoBloque)+1;
                int tamañoEncriptado=((bytesArchivo.length/tamañoBloque)+1)*tamañoBloque;
                FileChannel canalEntrada=new RandomAccessFile(archivo, "r").getChannel();
                ByteBuffer bufferEntrada=ByteBuffer.allocate((int)archivo.length());
                canalEntrada.read(bufferEntrada);
                bufferEntrada.flip();
                bufferEntrada.get(bytesArchivo);
                canalEntrada.close();
                //CIFRADO clave simétrica
                ByteBuffer bufferSalida=ByteBuffer.allocate(tamañoEncriptado);
                Cipher cifradorAES = Cipher.getInstance("AES/CBC/PKCS5Padding");
                cifradorAES.init(Cipher.ENCRYPT_MODE, clave,this.IV);
                bufferSalida.put(cifradorAES.doFinal(bytesArchivo));
                bufferSalida.flip();
                //ESCRITURA
                if(archivo.delete()){
                    FileChannel canalSalida=new RandomAccessFile(archivo,"rw").getChannel();
                    canalSalida.write(bufferSalida);
                    canalSalida.close();
                }else throw new Exception("No se pudo borrar el archivo "+archivo.getName()+", si lo tiene abierto, ciérrelo.");
            }else{
                if(!archivo.exists())throw new Exception("El archivo "+archivo.getName()+" no existe");
                if(!archivo.isFile())throw new Exception("No puede encriptar un directorio, trate\nde comprimirlo antes para luego encriptar los archivos");
                if(archivo.length()>700248752)throw new Exception("No se puede encriptar el archivo "+archivo.getName()+" porque ha superado el tamaño máximo\nde capacidad de memoria del JVM");
            }
        }
        catch (Exception ex){
            throw new Exception("Hubo un error al encriptar el archivo\n"+ archivo.getName() +"\n"+ex);
        }
    }
    public void desencriptarArchivo(String ruta,SecretKey clave)throws Exception{
        File archivoEncriptado=null;
        try{
            archivoEncriptado=new File(ruta);
            if(archivoEncriptado.exists()){
                //LECTURA
                byte[]bytesArchivoEncriptado=new byte[(int)archivoEncriptado.length()];
                int tamañoBloque=AES_KEYLENGTH/8;
                int numBloques=((int)archivoEncriptado.length()%tamañoBloque==0)?(int)archivoEncriptado.length()/tamañoBloque:((int)archivoEncriptado.length()/tamañoBloque)+1;
                FileChannel canalEntrada=new RandomAccessFile(archivoEncriptado, "r").getChannel();
                ByteBuffer bufferEntrada=ByteBuffer.allocate((int)archivoEncriptado.length());
                canalEntrada.read(bufferEntrada);
                bufferEntrada.flip();
                bufferEntrada.get(bytesArchivoEncriptado);
                canalEntrada.close();
                //DESCRIFRADO
                ByteBuffer bufferSalida=ByteBuffer.allocate((int)archivoEncriptado.length());
                if(comprobarKeys(clave)){
                    Cipher descifradorAES = Cipher.getInstance("AES/CBC/PKCS5Padding");
                    descifradorAES.init(Cipher.DECRYPT_MODE,clave,this.IV);
                    bufferSalida.put(descifradorAES.doFinal(bytesArchivoEncriptado));
                    bufferSalida.flip();
                }
                else{
                    System.gc();
                    throw new Exception("La clave ingresada es incorrecta");
                }
                //ESCRITURA            
                if(archivoEncriptado.delete()){
                    FileChannel canalSalida=new RandomAccessFile(ruta, "rw").getChannel();
                    canalSalida.write(bufferSalida);
                    canalSalida.close();
                }else throw new Exception("No se pudo eliminar el archivo "+archivoEncriptado.getName()+", si lo tiene abierto, ciérrelo.");
            }else{
                if(!archivoEncriptado.exists())throw new Exception("El archivo "+archivoEncriptado.getName()+" no existe");
            }
        }
        catch (Exception ex){
            System.gc();
            throw new Exception("Hubo un error al desencriptar\n"+archivoEncriptado.getName()+":\n"+ex.getMessage());
        }
    }    
    public SecretKey generarClaveSecreta(String clave){
        byte[]key=rellenarBytesClave(clave);
        SecretKey claveGenerada=new SecretKeySpec(key, "AES");
        return claveGenerada;
    }
    private byte[] rellenarBytesClave(String clave){
        byte[]key=clave.getBytes();
        while(key.length!=AES_KEYLENGTH/8){
            if(key.length<AES_KEYLENGTH/8){
                clave+="0";
                key=clave.getBytes();
            }
            if(key.length>AES_KEYLENGTH/8){
                clave=clave.substring(0,AES_KEYLENGTH/8);
                key=clave.getBytes();
            }
        }
        return key;
    }
    private boolean comprobarKeys(SecretKey clave){
        return this.CLAVESECRETA.equals(clave);
    }
    public void generarIV() throws Exception{
        try{
            byte[]VECTOR={1,6,1,2,1,9,9,7,7,9,9,1,2,1,6,1};
            FileChannel canalsalida=new RandomAccessFile(new File("initvectoraes.iv"), "rw").getChannel();
            MappedByteBuffer buffersalida=canalsalida.map(FileChannel.MapMode.READ_WRITE, 0, 16);
            buffersalida.put(VECTOR);
            buffersalida.force();
            canalsalida.close();
        }catch(Exception ex){
            throw new Exception("Error al generar el Vector de Inicialización de AES\n"+ex.getMessage());
        }
    }
    private byte[]obtenerIV()throws Exception{
        byte[]vectorcargado=null;
        try{
            FileChannel canalentrada=new RandomAccessFile(new File("initvectoraes.iv"), "r").getChannel();
            MappedByteBuffer bufferentrada=canalentrada.map(FileChannel.MapMode.READ_ONLY, 0, 16);
            vectorcargado=new byte[16];
            bufferentrada.get(vectorcargado);
            bufferentrada.load();
            canalentrada.close();
        }
        catch(Exception ex){
            throw new Exception("Error al obtener el Vector de Inicialización de AES\n"+ex.getMessage());
        }
        return vectorcargado;
    }
}
