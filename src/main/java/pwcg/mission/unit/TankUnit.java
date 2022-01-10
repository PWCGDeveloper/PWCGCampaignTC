package pwcg.mission.unit;

import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.company.Company;
import pwcg.core.exception.PWCGException;
import pwcg.mission.mcu.McuWaypoint;
import pwcg.mission.unit.tank.TankMcuFactory;

public abstract class TankUnit implements ITankUnit
{
    protected UnitVehicles unitVehicles;
    protected UnitInformation unitInformation;
    protected List<McuWaypoint> waypoints = new ArrayList<>();

    public TankUnit(UnitInformation unitInformation)
    {
        this.unitVehicles = new UnitVehicles();
        this.unitInformation = unitInformation;
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
    public UnitInformation getUnitInformation()
    {
        return unitInformation;
    }

    @Override
    public List<TankMcu> getTanks()
    {
        return unitVehicles.getTanks();
    }

    @Override
    public TankMcu getLeadVehicle()
    {
        return unitVehicles.getUnitLeader();
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
        return unitInformation.getCompany();
    }

    @Override
    public UnitVehicles getUnitTanks()
    {
        return unitVehicles;
    }

    @Override
    public void preparePlaneForCoop() throws PWCGException
    {
        unitVehicles.prepareTankForCoop();        
    }

    @Override
    public void write(BufferedWriter writer) throws PWCGException
    {        
        for(TankMcu tank : unitVehicles.getTanks())
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
        for(TankMcu tank : unitVehicles.getTanks())
        {
            tank.buildTankPayload(this, unitInformation.getCampaign().getDate());
        }
    }

    private void buildTanks() throws PWCGException
    {
        int numTanks = unitInformation.getParticipatingPlayersForCompany().size();
        if(numTanks < 4)
        {
            numTanks = 4;
        }
                
        TankMcuFactory tankMcuFactory = new TankMcuFactory(unitInformation);        
        List<TankMcu> tanks = tankMcuFactory.createTanksForUnit(numTanks);
        unitVehicles.setTanks(tanks);
        unitVehicles.setFuelForUnit(1.0);
    }

    private void createInitialPosition() throws PWCGException
    {
        UnitPositionSetter.setUnitTankPositions(this);
    }

    abstract protected void createWaypoints() throws PWCGException;
}
