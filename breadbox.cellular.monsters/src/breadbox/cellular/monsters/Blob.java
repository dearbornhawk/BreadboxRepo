package breadbox.cellular.monsters;

import java.util.ArrayList;

import toxi.geom.Vec2D;
import toxi.physics2d.VerletSpring2D;
import toxi.physics2d.behaviors.AttractionBehavior;

public class Blob {
	
	boolean chasing = false;
	
		
	ArrayList<Particle> allParticles;
	  ArrayList<Particle> allFace;
	  ArrayList allSprings;
	   
	  Particle body, head, n_eye1, n_eye2, n_mouth; //nucleo, head, eyes
	  float large;
	   
	  Monsters pApplet;
	 
	//define constructor
	Blob(  float x, float y, float sizeBlob, Monsters pApplet){
	   
	 this.pApplet = pApplet;
	//setup
	int particles_number= 30;
	float angle=pApplet.radians(pApplet.random(360));
	float u = pApplet.radians(360/particles_number);
	float v;
	large = sizeBlob; //dimension connections
	float _x = x;
	float _y = y;
	 
	  allParticles = new ArrayList<Particle>();
	  allFace = new ArrayList<Particle>();
	  allSprings = new ArrayList();
	   
	 
	   
	  
	  
	   ////////////// BODY
	   
	  body = new Particle(_x,_y,pApplet);
	   
	  body.r = large*1.3f; // ratio big circle
	  pApplet.physics.addBehavior(new AttractionBehavior(body, large*0.7f, -1));
	   
	  head = new Particle(_x,_y,pApplet);
	  
	  pApplet.physics.addParticle(body);
	 
	 
	    
	  head.r = large*1.1f;
	   
	  n_eye1 = new Particle(head.x,head.y,pApplet);
	  n_eye1.r = large*0.06f;
	   
	  n_eye2 = new Particle(head.x,head.y,pApplet);
	  n_eye2.r = large*0.06f;
	   
	  n_mouth = new Particle(head.x,head.y,pApplet);
	  n_mouth.r = 0;
	 
	 
	   
	  VerletSpring2D spring_body = new VerletSpring2D(body, head, large*0.4f, 0.1f); //join the body and face
	  VerletSpring2D spring_eye1 = new VerletSpring2D(body, n_eye1, large*0.5f, 0.1f);
	  VerletSpring2D spring_eye2 = new VerletSpring2D(body, n_eye2, large*0.5f, 0.1f);
	   
	  VerletSpring2D spring_eye1_1 = new VerletSpring2D(head, n_eye1, large*0.1f, 0.1f);
	  VerletSpring2D spring_eye2_2 = new VerletSpring2D(head, n_eye2, large*0.1f, 0.1f);
	   
	  VerletSpring2D spring_eyes = new VerletSpring2D(n_eye2, n_eye1, large*0.15f, 0.1f);
	  VerletSpring2D spring_mouth = new VerletSpring2D(body, n_mouth, large*0.3f, 0.1f);
	  VerletSpring2D spring_mouth2 = new VerletSpring2D(n_eye1, n_mouth, large*0.18f, 0.1f);
	  VerletSpring2D spring_mouth3 = new VerletSpring2D(n_eye2, n_mouth, large*0.18f, 0.1f);
	 
	 
	  pApplet.physics.addSpring(spring_body);
	  pApplet.physics.addSpring(spring_eye1);
	  pApplet.physics.addSpring(spring_eye2);
	  pApplet.physics.addSpring(spring_eye1_1);
	  pApplet.physics.addSpring(spring_eye2_2);
	  pApplet.physics.addSpring(spring_eyes);
	  pApplet.physics.addSpring(spring_mouth);
	  pApplet.physics.addSpring(spring_mouth2);
	  pApplet.physics.addSpring(spring_mouth3);
	   
	    ////////////// END BODY
	 
	 
	  //////////////////////////physics.addParticle(body);
	  for (int i= 0; i< particles_number; i++) {
	     
	    v = large + 3 * pApplet.sin(u*i*(particles_number/2.5f));
	    Particle p = new Particle(x + v * pApplet.cos(angle + i*u), y + v * pApplet.sin(angle + i*u), pApplet); //arrange different ratios in a sinusoidal way
	    p.r = pApplet.random(large*0.05f,large*0.2f);
	    pApplet.physics.addParticle(p);
	    allParticles.add(p);
	    pApplet.physics.addBehavior(new AttractionBehavior(p, 10, -1));
	 
	     
	    // add a spring between body and every p
	     
	    VerletSpring2D spring = new VerletSpring2D(body, allParticles.get(i), v-large*0.25f, 0.01f);
	    pApplet.physics.addSpring(spring);
	    allSprings.add(spring);
	     
	 
	     
	     
	     
	  }
	  //////////////////////////end physics.addParticle(body);
	   
	 
	  //////////////////////////physics.addParticle(head);
	   
	  for (int i= 0; i< 30; i++) {
	     
	    v = large*0.65f + 10 * pApplet.sin(u*i*(30/5.5f));
	    Particle p = new Particle(x + v * pApplet.cos(angle + i*u), y + v * pApplet.sin(angle + i*u), pApplet); //arrange different ratios in a sinusoidal way
	    p.r = pApplet.random(large*0.02f,large*0.1f);
	    pApplet.physics.addParticle(p);
	    allFace.add(p);
	    p.r=pApplet.random(1,4);
	    pApplet.physics.addBehavior(new AttractionBehavior(p, 10, -1));
	 
	    // add a spring between n2 and every p
	     
	    VerletSpring2D spring_face = new VerletSpring2D(head, allFace.get(i), v, 0.01f);
	    pApplet.physics.addSpring(spring_face);
	    allSprings.add(spring_face);
	         
	     
	     
	  }
	   
	 //////////////////////////end physics.addParticle(head);
	   
	  }
	   
	  //end define constructor
	   
	  void display(int c){
	     
		  pApplet.physics.update();
	     
	    for (Particle p : allParticles) {
	    p.displayParticles();
	    pApplet.stroke(c);
	    pApplet.strokeWeight(3);
	    pApplet.line(body.x, body.y, p.x, p.y);
	     
	    }
	     
	    for (Particle p : allFace) {
	    p.displayParticles();
	    pApplet.stroke(c);
	    pApplet.strokeWeight(0.5f);
	    pApplet.line(head.x, head.y, p.x, p.y);
	     
	    
	  }
	     
	   
	  body.displayParticles();
	  head.displayParticles();
	  n_eye1.displayFace(pApplet.floor(large));
	  n_eye2.displayFace(pApplet.floor(large));
	   
//	  Vec2D f = new Vec2D(0,(large*0.05f*(pApplet.mouseY-pApplet.height*0.5f)/10));
	  if(chasing){
		  Vec2D f = new Vec2D(0,(large*0.05f*(pApplet.textPosY- this.body.y)/10));
		  body.addForce(f);
		  if(pApplet.textPosX < body.x+body.r && pApplet.textPosX > body.x-body.r){
			  if(pApplet.textPosY < body.y+body.r && pApplet.textPosY > body.y-body.r){
				  pApplet.setWinningBlob(this);
			  }
		  }
	  }else{
		  Vec2D f = new Vec2D(0,(large*0.05f*(-pApplet.height*0.5f)/10));
		  body.addForce(f);
	  }
	   
	  float boca = pApplet.abs(pApplet.mouseY-pApplet.height*0.5f);
	  float bocaMap = pApplet.map(boca, 0,200,0,large*0.2f);
//	  pApplet.println(bocaMap);
	   
	   
	  
	  if (body.y > pApplet.height-large*0.5){
//	    body.y = height;
//	    body.lock();
	    n_mouth.displayMouth(4);
	  }
	   
	  else if (body.y < large*0.5){
//	    body.y = 0;
//	    body.lock();
	    n_mouth.displayMouth(4);
	  }
	   
	   
	  else{
	     n_mouth.displayMouth(bocaMap);
	     if(bocaMap>3){
//	        sound.play();
	      }else{
//	        sound.rewind();
	 
	  }
	  }
	   
	   
	   
	   
	   
	  }

}
