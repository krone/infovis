package infovis;

import infovis.gui.ScatterPlot;
import infovis.gui.VisPanel;
import prefuse.Constants;
import prefuse.Display;
import prefuse.Visualization;
import prefuse.action.ActionList;
import prefuse.action.RepaintAction;
import prefuse.action.assignment.ColorAction;
import prefuse.action.assignment.DataColorAction;
import prefuse.action.filter.VisibilityFilter;
import prefuse.action.layout.graph.ForceDirectedLayout;
import prefuse.activity.Activity;
import prefuse.controls.Control;
import prefuse.controls.DragControl;
import prefuse.controls.PanControl;
import prefuse.controls.ZoomControl;
import prefuse.data.Graph;
import prefuse.data.Table;
import prefuse.data.Tuple;
import prefuse.data.expression.AndPredicate;
import prefuse.data.expression.Predicate;
import prefuse.data.expression.parser.ExpressionParser;
import prefuse.data.io.DataIOException;
import prefuse.data.io.GraphMLReader;
import prefuse.data.query.SearchQueryBinding;
import prefuse.render.DefaultRendererFactory;
import prefuse.render.LabelRenderer;
import prefuse.util.ColorLib;
import prefuse.util.collections.IntIterator;
import prefuse.util.display.BackgroundPainter;
import prefuse.visual.VisualItem;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;


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
        // create main frame
        JFrame frame = new JFrame();
        frame.setLayout(new BorderLayout());
        frame.setSize(800,600);

        // read in and preprocess data
        Table data = IO.readCsv("data/Microblogs_test.csv");
        data = IO.preprocessPosts(data, 1826, 929);


        //String query = "text like %flu% or text like '%sick%' or text like '%cough%'";
        String query = "isSick = 1";
        Predicate myPredicate = (Predicate) ExpressionParser.parse(query);

        VisPanel vis = new VisPanel();
        vis.setData(data);
        vis.addPredicate(myPredicate);
        vis.render();


        JSlider dateSlider = new JSlider();
        dateSlider.setMinimum(1);
        dateSlider.setMinimum(10);

        //JToolBar toolbar = getEncodingToolbar(sp, xfield, yfield, sfield);

        //JFrame frame = new JFrame("p r e f u s e  |  s c a t t e r");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //frame.getContentPane().add(toolbar, BorderLayout.NORTH);
        frame.getContentPane().add(vis, BorderLayout.CENTER);

        frame.pack();
        frame.setVisible(true);
    }

}
