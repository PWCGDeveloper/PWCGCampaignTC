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
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.factory.ArmedServiceFactory;
import pwcg.campaign.resupply.CompanyNeedFactory;
import pwcg.campaign.resupply.CompanyNeedFactory.CompanyNeedType;
import pwcg.campaign.resupply.depot.EquipmentNeedForCompanysCalculator;
import pwcg.core.exception.PWCGException;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.CompanyTestProfile;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class EquipmentReplacementWeightNoNeedTest
{
    private Campaign campaign;

    @BeforeAll
    public void setupSuite() throws PWCGException
    {
        
        campaign = CampaignCache.makeCampaign(CompanyTestProfile.GROSS_DEUTSCHLAND_PROFILE);
    }
    
    
    @Test
    public void testGermanNeedNoLosses() throws PWCGException
    {
        ArmedService service = ArmedServiceFactory.createServiceManager().getArmedService(TCServiceManager.WEHRMACHT);
        CompanyManager companyManager = PWCGContext.getInstance().getCompanyManager();
        List<Company> companysForService = companyManager.getActiveCompaniesForService(campaign.getDate(), service);
        
        CompanyNeedFactory companyNeedFactory = new CompanyNeedFactory(CompanyNeedType.EQUIPMENT);
        EquipmentNeedForCompanysCalculator equipmentReplacementWeightNeed = new EquipmentNeedForCompanysCalculator(campaign, companyNeedFactory);
        Map<String, Integer> aircraftNeedByArchType = equipmentReplacementWeightNeed.getAircraftNeedByArchType(companysForService);

        assert(aircraftNeedByArchType.size() == 0);
    }
    
    @Test
    public void testRussianNeedNoLosses() throws PWCGException
    {
        ArmedService service = ArmedServiceFactory.createServiceManager().getArmedService(TCServiceManager.SSV);
        CompanyManager companyManager = PWCGContext.getInstance().getCompanyManager();
        List<Company> companysForService = companyManager.getActiveCompaniesForService(campaign.getDate(), service);
        
        CompanyNeedFactory companyNeedFactory = new CompanyNeedFactory(CompanyNeedType.EQUIPMENT);
        EquipmentNeedForCompanysCalculator equipmentReplacementWeightNeed = new EquipmentNeedForCompanysCalculator(campaign, companyNeedFactory);
        Map<String, Integer> aircraftNeedByArchType = equipmentReplacementWeightNeed.getAircraftNeedByArchType(companysForService);

        assert(aircraftNeedByArchType.size() == 0);
    }
    
    @Test
    public void testItalianNeedNoLosses() throws PWCGException
    {
        ArmedService service = ArmedServiceFactory.createServiceManager().getArmedService(20202);
        CompanyManager companyManager = PWCGContext.getInstance().getCompanyManager();
        List<Company> companysForService = companyManager.getActiveCompaniesForService(campaign.getDate(), service);
        
        CompanyNeedFactory companyNeedFactory = new CompanyNeedFactory(CompanyNeedType.EQUIPMENT);
        EquipmentNeedForCompanysCalculator equipmentReplacementWeightNeed = new EquipmentNeedForCompanysCalculator(campaign, companyNeedFactory);
        Map<String, Integer> aircraftNeedByArchType = equipmentReplacementWeightNeed.getAircraftNeedByArchType(companysForService);

        assert(aircraftNeedByArchType.size() == 0);
    }
}
