package pwcg.campaign.company;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.AceManager;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.tank.PwcgRoleCategory;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.RandomNumberGenerator;

public class CompanyTransferFinder
{
    private Campaign campaign;
    private CrewMember crewMember;
    private List<Integer> bestFitCompanys = new ArrayList<Integer>();
    private List<Integer> anyCompanys = new ArrayList<Integer>();
    
    
    public CompanyTransferFinder(Campaign campaign, CrewMember crewMember)
    {
        this.campaign = campaign;
        this.crewMember = crewMember;
    }

    public int chooseCompanyForTransfer() throws PWCGException
    {
        CompanyManager companyManager = PWCGContext.getInstance().getCompanyManager();
       
        for (Company possibleCompany : companyManager.getActiveCompanies(campaign.getDate()))
        {
            // Exclude companys commanded by an ace
            if (canTransferToThisCompany(possibleCompany))
            {
                anyCompanys.add(possibleCompany.getCompanyId());
            }
            
            if (isBestFitCompany(possibleCompany))
            {
                bestFitCompanys.add(possibleCompany.getCompanyId());
            }
        }
        
        int selectedCompanyId = selectCompanyToTransferTo();
        return selectedCompanyId;
    }

    private boolean canTransferToThisCompany(Company possibleCompany) throws PWCGException
    {
        if (!companyMemberHasCurrentCompany())
        {
            return true;
        }

        AceManager aceManager = PWCGContext.getInstance().getAceManager();
        Set<Integer> aceCommandedCompanys = aceManager.getAceCommandedCompanys();

        if (aceCommandedCompanys.contains(possibleCompany.getCompanyId()))
        {
            return false;
        }
        
        if (possibleCompany.getCompanyId() == crewMember.getCompanyId())
        {
            return false;
        }
        
        if (crewMember.determineCountry(campaign.getDate()).getCountryCode() != possibleCompany.getCountry().getCountryCode())
        {
            return false;
        }
        
        return true;
    }

    private boolean companyMemberHasCurrentCompany() throws PWCGException
    {
        CompanyManager companyManager = PWCGContext.getInstance().getCompanyManager();
        Company companyMemberCurrentCompany = companyManager.getCompany(crewMember.getCompanyId());
        if (companyMemberCurrentCompany == null)
        {
            return true;
        }
        
        return false;
    }

    private boolean isBestFitCompany(Company possibleCompany) throws PWCGException
    {
        CompanyManager companyManager = PWCGContext.getInstance().getCompanyManager();
        Company companyMemberCurrentCompany = companyManager.getCompany(crewMember.getCompanyId());
        if (companyMemberCurrentCompany != null)
        {
            PwcgRoleCategory bestRoleForThisCompany = companyMemberCurrentCompany.determineCompanyPrimaryRoleCategory(campaign.getDate());
            PwcgRoleCategory bestRoleForNewCompany = possibleCompany.determineCompanyPrimaryRoleCategory(campaign.getDate());
        
            if (bestRoleForThisCompany == bestRoleForNewCompany)
            {
                return true;
            }
        }
        
        return false;
    }


    private int selectCompanyToTransferTo()
    {
        int selectedCompanyId = -1;
        if (bestFitCompanys.size() > 0)
        {
            int size = bestFitCompanys.size();
            int index = RandomNumberGenerator.getRandom(size);
            selectedCompanyId = bestFitCompanys.get(index);
        }
        else
        {
            int size = anyCompanys.size();
            int index = RandomNumberGenerator.getRandom(size);
            selectedCompanyId = anyCompanys.get(index);
        }
        return selectedCompanyId;
    }

}
