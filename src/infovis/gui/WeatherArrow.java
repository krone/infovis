package infovis.gui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: krone
 * Date: 10/11/12
 * Time: 12:11 PM
 * To change this template use File | Settings | File Templates.
 */
public class WeatherArrow extends JPanel {

    BevelArrows arrows = new BevelArrows();
    private String current = "N";
    private double currentDir = 90.0;
    Image m_img;

    public WeatherArrow()
    {
        setBackground(Color.DARK_GRAY);
        setSize(200, 200);
        m_img = Toolkit.getDefaultToolkit().getImage("data/images/"+current+".png");
    }

    public void setCurrent(String c)
    {
        current = c;
        System.out.println(current);
        currentDir+=90.0;
        //m_img = Toolkit.getDefaultToolkit().getImage("data/images/"+current+".png");

    }

    @Override
    public void paintComponent ( Graphics g )
    {


        Graphics2D g2 = (Graphics2D) g;

        System.out.println("drawing image");

        g2.drawImage(m_img, 0, 0, this);
        g2.rotate(currentDir);

        g2.finalize();
    }
}
