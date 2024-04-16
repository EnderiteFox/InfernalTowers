import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * The main class, processing the command line arguments and starting the game
 */
public class Main {
    public static void main(String[] args) {
        JFrame jframe = new JFrame();
        jframe.setVisible(true);
        jframe.addKeyListener(
            new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_W) System.out.println("W was pressed");
                }
            }
        );
    }
}
