package pwcg.aar.outofmission.phase2.resupply;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import pwcg.campaign.Campaign;
import pwcg.campaign.company.Company;
import pwcg.campaign.company.CompanyManager;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.resupply.ICompanyNeed;
import pwcg.campaign.resupply.ResupplyCompanyChooser;
import pwcg.campaign.resupply.equipment.CompanyEquipmentNeed;
import pwcg.core.exception.PWCGException;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.CompanyTestProfile;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ResupplyCompanyChooserTest
{
    private Campaign campaign;
    
    @BeforeAll
    public void setupSuite() throws PWCGException
    {
        
        campaign = CampaignCache.makeCampaign(CompanyTestProfile.GROSS_DEUTSCHLAND_PROFILE);
    }

    @Test
    public void testChoosePlayerCompanyWhenDepleted() throws PWCGException
    {
        Map<Integer, ICompanyNeed> needs = getCompanyNeeds(7, 9, 2);
        ResupplyCompanyChooser resupplyCompanyChooser = new ResupplyCompanyChooser(campaign, needs);
        for (int i = 0; i < 3; ++i)
        {
            ICompanyNeed selectedCompanyNeed = resupplyCompanyChooser.getNeedyCompany();
            selectedCompanyNeed.noteResupply();
            assert(selectedCompanyNeed.getCompanyId() == 20111051);
        }
    }

    @Test
    public void testChoosePlayerCompanyUntilDepletedNotTrue() throws PWCGException
    {
        Map<Integer, ICompanyNeed> needs = getCompanyNeeds(6, 9, 2);
        ResupplyCompanyChooser resupplyCompanyChooser = new ResupplyCompanyChooser(campaign, needs);
        for (int i = 0; i < 3; ++i)
        {
            ICompanyNeed selectedCompanyNeed = resupplyCompanyChooser.getNeedyCompany();
            selectedCompanyNeed.noteResupply();
            if (i < 2)
            {
                assert(selectedCompanyNeed.getCompanyId() == 20111051);
            }
            else
            {
                assert(selectedCompanyNeed.getCompanyId() == TestIdentifiers.TEST_GERMAN_COMPANY_ID);
            }
        }
    }

    @Test
    public void testChooseCompanysUntilNoMoreReplacementsNeeded() throws PWCGException
    {
        Map<Integer, ICompanyNeed> needs = getCompanyNeeds(3, 2, 1);
        ResupplyCompanyChooser resupplyCompanyChooser = new ResupplyCompanyChooser(campaign, needs);
        
        int i_jg51Count = 0;
        int i_jg52Count = 0;
        int ii_jg52Count = 0;
        for (int i = 0; i < 6; ++i)
        {
            ICompanyNeed selectedCompanyNeed = resupplyCompanyChooser.getNeedyCompany();
            selectedCompanyNeed.noteResupply();
            if (selectedCompanyNeed.getCompanyId() == 20111051)
            {
                ++i_jg51Count;
            }
            else if (selectedCompanyNeed.getCompanyId() == TestIdentifiers.TEST_GERMAN_COMPANY_ID)
            {
                ++i_jg52Count;
            }
            else if (selectedCompanyNeed.getCompanyId() == 20112052)
            {
                ++ii_jg52Count;
            }
        }

        assert(i_jg51Count == 3);
        assert(i_jg52Count == 2);
        assert(ii_jg52Count == 1);
        
        ICompanyNeed selectedCompanyNeed = resupplyCompanyChooser.getNeedyCompany();
        assert(selectedCompanyNeed == null);        
    }

    private Map<Integer, ICompanyNeed> getCompanyNeeds(int playerPlanesNeeded, int i_jg52PlanesNeeded, int ii_jg52PlanesNeeded) throws PWCGException
    {
        Map<Integer, ICompanyNeed> needs = new HashMap<>();
        CompanyManager companyManager = PWCGContext.getInstance().getCompanyManager();
        
        Company playerCompany = companyManager.getCompany(20111051);
        CompanyEquipmentNeed playerCompanyEquipmentNeed = new CompanyEquipmentNeed(campaign, playerCompany);
        playerCompanyEquipmentNeed.setPlanesNeeded(playerPlanesNeeded);
        needs.put(playerCompany.getCompanyId(), playerCompanyEquipmentNeed);
        
        Company i_jg52 = companyManager.getCompany(TestIdentifiers.TEST_GERMAN_COMPANY_ID);
        CompanyEquipmentNeed i_jg52EquipmentNeed = new CompanyEquipmentNeed(campaign, i_jg52);
        i_jg52EquipmentNeed.setPlanesNeeded(i_jg52PlanesNeeded);
        needs.put(i_jg52.getCompanyId(), i_jg52EquipmentNeed);        
        
        Company ii_jg52 = companyManager.getCompany(20112052);
        CompanyEquipmentNeed ii_jg52EquipmentNeed = new CompanyEquipmentNeed(campaign, ii_jg52);
        ii_jg52EquipmentNeed.setPlanesNeeded(ii_jg52PlanesNeeded);
        needs.put(ii_jg52.getCompanyId(), ii_jg52EquipmentNeed);        
        
        return needs;
    }
}
