package pwcg.campaign.crewmember;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.CampaignAces;
import pwcg.campaign.CampaignGeneratorModel;
import pwcg.campaign.CampaignPersonnelManager;
import pwcg.campaign.api.IRankHelper;
import pwcg.campaign.company.Company;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.factory.RankFactory;
import pwcg.campaign.personnel.CompanyPersonnel;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.testutils.CampaignCacheBase;
import pwcg.testutils.CompanyTestProfile;

@ExtendWith(MockitoExtension.class)
public class CrewMemberStatusTest
{
    @Mock private Campaign campaign;
    @Mock private CampaignPersonnelManager campaignPersonnelManager;
    @Mock private CampaignAces campaignAces;

    private Date campaignDate;
    private CompanyPersonnel companyPersonnel;
    private Company company;
    private SerialNumber serialNumber = new SerialNumber();
    
    private CrewMemberFactory companyMemberFactory;

    @BeforeEach
    public void setupTest() throws PWCGException
    {
        
        campaignDate = DateUtils.getDateYYYYMMDD("19420801");
        Mockito.when(campaign.getDate()).thenReturn(campaignDate);
        Mockito.when(campaign.getSerialNumber()).thenReturn(serialNumber);
        Mockito.when(campaign.getPersonnelManager()).thenReturn(campaignPersonnelManager);
        Mockito.when(campaignPersonnelManager.getCampaignAces()).thenReturn(campaignAces);
        List<TankAce> aces = new ArrayList<>();
        Mockito.when(campaignAces.getActiveCampaignAcesByCompany(Mockito.anyInt())).thenReturn(aces);

        company = PWCGContext.getInstance().getCompanyManager().getCompany(CompanyTestProfile.THIRD_DIVISION_PROFILE.getCompanyId()); 
        companyPersonnel = new CompanyPersonnel(campaign, company);

        companyMemberFactory = new  CrewMemberFactory (campaign, company, companyPersonnel);
    }
    
    @Test
    public void testUpdateStatusActive() throws PWCGException
    {
        CrewMember crewMember = companyMemberFactory.createInitialAICrewMember("Sergent");
        crewMember.setCrewMemberActiveStatus(CrewMemberStatus.STATUS_ACTIVE, campaign.getDate(), null);
        assert(crewMember.getCrewMemberActiveStatus() == CrewMemberStatus.STATUS_ACTIVE);
        assert(crewMember.getRecoveryDate() == null);
        assert(crewMember.getInactiveDate() == null);
    }

    @Test
    public void testUpdateStatusKilled() throws PWCGException
    {
        CrewMember crewMember = companyMemberFactory.createInitialAICrewMember("Sergent");
        crewMember.setCrewMemberActiveStatus(CrewMemberStatus.STATUS_KIA, campaign.getDate(), null);
        assert(crewMember.getCrewMemberActiveStatus() == CrewMemberStatus.STATUS_KIA);
        assert(crewMember.getRecoveryDate() == null);
        assert(crewMember.getInactiveDate().equals(campaign.getDate()));
    }
    
    @Test
    public void testUpdateStatusWounded() throws PWCGException
    {
        Date returnDate = DateUtils.advanceTimeDays(campaign.getDate(), 21);
        CrewMember crewMember = companyMemberFactory.createInitialAICrewMember("Sergent");
        crewMember.setCrewMemberActiveStatus(CrewMemberStatus.STATUS_WOUNDED, campaign.getDate(), returnDate);
        assert(crewMember.getCrewMemberActiveStatus() == CrewMemberStatus.STATUS_WOUNDED);
        assert(crewMember.getRecoveryDate().after(campaign.getDate()));
        assert(crewMember.getInactiveDate() == null);
    }
    
    @Test
    public void testUpdateStatusAiSeriousWound() throws PWCGException
    {
        Date returnDate = DateUtils.advanceTimeDays(campaign.getDate(), 90);
        CrewMember crewMember = companyMemberFactory.createInitialAICrewMember("Sergent");
        crewMember.setCrewMemberActiveStatus(CrewMemberStatus.STATUS_SERIOUSLY_WOUNDED, campaign.getDate(), returnDate);
        assert(crewMember.getCrewMemberActiveStatus() == CrewMemberStatus.STATUS_SERIOUSLY_WOUNDED);
        assert(crewMember.getRecoveryDate() == null);
        assert(crewMember.getInactiveDate().equals(campaign.getDate()));
    }
    
    @Test
    public void testUpdateStatusPlayerWound() throws PWCGException
    {
        ArmedService service = company.determineServiceForCompany(campaignDate);
        String companyName = company.determineDisplayName(campaignDate);
        
        IRankHelper rank = RankFactory.createRankHelper();
        String rankName = rank.getRankByService(2, service);

        CampaignGeneratorModel generatorModel = new CampaignGeneratorModel();
        generatorModel.setCampaignDate(campaignDate);
        generatorModel.setPlayerName(CampaignCacheBase.TEST_CAMPAIGN_NAME);
        generatorModel.setPlayerRank(rankName);
        generatorModel.setPlayerRegion("");
        generatorModel.setService(service);
        generatorModel.setCompanyName(companyName);
        CrewMember player = companyMemberFactory.createPlayer(generatorModel);
        Date returnDate = DateUtils.advanceTimeDays(campaign.getDate(), 90);
        player.setCrewMemberActiveStatus(CrewMemberStatus.STATUS_WOUNDED, campaign.getDate(), returnDate);
        assert(player.getCrewMemberActiveStatus() == CrewMemberStatus.STATUS_WOUNDED);
        assert(player.getRecoveryDate().after(campaign.getDate()));
        assert(player.getInactiveDate() == null);
    }

    @Test
    public void testUpdateStatusPlayerSeriousWound() throws PWCGException
    {
        ArmedService service = company.determineServiceForCompany(campaignDate);
        String companyName = company.determineDisplayName(campaignDate);
        
        IRankHelper rank = RankFactory.createRankHelper();
        String rankName = rank.getRankByService(2, service);

        CampaignGeneratorModel generatorModel = new CampaignGeneratorModel();
        generatorModel.setCampaignDate(campaignDate);
        generatorModel.setPlayerName(CampaignCacheBase.TEST_CAMPAIGN_NAME);
        generatorModel.setPlayerRank(rankName);
        generatorModel.setPlayerRegion("");
        generatorModel.setService(service);
        generatorModel.setCompanyName(companyName);
        CrewMember player = companyMemberFactory.createPlayer(generatorModel);
        Date returnDate = DateUtils.advanceTimeDays(campaign.getDate(), 90);
        player.setCrewMemberActiveStatus(CrewMemberStatus.STATUS_SERIOUSLY_WOUNDED, campaign.getDate(), returnDate);
        assert(player.getCrewMemberActiveStatus() == CrewMemberStatus.STATUS_SERIOUSLY_WOUNDED);
        assert(player.getRecoveryDate().after(campaign.getDate()));
        assert(player.getInactiveDate() == null);
    }
}
