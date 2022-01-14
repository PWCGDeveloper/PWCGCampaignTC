package pwcg.campaign.crewmember;

import java.util.Date;

import pwcg.campaign.api.ICountry;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.tank.PwcgRoleCategory;
import pwcg.core.exception.PWCGException;
import pwcg.mission.ground.vehicle.IVehicle;
import pwcg.mission.ground.vehicle.VehicleClass;
import pwcg.mission.ground.vehicle.VehicleFactory;

public class GroundVictimGenerator
{    
    private CrewMember crewMember;
    private Date date;
    
    public GroundVictimGenerator (Date date, CrewMember crewMember) throws PWCGException
    {
        this.date = date;
        this.crewMember = crewMember;
    }

    public IVehicle generateVictimVehicle() throws PWCGException 
    {
        ICountry victimCountry = PWCGContext.getInstance().getCurrentMap().getGroundCountryForMapBySide(crewMember.getSide().getOppositeSide());
        
        VehicleClass victimType = determineVehicleClass();
        IVehicle victimVehicle = VehicleFactory.createVehicle(victimCountry, date, victimType);
        return victimVehicle;
    }

    private VehicleClass determineVehicleClass() throws PWCGException
    {
        return VehicleClass.Tank;
    }
    
    public static boolean shouldUse(PwcgRoleCategory companyPrimaryRoleCategory)
    {
        if (companyPrimaryRoleCategory == PwcgRoleCategory.MAIN_BATTLE_TANK || companyPrimaryRoleCategory == PwcgRoleCategory.TANK_DESTROYER)
        {
            return true;
        }
        return false;
    }
}
