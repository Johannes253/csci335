package robosim.ai;

import robosim.core.*;
import robosim.reinforcement.QTable;

import java.util.Optional;

public class WontHitInator implements Controller {

    private QTable qTable;

    public WontHitInator() {
        int states = 100;
        int actions = Action.values().length;
        int startState = 0;
        int targetVisits = 50;
        int rateConstant = 5;
        double discount = 0.9;

        qTable = new QTable(states, actions, startState, targetVisits, rateConstant, discount);
    }

        private int getState(Simulator sim) {
        double distanceToEdge = sim.findClosestEdge();
        int edgeProximity;
        if (distanceToEdge < 10) {
            edgeProximity = 0;
        } else if (distanceToEdge < 30) {
            edgeProximity = 1;
        } else {
            edgeProximity = 2;
        }

        Optional<Polar> closestObstacle = sim.findClosestObstacle();
        int obstacleProximity;
        if (closestObstacle.isPresent()) {
            double distance = closestObstacle.get().getR();
            if (distance < 10) {
                obstacleProximity = 0;
            } else if (distance < 30) {
                obstacleProximity = 1;
            } else {
                obstacleProximity = 2;
            }
        } else {
            obstacleProximity = 3;
        }


        int motionState;
        if (sim.getAngularVelocity() == 0 && sim.getTranslationalVelocity() > 0) {
            motionState = 0;  // Moving forward
        } else if (sim.getAngularVelocity() != 0) {
            motionState = 1;  // Turning
        } else {
            motionState = 2;  // Stationary
        }

        int state = edgeProximity + obstacleProximity * 3 + motionState * 12;

    return state;
}
    @Override
    public void control(Simulator sim) {
        int currentState = getState(sim);

        double reward = 0;
        if(sim.wasHit()) {
            reward = -100;
        }
        else if(sim.getTranslationalVelocity() > 0) {
            reward = 1;
        }

        int nextActionIndex = qTable.senseActLearn(currentState, reward);
        Action nextAction = Action.values()[nextActionIndex];

        nextAction.applyTo(sim);
    }
}