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
public class CrewMemberFactoryTest
{
    @Mock private Campaign campaign;
    @Mock private CampaignPersonnelManager campaignPersonnelManager;
    @Mock private CampaignAces campaignAces;
    
    private Date campaignDate;
    private CompanyPersonnel companyPersonnel;
    private Company company;
    private SerialNumber serialNumber = new SerialNumber();
    
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
        
        company = PWCGContext.getInstance().getCompanyManager().getCompany(CompanyTestProfile.TANK_DIVISION_147_PROFILE.getCompanyId()); 
        companyPersonnel = new CompanyPersonnel(campaign, company);
    }

    @Test
    public void testCreatePlayer() throws PWCGException
    {                
        ArmedService service = company.determineServiceForCompany(campaignDate);
        String companyName = company.determineDisplayName(campaignDate);
        
        IRankHelper rank = RankFactory.createRankHelper();
        String rankName = rank.getRankByService(2, service);

        CampaignGeneratorModel generatorModel = new CampaignGeneratorModel();
        generatorModel.setCampaignDate(campaignDate);
        generatorModel.setCampaignName(CampaignCacheBase.TEST_CAMPAIGN_NAME);
        generatorModel.setPlayerName(CampaignCacheBase.TEST_PLAYER_NAME);
        generatorModel.setPlayerRank(rankName);
        generatorModel.setPlayerRegion("");
        generatorModel.setService(service);
        generatorModel.setCompanyName(companyName);

        CrewMemberFactory companyMemberFactory = new  CrewMemberFactory (campaign, company, companyPersonnel);
        CrewMember player = companyMemberFactory.createPlayer(generatorModel);
        
        assert(player.isPlayer() == true);
        assert(player.getSerialNumber() >= SerialNumber.PLAYER_STARTING_SERIAL_NUMBER && player.getSerialNumber() < SerialNumber.AI_STARTING_SERIAL_NUMBER);
        assert(player.getName().equals(generatorModel.getPlayerName()));
        assert(player.getPicName() != null && !player.getPicName().isEmpty());
        assert(player.getRank().equals(generatorModel.getPlayerRank()));
        assert(player.getInactiveDate().equals(DateUtils.getEndOfWar()));
    }

    @Test
    public void testCreateAiCrewMember() throws PWCGException
    {
        CrewMemberFactory companyMemberFactory = new  CrewMemberFactory (campaign, company, companyPersonnel);
        CrewMember crewMember = companyMemberFactory.createInitialAICrewMember("Sergent");
        
        assert(crewMember.isPlayer() == false);
        assert(crewMember.getSerialNumber() > SerialNumber.AI_STARTING_SERIAL_NUMBER);
        assert(crewMember.getName() != null && !crewMember.getName().isEmpty());
        assert(crewMember.getPicName() != null && !crewMember.getPicName().isEmpty());
        assert(crewMember.getRank().equals("Sergent"));
        assert(crewMember.getInactiveDate().equals(DateUtils.getEndOfWar()));
    }
}
