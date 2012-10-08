package breadbox.dynamic.type;

import processing.core.PApplet;

public class ImgProc {
	
	PApplet applet;
	 
	  ImgProc(PApplet applet) {
		  this.applet = applet;
	  }
	 
	  void drawPixelArray(int[] src, int dx, int dy, int w, int h) { 
		  applet.loadPixels();
	    int x;
	    int y;
	    for(int i=0; i<w*h; i++) {
	      x = dx + i % w;
	      y = dy + i / w;
	      applet.pixels[x  + y * w] = src[i];
	    }
	    applet.updatePixels();
	  }
	 
	  void blur(int[] src, int[] dst, int w, int h) {
	    int c;
	    int r;
	    int g;
	    int b;
	 
	    for(int y=1; y<h-1; y++) {
	      for(int x=1; x<w-1; x++) {     
	        r = 0;
	        g = 0;
	        b = 0;
	        for(int yb=-1; yb<=1; yb++) {
	          for(int xb=-1; xb<=1; xb++) {
	            c = src[(x+xb)+(y-yb)*w];     
	            r += (c >> 16) & 0xFF;
	            g += (c >> 8) & 0xFF;
	            b += (c) & 0xFF;
	          }
	        }     
	        r /= 9;
	        g /= 9;
	        b /= 9;
	        dst[x + y*w] = 0xFF000000 | (r << 16) | (g << 8) | b;
	      }
	    }
	  }
	}

