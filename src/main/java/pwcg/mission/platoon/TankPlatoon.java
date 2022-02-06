package pwcg.mission.platoon;

import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.utils.IndexGenerator;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.ICompanyMission;
import pwcg.mission.ground.builder.ArmoredPlatoonResponsiveRoute;
import pwcg.mission.mcu.McuWaypoint;
import pwcg.mission.platoon.tank.TankMcu;

public abstract class TankPlatoon implements ITankPlatoon
{
    protected abstract void buildTanks() throws PWCGException;
    @Override
    public abstract boolean isPlayerPlatoon();

    protected PlatoonTanks platoonVehicles;
    protected PlatoonInformation platoonInformation;
    protected List<McuWaypoint> waypoints = new ArrayList<>();
    protected PlatoonMissionType platoonMissionType = PlatoonMissionType.ANY;
    protected int index;
    protected List<ArmoredPlatoonResponsiveRoute> responsiveRoutes = new ArrayList<>();

    public TankPlatoon(PlatoonInformation platoonInformation)
    {
        this.platoonVehicles = new PlatoonTanks();
        this.platoonInformation = platoonInformation;
        this.index = IndexGenerator.getInstance().getNextIndex();
    }

    @Override
    public void createUnit() throws PWCGException
    {
        buildTanks();
        setUnitPayload();
    }

    @Override
    public PlatoonInformation getPlatoonInformation()
    {
        return platoonInformation;
    }

    @Override
    public TankMcu getLeadVehicle()
    {
        return platoonVehicles.getUnitLeader();
    }

    @Override
    public List<McuWaypoint> getWaypoints()
    {
        return waypoints;
    }

    @Override
    public void setWaypoints(List<McuWaypoint> waypoints)
    {
        this.waypoints = waypoints;
    }

    @Override
    public ICompanyMission getCompany()
    {
        return platoonInformation.getCompany();
    }

    @Override
    public PlatoonTanks getPlatoonTanks()
    {
        return platoonVehicles;
    }

    @Override
    public void preparePlaneForCoop() throws PWCGException
    {
        platoonVehicles.prepareTankForCoop(platoonInformation.getCampaign());
    }

    @Override
    public void write(BufferedWriter writer) throws PWCGException
    {
        for(TankMcu tank : platoonVehicles.getTanks())
        {
            tank.write(writer);
        }

        for(McuWaypoint waypoint : waypoints)
        {
            waypoint.write(writer);
        }

        for(ArmoredPlatoonResponsiveRoute responsiveRoutes : responsiveRoutes)
        {
            responsiveRoutes.write(writer);
        }
    }

    protected void setUnitPayload() throws PWCGException
    {
        for(TankMcu tank : platoonVehicles.getTanks())
        {
            tank.buildTankPayload(this, platoonInformation.getCampaign().getDate());
        }
    }

    @Override
    public void setStartPosition(Coordinate startPosition, Coordinate towardsPosition) throws PWCGException
    {
        PlatoonPositionSetter.setPlatoonPositions(this, startPosition, towardsPosition);
    }

    @Override
    public int getIndex()
    {
        return index;
    }

    @Override
    public void setPlatoonMissionType(PlatoonMissionType platoonMissionType)
    {
        this.platoonMissionType = platoonMissionType;
    }

    @Override
    public PlatoonMissionType getPlatoonMissionType()
    {
        return platoonMissionType;
    }

    @Override
    public void addResponsiveRoute(ArmoredPlatoonResponsiveRoute platoonResponsiveRoute)
    {
        responsiveRoutes.add(platoonResponsiveRoute);
    }

    @Override
    public List<ArmoredPlatoonResponsiveRoute> getResponsiveRoutes()
    {
        return responsiveRoutes;
    }
}
