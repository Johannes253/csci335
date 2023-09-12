package maze.heuristics;

import maze.core.MazeExplorer;
import core.Pos;

import java.util.function.ToIntFunction;

public class ManhattanDistance implements ToIntFunction<MazeExplorer> {
    @Override
    public int applyAsInt(MazeExplorer explorer) {
        Pos current = explorer.getLocation();
        Pos goal = explorer.getGoal().getLocation();
        return Math.abs(current.getX() - goal.getX()) + Math.abs(current.getY() - goal.getY());
    }
}