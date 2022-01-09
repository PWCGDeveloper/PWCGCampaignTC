    package pwcg.product.bos.config;

import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.ground.BattleSize;

public class TCProductSpecificConfiguration
{
    private static final int INNER_LOOP_DISTANCE = 15000;
    private static final int BALLOON_DEFENSE_LOOP_DISTANCE = 20000;
    private static final int CLIMB_DISTANCE = 10000;
    private static final int NEUTRAL_ZONE = 5000;
    private static final int MIN_CLIMB_WP_ALT = 2000;
    private static final int CROSS_DIAMETER = 25000;
    private static final int CREEPING_LINE_LENGTH = 15000;
    private static final int CREEPING_LINE_CROSS = 8000;
    private static final int CLOSE_TO_FRONT_DISTANCE = 80000;
    private static final int MAX_DISTANCE_FROM_PLAYER_BOX = 60000;
    private static final int MISSION_RADIUS_LARGE = 100000;
    private static final int MISSION_RADIUS_MEDIUM = 50000;
    private static final int MISSION_RADIUS_SMALL = 30000;
    private static final int MISSION_RADIUS_VERY_SMALL = 10000;
    private static final int AIR_START_MAX_DISTANCE_FROM_INGRESS = 60000;
    private static final int INITIAL_WAYPOINT_ALTITUDE = 1500;
    private static final int GROUND_ATTACK_INGRESS_DISTANCE = 10000;
    private static final int AIRCRAFT_SPACING_HORIZONTAL = 200;
    private static final int AIRCRAFT_SPACING_VERTICAL = 100;
    private static final int TAKEOFF_SPACING = 35;
    private static final int ADDITIONAL_ALTITUDE_FOR_ESCORT = 800;
    private static final int INGRESS_AT_TARGET_MIN_DISTANCE = 4000;
    private static final int INGRESS_AT_TARGET_MAX_DISTANCE = 15000;  
    private static final int ADDITIONAL_RENDEZVOUS_DISTANCE_FROM_FRONT = 15000;
    private static final int ATTACK_AREA_SELECT_TARGET_DISTANCE = 5000;
    private static final int ATTACK_AREA_BOMB_DROP_DISTANCE = 1000;
    private static final int INGRESS_DISTANCE_FROM_FRONT = 10000;
    private static final int FORMATION_HORIZINTAL_SPACING = 200;
    private static final int FORMATION_VERTICAL_SPACING = 120;
    private static final int AIRFIELD_GO_AWAY_DISTANCE = 40000;
    private static final int VWP_SEPARATION_DISTANCE = 10000;
    private static final int VWP_PROXIMITY_TO_BOX_DISTANCE = 5000;
    private static final int VWP_PROXIMITY_TO_FRONT_DISTANCE = 15000;
    private static final int MAX_DEPTH_OF_PENETRATION_PATROL = 10000;
    private static final int MIN_DEPTH_OF_PENETRATION_OFFENSIVE = 10000;
    private static final int MAX_DEPTH_OF_PENETRATION_OFFENSIVE = 50000;
    private static final int MIN_DISTANCE_BETWEEN_PATROL_POINTS = 10000;
    


    public boolean useWaypointGoal()
    {
        return false;
    }


    public boolean usePlaneDir()
    {
        return true;
    }


    public boolean useFlagDir()
    {
        return true;
    }


    public boolean useCallSign()
    {
        return true;
    }


    public boolean usePosition1()
    {
        return true;
    }


    public int geNeutralZone()
    {
        return NEUTRAL_ZONE;
    }
    

    public int getClimbDistance()
    {
        return CLIMB_DISTANCE;
    }


    public int getMinClimbWPAlt()
    {
        return MIN_CLIMB_WP_ALT;
    }
    

    public int getAdditionalInitialTargetRadius(FlightTypes flightType)
    {
        int initialDistance = 0;
        if (flightType == FlightTypes.PARATROOP_DROP)
        {
            initialDistance = 5000;
        }
        else if (flightType == FlightTypes.CARGO_DROP)
        {
            initialDistance = 5000;
        }

        return initialDistance;
    }
    

    public int getAdditionalMaxTargetRadius(FlightTypes flightType)
    {
        int initialDistance = 20000;
        if (flightType == FlightTypes.DIVE_BOMB)
        {
            initialDistance = 30000;
        }
        else if (flightType == FlightTypes.PARATROOP_DROP)
        {
            initialDistance = 30000;
        }

        return initialDistance;
    }


    public int getInterceptInnerLoopDistance()
    {
        return INNER_LOOP_DISTANCE;
    }


    public int getNumAssaultSegments(BattleSize battleSize)
    {
        if (battleSize == BattleSize.BATTLE_SIZE_ASSAULT)
        {
            return (1 + RandomNumberGenerator.getRandom(3));
        }
        else if (battleSize == BattleSize.BATTLE_SIZE_OFFENSIVE)
        {
            return (1 + RandomNumberGenerator.getRandom(5));
        }
        else
        {
            return 1;
        }
    }


    public int getInterceptCrossDiameterDistance()
    {
        return CROSS_DIAMETER;
    }


    public int getInterceptCreepLegDistance()
    {
        return CREEPING_LINE_LENGTH;
    }


    public int getInterceptCreepCrossDistance()
    {
        return CREEPING_LINE_CROSS;
    }


    public int getCloseToFrontDistance()
    {
        return CLOSE_TO_FRONT_DISTANCE;
    }


    public int getMaxDistanceForVirtualFlightFromPlayerBox()
    {
        return MAX_DISTANCE_FROM_PLAYER_BOX;
    }


    public int getLargeMissionRadius()
    {
        return MISSION_RADIUS_LARGE;
    }


    public int getMediumMissionRadius()
    {
        return MISSION_RADIUS_MEDIUM;
    }


    public int getSmallMissionRadius()
    {
        return MISSION_RADIUS_SMALL;
    }


    public int getVerySmallMissionRadius()
    {
        return MISSION_RADIUS_VERY_SMALL;
    }


    public int getMaxDistanceForVirtualFlightAirStart()
    {
        return AIR_START_MAX_DISTANCE_FROM_INGRESS;
    }


    public int getInitialWaypointAltitude()
    {
        return INITIAL_WAYPOINT_ALTITUDE;
    }


    public int getGroundAttackIngressDistance()
    {
        return GROUND_ATTACK_INGRESS_DISTANCE;
    }


    public int getAircraftSpacingHorizontal()
    {
        return AIRCRAFT_SPACING_HORIZONTAL;
    }


    public int getAircraftSpacingVertical()
    {
        return AIRCRAFT_SPACING_VERTICAL;
    }


    public int getTakeoffSpacing()
    {
        return TAKEOFF_SPACING;
    }


    public int getAdditionalAltitudeForEscort()
    {
        return ADDITIONAL_ALTITUDE_FOR_ESCORT;
    }


    public int getIngressAtTargetMinDIstance()
    {
        return INGRESS_AT_TARGET_MIN_DISTANCE;
    }


    public int getIngressAtTargetMaxDIstance()
    {
        return INGRESS_AT_TARGET_MAX_DISTANCE;
    }


    public int getAttackAreaSelectTargetRadius()
    {
        return ATTACK_AREA_SELECT_TARGET_DISTANCE;
    }


    public int getRendezvousDistanceFromFront()
    {
        return getAttackAreaSelectTargetRadius() + ADDITIONAL_RENDEZVOUS_DISTANCE_FROM_FRONT;
    }


    public int getAttackAreaTriggerRadius()
    {
        return getAttackAreaSelectTargetRadius() + 2000;
    }


    public int getBombApproachDistance()
    {
        return getBombFinalApproachDistance() + 8000;
    }


    public int getBombFinalApproachDistance()
    {
        return getAttackAreaTriggerRadius() - 1000 ;
    }


    public int getAttackAreaBombDropRadius()
    {
        return ATTACK_AREA_BOMB_DROP_DISTANCE;
    }


    public int getDefaultIngressDistanceFromFront()
    {
        return INGRESS_DISTANCE_FROM_FRONT;
    }
    

    public int getFormationHorizontalSpacing()
    {
        return FORMATION_HORIZINTAL_SPACING;
    }


    public int getFormationVerticalSpacing()
    {
        return FORMATION_VERTICAL_SPACING;
    }


    public int getBalloonDefenseLoopDistance()
    {
        return BALLOON_DEFENSE_LOOP_DISTANCE;
    }


    public int getAirfieldGoAwayDistance()
    {
        return AIRFIELD_GO_AWAY_DISTANCE;
    }    


    public int getVwpSeparationDistance()
    {
        return VWP_SEPARATION_DISTANCE;
    }


    public int getVwpProximityToBoxDistance()
    {
        return VWP_PROXIMITY_TO_BOX_DISTANCE;
    }


    public int getVwpProximityToFrontDistance()
    {
        return VWP_PROXIMITY_TO_FRONT_DISTANCE;
    }


    public int getMaxDepthOfPenetrationPatrol()
    {
        return MAX_DEPTH_OF_PENETRATION_PATROL;
    }


    public int getMaxDepthOfPenetrationOffensive()
    {
        return MAX_DEPTH_OF_PENETRATION_OFFENSIVE;
    }


    public int getMinDepthOfPenetrationOffensive()
    {
        return MIN_DEPTH_OF_PENETRATION_OFFENSIVE;
    }


    public double getMinimumDistanceBetweenPatrolPoints()
    {
        return MIN_DISTANCE_BETWEEN_PATROL_POINTS;
    }
}
