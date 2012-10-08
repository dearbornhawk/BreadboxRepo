package breadbox.cellular.monsters;

import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PVector;

class Monster
{
    public PVector pos;
    public PVector vel;
    public PVector acc;
     
    public float dancePeriod;
    public float danceAmpX;
    public float danceAmpY;
    public float nextPeriod;
    public float nextAmpX;
    public float nextAmpY;
     
    float neighborDancePeriod = 0;
     
    public int moodColor;
     
    public ArrayList nodes;
     
    float bodyOffsetX = 0;//random(-0.2 * width / col, 0.2 * width / col);
    float bodyOffsetY = 0;//random(-0.2 * height / row, 0.2 * height / row);
    float headHeight;
    float headWidth;
    float bodyWidth;
    float bodyLength;  
    float feetSpacing;
    float bodyDent = 0;//random(0,5);
    float bodyTightness;
    float eyeDistance;
    float eyeRadius;
    float eyeShift;
    float pupilOffset;
    float pupilRadius;
    int eyeType;
     
     
    PVector vecHead;
    PVector vecLeftEar;
    PVector vecRightEar;
    PVector vecLeftFoot;
    PVector vecRightFoot;
    PVector vecBottom;
    PVector vecEyeCenter;
     
     
    PVector base_vecHead;
    PVector base_vecLeftEar;
    PVector base_vecRightEar;
    PVector base_vecLeftFoot;
    PVector base_vecRightFoot;
    PVector base_vecBottom;
    PVector base_vecEyeCenter;
     
    float base_danceAmpX;
    float base_danceAmpY;
     
    boolean hovering = false;
    
    Monsters pApplet;
     
    Monster(PVector initPos, Monsters pApplet)
    {
    	this.pApplet = pApplet;
    	
         headHeight = pApplet.random(5,25);//random(5,15);
         headWidth = pApplet.random(30,pApplet.width * 1.2f / pApplet.col);
         bodyWidth = headWidth * pApplet.random(0.8f,1.4f);//random(30,width * 1.5 / col);
         bodyLength = pApplet.random(40,60);//random(20,height / row - 15 - headHeight);  
         feetSpacing = pApplet.random(0,1.5f);
         bodyDent = 0;//random(0,5);
         bodyTightness = pApplet.random(-2.5f,-1);
         eyeDistance = pApplet.random(0.3f,0.8f) * headWidth;
         eyeRadius = pApplet.random(6,eyeDistance / 2);
         eyeShift = pApplet.random(-2, headHeight * 0.6f);
         pupilOffset = pApplet.random(-eyeRadius * 0.6f, eyeRadius * 0.6f);
         pupilRadius = pApplet.random(1, eyeRadius * 0.5f);
    	
        this.pos = initPos;
        this.nodes = new ArrayList();
         
        dancePeriod = 30;//int(random(10,60)) * 1.5;
        nextPeriod = dancePeriod;
        danceAmpX = pApplet.random(3,8);//int(random(1,20));
        nextAmpX = danceAmpX;
        danceAmpY = pApplet.random(16,20);//int(random(0,20));
        nextAmpY = danceAmpY;
                 
        vecHead = new PVector(0, - headHeight - bodyLength);       
        vecLeftEar = new PVector(-0.5f * headWidth,  - bodyLength);
        vecRightEar = new PVector(0.5f * headWidth,  - bodyLength);     
        vecLeftFoot = new PVector(-0.5f * bodyWidth, 0);
        vecRightFoot = new PVector(0.5f * bodyWidth, 0);    
        vecBottom = new PVector(0, 0); 
        vecEyeCenter = new PVector(0,0);
         
        base_vecHead = vecHead.get();  
        base_vecLeftEar = vecLeftEar.get();
        base_vecRightEar = vecRightEar.get();
        base_vecLeftFoot = vecLeftFoot.get();
        base_vecRightFoot = vecRightFoot.get();
        base_vecBottom = vecBottom.get();
        base_vecEyeCenter = vecEyeCenter.get();
         
        base_danceAmpX = danceAmpX;
        base_danceAmpY = danceAmpY;
                 
        moodColor = 100;//int(random(50,150));
        eyeType = (int)pApplet.random(0,5);
    }
     
    void printDNA()
    {
        System.out.println("--- Monster DNA ---");
        System.out.println("Body Offset:  " + pApplet.nf(bodyOffsetX,2,1) + ", " + pApplet.nf(bodyOffsetY,2,1));
        System.out.println("Head Height:  " + pApplet.nf(headHeight,2,1));
        System.out.println("Head Width:   " + pApplet.nf(headWidth,2,1));
        System.out.println("Body Height:  " + pApplet.nf(bodyLength,2,1));
        System.out.println("Body Width:   " + pApplet.nf(bodyWidth,2,1));
        System.out.println("Feet Spacing: " + pApplet.nf(feetSpacing,2,1));
        System.out.println("Feet Length:  " + pApplet.nf(bodyDent,2,1));
        System.out.println("Softness:     " + pApplet.nf(bodyTightness,2,1));
        System.out.println("Eye Type:     " + eyeType);
        System.out.println("Eye Distance: " + pApplet.nf(eyeDistance,2,1));
        System.out.println("Eye Radius:   " + pApplet.nf(eyeRadius,2,1));
        System.out.println("Eye Shift:    " + pApplet.nf(eyeShift,2,1));
        System.out.println("Pupil Offset: " + pApplet.nf(pupilOffset,2,1));
        System.out.println("Pupil Radius: " + pApplet.nf(pupilRadius,2,1));
        System.out.println("--- ----------- ---");
         
    }
     
    void rebuildBody()
    {
        vecHead = new PVector(0, - headHeight - bodyLength);       
        vecLeftEar = new PVector(-0.5f * headWidth,  - bodyLength);
        vecRightEar = new PVector(0.5f * headWidth,  - bodyLength);     
        vecLeftFoot = new PVector(-0.5f * bodyWidth, 0);
        vecRightFoot = new PVector(0.5f * bodyWidth, 0);    
        vecBottom = new PVector(0, 0); 
        vecEyeCenter = new PVector(0,0);
         
        base_vecHead = vecHead.get();  
        base_vecLeftEar = vecLeftEar.get();
        base_vecRightEar = vecRightEar.get();
        base_vecLeftFoot = vecLeftFoot.get();
        base_vecRightFoot = vecRightFoot.get();
        base_vecBottom = vecBottom.get();
        base_vecEyeCenter = vecEyeCenter.get();
    }
 
     
    void update()
    {
        int step = pApplet.frameCount % (int)dancePeriod;
         
        float position = (float)step / dancePeriod;
         
        if (Math.abs(position - 0.5) < 0.05) // && int(nextPeriod) != int(dancePeriod)
        {
            dancePeriod = nextPeriod;
            danceAmpX = nextAmpX;
            danceAmpY = nextAmpY;
        }
         
        position = pApplet.map(position,0,1,0,pApplet.TWO_PI);
         
        PVector danceOffset = new PVector((float)Math.sin(position) * danceAmpX, (float)(-1 * Math.abs(Math.sin(position) * danceAmpY)));
         
        vecHead = PVector.add(base_vecHead, danceOffset);
        vecLeftEar = PVector.add(base_vecLeftEar, danceOffset);
        vecRightEar = PVector.add(base_vecRightEar, danceOffset);  
        //vecLeftFoot = PVector.add(base_vecLeftFoot, danceOffset);
        //vecRightFoot = PVector.add(base_vecRightFoot, danceOffset);
        //vecBottom = PVector.add(base_vecBottom, danceOffset);
        vecEyeCenter = PVector.add(base_vecEyeCenter, PVector.mult(danceOffset,1.4f));
         
        //println(position + "/" + dancePeriod + ":" + danceOffset.x + "," + danceOffset.y);
    }
     
    void show()
    {
        pApplet.pushMatrix();
        pApplet.translate(pos.x + bodyOffsetX, pos.y + bodyOffsetY);
         
         
        //body
        pApplet.stroke(40);
         
        pApplet.colorMode(pApplet.HSB,360,100,100,250);
        if (hovering)
        {
        	pApplet.fill(moodColor,90,90,190);
        }
        else
        {
        	pApplet.fill(moodColor,90,90,200);
        }
        pApplet.colorMode(pApplet.RGB,255,255,255,250);
         
        pApplet.curveTightness(bodyTightness);
         
        pApplet.beginShape();
        pApplet.curveVertex(vecBottom.x,        vecBottom.y - bodyDent);
        pApplet.curveVertex(vecBottom.x - feetSpacing,      vecBottom.y);
        pApplet.curveVertex(vecLeftFoot.x,  vecLeftFoot.y);
        pApplet.curveVertex(vecLeftEar.x,       vecLeftEar.y);
        pApplet.curveVertex(vecHead.x,          vecHead.y);
        pApplet.curveVertex(vecRightEar.x,  vecRightEar.y);
        pApplet.curveVertex(vecRightFoot.x, vecRightFoot.y);
        pApplet.curveVertex(vecBottom.x + feetSpacing,      vecBottom.y);
        pApplet.curveVertex(vecBottom.x,        vecBottom.y - bodyDent);
        pApplet.endShape(pApplet.CLOSE);
         
        if (pApplet.debugging)
        {
            if (dancePeriod > neighborDancePeriod) //slower
            {   pApplet.fill(255,0,0);}
            else
            {
            	pApplet.fill(0,0,255);
            }
            //text(round(this.dancePeriod) + "/" + round(this.neighborDancePeriod), vecBottom.x, vecBottom.y - 40);
        }
         
        //draw eye
        pApplet.translate(vecEyeCenter.x, vecEyeCenter.y);
         
        pApplet.fill(255);
        pApplet.ellipse(-0.5f * eyeDistance, - bodyLength - eyeShift, eyeRadius, eyeRadius);
        pApplet.ellipse(0.5f * eyeDistance, - bodyLength - eyeShift, eyeRadius, eyeRadius);
         
        pApplet.stroke(90);
        pApplet.noFill();
         
        switch (eyeType)
        {
            case 1:
            	pApplet.line(-0.5f * eyeDistance - eyeRadius * 0.5f, - bodyLength - eyeShift + 2,  -0.5f * eyeDistance + eyeRadius * 0.5f, - bodyLength - eyeShift - 2);
            	pApplet.line(0.5f * eyeDistance - eyeRadius * 0.5f, - bodyLength - eyeShift - 2,  0.5f * eyeDistance + eyeRadius * 0.5f, - bodyLength - eyeShift + 2);
             
                break;
            case 2:
            	pApplet.line(-0.5f * eyeDistance - eyeRadius * 0.5f, - bodyLength - eyeShift - 2,  -0.5f * eyeDistance + eyeRadius * 0.5f, - bodyLength - eyeShift + 2);
            	pApplet.line(0.5f * eyeDistance - eyeRadius * 0.5f, - bodyLength - eyeShift + 2,  0.5f * eyeDistance + eyeRadius * 0.5f, - bodyLength - eyeShift - 2);
                break;
            case 3:
            	pApplet.fill(60);
            	pApplet.noStroke();
            	pApplet.ellipse(-0.5f * eyeDistance + pupilOffset, - bodyLength - eyeShift, pupilRadius, pupilRadius);
            	pApplet.ellipse(0.5f * eyeDistance + pupilOffset, - bodyLength - eyeShift, pupilRadius, pupilRadius);
                break;
            case 0:
            default:
            	pApplet.line(-0.5f * eyeDistance - eyeRadius * 0.6f, - bodyLength - eyeShift,  -0.5f * eyeDistance + eyeRadius * 0.6f, - bodyLength - eyeShift);
            	pApplet.line(0.5f * eyeDistance - eyeRadius * 0.6f, - bodyLength - eyeShift,  0.5f * eyeDistance + eyeRadius * 0.6f, - bodyLength - eyeShift);
                break;
        }
         
         
         
        pApplet.popMatrix();
    }
     
    public void mousePressed(){
    
        if (Math.abs(pApplet.mouseX - this.pos.x) <= pApplet.width / pApplet.col && ( pApplet.mouseY <= this.pos.y + 10 && pApplet.mouseY >= this.pos.y + 10 - pApplet.height / pApplet.row) )
        {
            /*
            hovering = true;
            nextPeriod += 5;
            danceAmpX = min(base_danceAmpX * 1.2,30);
            danceAmpY = min(base_danceAmpY * 1.2,45);
            */
            printDNA();
        }
        else
        {
            /*
            hovering = false;
            danceAmpX = base_danceAmpX;
            danceAmpY = base_danceAmpY;
            */
        }
    }
 
}