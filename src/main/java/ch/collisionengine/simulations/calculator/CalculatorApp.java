package ch.collisionengine.simulations.calculator;

import ch.collisionengine.simulator.App;
import ch.collisionengine.simulator.Graph;
import ch.collisionengine.window.Drawable;

import javax.swing.*;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.List;

public class CalculatorApp implements App {

    private List<CalculatorEngine> engines;
    private JPanel toolBar;

    @Override
    public void setup(JPanel toolBar) {
        this.toolBar = toolBar;
        this.toolBar.removeAll();
        final JButton printData = new JButton("Print data");
        printData.addActionListener(e -> engines.forEach(calculatorEngine -> System.out.println(calculatorEngine.getAveragePressure())));
        this.toolBar.add(printData);

        toolBar.revalidate();
        engines = new ArrayList<>();
        for (int i = 0; i < 64; i++) {
            engines.add(new CalculatorEngine(i * 10));
        }
    }

    @Override
    public void reset() {
        setup(toolBar);
    }

    @Override
    public void update(double time) {
        engines.parallelStream().forEach(e -> e.update(time));
    }

    @Override
    public Drawable drawable() {
        return (g, sizeScale) -> {
            final AffineTransform transform = g.getTransform();
            final int colSize = (int) (engines.size() / Math.sqrt(engines.size()));
            final double scale = 32;
            final double engineSize = 20.0;

            final double tableHeight = -colSize * engineSize / scale * sizeScale;

            g.translate(-engines.size() * engineSize / scale / colSize * sizeScale * 2, tableHeight / 2);

            g.setFont(g.getFont().deriveFont(5.0f));
            bigLoop:
            for (int col = 0; ; col++) {
                g.translate(engineSize / scale * sizeScale * 2, 0);
                for (int row = 0; row < colSize; row++) {

                    if (col * colSize + row >= engines.size()) {
                        break bigLoop;
                    }

                    g.translate(0, engineSize / scale * sizeScale);
                    final CalculatorEngine engine = engines.get(col * colSize + row);
                    engine.draw(g, sizeScale / scale);
                }
                g.translate(0, tableHeight);
            }

            g.setTransform(transform);
            g.translate(2 * sizeScale, -2.5 * sizeScale);
            g.setFont(g.getFont().deriveFont(40.0f));
            final Graph graph = new Graph("Pression pour différentes températures", "Température [K]", "Pression [10¯²⁵N/m]", (Math.pow(10, -24) - Math.pow(10, -26)), 1);
            graph.verticalScale = 3 * Math.pow(10, 23);
            graph.horizontalScale = 1.0 / engines.size();
            engines.stream().mapToDouble(CalculatorEngine::getAveragePressure).forEach(graph.values::addLast);
            graph.draw(g, sizeScale / 2);

            g.setTransform(transform);
        };
    }

    @Override
    public String toString() {
        return "Calculator";
    }
}
