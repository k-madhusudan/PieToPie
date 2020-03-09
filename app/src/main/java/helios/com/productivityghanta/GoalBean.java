package helios.com.productivityghanta;

public class GoalBean {
    String activityName;
    String actualMinutes;
    String from;
    String goalName;
    String minutes;
    String to;

    public String getActualMinutes() {
        return this.actualMinutes;
    }

    public void setActualMinutes(String actualMinutes) {
        this.actualMinutes = actualMinutes;
    }

    public String getActivityName() {
        return this.activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public String getGoalName() {
        return this.goalName;
    }

    public void setGoalName(String goalName) {
        this.goalName = goalName;
    }

    public String getFrom() {
        return this.from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return this.to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getMinutes() {
        return this.minutes;
    }

    public void setMinutes(String minutes) {
        this.minutes = minutes;
    }
}
