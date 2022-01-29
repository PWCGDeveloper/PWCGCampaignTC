package pwcg.aar.outofmission.phase2.resupply;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
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
import pwcg.campaign.context.Country;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.resupply.depot.EquipmentDepot;
import pwcg.campaign.resupply.equipment.EquipmentUpgradeHandler;
import pwcg.campaign.tank.Equipment;
import pwcg.campaign.tank.EquippedTank;
import pwcg.campaign.tank.TankEquipmentFactory;
import pwcg.core.exception.PWCGException;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.CompanyTestProfile;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class EquipmentUpgradeHandlerTest
{
    @Mock
    private ArmedService armedService;

    @BeforeEach
    public void setupTest() throws PWCGException
    {
        Mockito.when(armedService.getServiceId()).thenReturn(20111);
    }

    @Test
    public void testEquipmentUpgradeForPlayer() throws PWCGException
    {
        Campaign campaign = CampaignCache.makeCampaign(CompanyTestProfile.PZ16_PROFILE);

        EquipmentDepot equipmentDepotBeforeTest = campaign.getEquipmentManager().getEquipmentDepotForService(armedService.getServiceId());

        // Add good tanks to the depo
        List<Integer> veryGoodTanksInDepot = new ArrayList<>();
        for (int i = 0; i < 2; ++i)
        {
            EquippedTank veryGoodTank = TankEquipmentFactory.makeTankForDepot(campaign, "_pziii-m", Country.GERMANY);
            equipmentDepotBeforeTest.addTankToDepot(veryGoodTank);
            veryGoodTanksInDepot.add(veryGoodTank.getSerialNumber());
        }

        for (int veryGoodTankInDepot : veryGoodTanksInDepot)
        {
            Assertions.assertTrue (equipmentDepotBeforeTest.getTankFromDepot(veryGoodTankInDepot) != null);
        }

        // replace tanks in company with different quality 109s, but all worse
        // than the very good tanks in the depot
        Company playerCompany = campaign.determinePlayerCompanies().get(0);
        Equipment equipmentForCompanyBeforeTest = campaign.getEquipmentManager().getEquipmentForCompany(playerCompany.getCompanyId());
        for (EquippedTank tankInCompanyBeforeTest : equipmentForCompanyBeforeTest.getActiveEquippedTanks().values())
        {
            equipmentForCompanyBeforeTest.removeEquippedTank(tankInCompanyBeforeTest.getSerialNumber());
        }

        List<Integer> tanksThatShouldBeReplaced = new ArrayList<>();
        for (int i = 0; i < 2; ++i)
        {
            EquippedTank tankToReplace = TankEquipmentFactory.makeTankForCompany(campaign, "_pziii-l", playerCompany);
            equipmentForCompanyBeforeTest.addEquippedTankToCompany(campaign, playerCompany.getCompanyId(), tankToReplace);
            tanksThatShouldBeReplaced.add(tankToReplace.getSerialNumber());
        }

        List<Integer> tanksThatShouldNotBeReplaced = new ArrayList<>();
        for (int i = 0; i < 10; ++i)
        {
            EquippedTank tankToKeep = TankEquipmentFactory.makeTankForCompany(campaign, "_pziii-m", playerCompany);
            equipmentForCompanyBeforeTest.addEquippedTankToCompany(campaign, playerCompany.getCompanyId(), tankToKeep);
            tanksThatShouldNotBeReplaced.add(tankToKeep.getSerialNumber());
        }

        // The better tanks should be left in the company
        for (int tankThatShouldNotBeReplaced : tanksThatShouldNotBeReplaced)
        {
            Assertions.assertTrue (equipmentForCompanyBeforeTest.getEquippedTank(tankThatShouldNotBeReplaced) != null);
        }

        // Run the upgrade
        EquipmentUpgradeHandler equipmentUpgradeHandler = new EquipmentUpgradeHandler(campaign);
        equipmentUpgradeHandler.upgradeEquipment(armedService);

        // The worst tanks in the companys should be replaced and now be in the depot
        EquipmentDepot equipmentDepotAfterTest = campaign.getEquipmentManager().getEquipmentDepotForService(armedService.getServiceId());
        Equipment equipmentForCompanyAfterTest = campaign.getEquipmentManager().getEquipmentForCompany(playerCompany.getCompanyId());
        for (int tankThatShouldBeReplaced : tanksThatShouldBeReplaced)
        {
            Assertions.assertTrue (equipmentForCompanyAfterTest.getEquippedTank(tankThatShouldBeReplaced) == null);
            Assertions.assertTrue (equipmentDepotAfterTest.getTankFromDepot(tankThatShouldBeReplaced) != null);
        }

        // The better tanks should be left in the company
        for (int tankThatShouldNotBeReplaced : tanksThatShouldNotBeReplaced)
        {
            Assertions.assertTrue (equipmentForCompanyAfterTest.getEquippedTank(tankThatShouldNotBeReplaced) != null);
            Assertions.assertTrue (equipmentDepotAfterTest.getTankFromDepot(tankThatShouldNotBeReplaced) == null);
        }

        // The very good tanks in the depot should be moved from the depot to
        // the company
        for (int veryGoodTankInDepot : veryGoodTanksInDepot)
        {
            Assertions.assertTrue (equipmentForCompanyAfterTest.getEquippedTank(veryGoodTankInDepot) != null);
            Assertions.assertTrue (equipmentDepotAfterTest.getTankFromDepot(veryGoodTankInDepot) == null);
        }
    }

    @Test
    public void testEquipmentUpgradeForAI() throws PWCGException
    {
        Campaign campaign = CampaignCache.makeCampaign(CompanyTestProfile.GROSS_DEUTSCHLAND_PROFILE);

        EquipmentDepot equipmentDepotBeforeTest = campaign.getEquipmentManager().getEquipmentDepotForService(armedService.getServiceId());

        // Add good tanks to the depo
        List<Integer> veryGoodTanksInDepot = new ArrayList<>();
        for (int i = 0; i < 2; ++i)
        {
            EquippedTank veryGoodTank = TankEquipmentFactory.makeTankForDepot(campaign, "_pziii-m", Country.GERMANY);
            equipmentDepotBeforeTest.addTankToDepot(veryGoodTank);
            veryGoodTanksInDepot.add(veryGoodTank.getSerialNumber());
        }

        for (int veryGoodTankInDepot : veryGoodTanksInDepot)
        {
            Assertions.assertTrue (equipmentDepotBeforeTest.getTankFromDepot(veryGoodTankInDepot) != null);
        }

        // replace tanks in player company with very good quality panthers, to
        // avoid the need for replacement
        Company aiCompany = PWCGContext.getInstance().getCompanyManager().getCompany(CompanyTestProfile.PZ14_PROFILE.getCompanyId());
        Equipment equipmentForCompanyBeforeTest = campaign.getEquipmentManager().getEquipmentForCompany(aiCompany.getCompanyId());
        for (EquippedTank tankInCompanyBeforeTest : equipmentForCompanyBeforeTest.getActiveEquippedTanks().values())
        {
            equipmentForCompanyBeforeTest.removeEquippedTank(tankInCompanyBeforeTest.getSerialNumber());
        }

        List<Integer> originalCompanyTanks = new ArrayList<>();
        for (int i = 0; i < 16; ++i)
        {
            EquippedTank tankToReplace = TankEquipmentFactory.makeTankForCompany(campaign, "_pziii-l", aiCompany);
            equipmentForCompanyBeforeTest.addEquippedTankToCompany(campaign, aiCompany.getCompanyId(), tankToReplace);
            originalCompanyTanks.add(tankToReplace.getSerialNumber());
        }

        // Run the upgrade
        EquipmentUpgradeHandler equipmentUpgradeHandler = new EquipmentUpgradeHandler(campaign);
        equipmentUpgradeHandler.upgradeEquipment(armedService);

        // The tanks in the depot should be removed and sent to companies
        EquipmentDepot equipmentDepotAfterTest = campaign.getEquipmentManager().getEquipmentDepotForService(armedService.getServiceId());
        for (int depotTank : veryGoodTanksInDepot)
        {
            Assertions.assertTrue (equipmentDepotAfterTest.getTankFromDepot(depotTank) == null);
        }

        // No tanks should be replaced in the player company
        Equipment equipmentForPlayerCompanyAfterTest = campaign.getEquipmentManager().getEquipmentForCompany(CompanyTestProfile.GROSS_DEUTSCHLAND_PROFILE.getCompanyId());
        for (int depotTank : veryGoodTanksInDepot)
        {
            Assertions.assertTrue (equipmentForPlayerCompanyAfterTest.getEquippedTank(depotTank) == null);
        }

        // the tanks should be in a company
        for (int depotTankSerialNumber : veryGoodTanksInDepot)
        {
            boolean tankIsInCompany = false;
            for (Equipment companyEquipment : campaign.getEquipmentManager().getEquipmentAllCompanies().values())
            {
                for (int companyTankSerialNumber : companyEquipment.getAvailableDepotTanks().keySet())
                {
                    if (depotTankSerialNumber == companyTankSerialNumber)
                    {
                        tankIsInCompany = true;
                    }
                }
            }
            Assertions.assertTrue (tankIsInCompany);
        }
    }

    @Test
    public void testEquipmentUpgradeNotNeeded() throws PWCGException
    {
        Campaign campaign = CampaignCache.makeCampaign(CompanyTestProfile.PZ16_PROFILE);

        // Clear out the depot and replace with a couple of bad tanks
        EquipmentDepot equipmentDepotBeforeTest = campaign.getEquipmentManager().getEquipmentDepotForService(armedService.getServiceId());
        for (EquippedTank tankInCompanyBeforeTest : equipmentDepotBeforeTest.getAllTanksInDepot())
        {
            equipmentDepotBeforeTest.removeEquippedTankFromDepot(tankInCompanyBeforeTest.getSerialNumber());
        }

        List<Integer> badTanksInDepot = new ArrayList<>();
        for (int i = 0; i < 2; ++i)
        {
            EquippedTank badTankInDepot = TankEquipmentFactory.makeTankForDepot(campaign, "_pziii-l", Country.GERMANY);
            equipmentDepotBeforeTest.addTankToDepot(badTankInDepot);
            badTanksInDepot.add(badTankInDepot.getSerialNumber());
        }

        // Clear out the company and add better tanks than we have in the
        // depot
        Company playerCompany = campaign.determinePlayerCompanies().get(0);
        Equipment equipmentForCompanyBeforeTest = campaign.getEquipmentManager().getEquipmentForCompany(playerCompany.getCompanyId());

        for (EquippedTank tankInCompanyBeforeTest : equipmentForCompanyBeforeTest.getActiveEquippedTanks().values())
        {
            equipmentForCompanyBeforeTest.removeEquippedTank(tankInCompanyBeforeTest.getSerialNumber());
        }

        List<Integer> goodTanksInTheCompany = new ArrayList<>();
        for (int i = 0; i < 12; ++i)
        {
            EquippedTank goodTankInCompany = TankEquipmentFactory.makeTankForDepot(campaign, "_pzv-d", Country.GERMANY);
            equipmentForCompanyBeforeTest.addEquippedTankToCompany(campaign, playerCompany.getCompanyId(), goodTankInCompany);
            goodTanksInTheCompany.add(goodTankInCompany.getSerialNumber());
        }

        // Run the upgrade
        EquipmentUpgradeHandler equipmentUpgradeHandler = new EquipmentUpgradeHandler(campaign);
        equipmentUpgradeHandler.upgradeEquipment(armedService);

        EquipmentDepot equipmentDepotAfterTest = campaign.getEquipmentManager().getEquipmentDepotForService(armedService.getServiceId());
        Equipment equipmentForCompanyAfterTest = campaign.getEquipmentManager().getEquipmentForCompany(playerCompany.getCompanyId());

        // Good tanks in the company should stay in the company
        for (int tankThatShouldNotBeReplaced : goodTanksInTheCompany)
        {
            Assertions.assertTrue (equipmentForCompanyAfterTest.getEquippedTank(tankThatShouldNotBeReplaced) != null);
            Assertions.assertTrue (equipmentDepotAfterTest.getTankFromDepot(tankThatShouldNotBeReplaced) == null);
        }

        // Bad tanks in the depot should stay in the depot
        for (int badlaneInDepot : badTanksInDepot)
        {
            Assertions.assertTrue (equipmentForCompanyAfterTest.getEquippedTank(badlaneInDepot) == null);
            Assertions.assertTrue (equipmentDepotAfterTest.getTankFromDepot(badlaneInDepot) != null);
        }
    }
}
