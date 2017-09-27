package tarehart.rlbot.steps.defense;

import mikera.vectorz.Vector2;
import mikera.vectorz.Vector3;
import tarehart.rlbot.AgentInput;
import tarehart.rlbot.AgentOutput;
import tarehart.rlbot.input.CarData;
import tarehart.rlbot.math.SpaceTime;
import tarehart.rlbot.math.SpaceTimeVelocity;
import tarehart.rlbot.math.VectorUtil;
import tarehart.rlbot.physics.ArenaModel;
import tarehart.rlbot.physics.BallPath;
import tarehart.rlbot.physics.DistancePlot;
import tarehart.rlbot.planning.*;
import tarehart.rlbot.steps.Step;
import tarehart.rlbot.steps.strikes.DirectedSideHitStep;
import tarehart.rlbot.steps.strikes.InterceptStep;
import tarehart.rlbot.steps.strikes.KickAwayFromOwnGoal;
import tarehart.rlbot.tuning.BotLog;

import java.time.Duration;
import java.util.Optional;

public class WhatASaveStep implements Step {
    private Plan plan;
    private Double whichPost;

    @Override
    public Optional<AgentOutput> getOutput(AgentInput input) {

        if (plan != null && !plan.isComplete()) {
            return plan.getOutput(input);
        }

        CarData car = input.getMyCarData();
        BallPath ballPath = ArenaModel.predictBallPath(input, input.time, Duration.ofSeconds(5));
        Goal goal = GoalUtil.getOwnGoal(input.team);
        Optional<SpaceTimeVelocity> currentThreat = GoalUtil.predictGoalEvent(goal, ballPath);
        if (!currentThreat.isPresent()) {
            return Optional.empty();
        }

        SpaceTimeVelocity threat = currentThreat.get();

        if (whichPost == null) {

            Vector3 carToThreat = (Vector3) threat.space.subCopy(car.position);
            double carApproachVsBallApproach = SteerUtil.getCorrectionAngleRad(VectorUtil.flatten(carToThreat), VectorUtil.flatten(input.ballVelocity));
            // When carApproachVsBallApproach < 0, car is to the right of the ball, angle wise. Right is positive X when we're on the positive Y side of the field.
            whichPost = Math.signum(-carApproachVsBallApproach * threat.space.y);

        }

        double distance = VectorUtil.flatDistance(car.position, threat.getSpace());
        DistancePlot plot = AccelerationModel.simulateAcceleration(car, Duration.ofSeconds(5), car.boost, distance - 15);


        SpaceTime intercept = SteerUtil.getInterceptOpportunity(car, ballPath, plot).orElse(threat.toSpaceTime());

        Vector3 carToIntercept = (Vector3) intercept.space.subCopy(car.position);
        double carApproachVsBallApproach = SteerUtil.getCorrectionAngleRad(VectorUtil.flatten(carToIntercept), VectorUtil.flatten(input.ballVelocity));
        if (Math.abs(carApproachVsBallApproach) > Math.PI / 3) {

            if (Math.abs(SteerUtil.getCorrectionAngleRad(VectorUtil.flatten(car.orientation.noseVector), VectorUtil.flatten(carToIntercept))) < Math.PI / 12) {

                plan = new Plan(Plan.Posture.SAVE).withStep(new InterceptStep(new Vector3(0, Math.signum(goal.getCenter().y) * .7, 0)));
                plan.begin();
                return plan.getOutput(input);
            } else {
                SteerUtil.steerTowardGroundPosition(car, intercept.space);
            }
        }

        plan = new Plan().withStep(new DirectedSideHitStep(new KickAwayFromOwnGoal()));
        plan.begin();
        return plan.getOutput(input);
    }

    @Override
    public boolean isBlindlyComplete() {
        return false;
    }

    @Override
    public void begin() {

    }

    @Override
    public boolean canInterrupt() {
        return plan == null || plan.canInterrupt();
    }

    @Override
    public String getSituation() {
        return Plan.concatSituation("Making a save", plan);
    }
}