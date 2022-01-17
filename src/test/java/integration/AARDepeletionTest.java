package integration;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import pwcg.aar.AARCoordinator;
import pwcg.campaign.Campaign;
import pwcg.campaign.company.Company;
import pwcg.campaign.company.CompanyViability;
import pwcg.campaign.context.FrontMapIdentifier;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.personnel.CompanyPersonnel;
import pwcg.campaign.tank.Equipment;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.CompanyTestProfile;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AARDepeletionTest
{
    private Campaign campaign;    
    private static AARCoordinator aarCoordinator;

    @BeforeAll
    public void setupSuite() throws PWCGException
    {
        
        PWCGContext.getInstance().changeContext(FrontMapIdentifier.BODENPLATTE_MAP);
        campaign = CampaignCache.makeCampaignOnDisk(CompanyTestProfile.SEVENTH_DIVISION_PROFILE);
        aarCoordinator = AARCoordinator.getInstance();
    }

    @Test
    public void runMissionAARLeave () throws PWCGException
    {
        for (int i = 0; i < 180; ++i)
        {
            System.out.println(DateUtils.getDateStringPretty(campaign.getDate()));

            aarCoordinator.reset(campaign);
            aarCoordinator.submitLeave(campaign, 1);
            int numDepeletedCompanys = 0;
            for (Company company : PWCGContext.getInstance().getCompanyManager().getActiveCompanies(campaign.getDate()))
            {
                if (!CompanyViability.isCompanyViable(company, campaign))
                {
                    CompanyPersonnel companypersonnel = campaign.getPersonnelManager().getCompanyPersonnel(company.getCompanyId());
                    int numActiveCrewMembers = companypersonnel.getCrewMembers().getActiveCount(campaign.getDate());
                    
                    Equipment companyEquipment = campaign.getEquipmentManager().getEquipmentForCompany(company.getCompanyId());
                    int numActivePlanes = companyEquipment.getActiveEquippedTanks().size();
                    
                    printIterationResults(company, numActiveCrewMembers, numActivePlanes);
                    
                    ++numDepeletedCompanys;
                }
            }
            
            assert(numDepeletedCompanys < 15);
        }
    }

    private void printIterationResults(Company company, int numActiveCrewMembers, int numActivePlanes) throws PWCGException
    {
        System.out.println("    " + company.getCompanyId() + " " + company.determineDisplayName(campaign.getDate()));
        System.out.println("        CrewMembers: " + numActiveCrewMembers);
        System.out.println("        Planes: " + numActivePlanes);
    } 
}
