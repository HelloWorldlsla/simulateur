package ch.collisionengine.simulations.bullets;

import ch.collisionengine.simulator.App;
import ch.collisionengine.window.Drawable;

import javax.swing.*;

public class BulletsApp implements App {

    private final JTextField mass = new JTextField("1");
    private final JTextField velocity = new JTextField("5");
    private final JTextField radius = new JTextField("1");
    private final JTextField count = new JTextField("20");
    private final JLabel timeLabel = new JLabel("0");
    private BulletEngine engine = new BulletEngine(1, 5, 1, 20);
    private double time = 0;

    @Override
    public void setup(JPanel toolBar) {
        toolBar.removeAll();
        toolBar.add(new JLabel("Masse [kg]"));
        toolBar.add(mass);
        toolBar.add(new JLabel("Vitesse [m/s]"));
        toolBar.add(velocity);
        toolBar.add(new JLabel("Rayon [m]"));
        toolBar.add(radius);
        toolBar.add(new JLabel("Nombre de particules"));
        toolBar.add(count);
        toolBar.add(new JLabel("Temps [s]"));
        toolBar.add(timeLabel);
        toolBar.revalidate();
    }

    @Override
    public void reset() {
        time = 0;
        timeLabel.setText("0");
        engine = new BulletEngine(Double.parseDouble(mass.getText()), Double.parseDouble(velocity.getText()), Double.parseDouble(radius.getText()), Integer.parseInt(count.getText()));
    }

    @Override
    public void update(double time) {
        this.time += time;
        timeLabel.setText(String.valueOf((int) (this.time * 1000) / 1000.0));
        engine.update(time);
    }

    @Override
    public Drawable drawable() {
        return ((g, sizeScale) -> engine.draw(g, sizeScale * 0.4));
    }

    @Override
    public String toString() {
        return "Particules";
    }
}
