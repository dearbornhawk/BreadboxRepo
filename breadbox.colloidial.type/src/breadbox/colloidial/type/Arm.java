package breadbox.colloidial.type;

public class Arm
{
  float l;
  float a;
  float w;
  
  Arm( float li, float ai, float wi )
  {
    l = li;
    a = ai;
    w = wi;
  }
  
  public void update( float dt )
  {
    a += w*dt;
    if( a>Math.PI ) a -= 2*Math.PI;
    if( a<-Math.PI ) a+= 2*Math.PI;
  }
}