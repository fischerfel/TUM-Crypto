@SuppressWarnings("unchecked")
public TorrentInfo(byte[] torrent_file_bytes) throws BencodingException
{   
    // Make sure the input is valid
    if(torrent_file_bytes == null || torrent_file_bytes.length == 0)
        throw new IllegalArgumentException("Torrent file bytes must be non-null and have at least 1 byte.");

    // Assign the byte array
    this.torrent_file_bytes = torrent_file_bytes;

    // Assign the metainfo map
    this.torrent_file_map = (Map<ByteBuffer,Object>)Bencoder2.decode(torrent_file_bytes);

    // Try to extract the announce URL
    ByteBuffer url_buff = (ByteBuffer)this.torrent_file_map.get(TorrentInfo.KEY_ANNOUNCE);
    if(url_buff == null)
        throw new BencodingException("Could not retrieve anounce URL from torrent metainfo.  Corrupt file?");

    try {
        String url_string = new String(url_buff.array(), "ASCII");
        URL announce_url = new URL(url_string);
        this.announce_url = announce_url;
    }
    catch(UnsupportedEncodingException uee)
    {
        throw new BencodingException(uee.getLocalizedMessage());
    }
    catch(MalformedURLException murle)
    {
        throw new BencodingException(murle.getLocalizedMessage());
    }

    // Try to extract the info dictionary
    ByteBuffer info_bytes = Bencoder2.getInfoBytes(torrent_file_bytes);
    Map<ByteBuffer,Object> info_map = (Map<ByteBuffer,Object>)this.torrent_file_map.get(TorrentInfo.KEY_INFO);

    if(info_map == null)
        throw new BencodingException("Could not extract info dictionary from torrent metainfo dictionary.  Corrupt file?");
    this.info_map = info_map;

    // Try to generate the info hash value
    try {
        MessageDigest digest = MessageDigest.getInstance("SHA-1");
        digest.update(info_bytes.array());
        byte[] info_hash = digest.digest();
        this.info_hash = ByteBuffer.wrap(info_hash);
    }
    catch(NoSuchAlgorithmException nsae)
    {
        throw new BencodingException(nsae.getLocalizedMessage());
    }

    // Extract the piece length from the info dictionary
    Integer piece_length = (Integer)this.info_map.get(TorrentInfo.KEY_PIECE_LENGTH);
    if(piece_length == null)
        throw new BencodingException("Could not extract piece length from info dictionary.  Corrupt file?");
    this.piece_length = piece_length.intValue();

    // Extract the file name from the info dictionary
    ByteBuffer name_bytes = (ByteBuffer)this.info_map.get(TorrentInfo.KEY_NAME);
    if(name_bytes == null)
        throw new BencodingException("Could not retrieve file name from info dictionary.  Corrupt file?");
    try {
        this.file_name = new String(name_bytes.array(),"ASCII");
    }
    catch(UnsupportedEncodingException uee)
    {
        throw new BencodingException(uee.getLocalizedMessage());
    }

    // Extract the file length from the info dictionary
    Integer file_length = (Integer)this.info_map.get(TorrentInfo.KEY_LENGTH);
    if(file_length == null)
        throw new BencodingException("Could not extract file length from info dictionary.  Corrupt file?");
    this.file_length = file_length.intValue();

    // Extract the piece hashes from the info dictionary
    ByteBuffer all_hashes = (ByteBuffer)this.info_map.get(TorrentInfo.KEY_PIECES);
    if(all_hashes == null)
        throw new BencodingException("Could not extract piece hashes from info dictionary.  Corrupt file?");
    byte[] all_hashes_array = all_hashes.array();

    // Verify that the length of the array is a multiple of 20 bytes (160 bits)
    if(all_hashes_array.length % 20 != 0)
        throw new BencodingException("Piece hashes length is not a multiple of 20.  Corrupt file?");
    int num_pieces = all_hashes_array.length / 20;

    // Copy the values of the piece hashes into the local field
    this.piece_hashes = new ByteBuffer[num_pieces];
    for(int i = 0; i < num_pieces; i++)
    {
        byte[] temp_buff = new byte[20];
        System.arraycopy(all_hashes_array,i*20,temp_buff,0,20);
        this.piece_hashes[i] = ByteBuffer.wrap(temp_buff);
    }
}
