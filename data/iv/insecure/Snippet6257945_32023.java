public class ResourceDecryptor {
    private static ThreadLocal<Cipher>      mCipher;
    private byte[]                          mIV = new byte[ 8 ];
    private SecretKeySpec                   mKey;
    private String                          mResourcePath;

    private static final int                kAESBlockSize = 16;

    public ResourceDecryptor( String resourcePath, String decryptionKey ) throws UnsupportedOperationException {
        // initialization of mKey, mIV, & mResourcePath, elided 

        // store mCipher as a thread local because Cipher.getInstance() is so slow,
        // ResourceDecryptor is a static object that persists for the app lifetime
        // so this leak is intentional and ok.
        mCipher = new ThreadLocal<Cipher>() {
            protected Cipher initialValue() {
                try { return Cipher.getInstance( "AES/CTR/NoPadding" ); } catch ( Exception e ) { }

                return null;
            }
        };
    }

    public ByteBuffer read( long offset, int length ) throws GeneralSecurityException, IOException {
        Cipher                      cipher;
        byte[]                      data, iv;
        FileInputStream             input;
        int                         prefix, readLength;

        input = null;
        prefix = (int)( offset % kAESBlockSize );
        readLength = ( prefix + length + kAESBlockSize - 1 ) / kAESBlockSize * kAESBlockSize;
        data = new byte[ readLength ];
        iv = new byte[ 16 ];

        try {
            input = new FileInputStream( mResourcePath );
            input.skip( offset -= prefix );

            if ( input.read( data ) != readLength ) throw new IOException( "I/O error: unable to read " + readLength + " bytes from offset " + offset );

            System.arraycopy( mIV, 0, iv, 0, 8 );

            offset /= kAESBlockSize;

            iv[  8 ] = (byte)( offset >> 56 & 0xff );
            iv[  9 ] = (byte)( offset >> 48 & 0xff );
            iv[ 10 ] = (byte)( offset >> 40 & 0xff );
            iv[ 11 ] = (byte)( offset >> 32 & 0xff );
            iv[ 12 ] = (byte)( offset >> 24 & 0xff );
            iv[ 13 ] = (byte)( offset >> 16 & 0xff );
            iv[ 14 ] = (byte)( offset >>  8 & 0xff );
            iv[ 15 ] = (byte)( offset       & 0xff );

            if ( ( cipher = mCipher.get() ) == null ) throw new GeneralSecurityException( "Unable to initialize Cipher( \"AES/CTR/NoPadding\" )" );
            cipher.init( Cipher.DECRYPT_MODE, mKey, new IvParameterSpec( iv ) );

            long startTime = System.currentTimeMillis();
            data = cipher.doFinal( data );
            System.out.println( "decryption of " + data.length + " bytes took " + ( ( System.currentTimeMillis() - startTime ) / 1000.0 ) + "s" );

            // cipher.doFinal() takes 5.9s on Samsung Galaxy emulator for 128kb block
            // cipher.doFinal() takes 2.6s on Samsung Galaxy hardware for 128kb block
        } finally {
            if ( input != null ) try { input.close(); } catch ( Exception e ) { }
        }

        // the default order of ByteBuffer is BIG_ENDIAN so it is unnecessary to explicitly set the order()

        return ByteBuffer.wrap( data, prefix, length );
    }
}
