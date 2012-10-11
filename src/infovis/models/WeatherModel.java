package infovis.models;

/**
 * Created with IntelliJ IDEA.
 * User: krone
 * Date: 10/11/12
 * Time: 4:57 PM
 * To change this template use File | Settings | File Templates.
 */

import prefuse.data.Tuple;

import javax.swing.*;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Data Container class for a weather data point
 */
public class WeatherModel {

    public Date date;
    public String weather;
    public int averageWindSpeed;
    public String windDirection;

    private static final Format formatter = new SimpleDateFormat("M/d/yyyy");

    public WeatherModel()
    {

    }


    public String getDateAsString()
    {
        return WeatherModel.formatter.format(date);
    }
}
