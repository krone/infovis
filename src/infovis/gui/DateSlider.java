package infovis.gui;

import infovis.IO;
import prefuse.data.Table;
import prefuse.data.Tuple;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
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

    public DateSlider()
    {
        Table data = IO.readCsv("data/Weather.csv");
        Iterator iter = data.tuples();

        Hashtable labelTable = new Hashtable();

        int c = 0;
        while(iter.hasNext())
        {
            Tuple el = (Tuple)iter.next();
            labelTable.put(c, new JLabel(el.getString("Date")));
            System.out.println(el.getString("Date"));
            c++;
        }

        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(500, 50));

        JSlider slider = new JSlider(JSlider.HORIZONTAL, 0, 20, 0);
        slider.setMinorTickSpacing(1);
        slider.setMajorTickSpacing(2);
        slider.setPaintTicks(true);

        slider.setPaintLabels(true);
        slider.setLabelTable(labelTable);
        slider.setPaintLabels(true);
        slider.addChangeListener(new BoundedChangeListener());
        add(slider, BorderLayout.NORTH);
    }
}

class BoundedChangeListener implements ChangeListener {
    public void stateChanged(ChangeEvent changeEvent)
    {
        Object source = changeEvent.getSource();

        if (source instanceof JSlider) {
            JSlider theJSlider = (JSlider) source;
            if (!theJSlider.getValueIsAdjusting()) {
                System.out.println("Slider changed: " + theJSlider.getValue());
            }
        } else {
            System.out.println("Something changed: " + source);
        }
    }
}
