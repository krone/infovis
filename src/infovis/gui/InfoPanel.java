package infovis.gui;

/**
 * Created with IntelliJ IDEA.
 * User: krone
 * Date: 10/11/12
 * Time: 11:55 AM
 * To change this template use File | Settings | File Templates.
 */


import infovis.Controller;
import infovis.data.DB;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

/**
 * Displays information relating to some entity
 * when an event has occurred
 */
public class InfoPanel extends JPanel {

    private  JTextField[] txtFields = new JTextField[9];

    Color[] colours = {Color.red, Color.orange, Color.yellow, Color.green, Color.cyan, Color.blue, Color.magenta, Color.pink, Color.white };
    public  Map<Color, String> m_filters = new HashMap<Color, String>();

    public InfoPanel()
    {
        setSize(120, 800);
        setBackground(Color.DARK_GRAY);
        JLabel label = new JLabel("Filters");

        this.add(label);

        String[] filters = {"flu | cough | sick", "truck | accident | crash"};

        for(int i = 0; i<9; i++)
        {
            JPanel txtPanel = new JPanel();
            txtPanel.setBackground(colours[i]);
            txtPanel.setBorder(BorderFactory.createMatteBorder(1,1,1,1,Color.black));
            txtPanel.setSize(120, 40);

            txtFields[i] = new JTextField(15);
            if(i < filters.length &&  !filters[i].isEmpty())
            {
                txtFields[i].setText(filters[i]);
            }

            txtFields[i].setBorder(javax.swing.BorderFactory.createEmptyBorder());

            txtPanel.add(txtFields[i]);
            add(txtPanel);
        }

        initFiltersMap();

        JButton btnApply = new JButton("Apply");
        btnApply.addActionListener(new ApplyButtonClicked());
        add(btnApply);



    }

    public void initFiltersMap()
    {
        m_filters.clear();

        for(int i = 0; i<colours.length; i++)
        {
            if(txtFields[i].getText().trim().isEmpty()) continue;
            m_filters.put(colours[i], txtFields[i].getText());
        }
    }

    private class ApplyButtonClicked implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            initFiltersMap();

            // send the info to controller to action
            Controller.getInstance().filtersChangedUpdateData(m_filters);
        }
    }


}
