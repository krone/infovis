package infovis;
import infovis.gui.*;
import prefuse.data.Table;
import prefuse.data.expression.Predicate;
import prefuse.data.expression.parser.ExpressionParser;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: krone
 * Date: 10/11/12
 * Time: 6:54 PM
 * To change this template use File | Settings | File Templates.
 */
public class Controller {



    public Controller() {


        initGui();


        // read in and preprocess data
        Table data = IO.readCsv("data/Microblogs_test.csv");
        data = IO.preprocessPosts(data, 1826, 929);
        String query = "createdDate = '5/13/2011' AND isSick = 1";
        Predicate myPredicate = (Predicate) ExpressionParser.parse(query);

        // set data to the vis panel
        GuiFactory.getVisPanel().setData(data);
        GuiFactory.getVisPanel().addPredicate(myPredicate);
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

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, w, GuiFactory.getVisPanel());
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
     * Set all listeners and their actions
     */
    private void setActions(){
        GuiFactory.getDateSlider().setChangeListener(new DateChanged());
    }


    private class DateChanged implements ChangeListener {

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
    }

}
