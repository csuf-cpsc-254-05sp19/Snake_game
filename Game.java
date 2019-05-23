
package snake_2d;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.LinkedList;
import java.util.Random;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;
 
public class Game extends JPanel implements KeyListener, ActionListener
{
 
    // to array to locate the body of the snake.
    final int[] snake_x = new int[484];
    final int[] snake_y = new int[484];
    
    // LL to make the performnce faster
    LinkedList<Position> snake = new LinkedList();
 
    // direction of the snake
    boolean left = false;
    boolean right = false;
    boolean up = false;
    boolean down = false;
    // images for the snake to look to a specific direction.
    ImageIcon rightImg = new ImageIcon(this.getClass().getResource("/images/right.jpg"));
    ImageIcon leftImg = new ImageIcon(this.getClass().getResource("/images/left.jpg"));
    ImageIcon upImg = new ImageIcon(this.getClass().getResource("/images/up.jpg"));
    ImageIcon downImg = new ImageIcon(this.getClass().getResource("/images/down.jpg"));
    ImageIcon bodyImg = new ImageIcon(this.getClass().getResource("/images/body.png"));
    ImageIcon frtImg = new ImageIcon(this.getClass().getResource("/images/fruit.png"));
    // length of the snake
    int snake_length = 3;
    // array of the position of the fruit.
    int[] frt_x = {20, 40, 60, 80, 100, 120, 140, 160, 200, 220, 240, 260, 280, 300, 320, 340, 360, 380, 400, 420, 440, 460};
    int[] rt_y = {20, 40, 60, 80, 100, 120, 140, 160, 200, 220, 240, 260, 280, 300, 320, 340, 360, 380, 400, 420, 440, 460};
 
    // timer to make the snake keep moving.
    Timer snakeTimer;
 
    int delay = 100; // 
 
    int snake_move = 0; // to check if the snake is moving
 
    int score_total = 0; // store the total score
    int numOfFtrEtn = 0; // numbr of fruit eaten
    int countDown = 99; // to count down from 99
 
    int bestScore = getBestScore(); // to store the best score.
 
    Random random = new Random(); // generate random number
 
    // randomly generate the initial position of the fruit
    int frt_x_position = random.nextInt(22);
    int frt_y_position = 5+random.nextInt(17);
 
    boolean gameOver = false; // to check if the game is over ?
 
    public Game()
    {
        setPreferredSize(new Dimension(750, 500)); // dimension of the window
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        snakeTimer = new Timer(delay, this);
    }
 
    @Override
    public void setSize(int width, int height) {
        super.setSize(width, height);
    }
 
    @Override
    public void setBounds(int x, int y, int width, int height) {
        super.setBounds(x, y, width, height);
    }
 
    @Override
    public void paint(Graphics g)
    {

        // specify the position of the snake every time user start the game.
        if(snake_move == 0)
        {
            snake_x[2] = 40;
            snake_x[1] = 60;
            snake_x[0] = 80;
 
            snake_y[2] = 100;
            snake_y[1] = 100;
            snake_y[0] = 100;
 
            countDown = 99;
            snakeTimer.start();
        }
        // check if user got new best score.
        if(score_total > bestScore)
            bestScore = score_total;
 

        g.setColor(Color.DARK_GRAY); // background color
        g.fillRect(0, 0, 750, 500);
 
        g.setColor(Color.BLUE); // border of board 
        for(int i=6; i<=482; i+=17)
            for(int j=6; j<=482; j+=17)
                g.fillRect(i, j, 13, 13);
 
        g.setColor(Color.CYAN); // another border (border of the border)
        g.fillRect(20, 20, 460, 460);
 
        g.setColor(Color.CYAN); // Name of the game on the righ - label
        g.setFont(new Font("Arial", Font.BOLD, 24));
        g.drawString("Snake 2D", 565, 35);
 
        g.setFont(new Font("Arial", Font.PLAIN, 13));
        g.drawString("+ "+countDown, 510, 222);
 
        g.setColor(Color.LIGHT_GRAY);
 
        g.setFont(new Font("Arial", Font.PLAIN, 15));
        g.drawString("Alzamil's Snake ~", 530, 60);
 
        FontMetrics front_m = g.getFontMetrics(); // pexil
 
        g.setFont(new Font("Arial", Font.PLAIN, 18)); // everything else will be written as this
 
        g.drawString("Highest Score", 576, 110); // Highest score
        g.drawRect(550, 120, 140, 30);
        g.drawString(bestScore+"", 550+(142-front_m.stringWidth(bestScore+""))/2, 142);
 
        g.drawString("Total Score", 573, 190); // total score
        g.drawRect(550, 200, 140, 30);
        g.drawString(score_total+"", 550+(142-front_m.stringWidth(score_total+""))/2, 222);
 
        g.drawString("Fruit ", 575, 270); // number of fruit eaten
        g.drawRect(550, 280, 140, 30);
        g.drawString(numOfFtrEtn+"", 550+(142-front_m.stringWidth(numOfFtrEtn+""))/2, 302);
 
        g.setFont(new Font("Arial", Font.BOLD, 16));
        g.drawString("instruction", 550, 360); // instruction of the game 
 
        g.setFont(new Font("Arial", Font.PLAIN, 14));
        g.drawString("Pause - Start : Space", 550, 385);
        g.drawString("move Up : Arrow Up", 550, 410);
        g.drawString("move Down : Arrow Down", 550, 435);
        g.drawString("move Left : Arrow Left", 550, 460);
        g.drawString("move Right : Arrow Right", 550, 485);
 
        rightImg.paintIcon(this, g, snake_x[0], snake_y[0]); // snake look right
 
        snake.clear(); // clear the previous position of the snake 
 
        // loop to drw the snake every milisecond
        for(int i=0; i<snake_length; i++)
        {
            if(i==0 && left)
                leftImg.paintIcon(this, g, snake_x[i], snake_y[i]);
 
            else if(i==0 && right)
                rightImg.paintIcon(this, g, snake_x[i], snake_y[i]);
 
            else if(i==0 && up)
                upImg.paintIcon(this, g, snake_x[i], snake_y[i]);
 
            else if(i==0 && down)
                downImg.paintIcon(this, g, snake_x[i], snake_y[i]);
 
            else if(i!=0)
                bodyImg.paintIcon(this, g, snake_x[i], snake_y[i]);
 
                // to store the position of the snake
            snake.add(new Position(snake_x[i], snake_y[i]));
        }
 
        // the smallest value of the count down 10
        if(countDown != 10)
            countDown--;
 
        // loop to check if the snake touches its body.
        for(int i=1; i<snake_length; i++)
        {
            if(snake_x[i] == snake_x[0] && snake_y[i] == snake_y[0])
            {
                if(right)
                    rightImg.paintIcon(this, g, snake_x[1], snake_y[1]);
 
                else if(left)
                    leftImg.paintIcon(this, g, snake_x[1], snake_y[1]);
 
                else if(up)
                    upImg.paintIcon(this, g, snake_x[1], snake_y[1]);
 
                else if(down)
                    downImg.paintIcon(this, g, snake_x[1], snake_y[1]);
 
                gameOver = true; // if snake touches its body, set game over to TRUE
 
                snakeTimer.stop(); // and stop snake from moving
 
                g.setColor(Color.WHITE); // Game over label in white color
                g.setFont(new Font("Arial", Font.BOLD, 50));
                g.drawString("Game Over", 110, 220);
 
                g.setFont(new Font("Arial", Font.BOLD, 20)); // restart label 
                g.drawString("Press Space To Restart", 130, 260);
 
                setBestScore(); // to set the highest score.
            }
        }
        
        // if the snake eat the fruit,
        if((frt_x[frt_x_position] == snake_x[0]) && fruitYPos[frt_y_position] == snake_y[0])
        {
            score_total += countDown; // add the score to the total
            countDown = 99; // re-set the count down every time snake eat the fruit
            numOfFtrEtn++; // add the number of fruit eaten
            snake_length++; // increase the length of the snake
        }
 
        // loop to make sure the fruit does not stay above snake
        for(int i=0; i<snake.size(); i++)
        {
            // get randmon positon of the fruit in this case
            if(snake.get(i).x == frt_x[frt_x_position] && snake.get(i).y == fruitYPos[frt_y_position]) {
                frt_x_position = random.nextInt(22);
                frt_y_position = random.nextInt(22);
            }
        }
        // to set the fruit away from the snake.
        frtImg.paintIcon(this, g, frt_x[frt_x_position], fruitYPos[frt_y_position]);
 
        g.dispose();
    }
 
 
    @Override
    public void keyPressed(KeyEvent e) // to check what key is pressed
    {
        
        // check if user pause the game
        if(e.getKeyCode() == KeyEvent.VK_SPACE)
        {
            // if game is running , pause it 
            if(snakeTimer.isRunning() && gameOver == false)
                snakeTimer.stop();
            // re play the game was paused 
            else if(!snakeTimer.isRunning() && gameOver == false)
                snakeTimer.start();
            // play again if the game was over 
            else if(!snakeTimer.isRunning() && gameOver == true)
            {
                gameOver = false;
                snakeTimer.start();
                snake_move = 0;
                score_total = 0;
                numOfFtrEtn = 0;
                snake_length = 3;
                right = true;
                left = false;
                frt_x_position = random.nextInt(22);
                frt_y_position = 5+random.nextInt(17);
            }
        }
 
        // if the user pressed the right arrow key
        else if(e.getKeyCode() == KeyEvent.VK_RIGHT)
        {
            snake_move++;
            right = true; // set the right direction to true
 
            if(!left)
                right = true;
 
            else
            {
                right = false;
                left = true;
            }
 
            up = false;
            down = false;
        }
        // if the user pressed the left arrow key 
        else if(e.getKeyCode() == KeyEvent.VK_LEFT)
        {
            snake_move++;
            left = true; // set the left direction to true
 
            if(!right)
                left = true;
 
            else
            {
                left = false;
                right = true;
            }
 
            up = false;
            down = false;
        }
        // if the user pressed the up arrow
        else if(e.getKeyCode() == KeyEvent.VK_UP)
        {
            snake_move++;
            up = true; // set the up direction to true
 
            if(!down)
                up = true;
 
            else
            {
                up = false;
                down = true;
            }
 
            left = false;
            right = false;
        }
        
        // if the user poressed the down arrow
        else if(e.getKeyCode() == KeyEvent.VK_DOWN)
        {
            snake_move++;
            down = true; // set the down direction to true
 
            if(!up)
                down = true;
 
            else
            {
                up = true;
                down = false;
            }
 
            left = false;
            right = false;
        }
    }
 
 
    @Override
    public void actionPerformed(ActionEvent e) {
 
        if(right)
        {
            for(int i = snake_length-1; i>=0; i--)
                snake_y[i+1] = snake_y[i];
 
            for(int i = snake_length; i>=0; i--)
            {
                if(i==0)
                    snake_x[i] = snake_x[i] + 20;
 
                else
                    snake_x[i] = snake_x[i-1];
 
                if(snake_x[i] > 460)
                    snake_x[i] = 20;
            }
        }
 
        else if(left)
        {
            for(int i = snake_length-1; i>=0; i--)
                snake_y[i+1] = snake_y[i];
 
            for(int i = snake_length; i>=0; i--)
            {
                if(i==0)
                    snake_x[i] = snake_x[i] - 20;
 
                else
                    snake_x[i] = snake_x[i-1];
 
                if(snake_x[i] < 20)
                    snake_x[i] = 460;
            }
        }
 
        else if(up)
        {
            for(int i = snake_length-1; i>=0; i--)
                snake_x[i+1] = snake_x[i];
 
            for(int i = snake_length; i>=0; i--)
            {
                if(i==0)
                    snake_y[i] = snake_y[i] - 20;
 
                else
                    snake_y[i] = snake_y[i-1];
 
                if(snake_y[i] < 20)
                    snake_y[i] = 460;
            }
        }
 
        else if(down)
        {
            for(int i = snake_length-1; i>=0; i--)
                snake_x[i+1] = snake_x[i];
 
            for(int i = snake_length; i>=0; i--)
            {
                if(i==0)
                    snake_y[i] = snake_y[i] + 20;
 
                else
                    snake_y[i] = snake_y[i-1];
 
                if(snake_y[i] > 460)
                    snake_y[i] = 20;
            }
        }
 
        repaint();
    }
 
 
    @Override
    public void keyReleased(KeyEvent e) {
 
    }
 
 
    @Override
    public void keyTyped(KeyEvent e) {
 
    }
 
 
    // this method to store the highest score
    private void setBestScore()
    {
        // check if the current total score is greater than the best score 
        if(score_total >= bestScore)
        {
            // to handle exception when writing to the file
            try {
                FileOutputStream fos = new FileOutputStream("./snake-game-best-score.txt");
                OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
                osw.write(bestScore+"");
                osw.flush();
                osw.close();
            }
            catch(IOException e) {
            }
        }
    }
 
    // this method to get the highest score 
    private int getBestScore()
    {
        // handle exception
        try {
            InputStreamReader isr = new InputStreamReader( new FileInputStream("./snake-game-best-score.txt"), "UTF-8" );
            BufferedReader br = new BufferedReader(isr);
 
            String str = "";
            int c;
            while( (c = br.read()) != -1){
                if(Character.isDigit(c))
                    str += (char)c;
            }
            if(str.equals(""))
                str = "0";
 
            br.close();
            return Integer.parseInt(str);
        }
        catch(IOException e) {
        }
        return 0;
    }
 
}
