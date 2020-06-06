package pwcg.mission.flight.objective;

import java.util.List;

import pwcg.campaign.api.Side;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.IFlightInformation;
import pwcg.mission.ground.org.IGroundUnit;
import pwcg.mission.ground.org.IGroundUnitCollection;
import pwcg.mission.ground.unittypes.staticunits.AirfieldTargetGroup;
import pwcg.mission.mcu.McuWaypoint;
import pwcg.mission.target.TargetType;

public class GroundAttackObjective
{
    static String getMissionObjective(IFlight flight) throws PWCGException 
    {
        IFlightInformation flightInformation = flight.getFlightInformation();
        
        String objective = "Attack the specified objective using all available means.";
        
        List<McuWaypoint> targetWaypoints = flight.getWaypointPackage().getTargetWaypoints();
        if (!targetWaypoints.isEmpty())
        {
            Coordinate flightTargetPosition = targetWaypoints.get(0).getPosition();
            Side enemySide = flight.getSquadron().determineEnemySide();
            IGroundUnitCollection groundUnitCollection = flight.getMission().getMissionGroundUnitBuilder().getClosestGroundUnitForSide(flightTargetPosition, enemySide);
            for (IGroundUnit groundUnit : groundUnitCollection.getGroundUnits())
            {
                if (!groundUnit.getCountry().isSameSide(flight.getFlightInformation().getCountry()))
                {
                    objective = getObjectiveFromEnemyUnit(groundUnit, flightInformation);
                    break;
                }
            }
        }
        return objective;
    }

    private static String getObjectiveFromEnemyUnit(IGroundUnit enemyGroundUnit, IFlightInformation flightInformation) throws PWCGException
    {
        String objectiveLocation =  MissionObjective.getMissionObjectiveLocation(
                flightInformation.getSquadron(), flightInformation.getCampaign().getDate(), enemyGroundUnit);
        
        String objective = "Attack the specified objective using all available means.";
        TargetType targetType = enemyGroundUnit.getTargetType();
        
        
        if (targetType == TargetType.TARGET_INFANTRY)
        {
            objective = "Attack enemy troops" + objectiveLocation; 
        }
        else if (targetType == TargetType.TARGET_INFANTRY)
        {
            objective = "Attack enemy troops" + objectiveLocation; 
        }
        else if (targetType == TargetType.TARGET_AIRFIELD)
        {
            AirfieldTargetGroup target = (AirfieldTargetGroup)enemyGroundUnit;
            objective = "Attack the airfield at " + target.getAirfield().getName();
        }
        else if (targetType == TargetType.TARGET_TRAIN)
        {
            objective = "Attack the trains and rail facilities" + objectiveLocation;
        }
        else if (targetType == TargetType.TARGET_TRANSPORT)
        {
            objective = "Attack the transport and road facilities" + objectiveLocation; 
        }
        else if (targetType == TargetType.TARGET_SHIPPING)
        {
            objective = "Attack the shipping" + objectiveLocation; 
        }
        else if (targetType == TargetType.TARGET_DRIFTER)
        {
            objective = "Attack the light shipping" + objectiveLocation; 
        }
        else
        {
            objective = "Target" + objectiveLocation; 
        }
        return objective;

    }
}
