package maze.heuristics;

import core.Pos;
import maze.core.MazeExplorer;

import java.util.HashSet;
import java.util.Set;
import java.util.function.ToIntFunction;

public class ManhattanDistanceToClosestTreasure implements ToIntFunction<MazeExplorer> {
    @Override
    public int applyAsInt(MazeExplorer explorer) {

        Pos current = explorer.getLocation();

        Set<Pos> treasures = new HashSet<>(explorer.getAllTreasureFromMaze());
        treasures.removeAll(explorer.getAllTreasureFound());

        int minmanDistance = Integer.MAX_VALUE;

        if (!treasures.isEmpty()) {

            for (Pos treasure : treasures) {
                int manhattandistance = Math.abs(current.getX() - treasure.getX()) + Math.abs(current.getY() - treasure.getY());
                if (manhattandistance < minmanDistance)
                    minmanDistance = manhattandistance;

            }
        } else {
            Pos goal = explorer.getGoal().getLocation();
            return Math.abs(current.getX() - goal.getX()) + Math.abs(current.getY() - goal.getY());
        }


        return minmanDistance;
    }
}
