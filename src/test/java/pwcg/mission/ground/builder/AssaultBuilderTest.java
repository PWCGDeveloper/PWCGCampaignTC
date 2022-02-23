package pwcg.mission.ground.builder;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.company.Company;
import pwcg.campaign.context.Country;
import pwcg.campaign.context.FrontMapIdentifier;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.factory.CountryFactory;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.config.ConfigManagerCampaign;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.PWCGLocation;
import pwcg.core.utils.DateUtils;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.Mission;
import pwcg.mission.MissionFlights;
import pwcg.mission.MissionObjective;
import pwcg.mission.flight.plane.PlaneMcu;
import pwcg.mission.ground.org.GroundUnitCollection;
import pwcg.mission.ground.org.IGroundUnit;
import pwcg.mission.ground.vehicle.VehicleClass;

@ExtendWith(MockitoExtension.class)
public class AssaultBuilderTest
{
    @Mock private Campaign campaign;
    @Mock private Mission mission;
    @Mock private Company company;
    @Mock private ConfigManagerCampaign configManager;
    @Mock private MissionFlights missionFlightBuilder;
    @Mock private ICountry country;
    @Mock private PlaneMcu playerPlane;

    @BeforeEach
    public void setupTest() throws PWCGException
    {

        PWCGContext.getInstance().setCurrentMap(FrontMapIdentifier.STALINGRAD_MAP);

        Mockito.when(mission.getCampaign()).thenReturn(campaign);

        Mockito.when(campaign.getCampaignConfigManager()).thenReturn(configManager);
        Mockito.when(campaign.getDate()).thenReturn(DateUtils.getDateYYYYMMDD("19430401"));
        Mockito.when(configManager.getIntConfigParam(ConfigItemKeys.GroundUnitSpawnDistanceKey)).thenReturn(10000);

        PWCGLocation town = new PWCGLocation();
        town.setName("Targetville");
        town.setPosition(new Coordinate(60000, 0, 60000));

        MissionObjective objective = new MissionObjective(town);
        objective.setAssaultingCountry(CountryFactory.makeCountryByCountry(Country.GERMANY));
        objective.setDefendingCountry(CountryFactory.makeCountryByCountry(Country.RUSSIA));
        Mockito.when(mission.getObjective()).thenReturn(objective);

    }

    @Test
    public void createLargeAssaultWithoutBattleTest () throws PWCGException
    {
        try (MockedStatic<RandomNumberGenerator> mocked = Mockito.mockStatic(RandomNumberGenerator.class))
        {
            mocked.when(() -> RandomNumberGenerator.getRandom(100)).thenReturn(49);

            Mockito.when(campaign.getDate()).thenReturn(DateUtils.getDateYYYYMMDD("19430401"));
            createLargeAssaultTest ();
            GroundUnitCollection groundUnitGroup = createLargeAssaultTest ();
            validateRequiredUnits(groundUnitGroup, Country.GERMANY);
            validateRequiredUnits(groundUnitGroup, Country.RUSSIA);
            validateUntHasEquipment(groundUnitGroup);
        }
    }

    public GroundUnitCollection createLargeAssaultTest () throws PWCGException
    {
        GroundUnitCollection groundUnitGroup = FrontFixedUnitSegmentsBuilder.generateAssault(mission);

        Assertions.assertTrue (groundUnitGroup.getGroundUnits().size() >= 10);
        groundUnitGroup.validate();
        return groundUnitGroup;
    }

    private void validateRequiredUnits(GroundUnitCollection groundUnitGroup, Country country) throws PWCGException
    {
        int artilleryFound = 0;
        int antiTankFound = 0;
        int machineGunFound = 0;

        for (IGroundUnit groundUnit : groundUnitGroup.getGroundUnits())
        {
            if (groundUnit.getCountry().getCountry() == country)
            {
                if (groundUnit.getVehicleClass() == VehicleClass.ArtilleryHowitzer)
                {
                    ++artilleryFound;
                }
                else if (groundUnit.getVehicleClass() == VehicleClass.MachineGun)
                {
                    ++machineGunFound;
                }
                else if (groundUnit.getVehicleClass() == VehicleClass.ArtilleryAntiTank)
                {
                    ++antiTankFound;
                }
            }
        }

        Assertions.assertTrue(artilleryFound >= 3);
        Assertions.assertTrue(antiTankFound >= 3);
        Assertions.assertTrue(machineGunFound >= 3);
    }

    private void validateUntHasEquipment(GroundUnitCollection groundUnitGroup) throws PWCGException
    {
        for (IGroundUnit groundUnit : groundUnitGroup.getGroundUnits())
        {
            Assertions.assertTrue (groundUnit.getVehicles().size() >= 1);
        }
    }
}
