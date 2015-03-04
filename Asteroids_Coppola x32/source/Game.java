import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class Game extends PApplet {

Ship s;
//Asteroid test;
ArrayList<Asteroid> asteroids;
PShape[] shapes = new PShape[3];
PShape holder;
boolean first = true;
PFont f = createFont("Arial",16,true);

public void setup()
{
  asteroids = new ArrayList<Asteroid>();
   
  size(500,500,P2D);
  background(0);
  s = new Ship();
  
  //Create all the shapes of the asteroids
  holder = createShape();
  holder.beginShape();
  holder.vertex(20,0);
  holder.vertex(80,0);
  holder.vertex(100,20);
  holder.vertex(100,50);
  holder.vertex(60,100);
  holder.vertex(40,100);
  holder.vertex(50,70);
  holder.vertex(25,90);
  holder.vertex(0,70);
  holder.vertex(20,40);
  holder.vertex(0,20);
  holder.endShape(CLOSE);
  shapes[0] = holder;
  holder = createShape();
  holder.beginShape();
  holder.vertex(20,0);
  holder.vertex(50,0);
  holder.vertex(100,20);
  holder.vertex(100,30);
  holder.vertex(50,50);
  holder.vertex(100,60);
  holder.vertex(75,100);
  holder.vertex(50,80);
  holder.vertex(20,100);
  holder.vertex(0,75);
  holder.vertex(10,50);
  holder.endShape(CLOSE);
  shapes[1] = holder;
  holder = createShape();
  holder.beginShape();
  holder.vertex(30,0);
  holder.vertex(50,20);
  holder.vertex(75,0);
  holder.vertex(100,20);
  holder.vertex(80,50);
  holder.vertex(100,70);
  holder.vertex(75,100);
  holder.vertex(30,100);
  holder.vertex(0,70);
  holder.vertex(0,20);
  holder.endShape(CLOSE);
  shapes[2] = holder;
  
  //Add all the asteroids
  Asteroid one = new Asteroid(shapes,true);
  
  Asteroid two = new Asteroid(shapes,true);
  Asteroid three = new Asteroid(shapes,true);
  Asteroid four = new Asteroid(shapes,true);
  Asteroid five = new Asteroid(shapes,true);
  Asteroid six = new Asteroid(shapes,true);
  asteroids.add(one);
  asteroids.add(two);
  asteroids.add(three);
  asteroids.add(four);
  asteroids.add(five);
  asteroids.add(six);
  
}
public void draw()
{
  background(0);
  //Draw and update the ship
  if(s.alive)
  {
    if(s.inv) //If the game just started
    {
      pushMatrix();
      textFont(f,19);       
      stroke(0);      
      fill(255);                        
      text("ONCE YOU MOVE YOU ARE VULNERABLE! HAVE FUN!",5,height/2);
      popMatrix();
    }
    pushMatrix();
    s.update();
    s.draw();
    popMatrix();
  }
  else //If killed
  {
    textFont(f,16);                 
    fill(255);                        
    text("DEAD! Press control to restart.",width/2 - 100,height/2);
  }
  
      if(asteroids.isEmpty()) //If all asteroids are killed
    {
      setup();
    }
  //Draw and update the asteroids
  for(int i = 0; i < asteroids.size();i++)
  { 
    pushMatrix();
    asteroids.get(i).update();
    asteroids.get(i).draw();
    popMatrix();
  }
  
  //Update the bullets
  for(int i = 0; i < 3; i++)
  {
    pushMatrix();
    asteroids = s.bUpdate(i,asteroids);
    popMatrix();
  }
  
  //Check if the Ship is colliding with asteroids
  if(!s.inv)
  {
    for(int i = 0; i < asteroids.size();i++)
    {
      if(asteroids.get(i).big) //Big Asteroid Collision
      {
        if(dist(asteroids.get(i).position.x + 50, asteroids.get(i).position.y + 45, s.position.x, s.position.y) <= 60)
        {
          s.alive = false;
        }
      }
      else //Little Asteroid Collision
      {
        if(dist(asteroids.get(i).position.x + 10, asteroids.get(i).position.y + 10, s.position.x, s.position.y) <= 20)
        {
          s.alive = false;
        }
      }
    }
  }
}

//Check the key presses
public void keyPressed() 
{
  if (key == CODED)
  {
    if (keyCode == UP)
    {
      s.thrust();
    }
    if (keyCode == RIGHT)
    {
      s.rotateRight(); 
    }
    if (keyCode == LEFT)
    {
      s.rotateLeft();
    }
  }
}

public void keyReleased()
{
  if(key == CODED)
  {
    if(keyCode == UP)
    {
      s.accel = false; 
    }
    if(keyCode == CONTROL) //Reset the game
    {
      setup();
    }
  }
  if(!s.inv && s.alive)
  {
    if(key == ' ')
    {
      s.fire();
    }
  }
}
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
      scale(.25f); //Make it smaller
      shape(asteroidShape);
      stroke(0);
    }
    //hit.draw();
  }
}
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

//Useless class

/*public class Hitarea
{
  float x = 0, y = 0;
  float diameter;
  PShape ellipse = createShape(0,0,10,10);
  
  public void update(Asteroid a)
  {
    x = a.position.x;
    y = a.position.y;
    diameter = a.asteroidShape.width + 3;
  }
  
  public void draw()
  {
    stroke(50,205,50);
    shape(ellipse, x, y, diameter, diameter);
  }
  
}
*/

public class Ship
{
  //Globals
  float w = 21, h = 14;
  float da = 7.5f;
  float speed = 0;
  float friction = .80f;
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
    acceleration.rotate(-.40f);
  }
  
  public void rotateRight()
  {
    acceleration.rotate(.40f);
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
        speed += .8f;
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
    line(w * 0.1f, h * 0.1f, w*0.1f, h - h*0.1f);
  }
}
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "Game" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
