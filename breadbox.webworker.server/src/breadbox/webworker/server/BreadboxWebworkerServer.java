package breadbox.webworker.server;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.LinkedBlockingQueue;

import org.jwebsocket.api.WebSocketConnector;
import org.jwebsocket.api.WebSocketPacket;
import org.jwebsocket.config.JWebSocketConfig;
import org.jwebsocket.factory.JWebSocketFactory;
import org.jwebsocket.server.TokenServer;
import org.jwebsocket.tcp.TCPConnector;
import org.jwebsocket.token.JSONToken;
import org.jwebsocket.token.MapToken;
import org.jwebsocket.token.Token;

import org.jwebsocket.kit.WebSocketServerEvent;
import org.jwebsocket.listener.WebSocketServerTokenEvent;
import org.jwebsocket.listener.WebSocketServerTokenListener;

import processing.core.PApplet;
import processing.net.Client;
import processing.net.Server;
import breadbox.util.BreadboxConstants;

import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

public class BreadboxWebworkerServer extends PApplet implements
		BreadboxConstants, WebSocketServerTokenListener, UserStreamListener {

	TwitterStream twitterStream;
	TokenServer lTS;
	Server server;

	String incomingMessage = "";
	String currentMessage = null;

	String words[];

	ArrayList<Client> clientList = new ArrayList<Client>();
	LinkedBlockingQueue<String> messages = new LinkedBlockingQueue<String>(10);

	HashMap<Client, Integer> wordMap = new HashMap<Client, Integer>();
	HashMap<Client, Integer> stepOrder = new HashMap<Client, Integer>();

	String consumerKey = System.getProperty("consumerKey");
	String consumerSecret = System.getProperty("consumerSecret");
	String accessToken = System.getProperty("accessToken");
	String accessSecret = System.getProperty("accessSecret");

	public void disconnectEvent(Client client) {
		synchronized (clientList) {
			clientList.remove(client);
			wordMap.remove(client);
			stepOrder.remove(client);
		}
		// todo - need to adjust these!
	}

	public void serverEvent(Server server, Client client) {
		synchronized (clientList) {
			wordMap.put(client, -1);
			stepOrder.put(client, clientList.size());
			clientList.add(client);
		}
	}

	private void init(String args[]) {
		// start the jWebSocket server sub system
		JWebSocketConfig.initForConsoleApp(args);
		JWebSocketFactory.start();

		// if not loaded by jWebSocket.xml config file...
		lTS = (TokenServer) JWebSocketFactory.getServer("ts0");
		lTS.addListener(this);
		URL path = JWebSocketConfig.getURLFromPath("ts0");
		// System.out.println(path);
	}

	private void stopServer() {
		JWebSocketFactory.stop();
	}

	@Override
	public void processClosed(WebSocketServerEvent event) {
		connectorSet.remove(event.getConnector());
	}

	HashSet<WebSocketConnector> connectorSet = new HashSet<WebSocketConnector>();

	@Override
	public void processOpened(WebSocketServerEvent event) {
		// TODO Auto-generated method stub
		connectorSet.add(event.getConnector());

	}

	@Override
	public void processPacket(WebSocketServerEvent arg0, WebSocketPacket arg1) {
		// TODO Auto-generated method stub
		int x = 2;

	}

	@Override
	public void processToken(WebSocketServerTokenEvent arg0, Token arg1) {
		// TODO Auto-generated method stub
		int x = 2;

	}

	private void startTwitter() {

		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setOAuthConsumerKey(consumerKey);
		cb.setOAuthConsumerSecret(consumerSecret);
		cb.setOAuthAccessToken(accessToken);
		cb.setOAuthAccessTokenSecret(accessSecret);

		twitterStream = new TwitterStreamFactory(cb.build()).getInstance();
		twitterStream.addListener(this);
		// user() method internally creates a thread which manipulates
		// TwitterStream and calls these adequate listener methods continuously.
		twitterStream.filter(new FilterQuery()
				.track(new String[] { "Halloween" }));
		// twitterStream.user();
	}

	@Override
	public void onStatus(Status status) {
		// System.out.println("onStatus @" + status.getUser().getScreenName() +
		// " - " + status.getText());
		// stuff in a queue
		// send to the pipeline
		messages.offer(status.getUser().getScreenName() + " - "
				+ status.getText());
	}

	@Override
	public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
		// System.out.println("Got a status deletion notice id:" +
		// statusDeletionNotice.getStatusId());
	}

	@Override
	public void onDeletionNotice(long directMessageId, long userId) {
		// System.out.println("Got a direct message deletion notice id:" +
		// directMessageId);
	}

	@Override
	public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
		// System.out.println("Got a track limitation notice:" +
		// numberOfLimitedStatuses);
	}

	@Override
	public void onScrubGeo(long userId, long upToStatusId) {
		// System.out.println("Got scrub_geo event userId:" + userId +
		// " upToStatusId:" + upToStatusId);
	}

	@Override
	public void onFriendList(long[] friendIds) {
		// System.out.print("onFriendList");
		// for (long friendId : friendIds) {
		// System.out.print(" " + friendId);
		// }
		// System.out.println();
	}

	public void onFavorite(User source, User target, Status favoritedStatus) {
		// System.out.println("onFavorite source:@"
		// + source.getScreenName() + " target:@"
		// + target.getScreenName() + " @"
		// + favoritedStatus.getUser().getScreenName() + " - "
		// + favoritedStatus.getText());
	}

	public void onUnfavorite(User source, User target, Status unfavoritedStatus) {
		// System.out.println("onUnFavorite source:@"
		// + source.getScreenName() + " target:@"
		// + target.getScreenName() + " @"
		// + unfavoritedStatus.getUser().getScreenName()
		// + " - " + unfavoritedStatus.getText());
	}

	public void onFollow(User source, User followedUser) {
		// System.out.println("onFollow source:@"
		// + source.getScreenName() + " target:@"
		// + followedUser.getScreenName());
	}

	public void onRetweet(User source, User target, Status retweetedStatus) {
		// System.out.println("onRetweet @"
		// + retweetedStatus.getUser().getScreenName() + " - "
		// + retweetedStatus.getText());
	}

	public void onDirectMessage(DirectMessage directMessage) {
		// System.out.println("onDirectMessage text:"
		// + directMessage.getText());
	}

	public void onUserListMemberAddition(User addedMember, User listOwner,
			UserList list) {
		// System.out.println("onUserListMemberAddition added member:@"
		// + addedMember.getScreenName()
		// + " listOwner:@" + listOwner.getScreenName()
		// + " list:" + list.getName());
	}

	public void onUserListMemberDeletion(User deletedMember, User listOwner,
			UserList list) {
		// System.out.println("onUserListMemberDeleted deleted member:@"
		// + deletedMember.getScreenName()
		// + " listOwner:@" + listOwner.getScreenName()
		// + " list:" + list.getName());
	}

	public void onUserListSubscription(User subscriber, User listOwner,
			UserList list) {
		// System.out.println("onUserListSubscribed subscriber:@"
		// + subscriber.getScreenName()
		// + " listOwner:@" + listOwner.getScreenName()
		// + " list:" + list.getName());
	}

	public void onUserListUnsubscription(User subscriber, User listOwner,
			UserList list) {
		// System.out.println("onUserListUnsubscribed subscriber:@"
		// + subscriber.getScreenName()
		// + " listOwner:@" + listOwner.getScreenName()
		// + " list:" + list.getName());
	}

	public void onUserListCreation(User listOwner, UserList list) {
		// System.out.println("onUserListCreated  listOwner:@"
		// + listOwner.getScreenName()
		// + " list:" + list.getName());
	}

	public void onUserListUpdate(User listOwner, UserList list) {
		// System.out.println("onUserListUpdated  listOwner:@"
		// + listOwner.getScreenName()
		// + " list:" + list.getName());
	}

	public void onUserListDeletion(User listOwner, UserList list) {
		// System.out.println("onUserListDestroyed  listOwner:@"
		// + listOwner.getScreenName()
		// + " list:" + list.getName());
	}

	public void onUserProfileUpdate(User updatedUser) {
		// System.out.println("onUserProfileUpdated user:@" +
		// updatedUser.getScreenName());
	}

	public void onBlock(User source, User blockedUser) {
		// System.out.println("onBlock source:@" + source.getScreenName()
		// + " target:@" + blockedUser.getScreenName());
	}

	public void onUnblock(User source, User unblockedUser) {
		// System.out.println("onUnblock source:@" + source.getScreenName()
		// + " target:@" + unblockedUser.getScreenName());
	}

	public void onException(Exception ex) {
		ex.printStackTrace();
		System.out.println("onException:" + ex.getMessage());
	}

	public void draw() {

		synchronized (clientList) {

			if (clientList.isEmpty()) {
				return;
			}

			int clIndex = 0;
			for (Client cl : clientList){
				byte[] bytes = null;
				if(clIndex == 0){
					Client client = cl;
					if(client.available() > 0 && currentMessage != null){					
						bytes = client.readBytes();
						for(byte tb : bytes){
							if(tb == WORDBREAK || tb == HANDSHAKE){
								if (wordMap.containsKey(client)) {
									int nextWord = wordMap.get(client);
									if (nextWord + 2 < words.length) {
										nextWord++;
										String word = words[nextWord];
										wordMap.put(client, nextWord);
										for (byte c : word.getBytes()) {
											client.write(c);
										}
										client.write(wordBreak);
									} else {
										client.write(tweetBreak);
										wordMap.put(client, -1);
										currentMessage = null;
									}
								}
							}
							if(tb == this.TWEETBREAK){
								currentMessage = null;
							}

						}
					}else if(currentMessage == null){
						if (currentMessage == null) {
							currentMessage = messages.poll();
							if (currentMessage == null) {
								return;
							}
							// System.out.println(currentMessage);
							words = currentMessage.split(" ");
							
							int nextWord = wordMap.get(client);
							if (nextWord + 2 < words.length) {
								nextWord++;
								String word = words[nextWord];
								wordMap.put(client, nextWord);
								for (byte c : word.getBytes()) {
									client.write(c);
								}
								client.write(wordBreak);
							}
							
						}
						
					}

				}
				if(clIndex > 0 && cl.available() > 0){
					bytes = cl.readBytes();
				}
				if (bytes != null) {
					
					System.out.println("Client " + clIndex++ + " - " + cl.available());
					Client client = cl;
					int step = this.stepOrder.get(client);
					Client nextClient = null;
					if (step + 1 < this.clientList.size()) {
						nextClient = clientList.get(step + 1);
						nextClient.write(bytes);
					} else {
						// all done
						for(byte tb : bytes){
							if(tb == TWEETBREAK){
								currentMessage = null;
								JSONToken token = new JSONToken();
								token.setInteger("words", words.length);
								this.lTS.broadcastToken(token);
							}
						}

					}
					
					if (client.equals(clientList.get(0))) {
					}
				}
				clIndex++;
			}
		}
	}

	public void setup() {
		// frameRate(1);
		

		init(new String[] {});
		server = new Server(this, 5204);
		startTwitter();
	}

	public static void main(String args[]) {
		PApplet.main(new String[] { "--bgcolor=#F0F0F0",
				"breadbox.webworker.server.BreadboxWebworkerServer" });
	}

}
