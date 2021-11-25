package ch.collisionengine.simulations.fixedBullets;

import ch.collisionengine.simulations.fixedBullets.FixedParticleEngine.EngineSettings;
import ch.collisionengine.simulator.App;
import ch.collisionengine.simulator.SimpleGrid;
import ch.collisionengine.window.Drawable;

import javax.swing.*;

import java.awt.*;

import static java.awt.Color.RED;

public class FixedBulletsApp implements App {
    private final JTextField mass = new JTextField("1");
    private final JTextField velocity = new JTextField("5");
    private final JTextField radius = new JTextField("1");
    private final JTextField count = new JTextField("20");
    private final JLabel timeLabel = new JLabel("0");
    private FixedParticleEngine engine = new FixedParticleEngine(20, 20, 20,
            new EngineSettings(1, 1, 5, RED));
    private double time = 0;

    @Override
    public void setup(JPanel toolBar) {
        toolBar.removeAll();
        toolBar.setLayout(new GridLayout(5, 2, 10, 10));
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

        engine = new FixedParticleEngine(20, 20, Integer.parseInt(count.getText()),
                new EngineSettings(Double.parseDouble(radius.getText()), Double.parseDouble(mass.getText()), Double.parseDouble(velocity.getText()), RED));
    }

    @Override
    public void update(double time) {
        this.time += time;
        timeLabel.setText(String.valueOf((int) (this.time * 1000) / 1000.0));
        engine.update(time);
    }

    @Override
    public Drawable drawable() {
        return ((g, sizeScale) -> {
            new SimpleGrid(20).draw(g, sizeScale * 0.4);
            g.translate(-4 * sizeScale, -4 * sizeScale);
            engine.draw(g, sizeScale * 0.4);
        });
    }

    @Override
    public String toString() {
        return "Particules avec fix";
    }
}
