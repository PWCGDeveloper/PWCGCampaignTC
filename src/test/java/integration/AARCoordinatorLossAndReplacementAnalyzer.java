package integration;

import java.util.Date;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import pwcg.aar.AARFactory;
import pwcg.aar.AAROutOfMissionStepper;
import pwcg.aar.data.AARContext;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogTank;
import pwcg.campaign.Campaign;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.crewmember.CrewMembers;
import pwcg.campaign.personnel.CompanyPersonnel;
import pwcg.campaign.personnel.CrewMemberFilter;
import pwcg.campaign.tank.Equipment;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.CompanyTestProfile;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AARCoordinatorLossAndReplacementAnalyzer
{
    private Campaign campaign;    

    @BeforeAll
    public void setupSuite() throws PWCGException
    {
        
        campaign = CampaignCache.makeCampaignOnDisk(CompanyTestProfile.GROSS_DEUTSCHLAND_PROFILE);
    }

    @Test
    public void runMissionAARLeave () throws PWCGException
    {
    	Date newDate = DateUtils.getDateYYYYMMDD("19450301");

        int totalAirVictories = 0;
        int totalGroundVictories = 0;
    	int totalPersonnelLosses = 0;
        int totalAlliedPersonnelLosses = 0;
        int totalAxisPersonnelLosses = 0;
        int totalEquipmentLosses = 0;
        int totalAlliedEquipmentLosses = 0;
        int totalAxisEquipmentLosses = 0;
        int totalMedalsAwarded = 0;
        int totalPromotionsAwarded = 0;
        int totalTransfers = 0;
        int cycleNum = 0;
    	
	    do 
	    {
	        AARContext aarContext = AARFactory.makeAARContext(campaign);
            ++cycleNum;

            AAROutOfMissionStepper stepper = AARFactory.makeAAROutOfMissionStepper(campaign, aarContext);
            stepper.oneStep();

	    	int viableCompanys = PWCGContext.getInstance().getCompanyManager().getViableCompanies(campaign).size();
            int airVictories = aarContext.getPersonnelAcheivements().getTotalAirToAirVictories();
            int groundVictories = aarContext.getPersonnelAcheivements().getTotalAirToGroundVictories();
	    	int losses = aarContext.getPersonnelLosses().getSquadMembersLostAndInjured().size();
	    	int replacements = campaign.getPersonnelManager().getReplacementCount();
	    	int medalsAwarded = aarContext.getPersonnelAwards().getMedalsAwarded().size();
            int promotionsAwarded = aarContext.getPersonnelAwards().getPromotions().size();
            int transfers = aarContext.getResupplyData().getCompanyTransferData().getCrewMembersTransferred().size();
            
            Map<Integer, CrewMember> allCampaignMembers = campaign.getPersonnelManager().getAllCampaignMembers();  
            CrewMembers activeAiCampaignMembers = CrewMemberFilter.filterActiveAI(allCampaignMembers, campaign.getDate());
            int numAiCrewMembers = activeAiCampaignMembers.getCrewMemberList().size();
            int equipmentLosses = aarContext.getEquipmentLosses().getTanksDestroyed().size();

            totalAirVictories += airVictories;
            totalGroundVictories += groundVictories;
            totalPersonnelLosses += losses;
            totalMedalsAwarded += medalsAwarded;
            totalPromotionsAwarded += promotionsAwarded;
            totalTransfers += transfers;
            totalEquipmentLosses += equipmentLosses;
            
	    	int axisPersonnelLosses = 0;
	    	int alliedPersonnelLosses = 0;
	    	for (CrewMember lostCrewMember : aarContext.getPersonnelLosses().getSquadMembersLostAndInjured().values())
	    	{
	    		if (lostCrewMember.determineCountry(campaign.getDate()).getSide() == Side.ALLIED)
	    		{
	    			++alliedPersonnelLosses;	    			
	    			++totalAlliedPersonnelLosses;	    			
	    		}
	    		else
	    		{
	    			++axisPersonnelLosses;
	    			++totalAxisPersonnelLosses;
	    		}
	    	}
            
            int alliedEquipmentLosses = 0;
            int axisEquipmentLosses = 0;
            for (LogTank lostPlane : aarContext.getEquipmentLosses().getTanksDestroyed().values())
            {
                if (lostPlane.getCountry().getSide() == Side.ALLIED)
                {
                    ++alliedEquipmentLosses;                 
                    ++totalAlliedEquipmentLosses;                    
                }
                else
                {
                    ++axisEquipmentLosses;
                    ++totalAxisEquipmentLosses;
                }
            }
            
            
            printShortHandedCompanys();
            
            System.out.println("=====================================================");
            System.out.println("Cycle: " + cycleNum);
            System.out.println("Air Victories: " + totalAirVictories);
            System.out.println("Ground Victories: " + totalGroundVictories);
            System.out.println("Losses: " + losses);
            System.out.println("  Allied Personnel: " + alliedPersonnelLosses);
            System.out.println("  Axis Personnel: " + axisPersonnelLosses);
            System.out.println("  Personnel Replacements Available: " + campaign.getPersonnelManager().getReplacementCount());
            System.out.println("  Allied Equipment: " + alliedEquipmentLosses);
            System.out.println("  Axis Equipment: " + axisEquipmentLosses);
            System.out.println("  Equipment Replacements Available: " + campaign.getEquipmentManager().getReplacementCount());
            System.out.println("Replacement: " + replacements);
            System.out.println("Companys: " + viableCompanys);
            System.out.println("Medals: " + medalsAwarded);
            System.out.println("Promotions: " + promotionsAwarded);
            System.out.println("Transfers: " + transfers);
            System.out.println("Num Ai CrewMembers: " + numAiCrewMembers);
            System.out.println("End of cycle " + cycleNum);
            System.out.println("=====================================================");

	    }
	    while(campaign.getDate().before(newDate));
	    
        
        System.out.println("Total Personnel Allied: " + totalAlliedPersonnelLosses);
        System.out.println("Total Personnel Axis: " + totalAxisPersonnelLosses);
        System.out.println("Total Personnel Losses: " + totalPersonnelLosses);
        System.out.println("");
        
        System.out.println("Total Equipment Allied: " + totalAlliedEquipmentLosses);
        System.out.println("Total Equipment Axis: " + totalAxisEquipmentLosses);
        System.out.println("Total Equipment Losses: " + totalEquipmentLosses);
        System.out.println("");

        System.out.println("Total Air Victories: " + totalAirVictories);
        System.out.println("Total Ground Victories: " + totalGroundVictories);
        System.out.println("Total Medals: " + totalMedalsAwarded);
        System.out.println("Total Promotions: " + totalPromotionsAwarded);
        System.out.println("Total Transfers: " + totalTransfers);
        
        
        assert(totalAirVictories > 0);
        assert(totalGroundVictories > 0);
        assert(totalPersonnelLosses > 0);
        assert(totalAlliedPersonnelLosses > 0);
        assert(totalAxisPersonnelLosses > 0);
        assert(totalMedalsAwarded > 0);
        assert(totalPromotionsAwarded > 0);
        assert(totalTransfers > 0);

    }

    private void printShortHandedCompanys() throws PWCGException
    {
        System.out.println("=====================================================");
        for (CompanyPersonnel companyPersonnel : campaign.getPersonnelManager().getAllCompanyPersonnel())
        {
            System.out.println(companyPersonnel.getCompany().determineDisplayName(campaign.getDate()));
            System.out.println(" Personnel size is " + companyPersonnel.getCrewMembersWithAces().getActiveCount(campaign.getDate()));
            
            Equipment equipment = campaign.getEquipmentManager().getEquipmentForCompany(companyPersonnel.getCompany().getCompanyId());
            System.out.println(" Equipment size is " + equipment.getActiveEquippedTanks().size());
        }
    }
}
