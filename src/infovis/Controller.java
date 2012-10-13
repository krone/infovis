package infovis;
import infovis.data.DB;
import infovis.data.IO;
import infovis.gui.*;
import prefuse.action.ActionList;
import prefuse.data.Table;
import prefuse.data.expression.Predicate;
import prefuse.data.expression.parser.ExpressionParser;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.tools.JavaCompiler;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import java.sql.Date;


/**
 * Created with IntelliJ IDEA.
 * User: krone
 * Date: 10/11/12
 * Time: 6:54 PM
 * To change this template use File | Settings | File Templates.
 */

/**
 * Singleton class
 */
public class Controller {


    private static Controller _instance = null;

    public static Controller getInstance()
    {
        if (_instance == null)
        {
            _instance = new Controller();
        }
        return _instance;
    }


    private Table queryDataWithStrDates(Map<Color, String> terms, String fromTime, String toTime)
    {

        String filters = "";
        int i = 0;
        for(Color c: terms.keySet())
        {
            String str = terms.get(c);
            String colour = String.valueOf(c.getRGB());

            String sql =  "SELECT '"+colour+"' CLR, to_tsquery('"+str+"') QRY ";

            if(i < terms.size()-1)
            {
                sql+=" UNION ";
            }
            filters+=sql;
            i++;
        }

        System.out.println(filters);



        String query = "SELECT bl.x,bl.y, q0.CLR\n" +
                "FROM(SELECT NULL AS X) x\n" +
                "JOIN(" + filters +

                ") q0 ON 1=1\n" +
                "JOIN blogs bl ON 1=1\n" +
                "\n" +
                "AND bl.\"Created_at\" BETWEEN '"+fromTime+"' AND '"+toTime+"'\n" +
                "AND bl.textsearchable_index_col @@ q0.QRY\n" +
                "WHERE 1=1\n" +
                ";";

        String queryold = "SELECT bl.x,bl.y, q0.CLR\n" +
                "FROM(SELECT NULL AS X) x\n" +
                "JOIN(SELECT 'yellow' CLR, to_tsquery('flu | cough | sick')     QRY UNION\n" +
                "         SELECT 'red'    CLR, to_tsquery('crash')              QRY UNION\n" +
                "         SELECT 'green'  CLR, to_tsquery('foo')                QRY) q0 ON 1=1\n" +
                "JOIN blogs bl ON 1=1\n" +
                "\n" +
                "AND bl.\"Created_at\" BETWEEN '"+fromTime+"' AND '"+toTime+"'\n" +
                "AND bl.textsearchable_index_col @@ q0.QRY\n" +
                "WHERE 1=1\n" +
                ";";


        System.out.println(query);


        Table data = DB.query(query);
        System.out.println(data.isValidRow(1));
        return data;
    }



    private Table queryData(ArrayList<String> terms, Date fromTime, Date toTime)
    {
        String query = "SELECT bl.x,bl.y, q0.CLR\n" +
                "FROM(SELECT NULL AS X) x\n" +
                "JOIN(SELECT 'yellow' CLR, to_tsquery('flu | cough | sick')     QRY UNION\n" +
                "         SELECT 'red'    CLR, to_tsquery('crash')              QRY UNION\n" +
                "         SELECT 'green'  CLR, to_tsquery('foo')                QRY) q0 ON 1=1\n" +
                "JOIN blogs bl ON 1=1\n" +
                "\n" +
                "AND bl.\"Created_at\" BETWEEN '2011-05-18 09:00:00' AND '2011-05-19 08:59:59'\n" +
                "AND bl.textsearchable_index_col @@ q0.QRY\n" +
                "WHERE 1=1\n" +
                ";";


        //'2011-05-19 08:59:59'


        Calendar c = new GregorianCalendar();
        c.set(2011, 3, 30, 0, 0, 1);

        DateFormat formatter =  new SimpleDateFormat("yyyy-MM-dd H:00:s");
        System.out.println(formatter.format(c.getTime()));


        java.sql.Date d = new Date(c.getTimeInMillis());
        d.toString();



        System.out.println(d.toString());

        c.set(2011, 3, 30, 12, 0, 0);

        Table data = DB.query(query);
        System.out.println(data.isValidRow(1));
        return data;
    }

    /**
     * Query
     * @param terms
     * @param fromTime
     * @param toTime
     * @return
     */
    private Table queryDataWithinBox(ArrayList<String> terms, Date fromTime, Date toTime)
    {
       throw new NotImplementedException();
    }


    private Controller() {


        initGui();

        //"2011-05-18 13:26:00"

        // read in and preprocess data
        //Table data = IO.readCsv("data/Microblogs.csv");

        Table data = queryData(null, null, null);

        //data = IO.preprocessPosts(data, 1826, 929);
        //String query = "createdDate = '5/13/2011' AND isSick = 1";
        //Predicate myPredicate = (Predicate) ExpressionParser.parse(query);

        // set data to the vis panel
        GuiFactory.getVisPanel().setData(data);
        //GuiFactory.getVisPanel().addPredicate(myPredicate);
        GuiFactory.getVisPanel().render();


        setActions();
    }

    /**
     * Initialises gui components
     */
    private void initGui() {
        MainWindow frame = GuiFactory.getMainWindow();
        WeatherArrow w = new WeatherArrow();
        GuiFactory.getVisPanel().setWeatherPanel(w);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, GuiFactory.getInfoPanel(), GuiFactory.getVisPanel());
        split.setOneTouchExpandable(true);
        split.setDividerLocation(150);

        Dimension minimumSize = new Dimension(100, 50);
        w.setMinimumSize(minimumSize);
        GuiFactory.getVisPanel().setMinimumSize(minimumSize);

        frame.getContentPane().add(GuiFactory.getDateSlider(), BorderLayout.NORTH);
        frame.getContentPane().add(split, BorderLayout.CENTER);

        frame.pack();
        frame.setVisible(true);
    }


    /**
     * Called to apply new set of filters to the dataset
     * @param _filters
     */
    public void filtersChangedUpdateData(Map<Color, String> _filters)
    {

    }



    /**
     * Set all listeners and their actions
     */
    private void setActions(){
        GuiFactory.getDateSlider().setChangeListener(new DateSliderChanged());

    }

    /*private class DateSliderChanged implements ChangeListener {

        public void stateChanged(ChangeEvent changeEvent)
        {
            Object source = changeEvent.getSource();

            if (source instanceof JSlider) {
                JSlider theJSlider = (JSlider) source;
                if (!theJSlider.getValueIsAdjusting()) {

                    String date = GuiFactory.getDateSlider().getDates().get(theJSlider.getValue()).getDateAsString();
                    Predicate myPredicate = (Predicate) ExpressionParser.parse("createdDate = '" + date + "' AND isSick = 1");

                    GuiFactory.getVisPanel().getWeatherPanel().setCurrent("W");
                    GuiFactory.getVisPanel().getWeatherPanel().repaint();
                    GuiFactory.getVisPanel().addPredicate(myPredicate);
                    GuiFactory.getVisPanel().repaint();
                }
            } else {
                System.out.println("Something changed: " + source);
            }
        }
    } */

    private class DateSliderChanged implements ChangeListener {

        public void stateChanged(ChangeEvent changeEvent)
        {
            Object source = changeEvent.getSource();

            if (source instanceof JSlider) {
                JSlider theJSlider = (JSlider) source;
                if (!theJSlider.getValueIsAdjusting()) {

                    String toTime = GuiFactory.getDateSlider().strDateTimes.get(theJSlider.getValue());
                    String frmTime = GuiFactory.getDateSlider().strDateTimes.get(theJSlider.getValue()-1);

                    Table data = Controller.getInstance().queryDataWithStrDates(GuiFactory.getInfoPanel().m_filters, frmTime, toTime);
                    GuiFactory.getVisPanel().updateData(data);

                    /*GuiFactory.getVisPanel().getWeatherPanel().setCurrent("W");
                    GuiFactory.getVisPanel().getWeatherPanel().repaint();
                    GuiFactory.getVisPanel().addPredicate(myPredicate);*/


                    GuiFactory.getVisPanel().repaint();
                }
            } else {
                System.out.println("Something changed: " + source);
            }
        }
    }


}
