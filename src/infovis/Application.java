package infovis;

import infovis.gui.DateSlider;
import infovis.gui.ScatterPlot;
import infovis.gui.VisPanel;

import infovis.gui.WeatherArrow;
import prefuse.data.Table;
import prefuse.data.Tuple;
import prefuse.data.expression.AndPredicate;
import prefuse.data.expression.Predicate;
import prefuse.data.expression.parser.ExpressionParser;


import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.*;



/**
 * Created with IntelliJ IDEA.
 * User: krone
 * Date: 10/10/12
 * Time: 11:35 AM
 * To change this template use File | Settings | File Templates.
 */
public class Application {

    public static void main(String[] args)
    {
        Controller controller = new Controller();
    }

}
