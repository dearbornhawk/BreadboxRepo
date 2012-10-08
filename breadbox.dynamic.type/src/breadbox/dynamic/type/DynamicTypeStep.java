package breadbox.dynamic.type;

import java.util.ArrayList;

import breadbox.util.WordComms;
import breadbox.util.WordListener;

import processing.core.PApplet;
import processing.core.PVector;
import processing.net.Client;
import geomerative.*;

public class DynamicTypeStep extends PApplet implements WordListener {
	
	ImgProc imgProc;
	RFont font;
	 
	String inp = "", lastText;
	String zin = "";
	 
	int fontSize = 170;
	int currWord = 0;
	 
	int[] currFrame;
	int[] prevFrame;
	int[] tempFrame;
	String[] words = split(zin, " ");
	
	String message;
	
	byte[] handshake = new byte[] {(byte)0xCA,(byte)0xFE};
	 
	boolean newtext = true, auto = true, pauze = false;
	 
	ArrayList seekers, coords;
	
	boolean resetWords = false;
	boolean resetTweet = false;
	Client client;
	WordComms wordComms;
	
	
	@Override
	public void newLetter(char letter) {
		System.out.println(letter);
	}

	@Override
	public void endOfWord(String word) {
		// TODO Auto-generated method stub
		words = split(word, " ");
		resetWords = true;
		message = word;
	}

	@Override
	public void endOfTweet(String tweet) {
		resetTweet = true;
	}
	
	
	public void clientEvent(Client client){
		if(wordComms != null) wordComms.event();
	}
	
	public void setup(){
		 imgProc = new ImgProc(this);
		 size(640, 480);
		  noCursor();
		  RG.init(this);
		  font = new RFont("./data/haas.ttf", fontSize, RFont.CENTER);
		  seekers = new ArrayList();
		  coords = new ArrayList();
		  noStroke();
		  noCursor();
		  fill(0);
		  frameRate(30);
		 
		  //imgProc
		  currFrame = new int[width*height];
		  prevFrame = new int[width*height];
		  tempFrame = new int[width*height];
		  for(int i=0; i<width*height; i++) {
		    currFrame[i] = color(0);
		    prevFrame[i] = color(0);
		    tempFrame[i] = color(0);
		  }
		  
		  
		  client = new Client(this,"localhost",5204);
		  wordComms = new WordComms(this,client);
		  client.write(handshake);
		 
	}
	
	public void draw(){
		
		imgProc.blur(prevFrame, tempFrame, width, height);
		arraycopy(tempFrame, currFrame);
		 
		if(words.length == 0) return;
		
		  if((resetWords) && auto) {
		    inp = words[currWord];
		  }
		 
		  RGroup grp = font.toGroup(inp);
		  RCommand.setSegmentLength(1);
		  RCommand.setSegmentator(RCommand.UNIFORMLENGTH);
		  RPoint[] pnts = grp.getPoints();
		 
		  if(pnts != null) {
		    if(newtext) {
		      coords = new ArrayList();
		    }
		    if(pnts.length > 0) {
		      update(pnts.length, pnts, 4);
		    }
		    if(newtext) {
		      newtext = false;
		    }
		 
		    //add seekers if there are more points than seekers
		    checkSeekerCount(pnts.length);
		 
		    if(auto) {
		      if(arrived() == 100) {
		        pauze = true;
		      }
		      if(arrived() == 0 && pauze && seekers.size() == 0){
		        println("nieuw");
		        if(currWord < words.length-1) {
		          currWord++;
		          inp = words[currWord];
		        }
		        else {
		          currWord = 0;
		          inp = words[currWord];
		          wordComms.writeWord(message);
		          if(resetTweet){
		        	  wordComms.writeTweetBreak();
		        	  resetTweet = false;
		          }
		        }
		        pauze = false;
		      }
		       
		    }
		  }
		  else {
		    seekers = new ArrayList();
		  }
		  if(inp != lastText) {
		    newtext = true;
		  }
		  lastText = inp;
		  
		  imgProc.drawPixelArray(currFrame, 0, 0, width, height); 
		  arraycopy(currFrame, prevFrame);
		
	}
	
	float arrived() {
		  float arrived = 0;
		  if(coords != null) {
		    for(int i = 0; i < seekers.size(); i++) {
		      Boid seeker = (Boid) seekers.get(i);
		      if(seeker.arrived == true) {
		        arrived++;
		      }
		    }
		    return (arrived/coords.size())*100;
		  }
		  else {
		    return 0;
		  }
		}
		 
		void checkSeekerCount(int count) {
		  if(count > 1) {
		    if((seekers.size() < count) && (!pauze)) {
		      for(int y = 0; y < 15; y++) {
		        int px = (int) random(width);
		        int py = (int) random(height);
		        int c = color(19, 134, 214);
		        newSeeker(px,py,c);
		      }
		    }
		    if((seekers.size() > count)) {
		      for(int z = 0; z < (seekers.size() - count); z++) {
		        seekers.remove(seekers.size()-1);
		      }
		    }
		    if(pauze && seekers.size() > 15) {
		      for(int z = 0; z < 15; z++) {
		        seekers.remove(0);
		      }
		    }
		    else if(pauze && seekers.size() > 0) {
		      if((frameCount % 10) == 0) {
		        seekers.remove(0);
		      }
		    }
		  }
		}
		 
		void newSeeker(float x, float y, int c) {
		  float maxspeed = random(13,20);
		  float maxforce = random(0.5f, 0.6f);
		  seekers.add(new Boid(new PVector(x,y),maxspeed,maxforce,c,false));
		}
		 
		void update(int count, RPoint[] pnts, int baseR) {
		  for ( int i = 0; i < count; i++ )
		  {
		    float mx = (pnts[i].x)+width/2;
		    float my = (pnts[i].y)+height/2;
		 
		    if(newtext) {
		      coords.add(new Point(mx, my, false));
		    }
		 
		    if((i < seekers.size()) && (i < coords.size())) {
		      Boid seeker = (Boid) seekers.get(i);
		      Point coord = (Point) coords.get(i);
		      if(!pauze) {
		        seeker.arrive(new PVector(coord.x,coord.y));
		        seeker.update();
		      }
		      if((seeker.loc.x >= 0) && (seeker.loc.x < width-1) && (seeker.loc.y >= 0) && (seeker.loc.y < height-1)) {
		        int currC = currFrame[(int)seeker.loc.x + ((int)seeker.loc.y)*width];
		        currFrame[(int)seeker.loc.x + ((int)seeker.loc.y)*width] = blendColor(seeker.c, currC, ADD);
		      }
		 
		      if(((seeker.loc.x > mx-1) && (seeker.loc.x < mx+1)) && ((seeker.loc.y > my-1) && (seeker.loc.y < my+1)) && (coord.arrived == false)) {
		        seeker.arrived = true;
		      }
		      else {
		        seeker.arrived = false;
		      }
		    }
		  }
		}
		 
		public void keyPressed () {
		  if(auto == false) {
		    if (  keyCode == DELETE || keyCode == BACKSPACE ) {
		      if ( inp.length() > 0 ) {
		        inp = inp.substring(0,inp.length()-1);
		      }
		    }
		    else if (key != CODED) {
		      inp = inp + key;
		    }
		  }
		}
		
		 
		public void mouseReleased() {
		  //skip word
		  pauze =! pauze;
		}
	
	
	public static void main (String[] args)
	{
		String[] appletArgs = new String[args.length + 1];
		for(int i=0;i<args.length;i++) appletArgs[i] = args[i];
		appletArgs[args.length] = DynamicTypeStep.class.getName();
		PApplet.main(appletArgs);
	}


}
