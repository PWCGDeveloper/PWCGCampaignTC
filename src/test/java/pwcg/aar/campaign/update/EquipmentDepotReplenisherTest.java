package pwcg.aar.campaign.update;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import pwcg.campaign.Campaign;
import pwcg.campaign.company.Company;
import pwcg.campaign.company.CompanyManager;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.resupply.depot.EquipmentDepot;
import pwcg.campaign.resupply.depot.EquipmentDepotReplenisher;
import pwcg.campaign.resupply.depot.EquipmentReplacementUtils;
import pwcg.campaign.resupply.depot.EquipmentUpgradeRecord;
import pwcg.campaign.tank.CompanyTankAssignment;
import pwcg.campaign.tank.EquippedTank;
import pwcg.campaign.tank.TankArchType;
import pwcg.core.exception.PWCGException;
import pwcg.product.bos.country.TCServiceManager;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.CompanyTestProfile;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class EquipmentDepotReplenisherTest
{
    private Campaign campaign;

    @Mock
    EquippedTank me109E7;
    @Mock
    EquippedTank me109K4;

    @BeforeAll
    public void setupSuite() throws PWCGException
    {
        
        campaign = CampaignCache.makeCampaign(CompanyTestProfile.GROSS_DEUTSCHLAND_PROFILE);
    }

    @BeforeEach
    public void setupTest() throws PWCGException
    {
        Mockito.when(me109E7.getArchType()).thenReturn("pziii");
        Mockito.when(me109E7.getGoodness()).thenReturn(10);
        Mockito.when(me109K4.getArchType()).thenReturn("pziii");
        Mockito.when(me109K4.getGoodness()).thenReturn(95);
    }

    @Test
    public void testArchTypesInProductionForServiceLife() throws PWCGException
    {
        CompanyManager companymanager = PWCGContext.getInstance().getCompanyManager();
        for (Company company : companymanager.getAllCompanies())
        {
            for (CompanyTankAssignment planeAssignment : company.getPlaneAssignments())
            {
                TankArchType planeArchType = PWCGContext.getInstance().getPlayerTankTypeFactory().getTankArchType(planeAssignment.getArchType());
                String selectedTankType = EquipmentReplacementUtils.getTypeForReplacement(planeAssignment.getCompanyWithdrawal(), planeArchType);
                Assertions.assertTrue (selectedTankType.length() > 0);
            }
        }
    }

    @Test
    public void testUpdateWithReplacementsGermans() throws PWCGException
    {
        Map<Integer, Integer> replacementsAvailableBefore = determineReplacementsAvailableByService();
        EquipmentDepotReplenisher equipmentReplacementUpdater = new EquipmentDepotReplenisher(campaign);
        equipmentReplacementUpdater.replenishDepotsForServices();
        Map<Integer, Integer> replacementsAvailableAfter = determineReplacementsAvailableByService();
        validateReplacements(replacementsAvailableBefore, replacementsAvailableAfter);
    }

    @Test
    public void testUpgradeWithWorsePlane() throws PWCGException
    {
        EquipmentDepot equipmentDepot = campaign.getEquipmentManager().getEquipmentDepotForService(TCServiceManager.WEHRMACHT);
        EquipmentUpgradeRecord upgradeRecord = equipmentDepot.getUpgrade(me109E7);
        Assertions.assertTrue (upgradeRecord != null);
        Assertions.assertTrue (upgradeRecord.getUpgrade().getArchType().equals("pziii"));
        Assertions.assertTrue (upgradeRecord.getUpgrade().getGoodness() > 10);

        int upgradeSerialNumber = upgradeRecord.getUpgrade().getSerialNumber();

        EquippedTank planeInDepot = equipmentDepot.getTankFromDepot(upgradeSerialNumber);
        Assertions.assertTrue (planeInDepot != null);

        planeInDepot = equipmentDepot.removeEquippedTankFromDepot(upgradeSerialNumber);
        Assertions.assertTrue (planeInDepot != null);

        planeInDepot = equipmentDepot.getTankFromDepot(upgradeSerialNumber);
        Assertions.assertTrue (planeInDepot == null);
    }

    @Test
    public void testUpgradeWithBetterPlane() throws PWCGException
    {
        EquipmentDepot equipmentDepo = campaign.getEquipmentManager().getEquipmentDepotForService(TCServiceManager.WEHRMACHT);
        EquipmentUpgradeRecord replacementTank = equipmentDepo.getUpgrade(me109K4);
        Assertions.assertTrue (replacementTank == null);
    }

    private Map<Integer, Integer> determineReplacementsAvailableByService() throws PWCGException
    {
        Map<Integer, Integer> replacementsAvailable = new HashMap<>();
        for (Integer serviceId : campaign.getEquipmentManager().getServiceIdsForDepots())
        {
            EquipmentDepot equipmentDepot = campaign.getEquipmentManager().getEquipmentDepotForService(serviceId);
            replacementsAvailable.put(serviceId, equipmentDepot.getDepotSize());
        }
        return replacementsAvailable;
    }

    private void validateReplacements(Map<Integer, Integer> replacementsAvailableBefore, Map<Integer, Integer> replacementsAvailableAfter) throws PWCGException
    {
        for (Integer serviceId : replacementsAvailableBefore.keySet())
        {
            Assertions.assertTrue (replacementsAvailableAfter.get(serviceId) >= replacementsAvailableBefore.get(serviceId));
        }
    }
}
