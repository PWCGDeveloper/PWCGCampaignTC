package pwcg.aar.outofmission.phase2.resupply;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.company.Company;
import pwcg.campaign.company.CompanyManager;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.factory.ArmedServiceFactory;
import pwcg.campaign.resupply.CompanyNeedFactory;
import pwcg.campaign.resupply.CompanyNeedFactory.CompanyNeedType;
import pwcg.campaign.resupply.depot.EquipmentNeedForCompanysCalculator;
import pwcg.campaign.tank.Equipment;
import pwcg.campaign.tank.EquippedTank;
import pwcg.campaign.tank.TankStatus;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.product.bos.country.TCServiceManager;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.CompanyTestProfile;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class EquipmentReplacementWeightNeedTest
{
    private Campaign campaign;
    private static boolean runSetupOneTime = false;

    @BeforeAll
    public void setupSuite() throws PWCGException
    {
        if (!runSetupOneTime)
        {
            runSetupOneTime = true;
            
            campaign = CampaignCache.makeCampaign(CompanyTestProfile.GROSS_DEUTSCHLAND_PROFILE);
            deactivateAircraft();
        }
    }
    
    
    @Test
    public void testGermanEquipmentNeed() throws PWCGException
    {
        ArmedService service = ArmedServiceFactory.createServiceManager().getArmedService(TCServiceManager.WEHRMACHT);
        CompanyManager companyManager = PWCGContext.getInstance().getCompanyManager();
        List<Company> companysForService = companyManager.getActiveCompaniesForService(campaign.getDate(), service);
        
        CompanyNeedFactory companyNeedFactory = new CompanyNeedFactory(CompanyNeedType.EQUIPMENT);
        EquipmentNeedForCompanysCalculator equipmentReplacementWeightNeed = new EquipmentNeedForCompanysCalculator(campaign, companyNeedFactory);
        Map<String, Integer> tankNeedByArchType = equipmentReplacementWeightNeed.getAircraftNeedByArchType(companysForService);

        assert(tankNeedByArchType.containsKey("tiger"));
    }
    
    @Test
    public void testRussianEquipmentNeed() throws PWCGException
    {
        ArmedService service = ArmedServiceFactory.createServiceManager().getArmedService(TCServiceManager.SSV);
        CompanyManager companyManager = PWCGContext.getInstance().getCompanyManager();
        List<Company> companysForService = companyManager.getActiveCompaniesForService(campaign.getDate(), service);
        
        CompanyNeedFactory companyNeedFactory = new CompanyNeedFactory(CompanyNeedType.EQUIPMENT);
        EquipmentNeedForCompanysCalculator equipmentReplacementWeightNeed = new EquipmentNeedForCompanysCalculator(campaign, companyNeedFactory);
        Map<String, Integer> tankNeedByArchType = equipmentReplacementWeightNeed.getAircraftNeedByArchType(companysForService);

        assert(tankNeedByArchType.containsKey("t34"));
    }
    
    private void deactivateAircraft() throws PWCGException
    {
         Equipment wehrGD = campaign.getEquipmentManager().getEquipmentForCompany(CompanyTestProfile.GROSS_DEUTSCHLAND_PROFILE.getCompanyId());
         destroyPlanesInCompany(wehrGD, 2);

        Equipment ssv147 = campaign.getEquipmentManager().getEquipmentForCompany(CompanyTestProfile.TANK_DIVISION_147_PROFILE.getCompanyId());
        destroyPlanesInCompany(ssv147, 2);
    }


    private void destroyPlanesInCompany(Equipment companyEquipment, int numToDestroy) throws PWCGException
    {
        int numDestroyed = 0;
        for (EquippedTank equippedTank : companyEquipment.getActiveEquippedTanks().values())
        {
            equippedTank.setTankStatus(TankStatus.STATUS_DESTROYED);
            Date dateDestroyed = DateUtils.removeTimeDays(campaign.getDate(), 10);
            equippedTank.setDateRemovedFromService(dateDestroyed);
            ++numDestroyed;
            if (numDestroyed == numToDestroy)
            {
                break;
            }
        }
    }
}
