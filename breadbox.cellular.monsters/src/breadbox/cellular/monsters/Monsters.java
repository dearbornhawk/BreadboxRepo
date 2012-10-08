package breadbox.cellular.monsters;

import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PVector;
import processing.net.Client;
import toxi.geom.Rect;
import toxi.geom.Vec2D;
import toxi.physics2d.VerletPhysics2D;
import breadbox.util.WordComms;
import breadbox.util.WordListener;


public class Monsters extends PApplet implements WordListener {
	
	Monster [][]monsters;
	int row = 1;
	int col = 16;
	 
	PFont f;
	boolean debugging = false;
	boolean makeMovie = false;
	
	int textPosX;
	int textPosY;
	 
	 
	long lastRegenerate;
	
//	float x;  // horizontal location of headline
	int index = 0;
	 
	 
	CA ca;   // An instance object to describe the Wolfram basic Cellular Automata
	 
	CA caDanceX;
	CA caDanceY;
	CA caDancePeriod;
	 
	int []lastCaX = new int[col];
	int []lastCaY = new int[col];
	
	Client client;
	WordComms wordComms;
	String word;
	boolean resetTweet = false;

	//flying monsters
	VerletPhysics2D physics;
	int numberBlobs = 5; //declaring the elements of array
	Blob[] blobs= new Blob [numberBlobs]; //declaring the array
	 
	int[] mycolours = {color(0,204,242),color(97,200,60),color(255,205,0),color(255,128,10),color(255,81,130),color(183,106,253)};
	 

	Blob winningBlob = null;

	
	@Override
	public void newLetter(char letter) {
	}

	@Override
	public void endOfWord(String word) {
		this.word = word;
		winningBlob = null;
		//set the next followers
		for(Blob  blob : blobs){
			blob.chasing = false;
			float rand = random(1);
			float test = 1.0f/(blobs.length);
			if(rand < test)
//			if(this.random(1) < 1.0f/(blobs.length*3.0f)){
				blob.chasing = true;
//			}
		}
	}
	
	@Override
	public void endOfTweet(String tweet) {
		resetTweet = true;
	}


	
	
	public void clientEvent(Client client){
		if(wordComms != null) wordComms.event();
	}

	 
	public void setup()
	{
	    size(640,480);
	    frameRate(15);
	    smooth();
	    f = createFont("Helvetica", 12);
	    textFont(f);
	    textPosX = width;
	     
	     
	    monsters = new Monster[row][col];
	 
	    for (int i = 0; i < row ; i++)
	    {
	        for (int j = 0; j < col ; j++)
	        {
	            monsters[i][j] = new Monster(new PVector( (j + 0.5f) * width / col , (i + 1) * height / row - 10, 0), this);
	        }
	    }
	     
	    int[] ruleset = {0,1,0,1,1,0,1,0};    // An initial rule system
	  ca = new CA(ruleset, this);                 // Initialize CA
	    caDanceX = new CA(ruleset, this);
	    caDanceY = new CA(ruleset,this);
	    caDancePeriod = new CA(ruleset,this);
	     
	    lastCaX = caDanceX.cells;
	    lastCaY = caDanceY.cells;
	     
	     
	    lastRegenerate = frameCount;
	    
	    //setup blobs
	    physics=new VerletPhysics2D();
	    Vec2D center = new Vec2D(width/2, height/2);
	    // these are the worlds dimensions (50%, a vector pointing out from the center in both directions)
	    Vec2D extent = new Vec2D(width/2, height/2);
	   
	    // Set the world's bounding box
	    physics.setWorldBounds(Rect.fromCenterExtent(center, extent));
	     
	    for (int i=0; i< numberBlobs;i++) {
	    	blobs [i] = new Blob(random(width), random(50, height-50), random(40,80), this);
	    }
	     

	 
		  client = new Client(this,"localhost",5204);
		  wordComms = new WordComms(this,client);
		  client.write(handshake);
		  
		  textPosX = width;
		  textPosY = height/2;

	 
	}
	
	public void setWinningBlob(Blob winningBlob){
		if(this.winningBlob == null){
			for(Blob blob : blobs) blob.chasing = false;
			this.winningBlob= winningBlob;
			this.winningBlob.chasing = true;
		}
	}
	 
	public void draw()
	{
	    //ca.render();    // Draw the CA
	    //println(frameCount);
	    if (frameCount % 1 == 0)
	    {
	        ca.generate();  // Generate the next level
	    }
	     
	    if (frameCount % 60 == 0)
	    {
	        caDanceX.generate();
	        caDanceY.generate();
	        caDancePeriod.generate();
	         
	         
	    }
	    
	   

	  /*
	  if (ca.finished()) {   // If we're done, clear the screen, pick a new ruleset and restart
	    //background(0);
	    ca.randomize();
	    ca.restart();
	  }
	    */
	     
	     
	    //background(240,240,220);
	    background(120,120,110);
	    if(word != null){
		    textFont(f,16);        
		    textAlign(LEFT);
//		    text(word,x,20); 
		    textFont(f);         
		    int x = textPosX;
		    for (int i = 0; i < word.length(); i++) {
		      textSize(random(16,36));
		      if(winningBlob != null){
		    	  if(x > winningBlob.body.x){
				      	text(word.charAt(i),x,height/2);
		    	  }else
			    	  if(i == word.length()-1){
			    		  winningBlob.chasing = false;
			    	  }
		      }else{
			      	text(word.charAt(i),x,height/2);
		      }
		      	
		      // textWidth() spaces the characters out properly.
		      x += textWidth(word.charAt(i)); 
		    }
		    // Decrement x
		    textPosX -= 10;

		    // If x is less than the negative width, 
		    // then it is off the screen
		    float w = textWidth(word);
		    if (textPosX < -w) {
		      textPosX = width; 
		      if(winningBlob == null){
		    	  wordComms.writeWord(word);
		      }
		      else{
		    	  wordComms.writeWordBreak();
		      }
		      winningBlob = null;
		      word = null;
				for(Blob  blob : blobs)
					blob.chasing = false;
						
		      
		    }
	    }

	    for (int i = 0; i < row ; i++)
	    {
	        for (int j = 0; j < col ; j++)
	        {
	            Monster mon = monsters[i][j];
	             
	            if (frameCount % 1 == 0)
	            {
	                if (ca.cells[j] > 0)
	                {
	                    //mon.moodColor = 150;
	                    //mon.moodColor = 360;
	                     
	                    /*
	                    if (mon.moodColor > 150)
	                    {
	                        mon.moodColor -= 150;
	                        mon.moodColor += 50;
	                    }
	                    */
	                     
	                     
	                    mon.moodColor += 18;
	                     
	                    if (mon.moodColor > 360)
	                    {
	                        mon.moodColor -= 360;
	                        //mon.moodColor -= 50;
	                    }
	                     
	                }
	                else
	                {
	                    mon.moodColor -= 1;
	                     
	                    if (mon.moodColor < 0)
	                    {
	                        mon.moodColor += 360;
	                        //mon.moodColor -= 50;
	                    }
	                     
	                    /*
	                    if (mon.moodColor < 50)
	                    {
	                        mon.moodColor += 150;
	                        mon.moodColor -= 50;
	                    }
	                    */
	                }
	                //mon.moodColor = max(min(50,mon.moodColor),150);
	            }
	            

	             
	            if ((frameCount - lastRegenerate) % 60 == 0)
	            {
	                 
	                if (caDancePeriod.cells[j] > 0)
	                {
	                    if (random(0,1) > 0.5)
	                    {
	                        mon.nextPeriod = 20;
	                    }
	                    else
	                    {
	                        mon.nextPeriod = 40;
	                    }
	                }
	                else
	                {
	                    if (random(0,1) > 0.5)
	                    {
	                        mon.nextPeriod = 60;
	                    }
	                    else
	                    {
	                        mon.nextPeriod = 120;
	                    }
	                     
	                }
	                 
	                if (caDanceX.cells[j] != lastCaX[j])
	                {
	                    //println("X changed");
	                    mon.nextAmpX = 25 - mon.nextAmpX;
	                }
	                else
	                {
	                    //mon.nextAmpY = 5;
	                }
	                 
	                if (caDanceY.cells[j] != lastCaY[j])
	                {
	                    //println("Y changed");
	                    mon.nextAmpY = 25 - mon.nextAmpY;
	                }
	                else
	                {
	                    //mon.nextAmpX = 20;
	                }
	                 
	                mon.rebuildBody();
	            }
	             
	            //mon.mouseMoved();
	            //mon.look(i,j);
	            mon.update();
	            mon.show();
	             
				if(resetTweet){
					wordComms.writeTweetBreak();
					resetTweet = false;
				}

	             
	            lastCaY[j] = caDanceY.cells[j];
	            lastCaX[j] = caDanceX.cells[j];
	        }
	    }
	    
	    for (int i = 0; i< numberBlobs; i++) {
	        fill(mycolours[i%(mycolours.length)]);
	        blobs[i].display(mycolours[i]);
	     
	      }

	 
	     
	    if (debugging)
	    {
	        fill(255);
	        textMode(CENTER);
	        text(ca.printRules(), width/2, 20);
	    }
	}
	 
	public void keyPressed()
	{
	    if (key == ' ')
	    {
	         
	        for (int i = 0; i < row ; i++)
	        {
	            for (int j = 0; j < col ; j++)
	            {
	                monsters[i][j] = new Monster(new PVector( (j + 0.5f) * width / col , (i + 1) * height / row - 10, 0), this);
	            }
	        }
	         
	        ca.randomize();
	         
	        caDanceX.randomize();
	        caDanceY.randomize();
	        caDancePeriod.randomize();
	         
	        lastCaX = caDanceX.cells;
	        lastCaY = caDanceY.cells;
	         
	        lastRegenerate = frameCount;
	    }
	     
	    if (key == 'd')
	    {
	        debugging = !debugging;
	    }
	 
	     
	}
	 
	public void mousePressed()
	{
	    for (int i = 0; i < row ; i++)
	    {
	        for (int j = 0; j < col ; j++)
	        {
	            Monster mon = monsters[i][j];
	            mon.mousePressed();
	        }
	    }
	}

	public static void main (String[] args)
	{
		PApplet.main(new String[] { "--bgcolor=#F0F0F0", "breadbox.cellular.monsters.Monsters" });
	}


}
