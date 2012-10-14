package infovis.gui;

import javax.imageio.ImageIO;
import javax.swing.*;
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
public class WeatherPanel extends JComponent {

    BevelArrows arrows = new BevelArrows();
    private String current = "cloudy";
    private double currentDir = 90.0;


    Map<String, Image> m_images = new HashMap<String, Image>();

    public WeatherPanel()
    {
        setSize(200, 200);

        // set up images
        m_images.put("cloudy", Toolkit.getDefaultToolkit().getImage("data/images/weather-cloudy.png"));
        m_images.put("rain", Toolkit.getDefaultToolkit().getImage("data/images/weather-rain.png"));
        m_images.put("showers", Toolkit.getDefaultToolkit().getImage("data/images/weather-showers.png"));
        m_images.put("clear", Toolkit.getDefaultToolkit().getImage("data/images/weather-clear.png"));
        m_images.put("test", Toolkit.getDefaultToolkit().getImage("data/images/N.png"));

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

        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        Image img = m_images.get(current);
        if(img!=null)
        {

            g2.drawImage(img, 0, 0, this);
        }
    }
}
