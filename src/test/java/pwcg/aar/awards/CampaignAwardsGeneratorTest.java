package pwcg.aar.awards;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.aar.data.AARContext;
import pwcg.aar.data.AARPersonnelAcheivements;
import pwcg.aar.data.AARPersonnelAwards;
import pwcg.aar.data.AARPersonnelLosses;
import pwcg.aar.inmission.phase3.reconcile.victories.ReconciledMissionVictoryData;
import pwcg.aar.outofmission.phase2.awards.CampaignAwardsGenerator;
import pwcg.aar.prelim.PwcgMissionData;
import pwcg.campaign.Campaign;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.crewmember.CrewMembers;
import pwcg.campaign.crewmember.Victory;
import pwcg.campaign.personnel.CrewMemberFilter;
import pwcg.core.exception.PWCGException;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.CampaignPersonnelTestHelper;
import pwcg.testutils.CompanyTestProfile;

@ExtendWith(MockitoExtension.class)
public class CampaignAwardsGeneratorTest
{
    private Campaign campaign;
    
    @Mock
    AARContext aarContext;
    
    @Mock
    private ReconciledMissionVictoryData reconciledVictoryData;
    
    @Mock
    private AARPersonnelLosses personnelLosses;
    
    @Mock
    private Victory victory1;
    
    @Mock
    private Victory victory2;
    
    @Mock
    private AARPersonnelAcheivements personnelAcheivements;

    @Mock
    private PwcgMissionData pwcgMissionData;

    @BeforeEach
    public void setupTest() throws PWCGException
    {        
        campaign = CampaignCache.makeCampaign(CompanyTestProfile.THIRD_DIVISION_PROFILE);        

        Mockito.when(aarContext.getPersonnelAcheivements()).thenReturn(personnelAcheivements);
        Mockito.when(aarContext.getPersonnelLosses()).thenReturn(personnelLosses);
        Mockito.when(personnelLosses.crewMemberisWoundedToday(Mockito.any())).thenReturn(false);        
    }

    @Test
    public void testMedalAwardedForVictories () throws PWCGException
    {             
        final int lowRankingCrewMemberIndex = 13;
        
        CrewMembers aiCrewMembers = campaign.getPersonnelManager().getCompanyPersonnel(CompanyTestProfile.THIRD_DIVISION_PROFILE.getCompanyId()).getActiveCrewMembers();
        List<CrewMember> sortedCrewmembers = aiCrewMembers.sortCrewMembers(campaign.getDate());
        
        CampaignPersonnelTestHelper.addVictories(sortedCrewmembers.get(lowRankingCrewMemberIndex), campaign.getDate(), 20);
 
        CampaignPersonnelTestHelper.addVictories(sortedCrewmembers.get(lowRankingCrewMemberIndex), campaign.getDate(), 3);

        Map<Integer, CrewMember> crewMembersToEvaluate = new HashMap<>();
        crewMembersToEvaluate.put(sortedCrewmembers.get(lowRankingCrewMemberIndex).getSerialNumber(), sortedCrewmembers.get(lowRankingCrewMemberIndex));
        crewMembersToEvaluate.put(sortedCrewmembers.get(2).getSerialNumber(), sortedCrewmembers.get(2));
        crewMembersToEvaluate.put(sortedCrewmembers.get(3).getSerialNumber(), sortedCrewmembers.get(3));

        CampaignAwardsGenerator awardsGenerator = new CampaignAwardsGenerator(campaign, aarContext);
        AARPersonnelAwards campaignMemberAwards = awardsGenerator.createCampaignMemberAwards(new ArrayList<>(crewMembersToEvaluate.values()));
        
        Assertions.assertTrue (campaignMemberAwards.getCampaignMemberMedals().size() >= 1);
        Assertions.assertTrue (campaignMemberAwards.getCampaignMemberMedals().containsKey(sortedCrewmembers.get(lowRankingCrewMemberIndex).getSerialNumber()));
        Assertions.assertTrue (!campaignMemberAwards.getCampaignMemberMedals().containsKey(sortedCrewmembers.get(2).getSerialNumber()));
        Assertions.assertTrue (!campaignMemberAwards.getCampaignMemberMedals().containsKey(sortedCrewmembers.get(3).getSerialNumber()));
    }

    @Test
    public void testInjuredMemberssAwardedWoundBadge () throws PWCGException
    {            
        CrewMembers nonPlayerCrewMembers = CrewMemberFilter.filterActiveAINoWounded(campaign.getPersonnelManager().getAllCampaignMembers(), campaign.getDate());

        Map<Integer, CrewMember> crewMembersInjured = new HashMap<>();
        crewMembersInjured.put(nonPlayerCrewMembers.getCrewMemberList().get(1).getSerialNumber(), nonPlayerCrewMembers.getCrewMemberList().get(1));
        crewMembersInjured.put(nonPlayerCrewMembers.getCrewMemberList().get(2).getSerialNumber(), nonPlayerCrewMembers.getCrewMemberList().get(2));
        crewMembersInjured.put(nonPlayerCrewMembers.getCrewMemberList().get(3).getSerialNumber(), nonPlayerCrewMembers.getCrewMemberList().get(3));
        
        Mockito.when(personnelLosses.crewMemberisWoundedToday(nonPlayerCrewMembers.getCrewMemberList().get(2))).thenReturn(true);
         
        CampaignAwardsGenerator awardsGenerator = new CampaignAwardsGenerator(campaign, aarContext);
        AARPersonnelAwards campaignMemberAwards = awardsGenerator.createCampaignMemberAwards(new ArrayList<>(crewMembersInjured.values()));
        
        Assertions.assertTrue (campaignMemberAwards.getCampaignMemberMedals().size() == 1);
        Assertions.assertTrue (!campaignMemberAwards.getCampaignMemberMedals().containsKey(nonPlayerCrewMembers.getCrewMemberList().get(1).getSerialNumber()));
        Assertions.assertTrue (campaignMemberAwards.getCampaignMemberMedals().containsKey(nonPlayerCrewMembers.getCrewMemberList().get(2).getSerialNumber()));
        Assertions.assertTrue (!campaignMemberAwards.getCampaignMemberMedals().containsKey(nonPlayerCrewMembers.getCrewMemberList().get(3).getSerialNumber()));
    }


    @Test
    public void testPromotionAwardedForVictoriesAndMissionsCompleted () throws PWCGException
    {     
        CrewMembers nonPlayerCrewMembers = CrewMemberFilter.filterActiveAINoWounded(campaign.getPersonnelManager().getAllCampaignMembers(), campaign.getDate());

        Map<Integer, CrewMember> crewMembersInjured = new HashMap<>();
        crewMembersInjured.put(nonPlayerCrewMembers.getCrewMemberList().get(1).getSerialNumber(), nonPlayerCrewMembers.getCrewMemberList().get(1));
        crewMembersInjured.put(nonPlayerCrewMembers.getCrewMemberList().get(2).getSerialNumber(), nonPlayerCrewMembers.getCrewMemberList().get(2));
        crewMembersInjured.put(nonPlayerCrewMembers.getCrewMemberList().get(3).getSerialNumber(), nonPlayerCrewMembers.getCrewMemberList().get(3));
        
        CampaignPersonnelTestHelper.addVictories(nonPlayerCrewMembers.getCrewMemberList().get(3), campaign.getDate(), 20);
        nonPlayerCrewMembers.getCrewMemberList().get(3).setRank("Leutnant");
        nonPlayerCrewMembers.getCrewMemberList().get(3).setBattlesFought(150);

        CampaignAwardsGenerator awardsGenerator = new CampaignAwardsGenerator(campaign, aarContext);
        AARPersonnelAwards campaignMemberAwards = awardsGenerator.createCampaignMemberAwards(new ArrayList<>(crewMembersInjured.values()));
                
        Assertions.assertTrue (campaignMemberAwards.getPromotions().size() == 1);
        Assertions.assertTrue (campaignMemberAwards.getPromotions().containsKey(nonPlayerCrewMembers.getCrewMemberList().get(3).getSerialNumber()));
    }
}
