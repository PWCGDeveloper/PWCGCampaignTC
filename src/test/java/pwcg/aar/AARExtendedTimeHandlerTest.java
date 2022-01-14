package pwcg.aar;

import java.util.Date;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mock;

import pwcg.aar.data.AARContext;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogCrewMember;
import pwcg.campaign.Campaign;
import pwcg.campaign.CampaignMode;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.crewmember.CrewMemberStatus;
import pwcg.campaign.crewmember.CrewMembers;
import pwcg.campaign.personnel.CrewMemberFilter;
import pwcg.campaign.plane.EquippedPlane;
import pwcg.campaign.plane.PlaneStatus;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.CompanyTestProfile;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AARExtendedTimeHandlerTest
{
    private Campaign campaign;
    private AARContext aarContext;
    
    @Mock LogCrewMember playerLogCrewMember;
    
    @BeforeAll
    public void setupSuite() throws PWCGException
    {
        
        campaign = CampaignCache.makeCampaign(CompanyTestProfile.GROSS_DEUTSCHLAND_PROFILE);
        campaign.getCampaignData().setCampaignMode(CampaignMode.CAMPAIGN_MODE_SINGLE);
    }

    @BeforeEach
    public void setupTest() throws PWCGException
    {        
        aarContext = new AARContext(campaign);
    }

    @Test
    public void testPersonnelReplacedWhenTimePassed () throws PWCGException
    {
        CrewMembers crewMembers = CrewMemberFilter.filterActiveAIAndPlayerAndAces(
                        campaign.getPersonnelManager().getCompanyPersonnel(CompanyTestProfile.GROSS_DEUTSCHLAND_PROFILE.getCompanyId()).getCrewMembersWithAces().getCrewMemberCollection(), campaign.getDate());
        for (CrewMember crewMember : crewMembers.getCrewMemberList())
        {
            if (!crewMember.isPlayer())
            {
                crewMember.setCrewMemberActiveStatus(CrewMemberStatus.STATUS_KIA, campaign.getDate(), null);
            }
            CrewMembers crewMembersLeft = CrewMemberFilter.filterActiveAIAndPlayerAndAces(
                            campaign.getPersonnelManager().getCompanyPersonnel(CompanyTestProfile.GROSS_DEUTSCHLAND_PROFILE.getCompanyId()).getCrewMembersWithAces().getCrewMemberCollection(), campaign.getDate());
            if (crewMembersLeft.getCrewMemberCollection().size() < 3)
            {
                break;
            }
        }
        
        CrewMembers crewMembersLeft = CrewMemberFilter.filterActiveAIAndPlayerAndAces(
                        campaign.getPersonnelManager().getCompanyPersonnel(CompanyTestProfile.GROSS_DEUTSCHLAND_PROFILE.getCompanyId()).getCrewMembersWithAces().getCrewMemberCollection(), campaign.getDate());
        
        int numLeaves = 0;
        int numCrewMembers = crewMembersLeft.getCrewMemberCollection().size();
        while (numCrewMembers < 9)
        {
            AARExtendedTimeHandler extendedTimeHandler = new AARExtendedTimeHandler(campaign, aarContext);
            extendedTimeHandler.timePassedForLeave(21);
            if (numLeaves == 10)
            {
                break;
            }
            else
            {
                ++numLeaves;
                crewMembersLeft = CrewMemberFilter.filterActiveAIAndPlayerAndAces(
                                campaign.getPersonnelManager().getCompanyPersonnel(CompanyTestProfile.GROSS_DEUTSCHLAND_PROFILE.getCompanyId()).getCrewMembersWithAces().getCrewMemberCollection(), campaign.getDate());
                numCrewMembers = crewMembersLeft.getCrewMemberCollection().size();
            }
        }
        
        Assertions.assertTrue (numLeaves < 10);
        CrewMembers crewMembersAfter = CrewMemberFilter.filterActiveAIAndPlayerAndAces(
                        campaign.getPersonnelManager().getCompanyPersonnel(CompanyTestProfile.GROSS_DEUTSCHLAND_PROFILE.getCompanyId()).getCrewMembersWithAces().getCrewMemberCollection(), campaign.getDate());
        assert(crewMembersAfter.getCrewMemberCollection().size() >= 9);
    }

    
    @Test
    public void testEquipmentReplacedWhenTimePassed () throws PWCGException
    {
        Map<Integer, EquippedTank> activePlanes = campaign.getEquipmentManager().getEquipmentForCompany(CompanyTestProfile.GROSS_DEUTSCHLAND_PROFILE.getCompanyId()).getActiveEquippedTanks();
        for (EquippedTank equippedTank : activePlanes.values())
        {
            equippedTank.setTankStatus(TankStatus.STATUS_DESTROYED);
            Map<Integer, EquippedTank> activePlanesLeft = campaign.getEquipmentManager().getEquipmentForCompany(CompanyTestProfile.GROSS_DEUTSCHLAND_PROFILE.getCompanyId()).getActiveEquippedTanks();
            if (activePlanesLeft.size() < 3)
            {
                break;
            }
        }
        
        Map<Integer, EquippedTank> activePlanesLeft = campaign.getEquipmentManager().getEquipmentForCompany(CompanyTestProfile.GROSS_DEUTSCHLAND_PROFILE.getCompanyId()).getActiveEquippedTanks();
        int numLeaves = 0;
        int numPlanes = activePlanesLeft.size();
        while (numPlanes < 9)
        {
            AARExtendedTimeHandler extendedTimeHandler = new AARExtendedTimeHandler(campaign, aarContext);
            extendedTimeHandler.timePassedForLeave(21);
            if (numLeaves == 10)
            {
                break;
            }
            else
            {
                ++numLeaves;
                activePlanesLeft = campaign.getEquipmentManager().getEquipmentForCompany(CompanyTestProfile.GROSS_DEUTSCHLAND_PROFILE.getCompanyId()).getActiveEquippedTanks();
                numPlanes = activePlanesLeft.size();
            }
        }
        
        Assertions.assertTrue (numLeaves < 10);
        CrewMembers crewMembersAfter = CrewMemberFilter.filterActiveAIAndPlayerAndAces(
                        campaign.getPersonnelManager().getCompanyPersonnel(CompanyTestProfile.GROSS_DEUTSCHLAND_PROFILE.getCompanyId()).getCrewMembersWithAces().getCrewMemberCollection(), campaign.getDate());
        assert(crewMembersAfter.getCrewMemberCollection().size() >= 9);
    }

    @Test
    public void testTimePassedForLeave () throws PWCGException
    {
        Date startCampaignDate = campaign.getDate();
        AARExtendedTimeHandler extendedTimeHandler = new AARExtendedTimeHandler(campaign, aarContext);
        extendedTimeHandler.timePassedForLeave(21);
        Date endCampaignDate = campaign.getDate();
        
        assert(endCampaignDate.after(startCampaignDate));
        int daysPassed = DateUtils.daysDifference(startCampaignDate, endCampaignDate);
        assert(daysPassed > 19);
        assert(daysPassed < 23);
    }
    
    @Test
    public void testTimePassedForTransfer () throws PWCGException
    {
        Date startCampaignDate = campaign.getDate();
        AARExtendedTimeHandler extendedTimeHandler = new AARExtendedTimeHandler(campaign, aarContext);
        extendedTimeHandler.timePassedForTransfer(10);
        Date endCampaignDate = campaign.getDate();
        
        assert(endCampaignDate.after(startCampaignDate));
        int daysPassed = DateUtils.daysDifference(startCampaignDate, endCampaignDate);
        assert(daysPassed >= 9);
        assert(daysPassed <= 13);
    }
}
