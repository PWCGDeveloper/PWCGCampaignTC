package pwcg.mission.unit;

import java.io.BufferedWriter;
import java.util.List;

import pwcg.campaign.company.Company;
import pwcg.core.exception.PWCGException;
import pwcg.mission.mcu.McuWaypoint;

public interface ITankUnit
{

    void createUnit() throws PWCGException;

    UnitInformation getUnitInformation();

    List<TankMcu> getTanks();

    TankMcu getLeadVehicle();

    List<McuWaypoint> getWaypoints();

    Company getCompany();

    UnitVehicles getUnitTanks();

    void preparePlaneForCoop() throws PWCGException;

    void write(BufferedWriter writer) throws PWCGException;

    void setWaypoints(List<McuWaypoint> waypoints);

}