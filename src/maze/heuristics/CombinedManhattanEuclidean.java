package maze.heuristics;

import core.Pos;
import maze.core.MazeExplorer;

import java.util.function.ToIntFunction;

public class CombinedManhattanEuclidean implements ToIntFunction<MazeExplorer> {
    @Override
    public int applyAsInt(MazeExplorer explorer) {
        Pos current = explorer.getLocation();
        Pos goal = explorer.getGoal().getLocation();

        int manhattan = Math.abs(current.getX() - goal.getX()) + Math.abs(current.getY() - goal.getY());

        double dx = current.getX() - goal.getX();
        double dy = current.getY() - goal.getY();
        double euclidean = Math.sqrt(dx * dx + dy * dy);

        // Average of both distances
        double combined = (manhattan + euclidean) / 2.0;

        return (int) combined;
    }
}

