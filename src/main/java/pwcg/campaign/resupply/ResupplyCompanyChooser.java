package pwcg.campaign.resupply;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import pwcg.campaign.Campaign;
import pwcg.campaign.company.Company;
import pwcg.core.utils.RandomNumberGenerator;

public class ResupplyCompanyChooser
{
    private Campaign campaign;
    private Map<Integer, ICompanyNeed> companyNeeds = new TreeMap<>();
    private List<ICompanyNeed> playerCompanyNeeds = new ArrayList<>();
    private List<ICompanyNeed> desperateCompanyNeeds = new ArrayList<>();
    private List<ICompanyNeed> genericCompanyNeeds = new ArrayList<>();

    public ResupplyCompanyChooser(Campaign campaign, Map<Integer, ICompanyNeed> companyNeeds)
    {
        this.campaign = campaign;
        this.companyNeeds = companyNeeds;
    }
    
    public ICompanyNeed getNeedyCompany()
    {
        clearPreviousAssessment();
        sortNeedyCompanys();
        return chooseNeedyCompany();
    }

    private void clearPreviousAssessment()
    {
        playerCompanyNeeds.clear();
        desperateCompanyNeeds.clear();
        genericCompanyNeeds.clear();        
    }

    private ICompanyNeed chooseNeedyCompany()
    {
        if (playerCompanyNeeds.size() > 0)
        {
            int index = RandomNumberGenerator.getRandom(playerCompanyNeeds.size());
            return playerCompanyNeeds.get(index);
        }
        else if (desperateCompanyNeeds.size() > 0)
        {
            int index = RandomNumberGenerator.getRandom(desperateCompanyNeeds.size());
            return desperateCompanyNeeds.get(index);
        }
        else if (genericCompanyNeeds.size() > 0)
        {
            int index = RandomNumberGenerator.getRandom(genericCompanyNeeds.size());
            return genericCompanyNeeds.get(index);
        }
        else
        {
            return null;
        }
    }

    private void sortNeedyCompanys()
    {
        for (ICompanyNeed companyNeed : companyNeeds.values())
        {
            if (Company.isPlayerCompany(campaign, companyNeed.getCompanyId()))
            {
                desperateCompany(playerCompanyNeeds, companyNeed, 4);
            }
            else
            {
                desperateCompany(desperateCompanyNeeds, companyNeed, 6);
            }
        }
    }
    
    private void desperateCompany(List<ICompanyNeed> desperatePool, ICompanyNeed companyNeed, int limit)
    {
        if (companyNeed.getNumNeeded() > 0)
        {
            if (companyNeed.getNumNeeded() > limit)
            {
                desperatePool.add(companyNeed);
            }
            else
            {
                genericCompanyNeeds.add(companyNeed);
            }
        }
    }
}
