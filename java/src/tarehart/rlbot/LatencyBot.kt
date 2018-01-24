package tarehart.rlbot

import tarehart.rlbot.math.VectorUtil
import tarehart.rlbot.math.vector.Vector3
import tarehart.rlbot.planning.Plan
import tarehart.rlbot.planning.SteerUtil
import tarehart.rlbot.steps.debug.CalibrateStep

class LatencyBot(team: Bot.Team, playerIndex: Int) : Bot(team, playerIndex) {

    private var hasJumped: Boolean = false

    override fun getOutput(input: AgentInput): AgentOutput {

        if (!hasJumped) {
            if (VectorUtil.flatDistance(input.ballPosition, Vector3()) > 0) {
                hasJumped = true
                return AgentOutput().withJump()
            }

            val car = input.myCarData
            return SteerUtil.steerTowardGroundPosition(car, input.ballPosition.plus(Vector3(20.0, 0.0, 0.0)))
        }

        if (noActivePlanWithPosture(Plan.Posture.NEUTRAL)) {
            currentPlan = Plan().withStep(CalibrateStep())
        }

        return currentPlan?.getOutput(input) ?: AgentOutput()
    }
}
