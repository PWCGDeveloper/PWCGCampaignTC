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
            assert(selectedCompanyNeed.getCompanyId() == CompanyTestProfile.GROSS_DEUTSCHLAND_PROFILE.getCompanyId());
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
                assert(selectedCompanyNeed.getCompanyId() == CompanyTestProfile.GROSS_DEUTSCHLAND_PROFILE.getCompanyId());
            }
            else
            {
                assert(selectedCompanyNeed.getCompanyId() == 201014001);
            }
        }
    }

    @Test
    public void testChooseCompanysUntilNoMoreReplacementsNeeded() throws PWCGException
    {
        Map<Integer, ICompanyNeed> needs = getCompanyNeeds(3, 2, 1);
        ResupplyCompanyChooser resupplyCompanyChooser = new ResupplyCompanyChooser(campaign, needs);
        
        int gdPzCount = 0;
        int fourteenPzCount = 0;
        int sixteenPzCount = 0;
        for (int i = 0; i < 6; ++i)
        {
            ICompanyNeed selectedCompanyNeed = resupplyCompanyChooser.getNeedyCompany();
            selectedCompanyNeed.noteResupply();
            if (selectedCompanyNeed.getCompanyId() == CompanyTestProfile.GROSS_DEUTSCHLAND_PROFILE.getCompanyId())
            {
                ++gdPzCount;
            }
            else if (selectedCompanyNeed.getCompanyId() == 201014001)
            {
                ++fourteenPzCount;
            }
            else if (selectedCompanyNeed.getCompanyId() == 201016003)
            {
                ++sixteenPzCount;
            }
        }

        assert(gdPzCount == 3);
        assert(fourteenPzCount == 2);
        assert(sixteenPzCount == 1);
        
        ICompanyNeed selectedCompanyNeed = resupplyCompanyChooser.getNeedyCompany();
        assert(selectedCompanyNeed == null);        
    }

    private Map<Integer, ICompanyNeed> getCompanyNeeds(int playerPlanesNeeded, int fourteenPzPlanesNeeded, int ifourteenPzPlanesNeeded) throws PWCGException
    {
        Map<Integer, ICompanyNeed> needs = new HashMap<>();
        CompanyManager companyManager = PWCGContext.getInstance().getCompanyManager();
        
        Company playerCompany = companyManager.getCompany(CompanyTestProfile.GROSS_DEUTSCHLAND_PROFILE.getCompanyId());
        CompanyEquipmentNeed playerCompanyEquipmentNeed = new CompanyEquipmentNeed(campaign, playerCompany);
        playerCompanyEquipmentNeed.setPlanesNeeded(playerPlanesNeeded);
        needs.put(playerCompany.getCompanyId(), playerCompanyEquipmentNeed);
        
        Company fourteenPz = companyManager.getCompany(201014001);
        CompanyEquipmentNeed fourteenPzEquipmentNeed = new CompanyEquipmentNeed(campaign, fourteenPz);
        fourteenPzEquipmentNeed.setPlanesNeeded(fourteenPzPlanesNeeded);
        needs.put(fourteenPz.getCompanyId(), fourteenPzEquipmentNeed);        
        
        Company ifourteenPz = companyManager.getCompany(201016003);
        CompanyEquipmentNeed ifourteenPzEquipmentNeed = new CompanyEquipmentNeed(campaign, ifourteenPz);
        ifourteenPzEquipmentNeed.setPlanesNeeded(ifourteenPzPlanesNeeded);
        needs.put(ifourteenPz.getCompanyId(), ifourteenPzEquipmentNeed);        
        
        return needs;
    }
}
