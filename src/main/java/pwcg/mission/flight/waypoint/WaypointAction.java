package pwcg.mission.flight.waypoint;

public enum WaypointAction
{
	WP_ACTION_START("Start", false, true),
	WP_ACTION_TAKEOFF("Take Off", false, true),

	WP_ACTION_INGRESS("Ingress", true, true),
	WP_ACTION_EGRESS("Egress", true, false),
	
	WP_ACTION_PATROL("Patrol", true, false),

	WP_ACTION_TARGET_APPROACH("Target Approach", true, true),
	WP_ACTION_TARGET_FINAL("Target Final", false, true),
	WP_ACTION_TARGET_EGRESS("Target Egress", true, false),
	
    WP_ACTION_ATTACK("Attack", false, true),
	WP_ACTION_MOVE_TO("Move To", false, false);

    private String waypointAction;
    private boolean editable;
    private boolean isBeforeTarget;
	
    private WaypointAction(String waypointAction, boolean editable, boolean isBeforeTarget) 
    {
        this.waypointAction = waypointAction;
        this.editable = editable;
        this.isBeforeTarget = isBeforeTarget;
    }

    public String getAction() 
    {
        return waypointAction;
    }

    public boolean isEditable()
    {
        return editable;
    }

    public boolean isBeforeTarget()
    {
        return isBeforeTarget;
    }
}
