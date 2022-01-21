package pwcg.campaign.outofmission;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.IRankHelper;
import pwcg.campaign.company.Company;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.crewmember.CrewMemberNames;
import pwcg.campaign.factory.RankFactory;
import pwcg.campaign.tank.EquippedTank;
import pwcg.campaign.tank.TankEquipmentFactory;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.RandomNumberGenerator;

public class BeforeCampaignVictimGenerator implements IVictimGenerator
{
    private CrewMember victimCrewMember;

    private Campaign campaign;
    private Company victimCompany;
    private Date date;

    public BeforeCampaignVictimGenerator (Campaign campaign, Company company, Date date)
    {
        this.campaign = campaign;
        this.victimCompany = company;
        this.date = date;
    }

    @Override
    public EquippedTank generateVictimPlane() throws PWCGException
    {
        EquippedTank equippedTank = TankEquipmentFactory.makeTankForBeforeCampaign(campaign, victimCompany.determineSide(), date);
        return equippedTank;
    }

    @Override
    public CrewMember generateVictimAiCrew() throws PWCGException 
    {        
        List<CrewMember> allCrewMembers = generateAICrewMembers(victimCompany, date);
        victimCrewMember = selectVictim(allCrewMembers);
        return victimCrewMember;
    }

    private CrewMember selectVictim(List<CrewMember> crewMembers) throws PWCGException 
    {
        int index = RandomNumberGenerator.getRandom(crewMembers.size());
        return crewMembers.get(index);
    }

    private List<CrewMember> generateAICrewMembers(Company company, Date campaignDate) throws PWCGException 
    {
        Map<Integer, Integer> numAtRank = new HashMap<Integer, Integer>();
        numAtRank.put(0, 1);
        numAtRank.put(1, 1);
        numAtRank.put(2, 4);
        numAtRank.put(3, 3);
        numAtRank.put(4, 3);
                
        return generateAISquadMembers(company, campaignDate, numAtRank);
    }

    private List<CrewMember> generateAISquadMembers(
                    Company company, 
                    Date campaignDate, 
                    Map<Integer, Integer> numAtRank)
                    throws PWCGException
    {
        // Add the AI crewMembers
        IRankHelper rankLists = RankFactory.createRankHelper();
        List<String> ranks = rankLists.getRanksByService(company.determineServiceForCompany(campaignDate));
        
        List<CrewMember> squadMembers = new ArrayList<CrewMember>();
        
        for (int i = 0; i < numAtRank.size(); ++i)
        {
            int rankPos = i;
            // Some countries have fewer ranks
            if (rankPos >= ranks.size())
            {
                rankPos = ranks.size() - 1;
            }

            int atThisRank = numAtRank.get(i);
            for (int j = 0; j < atThisRank; ++j)
            {
                CrewMember aiCrewMember = new CrewMember();

                String squaddieName = CrewMemberNames.getInstance().getName(company.determineServiceForCompany(campaignDate), new HashMap<>());
                aiCrewMember.setName(squaddieName);
                aiCrewMember.setRank(rankLists.getRankByService(rankPos, company.determineServiceForCompany(campaignDate)));

                squadMembers.add(aiCrewMember);
            }
        }
        
        return squadMembers;
    }
}
