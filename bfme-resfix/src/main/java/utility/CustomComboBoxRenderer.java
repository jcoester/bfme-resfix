package utility;

import javax.swing.*;
import java.awt.*;

public class CustomComboBoxRenderer<E> extends DefaultListCellRenderer {
    private static final int ITEM_HEIGHT = 30; // Set your desired item height here

    public CustomComboBoxRenderer(ListCellRenderer<? super E> ignoredRenderer) {
        super();
        setOpaque(true);
    }

    @Override
    public Dimension getPreferredSize() {
        Dimension size = super.getPreferredSize();
        size.height = ITEM_HEIGHT;
        return size;
    }

    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
    }
}
