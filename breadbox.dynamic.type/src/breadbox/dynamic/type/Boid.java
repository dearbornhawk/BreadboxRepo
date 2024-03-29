package breadbox.dynamic.type;

import processing.core.PVector;


class Boid {
	 
	  PVector loc, vel, acc;
	  float r, maxforce, maxspeed;
	  boolean arrived;
	  int c;
	 
	  Boid(PVector l, float ms, float mf, int c, boolean arrived) {
	    acc = new PVector(0,0);
	    vel = new PVector(0,0);
	    loc = l.get();
	    r = 3.0f;
	    maxspeed = ms;
	    maxforce = mf;
	    this.c=c;
	    this.arrived = arrived;
	  }
	 
	  // Method to update location
	  void update() {
	    // Update velocity
	    vel.add(acc);
	    // Limit speed
	    vel.limit(maxspeed);
	    loc.add(vel);
	    // Reset accelertion to 0 each cycle
	    acc.mult(0);
	  }
	 
	  void arrive(PVector target) {
	    acc.add(steer(target,true));
	  }
	 
	  // A method that calculates a steering vector towards a target
	  // Takes a second argument, if true, it slows down as it approaches the target
	  PVector steer(PVector target, boolean slowdown) {
	    PVector steer;  // The steering vector
	    PVector desired = PVector.sub(target,loc);  // A vector pointing from the location to the target
	    float d = desired.mag(); // Distance from the target is the magnitude of the vector
	    // If the distance is greater than 0, calc steering (otherwise return zero vector)
	    if (d > 0) {
	      // Normalize desired
	      desired.normalize();
	      // Two options for desired vector magnitude (1 -- based on distance, 2 -- maxspeed)
	      if ((slowdown) && (d < 100.0f)) desired.mult(maxspeed*(d/100.0f)); // This damping is somewhat arbitrary
	      else desired.mult(maxspeed);
	      // Steering = Desired minus Velocity
	      steer = PVector.sub(desired,vel);
	      steer.limit(maxforce);  // Limit to maximum steering force
	    }
	    else {
	      steer = new PVector(0,0);
	    }
	    return steer;
	  }
	}
