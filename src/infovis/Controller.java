package infovis;
import infovis.data.DB;
import infovis.gui.*;
import infovis.models.WeatherModel;
import prefuse.data.Table;
import prefuse.data.Tuple;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
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

    private WeatherModel queryWeatherByDate(String date)
    {
        Table data = DB.query("select * from weather where \"Date\" = '"+date+"';");
        Iterator itr = data.tuples();
        if(itr.hasNext())
        {
            Tuple el = (Tuple)itr.next();
            WeatherModel weather = new WeatherModel();

            weather.averageWindSpeed = Integer.parseInt(el.getString("Average_Wind_Speed"));
            weather.date = el.getDate("Date");
            weather.windDirection = el.getString("Wind_Direction");
            weather.weather = el.getString("Weather");
            return weather;
        }
        return null;
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

        Table data = queryData(null, null, null);

        // set data to the vis panel
        GuiFactory.getVisPanel().setData(data);
        GuiFactory.getVisPanel().render();

        setActions();
    }

    /**
     * Initialises gui components
     */
    private void initGui() {
        System.out.println("init Gui");
        MainWindow frame = GuiFactory.getMainWindow();
        WeatherPanel weatherPanel = new WeatherPanel();

        // set the current weather pane
        GuiFactory.getVisPanel().setWeatherPanel(weatherPanel);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, GuiFactory.getInfoPanel(), GuiFactory.getVisPanel());

        GuiFactory.getInfoPanel().add(new WeatherPanel());

        split.setOneTouchExpandable(true);
        split.setDividerLocation(150);

        Dimension minimumSize = new Dimension(100, 50);
        weatherPanel.setMinimumSize(minimumSize);
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

    public void updateGuiToNewDateTime(DateSlider dateSlider, int dateSliderPosition)
    {
        String toTime = dateSlider.strDateTimes.get(dateSliderPosition);
        String frmTime = dateSlider.strDateTimes.get(dateSliderPosition-1);

        Table data = Controller.getInstance().queryDataWithStrDates(GuiFactory.getInfoPanel().m_filters, frmTime, toTime);
        GuiFactory.getVisPanel().updateData(data);

        String[] t = toTime.split(" ");

        // update weather
        WeatherModel weather = queryWeatherByDate(t[0]);

        GuiFactory.getVisPanel().updateWeather(weather.weather);
        GuiFactory.getVisPanel().repaint();
    }


    private class DateSliderChanged implements ChangeListener {

        public void stateChanged(ChangeEvent changeEvent)
        {
            Object source = changeEvent.getSource();

            if (source instanceof JSlider) {
                JSlider theJSlider = (JSlider) source;
                if (!theJSlider.getValueIsAdjusting()) {

                    updateGuiToNewDateTime(GuiFactory.getDateSlider(), theJSlider.getValue());

                    /*String toTime = GuiFactory.getDateSlider().strDateTimes.get(theJSlider.getValue());
                    String frmTime = GuiFactory.getDateSlider().strDateTimes.get(theJSlider.getValue()-1);

                    Table data = Controller.getInstance().queryDataWithStrDates(GuiFactory.getInfoPanel().m_filters, frmTime, toTime);
                    GuiFactory.getVisPanel().updateData(data);

                    String[] t = toTime.split(" ");

                    // update weather
                    WeatherModel weather = queryWeatherByDate(t[0]);

                    GuiFactory.getVisPanel().updateWeather(weather.weather);
                    GuiFactory.getVisPanel().repaint(); */
                }
            } else {
                System.out.println("Something changed: " + source);
            }
        }
    }


}
