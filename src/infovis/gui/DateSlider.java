package infovis.gui;

import infovis.IO;
import prefuse.data.Table;
import prefuse.data.Tuple;
import prefuse.data.expression.Predicate;
import prefuse.data.expression.parser.ExpressionParser;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.text.Format;
import java.text.SimpleDateFormat;
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



    public DateSlider(VisPanel vis)
    {
        Table data = IO.readCsv("data/Weather.csv");
        Iterator iter = data.tuples();
        Hashtable labelTable = new Hashtable();
        ArrayList<String> dates = new ArrayList<String>();

        Format formatter;
        formatter = new SimpleDateFormat("M/d/yyyy");

        int c = 0;
        while(iter.hasNext())
        {
            Tuple el = (Tuple)iter.next();
            labelTable.put(c, new JLabel(el.getString("Date")));
            String date = formatter.format(el.getDate("Date"));
            dates.add(date);
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
        slider.addChangeListener(new BoundedChangeListener(dates, vis));
        add(slider, BorderLayout.NORTH);
    }


}

class BoundedChangeListener implements ChangeListener
{
    ArrayList<String> dates;
    private VisPanel _vis;
    public BoundedChangeListener(ArrayList<String> vals, VisPanel vis)
    {
        dates = vals;
        _vis = vis;
    }
    public void stateChanged(ChangeEvent changeEvent)
    {
        Object source = changeEvent.getSource();

        if (source instanceof JSlider) {
            JSlider theJSlider = (JSlider) source;
            if (!theJSlider.getValueIsAdjusting()) {

                String date = dates.get(theJSlider.getValue());
                System.out.println(date);


                Predicate myPredicate = (Predicate) ExpressionParser.parse("createdDate = '"+date+"' AND isSick = 1");

                _vis.addPredicate(myPredicate);
                _vis.render();
                _vis.repaint();

            }
        } else {
            System.out.println("Something changed: " + source);
        }
    }
}
