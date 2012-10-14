package infovis.gui;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: krone
 * Date: 10/11/12
 * Time: 12:11 PM
 * To change this template use File | Settings | File Templates.
 */
public class WindPanel extends JComponent {


    private Map<String, Double> m_cadrinals = new HashMap<String, Double>();
    private String m_current = "E";
    private Image m_image;
    double ninety = 1.57079633;

    public WindPanel()
    {


        // set up images
        m_cadrinals.put("E", 0.0);
        m_cadrinals.put("S",ninety);
        m_cadrinals.put("W", ninety*2);
        m_cadrinals.put("N", ninety*3);
        m_cadrinals.put("SE", ninety/2);

        m_cadrinals.put("NW", ninety*3 - (ninety/2));
        m_cadrinals.put("NNW", ninety/2);
        m_cadrinals.put("WNW", ninety/2);

        this.setBounds(150, 0, 480, 480);
        //this.setBorder(new MatteBorder(2,2,2,2, Color.WHITE));
        m_image = Toolkit.getDefaultToolkit().getImage("data/images/E.png");
   }

    public void setCurrent(String c)
    {
        m_current = c;
        repaint();

    }
    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);

        m_image = Toolkit.getDefaultToolkit().getImage("data/images/"+m_current+".png");
        g.drawImage(m_image, 0, 0, this);

        /*Graphics2D g2d=(Graphics2D)g; // Create a Java2D version of g.
        g2d.translate(0, 0); // Translate the center of our coordinates.
        g2d.rotate(m_cadrinals.get(m_current));  // Rotate the image by 1 radian.*/
    }
}
