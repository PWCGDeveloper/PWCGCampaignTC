package pwcg.campaign.outofmission;

import java.util.ArrayList;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.core.utils.IWeight;
import pwcg.core.utils.WeightCalculator;
import pwcg.mission.ground.building.PwcgStructure;

public class DuringCampaignStructureVictimGenerator
{    
    public DuringCampaignStructureVictimGenerator () throws PWCGException
    {
    }

    public PwcgStructure generateVictimStructure() throws PWCGException 
    {
        PwcgStructure victimStructure = determineStructureType();
        return victimStructure;
    }

    private PwcgStructure determineStructureType() throws PWCGException
    {
        List<IWeight> possibleVictimTypes = new ArrayList<>();
        for (PwcgStructure structureType : PwcgStructure.values()) 
        {
            if (structureType.getWeight() > 0)
            {
                possibleVictimTypes.add(structureType);
            }
        }
        
        WeightCalculator weightCalculator = new WeightCalculator(possibleVictimTypes);
        int index = weightCalculator.getItemFromWeight();
        PwcgStructure victimType = (PwcgStructure)possibleVictimTypes.get(index);
        return victimType;
    }

}
