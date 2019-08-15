    public class MinimalServer
    {
        public static void main(String[] args)
        {
            try
            {
                java.net.ServerSocket       server_socket;
                java.net.Socket             client_socket;
                java.io.InputStream         input_from_client;
                java.io.OutputStream        output_to_client;
                java.security.PrivateKey    server_private_key;
                java.security.PublicKey     server_public_key;
                java.security.PublicKey     client_public_key;
                java.security.Key           symmetric_key;

                // Generate a new public/private keypair
                java.security.KeyPairGenerator keygen = java.security.KeyPairGenerator.getInstance("RSA");
                java.security.SecureRandom random = java.security.SecureRandom.getInstance("SHA1PRNG", "SUN");
                keygen.initialize(512, random);
                java.security.KeyPair pair = keygen.generateKeyPair();
                java.io.FileOutputStream output = new java.io.FileOutputStream("publickey");
                output.write(pair.getPublic().getEncoded());
                output = new java.io.FileOutputStream("privatekey");
                output.write(pair.getPrivate().getEncoded());

                // Load the public and private key files into memory
                java.nio.file.Path path = java.nio.file.Paths.get("publickey");
                byte[] public_key_raw = java.nio.file.Files.readAllBytes(path);
                java.security.spec.X509EncodedKeySpec pubkey_spec = new java.security.spec.X509EncodedKeySpec(public_key_raw);
                java.security.KeyFactory key_factory = java.security.KeyFactory.getInstance("RSA");
                server_public_key = key_factory.generatePublic(pubkey_spec);

                path = java.nio.file.Paths.get("privatekey");
                byte[] private_key_raw = java.nio.file.Files.readAllBytes(path);
                java.security.spec.PKCS8EncodedKeySpec privkey_spec = new java.security.spec.PKCS8EncodedKeySpec(private_key_raw);
                key_factory = java.security.KeyFactory.getInstance("RSA");
                server_private_key = key_factory.generatePrivate(privkey_spec);

                // Wait for clients to connect
                server_socket = new java.net.ServerSocket(7777);
                client_socket = server_socket.accept();
                client_socket.setSoTimeout(5000);
                input_from_client = client_socket.getInputStream();
                output_to_client = client_socket.getOutputStream();

                // Send server's public key to client
                output_to_client.write(server_public_key.getEncoded());

                // Get the public key from the client
                byte[] bytes = new byte[512];
                int number = input_from_client.read(bytes);
                bytes = java.util.Arrays.copyOf(bytes, number);
                pubkey_spec = new java.security.spec.X509EncodedKeySpec(bytes);
                key_factory = java.security.KeyFactory.getInstance("RSA");
                client_public_key = key_factory.generatePublic(pubkey_spec);

                // Sign the client's public key
                java.security.Signature signature = java.security.Signature.getInstance("SHA1withRSA");
                signature.initSign(server_private_key);
                signature.update(client_public_key.getEncoded());
                byte[] signed_signature = signature.sign();

                // Send the certificate to the client
                output_to_client.write(signed_signature);

                // Wait for the symmetric key from the client
                bytes = new byte[512];
                int code = input_from_client.read(bytes);
                bytes = java.util.Arrays.copyOf(bytes, code);
                javax.crypto.Cipher cipher = javax.crypto.Cipher.getInstance("RSA/ECB/PKCS1PADDING");
                cipher.init(javax.crypto.Cipher.DECRYPT_MODE, server_private_key);
                bytes = cipher.doFinal(bytes);
                symmetric_key = new javax.crypto.spec.SecretKeySpec(bytes, "AES");

                // Read super secret incoming data
                bytes = new byte[512];
                code = input_from_client.read(bytes);
                bytes = java.util.Arrays.copyOf(bytes, code);
                cipher = javax.crypto.Cipher.getInstance("AES/CBC/PKCS5Padding");
                byte[] iv = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
                javax.crypto.spec.IvParameterSpec ivspec = new javax.crypto.spec.IvParameterSpec(iv);
                cipher.init(javax.crypto.Cipher.DECRYPT_MODE, symmetric_key, ivspec);
                byte[] raw = cipher.doFinal(bytes);
                System.out.println(new String(raw));

                // Send a confirmation to the client
                cipher = javax.crypto.Cipher.getInstance("AES/CBC/PKCS5Padding");
                ivspec = new javax.crypto.spec.IvParameterSpec(iv);
                cipher.init(javax.crypto.Cipher.ENCRYPT_MODE, symmetric_key, ivspec);
                bytes = cipher.doFinal("OK".getBytes());
                output_to_client.write(bytes);
                server_socket.close();
            }
            catch (Exception exc)
            {
                exc.printStackTrace();
            }
        }
    }
