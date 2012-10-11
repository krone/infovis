package infovis.gui;

/**
 * Created with IntelliJ IDEA.
 * User: krone
 * Date: 10/11/12
 * Time: 7:07 PM
 * To change this template use File | Settings | File Templates.
 */


import javax.swing.*;

/**
 * Factory class for gui components
 */
public class GuiFactory {


    private static final VisPanel m_vis = new VisPanel();
    private static final MainWindow m_window = new MainWindow();
    private static final DateSlider m_dateslider = new DateSlider();


    public static VisPanel getVisPanel()
    {
        return  m_vis;
    }
    public static MainWindow getMainWindow()
    {
        return  m_window;
    }
    public static DateSlider getDateSlider()
    {
        return  m_dateslider;
    }
}
