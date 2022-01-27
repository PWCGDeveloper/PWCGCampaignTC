package pwcg.aar.outofmission.phase1.elapsedtime;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.api.IRankHelper;
import pwcg.campaign.company.Company;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.crewmember.CrewMembers;
import pwcg.campaign.crewmember.TankAce;
import pwcg.campaign.factory.RankFactory;
import pwcg.campaign.personnel.CompanyPersonnel;
import pwcg.campaign.personnel.CrewMemberFilter;
import pwcg.core.exception.PWCGException;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.CompanyTestProfile;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CrewMemberInitialVictoryBuilderTest
{
    @Test
    public void testInitialVictoriesGerman () throws PWCGException
    {
        Campaign germanCampaign = CampaignCache.makeCampaign(CompanyTestProfile.GROSS_DEUTSCHLAND_PROFILE);

        Company company = PWCGContext.getInstance().getCompanyManager().getCompany(CompanyTestProfile.GROSS_DEUTSCHLAND_PROFILE.getCompanyId());
        ArmedService service = company.determineServiceForCompany(germanCampaign.getDate());
        IRankHelper rankHelper = RankFactory.createRankHelper();
        CompanyPersonnel personnel = germanCampaign.getPersonnelManager().getCompanyPersonnel(CompanyTestProfile.GROSS_DEUTSCHLAND_PROFILE.getCompanyId());

        CrewMembers crewMembers = CrewMemberFilter.filterActiveAIAndPlayerAndAces(personnel.getCrewMembersWithAces().getCrewMemberCollection(), germanCampaign.getDate());
        for (CrewMember crewMember : crewMembers.getCrewMemberCollection().values())
        {
            int rankPos = rankHelper.getRankPosByService(crewMember.getRank(), service);
            if (rankPos == 0)
            {
                validateVictoryRange (crewMember, 3, 8);
            }
            else  if (rankPos == 1)
            {
                validateVictoryRange (crewMember, 2, 5);
            }
            else  if (rankPos == 2)
            {
                validateVictoryRange (crewMember, 0, 3);
            }
            else
            {
                validateVictoryRange (crewMember, 0, 0);
            }

        }
    }

    @Test
    public void testInitialVictoriesAmerican () throws PWCGException
    {
        Campaign americanCampaign = CampaignCache.makeCampaign(CompanyTestProfile.THIRD_DIVISION_PROFILE);

        Company company = PWCGContext.getInstance().getCompanyManager().getCompany(CompanyTestProfile.THIRD_DIVISION_PROFILE.getCompanyId());
        ArmedService service = company.determineServiceForCompany(americanCampaign.getDate());
        IRankHelper rankHelper = RankFactory.createRankHelper();
        
        CrewMembers crewMembers = CrewMemberFilter.filterActiveAIAndPlayerAndAces(americanCampaign.getPersonnelManager().
                        getCompanyPersonnel(CompanyTestProfile.THIRD_DIVISION_PROFILE.getCompanyId()).getCrewMembersWithAces().getCrewMemberCollection(), americanCampaign.getDate());
        for (CrewMember crewMember : crewMembers.getCrewMemberCollection().values())
        {
            int rankPos = rankHelper.getRankPosByService(crewMember.getRank(), service);
            if (rankPos == 0)
            {
                validateVictoryRange (crewMember, 1, 3);
            }
            else  if (rankPos == 1)
            {
                validateVictoryRange (crewMember, 1, 2);
            }
            else  if (rankPos == 2)
            {
                validateVictoryRange (crewMember, 0, 1);
            }
            else
            {
                validateVictoryRange (crewMember, 0, 0);
            }
        }
    }

    private void validateVictoryRange(CrewMember crewMember, int min, int max) throws PWCGException
    {
        if (crewMember instanceof TankAce)
        {
            return;
        }
        
        int numVictories = crewMember.getCrewMemberVictories().getTankVictoryCount();
        if (numVictories < min || numVictories > max)
        {
            System.out.println("Victoris not in range : " + numVictories + "     Min: " + min + "     Max: " + max);
        }
        assert(numVictories >= min);
        assert(numVictories <= max);
    }

}
