package ch.collisionengine.simulations.diffusion;

import ch.collisionengine.simulator.App;
import ch.collisionengine.window.Drawable;

import javax.swing.*;
import java.awt.*;

public class DiffusionApp implements App {

    private final JTextField redMass = new JTextField("1");
    private final JTextField redRadius = new JTextField("0.5");
    private final JTextField redCount = new JTextField("10");
    private final JTextField redSpeed = new JTextField("20");
    private final JTextField blueMass = new JTextField("1");
    private final JTextField blueRadius = new JTextField("0.5");
    private final JTextField blueCount = new JTextField("100");
    private final JTextField blueSpeed = new JTextField("2");
    private DiffusionEngine engine = new DiffusionEngine(1, 0.5, 10, 20, 1, 0.5, 100, 2);
    private JPanel toolBar;

    @Override
    public void setup(JPanel toolBar) {
        this.toolBar = toolBar;
        this.toolBar.removeAll();
        toolBar.revalidate();
        toolBar.setLayout(new GridLayout(9, 2, 10, 10));

        final JButton wall = new JButton("Retirer le mur");
        toolBar.add(new JLabel());
        toolBar.add(wall);
        wall.addActionListener(e -> {
            engine.removeWall();
            wall.setVisible(false);
        });
        toolBar.add(new JLabel("Rayon des particules rouges"));
        toolBar.add(redRadius);
        toolBar.add(new JLabel("Masse des particules rouges"));
        toolBar.add(redMass);
        toolBar.add(new JLabel("Nombre des particules rouges"));
        toolBar.add(redCount);
        toolBar.add(new JLabel("Vitesse des particules rouges"));
        toolBar.add(redSpeed);
        toolBar.add(new JLabel("Rayon des particules bleues"));
        toolBar.add(blueRadius);
        toolBar.add(new JLabel("Masse des particules bleues"));
        toolBar.add(blueMass);
        toolBar.add(new JLabel("Nombre des particules bleues"));
        toolBar.add(blueCount);
        toolBar.add(new JLabel("Vitesse des particules bleues"));
        toolBar.add(blueSpeed);
    }

    @Override
    public void reset() {
        engine = new DiffusionEngine(
                Double.parseDouble(redMass.getText()), Double.parseDouble(redRadius.getText()), Integer.parseInt(redCount.getText()), Double.parseDouble(redSpeed.getText()),
                Double.parseDouble(blueMass.getText()), Double.parseDouble(blueRadius.getText()), Integer.parseInt(blueCount.getText()), Double.parseDouble(blueSpeed.getText()));
        setup(toolBar);
    }

    @Override
    public void update(double time) {
        engine.update(time);
    }

    @Override
    public Drawable drawable() {
        return ((g, sizeScale) -> engine.draw(g, sizeScale * 0.4));
    }

    @Override
    public String toString() {
        return "Diffusion";
    }
}
