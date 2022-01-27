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
import pwcg.campaign.resupply.depot.EquipmentReplacementWeightByNeed;
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
public class EquipmentReplacementWeightByNeedTest
{
    private Campaign earlyCampaign;
    private Campaign lateCampaign;
    
    @BeforeAll
    public void setupSuite() throws PWCGException
    {
        
        earlyCampaign = CampaignCache.makeCampaign(CompanyTestProfile.GROSS_DEUTSCHLAND_PROFILE);
        lateCampaign = CampaignCache.makeCampaign(CompanyTestProfile.PANZER_LEHR_PROFILE);        
    }

    private void removePlanesFromCampaign(Campaign campaign) throws PWCGException
    {
        for (Equipment equipment : campaign.getEquipmentManager().getEquipmentAllCompanies().values())
        {
            int numDestroyedOverOneWeekAgo = 0;
            int numDestroyedlessThanOneWeekAgo = 0;
            for (EquippedTank equippedTank : equipment.getActiveEquippedTanks().values())
            {
                if (numDestroyedOverOneWeekAgo < 2)
                {
                    Date threeWeeksAgo = DateUtils.removeTimeDays(campaign.getDate(), 21);
                    ++numDestroyedOverOneWeekAgo;
                    equippedTank.setDateRemovedFromService(threeWeeksAgo);
                    equippedTank.setTankStatus(TankStatus.STATUS_DESTROYED);
                }
                else if (numDestroyedlessThanOneWeekAgo < 1)
                {
                    Date threeDaysAgo = DateUtils.removeTimeDays(campaign.getDate(), 3);
                    ++numDestroyedlessThanOneWeekAgo;
                    equippedTank.setDateRemovedFromService(threeDaysAgo);
                    equippedTank.setTankStatus(TankStatus.STATUS_DESTROYED);
                }
                else
                {
                    break;
                }
            }
        }
    }
    
    @Test
    public void testGermanReplacementArchTypes() throws PWCGException
    {
        removePlanesFromCampaign(earlyCampaign);
        
        ArmedService service = ArmedServiceFactory.createServiceManager().getArmedService(TCServiceManager.WEHRMACHT);
        CompanyManager companyManager = PWCGContext.getInstance().getCompanyManager();
        List<Company> companysForService = companyManager.getActiveCompaniesForService(earlyCampaign.getDate(), service);
        
        EquipmentReplacementWeightByNeed equipmentReplacementWeightUsage = new EquipmentReplacementWeightByNeed(earlyCampaign);
        Map<String, Integer> tankUsageByArchType = equipmentReplacementWeightUsage.getAircraftNeedByArchType(companysForService);

        assert(tankUsageByArchType.containsKey("pziii"));
        assert(tankUsageByArchType.containsKey("pziv"));
        assert(tankUsageByArchType.containsKey("tiger"));

        assert(!tankUsageByArchType.containsKey("elefant"));
        assert(!tankUsageByArchType.containsKey("panther"));
        
        int pziiiWeight = tankUsageByArchType.get("pziii");
        int tigerWeight = tankUsageByArchType.get("tiger");

        assert(pziiiWeight > tigerWeight);
    }

    @Test
    public void testRussianReplacementArchTypes() throws PWCGException
    {
        removePlanesFromCampaign(earlyCampaign);

        ArmedService service = ArmedServiceFactory.createServiceManager().getArmedService(TCServiceManager.SSV);
        CompanyManager companyManager = PWCGContext.getInstance().getCompanyManager();
        List<Company> companysForService = companyManager.getActiveCompaniesForService(earlyCampaign.getDate(), service);
        
        EquipmentReplacementWeightByNeed equipmentReplacementWeightUsage = new EquipmentReplacementWeightByNeed(earlyCampaign);
        Map<String, Integer> tankUsageByArchType = equipmentReplacementWeightUsage.getAircraftNeedByArchType(companysForService);

        assert(tankUsageByArchType.containsKey("t34"));
        assert(tankUsageByArchType.containsKey("kv1"));

        assert(!tankUsageByArchType.containsKey("su152"));
        
        int t34Weight = tankUsageByArchType.get("t34");
        int kv1Weight = tankUsageByArchType.get("kv1");

        assert(t34Weight > kv1Weight);
    }
    
    @Test
    public void testGermanLateReplacementArchTypes() throws PWCGException
    {
        removePlanesFromCampaign(lateCampaign);

        ArmedService service = ArmedServiceFactory.createServiceManager().getArmedService(TCServiceManager.WEHRMACHT);
        CompanyManager companyManager = PWCGContext.getInstance().getCompanyManager();
        List<Company> companysForService = companyManager.getActiveCompaniesForService(lateCampaign.getDate(), service);
        
        EquipmentReplacementWeightByNeed equipmentReplacementWeightUsage = new EquipmentReplacementWeightByNeed(lateCampaign);
        Map<String, Integer> tankUsageByArchType = equipmentReplacementWeightUsage.getAircraftNeedByArchType(companysForService);

        assert(tankUsageByArchType.containsKey("pziv"));
        assert(tankUsageByArchType.containsKey("tiger"));
        assert(tankUsageByArchType.containsKey("elefant"));
        assert(tankUsageByArchType.containsKey("panther"));

        assert(!tankUsageByArchType.containsKey("pziii"));

        int tigerWeight = tankUsageByArchType.get("tiger");
        int pzivWeight = tankUsageByArchType.get("pziv");
        
        assert(pzivWeight > tigerWeight);
    }

    @Test
    public void testAmericanReplacementArchTypes() throws PWCGException
    {
        removePlanesFromCampaign(lateCampaign);

        ArmedService service = ArmedServiceFactory.createServiceManager().getArmedService(TCServiceManager.US_ARMY);
        CompanyManager companyManager = PWCGContext.getInstance().getCompanyManager();
        List<Company> companysForService = companyManager.getActiveCompaniesForService(lateCampaign.getDate(), service);
        
        EquipmentReplacementWeightByNeed equipmentReplacementWeightUsage = new EquipmentReplacementWeightByNeed(lateCampaign);
        Map<String, Integer> tankUsageByArchType = equipmentReplacementWeightUsage.getAircraftNeedByArchType(companysForService);

        assert(tankUsageByArchType.containsKey("sherman"));
    }

    @Test
    public void testBritishReplacementArchTypes() throws PWCGException
    {
        removePlanesFromCampaign(lateCampaign);

        ArmedService service = ArmedServiceFactory.createServiceManager().getArmedService(TCServiceManager.BRITISH_ARMY);
        CompanyManager companyManager = PWCGContext.getInstance().getCompanyManager();
        List<Company> companysForService = companyManager.getActiveCompaniesForService(lateCampaign.getDate(), service);
        
        EquipmentReplacementWeightByNeed equipmentReplacementWeightUsage = new EquipmentReplacementWeightByNeed(lateCampaign);
        Map<String, Integer> tankUsageByArchType = equipmentReplacementWeightUsage.getAircraftNeedByArchType(companysForService);

        assert(tankUsageByArchType.containsKey("sherman"));
    }
}
