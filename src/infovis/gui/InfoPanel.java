package infovis.gui;

/**
 * Created with IntelliJ IDEA.
 * User: krone
 * Date: 10/11/12
 * Time: 11:55 AM
 * To change this template use File | Settings | File Templates.
 */


import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Displays information relating to some entity
 * when an event has occurred
 */
public class InfoPanel extends JPanel {
    public InfoPanel(){

        this.setSize(150, 800);
        this.setBackground(Color.DARK_GRAY);
        JLabel label = new JLabel("Filters");

        this.add(label);
        Color[] colours = {Color.RED, Color.orange, Color.yellow, Color.green, Color.cyan, Color.blue, Color.magenta, Color.pink, Color.white };

        for(int i = 0; i<8; i++)
        {
            JPanel txtPanel = new JPanel();
            txtPanel.setBackground(colours[i]);
            txtPanel.setBorder(BorderFactory.createMatteBorder(2,2,2,2,Color.gray));
            txtPanel.setSize(120, 40);

            JTextField txtfield = new JTextField(15);
            txtfield.setBorder(javax.swing.BorderFactory.createEmptyBorder());

            txtPanel.add(txtfield);
            this.add(txtPanel);
        }
        JButton btnApply = new JButton("Apply");
        btnApply.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Clicked");
                //To change body of implemented methods use File | Settings | File Templates.
            }
        });
        this.add(btnApply);

        //this.setLayout(new GridLayout());
    }

}
