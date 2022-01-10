package pwcg.aar.outofmission.phase1.elapsedtime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.core.config.ConfigManagerCampaign;
import pwcg.core.constants.AiSkillLevel;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.product.bos.country.TCServiceManager;

@ExtendWith(MockitoExtension.class)
public class OutOfMissionAAALossCalculatorTest
{
    @Mock
    private CrewMember crewMember;

    @Mock
    private Campaign campaign;

    @Mock
    private ConfigManagerCampaign configManager;

    @Mock
    private ArmedService service;
    
    @BeforeEach
    public void setupTest() throws PWCGException
    {
        
        Mockito.when(campaign.getDate()).thenReturn(DateUtils.getDateYYYYMMDD("19420801"));
    }
    
    @Test
    public void testCrewMemberLostToAAAOddsGroundAttackRussianNovice() throws PWCGException
    {
        Mockito.when(crewMember.getCompanyId()).thenReturn(10121312);
        Mockito.when(crewMember.getAiSkillLevel()).thenReturn(AiSkillLevel.NOVICE);
        Mockito.when(crewMember.determineService(campaign.getDate())).thenReturn(service);
        Mockito.when(service.getServiceId()).thenReturn(TCServiceManager.VVS);
        
        OutOfMissionAAAOddsCalculator aaaLossOddsCalculator = new OutOfMissionAAAOddsCalculator(campaign);
        int odds = aaaLossOddsCalculator.oddsShotDownByAAA(crewMember);
        assert(odds == 52);
    }
    
    @Test
    public void testCrewMemberLostToAAAOddsGroundAttackGermanNovice() throws PWCGException
    {
        Mockito.when(crewMember.getCompanyId()).thenReturn(10121312);
        Mockito.when(crewMember.getAiSkillLevel()).thenReturn(AiSkillLevel.NOVICE);
        Mockito.when(crewMember.determineService(campaign.getDate())).thenReturn(service);
        Mockito.when(service.getServiceId()).thenReturn(TCServiceManager.LUFTWAFFE);
        
        OutOfMissionAAAOddsCalculator aaaLossOddsCalculator = new OutOfMissionAAAOddsCalculator(campaign);
        int odds = aaaLossOddsCalculator.oddsShotDownByAAA(crewMember);
        assert(odds == 32);
    }
    
    @Test
    public void testCrewMemberLostToAAAOddsDiveBombGermanVeteran() throws PWCGException
    {
        Mockito.when(crewMember.getCompanyId()).thenReturn(20121077);
        Mockito.when(crewMember.getAiSkillLevel()).thenReturn(AiSkillLevel.VETERAN);
        Mockito.when(crewMember.determineService(campaign.getDate())).thenReturn(service);
        Mockito.when(service.getServiceId()).thenReturn(TCServiceManager.LUFTWAFFE);
        
        OutOfMissionAAAOddsCalculator aaaLossOddsCalculator = new OutOfMissionAAAOddsCalculator(campaign);
        int odds = aaaLossOddsCalculator.oddsShotDownByAAA(crewMember);
        assert(odds == 8);
    }
}
