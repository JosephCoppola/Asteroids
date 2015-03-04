public class Asteroid
{
  //Globals
  Asteroid little1;
  Asteroid little2;
  float vX = 0, vY = 0;
  PVector position;
  PVector velocity;
  PShape asteroidShape;
  boolean big;
  PShape[] shapes;
  
  //Constructor
  public Asteroid(PShape[] aShapes, boolean b)
  {
    shapes = aShapes;
    position = new PVector(random(0,500),random(0,500));
    velocity = new PVector(random(-2,2),random(-2,2));
    this.pickShape(aShapes);
    big = b;
  }
  
  //Assign the shape of the asteroid
  public void pickShape(PShape[] shapes)
  {
    int random = (int) random(0,2);
    
    asteroidShape = shapes[random];
  }
  
  //If blown up 
  public ArrayList<Asteroid> explode(ArrayList<Asteroid> asteroids, int i)
  {
    if(big) //If big create two tiny ones
    {
      little1 = new Asteroid(shapes, false);
      little2 = new Asteroid(shapes, false);
      little1.position = this.position;
      little2.position = new PVector(position.x,position.y);
      little1.velocity = this.velocity.get();
      vX = this.velocity.x;
      vY = this.velocity.y;
      little2.velocity = new PVector(-vX, -vY);
      asteroids.remove(i);
      asteroids.add(little1);
      asteroids.add(little2);
      return asteroids;
    }
    else //If a small asteroid
    {
      asteroids.remove(i);
      return asteroids;
    }
  }
 
  //Update
  public void update()
  {
    //Calculate postion
    position.add(velocity);
        
    //Check bounds
     if (position.x > width)
     {
       position.x = -100;
     }
     else if (position.x + 100 < 0)
     {
       position.x = width;
     }
            
     if (position.y > height)
     {
       position.y = -100;
     }
     else if (position.y + 100 < 0)
     {
       position.y = height;
     }
     
     println(asteroidShape.width);
     //hit.update(this);
  } 
  
  //Draw method
  public void draw()
  {
    if(big)
    {
      stroke(255);   
      translate(position.x, position.y);
      shape(asteroidShape);
      stroke(0);
    }
    else
    {
      stroke(255);   
      translate(position.x, position.y);
      scale(.25); //Make it smaller
      shape(asteroidShape);
      stroke(0);
    }
    //hit.draw();
  }
}
