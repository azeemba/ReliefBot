package tarehart.rlbot.intercept;

import tarehart.rlbot.time.Duration;

public class StrikeProfile {

    public enum Style {
        RAM,
        FLIP_HIT,
        SIDE_HIT,
        JUMP_HIT,
        AERIAL
    }

    /**
     * the extra approach time added by final maneuvers before striking the ball
     */
    public double maneuverSeconds;

    /**
     * The amount of speed potentially gained over the course of the strike's final stage (generally after driving over and lining up)
     */
    public double speedBoost;

    /**
     * The amount of time spent speeding up during the final stage
     */
    public double dodgeSeconds;

    public Style style;


    public StrikeProfile(double maneuverSeconds, double speedBoost, double dodgeSeconds, Style style) {
        this.maneuverSeconds = maneuverSeconds;
        this.speedBoost = speedBoost;
        this.dodgeSeconds = dodgeSeconds;
        this.style = style;
    }

    public StrikeProfile() {
        this(0, 0, 0, Style.RAM);
    }

    public Duration getTotalDuration() {
        return Duration.Companion.ofSeconds(maneuverSeconds + dodgeSeconds);
    }
}
