package ch.collisionengine.window.time;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.atomic.AtomicBoolean;

import static java.awt.event.ItemEvent.SELECTED;

public class TimeBar extends JToolBar {

    private final Playable playable;
    private final AtomicBoolean running = new AtomicBoolean(false);
    private final Object sync = new Object();
    private final JButton startStopButton;
    private Thread thread;
    private double speedFactor = 1f;

    public TimeBar(Playable playable) {
        super(HORIZONTAL);
        setFloatable(false);
        setBackground(Color.WHITE);
        this.playable = playable;
        add(new JLabel("Speed factor:"));
        final ButtonGroup group = new ButtonGroup();
        addSpeedFactorButton(0.25f, group);
        addSpeedFactorButton(0.5f, group);
        addSpeedFactorButton(1f, group);
        addSpeedFactorButton(2f, group);
        addSpeedFactorButton(4f, group);
        addSpeedFactorButton(8f, group);

        final JButton resetButton = new JButton(FontAwesome.REPLAY_ICON);
        startStopButton = new JButton(FontAwesome.PAUSE_ICON);
        startStopButton.setBackground(Color.WHITE);
        startStopButton.setBorderPainted(false);
        startStopButton.setFocusPainted(false);
        startStopButton.setFont(FontAwesome.font);

        resetButton.setBackground(Color.WHITE);
        resetButton.setBorderPainted(false);
        resetButton.setFocusPainted(false);
        resetButton.setFont(FontAwesome.font);
        resetButton.addActionListener(e -> playable.reset());

        startStopButton.addActionListener(e -> new Thread(this::toggle).start());

        add(startStopButton);
        add(resetButton);
    }

    private void addSpeedFactorButton(double factor, ButtonGroup group) {
        final JRadioButton button = new JRadioButton(factor + "x");
        group.add(button);
        add(button);
        button.setBackground(Color.WHITE);
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.addItemListener(e -> {
            button.setBackground(e.getStateChange() == SELECTED ? Color.GRAY : getBackground());
            if (e.getStateChange() == SELECTED) {
                this.speedFactor = factor;
            }
        });
        button.setSelected(factor == 1f);
        button.setIconTextGap(0);
        button.setIcon(new ImageIcon());
        button.setRolloverIcon(new ImageIcon());
        button.setSelectedIcon(new ImageIcon());
    }

    public void stopAnimation() {
        running.set(false);
        try {
            if (thread != null) {
                thread.join();
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void startAnimation() {
        running.set(true);
        thread = new Thread(() -> {
            try {
                run();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        thread.setDaemon(true);
        thread.start();
    }

    private void run() throws InterruptedException {
        while (running.get()) {
            final double baseTime = 0.008f;
            playable.update(baseTime * speedFactor);
            Thread.sleep(8);
        }
    }

    public void toggle() {
        synchronized (sync) {
            if (running.get()) {
                running.set(false);
                stopAnimation();
                startStopButton.setText(FontAwesome.PLAY_ICON);
            } else {
                startAnimation();
                startStopButton.setText(FontAwesome.PAUSE_ICON);
            }
        }
    }
}
