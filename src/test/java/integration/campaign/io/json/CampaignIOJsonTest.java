package integration.campaign.io.json;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.crewmember.CrewMembers;
import pwcg.campaign.crewmember.SerialNumber;
import pwcg.campaign.crewmember.TankAce;
import pwcg.campaign.factory.ArmedServiceFactory;
import pwcg.campaign.io.json.CampaignIOJson;
import pwcg.campaign.personnel.CompanyPersonnel;
import pwcg.campaign.personnel.CrewMemberFilter;
import pwcg.campaign.personnel.PersonnelReplacementsService;
import pwcg.campaign.tank.Equipment;
import pwcg.campaign.tank.EquippedTank;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.CampaignRemover;
import pwcg.core.utils.DateUtils;
import pwcg.product.bos.country.GermanServiceBuilder;
import pwcg.product.bos.country.RussianServiceBuilder;
import pwcg.product.bos.country.TCServiceManager;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.CampaignCacheBase;
import pwcg.testutils.CompanyTestProfile;

@ExtendWith(MockitoExtension.class)
public class CampaignIOJsonTest
{    
    @Test
    public void campaignIOJsonTest() throws PWCGException
    {
        

        deleteCampaign();
        writeCampaign();
        readCampaign();
        deleteCampaign();
    }

    private void writeCampaign() throws PWCGException
    {
        Campaign campaign = CampaignCache.makeCampaignOnDisk(CompanyTestProfile.GROSS_DEUTSCHLAND_PROFILE);
        CampaignIOJson.writeJson(campaign);
    }

    private void readCampaign() throws PWCGException
    {
        Campaign campaign = new Campaign();
        campaign.open(CampaignCacheBase.TEST_CAMPAIGN_NAME);
        PWCGContext.getInstance().setCampaign(campaign);

        validateCoreCampaign(campaign);        
        validateTankCrewMembers(campaign);        
    	validatePersonnelReplacements(campaign);
    	validateTankEquipment(campaign);
    }

    private void validateCoreCampaign(Campaign campaign) throws PWCGException
    {
    	CrewMembers players = campaign.getPersonnelManager().getAllActivePlayers();
        for (CrewMember player : players.getCrewMemberList())
        {
            Assertions.assertTrue (player.getSerialNumber() >= SerialNumber.PLAYER_STARTING_SERIAL_NUMBER && player.getSerialNumber() < SerialNumber.AI_STARTING_SERIAL_NUMBER);
        }
        
        Assertions.assertTrue (campaign.getDate().equals(DateUtils.getDateYYYYMMDD(CompanyTestProfile.GROSS_DEUTSCHLAND_PROFILE.getDateString())));
        Assertions.assertTrue (campaign.getCampaignData().getName().equals(CampaignCacheBase.TEST_CAMPAIGN_NAME));
        CrewMember player = campaign.findReferencePlayer();
        Assertions.assertTrue (player.getName().equals(CampaignCacheBase.TEST_PLAYER_NAME));
    }

    private void validatePersonnelReplacements(Campaign campaign) throws PWCGException
    {
        TCServiceManager armedServiceManager = ArmedServiceFactory.createServiceManager();
    	ArmedService germanArmedService = armedServiceManager.getArmedServiceByName(GermanServiceBuilder.WEHRMACHT_NAME);
        PersonnelReplacementsService germanReplacements = campaign.getPersonnelManager().getPersonnelReplacementsService(germanArmedService.getServiceId());
        assert(germanReplacements.getReplacements().getActiveCount(campaign.getDate()) == 20);
        assert(germanReplacements.getDailyReplacementRate() == 6);
        assert(germanReplacements.getLastReplacementDate().equals(campaign.getDate()));

        ArmedService belgianArmedService = armedServiceManager.getArmedServiceByName(RussianServiceBuilder.SSV_NAME);
        PersonnelReplacementsService belgianReplacements = campaign.getPersonnelManager().getPersonnelReplacementsService(belgianArmedService.getServiceId());
        assert(belgianReplacements.getReplacements().getActiveCount(campaign.getDate()) == 20);
        assert(belgianReplacements.getDailyReplacementRate() == 15);
    }

    private void validateTankCrewMembers(Campaign campaign) throws PWCGException
    {
        CompanyPersonnel companyPersonnel = campaign.getPersonnelManager().getCompanyPersonnel(CompanyTestProfile.GROSS_DEUTSCHLAND_PROFILE.getCompanyId());
        CrewMembers fighterCompanyPersonnel = CrewMemberFilter.filterActiveAIAndPlayerAndAces(companyPersonnel.getCrewMembersWithAces().getCrewMemberCollection(), campaign.getDate());        
        Assertions.assertTrue (campaign.getSerialNumber().getNextCrewMemberSerialNumber() > SerialNumber.AI_STARTING_SERIAL_NUMBER + 100);
        Assertions.assertTrue (fighterCompanyPersonnel.getCrewMemberList().size() >= 12);
        for (CrewMember crewMember : fighterCompanyPersonnel.getCrewMemberList())
        {
            if (crewMember.isPlayer())
            {
                Assertions.assertTrue (crewMember.getSerialNumber() >= SerialNumber.PLAYER_STARTING_SERIAL_NUMBER);
                Assertions.assertTrue (crewMember.getBattlesFought() == 0);
            }
            else if (crewMember instanceof TankAce)
            {
                Assertions.assertTrue (crewMember.getSerialNumber() >= SerialNumber.ACE_STARTING_SERIAL_NUMBER);
                Assertions.assertTrue (crewMember.getBattlesFought() > 0);
            }
            else
            {
                Assertions.assertTrue (crewMember.getSerialNumber() > SerialNumber.AI_STARTING_SERIAL_NUMBER);
                Assertions.assertTrue (crewMember.getBattlesFought() > 0);
            }
        }
    }

    private void validateTankEquipment(Campaign campaign) throws PWCGException
    {
        Equipment fighterCompanyEquipment = campaign.getEquipmentManager().getEquipmentForCompany(CompanyTestProfile.GROSS_DEUTSCHLAND_PROFILE.getCompanyId());
        Assertions.assertTrue (campaign.getSerialNumber().getNextTankSerialNumber() > SerialNumber.TANK_STARTING_SERIAL_NUMBER + 100);
        Assertions.assertTrue (fighterCompanyEquipment.getActiveEquippedTanks().size() >= 14);
        for (EquippedTank equippedTank : fighterCompanyEquipment.getActiveEquippedTanks().values())
        {
            Assertions.assertTrue (equippedTank.getSerialNumber() > SerialNumber.TANK_STARTING_SERIAL_NUMBER);
            Assertions.assertTrue (equippedTank.getArchType().equals("tiger"));
        }
    }

    private void deleteCampaign()
    {
        CampaignRemover.deleteCampaign(CampaignCacheBase.TEST_CAMPAIGN_NAME);
    }
}
