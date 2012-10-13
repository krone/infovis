package infovis.gui;

import infovis.data.IO;
import infovis.models.WeatherModel;
import prefuse.data.Table;
import prefuse.data.Tuple;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: krone
 * Date: 10/10/12
 * Time: 8:53 PM
 * To change this template use File | Settings | File Templates.
 */

public class DateSlider extends JPanel{

    private JSlider slider;
    private ArrayList<WeatherModel> dates;

    public ArrayList<String> strDateTimes;

    public DateSlider()
    {

        Hashtable labelTable = new Hashtable();
        dates = new ArrayList<WeatherModel>();
        strDateTimes = new ArrayList<String>();

        DateFormat formatter =  new SimpleDateFormat("yyyy-MM-dd H:00:s");

        DateFormat displayformatter =  new SimpleDateFormat("dd/MM");

        Calendar start = new GregorianCalendar();
        start.set(2011, 3, 30, 0, 0, 1);  // start time


        Calendar end = new GregorianCalendar();
        end.set(2011, 4, 22, 23, 59, 59);  // start time


        Calendar current = start;
        System.out.println(formatter.format(current.getTime()));

        int c = 0;
        while(current.getTimeInMillis()  < end.getTimeInMillis())
        {
             current.add(Calendar.HOUR, 6);
            JLabel lbl;

            if(c%6==0)
            {
                lbl = new JLabel(displayformatter.format(current.getTime()));
            }
            else{
                lbl = new JLabel(" ");
            }


            labelTable.put(c, lbl);

            strDateTimes.add(formatter.format(current.getTime()));
            System.out.println(formatter.format(current.getTime()));
            c++;
        }


        Table data = IO.readCsv("data/Weather.csv");
        Iterator itr = data.tuples();

        c = 0;
        while(itr.hasNext())
        {
            Tuple el = (Tuple)itr.next();

            //labelTable.put(c, new JLabel(el.getString("Date")));

            WeatherModel model = new WeatherModel();
            model.date =  el.getDate("Date");
            model.weather = el.getString("Weather");
            model.averageWindSpeed = el.getInt("Average_Wind_Speed");
            model.windDirection = el.getString("Wind_Direction");
            dates.add(model);
            c++;
        }

        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(500, 50));

        slider = new JSlider(JSlider.HORIZONTAL, 0, 83, 0);
        slider.setMinorTickSpacing(1);
        slider.setMajorTickSpacing(4);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.setLabelTable(labelTable);
        slider.setPaintLabels(true);

        add(slider, BorderLayout.NORTH);
    }



    public void DateSlider_t()
    {
        Table data = IO.readCsv("data/Weather.csv");
        Iterator itr = data.tuples();

        Hashtable labelTable = new Hashtable();
        dates = new ArrayList<WeatherModel>();

        int c = 0;
        while(itr.hasNext())
        {
            Tuple el = (Tuple)itr.next();

            labelTable.put(c, new JLabel(el.getString("Date")));

            WeatherModel model = new WeatherModel();
            model.date =  el.getDate("Date");
            model.weather = el.getString("Weather");
            model.averageWindSpeed = el.getInt("Average_Wind_Speed");
            model.windDirection = el.getString("Wind_Direction");
            dates.add(model);
            c++;

        }
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(500, 50));

        slider = new JSlider(JSlider.HORIZONTAL, 0, 20, 0);
        slider.setMinorTickSpacing(1);
        slider.setMajorTickSpacing(2);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.setLabelTable(labelTable);
        slider.setPaintLabels(true);

        add(slider, BorderLayout.NORTH);
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


