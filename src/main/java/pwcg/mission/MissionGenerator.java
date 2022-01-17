package pwcg.mission;

import java.util.HashMap;
import java.util.Map;

import pwcg.campaign.Campaign;
import pwcg.campaign.skirmish.Skirmish;
import pwcg.campaign.skirmish.SkirmishBuilder;
import pwcg.campaign.tank.PwcgRole;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.CoordinateBox;
import pwcg.mission.options.MissionOptions;
import pwcg.mission.options.MissionWeather;

public class MissionGenerator
{
    private Campaign campaign = null;

    public MissionGenerator(Campaign campaign)
    {
        this.campaign = campaign;
    }

    public Mission makeMission(MissionHumanParticipants participatingPlayers) throws PWCGException
    {
        return this.makeMission(participatingPlayers, new HashMap<>());
    }
    
    public Mission makeMission(MissionHumanParticipants participatingPlayers, Map<Integer, PwcgRole> companyRoleOverride) throws PWCGException
    {
        MissionOptions missionOptions = new MissionOptions(campaign.getDate());
        missionOptions.createOptions();

        MissionWeather weather = new MissionWeather(campaign, missionOptions.getMissionHour());
        weather.createMissionWeather();

        Skirmish skirmish = getSkirmishForMission(participatingPlayers);
        
        Mission mission = buildMission(participatingPlayers, weather, skirmish, missionOptions);
        
        return mission;
    }

    public Mission makeTestMissionFromSkirmish(
            MissionHumanParticipants participatingPlayers, 
            Skirmish skirmish) throws PWCGException
    {
        MissionOptions missionOptions = new MissionOptions(campaign.getDate());
        missionOptions.createOptions();

        campaign.getCampaignConfigManager().setParam(ConfigItemKeys.UseRealisticWeatherKey, "0");
        MissionWeather weather = new MissionWeather(campaign, missionOptions.getMissionHour());
        weather.createMissionWeather();

        Mission mission = buildMission(participatingPlayers, weather, skirmish, missionOptions);
        return mission;
    }


    public Mission makeTestMissionFromMissionType(MissionHumanParticipants participatingPlayers) throws PWCGException
    {
        MissionOptions missionOptions = new MissionOptions(campaign.getDate());
        missionOptions.createOptions();

        campaign.getCampaignConfigManager().setParam(ConfigItemKeys.UseRealisticWeatherKey, "0");
        MissionWeather weather = new MissionWeather(campaign, missionOptions.getMissionHour());
        weather.createMissionWeather();
        
        Skirmish skirmish = null;
        Mission mission = buildMission(participatingPlayers, weather, skirmish, missionOptions);
        return mission;
    }

    private Mission buildMission(
            MissionHumanParticipants participatingPlayers, 
            MissionWeather weather, 
            Skirmish skirmish,
            MissionOptions missionOptions) throws PWCGException
    {
        MissionObjectiveBuilder objectiveLocator = new MissionObjectiveBuilder(campaign, skirmish);
        MissionObjective objective = objectiveLocator.buildMissionObjective();
        
        campaign.setCurrentMission(null);
        
        CoordinateBox missionBorders = buildMissionBorders(participatingPlayers, skirmish);
        Mission mission = new Mission(campaign, objective, participatingPlayers, missionBorders, weather, skirmish, missionOptions);
        campaign.setCurrentMission(mission);
        mission.generate();

        return mission;
    }
    
    private Skirmish getSkirmishForMission(MissionHumanParticipants participatingPlayers) throws PWCGException
    {
        SkirmishBuilder skirmishBuilder = new SkirmishBuilder(campaign, participatingPlayers);
        return skirmishBuilder.chooseBestSkirmish();
    }

    private CoordinateBox buildMissionBorders(MissionHumanParticipants participatingPlayers, Skirmish skirmish) throws PWCGException
    {
        MissionBorderBuilder missionBorderBuilder = new MissionBorderBuilder(campaign, participatingPlayers, skirmish);
        CoordinateBox missionBorders = missionBorderBuilder.buildCoordinateBox();
        return missionBorders;
    }
}
