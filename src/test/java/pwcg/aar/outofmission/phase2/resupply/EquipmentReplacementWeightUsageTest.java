package pwcg.aar.outofmission.phase2.resupply;

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
import pwcg.campaign.resupply.depot.EquipmentReplacementWeightUsage;
import pwcg.core.exception.PWCGException;
import pwcg.product.bos.country.TCServiceManager;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.CompanyTestProfile;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class EquipmentReplacementWeightUsageTest
{
    private static Campaign earlyCampaign;
    private static Campaign lateCampaign;

    @BeforeAll
    public void setupSuite() throws PWCGException
    {
        
        earlyCampaign = CampaignCache.makeCampaign(CompanyTestProfile.GROSS_DEUTSCHLAND_PROFILE);
        lateCampaign = CampaignCache.makeCampaign(CompanyTestProfile.PANZER_LEHR_PROFILE);
    }
    
    @Test
    public void testGermanReplacementArchTypes() throws PWCGException
    {
        ArmedService service = ArmedServiceFactory.createServiceManager().getArmedService(TCServiceManager.WEHRMACHT);
        CompanyManager companyManager = PWCGContext.getInstance().getCompanyManager();
        List<Company> companysForService = companyManager.getActiveCompaniesForService(earlyCampaign.getDate(), service);
        
        EquipmentReplacementWeightUsage equipmentReplacementWeightUsage = new EquipmentReplacementWeightUsage(earlyCampaign.getDate());
        Map<String, Integer> tankUsageByArchType = equipmentReplacementWeightUsage.getTankUsageByArchType(companysForService);

        assert(tankUsageByArchType.containsKey("pziii"));
        assert(tankUsageByArchType.containsKey("pziv"));

        assert(!tankUsageByArchType.containsKey("panther"));
    }

    @Test
    public void testRussianReplacementArchTypes() throws PWCGException
    {
        ArmedService service = ArmedServiceFactory.createServiceManager().getArmedService(TCServiceManager.SSV);
        CompanyManager companyManager = PWCGContext.getInstance().getCompanyManager();
        List<Company> companysForService = companyManager.getActiveCompaniesForService(earlyCampaign.getDate(), service);
        
        EquipmentReplacementWeightUsage equipmentReplacementWeightUsage = new EquipmentReplacementWeightUsage(earlyCampaign.getDate());
        Map<String, Integer> tankUsageByArchType = equipmentReplacementWeightUsage.getTankUsageByArchType(companysForService);

        assert(tankUsageByArchType.containsKey("t34"));
        assert(tankUsageByArchType.containsKey("kv1")); 
    }

    @Test
    public void testGermanLateReplacementArchTypes() throws PWCGException
    {
        ArmedService service = ArmedServiceFactory.createServiceManager().getArmedService(TCServiceManager.WEHRMACHT);
        CompanyManager companyManager = PWCGContext.getInstance().getCompanyManager();
        List<Company> companysForService = companyManager.getActiveCompaniesForService(lateCampaign.getDate(), service);
        
        EquipmentReplacementWeightUsage equipmentReplacementWeightUsage = new EquipmentReplacementWeightUsage(lateCampaign.getDate());
        Map<String, Integer> tankUsageByArchType = equipmentReplacementWeightUsage.getTankUsageByArchType(companysForService);

        assert(tankUsageByArchType.containsKey("pziv"));
        assert(tankUsageByArchType.containsKey("tiger"));
        assert(tankUsageByArchType.containsKey("panther"));

        assert(!tankUsageByArchType.containsKey("pziii"));        
    }

    @Test
    public void testAmericanReplacementArchTypes() throws PWCGException
    {
        ArmedService service = ArmedServiceFactory.createServiceManager().getArmedService(TCServiceManager.US_ARMY);
        CompanyManager companyManager = PWCGContext.getInstance().getCompanyManager();
        List<Company> companysForService = companyManager.getActiveCompaniesForService(lateCampaign.getDate(), service);
        
        EquipmentReplacementWeightUsage equipmentReplacementWeightUsage = new EquipmentReplacementWeightUsage(lateCampaign.getDate());
        Map<String, Integer> tankUsageByArchType = equipmentReplacementWeightUsage.getTankUsageByArchType(companysForService);

        assert(tankUsageByArchType.containsKey("sherman"));
    }

    @Test
    public void testBritishReplacementArchTypes() throws PWCGException
    {
        ArmedService service = ArmedServiceFactory.createServiceManager().getArmedService(TCServiceManager.BRITISH_ARMY);
        CompanyManager companyManager = PWCGContext.getInstance().getCompanyManager();
        List<Company> companysForService = companyManager.getActiveCompaniesForService(lateCampaign.getDate(), service);
        
        EquipmentReplacementWeightUsage equipmentReplacementWeightUsage = new EquipmentReplacementWeightUsage(lateCampaign.getDate());
        Map<String, Integer> tankUsageByArchType = equipmentReplacementWeightUsage.getTankUsageByArchType(companysForService);

        assert(tankUsageByArchType.containsKey("sherman"));
    }
}
