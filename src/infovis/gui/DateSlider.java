package infovis.gui;

import infovis.data.IO;
import infovis.models.WeatherModel;
import prefuse.data.Table;
import prefuse.data.Tuple;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;

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

    public DateSlider()
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


