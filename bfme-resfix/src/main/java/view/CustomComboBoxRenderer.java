package view;

import javax.swing.*;
import java.awt.*;

public class CustomComboBoxRenderer extends DefaultListCellRenderer {
    private static final int ITEM_HEIGHT = 40; // Set your desired item height here

    public CustomComboBoxRenderer() {
        super();
        setOpaque(true);
    }

    @Override
    public Dimension getPreferredSize() {
        Dimension size = super.getPreferredSize();
        size.height = ITEM_HEIGHT;
        return size;
    }
}
