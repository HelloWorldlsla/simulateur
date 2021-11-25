package ch.collisionengine.window;

import ch.collisionengine.simulations.advancedBalls.AdvancedBallApp;
import ch.collisionengine.simulations.balls.BallApp;
import ch.collisionengine.simulations.bullets.BulletsApp;
import ch.collisionengine.simulations.calculator.CalculatorApp;
import ch.collisionengine.simulations.diffusion.DiffusionApp;
import ch.collisionengine.simulations.fixedBullets.FixedBulletsApp;
import ch.collisionengine.simulations.multicalc.MultiCalcApp;
import ch.collisionengine.simulations.multivol.MultiVolumeApp;
import ch.collisionengine.simulations.speed.SpeedApp;
import ch.collisionengine.simulations.volume.VolumeApp;
import ch.collisionengine.simulator.App;
import ch.collisionengine.window.time.Playable;
import ch.collisionengine.window.time.TimeBar;

import javax.swing.*;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.FontUIResource;
import java.awt.*;
import java.util.Enumeration;

import static java.awt.BorderLayout.*;

public class Main extends JFrame implements Playable {

    private static final double SIZE_SCALE = 100;
    private final JPanel toolBar = new JPanel();
    private final TimeBar timeBar = new TimeBar(this);
    private final App[] apps = new App[]{
            new BallApp(),
            new AdvancedBallApp(),
            new BulletsApp(),
            new FixedBulletsApp(),
            new DiffusionApp(),
            new CalculatorApp(),
            new MultiCalcApp(),
            new VolumeApp(),
            new MultiVolumeApp(),
            new SpeedApp()
    };
    private Renderer renderer = null;
    private App app = null;

    private Main() {
        super("Collision engine");
        setSize(1000, 800);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        final JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new OverlayLayout(mainPanel));
        add(mainPanel);

        mainPanel.add(overlay());

        final JPanel borderPanel = new JPanel(new BorderLayout());
        borderPanel.add(renderer, CENTER);
        borderPanel.add(timeBar, SOUTH);
        mainPanel.add(borderPanel);
    }

    public static void setUIFont(FontUIResource f) {
        final Enumeration<Object> keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            final Object key = keys.nextElement();
            final Object value = UIManager.get(key);
            if (value instanceof FontUIResource) UIManager.put(key, f);
        }
    }

    public static void main(String[] args) {
        setUIFont(new FontUIResource("arial", Font.PLAIN, 24));
        UIManager.put("ComboBox.selectionBackground", new ColorUIResource(Color.LIGHT_GRAY));
        new Main().start();
    }

    private JPanel overlay() {
        final JPanel overlay = new JPanel(new BorderLayout());
        overlay.setOpaque(false);

        final JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        final JComboBox<App> appChooser = new JComboBox<>(apps);
        appChooser.addItemListener(e -> {
            final App item = (App) e.getItem();
            synchronized (renderer.sync) {
                item.setup(toolBar);
                app = item;
                renderer.setDrawable(app.drawable());
            }
        });
        appChooser.setBackground(Color.WHITE);
        appChooser.setPreferredSize(new Dimension(200, 50));
        app = appChooser.getItemAt(0);
        renderer = new Renderer(app.drawable(), SIZE_SCALE);
        panel.add(appChooser);
        panel.add(toolBar);

        final JPanel p = new JPanel();
        p.setOpaque(false);
        p.add(panel);

        overlay.add(p, EAST);
        return overlay;
    }

    private void start() {
        app.setup(toolBar);
        setVisible(true);
        timeBar.startAnimation();
    }

    @Override
    public void reset() {
        synchronized (renderer.sync) {
            app.reset();
            renderer.setDrawable(app.drawable());
        }
    }

    @Override
    public void update(double time) {
        synchronized (renderer.sync) {
//            for (int i = 0; i < 20; i++) {
//                app.update(time);
//            }
            app.update(time);
//            app.update(time);
//            app.update(time);
//            app.update(time);
            toolBar.repaint();
            repaint();
            getToolkit().sync();
        }
    }
}