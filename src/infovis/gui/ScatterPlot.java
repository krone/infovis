package infovis.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Rectangle2D;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JToolBar;

import prefuse.Constants;
import prefuse.Display;
import prefuse.Visualization;
import prefuse.action.ActionList;
import prefuse.action.RepaintAction;
import prefuse.action.assignment.*;
import prefuse.action.layout.AxisLayout;
import prefuse.action.layout.Layout;
import prefuse.controls.PanControl;
import prefuse.controls.ToolTipControl;
import prefuse.controls.ZoomControl;
import prefuse.data.Schema;
import prefuse.data.Table;
import prefuse.data.expression.Predicate;
import prefuse.data.expression.parser.ExpressionParser;
import prefuse.data.io.DataIOException;
import prefuse.data.io.DelimitedTextTableReader;
import prefuse.data.io.sql.ConnectionFactory;
import prefuse.data.io.sql.DatabaseDataSource;
import prefuse.render.DefaultRendererFactory;
import prefuse.render.ShapeRenderer;
import prefuse.util.ColorLib;
import prefuse.util.FontLib;
import prefuse.util.PrefuseLib;
import prefuse.visual.DecoratorItem;
import prefuse.visual.VisualItem;
import prefuse.visual.expression.VisiblePredicate;

/**
 * A simple scatter plot visualization that allows visual encodings to
 * be changed at runtime.
 *
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
public class ScatterPlot extends Display{

    // create data description of labels, setting colors, fonts ahead of time
    private static final Schema LABEL_SCHEMA = PrefuseLib.getVisualItemSchema();
    static {
        LABEL_SCHEMA.setDefault(VisualItem.INTERACTIVE, false);
        LABEL_SCHEMA.setDefault(VisualItem.TEXTCOLOR, ColorLib.gray(200));
        LABEL_SCHEMA.setDefault(VisualItem.FONT, FontLib.getFont("Tahoma", 16));
    }


    private static final String group = "data";
    ArrayList<ColorAction> colours = new ArrayList<ColorAction>();

    private ShapeRenderer m_shapeR = new ShapeRenderer(2);

    public ScatterPlot(Table t, String xfield, String yfield) {
        this(t, xfield, yfield, null);
    }

    public void addColor(Predicate predicate, int color)
    {
        ColorAction c = new ColorAction(group, predicate, VisualItem.STROKECOLOR, color);
        ColorAction f = new ColorAction(group, predicate, VisualItem.FILLCOLOR, color);
        m_vis.putAction("color", c);
        m_vis.putAction("color", f);
        colours.add(c);
        colours.add(f);

    }

    public void updateData(Table t)
    {
        m_vis.reset();
        m_vis.addTable(group, t);

    }


    public ScatterPlot(Table t, String xfield, String yfield, String sfield) {
        super(new Visualization());

        m_vis.addTable(group, t);

        DefaultRendererFactory rf = new DefaultRendererFactory(m_shapeR);
        m_vis.setRendererFactory(rf);


        AxisLayout x_axis = new AxisLayout(group, xfield, Constants.X_AXIS, VisiblePredicate.TRUE);
        m_vis.putAction("x", x_axis);
        AxisLayout y_axis = new AxisLayout(group, yfield, Constants.Y_AXIS, VisiblePredicate.TRUE);
        m_vis.putAction("y", y_axis);

        //ColorAction color = new ColorAction(group, VisualItem.STROKECOLOR, ColorLib.rgb(255,0,0));
        //ColorAction fill = new ColorAction(group, VisualItem.FILLCOLOR, ColorLib.rgb(255,0,0));
        // colour palette for nominal data type
        /* ColorLib.rgb converts the colour values to integers */
        // map data to colours in the palette
        //DataColorAction fill = new DataColorAction(group, "type", Constants.NOMINAL, VisualItem.FILLCOLOR, palette);
        //DataColorAction color = new DataColorAction(group, "type", Constants.NOMINAL, VisualItem.STROKECOLOR, palette);


        /*ColorAction color = new ColorAction(group, VisualItem.STROKECOLOR, ColorLib.rgb(255,0,0));
        ColorAction fill = new ColorAction(group, VisualItem.FILLCOLOR, ColorLib.rgb(255,0,0));
        m_vis.putAction("color", fill);
        m_vis.putAction("color", color);*/

        Color[] coloursList = {Color.red, Color.orange, Color.yellow, Color.green, Color.cyan, Color.blue, Color.magenta, Color.pink, Color.white };
        for(Color c:coloursList)
        {
            addColor((Predicate)ExpressionParser.parse("clr = '"+String.valueOf(c.getRGB())+"'"), ColorLib.rgb(c.getRed(), c.getGreen(), c.getBlue()));
        }

        ShapeAction shape = new ShapeAction(group, Constants.SHAPE_ELLIPSE);
        m_vis.putAction("shape", shape);

        ActionList draw = new ActionList();
        draw.add(x_axis);
        draw.add(y_axis);
        draw.add(shape);
        //draw.add(new LabelLayout("labelsGrp"));

        for(ColorAction ct:colours)
        {
            draw.add(ct);
        }
        draw.add(new RepaintAction());
        m_vis.putAction("draw", draw);

        // --------------------------------------------------------------------
        // STEP 3: set up a display and ui components to show the visualization
        setBorder(BorderFactory.createEmptyBorder(0,0,0,0));
        setSize(1826,929);
        setHighQuality(true);
        setBackgroundImage("data/Vastopolis_Map_small.jpg", false, false);

        // add movement controls
        addControlListener(new ZoomControl());
        addControlListener(new PanControl());

        ToolTipControl ttc = new ToolTipControl(new String[] {xfield,yfield,sfield});
        //BetterTooltipControl ttc = new BetterTooltipControl("URL=%s,Size=%s,Links=%s",new String[] {xfield,yfield,sfield});
        addControlListener(ttc);

        m_vis.run("draw");
    }

    public void run()
    {
        m_vis.run("draw");
    }

    public int getPointSize() {
        return m_shapeR.getBaseSize();
    }

    public void setPointSize(int size) {
        m_shapeR.setBaseSize(size);
        repaint();
    }

} // end of class ScatterPlot