package breadbox.cellular.monsters;

import toxi.physics2d.VerletParticle2D;

public class Particle extends VerletParticle2D {
	float r;
	   Monsters pApplet;
	  Particle(float x, float y, Monsters pApplet) {
	    super(x,y);
	    this.pApplet = pApplet;
	  }
	   
	 
	  // All we're doing really is adding a display() function to a VerletParticle
	  void displayParticles() {
	    //fill(0,204,242);
		  pApplet.noStroke();
		  pApplet.ellipse(x,y,r,r);
	  }
	   
	 void displayFace(int delayEyes) {
	 
	   float time = pApplet.millis()/(100+delayEyes);
	   float blinkAmount = time%20;
	   int escala =0;
	   if(blinkAmount==0 || blinkAmount==2) escala=0;
	   else escala=1;
	   pApplet.fill(0);
	   pApplet.noStroke();
	   pApplet.ellipse(x,y,escala*r,escala*r);
	    
	 }
	   
	  void displayMouth(float escala) {
	     
		  pApplet.fill(0);
		  pApplet.noStroke();
		  pApplet.ellipse(x,y,escala,escala);
	     
	  }


}
