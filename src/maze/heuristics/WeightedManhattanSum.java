package maze.heuristics;

import core.Pos;
import maze.core.MazeExplorer;

import java.util.HashSet;
import java.util.Set;
import java.util.function.ToIntFunction;

public class WeightedManhattanSum implements ToIntFunction<MazeExplorer> {
    @Override
    public int applyAsInt(MazeExplorer explorer) {
        Pos current = explorer.getLocation();
        Set<Pos> allTreasures = new HashSet<>(explorer.getAllTreasureFromMaze());
        Set<Pos> foundTreasures = explorer.getAllTreasureFound();
        allTreasures.removeAll(foundTreasures);

        double totalWeightedDistance = 0;
        for (Pos treasure : allTreasures) {
            int distance = Math.abs(current.getX() - treasure.getX()) + Math.abs(current.getY() - treasure.getY());
            totalWeightedDistance += 1.0 / (1 + distance); // closer treasure higher value
        }

        return (int) -totalWeightedDistance;
    }
}

