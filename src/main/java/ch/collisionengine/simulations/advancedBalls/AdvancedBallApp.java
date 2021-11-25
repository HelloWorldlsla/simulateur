package ch.collisionengine.simulations.advancedBalls;

import ch.collisionengine.maths.Vector;
import ch.collisionengine.simulator.App;
import ch.collisionengine.window.Drawable;

import javax.swing.*;
import java.awt.*;

public class AdvancedBallApp implements App {
    private final AdvancedBall a = new AdvancedBall(1, 1, new Vector(-10, 0.5), new Vector(5, 0));
    private final AdvancedBall b = new AdvancedBall(1, 1, new Vector(), new Vector());
    private final AdvancedBallEngine engine = new AdvancedBallEngine(a, b);
    private final JTextField d = new JTextField("0.5");
    private final JTextField mass1 = new JTextField("1");
    private final JTextField mass2 = new JTextField("1");
    private final JTextField velocity1 = new JTextField("5");
    private final JTextField velocity2 = new JTextField("0");
    private final JCheckBox centered = new JCheckBox();
    private final JLabel timeLabel = new JLabel("0");
    private double time = 0;

    @Override
    public void setup(JPanel toolBar) {
        toolBar.removeAll();
        toolBar.setLayout(new GridLayout(7, 2, 10, 10));
        toolBar.add(new JLabel("Masse A [kg]"));
        toolBar.add(mass1);
        toolBar.add(new JLabel("Vitesse A [m/s]"));
        toolBar.add(velocity1);
        toolBar.add(new JLabel("Masse B [kg]"));
        toolBar.add(mass2);
        toolBar.add(new JLabel("Vitesse B [m/s]"));
        toolBar.add(velocity2);
        toolBar.add(new JLabel("Distance d [m]"));
        toolBar.add(d);
        toolBar.add(new JLabel("Centrer au centre de masse"));
        toolBar.add(centered);
        toolBar.add(new JLabel("Temps [s]"));
        toolBar.add(timeLabel);
        toolBar.revalidate();
    }

    @Override
    public void reset() {
        engine.reset(centered.isSelected());
        a.position.set(-10, Double.parseDouble(d.getText()));
        a.velocity.set(Double.parseDouble(velocity1.getText()), 0);
        a.mass = Double.parseDouble(mass1.getText());
        b.position.set(0, 0);
        b.velocity.set(Double.parseDouble(velocity2.getText()), 0);
        b.mass = Double.parseDouble(mass2.getText());
        engine.d = Double.parseDouble(d.getText());
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
        return "Collision 2D";
    }
}
