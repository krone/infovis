package infovis.gui;

/**
 * Created with IntelliJ IDEA.
 * User: krone
 * Date: 10/11/12
 * Time: 7:07 PM
 * To change this template use File | Settings | File Templates.
 */


import infovis.Controller;

import javax.swing.*;

/**
 * Factory class for gui components
 */
public class GuiFactory {

    private static VisPanel m_vis = new VisPanel();
    private static MainWindow m_window = new MainWindow();
    private static DateSlider m_dateslider = new DateSlider();
    private static InfoPanel m_info = new InfoPanel();


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
    public static InfoPanel getInfoPanel()
    {
        return  m_info;
    }
}
