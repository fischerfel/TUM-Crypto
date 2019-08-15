public boolean equals(Object o){
        if (this == o){
            return true;
        }

        if(!(o instanceof edge)){
            return false;
        }

        edge edge = (edge) o;
        return i==edge.i && j==edge.j && Objects.equals(edgeType, edge.edgeType);
    }

    @Override
    public int hashCode() {
        String uniqueIdentified = edgeType + i + j;
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        byte[] hash = digest.digest(uniqueIdentified.getBytes(StandardCharsets.UTF_8));
        ByteBuffer wrapped = ByteBuffer.wrap(hash); // big-endian by default
        short num = wrapped.getShort();
        return num;

    }
