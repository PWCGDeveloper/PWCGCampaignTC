package pwcg.testutils;

import java.util.Date;

import org.mockito.Mock;
import org.mockito.Mockito;

import pwcg.campaign.Campaign;
import pwcg.campaign.CampaignEquipmentManager;
import pwcg.campaign.CampaignPersonnelManager;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.company.Company;
import pwcg.campaign.context.Country;
import pwcg.campaign.context.FrontMapIdentifier;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.factory.CountryFactory;
import pwcg.campaign.personnel.CompanyPersonnel;
import pwcg.campaign.tank.Equipment;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.config.ConfigManagerCampaign;
import pwcg.core.config.ConfigSimple;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.CoordinateBox;
import pwcg.core.utils.DateUtils;
import pwcg.mission.Mission;
import pwcg.mission.MissionFlights;
import pwcg.mission.MissionGroundUnitResourceManager;
import pwcg.mission.MissionHumanParticipants;
import pwcg.mission.platoon.PlatoonInformation;

public class KubanAttackMockCampaign
{
    @Mock protected Campaign campaign;
    @Mock protected Mission mission;
    @Mock protected MissionFlights missionFlightBuilder;
    @Mock protected ConfigManagerCampaign configManager;
    @Mock protected CampaignPersonnelManager personnelManager;
    @Mock protected CompanyPersonnel companyPersonnel;
    @Mock protected CampaignEquipmentManager equipmentManager;
    @Mock protected Equipment companyEquipment;
    @Mock protected CrewMember player;
    @Mock protected PlatoonInformation platoonInformation;
    @Mock protected MissionHumanParticipants humanParticipants;

    protected ICountry country = CountryFactory.makeCountryByCountry(Country.GERMANY);
    protected Coordinate myTestPosition = new Coordinate (100000, 0, 100000);
    protected Coordinate mytargetLocation = new Coordinate (100000, 0, 150000);
    
    protected Date date;
    protected MissionGroundUnitResourceManager missionGroundUnitResourceManager = new MissionGroundUnitResourceManager();
    protected Company company;

    public void mockCampaignSetup() throws PWCGException
    {
        
        PWCGContext.getInstance().changeContext(FrontMapIdentifier.KUBAN_MAP);

        date = DateUtils.getDateYYYYMMDD("19430401");
        
        company = PWCGContext.getInstance().getCompanyManager().getCompany(20121002); // I./St.G.2

        Mockito.when(campaign.getCampaignConfigManager()).thenReturn(configManager);
        Mockito.when(campaign.getDate()).thenReturn(date);

        Mockito.when(campaign.getPersonnelManager()).thenReturn(personnelManager);
        Mockito.when(personnelManager.getCompanyPersonnel(Mockito.any())).thenReturn(companyPersonnel);
        Mockito.when(companyPersonnel.isCompanyPersonnelViable()).thenReturn(true);
        
        Mockito.when(campaign.getEquipmentManager()).thenReturn(equipmentManager);
        Mockito.when(equipmentManager.getEquipmentForCompany(Mockito.any())).thenReturn(companyEquipment);
        Mockito.when(companyEquipment.isCompanyEquipmentViable()).thenReturn(true);

        Mockito.when(configManager.getStringConfigParam(ConfigItemKeys.SimpleConfigGroundKey)).thenReturn(ConfigSimple.CONFIG_LEVEL_MED);
        Mockito.when(mission.getMissionGroundUnitManager()).thenReturn(missionGroundUnitResourceManager);
        
        CoordinateBox missionBorders = CoordinateBox.coordinateBoxFromCenter(myTestPosition, 100000);
        Mockito.when(mission.getFlights()).thenReturn(missionFlightBuilder);
        Mockito.when(mission.getMissionBorders()).thenReturn(missionBorders);
        Mockito.when(mission.getCampaign()).thenReturn(campaign);
        
        buildMockPlatoonInformation();
    }
    
    public void buildMockPlatoonInformation()
    {
        Mockito.when(platoonInformation.getCampaign()).thenReturn(campaign);
        Mockito.when(platoonInformation.getMission()).thenReturn(mission);
        Mockito.when(platoonInformation.getCompany()).thenReturn(company);
    }


}
