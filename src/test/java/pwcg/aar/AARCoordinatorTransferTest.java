package pwcg.aar;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.FrontMapIdentifier;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.crewmember.CrewMemberStatus;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.CompanyTestProfile;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AARCoordinatorTransferTest
{
    private Campaign campaign;    
    private static AARCoordinator aarCoordinator;
    private static ExpectedResults expectedResults;

    @BeforeAll
    public void setupSuite() throws PWCGException
    {
        
        PWCGContext.getInstance().changeContext(FrontMapIdentifier.MOSCOW_MAP);
        campaign = CampaignCache.makeCampaign(CompanyTestProfile.GROSS_DEUTSCHLAND_PROFILE);
        expectedResults = new ExpectedResults(campaign);
        aarCoordinator = AARCoordinator.getInstance();
        aarCoordinator.reset(campaign);
    }

    @Test
    public void runMissionAARLeave () throws PWCGException
    {
        aarCoordinator.submitLeave(campaign, 20);
        expectedResults.buildExpectedResultsFromAARContext(aarCoordinator.getAarContext());
        validateLeave();
    }

    public void validateLeave() throws PWCGException
    {
        CrewMember player = campaign.findReferencePlayer();
        assert(campaign.getDate().after(DateUtils.getDateYYYYMMDD("19411101")));
        assert(player.getCrewMemberVictories().getAirToAirVictoryCount() == 0);
        assert(player.getCrewMemberVictories().getGroundVictoryCount() == 0);
        
        for (Integer serialNumber : expectedResults.getLostCrewMembers())
        {
            CrewMember lostCrewMember = campaign.getPersonnelManager().getAnyCampaignMember(serialNumber);
            assert(lostCrewMember.getCrewMemberActiveStatus() <= CrewMemberStatus.STATUS_WOUNDED);
        }
    }
}
