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
        Map<String, Integer> aircraftUsageByArchType = equipmentReplacementWeightUsage.getAircraftNeedByArchType(companysForService);

        assert(aircraftUsageByArchType.containsKey("bf109"));
        assert(aircraftUsageByArchType.containsKey("bf110"));
        assert(aircraftUsageByArchType.containsKey("he111"));
        assert(aircraftUsageByArchType.containsKey("ju87"));
        assert(aircraftUsageByArchType.containsKey("ju88"));
        assert(aircraftUsageByArchType.containsKey("ju52"));

        assert(!aircraftUsageByArchType.containsKey("hs129"));
        assert(!aircraftUsageByArchType.containsKey("fw190"));
        assert(!aircraftUsageByArchType.containsKey("yak"));
        assert(!aircraftUsageByArchType.containsKey("il2"));
        assert(!aircraftUsageByArchType.containsKey("fw190d"));
        assert(!aircraftUsageByArchType.containsKey("me262"));
        
        int me109Weight = aircraftUsageByArchType.get("bf109");
        int me110Weight = aircraftUsageByArchType.get("bf110");
        int ju52Weight = aircraftUsageByArchType.get("ju52");
        int ju87Weight = aircraftUsageByArchType.get("ju87");
        int ju88Weight = aircraftUsageByArchType.get("ju88");
        int he111Weight = aircraftUsageByArchType.get("he111");
        assert(me109Weight > ju88Weight);
        assert(ju87Weight > he111Weight);
        assert(he111Weight > ju52Weight);
        assert(me110Weight == ju52Weight);
    }

    @Test
    public void testRussianReplacementArchTypes() throws PWCGException
    {
        removePlanesFromCampaign(earlyCampaign);

        ArmedService service = ArmedServiceFactory.createServiceManager().getArmedService(TCServiceManager.SSV);
        CompanyManager companyManager = PWCGContext.getInstance().getCompanyManager();
        List<Company> companysForService = companyManager.getActiveCompaniesForService(earlyCampaign.getDate(), service);
        
        EquipmentReplacementWeightByNeed equipmentReplacementWeightUsage = new EquipmentReplacementWeightByNeed(earlyCampaign);
        Map<String, Integer> aircraftUsageByArchType = equipmentReplacementWeightUsage.getAircraftNeedByArchType(companysForService);

        assert(aircraftUsageByArchType.containsKey("il2"));
        assert(aircraftUsageByArchType.containsKey("i16"));
        assert(aircraftUsageByArchType.containsKey("lagg"));
        assert(aircraftUsageByArchType.containsKey("pe2"));
        assert(aircraftUsageByArchType.containsKey("mig3"));
        assert(aircraftUsageByArchType.containsKey("p40"));

        assert(!aircraftUsageByArchType.containsKey("yak"));
        assert(!aircraftUsageByArchType.containsKey("bf109"));
        assert(!aircraftUsageByArchType.containsKey("he111"));
        
        int il2Weight = aircraftUsageByArchType.get("il2");
        int i16Weight = aircraftUsageByArchType.get("i16");
        int laggWeight = aircraftUsageByArchType.get("lagg");
        int pe2Weight = aircraftUsageByArchType.get("pe2");
        int mig3Weight = aircraftUsageByArchType.get("mig3");
        int p40Weight = aircraftUsageByArchType.get("p40");
        assert(il2Weight > i16Weight);
        assert(mig3Weight > laggWeight);
        assert(pe2Weight > p40Weight);
    }
    
    @Test
    public void testItalianReplacementArchTypes() throws PWCGException
    {
        removePlanesFromCampaign(earlyCampaign);

        ArmedService service = ArmedServiceFactory.createServiceManager().getArmedService(20202);
        CompanyManager companyManager = PWCGContext.getInstance().getCompanyManager();
        List<Company> companysForService = companyManager.getActiveCompaniesForService(earlyCampaign.getDate(), service);
        
        EquipmentReplacementWeightByNeed equipmentReplacementWeightUsage = new EquipmentReplacementWeightByNeed(earlyCampaign);
        Map<String, Integer> aircraftUsageByArchType = equipmentReplacementWeightUsage.getAircraftNeedByArchType(companysForService);

        assert(aircraftUsageByArchType.containsKey("mc200"));
        assert(aircraftUsageByArchType.size() == 1);
    }
    
    @Test
    public void testGermanLateReplacementArchTypes() throws PWCGException
    {
        removePlanesFromCampaign(lateCampaign);

        ArmedService service = ArmedServiceFactory.createServiceManager().getArmedService(TCServiceManager.WEHRMACHT);
        CompanyManager companyManager = PWCGContext.getInstance().getCompanyManager();
        List<Company> companysForService = companyManager.getActiveCompaniesForService(lateCampaign.getDate(), service);
        
        EquipmentReplacementWeightByNeed equipmentReplacementWeightUsage = new EquipmentReplacementWeightByNeed(lateCampaign);
        Map<String, Integer> aircraftUsageByArchType = equipmentReplacementWeightUsage.getAircraftNeedByArchType(companysForService);

        assert(aircraftUsageByArchType.containsKey("bf109"));
        assert(aircraftUsageByArchType.containsKey("ju88"));
        assert(aircraftUsageByArchType.containsKey("fw190"));
        assert(aircraftUsageByArchType.containsKey("fw190d"));
        assert(aircraftUsageByArchType.containsKey("me262"));
        assert(aircraftUsageByArchType.containsKey("ju52"));
        assert(aircraftUsageByArchType.containsKey("bf110"));        
        assert(aircraftUsageByArchType.containsKey("he111"));
        assert(aircraftUsageByArchType.containsKey("ju87"));
        assert(aircraftUsageByArchType.containsKey("hs129"));        
        
        int me109Weight = aircraftUsageByArchType.get("bf109");
        int ju88Weight = aircraftUsageByArchType.get("ju88");
        
        assert(me109Weight > ju88Weight);
    }

    @Test
    public void testAmericanReplacementArchTypes() throws PWCGException
    {
        removePlanesFromCampaign(lateCampaign);

        ArmedService service = ArmedServiceFactory.createServiceManager().getArmedService(TCServiceManager.US_ARMY);
        CompanyManager companyManager = PWCGContext.getInstance().getCompanyManager();
        List<Company> companysForService = companyManager.getActiveCompaniesForService(lateCampaign.getDate(), service);
        
        EquipmentReplacementWeightByNeed equipmentReplacementWeightUsage = new EquipmentReplacementWeightByNeed(lateCampaign);
        Map<String, Integer> aircraftUsageByArchType = equipmentReplacementWeightUsage.getAircraftNeedByArchType(companysForService);

        assert(aircraftUsageByArchType.containsKey("p47"));
    }

    @Test
    public void testBritishReplacementArchTypes() throws PWCGException
    {
        removePlanesFromCampaign(lateCampaign);

        ArmedService service = ArmedServiceFactory.createServiceManager().getArmedService(TCServiceManager.BRITISH_ARMY);
        CompanyManager companyManager = PWCGContext.getInstance().getCompanyManager();
        List<Company> companysForService = companyManager.getActiveCompaniesForService(lateCampaign.getDate(), service);
        
        EquipmentReplacementWeightByNeed equipmentReplacementWeightUsage = new EquipmentReplacementWeightByNeed(lateCampaign);
        Map<String, Integer> aircraftUsageByArchType = equipmentReplacementWeightUsage.getAircraftNeedByArchType(companysForService);

        assert(aircraftUsageByArchType.containsKey("spitfire"));
    }
}
