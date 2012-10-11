package infovis.gui;

import javax.swing.*;
import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: krone
 * Date: 10/11/12
 * Time: 7:16 PM
 * To change this template use File | Settings | File Templates.
 */
public class MainWindow extends JFrame {
    public MainWindow()
    {
        super("Info Vis");
        setLayout(new BorderLayout());
        setSize(800,600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setLayout(new BorderLayout());
        getGlassPane().setVisible(true);

    }
}
