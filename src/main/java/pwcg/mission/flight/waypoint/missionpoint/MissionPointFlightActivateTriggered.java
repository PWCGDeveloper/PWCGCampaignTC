package pwcg.mission.flight.waypoint.missionpoint;

import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.gui.rofmap.brief.model.BriefingMapPoint;
import pwcg.mission.MissionBeginUnit;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.flight.plane.PlaneMcu;
import pwcg.mission.mcu.BaseFlightMcu;
import pwcg.mission.mcu.Coalition;
import pwcg.mission.mcu.CoalitionFactory;
import pwcg.mission.mcu.McuActivate;
import pwcg.mission.mcu.McuCheckZone;
import pwcg.mission.mcu.McuTimer;
import pwcg.mission.mcu.McuWaypoint;

public class MissionPointFlightActivateTriggered implements IMissionPointSet
{
    private IFlight flight;

    private MissionBeginUnit missionBeginUnit;
    private McuTimer missionBeginTimer = null;
    private McuTimer checkzoneTriggeredTimer = null;
    private McuCheckZone takeoffCheckZone = null;
    private McuActivate activationEntity = null;
    private boolean linkToNextTarget = true;
    private MissionPointSetType missionPointSetType;

    public MissionPointFlightActivateTriggered(IFlight flight)
    {
        this.flight = flight;
        this.missionPointSetType = MissionPointSetType.MISSION_POINT_SET_ACTIVATE;
    }
    
    public void createFlightActivate() throws PWCGException, PWCGException 
    {
        createFlightMissionBegin();
        createActivation();
        createTargetAssociations();
    }

    @Override
    public void setLinkToNextTarget(int nextTargetIndex) throws PWCGException
    {
        missionBeginTimer.setTarget(nextTargetIndex);
    }

    @Override
    public int getEntryPoint() throws PWCGException
    {
        return missionBeginTimer.getIndex();
    }

    @Override
    public List<MissionPoint> getFlightMissionPoints()
    {
        return new ArrayList<MissionPoint>();
    }

    @Override
    public void disableLinkToNextTarget()
    {
        linkToNextTarget = false;        
    }

    @Override
    public boolean isLinkToNextTarget()
    {
        return linkToNextTarget;
    }

    @Override
    public void finalizeMissionPointSet(PlaneMcu flightLeader) throws PWCGException
    {
        createObjectAssociations(flightLeader);
    }

    @Override
    public void write(BufferedWriter writer) throws PWCGException 
    {
        missionBeginUnit.write(writer);
        missionBeginTimer.write(writer);
        takeoffCheckZone.write(writer);
        checkzoneTriggeredTimer.write(writer);
        activationEntity.write(writer);
    }
    
    private void createFlightMissionBegin() throws PWCGException
    {
        missionBeginUnit = new MissionBeginUnit(flight.getFlightHomePosition());
        missionBeginUnit.setStartTime(1);
    }

    private void createActivation() throws PWCGException
    {
        FlightInformation flightInformation = flight.getFlightInformation();
   
        Coalition enemyCoalition = CoalitionFactory.getEnemyCoalition(flight.getSquadron().getCountry());

        takeoffCheckZone = new McuCheckZone("Check Zone Takeoff");
        takeoffCheckZone.setZone(10000);
        takeoffCheckZone.triggerCheckZoneByCoalition(enemyCoalition);
        takeoffCheckZone.setDesc("Check Zone Takeoff");
        takeoffCheckZone.setPosition(flightInformation.getDepartureAirfield().getPosition().copy());

        activationEntity = new McuActivate();
        activationEntity.setName("Activate");
        activationEntity.setDesc("Activate entity");
        activationEntity.setPosition(flightInformation.getDepartureAirfield().getPosition().copy());

        missionBeginTimer = new McuTimer();
        missionBeginTimer.setName("Activation Timer");
        missionBeginTimer.setDesc("Activation Timer");
        missionBeginTimer.setPosition(flightInformation.getDepartureAirfield().getPosition().copy());        
        missionBeginTimer.setTime(1);

        checkzoneTriggeredTimer = new McuTimer();
        checkzoneTriggeredTimer.setName("Takeoff Check Zone Timer");
        checkzoneTriggeredTimer.setDesc("Takeoff Check Zone Timer");
        checkzoneTriggeredTimer.setPosition(flightInformation.getDepartureAirfield().getPosition().copy());        
        checkzoneTriggeredTimer.setTime(1);
    }

    private void createTargetAssociations()
    {
        missionBeginUnit.linkToMissionBegin(missionBeginTimer.getIndex());
        missionBeginTimer.setTarget(takeoffCheckZone.getIndex());
        takeoffCheckZone.setTarget(checkzoneTriggeredTimer.getIndex());
        checkzoneTriggeredTimer.setTarget(activationEntity.getIndex());
    }

    private void createObjectAssociations(PlaneMcu plane)
    {
        activationEntity.setObject(plane.getLinkTrId());
    }
    
    @Override
    public List<McuWaypoint> getAllWaypoints()
    {
        List<McuWaypoint> allWaypoints = new ArrayList<>();
        return allWaypoints;
    }

    @Override
    public boolean containsWaypoint(long waypointIdToFind)
    {
        return false;
    }

    @Override
    public McuWaypoint getWaypointById(long waypointId) throws PWCGException
    {
        throw new PWCGException("No waypoints in flight activate");                
    }

    @Override
    public void updateWaypointFromBriefing(BriefingMapPoint waypoint) throws PWCGException
    {
        throw new PWCGException("No waypoints in flight activate");                
    }

    @Override
    public void addWaypointAfterWaypoint(McuWaypoint newWaypoint, long waypointIdAfter) throws PWCGException
    {
        throw new PWCGException("No waypoints in flight activate");                        
    }

    @Override
    public void addWaypointBeforeWaypoint(McuWaypoint newWaypoint, long waypointIdBefore) throws PWCGException
    {
        throw new PWCGException("No waypoints in flight activate");                        
    }

    @Override
    public long addWaypointFromBriefing(BriefingMapPoint newWaypoint, long waypointIdAfter) throws PWCGException
    {
        throw new PWCGException("No waypoints in flight activate");                                
    }

    @Override
    public void removeUnwantedWaypoints(List<BriefingMapPoint> waypointsInBriefing) throws PWCGException
    {
    }

    @Override
    public List<BaseFlightMcu> getAllFlightPoints()
    {
        List<BaseFlightMcu> allFlightPoints = new ArrayList<>();
        return allFlightPoints;
    }

    @Override
    public MissionPointSetType getMissionPointSetType()
    {
        return missionPointSetType;
    }

    public McuTimer getMissionBeginTimer()
    {
        return missionBeginTimer;
    }    
}
