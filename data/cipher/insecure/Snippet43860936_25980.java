public class CipherFactory extends BasePooledObjectFactory<Cipher> {

    private boolean running = false;

    @Override
    public Cipher create() throws Exception {
        return Cipher.getInstance("DESede/CBC/NoPadding");
    }

    @Override
    public PooledObject<Cipher> wrap(Cipher arg0) {
        return new DefaultPooledObject<Cipher>(arg0);
    }

    @Override
    public boolean validateObject(PooledObject<Cipher> p) {
        //Ensures that the instance is safe to be returned by the pool
        return true;
    }

    @Override
    public void destroyObject(PooledObject<Cipher> p) {
        //Destroys an instance no longer needed by the pool. 
        System.out.println("destroying");
    }

    @Override
    public void activateObject(PooledObject<Cipher> p) throws Exception { //Reinitialize an instance to be returned by the pool

        setRunning(true);
    }

    @Override
    public void passivateObject(PooledObject<Cipher> p) throws Exception {   // reset the object after the object returns to the pool

        setRunning(false);
    }

    public void setRunning(boolean running) {

        this.running = running;
    }
//    
}
