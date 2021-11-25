package ch.collisionengine.simulations.multicalc;

import ch.collisionengine.simulations.calculator.CalculatorEngine;
import ch.collisionengine.simulator.Engine;
import ch.collisionengine.simulator.Graph;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MultiCalcEngine implements Engine {

    private final List<CalculatorEngine> engines = new ArrayList<>();

    public MultiCalcEngine(double width) {
        for (int i = 0; i < 32; i++) {
            engines.add(new CalculatorEngine(i * 10, width));
        }
    }

    @Override
    public void update(double time) {
        engines.forEach(c -> c.update(time));
    }

    @Override
    public void draw(Graphics2D g, double sizeScale) {
        g.setColor(Color.BLACK);//³
        final Graph graph = new Graph("Pression pour différentes températures et volumes", "Température [K]", "Pression [10¯²⁵N/m]", (Math.pow(10, -24) - Math.pow(10, -26)), 1);
        graph.verticalScale = 3 * Math.pow(10, 23);
        graph.horizontalScale = 1.0 / engines.size();
        engines.stream().mapToDouble(CalculatorEngine::getAveragePressure).forEach(graph.values::addLast);
        graph.draw(g, sizeScale);
    }
}
