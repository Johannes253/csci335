package robosim.ai;

import core.Duple;
import robosim.core.*;
import robosim.reinforcement.QTable;

import java.util.Comparator;
import java.util.Optional;

public class HouseCleanInator implements Controller {

    private QTable qTable;
    private Action[] recentActions = new Action[5];
    private int actionIndex = 0;

    public HouseCleanInator() {
        int states = 200;
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

        Optional<Polar> closestDirt = findClosestDirt(sim);
        int dirtProximity;
        if (closestDirt.isPresent()) {
            double distance = closestDirt.get().getR();
            if (distance < 10) {
                dirtProximity = 0;
            } else if (distance < 30) {
                dirtProximity = 1;
            } else {
                dirtProximity = 2;
            }
        } else {
            dirtProximity = 3;
        }



        int motionState;
        if (sim.getAngularVelocity() == 0 && sim.getTranslationalVelocity() > 0) {
            motionState = 0;  // Moving forward
        } else if (sim.getAngularVelocity() != 0) {
            motionState = 1;  // Turning
        } else {
            motionState = 2;  // Stationary
        }

        int state = edgeProximity + dirtProximity * 3 + obstacleProximity * 12 + motionState * 48;

        return state;
    }
    private int previousDirtCollected = 0;
    private double previousDirtDistance = Double.MAX_VALUE;

    @Override
    public void control(Simulator sim) {
        int currentState = getState(sim);

        double reward = 0;
        Optional<Polar> closestDirt = findClosestDirt(sim);
        double currentDirtDistance = closestDirt.isPresent() ? closestDirt.get().getR() : Double.MAX_VALUE;

        if(sim.wasHit()) {
            reward = -150;
        }
        else if(sim.getDirt() > previousDirtCollected) {
            reward = 150;
            previousDirtCollected ++;
        }
        else if(currentDirtDistance < previousDirtDistance) {
            reward = 5;
        }
        else if(currentDirtDistance > previousDirtDistance) {
            reward = -5;
        }
        else if(sim.getTranslationalVelocity() > 0) {
            reward = 1;
        }
        else if(sim.getAngularVelocity() == 0 && sim.getTranslationalVelocity() == 0) {
            reward = -5;
        }

        // Robot always starts oscillating, whatever rewards or punishments are
        if (isOscillating()) {
            reward = -30;
        }

        previousDirtDistance = currentDirtDistance;

        int nextActionIndex = qTable.senseActLearn(currentState, reward);

        Action nextAction = Action.values()[nextActionIndex];
        recordAction(nextAction);

        System.out.println("Reward: " +reward);

        nextAction.applyTo(sim);
    }

    private void recordAction(Action action) {
        recentActions[actionIndex] = action;
        actionIndex = (actionIndex + 1) % recentActions.length;
    }

    private boolean isOscillating() {
        int forwardCount = 0, backwardCount = 0;
        for (Action action : recentActions) {
            if (action == Action.FORWARD) forwardCount++;
            if (action == Action.BACKWARD) backwardCount++;
        }
        return forwardCount >= 2 && backwardCount >= 2;

    }
    public Optional<Polar> findClosestDirt(Simulator sim) {
        return sim.allVisibleObjects().stream()
                .filter(o -> o.getFirst() instanceof Dirt)
                .map(Duple::getSecond)
                .min(Comparator.comparingDouble(Polar::getR));
    }
}