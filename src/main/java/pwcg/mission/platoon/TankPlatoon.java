package pwcg.mission.platoon;

import java.io.BufferedWriter;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.utils.IndexGenerator;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.gui.rofmap.brief.model.BriefingMapPoint;
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
    protected PlatoonMissionType platoonMissionType = PlatoonMissionType.ANY;
    private PlatoonWaypoints platoonWaypoints = new PlatoonWaypoints();
    protected int index;

    public TankPlatoon(PlatoonInformation platoonInformation)
    {
        this.platoonVehicles = new PlatoonTanks();
        this.platoonInformation = platoonInformation;
        this.index = IndexGenerator.getInstance().getNextIndex();
    }

    @Override
    public void write(BufferedWriter writer) throws PWCGException
    {
        for(TankMcu tank : platoonVehicles.getTanks())
        {
            tank.write(writer);
        }

        platoonWaypoints.write(writer);
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
    public List<McuWaypoint> getWaypoints()
    {
        return platoonWaypoints.getWaypoints();
    }

    @Override
    public void setWaypoints(List<McuWaypoint> waypoints) throws PWCGException
    {
        Campaign campaign = platoonInformation.getCampaign();
        Coordinate basePosition = platoonInformation.getCompany().determineCurrentPosition(campaign.getDate());
        platoonWaypoints.setWaypoints(campaign, basePosition, this.getLeadVehicle().getLinkTrId(), waypoints);
    }

    @Override
    public void addResponsiveRoute(ArmoredPlatoonResponsiveRoute platoonResponsiveRoute)
    {
        platoonWaypoints.addResponsiveRoute(this.getLeadVehicle().getLinkTrId(), platoonResponsiveRoute);
    }

    @Override
    public List<ArmoredPlatoonResponsiveRoute> getResponsiveRoutes()
    {
        return platoonWaypoints.getResponsiveRoutes();
    }

    @Override
    public void updateWaypointsFromBriefing(List<BriefingMapPoint> briefingMapMapPoints) throws PWCGException
    {
        updateStartPositionFromBriefing(briefingMapMapPoints);
        platoonWaypoints.updateWaypointsFromBriefing(briefingMapMapPoints);
    }

    private void updateStartPositionFromBriefing(List<BriefingMapPoint> briefingMapPoints) throws PWCGException
    {
        Coordinate startPosition = briefingMapPoints.get(0).getPosition();
        Coordinate towardsPosition = briefingMapPoints.get(1).getPosition();
        this.setStartPosition(startPosition, towardsPosition);
    }
}
