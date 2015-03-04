public class Bullet
{
  //Globals
  boolean active = false;
  PVector pos;
  PVector vel;
  PVector accel = PVector.fromAngle(0);
  PShape shape;
  Ship ship;
  
  //Constructor
  public Bullet(Ship s)
  {
    ship = s;
    shape = createShape(createShape(ELLIPSE, 0, 0, 5, 5));
  }
  
  //Update
  public ArrayList<Asteroid> update(boolean first, Ship pShip, ArrayList<Asteroid> asteroids)
  {
    if(first) //If JUST shot
    {
      this.pos = pShip.position.get();
      this.accel = pShip.acceleration.get();
      return asteroids;
    }
    else if(active) //If is active and not the first update
    { 
      vel = PVector.mult(accel, 5); //Find velocity
      
      pos.add(vel); //Find Position
     
     //Check Bounds
      if (pos.x > width)
      {
        this.active = false;
      }
      else if (pos.x < 0)
      {
        this.active = false;
      }
            
      if (pos.y > height)
      {
        this.active = false;
      }
      else if (pos.y < 0)
      {
        this.active = false;
      }
      
      //Check collisions
      for(int i = 0; i < asteroids.size(); i++)
      {
        if(asteroids.get(i).big)
        {
          if(dist(asteroids.get(i).position.x + 50, asteroids.get(i).position.y + 45, this.pos.x, this.pos.y) <= 80)
          {
            this.active = false;
            return asteroids.get(i).explode(asteroids, i);
          }
        }
        else
        {
          if(dist(asteroids.get(i).position.x + 20, asteroids.get(i).position.y + 20, this.pos.x, this.pos.y) <= 18)
          {
            this.active = false;
            return asteroids.get(i).explode(asteroids, i);
          }
        }
      }
      
     this.draw(); //Draw the bullet
     return asteroids;
    }
    
     return asteroids;
  } 
  
  public void draw()
  {
    if (active)
    {
      stroke(255);   
      translate(pos.x, pos.y);
      rotate(accel.heading());
      shape(shape);
    }
  }
}
