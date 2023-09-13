package maze.heuristics;

import maze.core.MazeExplorer;
import core.Pos;

import java.util.Set;
import java.util.function.ToIntFunction;

//sum of the Manhattan distance to the treasures and goal
public class ManhattanSum implements ToIntFunction<MazeExplorer> {
    @Override
    public int applyAsInt(MazeExplorer explorer) {
        Pos current = explorer.getLocation();
        Set<Pos> allTreasures = explorer.getAllTreasureFromMaze();
        Set<Pos> foundTreasures = explorer.getAllTreasureFound();
        allTreasures.removeAll(foundTreasures);

        int totalDistance = 0;
        for (Pos treasure : allTreasures) {
            totalDistance += Math.abs(current.getX() - treasure.getX()) + Math.abs(current.getY() - treasure.getY());
        }

        Pos goal = explorer.getGoal().getLocation();
        totalDistance += Math.abs(current.getX() - goal.getX()) + Math.abs(current.getY() - goal.getY());

        return totalDistance;
    }
}
