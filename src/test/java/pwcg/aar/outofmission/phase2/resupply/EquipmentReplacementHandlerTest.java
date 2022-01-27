package pwcg.aar.outofmission.phase2.resupply;

import java.util.Date;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.company.Company;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.resupply.ResupplyNeedBuilder;
import pwcg.campaign.resupply.equipment.EquipmentReplacementHandler;
import pwcg.campaign.resupply.equipment.EquipmentResupplyData;
import pwcg.campaign.tank.Equipment;
import pwcg.campaign.tank.EquippedTank;
import pwcg.campaign.tank.TankStatus;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.CompanyTestProfile;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class EquipmentReplacementHandlerTest
{
    private Campaign campaign;
    
    @Mock private ArmedService armedService;

    @BeforeAll
    public void setupSuite() throws PWCGException
    {
        
        campaign = CampaignCache.makeCampaign(CompanyTestProfile.GROSS_DEUTSCHLAND_PROFILE);
     }

    @BeforeEach
    public void setupTest() throws PWCGException
    {        
        Mockito.when(armedService.getServiceId()).thenReturn(20111);
    }

    @Test
    public void testTransfersInForLostCampaignMembers() throws PWCGException
    {
        ResupplyNeedBuilder equipmentNeedBuilder = new ResupplyNeedBuilder(campaign, armedService);
        EquipmentReplacementHandler companyTransferHandler = new EquipmentReplacementHandler(campaign, equipmentNeedBuilder);
        
        deactivateCampaignEquipment();
      
        EquipmentResupplyData equipmentTransferData = companyTransferHandler.resupplyForLosses(armedService);
        Assertions.assertTrue (equipmentTransferData.getTransferCount() == 3);
    }

    private void deactivateCampaignEquipment() throws PWCGException
    {
        Date inactiveDate = DateUtils.removeTimeDays(campaign.getDate(), 10);
        Company playerCompany = campaign.determinePlayerCompanies().get(0);
        int numInactivated = 0;
        for (Equipment equipment: campaign.getEquipmentManager().getEquipmentAllCompanies().values())
        {
            for (EquippedTank equippedTank : equipment.getActiveEquippedTanks().values())
            {
                Company company = PWCGContext.getInstance().getCompanyManager().getCompany(equippedTank.getCompanyId());
                if (company.getService() == armedService.getServiceId())
                {
                    if (playerCompany.getCompanyId() != equippedTank.getCompanyId())
                    {
                        equippedTank.setTankStatus(TankStatus.STATUS_DESTROYED);
                        equippedTank.setDateRemovedFromService(inactiveDate);
                        ++numInactivated;
                    }
                }
                
                break;
            }

            if (numInactivated == 3)
            {
                break;
            }
        }
    }

    @Test
    public void testTransfersInForLostCrewMembers() throws PWCGException
    {
        ResupplyNeedBuilder equipmentNeedBuilder = new ResupplyNeedBuilder(campaign, armedService);
        EquipmentReplacementHandler companyTransferHandler = new EquipmentReplacementHandler(campaign, equipmentNeedBuilder);
        
        deactivateCompanyEquipment();
      
        EquipmentResupplyData equipmentTransferData = companyTransferHandler.resupplyForLosses(armedService);
        int numTransferredToPlayerCompany = equipmentTransferData.getEquipmentResuppliedToCompany(CompanyTestProfile.GROSS_DEUTSCHLAND_PROFILE.getCompanyId()).size();
        Assertions.assertTrue (numTransferredToPlayerCompany >= 1);
    }

    private void deactivateCompanyEquipment() throws PWCGException
    {
        Date inactiveDate = DateUtils.removeTimeDays(campaign.getDate(), 10);

        int numInactivated = 0;
        Company playerCompany = campaign.determinePlayerCompanies().get(0);
        Equipment equipment = campaign.getEquipmentManager().getEquipmentForCompany(playerCompany.getCompanyId());
        for (EquippedTank equippedTank : equipment.getActiveEquippedTanks().values())
        {
            Company company = PWCGContext.getInstance().getCompanyManager().getCompany(playerCompany.getCompanyId());
            if (company.getCompanyId() == equippedTank.getCompanyId())
            {                
                equippedTank.setTankStatus(TankStatus.STATUS_DESTROYED);
                equippedTank.setDateRemovedFromService(inactiveDate);
                ++numInactivated;
            }
                            
            if (numInactivated == 3)
            {
                break;
            }
        }
    }
}
