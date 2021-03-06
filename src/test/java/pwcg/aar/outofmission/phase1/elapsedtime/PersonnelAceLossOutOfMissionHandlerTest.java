package pwcg.aar.outofmission.phase1.elapsedtime;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.aar.data.AARContext;
import pwcg.campaign.Campaign;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.CompanyTestProfile;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PersonnelAceLossOutOfMissionHandlerTest
{
    private Campaign campaign;
 
    @Mock
    private AARContext aarContext;
    
    @BeforeAll
    public void setupSuite() throws PWCGException
    {
        
        campaign = CampaignCache.makeCampaign(CompanyTestProfile.THIRD_DIVISION_PROFILE);
    }

    @Test
    public void testAceLossesOutOfMission () throws PWCGException
    {     
        Mockito.when(aarContext.getNewDate()).thenReturn(DateUtils.getDateYYYYMMDD(CompanyTestProfile.THIRD_DIVISION_PROFILE.getDateString()));
        OutOfMissionAceLossCalculator aceLossOutOfMissionHandler = new OutOfMissionAceLossCalculator(campaign, aarContext);
        List<CrewMember> acesKilled = aceLossOutOfMissionHandler.acesKilledHistorically();
        Assertions.assertTrue (acesKilled.size() == 0);
    }

}
