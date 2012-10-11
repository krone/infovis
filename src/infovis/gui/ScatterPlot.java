package infovis.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

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
import prefuse.action.assignment.ColorAction;
import prefuse.action.assignment.DataColorAction;
import prefuse.action.assignment.DataShapeAction;
import prefuse.action.assignment.ShapeAction;
import prefuse.action.layout.AxisLayout;
import prefuse.controls.PanControl;
import prefuse.controls.ToolTipControl;
import prefuse.controls.ZoomControl;
import prefuse.data.Table;
import prefuse.data.io.DataIOException;
import prefuse.data.io.DelimitedTextTableReader;
import prefuse.data.io.sql.ConnectionFactory;
import prefuse.data.io.sql.DatabaseDataSource;
import prefuse.render.DefaultRendererFactory;
import prefuse.render.ShapeRenderer;
import prefuse.util.ColorLib;
import prefuse.visual.VisualItem;
import prefuse.visual.expression.VisiblePredicate;

/**
 * A simple scatter plot visualization that allows visual encodings to
 * be changed at runtime.
 *
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
public class ScatterPlot extends Display {

    private static final String group = "data";

    private ShapeRenderer m_shapeR = new ShapeRenderer(2);

    public ScatterPlot(Table t, String xfield, String yfield) {
        this(t, xfield, yfield, null);
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
        int[] palette = new int[]{ColorLib.rgb(0, 255, 0), ColorLib.rgb(255, 0, 0), ColorLib.rgb(0, 0, 255)};
        /* ColorLib.rgb converts the colour values to integers */
        // map data to colours in the palette
        DataColorAction fill = new DataColorAction(group, "type", Constants.NOMINAL, VisualItem.FILLCOLOR, palette);
        DataColorAction color = new DataColorAction(group, "type", Constants.NOMINAL, VisualItem.STROKECOLOR, palette);
        m_vis.putAction("color", fill);
        m_vis.putAction("color", color);


        ShapeAction shape = new ShapeAction(group, Constants.SHAPE_ELLIPSE);
        m_vis.putAction("shape", shape);

        ActionList draw = new ActionList();
        draw.add(x_axis);
        draw.add(y_axis);
        draw.add(shape);
        draw.add(color);
        draw.add(fill);
        draw.add(new RepaintAction());
        m_vis.putAction("draw", draw);

        // --------------------------------------------------------------------
        // STEP 3: set up a display and ui components to show the visualization
        setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        setSize(1280,700);
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



    // ------------------------------------------------------------------------

    public static void main(String[] argv) {
        String data = "/fisher.iris.txt";
        String xfield = "page_size";
        String yfield = "count";
        String sfield = "url";
        if ( argv.length >= 3 ) {
            data = argv[0];
            xfield = argv[1];
            yfield = argv[2];
            sfield = ( argv.length > 3 ? argv[3] : null );
        }

        final ScatterPlot sp = demo(data, xfield, yfield, sfield);
        JToolBar toolbar = getEncodingToolbar(sp, xfield, yfield, sfield);



        JFrame frame = new JFrame("p r e f u s e  |  s c a t t e r");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(toolbar, BorderLayout.NORTH);
        frame.getContentPane().add(sp, BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);
    }

    public static ScatterPlot demo(String data, String xfield, String yfield) {
        return demo(data, xfield, yfield, null);
    }

    public static ScatterPlot demo(String data, String xfield,
                                   String yfield, String sfield)
    {


        Table table = null;
        try
        {
            @SuppressWarnings("unused")
            DatabaseDataSource db = ConnectionFactory.getDatabaseConnection("org.postgresql.Driver","jdbc:postgresql://localhost/dataDB", "prefuse", "password");
            table = db.getData("SELECT p.url_id,p.url, p.page_size, COUNT(l.from_url_id) From t_links l, t_pages p Where l.from_url_id = p.url_id AND p.crawl_id = 0 GROUP BY p.url_id, p.page_size, p.url");
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (DataIOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        finally
        {

        }

        ScatterPlot scatter = new ScatterPlot(table, xfield, yfield, sfield);
        scatter.setPointSize(3);
        return scatter;
    }

    private static JToolBar getEncodingToolbar(final ScatterPlot sp,
                                               final String xfield, final String yfield, final String sfield)
    {
        int spacing = 10;

        // create list of column names
        Table t = (Table)sp.getVisualization().getSourceData(group);
        String[] colnames = new String[t.getColumnCount()];
        for ( int i=0; i<colnames.length; ++i )
            colnames[i] = t.getColumnName(i);

        // create toolbar that allows visual mappings to be changed
        JToolBar toolbar = new JToolBar();
        toolbar.setLayout(new BoxLayout(toolbar, BoxLayout.X_AXIS));
        toolbar.add(Box.createHorizontalStrut(spacing));

        final JComboBox xcb = new JComboBox(colnames);
        xcb.setSelectedItem(xfield);
        xcb.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Visualization vis = sp.getVisualization();
                AxisLayout xaxis = (AxisLayout)vis.getAction("x");
                xaxis.setDataField((String)xcb.getSelectedItem());
                vis.run("draw");
            }
        });
        toolbar.add(new JLabel("X: "));
        toolbar.add(xcb);
        toolbar.add(Box.createHorizontalStrut(2*spacing));

        final JComboBox ycb = new JComboBox(colnames);
        ycb.setSelectedItem(yfield);
        ycb.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Visualization vis = sp.getVisualization();
                AxisLayout yaxis = (AxisLayout)vis.getAction("y");
                yaxis.setDataField((String)ycb.getSelectedItem());
                vis.run("draw");
            }
        });
        toolbar.add(new JLabel("Y: "));
        toolbar.add(ycb);
        toolbar.add(Box.createHorizontalStrut(2*spacing));

        final JComboBox scb = new JComboBox(colnames);
        scb.setSelectedItem(sfield);
        scb.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Visualization vis = sp.getVisualization();
                DataShapeAction s = (DataShapeAction)vis.getAction("shape");
                s.setDataField((String)scb.getSelectedItem());
                vis.run("draw");
            }
        });
        toolbar.add(new JLabel("Shape: "));
        toolbar.add(scb);
        toolbar.add(Box.createHorizontalStrut(spacing));
        toolbar.add(Box.createHorizontalGlue());

        return toolbar;
    }

} // end of class ScatterPlot
