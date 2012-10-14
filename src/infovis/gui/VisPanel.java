package infovis.gui;

import prefuse.data.Table;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import prefuse.data.expression.Predicate;

/**
 * Created with IntelliJ IDEA.
 * User: krone
 * Date: 10/10/12
 * Time: 1:00 PM
 * To change this template use File | Settings | File Templates.
 */


/**
 * Main Visualisation Panel
 */
public class VisPanel extends JLayeredPane implements MouseListener
{
    private ScatterPlot _sp;
    private ArrayList<Predicate> _predicates = new ArrayList<Predicate>();
    private WeatherPanel m_w;
    private JLabel m_timeLabel = new JLabel();
    /**
     * Takes in prefuse Table
     * and renders the visualisation
     * in the panel
     */
    public VisPanel()
    {
        System.out.println("CREATD NEW VIS PANEL");


        m_timeLabel.setText("TESTING");
        m_timeLabel.setForeground(Color.white);
        m_timeLabel.setFont(new Font("Serif", Font.BOLD, 14));


        setLayer(m_timeLabel, 90);

        m_timeLabel.setBounds(900, 8, 400, 50);
        this.add(m_timeLabel, new Integer(20));

    }


    public void updateText(String txt)
    {
        m_timeLabel.setText(txt);
        m_timeLabel.repaint();
    }


    public void updateData(Table data)
    {
        _sp.updateData(data);
        _sp.repaint();
        _sp.run();
    }


    public void setData(Table data)
    {
        _sp = new ScatterPlot(data, "x", "y");
        _sp.setPointSize(7);
        _sp.setSize(1826, 929);
        this.setSize(1826, 929);
    }
    public void addPredicate(Predicate p)
    {
        _predicates.add(p);
        _sp.setPredicate(p);
    }

    public void render()
    {
        this.add(_sp);
    }

    public void repaint()
    {
        if(_sp!=null)
        {
            _sp.run();
        }

    }
    public void setWeatherPanel(WeatherPanel w)
    {
        add(w, 100);
        m_w = w;
        w.repaint();
    }
    public WeatherPanel getWeatherPanel()
    {
        return m_w;
    }


    public void updateWeather(String weather)
    {
        m_w.setCurrent(weather);
        repaint();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        System.out.println(e.getX());
        System.out.println(e.getY());
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void mousePressed(MouseEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
        System.out.println(e.getX());
        System.out.println(e.getY());
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void mouseExited(MouseEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }


}
