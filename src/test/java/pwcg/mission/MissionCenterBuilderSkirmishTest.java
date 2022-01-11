package pwcg.mission;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import pwcg.campaign.Campaign;
import pwcg.campaign.company.Company;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.skirmish.Skirmish;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.CoordinateBox;
import pwcg.core.utils.DateUtils;
import pwcg.mission.flight.FlightTypes;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.CompanyTestProfile;
import pwcg.testutils.TestMissionBuilderUtility;

public class MissionCenterBuilderSkirmishTest
{    
    public MissionCenterBuilderSkirmishTest() throws PWCGException
    {
        
    }
    
    @Test
    public void testNoSkirmish() throws PWCGException
    {
        Campaign campaign = CampaignCache.makeCampaign(CompanyTestProfile.GROSS_DEUTSCHLAND_PROFILE);
        PWCGContext.getInstance().setCampaign(campaign);

        List<Skirmish> skirmishes = PWCGContext.getInstance().getCurrentMap().getSkirmishManager().getSkirmishesForDate(campaign, TestMissionBuilderUtility.buildTestParticipatingHumans(campaign));
        Assertions.assertTrue (skirmishes.size() == 0);
    }

    @Test
    public void singlePlayerMissionBoxArnhemEarlyTest() throws PWCGException
    {
        Campaign campaign = CampaignCache.makeCampaign(CompanyTestProfile.THIRD_DIVISION_PROFILE);
        PWCGContext.getInstance().setCampaign(campaign);
        campaign.setDate(DateUtils.getDateYYYYMMDD("19440917"));

        createMissionAtSkirmish(campaign);
    }

    @Test
    public void singlePlayerMissionBoxArnhemLateTest() throws PWCGException
    {
        Campaign campaign = CampaignCache.makeCampaign(CompanyTestProfile.THIRD_DIVISION_PROFILE);
        PWCGContext.getInstance().setCampaign(campaign);
        campaign.setDate(DateUtils.getDateYYYYMMDD("19440928"));

        createMissionAtSkirmish(campaign);
    }

    private void createMissionAtSkirmish(Campaign campaign) throws PWCGException
    {
        MissionHumanParticipants participatingPlayers = makeParticipatingPlayers(campaign);
                
        List<Skirmish> skirmishes = PWCGContext.getInstance().getCurrentMap().getSkirmishManager().getSkirmishesForDate(campaign, TestMissionBuilderUtility.buildTestParticipatingHumans(campaign));
        Assertions.assertTrue (skirmishes.size() > 0);
        
        Company playerCompany = participatingPlayers.getAllParticipatingPlayers().get(0).determineCompany();
        MissionCompanyFlightTypes playerFlightTypes = MissionCompanyFlightTypes.buildPlayerFlightType(FlightTypes.PATROL, playerCompany);

        MissionBorderBuilder missionBorderBuilder = new MissionBorderBuilder(campaign, participatingPlayers, skirmishes.get(0), playerFlightTypes);

        CoordinateBox missionBorders = missionBorderBuilder.buildCoordinateBox();
        Coordinate missionBoxCenter = missionBorders.getCenter();
                
        assert(skirmishes.get(0).getCoordinateBox().isInBox(missionBoxCenter));
    }

    private MissionHumanParticipants makeParticipatingPlayers(Campaign campaign) throws PWCGException
    {
        MissionHumanParticipants participatingPlayers = new MissionHumanParticipants();
        CrewMember player = campaign.findReferencePlayer();
        participatingPlayers.addCrewMember(player);
        return participatingPlayers;
    }

}
