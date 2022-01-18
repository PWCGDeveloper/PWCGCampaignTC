package pwcg.mission.unit;

import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.company.Company;
import pwcg.core.exception.PWCGException;
import pwcg.mission.mcu.McuWaypoint;
import pwcg.mission.unit.tank.TankMcuFactory;

public abstract class TankPlatoon implements ITankPlatoon
{
    protected PlatoonTanks platoonVehicles;
    protected PlatoonInformation platoonInformation;
    protected List<McuWaypoint> waypoints = new ArrayList<>();

    public TankPlatoon(PlatoonInformation platoonInformation)
    {
        this.platoonVehicles = new PlatoonTanks();
        this.platoonInformation = platoonInformation;
    }

    @Override
    public void createUnit() throws PWCGException
    {
        buildTanks();
        setUnitPayload();
        createInitialPosition();
        createWaypoints();
    }

    @Override
    public PlatoonInformation getUnitInformation()
    {
        return platoonInformation;
    }

    @Override
    public List<TankMcu> getTanks()
    {
        return platoonVehicles.getTanks();
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

    public void setWaypoints(List<McuWaypoint> waypoints)
    {
        this.waypoints = waypoints;
    }
    
    @Override
    public Company getCompany()
    {
        return platoonInformation.getCompany();
    }

    @Override
    public PlatoonTanks getUnitTanks()
    {
        return platoonVehicles;
    }

    @Override
    public void preparePlaneForCoop() throws PWCGException
    {
        platoonVehicles.prepareTankForCoop();        
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
    }

    protected void setUnitPayload() throws PWCGException
    {
        for(TankMcu tank : platoonVehicles.getTanks())
        {
            tank.buildTankPayload(this, platoonInformation.getCampaign().getDate());
        }
    }

    private void buildTanks() throws PWCGException
    {
        int numTanks = platoonInformation.getParticipatingPlayersForCompany().size();
        if(numTanks < 4)
        {
            numTanks = 4;
        }
                
        TankMcuFactory tankMcuFactory = new TankMcuFactory(platoonInformation);        
        List<TankMcu> tanks = tankMcuFactory.createTanksForUnit(numTanks);
        platoonVehicles.setTanks(tanks);
        platoonVehicles.setFuelForUnit(1.0);
    }

    private void createInitialPosition() throws PWCGException
    {
        UnitPositionSetter.setUnitTankPositions(this);
    }

    abstract protected void createWaypoints() throws PWCGException;
}
