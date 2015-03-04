public class Ship
{
  //Globals
  float w = 21, h = 14;
  float da = 7.5;
  float speed = 0;
  float friction = .80;
  PVector position = new PVector(255,255);
  PVector velocity = new PVector(0,0);
  PVector acceleration = PVector.fromAngle(0);
  boolean accel = false;
  public Bullet[] bullets = new Bullet[4];
  public boolean alive = true;
  public boolean inv = true;
  
  //Constructor
  public Ship()
  {
    for(int i = 0; i < 4; i++)
    {
      bullets[i] = new Bullet(this);
    }
  }
  
  //Activate acceleration
  public void thrust() 
  {
    accel = true;
    inv = false;
  }
  
  //Bullet fire method
  public void fire()
  {
     for (int i = 0; i < 3; i++)
     {
       if(bullets[i].active)
       {
          //Don't Fire
       }
       else
       {
         //bullets[i].active = true;
         //bullets[i].position = position;
         //bullets[i].acceleration = acceleration;
         //return;
         bullets[i].active = true;
         bullets[i].update(true,this,null);
         return;
       }
     }
  }
  
  //Rotate
  public void rotateLeft()
  {
    acceleration.rotate(-.40);
  }
  
  public void rotateRight()
  {
    acceleration.rotate(.40);
  }
  
  //Update the bullets
  public ArrayList<Asteroid> bUpdate(int pos, ArrayList<Asteroid> asteroids)
  {
    return bullets[pos].update(false,this,asteroids);
  }
  
  //Update the ship
  public void update()
  {
    speed *= friction; //apply firction
    
    if(accel) //Apply acceleration
    {
      velocity = PVector.mult(acceleration, speed);
      
      if(speed > 3) //Speed Cap
      {
        
      }
      else
      {
        speed += .8;
      }
    }
    
    
    position.add(velocity);
    
    //Check Bounds
     if (position.x > width)
     {
       position.x = -w;
     }
     else if (position.x < -w)
     {
       position.x = width;
     }
            
     if (position.y > height)
     {
       position.y = -h;
     }
     else if (position.y < -h)
     {
       position.y = height;
     }
  }
  
  //Draw
  public void draw()
  {
    stroke(255);   
    translate(position.x, position.y);
    //println("X: " + position.x);
    rotate(acceleration.heading());
    translate(-w/2, -h/2);    
    line(0, 0, w, h/2);
    line(w, h/2, 0, h);
    line(w * 0.1, h * 0.1, w*0.1, h - h*0.1);
  }
}
