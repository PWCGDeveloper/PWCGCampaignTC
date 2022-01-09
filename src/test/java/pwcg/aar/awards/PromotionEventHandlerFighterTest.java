package pwcg.aar.awards;

import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.aar.outofmission.phase2.awards.PromotionEventHandler;
import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.company.Company;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.crewmember.CrewMemberVictories;
import pwcg.campaign.crewmember.Victory;
import pwcg.campaign.plane.PwcgRoleCategory;
import pwcg.campaign.promotion.PromotionArbitrator;
import pwcg.core.exception.PWCGException;
import pwcg.product.fc.country.FCServiceManager;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.SquadronTestProfile;
import pwcg.testutils.VictoryMaker;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PromotionEventHandlerFighterTest
{
    private Campaign campaign;
    
    @Mock private ArmedService service;
    @Mock private Company squadron;
    @Mock private CrewMember crewMember;
    @Mock private CrewMemberVictories squadronMemberVictories;

    @BeforeAll
    public void setupSuite() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.TC);
        campaign = CampaignCache.makeCampaign(SquadronTestProfile.ESC_103_PROFILE);
    }

    @BeforeEach
    public void setupTest() throws PWCGException
    {        
        Mockito.when(crewMember.determineSquadron()).thenReturn(squadron);
        Mockito.when(crewMember.getCrewMemberVictories()).thenReturn(squadronMemberVictories);
        Mockito.when(squadronMemberVictories.getAirToAirVictoryCount()).thenReturn(0);        
        Mockito.when(squadron.determineSquadronPrimaryRoleCategory(Mockito.any())).thenReturn(PwcgRoleCategory.FIGHTER);
        Mockito.when(squadron.getService()).thenReturn(FCServiceManager.LAVIATION_MILITAIRE);
    }

    @Test
    public void promoteCorporalToSergent () throws PWCGException
    {     
        List<Victory> victories = VictoryMaker.makeMultipleAlliedVictories(1, campaign.getDate());

        Mockito.when(squadronMemberVictories.getAirToAirVictoryCount()).thenReturn(victories.size());
        Mockito.when(crewMember.determineService(ArgumentMatchers.<Date>any())).thenReturn(campaign.determinePlayerCompanies().get(0).determineServiceForSquadron(campaign.getDate()));
        Mockito.when(crewMember.getBattlesFought()).thenReturn(20);
        Mockito.when(crewMember.getRank()).thenReturn("Corporal");

        String promotion = PromotionEventHandler.promoteNonHistoricalCrewMembers(campaign, crewMember);

        Assertions.assertTrue (promotion.equals("Sergent"));
    }

    @Test
    public void promoteSergent () throws PWCGException
    {     
        List<Victory> victories = VictoryMaker.makeMultipleAlliedVictories(3, campaign.getDate());

        Mockito.when(squadronMemberVictories.getAirToAirVictoryCount()).thenReturn(victories.size());
        Mockito.when(crewMember.determineService(ArgumentMatchers.<Date>any())).thenReturn(campaign.determinePlayerCompanies().get(0).determineServiceForSquadron(campaign.getDate()));
        Mockito.when(crewMember.getBattlesFought()).thenReturn(50);
        Mockito.when(crewMember.getRank()).thenReturn("Sergent");

        String promotion = PromotionEventHandler.promoteNonHistoricalCrewMembers(campaign, crewMember);

        Assertions.assertTrue (promotion.equals("Sous Lieutenant"));
    }

    @Test
    public void promoteSousLieutenant () throws PWCGException
    {     
        List<Victory> victories = VictoryMaker.makeMultipleAlliedVictories(7, campaign.getDate());

        Mockito.when(squadronMemberVictories.getAirToAirVictoryCount()).thenReturn(victories.size());
        Mockito.when(crewMember.determineService(ArgumentMatchers.<Date>any())).thenReturn(campaign.determinePlayerCompanies().get(0).determineServiceForSquadron(campaign.getDate()));
        Mockito.when(crewMember.getBattlesFought()).thenReturn(80);
        Mockito.when(crewMember.getRank()).thenReturn("Sous Lieutenant");
        String promotion = PromotionEventHandler.promoteNonHistoricalCrewMembers(campaign, crewMember);

        Assertions.assertTrue (promotion.equals("Lieutenant"));
    }

    @Test
    public void promoteCapitaine () throws PWCGException
    {     
        List<Victory> victories = VictoryMaker.makeMultipleAlliedVictories(15, campaign.getDate());

        Mockito.when(squadronMemberVictories.getAirToAirVictoryCount()).thenReturn(victories.size());
        Mockito.when(crewMember.determineService(ArgumentMatchers.<Date>any())).thenReturn(campaign.determinePlayerCompanies().get(0).determineServiceForSquadron(campaign.getDate()));
        Mockito.when(crewMember.getBattlesFought()).thenReturn(110);
        Mockito.when(crewMember.getRank()).thenReturn("Lieutenant");
        Mockito.when(crewMember.isPlayer()).thenReturn(true);
        Mockito.when(crewMember.getCompanyId()).thenReturn(SquadronTestProfile.ESC_103_PROFILE.getCompanyId());

        String promotion = PromotionEventHandler.promoteNonHistoricalCrewMembers(campaign, crewMember);

        Assertions.assertTrue (promotion.equals("Capitaine"));
    }

    @Test
    public void promoteCapitaineFailNotPlayer () throws PWCGException
    {     
        List<Victory> victories = VictoryMaker.makeMultipleAlliedVictories(15, campaign.getDate());

        Mockito.when(squadronMemberVictories.getAirToAirVictoryCount()).thenReturn(victories.size());
        Mockito.when(crewMember.determineService(ArgumentMatchers.<Date>any())).thenReturn(campaign.determinePlayerCompanies().get(0).determineServiceForSquadron(campaign.getDate()));
        Mockito.when(crewMember.getBattlesFought()).thenReturn(110);
        Mockito.when(crewMember.getRank()).thenReturn("Lieutenant");
        Mockito.when(crewMember.isPlayer()).thenReturn(false);
        Mockito.when(crewMember.getCompanyId()).thenReturn(SquadronTestProfile.ESC_103_PROFILE.getCompanyId());

        String promotion = PromotionEventHandler.promoteNonHistoricalCrewMembers(campaign, crewMember);

        Assertions.assertTrue (promotion.equals(PromotionArbitrator.NO_PROMOTION));
    }

    @Test
    public void promoteCapitaineFailNotEnoughMissions () throws PWCGException
    {     
        List<Victory> victories = VictoryMaker.makeMultipleAlliedVictories(15, campaign.getDate());

        Mockito.when(squadronMemberVictories.getAirToAirVictoryCount()).thenReturn(victories.size());
        Mockito.when(crewMember.determineService(ArgumentMatchers.<Date>any())).thenReturn(campaign.determinePlayerCompanies().get(0).determineServiceForSquadron(campaign.getDate()));
        Mockito.when(crewMember.getBattlesFought()).thenReturn(109);
        Mockito.when(crewMember.getRank()).thenReturn("Lieutenant");

        String promotion = PromotionEventHandler.promoteNonHistoricalCrewMembers(campaign, crewMember);

        Assertions.assertTrue (promotion.equals(PromotionArbitrator.NO_PROMOTION));
    }

    @Test
    public void promoteCapitaineFailNotEnoughictories () throws PWCGException
    {     
        List<Victory> victories = VictoryMaker.makeMultipleAlliedVictories(14, campaign.getDate());

        Mockito.when(squadronMemberVictories.getAirToAirVictoryCount()).thenReturn(victories.size());
        Mockito.when(crewMember.determineService(ArgumentMatchers.<Date>any())).thenReturn(campaign.determinePlayerCompanies().get(0).determineServiceForSquadron(campaign.getDate()));
        Mockito.when(crewMember.getBattlesFought()).thenReturn(90);
        Mockito.when(crewMember.getRank()).thenReturn("Lieutenant");

        String promotion = PromotionEventHandler.promoteNonHistoricalCrewMembers(campaign, crewMember);

        Assertions.assertTrue (promotion.equals(PromotionArbitrator.NO_PROMOTION));
    }

}
