package pwcg.campaign.context;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pwcg.campaign.Campaign;
import pwcg.campaign.company.CompanyManager;
import pwcg.campaign.company.SkirmishProfileManager;
import pwcg.campaign.group.AirfieldManager;
import pwcg.campaign.group.airfield.Airfield;
import pwcg.campaign.newspapers.NewspaperManager;
import pwcg.campaign.plane.PlaneTypeFactory;
import pwcg.campaign.skin.SkinManager;
import pwcg.campaign.tank.AiTankTypeFactory;
import pwcg.campaign.tank.FullTankTypeFactory;
import pwcg.campaign.tank.ITankTypeFactory;
import pwcg.campaign.tank.PlayerTankTypeFactory;
import pwcg.core.exception.PWCGException;
import pwcg.mission.ground.vehicle.VehicleDefinitionManager;

public abstract class PWCGContextBase
{
    protected Map<FrontMapIdentifier, PWCGMap> pwcgMaps = new HashMap<FrontMapIdentifier, PWCGMap>();
    protected FrontMapIdentifier currentMap = null;
    protected Campaign campaign = null;
    protected AceManager aceManager = new AceManager();
    protected CompanyManager companyManager = new CompanyManager();
    protected NewspaperManager newspaperManager = new NewspaperManager();
    protected SkinManager skinManager = new SkinManager();
    protected SkirmishProfileManager skirmishProfileManager = new SkirmishProfileManager();
    protected VehicleDefinitionManager vehicleDefinitionManager = new VehicleDefinitionManager();
    protected boolean testMode = false;
    protected String missionLogPath = "";
    protected PlaneTypeFactory planeTypeFactory = new PlaneTypeFactory();
    protected ITankTypeFactory fullTankTypeFactory = new FullTankTypeFactory();
    protected ITankTypeFactory playerTankTypeFactory = new PlayerTankTypeFactory();
    protected ITankTypeFactory aiTankTypeFactory = new AiTankTypeFactory();

    protected List<String> campaignStartDates = new ArrayList<String>();

    public void configurePwcgMaps() throws PWCGException
    {
        for (PWCGMap map : pwcgMaps.values())
        {
            map.configure();
        }

        companyManager.initialize();
        newspaperManager.initialize();
    }

    protected void initialize() throws PWCGException
    {
        initializeMap();

        planeTypeFactory.initialize();
        fullTankTypeFactory.initialize();
        playerTankTypeFactory.initialize();
        aiTankTypeFactory.initialize();
        aceManager.configure();
        companyManager.initialize();
        skirmishProfileManager.initialize();
        skinManager.initialize();
        vehicleDefinitionManager.initialize();
    }

    public void changeContext(FrontMapIdentifier frontMapIdentifier) throws PWCGException
    {
        frontMapIdentifier = StalingradMapResolver.resolveStalingradMap(campaign, frontMapIdentifier);
        currentMap = frontMapIdentifier;
    }

    public void setCampaign(Campaign campaign) throws PWCGException
    {
        this.campaign = campaign;
        if (campaign != null)
        {
            setMapForCampaign(campaign);
        }
    }

    public void setMapForCampaign(Campaign campaign) throws PWCGException
    {
        FrontMapIdentifier mapIdentifier = campaign.getCampaignMap();
        if (mapIdentifier != null && mapIdentifier != FrontMapIdentifier.NO_MAP)
        {
            changeContext(mapIdentifier);
            configurePwcgMaps();
        }
    }

    public Date getEarliestPwcgDate() throws PWCGException
    {
        Date earliesDateForPWCG = null;
        for (PWCGMap map : pwcgMaps.values())
        {
            Date earliestMapDate = map.getFrontDatesForMap().getEarliestMapDate();
            if (earliesDateForPWCG == null)
            {
                earliesDateForPWCG = earliestMapDate;
            }
            else
            {
                if (earliestMapDate.before(earliesDateForPWCG))
                {
                    earliesDateForPWCG = earliestMapDate;
                }
            }
        }

        return earliesDateForPWCG;
    }

    public Campaign getCampaign()
    {
        return campaign;
    }

    public PWCGMap getCurrentMap()
    {
        return pwcgMaps.get(currentMap);
    }

    public PWCGMap getMapByMapName(String mapName)
    {
        FrontMapIdentifier mapId = FrontMapIdentifier.getFrontMapIdentifierForName(mapName);
        return pwcgMaps.get(mapId);
    }

    public PWCGMap getMapByMapId(FrontMapIdentifier mapId)
    {
        return pwcgMaps.get(mapId);
    }

    public AceManager getAceManager()
    {
        return aceManager;
    }

    public boolean isTestMode()
    {
        return testMode;
    }

    public void setTestMode(boolean testMode)
    {
        this.testMode = testMode;
    }

    public List<String> getCampaignStartDates()
    {
        return campaignStartDates;
    }

    public List<PWCGMap> getAllMaps()
    {
        List<PWCGMap> allMaps = new ArrayList<PWCGMap>();
        allMaps.addAll(pwcgMaps.values());

        return allMaps;
    }

    public Airfield getAirfieldAllMaps(String airfieldName)
    {
        Airfield airfield = null;

        for (PWCGMap map : pwcgMaps.values())
        {
            AirfieldManager airfieldManager = map.getAirfieldManager();
            if (airfieldManager != null)
            {
                airfield = airfieldManager.getAirfield(airfieldName);

                if (airfield != null)
                {
                    return airfield;
                }
            }
        }

        return airfield;
    }

    public void setCurrentMap(FrontMapIdentifier mapIdentifier) throws PWCGException
    {
        changeContext(mapIdentifier);
    }

    public CompanyManager getCompanyManager()
    {
        return companyManager;
    }

    public SkirmishProfileManager getSkirmishProfileManager()
    {
        return skirmishProfileManager;
    }

    public SkinManager getSkinManager()
    {
        return skinManager;
    }
    
    public ITankTypeFactory getFullTankTypeFactory()
    {
        return fullTankTypeFactory;
    }

    public ITankTypeFactory getPlayerTankTypeFactory()
    {
        return playerTankTypeFactory;
    }

    public ITankTypeFactory getAiTankTypeFactory()
    {
        return aiTankTypeFactory;
    }

    public PlaneTypeFactory getPlaneTypeFactory()
    {
        return planeTypeFactory;
    }

    public List<PWCGMap> getMaps()
    {
        return new ArrayList<PWCGMap>(pwcgMaps.values());
    }

    public VehicleDefinitionManager getVehicleDefinitionManager()
    {
        return vehicleDefinitionManager;
    }

    public void setMissionLogDirectory(String missionLogPath)
    {
        this.missionLogPath = missionLogPath + "\\";
    }

    public String getMissionLogDirectory()
    {
        return missionLogPath;
    }

    public NewspaperManager getNewspaperManager()
    {
        return newspaperManager;
    }

    public abstract void initializeMap() throws PWCGException;

}
