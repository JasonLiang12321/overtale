import java.awt.*;
import javax.swing.*;
import javax.swing.Timer;
import java.awt.event.*;
import javax.imageio.*;
import javax.sound.sampled.*;
import java.io.*;
import java.util.ArrayList;
import java.awt.image.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;



//initalizes project 

public class FinalProject{
    public static int width = 1200;
    public static int height = 760;
    public static void main(String[] args) {
       
    JFrame frame = new JFrame();
    frame.setTitle("FinalProject-Jason");
    
    frame.setResizable(false); 
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    FinalProjectGame game = new FinalProjectGame();
    frame.setBounds(0,0,width,height);
    frame.add(game);
    frame.setVisible(true);
    frame.setCursor(Cursor.getDefaultCursor());
    BufferedImage c = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
    Cursor nocursor = Toolkit.getDefaultToolkit().createCustomCursor(
    c, new Point(0,0),"nocursor");
    frame.getContentPane().setCursor(nocursor);
    frame.setFont(new Font("Comic Sans MS", Font.BOLD,75));
    

        
    
 

       
    }
}
//class of enemies
class enemies{
    int sizex = 30;
    int sizey = 30;
    int x;
    int y;
    int suddenDropTimer = 0;
    String dir;
    String type;
    //type - > normal, healing, instantd, normalsuddendrop, instantdsuddendrop, wall
}
//the player class
class player{
     int height = 730;
     int width = 1200;
     int x = width/2;
     int y = width/2;
     int size = 30;
    
    
    //checks if player is in collision
    String isCollideEnemy(ArrayList<enemies> enemiespos){
        
        for (enemies p : enemiespos){
            if (Math.abs(p.x-this.x) < (this.x < p.x ? this.size : p.sizex) && Math.abs(p.y-this.y) < (this.y< p.y ? this.size : p.sizey)){ 
                enemiespos.remove(p);
               if(p.type.contains( "healing")) {
                    return "heal";
                }else if (p.type.contains("instantd")){
                    return "instantdeath";
                }else{
                    return "yes";
                }

            }
           
            }
        return "no";
        }
        

        
    //prevents player from going out of bounds
     void setBorders(){
        if (x >= width-size){
            x = width-size;

            
        }
        
        
        if (y >= height-size){
            y = height-size;

        
        }
           
        
        
       
    }
}



 //main body
class FinalProjectGame extends JPanel implements MouseListener, ActionListener, KeyListener, MouseMotionListener{
    //variables
    Timer t; 
    static int height = 730;
    static int width = 1200;
    static ArrayList<enemies> enemiepos = new ArrayList<enemies>();
    static double wave = 0;
    static ArrayList<Integer> spawnSideEnemyY = new ArrayList<Integer>();
    static ArrayList<Integer> spawnNormalEnemyX = new ArrayList<Integer>();
    static String level = "main";
    static Image background;
    static String overtaleFont = "Wlmsl";
    static int mousePressedX;
    static int mousePressedY;
    static int currentscore;
    static int universalcounter = 0;
    static int L1tutorialstage = 0;
    static int mainMenuSFXCountL1 = 0;
    static int mainMenuSFXCountL2 = 0;
    static int mainMenuSFXCountL3 = 0;
    static int mainMenuSFXCountEndless = 0;
    static int L1totalenemyspawnednormal = 0;
    static int L1totalenemyspawnedheal = 0;
    static int L1totalenemyspawnedinstantd = 0;
    static double L2Progression = 0;
    static int L2Wave4sprayCount = 1170;
    static boolean L2Wave4Boolean = true;
    static boolean L2Wave3Boolean = true;
    static double L3Progression = 0;
    static boolean L3Boolean = true;
    static Clip creditsMusic = null;
    static Image credgif;
    static Image cred1;
    static boolean creditsrolling = false;
    


    
    Music SFX = new Music();
    String soundFile;


  

    //initializes player
    player me = new player();
    //method to spawn enemy
    public static void spawnenemy(String direc, String type){
    
        enemies p = new enemies();
        p.dir = direc;
        p.type = type;
        
      if (p.dir == "normal"){
            int randomdecider = (int) (Math.random()*width);
            p.x = randomdecider- randomdecider%30;
            p.y = 0;
            //below is to remove duplicates/overlaps
            if (spawnNormalEnemyX.size() < 20){
                spawnNormalEnemyX.add(p.x);
            }else{
                while (spawnNormalEnemyX.contains(p.x)){
                    randomdecider = (int) (Math.random()*width);
                    p.x = randomdecider- randomdecider%30;
                }
                spawnNormalEnemyX.remove(0);
                spawnNormalEnemyX.add(p.x);
            }
            //removed duplicates/overlaps
          
  
      }else if (p.dir == "side"){
        int randomdecider = (int) (Math.random()*height);
        p.y = randomdecider- randomdecider%30;
        p.x = width;

        //removes duplicates/overlaps below
        if (spawnSideEnemyY.size() < 20){
            spawnSideEnemyY.add(p.y);
        }else{
            while (spawnSideEnemyY.contains(p.y)){
                randomdecider = (int) (Math.random()*width);
                p.y = randomdecider- randomdecider%30;
            }
            spawnSideEnemyY.remove(0);
            spawnSideEnemyY.add(p.y);
        }
      }
       
        enemiepos.add(p);
        
        }
        //the below method is a copy of the above method, made it more customizable
        //Could just modify the above method to be more customizable but already too late
        //into the project and would have to reset parameters for too many places of my code
        public static void spawnenemyCustomizable(String direc, String type, int x, int y){
    
            enemies p = new enemies();
            p.dir = direc;
            p.type = type;
            p.x = x;
            p.y = y;
           
            enemiepos.add(p);
            
            }
       
    
    //adds mouselistener, setup()
    public FinalProjectGame(){
        addMouseListener(this);
        addMouseMotionListener(this);
       
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        soundFile = "pop.wav";
        SFX.setFile(soundFile);
        

    
        

        


    
   

       
        t = new Timer(5,this); 
        t.start();
        try{
            background = ImageIO.read(new File("OvertaleTitleScreenv4.png"));
        }catch (IOException e){
            System.out.println(e);
        }
    }
    //main paint method
    public void paint(Graphics g){
        
        g.setFont(new Font(overtaleFont, Font.BOLD,30));
        if (creditsrolling){
            credits(g);
        }else{
        //level endless
        if (level == "endless"){ 
        g.setColor(Color.BLACK);
        g.fillRect(0,0,1200,750);
        g.setColor(new Color(61, 63, 125));
        g.setFont(new Font(overtaleFont,Font.BOLD,60));
         g.drawString("Size: " + me.size, 10,60);
        me.setBorders();
        g.setColor(Color.ORANGE);
        g.fillRect(me.x,me.y,me.size,me.size);
        wave += 0.005;
        int sizelimit = 20 + (int)(wave/10);
        if(enemiepos.size() < sizelimit && wave <= 5){
                double typedecider = Math.random();
                String tospawntype = "";
                if (typedecider > 0.98){
                    tospawntype = "healing";
                }else if (typedecider < 0.1){
                    tospawntype = "instantd";
                }else{
                    tospawntype = "normal";
                }
                spawnenemy("normal",tospawntype);
        }else if (enemiepos.size() < sizelimit && wave <= 10){
                double typedecider = Math.random();
                String tospawntype = "";
                if (typedecider > 0.98){
                    tospawntype = "healing";
                }else if (typedecider < 0.1){
                    tospawntype = "instantd";
                }else{
                    tospawntype = "normal";
                }
       
                
                spawnenemy("side",tospawntype);
        }else if(enemiepos.size() < sizelimit){
                int randomdecider = (int) (Math.random()*2);
                double typedecider = Math.random();
                String tospawntype = "";
                if (typedecider > 0.98){
                    tospawntype = "healing";
                }else if (typedecider < 0.1){
                    tospawntype = "instantd";
                }else{
                    tospawntype = "normal";
                }

                if (randomdecider == 0){
                    spawnenemy("side",tospawntype);
                }else{
                    spawnenemy("normal",tospawntype);
                }
                
        
            }
            
            ArrayList <enemies> toremove = new ArrayList<enemies>();
            
            
            if (me.size <= 0){
                level = "DeathScreen";
            }
            for (enemies p: enemiepos){
                if (p.y > height || p.y < 0 || p.x < 0 || p.x > width){
                    
                    toremove.add(p);
                    
                    
             
                    continue;
                }
                if ((p.type == "normal")){
        
                    g.setColor(Color.RED);

                }else if (p.type == "healing"){
                    g.setColor(Color.GREEN);
                }else if (p.type == "instantd"){
                    g.setColor(new Color((int)(Math.random()*256),(int)(Math.random()*256),(int)(Math.random()*256)));
                }
                g.fillRect(p.x,p.y,p.sizex,p.sizey);
                if (p.dir == "normal" ){
                    p.y += 5;
                }else{
                    p.x -= 5;
                }
                
               
           
            }
            for(enemies p: toremove){
                enemiepos.remove(p);

            }
            String collideE = me.isCollideEnemy(enemiepos);
            if (collideE == "yes"){
               
                me.size--;
                
            }else if(collideE == "heal" ){
                me.size += 3;
            }else if (collideE == "instantdeath"){
                me.size = 0;
            }
            g.setColor(Color.PINK);
            
            g.drawString(""+(int)wave, width-140,50);
            
            
     
        //level menu/main
        }else if (level == "main"){

            
            
            startScreen(g);
            me.setBorders();
            g.setFont(new Font(overtaleFont,Font.BOLD, 150));
            
            g.drawString("Overtale",310,400);
            
            g.setFont(new Font(overtaleFont,Font.BOLD, 100));
            if (me.x >= 70 && me.x <= 180 && me.y <= 650 && me.y >= 565){
                g.setFont(new Font(overtaleFont,Font.BOLD, 125));
                g.setColor(Color.GREEN);
                if(mainMenuSFXCountL1 == 0){
                    SFX.c.setFramePosition(5000);
                    SFX.play();

                }
              
                mainMenuSFXCountL1++;
            }else{
                g.setColor(Color.WHITE);
                g.setFont(new Font(overtaleFont,Font.BOLD, 100));
                
               
                mainMenuSFXCountL1 = 0;
                
               
                
                
            }
            
            g.drawString("L1",100,650);
            if (me.x >= 320 && me.x <= 430 && me.y <= 650 && me.y >= 565){
                g.setColor(Color.GREEN);
                g.setFont(new Font(overtaleFont,Font.BOLD, 125));
                if(mainMenuSFXCountL2 == 0){
                    SFX.c.setFramePosition(5000);
                    SFX.play();

                }
                mainMenuSFXCountL2++;
            }else{
                g.setColor(Color.WHITE);
                g.setFont(new Font(overtaleFont,Font.BOLD, 100));
                mainMenuSFXCountL2 = 0;
                
                
            }

            g.drawString("L2",310,650);
            if (me.x >= 480 && me.x <= 670 && me.y <= 630 && me.y >= 530){
                g.setColor(Color.GREEN);
                g.setFont(new Font(overtaleFont,Font.BOLD, 125));
                if(mainMenuSFXCountL3 == 0){
                    SFX.c.setFramePosition(5000);
                    SFX.play();

                }
                mainMenuSFXCountL3++;

            }else{
                g.setColor(Color.WHITE);
                g.setFont(new Font(overtaleFont,Font.BOLD, 100));
                mainMenuSFXCountL3 = 0;
               
                
            }
            g.drawString("L3", 550,650);
            if (me.x >= 720 && me.x <= 1050 && me.y <= 650 && me.y >= 565){
                g.setColor(Color.GREEN);
                g.setFont(new Font(overtaleFont,Font.BOLD, 125));
                if(mainMenuSFXCountEndless == 0){
                    SFX.c.setFramePosition(5000);
                    SFX.play();
                    
                }
                mainMenuSFXCountEndless++;
            }else{
                g.setColor(Color.WHITE);
                g.setFont(new Font(overtaleFont,Font.BOLD, 100));
                mainMenuSFXCountEndless = 0;
               
            }
            g.drawString("endless",750,650);

            
            g.setColor(new Color(0,163,108));
            g.fillRect(me.x,me.y,me.size,me.size);
            if (mousePressedX >= 70 && mousePressedX <= 180 && mousePressedY <= 650 && mousePressedY >= 565){
                level = "L1";
                SFX.setFile("Camp.wav");
                SFX.c.setFramePosition(70000);
                SFX.c.loop(1000);
            }
            if (mousePressedX >= 320 && mousePressedX <= 430 && mousePressedY <= 650 && mousePressedY >= 565){
                level = "L2";
                SFX.setFile("RushE.wav");
                SFX.c.loop(1000000);
            }
            if (mousePressedX >= 520 && mousePressedX <= 670 && mousePressedY <= 650 && mousePressedY >= 530){
                level = "L3";
                SFX.setFile("Moonlight.wav");
                SFX.c.loop(1000);
            }
            if (mousePressedX >= 720 && mousePressedX <= 1050 && mousePressedY <= 650 && mousePressedY >= 565){
                level = "endless";
                SFX.setFile("Mozart.wav");
                SFX.c.loop(1000);
              
            }
            
            
            
        //level death
        }else if (level == "DeathScreen"){
            SFX.c.stop();
            g.setColor(Color.BLACK);
            g.fillRect(0,0,1200,750);
            g.setColor(Color.RED);
            g.setFont(new Font(overtaleFont,Font.BOLD, 150));
            g.drawString("You Suck", 300,400);
            g.setColor(new Color(123,234,132));
            g.fillRect(350,500,500,100);
            g.setColor(Color.RED);
            g.setFont(new Font(overtaleFont,Font.BOLD, 50));
            g.drawString("Press Q to restart", 390,560);
            g.drawString("Score: " +Math.max(Math.max((int)wave,(int)L2Progression),(int)L3Progression),100,100);
            me.size = 30;
                                                                                                            
        //level 1/tutorial
        }else if (level == "L1"){
            if (L1tutorialstage == 0){
            me.setBorders();
            g.setColor(Color.BLACK);
            g.fillRect(0,0,1200,750);
            g.setFont(new Font(overtaleFont,Font.BOLD, 100));
            g.setColor(new Color(0,163,108));
            
            g.drawString("Welcome to the tutorial",50,350);
            g.setFont(new Font(overtaleFont,Font.BOLD, 75));
            g.setColor(new Color(0,200,108));
            if((universalcounter %200 <= 150)){
                g.drawString("click anywhere to start",200,450);
            }
            g.setColor(Color.ORANGE);
            g.fillRect(me.x,me.y,me.size,me.size);
        }else if (L1tutorialstage == 1){
            g.setColor(Color.BLACK);
            g.fillRect(0,0,1200,750);
            g.setColor(new Color(61, 63, 125));
            g.setFont(new Font(overtaleFont,Font.BOLD,60));
            g.drawString("Red enemies decreases your size by 1",70,350);
            g.drawString("Size: " + me.size, 10,60);
            g.setColor(Color.ORANGE);
            g.fillRect(me.x,me.y,me.size,me.size);
            me.setBorders();
            if (me.isCollideEnemy(enemiepos) == "yes"){
                me.size--;
                
            }
            if (enemiepos.size()<20 && L1totalenemyspawnednormal < 200){
            spawnenemy("normal","normal");
            L1totalenemyspawnednormal++;

            }
            ArrayList <enemies> toremove = new ArrayList<enemies>();
            //spawn enemies - start
            for (enemies p: enemiepos){
                if (p.y > height || p.y < 0 || p.x < 0 || p.x > width){
                    
                   toremove.add(p);
                    
                    
             
                    continue;
                }
                if ((p.type == "normal")){
        
                    g.setColor(Color.RED);

                }else if (p.type == "healing"){
                    g.setColor(Color.GREEN);
                }else if (p.type == "instantd"){
                    g.setColor(new Color((int)(Math.random()*256),(int)(Math.random()*256),(int)(Math.random()*256)));
                }
                g.fillRect(p.x,p.y,p.sizex,p.sizey);
                if (p.dir == "normal" ){
                    p.y += 5;
                }else{
                    p.x -= 5;
                }
                
               
           
            }
            for(enemies p : toremove){
                enemiepos.remove(p);
            }
            if (L1totalenemyspawnednormal >= 200){
                L1tutorialstage = 2;
            }
            if(me.size <= 0){
                level = "DeathScreen";
                L1totalenemyspawnednormal = 0;
            }
            //end
            
            

   
        //level 2
        }else if (L1tutorialstage == 2){
            me.setBorders();
            g.setColor(Color.BLACK);
            g.fillRect(0,0,1200,750);
            g.setColor(new Color(61, 63, 125));
            g.setFont(new Font(overtaleFont,Font.BOLD,60));
            if (L1totalenemyspawnedheal < 90){
            g.drawString("Reaching a size of 0 results in DEATH", 70 ,350);
            g.drawString("Green blocks heals you by 3", 150,450);
            }else if (L1totalenemyspawnedheal >= 90 && L1totalenemyspawnedinstantd < 100){
                g.setColor(new Color(61, 63, 125));
                g.drawString("Seizure blocks will instantly kill you",100,350);
            }else{
                g.setColor(new Color(61, 63, 125));
                g.drawString("You have completed the tutorial...",70,350);
                g.drawString("press q to return to the main menu...", 150,450);
            }
            g.drawString("Size: " + me.size, 10,60);
            g.setColor(Color.ORANGE);
            g.fillRect(me.x,me.y,me.size,me.size);
            me.setBorders();
            String collideE = me.isCollideEnemy(enemiepos);
            if (collideE == "heal"){
                me.size+=3;
                
            }else if (collideE == "instantdeath"){
                me.size = 0;
                
            }
            if (enemiepos.size()<20 && L1totalenemyspawnedheal < 100){
            spawnenemy("normal","healing");
            L1totalenemyspawnedheal++;

            }else if (enemiepos.size() < 15 && L1totalenemyspawnedheal >= 100 && L1totalenemyspawnedinstantd < 100){
                spawnenemy("normal","instantd");
                L1totalenemyspawnedinstantd++;
            }
            ArrayList <enemies> toremove = new ArrayList<enemies>();
            //spawn enemies - start
            for (enemies p: enemiepos){
                if (p.y > height || p.y < 0 || p.x < 0 || p.x > width){
                    
                   toremove.add(p);
                    
                    
             
                    continue;
                }
                if ((p.type == "normal")){
        
                    g.setColor(Color.RED);

                }else if (p.type == "healing"){
                    g.setColor(Color.GREEN);
                }else if (p.type == "instantd"){
                    g.setColor(new Color((int)(Math.random()*256),(int)(Math.random()*256),(int)(Math.random()*256)));
                }
                g.fillRect(p.x,p.y,p.sizex,p.sizey);
                if (p.dir == "normal" ){
                    p.y += 5;
                }else{
                    p.x -= 5;
                }
                
                
        }
        for(enemies p : toremove){
            enemiepos.remove(p);
        }
        if(me.size <= 0){
            level = "DeathScreen";
            L1totalenemyspawnednormal = 0;
            L1totalenemyspawnedheal = 0;
            L1totalenemyspawnedinstantd = 0;
        }
        
          
        
            
        }
    }else if (level == "L2"){
        //level visual default
        g.setColor(Color.BLACK);
        L2Progression += 0.005;
        g.fillRect(0,0,1200,760);
        g.setColor(new Color(224,209,145)); //Color Jazzamoon
        g.setFont(new Font(overtaleFont,Font.BOLD,60));
        g.drawString("Size: " + me.size, 10,60);
        g.setColor(Color.PINK);
        g.setFont(new Font(overtaleFont,Font.BOLD,35));
        if (L2Progression >= 101){
            level = "L2WinScreen";
        }
        g.drawString(""+(int)L2Progression + "/100", width-140,50);
        me.setBorders();
        
        
        ArrayList <enemies> toremove = new ArrayList<enemies>();
        //the main "brain" for the collision, type detection...etc
        for (enemies p: enemiepos){
            if (p.y > height || p.y < 0 || p.x < 0 || p.x > width){
                
               toremove.add(p);
                
                
         
                continue;
            }
            if ((p.type == "normal")){
    
                g.setColor(Color.RED);

            }else if (p.type == "healing"){
                g.setColor(Color.GREEN);
            }else if (p.type == "instantd"){
                g.setColor(new Color((int)(Math.random()*256),(int)(Math.random()*256),(int)(Math.random()*256)));
            }
            g.fillRect(p.x,p.y,p.sizex,p.sizey);
            if (p.dir == "normal" ){
                p.y += 5;
            }else{
                p.x -= 5;
            }
            
           
       
        }
        for(enemies p : toremove){
            enemiepos.remove(p);
        }
       
        //end
        //player default settings
        g.setColor(Color.ORANGE);
        g.fillRect(me.x,me.y,me.size,me.size);
        me.setBorders();
        String CollideE = me.isCollideEnemy(enemiepos);
        if (CollideE == "yes"){
            me.size--;
        }else if (CollideE == "instantdeath"){
            me.size = 0;
        }else if (CollideE == "heal"){
            me.size+=3;
        }
        if (me.size == 0){
            level = "DeathScreen";
        }

        //customization of the level
        if (L2Progression <= 5 && enemiepos.size() < 1){
            spawnenemyCustomizable("normal","normal",600,0);
        }else if (L2Progression <= 10 && L2Progression > 5 && enemiepos.size() < 10){
            spawnenemyCustomizable("normal","normal",600,0);
        }else if (L2Progression > 10 && L2Progression <= 20 && enemiepos.size() < 200){
            for (int i = 200; i < 1400; i+=200){
                 spawnenemyCustomizable("normal","normal",i,0);
            }
                
            for (int i = me.x; i < 1400; i+=100){
                spawnenemyCustomizable("normal","normal",i,0);
            }
        }else if (L2Progression > 20 && L2Progression <= 50){
            spawnenemyCustomizable("normal","normal",L2Wave4sprayCount,0);
            if (L2Wave4sprayCount <= 1170 && L2Wave4sprayCount > 30 && L2Wave4Boolean){
              
                L2Wave4sprayCount-=5;
                if (L2Wave4sprayCount == 30){
                    L2Wave4Boolean = false;
                }
            }else{
                
                L2Wave4sprayCount+=5;
                if (L2Wave4sprayCount == 1140){
                    L2Wave4Boolean = true;
                }
            }
            
            
        }
        if (L2Progression > 30 && L2Progression <= 50 && enemiepos.size() < 148+20){
            spawnenemy("normal","normal");
       
        }
        
        
        int instantspawned = 0;
        int normalsidespawned = 0;
        for (enemies p : enemiepos){
            if (p.type == "instantd"){
                instantspawned++;
            }else if(p.type == "normal"){
                normalsidespawned++;
            }
        }
        if (L2Progression > 50 && L2Progression <= 75 && instantspawned<10){
            
           spawnenemy("side","instantd");

        }
        if(L2Progression > 60 && L2Progression <= 75 && normalsidespawned<15){
            spawnenemy("normal","normal");
        }
        if (L2Progression > 75 && L2Progression < 85 && enemiepos.size()<1){
            spawnenemy("normal", "healing");
        }
        if (L2Progression > 85 && L2Progression< 100 && enemiepos.size() < 15){
            spawnenemy("normal","instantd");
        }
        

        
        
       
    //winscreen for l2
    }else if (level == "L2WinScreen"){
        g.setColor(Color.BLACK);
        g.fillRect(0,0,1200,760);
        g.setColor(new Color(224,209,145)); //Color Jazzamoon
        g.setFont(new Font(overtaleFont,Font.BOLD,100));
        g.drawString("Good Job...",300,330);
        g.drawString("Now try L3...", 350,450);


    //level 3 
    }else if (level == "L3"){
         //level visual default
        
         g.setColor(Color.BLACK);
         L3Progression += 0.005;
         g.fillRect(0,0,1200,760);
         g.setColor(new Color(224,209,145)); //Color Jazzamoon
         g.setFont(new Font(overtaleFont,Font.BOLD,60));
         g.drawString("Size: " + me.size, 10,60);
         g.setColor(Color.PINK);
         g.setFont(new Font(overtaleFont,Font.BOLD,35));
         if (L3Progression >= 101){
             level = "L3WinScreen";
         }
         g.drawString(""+(int)L3Progression + "/100", width-140,50);
         me.setBorders();
         
         
         ArrayList <enemies> toremove = new ArrayList<enemies>();
         //the main "brain" for the collision, type detection...etc
         for (enemies p: enemiepos){
             if (p.y > height || p.y < 0 || p.x < 0 || p.x > width){
                 
                toremove.add(p);
                 
                 
          
                 continue;
             }
             if ((p.type.contains("normal"))){
     
                 g.setColor(Color.RED);
 
             }
             if (p.type.contains("healing")){
                 g.setColor(Color.GREEN);
             }
              if (p.type.contains("instantd")){
                 g.setColor(new Color((int)(Math.random()*256),(int)(Math.random()*256),(int)(Math.random()*256)));
             }
             if(p.type.contains("suddendrop")){
               
                p.suddenDropTimer++;
             }
             
             g.fillRect(p.x,p.y,p.sizex,p.sizey);
             if (p.type.contains("suddendrop")){
                if (p.suddenDropTimer > 100){
                if(p.dir == "normal"){
                    p.y += 15;
                }else{
                    p.x -= 15;
                }
            }
                
                
             }else if (p.dir == "normal" ){
                 p.y += 5;
             }else if (p.dir == "side"){
                 p.x -= 5;
             }
             
            
        
         }
         for(enemies p : toremove){
             enemiepos.remove(p);
         }
        
         //end
         //player default settings
         g.setColor(Color.ORANGE);
         g.fillRect(me.x,me.y,me.size,me.size);
         me.setBorders();
         String CollideE = me.isCollideEnemy(enemiepos);
         if (CollideE == "yes"){
             me.size--;
         }else if (CollideE == "instantdeath"){
             me.size = 0;
         }else if (CollideE == "heal"){
             me.size+=3;
         }
         if (me.size == 0){
             level = "DeathScreen";
         }
         //customization of level
         if (L3Progression <= 10 && enemiepos.size() < 10){
            spawnenemy("normal", "healing");
            spawnenemy("side", "healing");
         }
         if ((int)L3Progression == 11 && enemiepos.size() < 20){
            for(int i = 30; i < 1200; i+=30){
                spawnenemyCustomizable("normal", "instantd", i, 0);
            }
         }
         if (((int)L3Progression == 12 || (int)L3Progression == 13) && enemiepos.size()<20){
            for(int i = 1140; i >= 0 ; i-=30){
                spawnenemyCustomizable("normal", "instantd", i, 0);
            }
         }
         if (L3Progression < 30 && L3Progression > 12 && enemiepos.size()< 30){
            spawnenemyCustomizable("normal","normalsuddendrop",(int)(Math.random()*1170)-((int)(Math.random()*1170)%30),0);

         }
         if (L3Progression < 40 && L3Progression > 30){
            spawnenemyCustomizable("normal","instantdsuddendrop",me.x,0);

         }
         if (L3Progression < 50 && L3Progression > 43){
            spawnenemyCustomizable("side", "instantdsuddendrop", 1170, me.y);
         }
         if (L3Progression < 60){
            if (L3Progression > 52){
                spawnenemyCustomizable("normal","instantdsuddendrop",me.x,0);
            }if (L3Progression > 53){
                spawnenemyCustomizable("side", "instantdsuddendrop", 1170, me.y);
            }
         }
         if (L3Progression < 70 && L3Progression > 61 && enemiepos.size() < 20){
            if (L3Boolean){
            for (int i = 0; i < 600; i+=30){
            spawnenemyCustomizable("normal","instantdsuddendrop",i,0);
        
            }
            for(int i = 0; i < 350; i+=30){
                spawnenemyCustomizable("side","instantdsuddendrop",1170,i);

            }
            L3Boolean = false;
        }else{
            for (int i = 600; i < 1170; i+=30){
                spawnenemyCustomizable("normal","instantdsuddendrop",i,0);
            
                }
                for(int i = 350; i < 730; i+=30){
                    spawnenemyCustomizable("side","instantdsuddendrop",1170,i);
    
                }
                L3Boolean = true;
        
        }
        
         }
         if (L3Progression < 75 && L3Progression > 70){
             g.setColor(new Color(224,209,145)); //Color Jazzamoon
             g.setFont(new Font(overtaleFont,Font.BOLD,100));
             g.drawString("You are almost there!", 70,400);
            
         }
         if (L3Progression < 80 && L3Progression >75){
            g.setColor(new Color(224,209,145)); //Color Jazzamoon
            g.setFont(new Font(overtaleFont,Font.BOLD,100));
            g.drawString("For the last stage:", 175,400);
            g.drawString("HINT:Left 1", 350,500);
         }
         if (L3Progression > 81 && L3Progression < 98 && enemiepos.size() < 20){
            spawnenemy("normal", "normal");

         }
         if ((int)L3Progression == 99){
            for(int i = 1; i < 1200; i+=30){
                spawnenemyCustomizable("normal", "instantdsuddendrop", i, 0);
            }
         }
        
      
        


 
        
    //level 3 win screen
    }else if(level == "L3WinScreen"){
        g.setColor(Color.BLACK);
        g.fillRect(0,0,1200,760);
        g.setColor(new Color(224,209,145)); //Color Jazzamoon
        g.setFont(new Font(overtaleFont,Font.BOLD,70));
        g.drawString("You beat the hardest level!", 110,400);
        
    }
        mousePressedX = 0; 
        mousePressedY = 0;
        universalcounter++;
    }
}


        
      
    //starts screen
    public void startScreen(Graphics g){

        g.drawImage(background,0,0,null);


    }
    
    public void mouseClicked(MouseEvent e){
        
        
        
    }
    //checks if mouse pressed
    public void mousePressed(MouseEvent e){
        mousePressedX = e.getX();
        mousePressedY = e.getY();
        if (L1tutorialstage == 0 && level == "L1"){
            L1tutorialstage = 1;
        } 


    }
    public void mouseReleased(MouseEvent e){

    }
    public void mouseEntered(MouseEvent e){
        

    }
    public void mouseExited(MouseEvent e){

    }
    //self-explanatory/ if mouse moves
    public void mouseMoved(MouseEvent e){
        me.x = e.getX();
        me.y = e.getY(); 

        
    }
    public void mouseDragged(MouseEvent e){
        //prevents zak's stupid teleporting exploit
        me.x = e.getX();
        me.y = e.getY(); 

    }
    
    public void actionPerformed(ActionEvent e){

       repaint();

    }
    //if key is pressed
    public void keyPressed(KeyEvent e){
        if (e.getKeyChar() == 'q'){
            level = "main";
            me.size = 30;
            wave = 0;
            enemiepos = new ArrayList<enemies>();
            spawnNormalEnemyX = new ArrayList<Integer>();
            spawnSideEnemyY = new ArrayList<Integer>();
            L1tutorialstage = 0;
            L2Progression = 0;
            L2Wave4sprayCount =1170;
            L2Wave4Boolean = true;
            L1totalenemyspawnedheal = 0;
            L1totalenemyspawnedinstantd = 0;
            L1totalenemyspawnednormal = 0;
            L2Wave3Boolean = true;
            L3Progression = 0;
           
            SFX.c.stop();
            SFX.setFile("pop.wav");
            
       

   
        }
        if(e.getKeyChar() == 'c'){
            if (creditsrolling && level == "main"){
                creditsMusic.stop();
            }
            if(level == "main"){
            creditsrolling = !creditsrolling;
            }
        }
   
      }
    public void keyReleased(KeyEvent e){
       
    }
    public void keyTyped(KeyEvent e){
        
       

    }
    //Additional Code not included in the final submission, made by Mac
       public void JasonCreditsTest()
    {
        
        try
        {
            cred1 = ImageIO.read(new File("cred1.png"));
            credgif = Toolkit.getDefaultToolkit().createImage("credsgif.gif");
        }
        catch (IOException e)
        {
            
        }
    }
    
   
    
    public void creditsMusic()
    {
        try
        {
            
            File creditsMusicFile = new File ("creditsmusic.wav");
               if (creditsMusic == null || !creditsMusic.isRunning())
                {
            creditsMusic = AudioSystem.getClip();
            creditsMusic.open(AudioSystem.getAudioInputStream(creditsMusicFile));
            creditsMusic.start();
            }
            else
            return;
        }
        catch (IOException | UnsupportedAudioFileException | LineUnavailableException h)
        {
            h.printStackTrace();
        }
    }
    
    public void credits(Graphics g)
    {
        creditsMusic();
        JasonCreditsTest();

        g.drawImage(cred1, 0, 0, this);
        
    }


    
    //End of code made by Mac

    //class of music, adds music to the game
    public class Music{
        Clip c;
        AudioInputStream SoundE;
        public void setFile(String name){
            try{
                File file = new File(name);
                SoundE = AudioSystem.getAudioInputStream(file);
                c = AudioSystem.getClip();
                c.open(SoundE);



            }catch(Exception E){
                System.out.print(E);
            }
        }
        public void play(){
            c.start();
        }
        

        

    }
}