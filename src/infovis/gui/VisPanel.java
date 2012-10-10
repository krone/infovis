package infovis.gui;

import prefuse.Constants;
import prefuse.Display;
import prefuse.Visualization;
import prefuse.action.ActionList;
import prefuse.action.assignment.ColorAction;
import prefuse.action.layout.AxisLayout;
import prefuse.controls.Control;
import prefuse.controls.ZoomControl;
import prefuse.data.Table;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import infovis.gui.ScatterPlot;
import prefuse.data.expression.Predicate;
import prefuse.util.ColorLib;
import prefuse.util.display.BackgroundPainter;
import prefuse.visual.VisualItem;
import prefuse.visual.expression.VisiblePredicate;

/**
 * Created with IntelliJ IDEA.
 * User: krone
 * Date: 10/10/12
 * Time: 1:00 PM
 * To change this template use File | Settings | File Templates.
 */


/**
 * Main Visualisation Panel
 */
public class VisPanel extends JPanel
{
    private ScatterPlot _sp;
    private ArrayList<Predicate> _predicates = new ArrayList<Predicate>();
    /**
     * Takes in prefuse Table
     * and renders the visualisation
     * in the panel
     */
    public VisPanel()
    {


    }

    public void setData(Table data)
    {
        ZoomControl zoom = new ZoomControl(Control.RIGHT_MOUSE_BUTTON);
        _sp = new ScatterPlot(data, "x", "y");
        _sp.addControlListener(zoom);
        _sp.setPointSize(5);
        _sp.setSize(1800, 930);
    }
    public void addPredicate(Predicate p)
    {
        _predicates.add(p);
        _sp.setPredicate(p);
    }

    public void render()
    {
        this.add(_sp);
    }



}
