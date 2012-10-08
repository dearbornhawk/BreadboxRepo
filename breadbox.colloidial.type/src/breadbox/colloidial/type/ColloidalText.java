package breadbox.colloidial.type;

import java.util.concurrent.LinkedBlockingQueue;

import breadbox.util.WordComms;
import breadbox.util.WordListener;
import processing.core.PApplet;
import processing.core.PFont;
import processing.net.Client;
import geomerative.*;

public class ColloidalText extends PApplet implements WordListener {
	RFont font;
	PFont p;
	RGroup grp;
	RPoint[] pnts;
	float [] iniPntxs;
	float [] iniPntys;
	float [] iniPntxsT;
	float [] iniPntysT;
	char [] targetKeys = {
	  'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h',
	  'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p',
	  'q', 'r', 's', 't', 'u', 'v', 'w', 'x',
	  'y', 'z',
	  'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H',
	  'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P',
	  'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X',
	  'Y', 'Z',
	  '#', '@', '-',
	  '1', '2', '3', '4', '5', '6', '7', '8', '9', '0'
	  
	};
	float xOffset, yOffset;
	
	Client client;
	WordComms wordComms;
	int letterFrameCount = 0;
	char letter;
	boolean started = false;
	
	int lastMillis;
	
	LinkedBlockingQueue<Character> characterQueue = new LinkedBlockingQueue<Character>();
	
	Hand h;
	
	boolean drawCircles = true;
	boolean fillShape = true;
	
	
	@Override
	public void newLetter(char letter) {
		characterQueue.add(letter);
		started = true;
	}

	@Override
	public void endOfWord(String word) {
		characterQueue.add((char)this.WORDBREAK);
	}
	
	@Override
	public void endOfTweet(String tweet) {
		characterQueue.add((char)this.TWEETBREAK);
	}

	
	public void clientEvent(Client client){
		if(wordComms != null) wordComms.event();
	}

	 
	public void setup()
	{
	  background(255);
	  size(640, 480);
	  RG.init(this);
	  p = loadFont("./data/Corbel-BoldItalic-14.vlw");
	  font = new RFont( "./data/Corbel Bold Italic.ttf",250, RFont.CENTER);
	  frameRate(30);
	  xOffset = width/2;
	  yOffset = height/4*3;
	  grp = font.toGroup("c");
	  RCommand.setSegmentLength(1);
	  RCommand.setSegmentator(RCommand.UNIFORMLENGTH);
	  grp = grp.toPolygonGroup();
	  pnts = grp.getPoints();
	  iniPntxsT = new float[pnts.length];
	  iniPntysT = new float[pnts.length];
	  iniPntxs = new float[pnts.length];
	  iniPntys = new float[pnts.length];
	  for ( int i = 0; i < pnts.length; i++ )
	  {
	    iniPntxsT[i] = pnts[i].x+xOffset;
	    iniPntysT[i] = pnts[i].y+yOffset;
	    iniPntxs[i] = iniPntxsT[i]*1.1f+random(-50, 50);
	    iniPntys[i] = iniPntysT[i]*1.1f+random(-50, 50);
	  }
	  smooth();
	  
	  h = new Hand(width/2f,1*height/2f,20,15,12.0f,22.0f,3,5,7,-11, width, height);
	  lastMillis = millis();  
		  client = new Client(this,"localhost",5204);
		  wordComms = new WordComms(this,client);
		  client.write(handshake);

	}
	 
	public void draw()
	{
	  noStroke();
	  fill(255,30);
	  rect(0,0,width,height);
	  strokeWeight(0.5f);
	  stroke(0);
	  fill(255,20);
	  if(!started) return;
	  if(letterFrameCount == 0){
		  if(characterQueue.peek() != null){
				this.key = this.characterQueue.poll();
				this.letter = this.key;
				
				  if(letter == WORDBREAK){
					  wordComms.writeWordBreak();
					  return;
				  }
				  else if(letter == TWEETBREAK){
					  wordComms.writeTweetBreak();
					  return;
				  }
				
				keyPressed();
		  }else{
			  return;
		  }
	  }
	  
	  int tmp = millis();
	  float dt = (tmp-lastMillis)/1000.0f;
	  lastMillis = tmp;
	  
	  dt = 1/30f;
	  h.update( dt );

	  float maxDist = Float.MIN_VALUE;
	  float minDist = Float.MAX_VALUE;
	  
	  if(fillShape) fill(0);

	  beginShape();
	  for ( int i = 0; i < pnts.length; i++ )
	  {
	    iniPntxs[i] += (iniPntxsT[i]-iniPntxs[i])/5;
	    iniPntys[i] += (iniPntysT[i]-iniPntys[i])/5;
	    float x0 = pnts[i].x;
	    float y0 = pnts[i].y;
	    float a = h.xp-x0;
	    float b = h.yp-y0;
	    float r = sqrt(a*a+b*b);
	    float quer_fugir_x = pnts[i].x-(a/r)*200/r;
	    float quer_fugir_y = pnts[i].y-(b/r)*200/r;
	    float quer_voltar_x = (iniPntxs[i]-x0)/3;
	    float quer_voltar_y = (iniPntys[i]-y0)/3;
	    pnts[i].x = quer_fugir_x+quer_voltar_x;
	    pnts[i].y = quer_fugir_y+quer_voltar_y;
	    curveVertex(pnts[i].x, pnts[i].y);
	    if(drawCircles){
	    	float d = this.dist(pnts[i].x, pnts[i].y, h.xp, h.yp);
	    	if(d > maxDist) maxDist = d;
	    	if(d < minDist) minDist = d;
	    }
	  }
	  endShape();
	  rect(h.xp, h.yp, 2, 2);
	  if(drawCircles){
		  this.ellipseMode(CENTER);
		  this.fill(255);
		  for ( int i = 0; i < pnts.length; i++ ){
			  if(i % 25 == 0){
				  float r = 30f*dist(pnts[i].x, pnts[i].y, h.xp, h.yp)/maxDist;
				  this.ellipse(pnts[i].x, pnts[i].y, r, r);
			  }
		  }

	  }
	  fill(0,40);
	  if(letterFrameCount++ > 100){
		  wordComms.writeLetter(letter);
		  letterFrameCount = 0;
		  this.drawCircles = this.random(1.0f) > 0.5;
		  this.fillShape = this.random(1.0f) > 0.5;
	  }
	  
	   
	}
	 
	public void keyPressed() {
	  for (int i=0;i<targetKeys.length;i++) {
	    testKey(targetKeys[i]);
	  }
	}
	 
	void testKey(char keyInput_) {
	  if (key == keyInput_) {
	    String targetLetter = str(keyInput_);
	    grp = font.toGroup(targetLetter);
	    RCommand.setSegmentLength(1);
	    RCommand.setSegmentator(RCommand.UNIFORMLENGTH);
	    grp = grp.toPolygonGroup();
	    pnts = grp.getPoints();
	    iniPntxsT = new float[pnts.length];
	    iniPntysT = new float[pnts.length];
	    iniPntxs = new float[pnts.length];
	    iniPntys = new float[pnts.length];
	    for ( int i = 0; i < pnts.length; i++ )
	    {
	      iniPntxsT[i] = pnts[i].x+xOffset;
	      iniPntysT[i] = pnts[i].y+yOffset;
	      iniPntxs[i] = iniPntxsT[i]*1.1f+random(-50, 50);
	      iniPntys[i] = iniPntysT[i]*1.1f+random(-50, 50);
	    }
	  }
	}

	public static void main (String[] args)
	{
		String[] appletArgs = new String[args.length + 1];
		for(int i=0;i<args.length;i++) appletArgs[i] = args[i];
		appletArgs[args.length] = ColloidalText.class.getName();
		PApplet.main(appletArgs);
	}

}
