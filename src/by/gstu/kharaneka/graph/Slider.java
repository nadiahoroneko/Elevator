package by.gstu.kharaneka.graph;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.BorderLayout;
import java.util.Dictionary;
import java.util.Hashtable;

public class Slider extends JPanel{
    private int value=50;

    public Slider()
    {
        // Создание модели ползунков
        BoundedRangeModel model = new DefaultBoundedRangeModel(50, 0, 0, 100);
        // Создание ползунков
        JSlider slider = new JSlider(model);
        JPanel panel=new JPanel();
        slider.setPaintTicks(true);
        // присоединяем слушателя
        slider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                // меняем надпись
                int value = ((JSlider)e.getSource()).getValue();
                setValue(value);
            }
        });
        panel.add(slider);
        add(panel);
    }
    private void setValue(int value)
    {
        this.value=value;
    }
    public int getValue(){
        return value;
    }
}
