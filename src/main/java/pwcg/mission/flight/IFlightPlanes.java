package pwcg.mission.flight;

import java.io.BufferedWriter;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.mission.flight.plane.PlaneMcu;
import pwcg.mission.ground.org.IGroundUnit;

public interface IFlightPlanes
{

    void enableNonVirtualFlight();

    List<PlaneMcu> getAiPlanes() throws PWCGException;

    List<PlaneMcu> getPlayerPlanes() throws PWCGException;

    PlaneMcu getPlaneForPilot(Integer pilotSerialNumber);

    PlaneMcu getPlaneByLinkTrId(Integer planeLinkTrId);

    PlaneMcu getFlightLeader();

    List<PlaneMcu> getPlanes();

    void setFuel(double myFuel);

    void addFlightTarget(IFlight targetFlight);

    void addGroundUnitTarget(IGroundUnit targetGroundUnit);

    int getFlightCruisingSpeed();

    boolean isFlightHasFighterPlanes();

    void setPlanes(List<PlaneMcu> planes) throws PWCGException;

    List<Integer> getPlaneLinkTrIds();

    void setFlightPayload() throws PWCGException;

    void setPlanePosition(Integer planeLinkTrId, Coordinate planeCoords, Orientation planeOrientation, int startingPoint);

    void preparePlaneForCoop(IFlight flight) throws PWCGException;

    void write(BufferedWriter writer) throws PWCGException;

    int getFlightSize();

    void finalize() throws PWCGException;

}