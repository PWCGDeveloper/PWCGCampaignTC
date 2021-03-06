package pwcg.testutils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.CampaignGeneratorModel;
import pwcg.campaign.CampaignHumanCrewMemberHandler;
import pwcg.campaign.api.IRankHelper;
import pwcg.campaign.company.Company;
import pwcg.campaign.company.CompanyManager;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.crewmember.AiCrewMemberRemovalChooser;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.factory.RankFactory;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;

public abstract class CampaignCacheBase implements ICampaignCache
{
    public static final String TEST_CAMPAIGN_NAME = "Test Campaign";
    public static final String TEST_PLAYER_NAME = "Test Player";

    protected Map<String, CampaignGeneratorModel> campaignProfiles = new HashMap<>();
    protected abstract void loadCampaignProfiles() throws PWCGException;
    protected abstract Campaign makeCampaignFromModel(CampaignGeneratorModel model) throws PWCGException;
    protected abstract void initialize() throws PWCGException;


    protected void makeProfile(CompanyTestProfile profile) throws PWCGException
    {
        CampaignGeneratorModel generatorModel = makeCampaignModelForProfile (profile);
        campaignProfiles.put(profile.getKey(), generatorModel);
    }

    @Override
    public Campaign makeCampaignForceCreation(CompanyTestProfile profile) throws PWCGException
    {
        initialize();
        if (campaignProfiles.containsKey(profile.getKey()))
        {
            System.out.println("Create Test Campaign " + profile.getKey());

            CampaignGeneratorModel model = campaignProfiles.get(profile.getKey());
            Campaign campaign = makeCampaignFromModel(model);
            
            if (profile.isCompetitive())
            {
	            List<CrewMember> players = campaign.getPersonnelManager().getAllActivePlayers().getCrewMemberList();
	            
	            addMoreCrewMembersForCoop(campaign, "Company Mate", "Leutnant", TestIdentifiers.TEST_GERMAN_COMPANY_ID);
	            players = campaign.getPersonnelManager().getAllActivePlayers().getCrewMemberList();
	            
	            addMoreCrewMembersForCoop(campaign, "Friendly Fighter", "Leutnant", TestIdentifiers.TEST_GERMAN_COMPANY_COMPETITIVE_ID);
	            players = campaign.getPersonnelManager().getAllActivePlayers().getCrewMemberList();
	            
	            addMoreCrewMembersForCoop(campaign, "Friendly Bombermaj", "Hauptmann", TestIdentifiers.TEST_GERMAN_COMPANY_COMPETITIVE_ID);
	            players = campaign.getPersonnelManager().getAllActivePlayers().getCrewMemberList();

	            addMoreCrewMembersForCoop(campaign, "Enemy Fighter", "Leyitenant", TestIdentifiers.TEST_RUSSIAN_COMPANY_ID);
	            players = campaign.getPersonnelManager().getAllActivePlayers().getCrewMemberList();
	            
	            addMoreCrewMembersForCoop(campaign, "Enemy Bomber", "Major", TestIdentifiers.TEST_RUSSIAN_COMPANY_ID);
	            players = campaign.getPersonnelManager().getAllActivePlayers().getCrewMemberList();
	            
	            addMoreCrewMembersForCoop(campaign, "Enemy Bomber", "Kapitan", TestIdentifiers.TEST_RUSSIAN_COMPANY_COMPETITIVE_ID);
	            players = campaign.getPersonnelManager().getAllActivePlayers().getCrewMemberList();

	            assert(players.size() == 7);
            }
            return campaign;
        }
        
        throw new PWCGException("No campaign found for profile " + profile.getKey());
    }
    
    protected void addMoreCrewMembersForCoop(Campaign campaign, String name, String rank, int companyId) throws PWCGException
    {
        AiCrewMemberRemovalChooser crewMemberRemovalChooser = new AiCrewMemberRemovalChooser(campaign);
        CrewMember companyMemberToReplace = crewMemberRemovalChooser.findAiCrewMemberToRemove(rank, companyId);
        
        CampaignHumanCrewMemberHandler humanCrewMemberHandler = new CampaignHumanCrewMemberHandler(campaign);
        humanCrewMemberHandler.addNewCrewMember(
                name, 
                rank, 
                companyMemberToReplace.getSerialNumber(), 
                companyId);
    }
    
	public static CampaignGeneratorModel makeCampaignModelForProfile(CompanyTestProfile profile) throws PWCGException
    {
        CompanyManager companyManager = PWCGContext.getInstance().getCompanyManager();
        Company company = companyManager.getCompany(profile.getCompanyId());
        
        Date campaignDate = DateUtils.getDateYYYYMMDD(profile.getDateString());
        ArmedService service = company.determineServiceForCompany(campaignDate);
        String companyName = company.determineDisplayName(campaignDate);
        
        IRankHelper rank = RankFactory.createRankHelper();
        String rankName = rank.getRankByService(2, service);
    
        CampaignGeneratorModel generatorModel = new CampaignGeneratorModel();
        generatorModel.setCampaignDate(campaignDate);
        generatorModel.setCampaignName(TEST_CAMPAIGN_NAME);
        generatorModel.setPlayerName(TEST_PLAYER_NAME);
        generatorModel.setPlayerRank(rankName);
        generatorModel.setPlayerRegion("");
        generatorModel.setService(service);
        generatorModel.setCompanyName(companyName);
        generatorModel.setCampaignMode(profile.getCampaignMode());

        return generatorModel;
    }

}
