package pwcg.mission;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.campaign.Campaign;
import pwcg.campaign.company.Company;
import pwcg.campaign.context.FrontMapIdentifier;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.skirmish.Skirmish;
import pwcg.campaign.skirmish.SkirmishBuilder;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.CoordinateBox;
import pwcg.core.utils.DateUtils;
import pwcg.core.utils.MathUtils;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.CompanyTestProfile;
import pwcg.testutils.TestMissionBuilderUtility;

@ExtendWith(MockitoExtension.class)
public class MissionBorderBuilderTest
{
    public MissionBorderBuilderTest() throws PWCGException
    {
        
        PWCGContext.getInstance().setCurrentMap(FrontMapIdentifier.STALINGRAD_MAP);
    }

    @Test
    public void singlePlayerMissionBoxTest() throws PWCGException
    {
        Campaign campaign = CampaignCache.makeCampaign(CompanyTestProfile.GROSS_DEUTSCHLAND_PROFILE);
        PWCGContext.getInstance().setCampaign(campaign);
        MissionHumanParticipants participatingPlayers = new MissionHumanParticipants();

        CrewMember player = campaign.findReferencePlayer();
        participatingPlayers.addCrewMember(player);
        
        for (int i = 0; i < 10; ++i)
        {
            Company playerCompany = participatingPlayers.getAllParticipatingPlayers().get(0).determineCompany();
            Coordinate objective = TestMissionBuilderUtility.buildMissionCenter(campaign, playerCompany.getCompanyId());
            MissionBorderBuilder missionBorderBuilder = new MissionBorderBuilder(campaign, objective);
            CoordinateBox missionBorders = missionBorderBuilder.buildCoordinateBox();
            
            Coordinate missionBoxCenter = missionBorders.getCenter();
            
            Coordinate playerCompanyLocation = playerCompany.determineCurrentPosition(campaign.getDate());
            
            double distanceToMission = MathUtils.calcDist(missionBoxCenter, playerCompanyLocation);
            assert(distanceToMission < 85000);
        }
    }

    @Test
    public void multiPlayerMissionBoxTest() throws PWCGException
    {
        Campaign coopCampaign = CampaignCache.makeCampaign(CompanyTestProfile.COOP_COMPETITIVE_PROFILE);
        PWCGContext.getInstance().setCampaign(coopCampaign);
        
        MissionHumanParticipants participatingPlayers = new MissionHumanParticipants();
        for (CrewMember player: coopCampaign.getPersonnelManager().getAllActivePlayers().getCrewMemberList())
        {
            participatingPlayers.addCrewMember(player);
        }
        
        for (int i = 0; i < 10; ++i)
        {
            Coordinate objective = TestMissionBuilderUtility.buildMissionCenter(coopCampaign, CompanyTestProfile.COOP_COMPETITIVE_PROFILE.getCompanyId());
            MissionBorderBuilder missionBorderBuilder = new MissionBorderBuilder(coopCampaign, objective);

            CoordinateBox missionBorders = missionBorderBuilder.buildCoordinateBox();
            Coordinate missionBoxCenter = missionBorders.getCenter();
            assert(missionBoxCenter.getXPos() > 0.0);
            assert(missionBoxCenter.getZPos() > 0.0);
        }
    }

    @Test
    public void buildBorderForSkirmishTest() throws PWCGException
    {
        Campaign campaign = CampaignCache.makeCampaign(CompanyTestProfile.THIRD_DIVISION_PROFILE);
        PWCGContext.getInstance().setCampaign(campaign);
        campaign.setDate(DateUtils.getDateYYYYMMDD("19440917"));
        
        MissionHumanParticipants participatingPlayers = new MissionHumanParticipants();
        for (CrewMember player: campaign.getPersonnelManager().getAllActivePlayers().getCrewMemberList())
        {
            participatingPlayers.addCrewMember(player);
        }

        SkirmishBuilder skirmishBuilder = new SkirmishBuilder(campaign, participatingPlayers);
        Skirmish skirmish = skirmishBuilder.chooseBestSkirmish();

        MissionBorderBuilder missionBorderBuilder = new MissionBorderBuilder(campaign, skirmish.getCenter());
        CoordinateBox missionBorders = missionBorderBuilder.buildCoordinateBox();

        Coordinate missionBoxCenter = missionBorders.getCenter();
        
        assert(skirmish.getCoordinateBox().isInBox(missionBoxCenter));

    }
}
