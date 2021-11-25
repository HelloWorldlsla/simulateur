package ch.collisionengine.simulations.balls;

import ch.collisionengine.simulator.App;
import ch.collisionengine.window.Drawable;

import javax.swing.*;
import java.awt.*;

public class BallApp implements App {
    private final Ball a = new Ball(1, 1, -10, 5);
    private final Ball b = new Ball(1, 1, 0, 0);
    private final BallEngine engine = new BallEngine(a, b);
    private final JTextField mass1 = new JTextField("1");
    private final JTextField mass2 = new JTextField("1");
    private final JTextField velocity1 = new JTextField("5");
    private final JTextField velocity2 = new JTextField("0");
    private final JLabel timeLabel = new JLabel("0");
    private double time = 0;

    @Override
    public void setup(JPanel toolBar) {
        toolBar.removeAll();
        toolBar.setLayout(new GridLayout(5, 2, 10, 10));
        toolBar.add(new JLabel("Masse A [kg]"));
        toolBar.add(mass1);
        toolBar.add(new JLabel("Vitesse A [m/s]"));
        toolBar.add(velocity1);
        toolBar.add(new JLabel("Masse B [kg]"));
        toolBar.add(mass2);
        toolBar.add(new JLabel("Vitesse B [m/s]"));
        toolBar.add(velocity2);
        toolBar.add(new JLabel("Temps [s]"));
        toolBar.add(timeLabel);
    }

    @Override
    public void reset() {
        a.position = -10;
        a.velocity = Double.parseDouble(velocity1.getText());
        a.mass = Double.parseDouble(mass1.getText());
        b.position = 0;
        b.velocity = Double.parseDouble(velocity2.getText());
        b.mass = Double.parseDouble(mass2.getText());
        engine.oldSpeedA = 0;
        engine.oldSpeedB = 0;
        time = 0;
        timeLabel.setText("0");
    }

    @Override
    public void update(double time) {
        this.time += time;
        timeLabel.setText(String.valueOf((int) (this.time * 1000) / 1000.0));
        engine.update(time);
    }

    @Override
    public Drawable drawable() {
        return engine;
    }

    @Override
    public String toString() {
        return "Collision 1D";
    }
}
