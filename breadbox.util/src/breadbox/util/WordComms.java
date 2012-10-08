package breadbox.util;

import processing.net.Client;

public class WordComms implements BreadboxConstants {
	
	WordListener listener;
	Client client;
	boolean running;
	StringBuffer buffer = new StringBuffer();
	StringBuffer tweetBuffer = new StringBuffer();
	
	String handshakeString = new String(handshake);
	String wordBreakString = new String(wordBreak);
	
	public WordComms(WordListener listener, Client client){
		this.listener = listener;
		this.client = client;
	}
	
	public void event(){
		boolean active = client.active();
		if(client.available() > 0){
			byte[] bytes = client.readBytes();
			for(byte b : bytes){
				switch((int)b){
				case HANDSHAKE:
//					System.out.println("Read Handshake");
					break;
				case WORDBREAK:
					listener.endOfWord(buffer.toString());
//					System.out.println("Read Word Break " + buffer.toString());
					buffer.setLength(0);
					break;
				case TWEETBREAK:
					listener.endOfTweet(tweetBuffer.toString());
//					System.out.println("Read Tweet Break " + tweetBuffer.toString());
					tweetBuffer.setLength(0);
					break;
				default:
					buffer.append(new String(new byte[]{b}));
					tweetBuffer.append(new String(new byte[]{b}));
					listener.newLetter((char)b);
					break;
				}
			}
		}
	}
	
	public void writeLetter(char letter){
		Character c = new Character(letter);
		byte[] bytes = c.toString().getBytes();
		client.write((byte)letter);
//		System.out.println("Letter" + letter);
	}
	
	public void writeWord(String word){
		word = word.trim();
		for(byte b : word.getBytes())
			client.write(new byte[]{b});
		System.out.println("Word <" + word + ">");
		writeWordBreak();
	}
	
	public void writeWordBreak(){
		client.write(wordBreak);
//		System.out.println("Word break");
	}
	
	public void writeTweetBreak(){
		client.write(tweetBreak);
//		System.out.println("Tweet break");
	}

}
