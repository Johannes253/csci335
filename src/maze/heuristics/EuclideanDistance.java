package maze.heuristics;

import core.Pos;
import maze.core.MazeExplorer;

import java.util.function.ToIntFunction;

public class EuclideanDistance implements ToIntFunction<MazeExplorer> {
    @Override
    public int applyAsInt(MazeExplorer explorer) {
        Pos current = explorer.getLocation();
        Pos goal = explorer.getGoal().getLocation();
        int dx = current.getX() - goal.getX();
        int dy = current.getY() - goal.getY();
        return (int) Math.sqrt(dx * dx + dy * dy);
    }
}