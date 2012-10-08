package breadbox.util;



public interface BreadboxConstants {
	
	final byte HANDSHAKE = 0x01;
	final byte WORDBREAK = 0x02;
	final byte TWEETBREAK = 0x03;
	
	byte[] handshake = new byte[] {(byte)HANDSHAKE};
	byte[] wordBreak = new byte[] {(byte)WORDBREAK};
	byte[] tweetBreak = new byte[] {(byte)TWEETBREAK};
	
}
