package infovis.gui.weather;

import infovis.gui.GuiFactory;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: krone
 * Date: 10/15/12
 * Time: 12:16 PM
 * To change this template use File | Settings | File Templates.
 */
public class Rain extends JComponent implements ActionListener{

    private static int DELAY = 120;
    private static int density = 200;
    private static int size = 10;
    private Timer m_timer;
    Random randomGenerator;
    private boolean clear = false;

    public Rain() {
        setDoubleBuffered(true);
        setSize(1800, 930);
        m_timer = new Timer(DELAY, this);
        randomGenerator = new Random();

    }

    public void stopAndClear(){
        clear = true;
        repaint();
        if(m_timer.isRunning())
        {
            m_timer.stop();
        }

    }

    public void start() {

        clear = false;
        m_timer.start();
    }

    @Override
    public void paint(Graphics g)
    {
        super.paint(g);

        if(clear) return;

        g.setColor(Color.lightGray);
        for(int i = 0; i<density; i++)
        {
            int randomX = randomGenerator.nextInt(1800);
            int randomY = randomGenerator.nextInt(930);

            g.drawLine(randomX, randomY, randomX+size, randomY+size);
        }
        g.dispose();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("Action preformed rain");
        repaint();
    }

}
