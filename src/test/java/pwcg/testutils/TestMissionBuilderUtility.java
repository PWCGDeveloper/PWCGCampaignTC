package pwcg.testutils;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.group.Block;
import pwcg.campaign.group.Bridge;
import pwcg.campaign.skirmish.Skirmish;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.CoordinateBox;
import pwcg.mission.Mission;
import pwcg.mission.MissionHumanParticipants;
import pwcg.mission.MissionObjective;
import pwcg.mission.options.MissionOptions;
import pwcg.mission.options.MissionWeather;

public class TestMissionBuilderUtility
{

    public static Mission createTestMission(Campaign campaign, MissionHumanParticipants participatingPlayers, CoordinateBox missionBorders) throws PWCGException
    {
        MissionOptions missionOptions = new MissionOptions(campaign.getDate());
        missionOptions.createOptions();

        campaign.getCampaignConfigManager().setParam(ConfigItemKeys.UseRealisticWeatherKey, "0");
        MissionWeather weather = new MissionWeather(campaign, missionOptions.getMissionHour());
        weather.createMissionWeather();

        Bridge bridge = PWCGContext.getInstance().getCurrentMap().getGroupManager().getBridgeFinder().findAllBridgesForSide(Side.AXIS, campaign.getDate()).get(0);
        
        Block station = PWCGContext.getInstance().getCurrentMap().getGroupManager().getRailroadStationFinder().getClosestTrainPositionBySide(Side.AXIS, campaign.getDate(), bridge.getPosition());
        MissionObjective objective = new MissionObjective(station);
        objective.setAssaultingCountry(PWCGContext.getInstance().getCurrentMap().getGroundCountryForMapBySide(Side.ALLIED));
        objective.setDefendingCountry(PWCGContext.getInstance().getCurrentMap().getGroundCountryForMapBySide(Side.AXIS));
        
        Skirmish skirmish = null;
        Mission mission = new Mission(campaign, objective, participatingPlayers, missionBorders, weather, skirmish, missionOptions);
        campaign.setCurrentMission(mission);
        return mission;
    }

    public static MissionHumanParticipants buildTestParticipatingHumans(Campaign campaign) throws PWCGException
    {
        MissionHumanParticipants participatingPlayers = new MissionHumanParticipants();
        for (CrewMember player: campaign.getPersonnelManager().getAllActivePlayers().getCrewMemberList())
        {
            participatingPlayers.addCrewMember(player);
        }
        return participatingPlayers;
    }
}
