package pwcg.aar.campaign.update;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.aar.AARCoordinator;
import pwcg.campaign.Campaign;
import pwcg.campaign.tank.Equipment;
import pwcg.campaign.tank.EquippedTank;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.CompanyTestProfile;

@ExtendWith(MockitoExtension.class)
public class CampaignEquipmentArchtypeChangeHandlerTest
{    
    public CampaignEquipmentArchtypeChangeHandlerTest() throws PWCGException
    {
        
    }
    
    @Test
    public void testArchtypeReplacement() throws PWCGException 
    {
        Campaign campaign = CampaignCache.makeCampaign(CompanyTestProfile.GROSS_DEUTSCHLAND_PROFILE);
        assertTankArchType(campaign, "bf109");
        assertNotTankArchType(campaign, "fw190");
        int daysOff = DateUtils.daysDifference(campaign.getDate(), DateUtils.getDateYYYYMMDD("19420514"));
        AARCoordinator aarCoordinator = AARCoordinator.getInstance();
        aarCoordinator.submitLeave(campaign, daysOff);
        assertNotTankArchType(campaign, "bf109");
        assertTankArchType(campaign, "fw190");
    }
    
    @Test
    public void testArchtypeNoReplacement() throws PWCGException 
    {
        Campaign campaign = CampaignCache.makeCampaign(CompanyTestProfile.GROSS_DEUTSCHLAND_PROFILE);
        assertTankArchType(campaign, "bf109");
        assertNotTankArchType(campaign, "fw190");
        int daysOff = DateUtils.daysDifference(campaign.getDate(), DateUtils.getDateYYYYMMDD("19420513"));
        AARCoordinator aarCoordinator = AARCoordinator.getInstance();
        aarCoordinator.submitLeave(campaign, daysOff);
        assertTankArchType(campaign, "bf109");
        assertNotTankArchType(campaign, "fw190");
    }
    
    private void assertTankArchType(Campaign campaign, String planeArchTypeName)
    {
    	Equipment equipment = campaign.getEquipmentManager().getEquipmentForCompany(CompanyTestProfile.GROSS_DEUTSCHLAND_PROFILE.getCompanyId());
        for (EquippedTank plane : equipment.getActiveEquippedTanks().values())
        {
            assert(plane.getArchType().equals(planeArchTypeName));
        }
    }
    
    private void assertNotTankArchType(Campaign campaign, String planeArchTypeName)
    {
    	Equipment equipment = campaign.getEquipmentManager().getEquipmentForCompany(CompanyTestProfile.GROSS_DEUTSCHLAND_PROFILE.getCompanyId());
        for (EquippedTank plane : equipment.getActiveEquippedTanks().values())
        {
            assert(!(plane.getArchType().equals(planeArchTypeName)));
        }
    }


}
