package pwcg.mission.flight.waypoint.missionpoint;

import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.List;

import pwcg.core.config.ConfigItemKeys;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.IFlightInformation;
import pwcg.mission.flight.plane.PlaneMcu;
import pwcg.mission.flight.waypoint.WaypointAction;
import pwcg.mission.flight.waypoint.WaypointFactory;
import pwcg.mission.flight.waypoint.WaypointType;
import pwcg.mission.flight.waypoint.begin.ClimbWaypointBuilder;
import pwcg.mission.mcu.BaseFlightMcu;
import pwcg.mission.mcu.McuFormation;
import pwcg.mission.mcu.McuMessage;
import pwcg.mission.mcu.McuTakeoff;
import pwcg.mission.mcu.McuTimer;
import pwcg.mission.mcu.McuWaypoint;

public class MissionPointFlightBeginTakeoff extends MissionPointSetSingleWaypointSet implements IMissionPointSet
{
    private IFlight flight;
    private McuTakeoff takeoffMcu = null;
    private McuTimer formationTimer = null;
    private McuFormation formationEntity = null;
    private boolean linkToNextTarget = true;

    public MissionPointFlightBeginTakeoff(IFlight flight)
    {
        this.flight = flight;
    }
    
    public void createFlightBegin() throws PWCGException 
    {
        createTakeoff();  
        createFormation();
        createTakeOffWaypoints();
    }

    @Override
    public void write(BufferedWriter writer) throws PWCGException 
    {
        takeoffMcu.write(writer);
        formationTimer.write(writer);
        formationEntity.write(writer);
        super.write(writer);
    }

    @Override
    public void setLinkToNextTarget(int nextTargetIndex) throws PWCGException
    {
        super.getLastWaypoint().setTarget(nextTargetIndex);
    }    

    @Override
    public List<MissionPoint> getFlightMissionPoints()
    {
        List<MissionPoint> missionPoints = new ArrayList<>();
        MissionPoint takeoffPoint = new MissionPoint(takeoffMcu.getPosition(), WaypointAction.WP_ACTION_TAKEOFF);
        missionPoints.add(takeoffPoint);
        
        List<MissionPoint> postTakeoffWaypoints =  super.getWaypointsAsMissionPoints();
        missionPoints.addAll(postTakeoffWaypoints);

        return missionPoints;
    }

    @Override
    public void finalize(PlaneMcu plane) throws PWCGException
    {
        super.finalize(plane);
        createTargetAssociations();
        createObjectAssociations(plane);        
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
    public int getEntryPoint() throws PWCGException
    {
        return formationTimer.getIndex();
    }
    
    @Override
    public IMissionPointSet duplicateWithOffset(IFlightInformation flightInformation, int positionInFormation) throws PWCGException
    {
        MissionPointRouteSet duplicate = new MissionPointRouteSet();
        duplicate.waypoints = super.duplicateWaypoints(positionInFormation);
        return duplicate;
    }

    private void createTakeoff() throws PWCGException
    {
        takeoffMcu = null;
        if (flight.getFlightInformation().isPlayerFlight())
        {
            if (!flight.getFlightInformation().isAirStart())
            {
                takeoffMcu = new McuTakeoff();
                takeoffMcu.setPosition(flight.getFlightInformation().getDepartureAirfield().getTakeoffLocation().getPosition().copy());
                takeoffMcu.setOrientation(flight.getFlightInformation().getDepartureAirfield().getTakeoffLocation().getOrientation().copy());
            }
        }
    }
    
    private void createFormation() throws PWCGException
    {
        IFlightInformation flightInformation = flight.getFlightInformation();

        formationEntity = new McuFormation();
        formationEntity.setPosition(flightInformation.getDepartureAirfield().getPosition());

        formationTimer = new McuTimer();
        formationTimer.setName(flightInformation.getSquadron().determineDisplayName(flightInformation.getCampaign().getDate()) + ": Formation Timer");
        formationTimer.setDesc("Formation timer entity for " + flightInformation.getSquadron().determineDisplayName(flightInformation.getCampaign().getDate()));
        formationTimer.setPosition(flightInformation.getDepartureAirfield().getPosition().copy());
        formationTimer.setTimer(2);
    }

    private void createTakeOffWaypoints() throws PWCGException
    {
        McuWaypoint takeoffWP = clearTheDeck();
        createClimbWaypoints(takeoffWP);
    }  


    private McuWaypoint clearTheDeck() throws PWCGException 
    {
        Coordinate initialClimbCoords = createTakeOffCoords();

        McuWaypoint takeoffWP = WaypointFactory.createTakeOffWaypointType();
        takeoffWP.setTriggerArea(McuWaypoint.INITIAL_CLIMB_AREA);
        takeoffWP.setDesc(flight.getFlightInformation().getSquadron().determineDisplayName(flight.getCampaign().getDate()), WaypointType.TAKEOFF_WAYPOINT.getName());
        takeoffWP.setSpeed(flight.getFlightPlanes().getFlightCruisingSpeed());
        takeoffWP.setPosition(initialClimbCoords);
        takeoffWP.setOrientation(flight.getFlightInformation().getAirfield().getTakeoffLocation().getOrientation().copy());

        super.addWaypoint(takeoffWP);
        return takeoffWP;
    }
    private Coordinate createTakeOffCoords() throws PWCGException, PWCGException
    {
        int takeoffWaypointDistance = flight.getCampaign().getCampaignConfigManager().getIntConfigParam(ConfigItemKeys.TakeoffWaypointDistanceKey);
        int takeoffWaypointAltitude = flight.getCampaign().getCampaignConfigManager().getIntConfigParam(ConfigItemKeys.TakeoffWaypointAltitudeKey);

        double takeoffOrientation = flight.getFlightInformation().getAirfield().getTakeoffLocation().getOrientation().getyOri();
        Coordinate initialClimbCoords = MathUtils.calcNextCoord(flight.getFlightInformation().getAirfield().getTakeoffLocation().getPosition().copy(), takeoffOrientation, takeoffWaypointDistance);
        initialClimbCoords.setYPos(takeoffWaypointAltitude);

        return initialClimbCoords;
    }

    private void createClimbWaypoints(McuWaypoint takeoffWP) throws PWCGException
    {
        ClimbWaypointBuilder initialWaypointGenerator = new ClimbWaypointBuilder(flight);
        List<McuWaypoint> climbWaypoints = initialWaypointGenerator.createClimbWaypointsForPlayerFlight(takeoffWP);
        super.addWaypoints(climbWaypoints);
    }  

    private void createTargetAssociations() throws PWCGException
    {
        formationTimer.setTarget(formationEntity.getIndex());
        
        flight.getFlightPlanes().getFlightLeader().getEntity().setOnMessages(
                McuMessage.ONTAKEOFF,
                takeoffMcu.getIndex(),
                super.getFirstWaypoint().getIndex());
    }

    private void createObjectAssociations(PlaneMcu plane)
    {
        int flightLeaderIndex = plane.getLinkTrId();
        takeoffMcu.setObject(flightLeaderIndex);
        formationEntity.setObject(flightLeaderIndex);
    }

    @Override
    public List<BaseFlightMcu> getAllFlightPoints()
    {
        List<BaseFlightMcu> allFlightPoints = new ArrayList<>();
        allFlightPoints.addAll(waypoints.getWaypoints());
        return allFlightPoints;
    }
}