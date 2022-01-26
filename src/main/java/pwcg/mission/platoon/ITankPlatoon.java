package pwcg.mission.platoon;

import java.io.BufferedWriter;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.ICompanyMission;
import pwcg.mission.mcu.McuWaypoint;
import pwcg.mission.platoon.tank.TankMcu;

public interface ITankPlatoon
{

    void createUnit() throws PWCGException;

    PlatoonInformation getPlatoonInformation();

    TankMcu getLeadVehicle();

    List<McuWaypoint> getWaypoints();

    ICompanyMission getCompany();

    PlatoonTanks getPlatoonTanks();

    void preparePlaneForCoop() throws PWCGException;

    void write(BufferedWriter writer) throws PWCGException;

    void setWaypoints(List<McuWaypoint> waypoints);

    int getIndex();

    boolean isPlayerPlatoon();

    void setPlatoonMissionType(PlatoonMissionType platoonMissionType);

    PlatoonMissionType getPlatoonMissionType();

    void setStartPosition(Coordinate startPosition, Coordinate towardsPosition) throws PWCGException;
}