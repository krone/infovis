package infovis;
/**
 * Created with IntelliJ IDEA.
 * User: krone
 * Date: 10/10/12
 * Time: 11:39 AM
 * To change this template use File | Settings | File Templates.
 */

import prefuse.data.Table;
import prefuse.data.Tuple;
import prefuse.data.io.CSVTableReader;
import prefuse.data.io.DataIOException;

import java.util.Iterator;

public class IO
{
    /**
     * Read in a csv file and convert to Table datatype
     * @param filename
     * @return
     * @throws NullPointerException
     */
    public static Table readCsv(String filename) throws NullPointerException {
        CSVTableReader reader = new CSVTableReader();

        try {
            return new CSVTableReader().readTable(filename);
        }
        catch(DataIOException e) {
           e.printStackTrace();
        }
        throw new NullPointerException("Could not read in csv table");
    }

    /**
     * Preprocess the dataset into what we need to display
     * This is only for the microblogs csv file!
     * @param data
     * @return
     */
    public static Table preprocessPosts(Table data, int w, int h) {
        // add our custom fields
        data.addColumn("latitude", Float.class);
        data.addColumn("longitude", Float.class);
        data.addColumn("x", Integer.class);
        data.addColumn("y", Integer.class);
        data.addColumn("isSick", Integer.class);
        data.addColumn("createdDate", String.class);
        data.addColumn("type", String.class);

        float maxLeft = 93.5673f;
        float maxUp = 42.3017f;
        float scale_x = (float)0.375/w;
        float scale_y = (float)0.1408/h;

        // iterate through each tuple in the data
        Iterator itr = data.tuples();
        while(itr.hasNext())
        {
            Tuple el = (Tuple)itr.next();
            String location = el.getString(2);
            location = location.replace("[", "");
            location = location.replace("]", "");
            String[] v = location.split(", ");

            String text = el.getString("text");
            el.set("isSick", 0);
            if(text.contains("cough"))
            {
                el.set("isSick", 1);
                el.set("type", "cough");
            }
            if(text.contains("truck"))
            {
                el.set("isSick", 1);
                el.set("type", "truck");
            }
            if(text.contains("headache"))
            {
                el.set("isSick", 1);
                el.set("type", "headache");
            }

            String date = el.getString("Created_at");
            String[] c = date.split(" ");
            el.set("createdDate", c[0].trim());

            float latitude = Float.parseFloat(v[0]);
            float longitude = Float.parseFloat(v[1]);

            el.set("latitude", latitude);
            el.set("longitude", longitude);

            int xVal = (int)Math.round((maxLeft - longitude)/scale_x);
            el.set("x", xVal);

            int yVal = (int)Math.round((maxUp - latitude)/scale_y);
            el.set("y", yVal);

        }
        return data;
    }




}

