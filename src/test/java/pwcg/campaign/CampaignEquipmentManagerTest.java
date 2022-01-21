package pwcg.campaign;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import pwcg.campaign.company.Company;
import pwcg.campaign.context.FrontMapIdentifier;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.tank.Equipment;
import pwcg.campaign.tank.EquippedTank;
import pwcg.campaign.tank.TankAttributeMapping;
import pwcg.campaign.tank.TankTypeInformation;
import pwcg.campaign.tank.TankTypeFactory;
import pwcg.core.exception.PWCGException;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.CompanyTestProfile;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CampaignEquipmentManagerTest
{    
    @BeforeAll
    public void setupSuite() throws PWCGException
    {
        
        PWCGContext.getInstance().changeContext(FrontMapIdentifier.STALINGRAD_MAP);
    }
    
    @Test
    public void makeAircraftForCompanyTest () throws PWCGException
    {
        Campaign campaign = CampaignCache.makeCampaign(CompanyTestProfile.GROSS_DEUTSCHLAND_PROFILE);
        Equipment equipment = campaign.getEquipmentManager().getEquipmentForCompany(CompanyTestProfile.GROSS_DEUTSCHLAND_PROFILE.getCompanyId());
        for (EquippedTank tank : equipment.getActiveEquippedTanks().values())
        {
            Assertions.assertTrue(TankAttributeMapping.TIGER_I.getTankType().equals(tank.getType()));
        }
        

    }
    
    @Test
    public void replaceTanksForCompanyTest () throws PWCGException
    {
        List<Integer> tanksToReplace = new ArrayList<>();
        
        Campaign campaign = CampaignCache.makeCampaign(CompanyTestProfile.GROSS_DEUTSCHLAND_PROFILE);
        Equipment equipment = campaign.getEquipmentManager().getEquipmentForCompany(CompanyTestProfile.GROSS_DEUTSCHLAND_PROFILE.getCompanyId());
        int count = 0;
        for (EquippedTank tank : equipment.getActiveEquippedTanks().values())
        {
            Assertions.assertTrue(TankAttributeMapping.TIGER_I.getTankType().equals(tank.getType()));
            if (count == 0 || count == 3 || count == 6)
            {
                tanksToReplace.add(tank.getSerialNumber());
            }
            ++count;
        }
        
        Company company = PWCGContext.getInstance().getCompanyManager().getCompany(CompanyTestProfile.GROSS_DEUTSCHLAND_PROFILE.getCompanyId());
        TankTypeFactory tankTypeFactory = PWCGContext.getInstance().getTankTypeFactory();
        TankTypeInformation tankType = tankTypeFactory.getTankById(TankAttributeMapping.PANTHER_D.getTankType());
        campaign.getEquipmentManager().actOnEquipmentRequest(company, tanksToReplace, tankType.getDisplayName());
        
        int pantherCount = 0;
        for (EquippedTank tank : equipment.getActiveEquippedTanks().values())
        {
            if (tank.getType().contentEquals(TankAttributeMapping.PANTHER_D.getTankType()))
            {
                ++pantherCount;
            }
        }
        assert(pantherCount == 3);
    }
}
