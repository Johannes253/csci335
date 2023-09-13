package maze.heuristics;

import core.Pos;
import maze.core.MazeExplorer;

import java.util.HashSet;
import java.util.Set;
import java.util.function.ToIntFunction;

public class EuclideantoclosestTreasure implements ToIntFunction<MazeExplorer> {
    @Override
    public int applyAsInt(MazeExplorer explorer) {
        Pos current = explorer.getLocation();
        Set<Pos> allTreasures = new HashSet<>(explorer.getAllTreasureFromMaze());
        Set<Pos> foundTreasures = explorer.getAllTreasureFound();
        allTreasures.removeAll(foundTreasures);

        double closestDistance = Double.MAX_VALUE;
        for (Pos treasure : allTreasures) {
            double dx = current.getX() - treasure.getX();
            double dy = current.getY() - treasure.getY();
            double distance = Math.sqrt(dx * dx + dy * dy);
            closestDistance = Math.min(closestDistance, distance);
        }

        return (int) closestDistance;
    }
}
