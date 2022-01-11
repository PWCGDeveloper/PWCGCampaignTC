package pwcg.aar.outofmission.phase1.elapsedtime;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.api.IRankHelper;
import pwcg.campaign.company.Company;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
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
    private Campaign germanCampaign;
    private Campaign americanCampaign;
    
    @BeforeAll
    public void setupSuite() throws PWCGException
    {
        
        germanCampaign = CampaignCache.makeCampaign(CompanyTestProfile.GROSS_DEUTSCHLAND_PROFILE);
        americanCampaign = CampaignCache.makeCampaign(CompanyTestProfile.THIRD_DIVISION_PROFILE);
    }


    @Test
    public void testInitialVictoriesGermanFighter () throws PWCGException
    {
        Company company = PWCGContext.getInstance().getCompanyManager().getCompany(20112052);
        ArmedService service = company.determineServiceForCompany(germanCampaign.getDate());
        IRankHelper rankHelper = RankFactory.createRankHelper();
        CompanyPersonnel jg52Personnel = germanCampaign.getPersonnelManager().getCompanyPersonnel(20112052);

        CrewMembers crewMembers = CrewMemberFilter.filterActiveAIAndPlayerAndAces(jg52Personnel.getCrewMembersWithAces().getCrewMemberCollection(), germanCampaign.getDate());
        for (CrewMember crewMember : crewMembers.getCrewMemberCollection().values())
        {
            int rankPos = rankHelper.getRankPosByService(crewMember.getRank(), service);
            if (rankPos == 0)
            {
                validateVictoryRange (crewMember, 6, 30);
            }
            else  if (rankPos == 1)
            {
                validateVictoryRange (crewMember, 3, 20);
            }
            else  if (rankPos == 2)
            {
                validateVictoryRange (crewMember, 0, 8);
            }
            else
            {
                validateVictoryRange (crewMember, 0, 0);
            }

        }
    }

    @Test
    public void testInitialVictoriesRussianFighter () throws PWCGException
    {
        Company company = PWCGContext.getInstance().getCompanyManager().getCompany(10111126);
        ArmedService service = company.determineServiceForCompany(germanCampaign.getDate());
        IRankHelper rankHelper = RankFactory.createRankHelper();
        
        CrewMembers crewMembers = CrewMemberFilter.filterActiveAIAndPlayerAndAces(germanCampaign.getPersonnelManager().
                        getCompanyPersonnel(10111126).getCrewMembersWithAces().getCrewMemberCollection(), germanCampaign.getDate());
        for (CrewMember crewMember : crewMembers.getCrewMemberCollection().values())
        {
            int rankPos = rankHelper.getRankPosByService(crewMember.getRank(), service);
            if (rankPos == 0)
            {
                validateVictoryRange (crewMember, 0, 6);
            }
            else  if (rankPos == 1)
            {
                validateVictoryRange (crewMember, 0, 1);
            }
            else  if (rankPos == 2)
            {
                validateVictoryRange (crewMember, 0, 0);
            }
            else
            {
                validateVictoryRange (crewMember, 0, 0);
            }
        }
    }

    @Test
    public void testInitialVictoriesGermanFighterWest () throws PWCGException
    {
        Company company = PWCGContext.getInstance().getCompanyManager().getCompany(20112052);
        ArmedService service = company.determineServiceForCompany(germanCampaign.getDate());
        IRankHelper rankHelper = RankFactory.createRankHelper();
        CompanyPersonnel jg52Personnel = germanCampaign.getPersonnelManager().getCompanyPersonnel(20112052);

        CrewMembers crewMembers = CrewMemberFilter.filterActiveAIAndPlayerAndAces(jg52Personnel.getCrewMembersWithAces().getCrewMemberCollection(), germanCampaign.getDate());
        for (CrewMember crewMember : crewMembers.getCrewMemberCollection().values())
        {
            int rankPos = rankHelper.getRankPosByService(crewMember.getRank(), service);
            if (rankPos == 0)
            {
                validateVictoryRange (crewMember, 6, 30);
            }
            else  if (rankPos == 1)
            {
                validateVictoryRange (crewMember, 1, 20);
            }
            else  if (rankPos == 2)
            {
                validateVictoryRange (crewMember, 0, 8);
            }
            else
            {
                validateVictoryRange (crewMember, 0, 0);
            }

        }
    }

    @Test
    public void testInitialVictoriesAmericanFighterWest () throws PWCGException
    {

        Company company = PWCGContext.getInstance().getCompanyManager().getCompany(102362377);
        ArmedService service = company.determineServiceForCompany(americanCampaign.getDate());
        IRankHelper rankHelper = RankFactory.createRankHelper();
        CompanyPersonnel fg362Personnel = americanCampaign.getPersonnelManager().getCompanyPersonnel(102362377);

        CrewMembers crewMembers = CrewMemberFilter.filterActiveAIAndPlayerAndAces(fg362Personnel.getCrewMembersWithAces().getCrewMemberCollection(), americanCampaign.getDate());
        for (CrewMember crewMember : crewMembers.getCrewMemberCollection().values())
        {
            int rankPos = rankHelper.getRankPosByService(crewMember.getRank(), service);
            if (rankPos == 0)
            {
                validateVictoryRange (crewMember, 4, 17);
            }
            else  if (rankPos == 1)
            {
                validateVictoryRange (crewMember, 1, 10);
            }
            else  if (rankPos == 2)
            {
                validateVictoryRange (crewMember, 0, 7);
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
        
        int numVictories = crewMember.getCrewMemberVictories().getAirToAirVictoryCount();
        if (numVictories < min || numVictories > max)
        {
            System.out.println("Victoris not in range : " + numVictories + "     Min: " + min + "     Max: " + max);
        }
        assert(numVictories >= min);
        assert(numVictories <= max);
    }

}
