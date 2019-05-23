
package snake_2d;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
 
public class MainClass extends JFrame 
{
 
    public MainClass()
    {
        showGui(); // to create and show gui
    }
 
    // show gui method
    private void showGui()
    {
 
        Game gameDrawer = new Game(); // to draw the game board
 
        add(gameDrawer); // add it to the gui
 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // close when click on x
        setResizable(false); // to make the window not resizable
        pack(); // to pack
        setLocationRelativeTo(null); // make it in the center of the screen 
        setVisible(true); // to make the gui visible 
 
    }
 
    public static void main(String[] args) 
    {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MainClass();
            }
        });
    }
 
}