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
import java.io.Console;
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
    private String m_frmTime;
    private String m_toTime;

    private Controller() { }

    public static Controller getInstance()
    {
        if (_instance == null)
        {
            System.out.println("NEW CONTROLLER");
            _instance = new Controller();
        }
        return _instance;
    }


    public void init()
    {
        initGui();

        // set data to the vis panel
        GuiFactory.getVisPanel().setData(new Table());
        GuiFactory.getVisPanel().render();

        setActions();

        GuiFactory.getDateSlider().slider.setValue(1);

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

        Table data = DB.query(query);
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


    /**
     * Initialises gui components
     */
    private void initGui() {
        System.out.println("init GUi");
        MainWindow frame = GuiFactory.getMainWindow();
        WeatherPanel weatherPanel = new WeatherPanel();

        // set the current weather pane
        GuiFactory.getVisPanel().setWeatherPanel(weatherPanel);
        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, GuiFactory.getInfoPanel(), GuiFactory.getVisPanel());


        split.setOneTouchExpandable(true);
        split.setDividerLocation(200);

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
     *
     */
    public void filtersChangedUpdateData()
    {
        updateVisNewData();
    }

    /**
     * Set all listeners and their actions
     */
    private void setActions(){
        GuiFactory.getDateSlider().setChangeListener(new DateSliderChanged());

    }

    public void updateWeather()
    {
        // update weather
        String[] t = m_toTime.split(" ");
        WeatherModel weather = queryWeatherByDate(t[0]);

        if(weather!=null)
        {
            GuiFactory.getVisPanel().updateWeather(weather.weather);
        }
    }

    public void updateVisNewData()
    {
        Table data = Controller.getInstance().queryDataWithStrDates(GuiFactory.getInfoPanel().getFilters(), m_frmTime, m_toTime);
        GuiFactory.getVisPanel().updateData(data);

    }


    private class DateSliderChanged implements ChangeListener {

        public void stateChanged(ChangeEvent changeEvent)
        {
            System.out.println("DATE SLIDER CHANGE CALLED");
            Object source = changeEvent.getSource();

            if (source instanceof JSlider) {
                JSlider theJSlider = (JSlider) source;
                if (!theJSlider.getValueIsAdjusting()) {

                    m_toTime = GuiFactory.getDateSlider().strDateTimes.get(theJSlider.getValue());

                    System.out.println(m_toTime);

                    m_frmTime = GuiFactory.getDateSlider().strDateTimes.get(theJSlider.getValue()-1);

                    updateVisNewData();
                    updateWeather();

                }
            } else {
                System.out.println("Something changed: " + source);
            }
        }
    }


}
