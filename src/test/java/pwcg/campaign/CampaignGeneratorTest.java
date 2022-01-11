package pwcg.campaign;

import java.util.Date;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.campaign.api.IRankHelper;
import pwcg.campaign.company.Company;
import pwcg.campaign.company.CompanyManager;
import pwcg.campaign.context.Country;
import pwcg.campaign.context.FrontMapIdentifier;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.crewmember.CrewMembers;
import pwcg.campaign.factory.RankFactory;
import pwcg.campaign.personnel.CompanyPersonnel;
import pwcg.campaign.personnel.CrewMemberFilter;
import pwcg.campaign.tank.Equipment;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.testutils.CampaignCacheBase;
import pwcg.testutils.CompanyTestProfile;

public class CampaignGeneratorTest
{
	public CampaignGeneratorTest() throws PWCGException
	{
    	
        PWCGContext.getInstance().changeContext(FrontMapIdentifier.STALINGRAD_MAP);
	}
	
    @Test
    public void createWWIICampaign () throws PWCGException
    {        
    	Campaign campaign = generateCampaign(CompanyTestProfile.GROSS_DEUTSCHLAND_PROFILE.getCompanyId(), DateUtils.getDateYYYYMMDD("19420801"));
    	assert(campaign.getPersonnelManager().getAllActivePlayers().getCrewMemberList().size() == 1);
        CrewMember player = campaign.findReferencePlayer();
        Assertions.assertTrue (player.determineCompany().getCompanyId() == CompanyTestProfile.GROSS_DEUTSCHLAND_PROFILE.getCompanyId());
        Assertions.assertTrue (player.determineCompany().determineCompanyCountry(campaign.getDate()).getCountry() == Country.GERMANY);
        Assertions.assertTrue (campaign.getCampaignData().getName().equals(CampaignCacheBase.TEST_CAMPAIGN_NAME));
        assert(campaign.getPersonnelManager().getAllCompanyPersonnel().size() > 6);
        assert(campaign.getEquipmentManager().getEquipmentAllCompanies().size() > 6);
        
        for (CompanyPersonnel companyPersonnel : campaign.getPersonnelManager().getAllCompanyPersonnel())
        {
            CrewMembers crewMembers = CrewMemberFilter.filterActiveAIAndPlayerAndAces(companyPersonnel.getCrewMembersWithAces().getCrewMemberCollection(), campaign.getDate());
            assert(crewMembers.getCrewMemberList().size() == Company.COMPANY_STAFF_SIZE);
        }
        
        
        for (Equipment equipment : campaign.getEquipmentManager().getEquipmentAllCompanies().values())
        {
            assert(equipment.getActiveEquippedTanks().size() == Company.COMPANY_EQUIPMENT_SIZE);
        }
    }

    public Campaign generateCampaign(
                    int companyId,
                    Date campaignDate) throws PWCGException 
    {
        CompanyManager companyManager = PWCGContext.getInstance().getCompanyManager();
        
        Company company = companyManager.getCompany(companyId);
        
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
        
        CampaignGenerator generator = new CampaignGenerator(generatorModel);
        Campaign campaign = generator.generate();          

        PWCGContext.getInstance().setCampaign(campaign);
        
        return campaign;
    }


}
