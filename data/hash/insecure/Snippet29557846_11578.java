                public byte[] computeSignature() throws IOException {
            MessageDigest digest;
            try {
            digest = MessageDigest.getInstance("SHA-1");
            } catch (NoSuchAlgorithmException e) {
            throw new AssertionError();
            }
            byte[] buffer = new byte[8192];
            ByteBuffer data = this.data.duplicate(); // positioned ByteBuffers aren't thread safe
            data.limit(data.capacity());
            data.position(SIGNATURE_OFFSET + SIGNATURE_SIZE);
            while (data.hasRemaining()) {
            int count = Math.min(buffer.length, data.remaining());
            data.get(buffer, 0, count);
            digest.update(buffer, 0, count);
            }
            return digest.digest();
            }
