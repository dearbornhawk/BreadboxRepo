package breadbox.colloidial.type;

import java.awt.geom.Point2D;

public class Hand
{
  int na;
  Arm []a;
  float xpos ;
  float ypos ;
  float vx,vy;
  float sx,sy;
  float px,py;
  float width;
  float height;
  
  float xp;
  float yp;


  Hand( float xi, float yi, float vxi, float vyi, float sxi, float syi,
         int a1,int a2, int a3, int a4, int width,int height )
  {
    this.width = width;
    this.height = height;
    
    na = 4;
    a = new Arm[na];

    a[0] = new Arm( 5,0,(float)(2*Math.PI/a1));
    a[1] = new Arm( 5,0,(float)(2*Math.PI/a2));
    a[2] = new Arm( 5,0,(float)(2*Math.PI/a3));
    a[3] = new Arm( 5,0,(float)(2*Math.PI/a4));
  
    xpos = xi;
    ypos = yi;
    vx = vxi;
    vy = vyi;
    sx = sxi;
    sy = syi;
  }
  
  public void update( float dt )
  {
  int i,j;
   float rx = 1;
   float ry = 0;
   float l = (float)Point2D.distance(0,0,vx,vy);
   if( l!=0)
   { 
     rx = vx/l;
     ry = vy/l;
   }


    for(i=0; i<na;i++)
    {
      a[i].update(dt);
    }
    
    float [] ax = new float[na];
    float [] ay = new float[na];
    float x = 0;
    float y = 0;
    for( i=0; i<na; i++)
    {
      x += Math.cos(a[i].a)*a[i].l;
      y += Math.sin(a[i].a)*a[i].l;
      ax[i] = xpos + x*sx;
      ay[i] = ypos + y*sy;
    }

//    pushMatrix();
//    translate( width*0.8f-xpos,0);
    x *= sx;
    y *= sy;
    xp = xpos + x*rx - y*ry;
    yp = ypos + y*rx + x*ry;
//    pen.drawTo( xp,yp);
//    pen.render();

    // draw machine [
//    fill(0);
//    noStroke();
//    for( i=na-1; i>=0; i--)
//    {
//     noStroke();
//        fill(0);
//      if( i<na-1)
//        ellipse(ax[i],ay[i],20,20);
//      else
//        ellipse(xpos,ypos,24,24);
//      stroke(0);
//      strokeWeight(12);
//      if( i>0 )
//        line(ax[i-1],ay[i-1],ax[i],ay[i]);
//      else
//        line(xpos,ypos,ax[i],ay[i]);
//     }

//    for( i=na-1; i>=0; i--)
//    {
//      noStroke();
//      if( i<na-1)
//      {
//        fill(0);
//        ellipse(ax[i],ay[i],16,16);
//        fill(255);
//        ellipse(ax[i],ay[i],14,14);
//      }
//      stroke(0);
//      strokeWeight(8);
//      if( i>0 )
//        line(ax[i-1],ay[i-1],ax[i],ay[i]);
//      else
//        line(xpos,ypos,ax[i],ay[i]);
//      stroke(255);
//      strokeWeight(6);
//      if( i>0 )
//        line(ax[i-1],ay[i-1],ax[i],ay[i]);
//      else
//        line(xpos,ypos,ax[i],ay[i]);
//  
//      if(i==0)
//      {
//        noStroke();
//        fill(0);
//        ellipse(xpos,ypos,20,20);
//        fill(255);
//        ellipse(xpos,ypos,18,18);
//      }
//
//
//    }
    // ] machine
//    popMatrix();
    
//    xpos += dt*vx;
//    ypos += dt*vy;

    if( false && xpos >width )
    {
      xpos = 0;
      ypos += height*3/9;
    }
    
  }
  
}