package tarehart.rlbot.planning

import tarehart.rlbot.bots.Team
import tarehart.rlbot.math.BallSlice
import tarehart.rlbot.math.Plane
import tarehart.rlbot.math.vector.Vector3
import tarehart.rlbot.physics.BallPath


abstract class Goal( negativeSide: Boolean) {

    abstract val center: Vector3
    abstract val scorePlane: Plane
    val team: Team = if (negativeSide) Team.BLUE else Team.ORANGE

    /**
     * From shooter's perspective
     */
    val leftPost: Vector3
        get() = getLeftPost(0F)

    /**
     * From shooter's perspective
     */
    val rightPost: Vector3
        get() = getRightPost(0F)

    abstract fun getNearestEntrance(ballPosition: Vector3, padding: Number): Vector3

    /**
     * From shooter's perspective
     */
    abstract fun getLeftPost(padding: Number): Vector3

    /**
     * From shooter's perspective
     */
    abstract fun getRightPost(padding: Number): Vector3

    abstract fun predictGoalEvent(ballPath: BallPath): BallSlice?

    abstract fun isGoalEvent(planeBreakLocation: Vector3): Boolean
}
