package pwcg.mission;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.Side;
import pwcg.campaign.company.Company;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.tank.PwcgRole;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.RandomNumberGenerator;

public class OpposingCompanyChooser
{
    private Campaign campaign;
    private List<PwcgRole> opposingRoles = new ArrayList<>();
    private Side opposingSide;
    private int numberOfOpposingFlights = 1;

    public OpposingCompanyChooser(Campaign campaign, List<PwcgRole> opposingRoles, Side opposingSide, int numberOfOpposingFlights)
    {
        this.campaign = campaign;
        this.opposingRoles = opposingRoles;
        this.opposingSide = opposingSide;
        this.numberOfOpposingFlights = numberOfOpposingFlights;
    }

    public List<Company> getOpposingCompanys() throws PWCGException
    {
        List<Company> viableOpposingSquads = getViableOpposingCompanys();        
        if (viableOpposingSquads.size() <= numberOfOpposingFlights)
        {
            return viableOpposingSquads;
        }
        else
        {
            return selectOpposingCompanys(viableOpposingSquads);            
        }
    }

    private List<Company> selectOpposingCompanys(List<Company> viableOpposingSquads)
    {
        Map<Integer, Company> selectedOpposingSquads = new HashMap<>();
        HashSet<Integer> alreadyPicked = new HashSet<>();
        while (selectedOpposingSquads.size() < numberOfOpposingFlights)
        {
            int index= RandomNumberGenerator.getRandom(viableOpposingSquads.size());
            Company opposingCompany = viableOpposingSquads.get(index);
            if (!selectedOpposingSquads.containsKey(opposingCompany.getCompanyId()))
            {
                selectedOpposingSquads.put(opposingCompany.getCompanyId(), opposingCompany);
                alreadyPicked.add(index);
            }
        }
        return new ArrayList<>(selectedOpposingSquads.values());
    }

    private List<Company> getViableOpposingCompanys() throws PWCGException
    {        
        List<Company> viableOpposingSquads = PWCGContext.getInstance().getCompanyManager().getViableAiCompaniesForCurrentMapAndSideAndRole(campaign, opposingRoles, opposingSide);
        return viableOpposingSquads;
    }
}
