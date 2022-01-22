package pwcg.campaign.crewmember;

import pwcg.campaign.Campaign;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;

public class VictoryDescriptionBuilderAir extends VictoryDescriptionBuilderBase
{
    public VictoryDescriptionBuilderAir (Campaign campaign, Victory victory)
    {
        super(campaign, victory);
    }    
 
    public String getVictoryDescriptionAirToAirSimple() throws PWCGException
    {
        String victimTankType = "Enemy Aircraft";
        if (victory.getVictim().getType() != null && !victory.getVictim().getType().isEmpty())
        {
            victimTankType = getVehicleDescription(victory.getVictim().getType());
        }
     
        String victoryDesc = "";
        if (victory.getDate() != null)
        {
            victoryDesc +=  "On " + DateUtils.getDateString(victory.getDate()) + " a ";
        }
        
        victoryDesc += victimTankType + " was shot down.";
        
        return victoryDesc;
    }

    String createVictoryDescriptionUnknownToAir() throws PWCGException
    {
        String victoryDesc = "";
        
        String victimTankType = getVehicleDescription(victory.getVictim().getType());

        if (!(victory.getVictim().getCrewMemberName().isEmpty()))
        {
            // Line 1
            victoryDesc +=  "On " + DateUtils.getDateString(victory.getDate());
            if (!victory.getLocation().isEmpty())
            {
                victoryDesc +=  " near " + victory.getLocation();
            }
            victoryDesc +=  ".";

            // Line 2
            victoryDesc +=  "\n";
            victoryDesc +=  "A " + victimTankType + " of " + victory.getVictim().getCompanyName() + " crashed.";
        }
        // Backwards compatibility - we do not have the information for the latest victory logs
        else
        {
            victoryDesc +=  victimTankType + " crashed.";
        }
        
        return victoryDesc;
    }
}
