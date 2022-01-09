package pwcg.mission.unit;

import java.io.BufferedWriter;
import java.util.List;

import pwcg.campaign.company.Company;
import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.waypoint.WaypointPackage;
import pwcg.mission.unit.tank.TankMcuFactory;

public abstract class PlayerUnit implements IPlayerUnit
{
    protected UnitVehicles unitVehicles;
    protected UnitInformation unitInformation;
    
    public PlayerUnit(UnitInformation unitInformation)
    {
        this.unitVehicles = new UnitVehicles(this);
        this.unitInformation = unitInformation;
    }

    @Override
    public void createUnit() throws PWCGException
    {
        buildTanks();
        setUnitPayload();
        createWaypoints();
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
        return null;
    }

    @Override
    public WaypointPackage getWaypointPackage()
    {
        return null;
    }

    @Override
    public Company getCompany()
    {
        return null;
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
    public void write(BufferedWriter writer)
    {        
    }

    protected void setUnitPayload() throws PWCGException
    {
        for(TankMcu tank : unitVehicles.getTanks())
        {
            tank.buildTankPayload(this, unitInformation.getCampaign().getDate());
        }
    }

    abstract protected void createWaypoints() throws PWCGException;
}
