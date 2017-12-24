package tarehart.rlbot.steps.strikes;

import tarehart.rlbot.AgentInput;
import tarehart.rlbot.input.CarData;
import tarehart.rlbot.math.BallSlice;
import tarehart.rlbot.math.SpaceTime;
import tarehart.rlbot.math.VectorUtil;
import tarehart.rlbot.math.vector.Vector2;
import tarehart.rlbot.math.vector.Vector3;
import tarehart.rlbot.physics.ArenaModel;
import tarehart.rlbot.planning.*;
import tarehart.rlbot.time.Duration;
import tarehart.rlbot.tuning.ManeuverMath;

import java.util.Optional;
import java.util.function.BiPredicate;

public class DirectedKickUtil {
    private static final double BALL_VELOCITY_INFLUENCE = .3;

    public static Optional<DirectedKickPlan> planKick(AgentInput input, KickStrategy kickStrategy, boolean isSideHit) {
        Vector3 interceptModifier = kickStrategy.getKickDirection(input).normaliseCopy().scaled(-2);
        StrikeProfile strikeProfile = new StrikeProfile(.5, 0, 0);
        return planKick(input, kickStrategy, isSideHit, interceptModifier, strikeProfile);
    }

    static Optional<DirectedKickPlan> planKick(AgentInput input, KickStrategy kickStrategy, boolean isSideHit, Vector3 interceptModifier, StrikeProfile strikeProfile) {
        final DirectedKickPlan kickPlan = new DirectedKickPlan();
        kickPlan.interceptModifier = interceptModifier;

        CarData car = input.getMyCarData();

        kickPlan.ballPath = ArenaModel.predictBallPath(input);
        kickPlan.distancePlot = AccelerationModel.simulateAcceleration(car, Duration.ofSeconds(6), car.boost);


        BiPredicate<CarData, SpaceTime> verticalPredicate = isSideHit ? AirTouchPlanner::isJumpHitAccessible : AirTouchPlanner::isVerticallyAccessible;
        BiPredicate<CarData, SpaceTime> overallPredicate = (cd, st) -> verticalPredicate.test(cd, st) && kickStrategy.looksViable(cd, st.space);

        Optional<Intercept> interceptOpportunity = SteerUtil.getFilteredInterceptOpportunity(
                car, kickPlan.ballPath, kickPlan.distancePlot, interceptModifier,
                overallPredicate, (space) -> strikeProfile);
        Optional<BallSlice> ballMotion = interceptOpportunity.flatMap(inter -> kickPlan.ballPath.getMotionAt(inter.getTime()));

        if (!ballMotion.isPresent() || !interceptOpportunity.isPresent()) {
            return Optional.empty();
        }
        kickPlan.ballAtIntercept = ballMotion.get();

        double secondsTillImpactRoughly = Duration.between(input.time, kickPlan.ballAtIntercept.getTime()).getSeconds();
        double impactSpeed = isSideHit ? ManeuverMath.DODGE_SPEED :
                kickPlan.distancePlot.getMotionAfterDuration(secondsTillImpactRoughly).map(dts -> dts.speed).orElse(AccelerationModel.SUPERSONIC_SPEED);

        Vector3 easyForce;
        if (isSideHit) {
            Vector2 carToIntercept = interceptOpportunity.get().getSpace().minus(car.position).flatten();
            Vector2 sideHit = VectorUtil.orthogonal(carToIntercept, v -> v.dotProduct(interceptModifier.flatten()) < 0);
            easyForce = new Vector3(sideHit.x, sideHit.y, 0).scaledToMagnitude(impactSpeed);
        } else {
            easyForce = kickPlan.ballAtIntercept.getSpace().minus(car.position).scaledToMagnitude(impactSpeed);
        }

        Vector3 easyKick = bump(kickPlan.ballAtIntercept.getVelocity(), easyForce);
        Vector3 kickDirection = kickStrategy.getKickDirection(input, kickPlan.ballAtIntercept.getSpace(), easyKick);

        if (easyKick.x == kickDirection.x && easyKick.y == kickDirection.y) {
            // The kick strategy is fine with the easy kick.
            kickPlan.plannedKickForce = easyForce;
            kickPlan.desiredBallVelocity = easyKick;
        } else {

            // TODO: this is a rough approximation.
            Vector2 orthogonal = VectorUtil.orthogonal(kickDirection.flatten());
            Vector2 transverseBallVelocity = VectorUtil.project(kickPlan.ballAtIntercept.getVelocity().flatten(), orthogonal);
            kickPlan.desiredBallVelocity = kickDirection.normaliseCopy().scaled(impactSpeed + transverseBallVelocity.magnitude() * .7);
            kickPlan.plannedKickForce = new Vector3(
                    kickPlan.desiredBallVelocity.x - transverseBallVelocity.x * BALL_VELOCITY_INFLUENCE,
                    kickPlan.desiredBallVelocity.y - transverseBallVelocity.y * BALL_VELOCITY_INFLUENCE,
                    kickPlan.desiredBallVelocity.z);
        }

        if (!isSideHit) {
            double backoff = 3 + kickPlan.ballAtIntercept.getSpace().z;
            kickPlan.launchPad = kickPlan.ballAtIntercept.getSpace().flatten().minus(kickPlan.plannedKickForce.flatten().scaledToMagnitude(backoff));
        }

        return Optional.of(kickPlan);
    }


    /**
     * https://math.stackexchange.com/questions/13261/how-to-get-a-reflection-vector
     */
    private static Vector3 reflect(Vector3 incident, Vector3 normal) {
        normal = normal.normaliseCopy();
        return incident.minus(normal.scaled(2 * incident.dotProduct(normal)));
    }

    private static Vector3 bump(Vector3 incident, Vector3 movingWall) {
        // Move into reference frame of moving wall
        Vector3 incidentAccordingToWall = incident.minus(movingWall);
        Vector3 reflectionAccordingToWall = reflect(incidentAccordingToWall, movingWall);
        return reflectionAccordingToWall.plus(movingWall);
    }


    static double getAngleOfKickFromApproach(CarData car, DirectedKickPlan kickPlan) {
        Vector2 strikeForceFlat = kickPlan.plannedKickForce.flatten();
        Vector3 carPositionAtIntercept = kickPlan.getCarPositionAtIntercept();
        Vector2 carToIntercept = carPositionAtIntercept.minus(car.position).flatten();
        return carToIntercept.correctionAngle(strikeForceFlat);
    }
}
