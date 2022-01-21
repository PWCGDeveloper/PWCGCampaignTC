package integration.campaign.io.json;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.api.TCServiceManager;
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
        validateFighterCrewMembers(campaign);        
        validateReconCrewMembers(campaign);        
    	validatePersonnelReplacements(campaign);
    	validateFighterEquipment(campaign);
    	validateReconEquipment(campaign);
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
    	ArmedService germanArmedService = armedServiceManager.getArmedServiceByName(TCServiceManager.WEHRMACHT, campaign.getDate());
        PersonnelReplacementsService germanReplacements = campaign.getPersonnelManager().getPersonnelReplacementsService(germanArmedService.getServiceId());
        assert(germanReplacements.getReplacements().getActiveCount(campaign.getDate()) == 20);
        assert(germanReplacements.getDailyReplacementRate() == 22);
        assert(germanReplacements.getLastReplacementDate().equals(campaign.getDate()));

        ArmedService belgianArmedService = armedServiceManager.getArmedServiceByName(TCServiceManager.AVIATION_MILITAIRE_BELGE_NAME, campaign.getDate());
        PersonnelReplacementsService belgianReplacements = campaign.getPersonnelManager().getPersonnelReplacementsService(belgianArmedService.getServiceId());
        assert(belgianReplacements.getReplacements().getActiveCount(campaign.getDate()) == 20);
        assert(belgianReplacements.getDailyReplacementRate() == 3);
    }

    private void validateReconCrewMembers(Campaign campaign) throws PWCGException
    {
        CompanyPersonnel companyPersonnel = campaign.getPersonnelManager().getCompanyPersonnel(CompanyTestProfile.RFC_2_PROFILE.getCompanyId());
        CrewMembers reconCompanyPersonnel = CrewMemberFilter.filterActiveAIAndPlayerAndAces(companyPersonnel.getCrewMembersWithAces().getCrewMemberCollection(), campaign.getDate());        
        Assertions.assertTrue (reconCompanyPersonnel.getCrewMemberList().size() == 12);
        for (CrewMember crewMember : reconCompanyPersonnel.getCrewMemberList())
        {
            Assertions.assertTrue (crewMember.getSerialNumber() > SerialNumber.AI_STARTING_SERIAL_NUMBER);
            Assertions.assertTrue (crewMember.getBattlesFought() > 0);
        }
    }

    private void validateFighterCrewMembers(Campaign campaign) throws PWCGException
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

    private void validateFighterEquipment(Campaign campaign) throws PWCGException
    {
        Equipment fighterCompanyEquipment = campaign.getEquipmentManager().getEquipmentForCompany(CompanyTestProfile.GROSS_DEUTSCHLAND_PROFILE.getCompanyId());
        Assertions.assertTrue (campaign.getSerialNumber().getNextTankSerialNumber() > SerialNumber.TANK_STARTING_SERIAL_NUMBER + 100);
        Assertions.assertTrue (fighterCompanyEquipment.getActiveEquippedTanks().size() >= 14);
        for (EquippedTank equippedTank : fighterCompanyEquipment.getActiveEquippedTanks().values())
        {
            Assertions.assertTrue (equippedTank.getSerialNumber() > SerialNumber.TANK_STARTING_SERIAL_NUMBER);
            Assertions.assertTrue (equippedTank.getArchType().equals("albatrosd"));
        }
    }

    private void validateReconEquipment(Campaign campaign) throws PWCGException
    {
        Equipment reconCompanyEquipment = campaign.getEquipmentManager().getEquipmentForCompany(CompanyTestProfile.RFC_2_PROFILE.getCompanyId());
        Assertions.assertTrue (campaign.getSerialNumber().getNextTankSerialNumber() > SerialNumber.TANK_STARTING_SERIAL_NUMBER + 100);
        Assertions.assertTrue (reconCompanyEquipment.getActiveEquippedTanks().size() >= 14);
        for (EquippedTank equippedTank : reconCompanyEquipment.getActiveEquippedTanks().values())
        {
            Assertions.assertTrue (equippedTank.getSerialNumber() > SerialNumber.TANK_STARTING_SERIAL_NUMBER);
            Assertions.assertTrue (equippedTank.getArchType().contains("aircodh4"));
        }
    }

    private void deleteCampaign()
    {
        CampaignRemover.deleteCampaign(CampaignCacheBase.TEST_CAMPAIGN_NAME);
    }
}
