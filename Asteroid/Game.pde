Ship s;
//Asteroid test;
ArrayList<Asteroid> asteroids;
PShape[] shapes = new PShape[3];
PShape holder;
boolean first = true;
PFont f = createFont("Arial",16,true);

void setup()
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
void draw()
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
void keyPressed() 
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

void keyReleased()
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
