package pwcg.campaign.personnel;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import pwcg.campaign.Campaign;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.crewmember.CrewMemberStatus;
import pwcg.campaign.crewmember.CrewMembers;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.CompanyTestProfile;
import pwcg.testutils.CrewMemberPicker;

public class CrewMemberFilterTest
{
    private Campaign campaign;
    private Map<Integer, CrewMember> woundedCrewMembers = new HashMap<>();
    private Map<Integer, CrewMember> maimedCrewMembers = new HashMap<>();
    private Map<Integer, CrewMember> capturedCrewMembers = new HashMap<>();
    private Map<Integer, CrewMember> deadCrewMembers = new HashMap<>();
    
    @BeforeEach
    public void setupTest() throws PWCGException
     {
        
        campaign = CampaignCache.makeCampaign(CompanyTestProfile.GROSS_DEUTSCHLAND_PROFILE);
        
        while (woundedCrewMembers.size() < 5)
        {
            CrewMember crewMember = CrewMemberPicker.pickNonAceCampaignMember(campaign, CompanyTestProfile.GROSS_DEUTSCHLAND_PROFILE.getCompanyId());
            crewMember.setCrewMemberActiveStatus(CrewMemberStatus.STATUS_WOUNDED, campaign.getDate(), DateUtils.advanceTimeDays(campaign.getDate(), 10));
            if (!campaignMemberUsed(crewMember.getSerialNumber()) && crewMember.isPlayer() == false)
            {
                woundedCrewMembers.put(crewMember.getSerialNumber(), crewMember);
            }
        }
        
        while (maimedCrewMembers.size() < 4)
        {
            CrewMember crewMember = CrewMemberPicker.pickNonAceCampaignMember(campaign, CompanyTestProfile.GROSS_DEUTSCHLAND_PROFILE.getCompanyId());
            crewMember.setCrewMemberActiveStatus(CrewMemberStatus.STATUS_SERIOUSLY_WOUNDED, campaign.getDate(), null);
            if (!campaignMemberUsed(crewMember.getSerialNumber()) && crewMember.isPlayer() == false)
            {
                maimedCrewMembers.put(crewMember.getSerialNumber(), crewMember);
            }
        }
        
        while (deadCrewMembers.size() < 3)
        {
            CrewMember crewMember = CrewMemberPicker.pickNonAceCampaignMember(campaign, CompanyTestProfile.GROSS_DEUTSCHLAND_PROFILE.getCompanyId());
            crewMember.setCrewMemberActiveStatus(CrewMemberStatus.STATUS_KIA, campaign.getDate(), null);
            if (!campaignMemberUsed(crewMember.getSerialNumber()) && crewMember.isPlayer() == false)
            {
                deadCrewMembers.put(crewMember.getSerialNumber(), crewMember);
            }
        }
        
        while (capturedCrewMembers.size() < 2)
        {
            CrewMember crewMember = CrewMemberPicker.pickNonAceCampaignMember(campaign, CompanyTestProfile.GROSS_DEUTSCHLAND_PROFILE.getCompanyId());
            crewMember.setCrewMemberActiveStatus(CrewMemberStatus.STATUS_CAPTURED, campaign.getDate(), null);
            if (!campaignMemberUsed(crewMember.getSerialNumber()) && crewMember.isPlayer() == false)
            {
                capturedCrewMembers.put(crewMember.getSerialNumber(), crewMember);
            }
        }
    }
    
    private boolean campaignMemberUsed(int serialNumber)
    {
        if (woundedCrewMembers.containsKey(serialNumber))
        {
            return true;
        }
        
        if (maimedCrewMembers.containsKey(serialNumber))
        {
            return true;
        }
        
        if (capturedCrewMembers.containsKey(serialNumber))
        {
            return true;
        }

        if (deadCrewMembers.containsKey(serialNumber))
        {
            return true;
        }
        
        return false;
    }
    
    @Test
    public void validateFilters() throws PWCGException
    {
        Map<Integer, CrewMember> allCampaignMembers = campaign.getPersonnelManager().getAllCampaignMembers();
        int numAces = campaign.getPersonnelManager().getCampaignAces().getAllCampaignAces().size();
        int numPlayers = CrewMemberFilter.filterActivePlayers(allCampaignMembers, campaign.getDate()).getCrewMemberList().size();
        int numInactive = maimedCrewMembers.size() + capturedCrewMembers.size() + deadCrewMembers.size();
        int numWounded = woundedCrewMembers.size();
 
        assert(numPlayers == 1);

        CrewMembers crewMembers;
        crewMembers = CrewMemberFilter.filterActiveAIAndPlayerAndAces(allCampaignMembers, campaign.getDate());
        assert(crewMembers.getCrewMemberList().size() == (allCampaignMembers.size() - numInactive));
        
        crewMembers = CrewMemberFilter.filterActiveAI(allCampaignMembers, campaign.getDate());
        assert(crewMembers.getCrewMemberList().size() == (allCampaignMembers.size() - (numInactive + numAces + numPlayers)));
        
        crewMembers = CrewMemberFilter.filterActiveAIAndPlayer(allCampaignMembers, campaign.getDate());
        assert(crewMembers.getCrewMemberList().size() == (allCampaignMembers.size() - (numInactive + numAces)));
        
        crewMembers = CrewMemberFilter.filterActiveAIAndAces(allCampaignMembers, campaign.getDate());
        assert(crewMembers.getCrewMemberList().size() == (allCampaignMembers.size() - (numInactive + numPlayers)));
        
        crewMembers = CrewMemberFilter.filterInactiveAIAndPlayerAndAces(allCampaignMembers, campaign.getDate());
        assert(crewMembers.getCrewMemberList().size() == (numInactive));
        
        crewMembers = CrewMemberFilter.filterActiveAINoWounded(allCampaignMembers, campaign.getDate());
        assert(crewMembers.getCrewMemberList().size() == (allCampaignMembers.size() - (numInactive + numAces + numPlayers + numWounded)));
        
        validateFiltersOnActive(crewMembers.getCrewMemberList().size());
    }
    
    public void validateFiltersOnActive(int numActiveExpected) throws PWCGException
    {
        Map<Integer, CrewMember> allActiveCampaignMembers = campaign.getPersonnelManager().getActiveCampaignMembers();
        int numAces = campaign.getPersonnelManager().getCampaignAces().getActiveCampaignAces().size();
        int numPlayers = CrewMemberFilter.filterActivePlayers(allActiveCampaignMembers, campaign.getDate()).getCrewMemberList().size();
 
        assert(numPlayers == 1);
        assert(numAces == 0);
        assert(allActiveCampaignMembers.size() == (numActiveExpected + numAces + numPlayers));
    }

}
