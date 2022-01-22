package pwcg.aar.outofmission.phase2.resupply;

import java.util.ArrayList;
import java.util.List;

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
import pwcg.campaign.resupply.depot.EquipmentDepot;
import pwcg.campaign.resupply.equipment.EquipmentUpgradeHandler;
import pwcg.campaign.tank.Equipment;
import pwcg.campaign.tank.EquippedTank;
import pwcg.campaign.tank.TankStatus;
import pwcg.campaign.tank.ITankTypeFactory;
import pwcg.campaign.tank.TankTypeInformation;
import pwcg.core.exception.PWCGException;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.CompanyTestProfile;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class EquipmentUpgradeHandlerTest
{
    private Campaign campaign;

    @Mock
    private ArmedService armedService;

    @BeforeAll
    public void setupSuite() throws PWCGException
    {
        
        campaign = CampaignCache.makeCampaign(CompanyTestProfile.GROSS_DEUTSCHLAND_PROFILE);
    }

    @BeforeEach
    public void setupTest() throws PWCGException
    {
        Mockito.when(armedService.getServiceId()).thenReturn(20101);
    }

    @Test
    public void testEquipmentUpgradeForPlayer() throws PWCGException
    {
        ITankTypeFactory planeTypeFactory = PWCGContext.getInstance().getPlayerTankTypeFactory();
        EquipmentDepot equipmentDepotBeforeTest = campaign.getEquipmentManager().getEquipmentDepotForService(armedService.getServiceId());

        // Add good planes to the depo
        List<Integer> veryGoodPlanesInDepot = new ArrayList<>();
        for (int i = 0; i < 2; ++i)
        {
            TankTypeInformation planeTypeMe109K4 = planeTypeFactory.createTankTypeByType("bf109k4");
            EquippedTank me109K4ForDepot = new EquippedTank(planeTypeMe109K4, campaign.getSerialNumber().getNextTankSerialNumber(), -1,
                    TankStatus.STATUS_DEPOT);
            equipmentDepotBeforeTest.addPlaneToDepot(me109K4ForDepot);
            veryGoodPlanesInDepot.add(me109K4ForDepot.getSerialNumber());
        }

        for (int veryGoodPlaneInDepot : veryGoodPlanesInDepot)
        {
            Assertions.assertTrue (equipmentDepotBeforeTest.getPlaneFromDepot(veryGoodPlaneInDepot) != null);
        }

        // replace planes in company with different quality 109s, but all worse
        // than the very good planes in the depot
        Company playerCompany = campaign.determinePlayerCompanies().get(0);
        Equipment equipmentForCompanyBeforeTest = campaign.getEquipmentManager().getEquipmentForCompany(playerCompany.getCompanyId());
        for (EquippedTank planeInCompanyBeforeTest : equipmentForCompanyBeforeTest.getActiveEquippedTanks().values())
        {
            equipmentForCompanyBeforeTest.removeEquippedTank(planeInCompanyBeforeTest.getSerialNumber());
        }

        List<Integer> planesThatShouldBeReplaced = new ArrayList<>();
        for (int i = 0; i < 2; ++i)
        {
            TankTypeInformation planeTypeMe109F2 = planeTypeFactory.createTankTypeByType("bf109f2");
            EquippedTank me109F2ForCompany = new EquippedTank(planeTypeMe109F2, campaign.getSerialNumber().getNextTankSerialNumber(), -1,
                    TankStatus.STATUS_DEPLOYED);
            equipmentForCompanyBeforeTest.addEquippedTankToCompany(campaign, playerCompany.getCompanyId(), me109F2ForCompany);
            planesThatShouldBeReplaced.add(me109F2ForCompany.getSerialNumber());
        }

        List<Integer> planesThatShouldNotBeReplaced = new ArrayList<>();
        for (int i = 0; i < 10; ++i)
        {
            TankTypeInformation planeTypeMe109F4 = planeTypeFactory.createTankTypeByType("bf109f4");
            EquippedTank me109F4ForCompany = new EquippedTank(planeTypeMe109F4, campaign.getSerialNumber().getNextTankSerialNumber(), -1,
                    TankStatus.STATUS_DEPLOYED);
            equipmentForCompanyBeforeTest.addEquippedTankToCompany(campaign, playerCompany.getCompanyId(), me109F4ForCompany);
            planesThatShouldNotBeReplaced.add(me109F4ForCompany.getSerialNumber());
        }

        // The better planes should be left in the company
        for (int planeThatShouldNotBeReplaced : planesThatShouldNotBeReplaced)
        {
            Assertions.assertTrue (equipmentForCompanyBeforeTest.getEquippedTank(planeThatShouldNotBeReplaced) != null);
        }

        // Run the upgrade
        EquipmentUpgradeHandler equipmentUpgradeHandler = new EquipmentUpgradeHandler(campaign);
        equipmentUpgradeHandler.upgradeEquipment(armedService);

        // The worst planes in the companys should be replaced and now be in
        // the depot
        EquipmentDepot equipmentDepotAfterTest = campaign.getEquipmentManager().getEquipmentDepotForService(armedService.getServiceId());
        Equipment equipmentForCompanyAfterTest = campaign.getEquipmentManager().getEquipmentForCompany(playerCompany.getCompanyId());
        for (int planeThatShouldBeReplaced : planesThatShouldBeReplaced)
        {
            Assertions.assertTrue (equipmentForCompanyAfterTest.getEquippedTank(planeThatShouldBeReplaced) == null);
            Assertions.assertTrue (equipmentDepotAfterTest.getPlaneFromDepot(planeThatShouldBeReplaced) != null);
        }

        // The better planes should be left in the company
        for (int planeThatShouldNotBeReplaced : planesThatShouldNotBeReplaced)
        {
            Assertions.assertTrue (equipmentForCompanyAfterTest.getEquippedTank(planeThatShouldNotBeReplaced) != null);
            Assertions.assertTrue (equipmentDepotAfterTest.getPlaneFromDepot(planeThatShouldNotBeReplaced) == null);
        }

        // The very good planes in the depot should be moved from the depot to
        // the company
        for (int veryGoodPlaneInDepot : veryGoodPlanesInDepot)
        {
            Assertions.assertTrue (equipmentForCompanyAfterTest.getEquippedTank(veryGoodPlaneInDepot) != null);
            Assertions.assertTrue (equipmentDepotAfterTest.getPlaneFromDepot(veryGoodPlaneInDepot) == null);
        }
    }

    @Test
    public void testEquipmentUpgradeForAI() throws PWCGException
    {
        ITankTypeFactory planeTypeFactory = PWCGContext.getInstance().getPlayerTankTypeFactory();
        EquipmentDepot equipmentDepotBeforeTest = campaign.getEquipmentManager().getEquipmentDepotForService(armedService.getServiceId());

        // Add good planes to the depo
        List<Integer> veryGoodPlanesInDepot = new ArrayList<>();
        for (int i = 0; i < 2; ++i)
        {
            TankTypeInformation planeTypeMe109K4 = planeTypeFactory.createTankTypeByType("bf109k4");
            EquippedTank me109K4ForDepot = new EquippedTank(planeTypeMe109K4, campaign.getSerialNumber().getNextTankSerialNumber(), -1,
                    TankStatus.STATUS_DEPOT);
            equipmentDepotBeforeTest.addPlaneToDepot(me109K4ForDepot);
            veryGoodPlanesInDepot.add(me109K4ForDepot.getSerialNumber());
        }

        for (int veryGoodPlaneInDepot : veryGoodPlanesInDepot)
        {
            Assertions.assertTrue (equipmentDepotBeforeTest.getPlaneFromDepot(veryGoodPlaneInDepot) != null);
        }

        // replace planes in player company with very good quality 109s, to
        // avoid the need for replacement
        Company playerCompany = campaign.determinePlayerCompanies().get(0);
        Equipment equipmentForCompanyBeforeTest = campaign.getEquipmentManager().getEquipmentForCompany(playerCompany.getCompanyId());
        for (EquippedTank planeInCompanyBeforeTest : equipmentForCompanyBeforeTest.getActiveEquippedTanks().values())
        {
            equipmentForCompanyBeforeTest.removeEquippedTank(planeInCompanyBeforeTest.getSerialNumber());
        }

        List<Integer> originalPlayerCompanyPlanes = new ArrayList<>();
        for (int i = 0; i < 16; ++i)
        {
            TankTypeInformation planeTypeMe109K4 = planeTypeFactory.createTankTypeByType("bf109k4");
            EquippedTank me109F2ForCompany = new EquippedTank(planeTypeMe109K4, campaign.getSerialNumber().getNextTankSerialNumber(), -1,
                    TankStatus.STATUS_DEPLOYED);
            equipmentForCompanyBeforeTest.addEquippedTankToCompany(campaign, playerCompany.getCompanyId(), me109F2ForCompany);
            originalPlayerCompanyPlanes.add(me109F2ForCompany.getSerialNumber());
        }

        // Run the upgrade
        EquipmentUpgradeHandler equipmentUpgradeHandler = new EquipmentUpgradeHandler(campaign);
        equipmentUpgradeHandler.upgradeEquipment(armedService);

        // The planes in the depot should be removed and sent to companys
        EquipmentDepot equipmentDepotAfterTest = campaign.getEquipmentManager().getEquipmentDepotForService(armedService.getServiceId());
        for (int depotPlane : veryGoodPlanesInDepot)
        {
            Assertions.assertTrue (equipmentDepotAfterTest.getPlaneFromDepot(depotPlane) == null);
        }

        // No planes should be replaced in the player company
        Equipment equipmentForPlayerCompanyAfterTest = campaign.getEquipmentManager().getEquipmentForCompany(playerCompany.getCompanyId());
        for (int depotPlane : veryGoodPlanesInDepot)
        {
            Assertions.assertTrue (equipmentForPlayerCompanyAfterTest.getEquippedTank(depotPlane) == null);
        }

        // the planes should be in a company
        for (int depotPlaneSerialNumber : veryGoodPlanesInDepot)
        {
            boolean planeIsInCompany = false;
            for (Equipment companyEquipment : campaign.getEquipmentManager().getEquipmentAllCompanies().values())
            {
                for (int companyPlaneSerialNumber : companyEquipment.getAvailableDepotTanks().keySet())
                {
                    if (depotPlaneSerialNumber == companyPlaneSerialNumber)
                    {
                        planeIsInCompany = true;
                    }
                }
            }
            Assertions.assertTrue (planeIsInCompany);
        }
    }

    @Test
    public void testEquipmentUpgradeNotNeeded() throws PWCGException
    {
        ITankTypeFactory planeTypeFactory = PWCGContext.getInstance().getPlayerTankTypeFactory();

        // Clear out the depot and replace with a couple of bad planes
        EquipmentDepot equipmentDepotBeforeTest = campaign.getEquipmentManager().getEquipmentDepotForService(armedService.getServiceId());
        for (EquippedTank planeInCompanyBeforeTest : equipmentDepotBeforeTest.getAllPlanesInDepot())
        {
            equipmentDepotBeforeTest.removeEquippedPlaneFromDepot(planeInCompanyBeforeTest.getSerialNumber());
        }

        List<Integer> badPlanesInDepot = new ArrayList<>();
        for (int i = 0; i < 2; ++i)
        {
            TankTypeInformation planeTypeMe109F2 = planeTypeFactory.createTankTypeByType("bf109f2");
            EquippedTank me109F2ForDepot = new EquippedTank(planeTypeMe109F2, campaign.getSerialNumber().getNextTankSerialNumber(), -1,
                    TankStatus.STATUS_DEPOT);
            equipmentDepotBeforeTest.addPlaneToDepot(me109F2ForDepot);
            badPlanesInDepot.add(me109F2ForDepot.getSerialNumber());
        }

        // Clear out the company and add better planes than we have in the
        // depot
        Company playerCompany = campaign.determinePlayerCompanies().get(0);
        Equipment equipmentForCompanyBeforeTest = campaign.getEquipmentManager().getEquipmentForCompany(playerCompany.getCompanyId());

        for (EquippedTank planeInCompanyBeforeTest : equipmentForCompanyBeforeTest.getActiveEquippedTanks().values())
        {
            equipmentForCompanyBeforeTest.removeEquippedTank(planeInCompanyBeforeTest.getSerialNumber());
        }

        List<Integer> goodPlanesInTheCompany = new ArrayList<>();
        for (int i = 0; i < 12; ++i)
        {
            TankTypeInformation planeTypeMe109K4 = planeTypeFactory.createTankTypeByType("bf109k4");
            EquippedTank me109K4ForCompany = new EquippedTank(planeTypeMe109K4, campaign.getSerialNumber().getNextTankSerialNumber(), -1,
                    TankStatus.STATUS_DEPLOYED);
            equipmentForCompanyBeforeTest.addEquippedTankToCompany(campaign, playerCompany.getCompanyId(), me109K4ForCompany);
            goodPlanesInTheCompany.add(me109K4ForCompany.getSerialNumber());
        }

        // Run the upgrade
        EquipmentUpgradeHandler equipmentUpgradeHandler = new EquipmentUpgradeHandler(campaign);
        equipmentUpgradeHandler.upgradeEquipment(armedService);

        EquipmentDepot equipmentDepotAfterTest = campaign.getEquipmentManager().getEquipmentDepotForService(armedService.getServiceId());
        Equipment equipmentForCompanyAfterTest = campaign.getEquipmentManager().getEquipmentForCompany(playerCompany.getCompanyId());

        // Good planes in the company should stay in the company
        for (int planeThatShouldNotBeReplaced : goodPlanesInTheCompany)
        {
            Assertions.assertTrue (equipmentForCompanyAfterTest.getEquippedTank(planeThatShouldNotBeReplaced) != null);
            Assertions.assertTrue (equipmentDepotAfterTest.getPlaneFromDepot(planeThatShouldNotBeReplaced) == null);
        }

        // Bad planes in the depot should stay in the depot
        for (int badlaneInDepot : badPlanesInDepot)
        {
            Assertions.assertTrue (equipmentForCompanyAfterTest.getEquippedTank(badlaneInDepot) == null);
            Assertions.assertTrue (equipmentDepotAfterTest.getPlaneFromDepot(badlaneInDepot) != null);
        }
    }
}
