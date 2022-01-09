package pwcg.mission.unit;

import java.io.BufferedWriter;
import java.util.List;

import pwcg.campaign.company.Company;
import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.waypoint.WaypointPackage;

public interface IPlayerUnit
{

    void createUnit() throws PWCGException;

    UnitInformation getUnitInformation();

    List<TankMcu> getTanks();

    TankMcu getLeadVehicle();

    WaypointPackage getWaypointPackage();

    Company getCompany();

    UnitVehicles getUnitTanks();

    void preparePlaneForCoop() throws PWCGException;

    void write(BufferedWriter writer);

}