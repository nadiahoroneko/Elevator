package by.gstu.kharaneka.graph;

import by.gstu.kharaneka.controllers.ManagerElevator;
import by.gstu.kharaneka.entities.Passenger;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by nadia on 31.07.2017.
 */
public class ElevatorGraph extends JComponent {
    private static final Logger logger = Logger.getLogger(ManagerElevator.class);
    private List<Passenger> passengerList;
    private int floorNumber;
    private int capacity;//вместимость
    private int pasFloorRun[];
    private int pasFloorFinish[];
    private int pasEl;
    private int heightE;
    private int wWidth;


    int floor;

    public ElevatorGraph(int floorNumber, int capacity, List<Passenger> passengerList,int heightE,int wWidth) {
        this.floorNumber = floorNumber;
        this.capacity = capacity;
        this.passengerList = passengerList;
        this.heightE=heightE;
        this.wWidth=wWidth;
        pasFloorRun = new int[floorNumber];
        pasFloorFinish = new int[floorNumber];
        pasEl = 0;
    }

    public void paintElevator(int floor, List<Passenger> passengerList,int ktime) {
        this.floor = floor;
        this.passengerList = passengerList;
        pasFloorRun = new int[floorNumber];
        pasFloorFinish = new int[floorNumber];
        pasEl = 0;
        if(ktime<2) ktime=2;
        try {
            TimeUnit.MILLISECONDS.sleep(10*ktime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        repaint();
    }

    /*Метод, перерисовывающий элемент внутри окна
    *при обновлении*/
    public void paintComponent(Graphics g) {
        super.paintComponents(g);

        Graphics2D g2d = (Graphics2D) g;

/* 	Устанавливает цвет рисования в зелёный*/
        g2d.setPaint(Color.GRAY);
        for (int i = 0; i < floorNumber; i++) {
            g2d.drawRect(10, 50+heightE*(floorNumber-i), wWidth-30, heightE);
        }
/* 	Рисует текущим цветом прямоугольник	*/
        g2d.setStroke(new BasicStroke(3.0f));  // толщина равна 10
        g2d.setPaint(Color.BLACK);
        int xE = wWidth/2-heightE*capacity/2;
        int yE = 50 + heightE * (floorNumber-floor);
        int widthE = (heightE-6) * capacity;
        g2d.drawRect(xE, yE, widthE, heightE);
        //logger.debug("floor " + floor);
        g2d.setPaint(Color.RED);
/* 	Рисует текущим цветом в координатах (150,150) строку "привет мир"*/
        g2d.setFont(new Font("TimesRoman", Font.BOLD, 24));

        g2d.drawString("Этаж " + Integer.toString(floor + 1), wWidth/2-50, 24);
        g2d.setColor(Color.blue);

        g2d.setFont(new Font("TimesRoman", Font.BOLD, 16));
        String up="+";
        for (Passenger p : passengerList) {
            if(p.getGoUp()==1) up="+";
            else up="-";
            if (p.getStatus() < 2) {
                g2d.setPaint(Color.GREEN);
                int xP = (int) (xE - 0.8*heightE - (heightE-8) * pasFloorRun[p.getFloorRun()]);
                int yP = 55 + heightE * (floorNumber-p.getFloorRun());
                g2d.fillOval(xP, yP, heightE-10, heightE-10);
                g2d.setPaint(Color.BLACK);
                g2d.drawString(Integer.toString(p.getPassengerId())+up, xP+3, yP + 17);
                pasFloorRun[p.getFloorRun()]++;
            } else if (p.getStatus() == 2) {
                g2d.setPaint(Color.YELLOW);
                int xP = xE +3+ (heightE-8)* pasEl;
                int yP = yE+5;
                g2d.fillOval(xP, yP, heightE-10, heightE-10);
                g2d.setPaint(Color.BLACK);
                g2d.drawString(Integer.toString(p.getPassengerId())+up, xP+3, yP + 17);
                pasEl++;
            } else {
                g2d.setPaint(Color.white);
                int xP = xE + 3+widthE + (heightE-8) * pasFloorFinish[p.getFloorFinish()];
                int yP = 55 + heightE * (floorNumber-p.getFloorFinish());
                g2d.fillOval(xP, yP, heightE-10, heightE-10);
                g2d.setPaint(Color.BLACK);
                g2d.drawString(Integer.toString(p.getPassengerId()), xP+3, yP + 17);
                pasFloorFinish[p.getFloorFinish()]++;
            }

        }

/* 	Вызывает обновление себя после завершения рисования	*/
        //super.repaint();
    }

}
