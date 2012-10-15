package infovis.gui;

import infovis.data.IO;
import infovis.models.WeatherModel;
import prefuse.action.ActionList;
import prefuse.data.Table;
import prefuse.data.Tuple;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.swing.Timer;

/**
 * Created with IntelliJ IDEA.
 * User: krone
 * Date: 10/10/12
 * Time: 8:53 PM
 * To change this template use File | Settings | File Templates.
 */

public class DateSlider extends JPanel{

    public JSlider slider;
    private ArrayList<WeatherModel> dates;
    protected boolean isPlay = true;

    public ArrayList<String> strDateTimes;

    public DateSlider()
    {
        Hashtable labelTable = new Hashtable();
        dates = new ArrayList<WeatherModel>();
        strDateTimes = new ArrayList<String>();

        DateFormat formatter =  new SimpleDateFormat("yyyy-MM-dd HH:00:s");
        DateFormat displayformatter =  new SimpleDateFormat("dd/MM");

        Calendar start = new GregorianCalendar();
        start.set(2011, 3, 30, 0, 0, 0);  // start time

        Calendar end = new GregorianCalendar();
        end.set(2011, 4, 22, 23, 59, 59);  // start time


        Calendar current = start;
        System.out.println(formatter.format(current.getTime()));

        int c = 0;
        while(current.getTimeInMillis()  < end.getTimeInMillis())
        {
            JLabel lbl;
            if(c%12==0)
            {
                lbl = new JLabel(displayformatter.format(current.getTime()));
            }
            else{
                lbl = new JLabel(" ");
            }
            labelTable.put(c, lbl);

            strDateTimes.add(formatter.format(current.getTime()));
            System.out.println(formatter.format(current.getTime()));

            current.add(Calendar.HOUR, 2);

            c++;
        }
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(500, 50));

        slider = new JSlider(JSlider.HORIZONTAL, 0, 264, 0);
        slider.setMinorTickSpacing(1);
        slider.setMajorTickSpacing(6);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.setLabelTable(labelTable);
        slider.setPaintLabels(true);



        JToggleButton btnPLay = new JToggleButton();
        btnPLay.setSelected(false);
        btnPLay.setSize(100, 40);
        btnPLay.setIcon(new ImageIcon("data/images/media-playback-start.png"));
        btnPLay.addActionListener(new PlayButtonClicked());
        add(btnPLay, BorderLayout.WEST);

        add(slider, BorderLayout.CENTER);
    }


    private class PlayButtonClicked implements ActionListener{

        private int DELAY = 120;

        public class AnimateAction implements ActionListener
        {
            @Override
            public void actionPerformed(ActionEvent e) {
                int currentVal = slider.getValue();

                if(currentVal<slider.getMaximum())
                {
                    slider.setValue(slider.getValue()+1);
                }
            }
        };

        private Timer m_timer;

        public PlayButtonClicked()
        {

        }
        @Override
        public void actionPerformed(ActionEvent e) {

            JToggleButton btn = null;
            if(e.getSource() instanceof JToggleButton)
            {
                btn = (JToggleButton)e.getSource();
            }
            if(btn==null) return;

            if(isPlay)
            {
                m_timer = new Timer(100, new AnimateAction());
                btn.setIcon(new ImageIcon("data/images/media-playback-pause.png"));
                isPlay = false;
                m_timer.start();
                int currentVal = slider.getValue();
                if(currentVal>=slider.getMaximum())
                {
                    m_timer.stop();
                }
            }
            else{
                // set to play as next action
                btn.setIcon(new ImageIcon("data/images/media-playback-start.png"));
                isPlay = true;
                m_timer.stop();
            }
        }
    }


    public ArrayList<WeatherModel> getDates()
    {
        return dates;
    }

    public void setChangeListener(ChangeListener changeListener)
    {
        slider.addChangeListener(changeListener);
    }
}


