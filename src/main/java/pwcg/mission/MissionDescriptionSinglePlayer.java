package pwcg.mission;

import java.util.ArrayList;
import java.util.HashMap;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.context.PWCGContext;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.core.utils.MathUtils;
import pwcg.mission.options.MissionOptions;
import pwcg.mission.options.MissionWeather;
import pwcg.mission.options.WindLayer;
import pwcg.mission.playerunit.objective.MissionObjectiveFactory;
import pwcg.mission.unit.ITankPlatoon;

public class MissionDescriptionSinglePlayer implements IMissionDescription 
{
    private Mission mission;
    private Campaign campaign;
    private ITankPlatoon playerPlatoon;
    
	private String author = "Brought to you by PWCGCampaign";
	private String title = "";
    private String singlePlayerHtmlTemplate = 
                    "<br><COMPANY> stationed near <TOWN>" +
                    "<br> <DATE>" +
                    "<br>Primary Objective <OBJECTIVE>";
    
	private String descSinglePlayerTemplate = 
		"Vehicle  <VEHICLE>\n" +
		"Unit  <COMPANY>\n" +
		"Base  <TOWN>\n" +
        "Date  <DATE>\n" +
        "Time  <TIME>\n" +
		"\n" +
		"Weather Report \n" +
		"    <CLOUDS>\n" +
		"    <WIND>\n" +
		"\n" +
		"Primary Objective \n" +
        "    <OBJECTIVE>\n" +
        "\n";
    
	private String campaignDateString = "";
		
	private ArrayList<String> enemyIntList = new ArrayList<String>();
	private ArrayList<String> friendlyIntList = new ArrayList<String>();
	private ArrayList<String> enemyIntHtmlList = new ArrayList<String>();
	private ArrayList<String> friendlyIntHtmlList = new ArrayList<String>();
	
    public MissionDescriptionSinglePlayer (Campaign campaign, Mission mission, ITankPlatoon  playerPlatoon)
    {
        this.mission = mission;
        this.campaign = campaign;
        this.playerPlatoon = playerPlatoon;
        campaignDateString = DateUtils.getDateStringDashDelimitedYYYYMMDD(campaign.getDate());
    }

	public String createDescription() throws PWCGException 
    {
        MissionWeather weather = mission.getWeather();
        setClouds(weather.getWeatherDescription());
        setWind(weather.getWindLayers().get(0));

        MissionOptions missionOptions = mission.getMissionOptions();
        setMissionDateTime(DateUtils.getDateAsMissionFileFormat(campaign.getDate()), missionOptions.getMissionTime().getMissionTime());

        setVehicle(playerPlatoon.getLeadVehicle().getDisplayName());
        setTown(playerPlatoon.getUnitInformation().getBase());
        setObjective(MissionObjectiveFactory.formMissionObjective(playerPlatoon, campaign.getDate()));
        setCompany(playerPlatoon.getCompany().determineDisplayName(campaign.getDate()));
        buildTitleDescription(campaign.getCampaignData().getName(), playerPlatoon.getUnitInformation().getCompany().determineDisplayName(campaign.getDate()));

        HashMap<String, ITankPlatoon> companyMap = new HashMap<>();
        for (ITankPlatoon unit : mission.getPlatoons().getPlayerUnits())
        {
            companyMap.put(unit.getCompany().determineDisplayName(campaign.getDate()), unit);
        }

        for (ITankPlatoon unit : companyMap.values())
        {
            setUnit(playerPlatoon.getUnitInformation().getCountry(), unit);
        }
        
        return descSinglePlayerTemplate;
    }

    
    private void setUnit(ICountry country, ITankPlatoon unit) throws PWCGException 
    {
        Campaign campaign =     PWCGContext.getInstance().getCampaign();
        
        String company = unit.getCompany().determineDisplayName(campaign.getDate());
        String vehicle = unit.getLeadVehicle().getDesc();
        ICountry vehicleCountry = unit.getUnitInformation().getCountry();
        
        if (country.isSameSide(vehicleCountry))
        {
            String friendlyInt = "    " + company + " using " + vehicle;
            friendlyIntList.add(friendlyInt + "\n");
            
            String friendlyInthtml = "<br>    " + friendlyInt;
            friendlyIntHtmlList.add(friendlyInthtml);           
        }
        else
        {
            String enemyInt = "    " + company + " using " + vehicle;
            enemyIntList.add(enemyInt + "\n");
            
            String enemyInthtml = "<br>    " + enemyInt;
            enemyIntHtmlList.add(enemyInthtml);
        }
    }


	public void setVehicle(String replacement)
	{
		descSinglePlayerTemplate = replace(descSinglePlayerTemplate, "<VEHICLE>", replacement);
	}
	
	public void setCompany(String replacement)
	{
		descSinglePlayerTemplate = replace(descSinglePlayerTemplate, "<COMPANY>", replacement);
		singlePlayerHtmlTemplate = replace(singlePlayerHtmlTemplate, "<COMPANY>", replacement);
	}
	
	public void setTown(String replacement)
	{
		descSinglePlayerTemplate = replace(descSinglePlayerTemplate, "<TOWN>", replacement);
		singlePlayerHtmlTemplate = replace(singlePlayerHtmlTemplate, "<TOWN>", replacement);
	}
	
	private void setMissionDateTime(String missionDate, String missionTime)
	{
		descSinglePlayerTemplate = replace(descSinglePlayerTemplate, "<DATE>", missionDate);
		singlePlayerHtmlTemplate = replace(singlePlayerHtmlTemplate, "<DATE>", missionDate);
		
		setTime(missionTime);
	}
	   
    private void setObjective(String replacement)
    {
        descSinglePlayerTemplate = replace(descSinglePlayerTemplate, "<OBJECTIVE>", replacement);
        singlePlayerHtmlTemplate = replace(singlePlayerHtmlTemplate, "<OBJECTIVE>", replacement);
    }

	private void setTime(String missionTime)
	{
		descSinglePlayerTemplate = replace(descSinglePlayerTemplate, "<TIME>", missionTime);
	}
	
	private void setClouds(String replacement)
	{
		descSinglePlayerTemplate = replace(descSinglePlayerTemplate, "<CLOUDS>", replacement);
	}
	
	private void setWind(WindLayer layer) throws PWCGException
	{
		int windFrom = Double.valueOf(MathUtils.adjustAngle (layer.getDirection(), 180)).intValue();		
		String windCond = "Wind speed is " + layer.getSpeed() + " M/S." + 
				  "\n    Wind direction is " + windFrom + ".";			
		
		descSinglePlayerTemplate = replace(descSinglePlayerTemplate, "<WIND>", windCond);
	}

    private String replace(String str, String pattern, String replacement) 
    {
        int s = 0;
        int e = 0;
        StringBuffer result = new StringBuffer();

        while ((e = str.indexOf(pattern, s)) >= 0) 
        {
            result.append(str.substring(s, e));
            result.append(replacement);
            s = e+pattern.length();
        }
        result.append(str.substring(s));
        
        return result.toString();
    }

	private void buildTitleDescription(String campaignName, String missionType) 
	{
		this.title = campaignName + " " + campaignDateString + " " + missionType;
	}

	@Override
	public String getHtml()
	{
		return singlePlayerHtmlTemplate;
	}

	@Override
	public String getTitle() {
		return title;
	}

	@Override
	public String getAuthor() {
		return author;
	}
}
