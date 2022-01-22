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
import pwcg.campaign.api.Side;
import pwcg.campaign.company.Company;
import pwcg.campaign.context.Country;
import pwcg.campaign.context.FrontMapIdentifier;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.factory.CountryFactory;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.config.ConfigManagerCampaign;
import pwcg.core.config.ConfigSimple;
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
        Mockito.when(mission.getFlights()).thenReturn(missionFlightBuilder);
        Mockito.when(company.getCountry()).thenReturn(country);
        Mockito.when(country.getSide()).thenReturn(Side.AXIS);

        Mockito.when(campaign.getCampaignConfigManager()).thenReturn(configManager);
        Mockito.when(campaign.getDate()).thenReturn(DateUtils.getDateYYYYMMDD("19430401"));
        Mockito.when(configManager.getStringConfigParam(ConfigItemKeys.SimpleConfigGroundKey)).thenReturn(ConfigSimple.CONFIG_LEVEL_MED);
        
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
            validate(groundUnitGroup, Country.GERMANY, Country.RUSSIA);
        }
    }
    
    public GroundUnitCollection createLargeAssaultTest () throws PWCGException 
    {
        GroundUnitCollection groundUnitGroup = AssaultFixedUnitSegmentBuilder.generateAssault(mission);
        
        Assertions.assertTrue (groundUnitGroup.getGroundUnits().size() >= 10);
        groundUnitGroup.validate();
        return groundUnitGroup;
    }

    private void validate(GroundUnitCollection groundUnitGroup, Country attacker, Country defender) throws PWCGException
    {
        for (IGroundUnit groundUnit : groundUnitGroup.getGroundUnits())
        {
            if (groundUnit.getCountry().getCountry() == attacker)
            {
                if (groundUnit.getVehicleClass() == VehicleClass.Tank)
                {
                    Assertions.assertTrue (groundUnit.getVehicles().size() >= 2);
                }
                else if (groundUnit.getVehicleClass() == VehicleClass.ArtilleryHowitzer)
                {
                    Assertions.assertTrue (groundUnit.getVehicles().size() >= 2);
                }
                else if (groundUnit.getVehicleClass() == VehicleClass.MachineGun)
                {
                    Assertions.assertTrue (groundUnit.getVehicles().size() >= 2);
                }
                else if (groundUnit.getVehicleClass() == VehicleClass.AAAArtillery)
                {
                    Assertions.assertTrue (groundUnit.getVehicles().size() >= 2);
                }
                else if (groundUnit.getVehicleClass() == VehicleClass.AAAMachineGun)
                {
                    Assertions.assertTrue (groundUnit.getVehicles().size() >= 2);
                }
                else
                {
                    throw new PWCGException("Unexpected unit type: " + groundUnit.getVehicleClass());
                }
            }
            else if (groundUnit.getCountry().getCountry() == defender)
            {
                if (groundUnit.getVehicleClass() == VehicleClass.ArtilleryAntiTank)
                {
                    Assertions.assertTrue (groundUnit.getVehicles().size() >= 2);
                }
                else if (groundUnit.getVehicleClass() == VehicleClass.Tank)
                {
                    Assertions.assertTrue (groundUnit.getVehicles().size() >= 2);
                }
                else if (groundUnit.getVehicleClass() == VehicleClass.ArtilleryHowitzer)
                {
                    Assertions.assertTrue (groundUnit.getVehicles().size() >= 2);
                }
                else if (groundUnit.getVehicleClass() == VehicleClass.MachineGun)
                {
                    Assertions.assertTrue (groundUnit.getVehicles().size() >= 2);
                }
                else if (groundUnit.getVehicleClass() == VehicleClass.AAAArtillery)
                {
                    Assertions.assertTrue (groundUnit.getVehicles().size() >= 2);
                }
                else if (groundUnit.getVehicleClass() == VehicleClass.AAAMachineGun)
                {
                    Assertions.assertTrue (groundUnit.getVehicles().size() >= 2);
                }
                else
                {
                    throw new PWCGException("Unexpected unit type: " + groundUnit.getVehicleClass());
                }
            }
            else
            {
                throw new PWCGException("Unit from unidentified nation in assault: " + groundUnit.getCountry().getCountry());
            }
        }
    }
}
