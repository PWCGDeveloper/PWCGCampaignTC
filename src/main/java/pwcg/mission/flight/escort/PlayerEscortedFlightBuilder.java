package pwcg.mission.flight.escort;

import pwcg.campaign.context.FrontLinesForMap;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.mission.MissionBeginUnit;
import pwcg.mission.flight.AttackMcuSequence;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.flight.FlightInformationFactory;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.bomb.BombingFlight;

public class PlayerEscortedFlightBuilder
{
    private FlightInformation flightInformation;
    private Squadron friendlyBombSquadron;
	private BombingFlight bombingFlightEscortedByPlayer;
	
	public PlayerEscortedFlightBuilder (FlightInformation flightInformation, Squadron friendlyBombSquadron) throws PWCGException 
	{
        this.flightInformation = flightInformation;
        this.friendlyBombSquadron = friendlyBombSquadron;
	}
	
	public Flight createEscortedFlight (Coordinate enemyGroundUnitCoordinates) throws PWCGException 
	{
		MissionBeginUnit missionBeginUnit = new MissionBeginUnit(friendlyBombSquadron.determineCurrentPosition(flightInformation.getCampaign().getDate()));
        FlightInformation escortedFlightInformation = FlightInformationFactory.buildAiFlightInformation(friendlyBombSquadron, flightInformation.getMission(), FlightTypes.BOMB);
        escortedFlightInformation.setEscortedByPlayerFlight(true);
        bombingFlightEscortedByPlayer = new BombingFlight (escortedFlightInformation, missionBeginUnit);
		bombingFlightEscortedByPlayer.createUnitMission();
        moveEscortedFlightCloseToPlayer(enemyGroundUnitCoordinates, bombingFlightEscortedByPlayer.getWaypointPackage().getWaypointsForLeadPlane().get(0).getPosition());
        return bombingFlightEscortedByPlayer;
	}

	private void moveEscortedFlightCloseToPlayer(Coordinate targetCoordinates, Coordinate escortedFlightIngressPosition) throws PWCGException
	{
        Coordinate playerSquadronPosition = flightInformation.getSquadron().determineCurrentPosition(flightInformation.getCampaign().getDate());

        FrontLinesForMap frontMap = PWCGContextManager.getInstance().getCurrentMap().getFrontLinesForMap(flightInformation.getCampaign().getDate());
        Coordinate closestFrontToTarget = frontMap.findClosestFrontCoordinateForSide(targetCoordinates, flightInformation.getSquadron().determineSide());
                        
        double angleToTarget = MathUtils.calcAngle(closestFrontToTarget, playerSquadronPosition);
		Coordinate rendevousCoords = MathUtils.calcNextCoord(closestFrontToTarget, angleToTarget, 10000.0);
        rendevousCoords.setYPos(escortedFlightIngressPosition.getYPos());

		double distanceToTarget = MathUtils.calcDist(targetCoordinates, rendevousCoords);
        if (distanceToTarget < (AttackMcuSequence.CHECK_ZONE_DEFAULT_DISTANCE + 10000))
        {
            rendevousCoords = moveRendezvousZoneAwayFromTarget(rendevousCoords, targetCoordinates);
        }
        
        bombingFlightEscortedByPlayer.resetFlightForPlayerEscort(rendevousCoords, targetCoordinates);
	}
	

	
    private Coordinate moveRendezvousZoneAwayFromTarget(Coordinate initialRendevousCoords, Coordinate targetPosition) throws PWCGException
    {
        double angleAwayFromTarget = MathUtils.calcAngle(targetPosition, initialRendevousCoords);
        Coordinate rendevousCoords = MathUtils.calcNextCoord(initialRendevousCoords, angleAwayFromTarget, AttackMcuSequence.CHECK_ZONE_DEFAULT_DISTANCE + 10000);
        rendevousCoords.setYPos(initialRendevousCoords.getYPos());
        return rendevousCoords;
    }


}
