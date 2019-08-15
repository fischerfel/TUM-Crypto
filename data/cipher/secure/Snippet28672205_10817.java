    public class MinimalClient
    {
        public static void main(String[] args)
        {
            try
            {
                java.net.Socket             client_socket = null;
                java.io.InputStream         input_from_server = null;
                java.io.OutputStream        output_to_server = null;
                java.security.PrivateKey    client_private_key = null;
                java.security.PublicKey     server_public_key, trusted_server = null;
                java.security.PublicKey     client_public_key = null;
                java.security.Key           symmetric_key = null;
                String                      data = "super secret data";

                // Load trusted server pubkey into memory
                java.nio.file.Path path = java.nio.file.Paths.get("publickey");
                byte[] trusted = java.nio.file.Files.readAllBytes(path);
                java.security.spec.X509EncodedKeySpec pubkey_spec = new java.security.spec.X509EncodedKeySpec(trusted);
                java.security.KeyFactory key_factory = java.security.KeyFactory.getInstance("RSA");
                trusted_server = key_factory.generatePublic(pubkey_spec);

                // Generate new RSA keypair
                java.security.KeyPairGenerator keygen = java.security.KeyPairGenerator.getInstance("RSA");
                java.security.SecureRandom random = java.security.SecureRandom.getInstance("SHA1PRNG", "SUN");
                keygen.initialize(512, random);
                java.security.KeyPair pair = keygen.generateKeyPair();
                client_private_key = pair.getPrivate();
                client_public_key = pair.getPublic();

                // Then generate the symmetric key
                javax.crypto.KeyGenerator kg = javax.crypto.KeyGenerator.getInstance("AES");
                random = new java.security.SecureRandom();
                kg.init(random);
                symmetric_key = kg.generateKey();

                // Connect to host
                client_socket = new java.net.Socket("localhost", 7777);
                client_socket.setSoTimeout(5000);
                output_to_server = client_socket.getOutputStream();
                input_from_server = client_socket.getInputStream();

                // Send client's public key to the server
                output_to_server.write(client_public_key.getEncoded());

                // Wait for the server to send its public key, and load it into memory
                byte[] bytes = new byte[1024];
                int number = input_from_server.read(bytes);
                bytes = java.util.Arrays.copyOf(bytes, number); 
                pubkey_spec = new java.security.spec.X509EncodedKeySpec(bytes);
                key_factory = java.security.KeyFactory.getInstance("RSA");
                server_public_key = key_factory.generatePublic(pubkey_spec);

                // Check if trusted
                if (!java.util.Arrays.equals(server_public_key.getEncoded(), trusted_server.getEncoded()))
                    return;

                // Get server certificate (basically the client's signed public key)
                int length = input_from_server.read(bytes);

                // Verify the server's authenticity (signature)
                bytes = java.util.Arrays.copyOf(bytes, length);
                java.security.Signature sig = java.security.Signature.getInstance("SHA1withRSA");
                sig.initVerify(server_public_key);
                sig.update(client_public_key.getEncoded());
                if (!sig.verify(bytes))
                    return;

                // Send the symmetric key encrypted via RSA
                javax.crypto.Cipher cipher = javax.crypto.Cipher.getInstance("RSA/ECB/PKCS1PADDING");
                cipher.init(javax.crypto.Cipher.ENCRYPT_MODE, server_public_key);
                output_to_server.write(cipher.doFinal(symmetric_key.getEncoded()));


                // Send the data that must remain a secret
                cipher = javax.crypto.Cipher.getInstance("AES/CBC/PKCS5Padding");
                byte[] iv = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
                javax.crypto.spec.IvParameterSpec ivspec = new javax.crypto.spec.IvParameterSpec(iv);
                cipher.init(javax.crypto.Cipher.ENCRYPT_MODE, symmetric_key, ivspec);
                byte[] raw = cipher.doFinal(data.getBytes());
                output_to_server.write(raw);

                // Get a response
                bytes = new byte[1024];
                length = input_from_server.read(bytes);
                bytes = java.util.Arrays.copyOf(bytes, length);
                cipher = javax.crypto.Cipher.getInstance("AES/CBC/PKCS5Padding");
                ivspec = new javax.crypto.spec.IvParameterSpec(iv);
                cipher.init(javax.crypto.Cipher.DECRYPT_MODE, symmetric_key, ivspec);
                raw = cipher.doFinal(bytes);
                System.out.println("Response from server: '" + new String(raw) + "'");
            }
            catch (Exception exc)
            {
                exc.printStackTrace();
            }
        }
    }
