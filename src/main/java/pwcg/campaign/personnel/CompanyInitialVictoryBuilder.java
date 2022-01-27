package pwcg.campaign.personnel;

import java.util.Calendar;
import java.util.Date;

import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.company.Company;
import pwcg.campaign.context.Country;
import pwcg.campaign.crewmember.TankVictoryBuilder;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.crewmember.GroundVictimGenerator;
import pwcg.campaign.crewmember.Victory;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.ground.vehicle.IVehicle;

public class CompanyInitialVictoryBuilder
{
    private Campaign campaign;
    private Company victorCompany;
    private int minVictories;
    private int maxVictories;

    public CompanyInitialVictoryBuilder(Campaign campaign, Company company)
    {
        this.campaign = campaign;
        this.victorCompany = company;
    }
    
    public void createCrewMemberVictories(CrewMember newCrewMember, int rankPos) throws PWCGException
    {
        initializeVictoriesFromRank(rankPos);
        factorServiceQuality(rankPos);

        int victories = calcNumberOfVictories(minVictories, maxVictories);
        addVictories(newCrewMember, victories);
    }

    private void initializeVictoriesFromRank(int rankPos) throws PWCGException
    {
        if (rankPos >= 3)
        {
            minVictories = 0;
            maxVictories = 0;
        }
        else if (rankPos == 2)
        {
            minVictories = 0;
            maxVictories = 1;
        }
        else if (rankPos == 1)
        {
            minVictories = 1;
            maxVictories = 2;
        }
        else if (rankPos == 0)
        {
            minVictories = 1;
            maxVictories = 3;
        }
    }

    private void factorServiceQuality(int rankPos) throws PWCGException
    {
        int minAdjustment = 0;
        int maxAdjustment = 0;

        ArmedService service = victorCompany.determineServiceForCompany(campaign.getDate());
        if (service.getCountry().getCountry() == Country.GERMANY)
        {
            if (rankPos == 2)
            {
                minAdjustment = 0;
                maxAdjustment = 2;
            }
            else if (rankPos == 1)
            {
                minAdjustment = 1;
                maxAdjustment = 3;
            }
            else if (rankPos == 0)
            {
                minAdjustment = 2;
                maxAdjustment = 5;
            }

        }

        minVictories += minAdjustment;
        maxVictories += maxAdjustment;
    }

    private int calcNumberOfVictories(int minPossibleVictories, int maxAdditionalVictories)
    {
        int victories = RandomNumberGenerator.getRandom(maxVictories - minVictories) + minVictories;
        
        if (victories < 0)
        {
            victories = 0;
        }
        
        return victories;
    }

    private void addVictories(CrewMember newCrewMember, int victories) throws PWCGException
    {
        for (int i = victories; i > 0; --i)
        {
            Date victoryDate = generateVictoryDate(i);
            Victory victory = generateVictory(newCrewMember, victoryDate);
            if (victory != null)
            {
                newCrewMember.addGroundVictory(victory);
            }
        }
    }

    private Victory generateVictory(CrewMember newCrewMember, Date victoryDate) throws PWCGException
    {
        GroundVictimGenerator duringCampaignVictimGenerator = new GroundVictimGenerator(victoryDate, newCrewMember);
        IVehicle victimVehicle = duringCampaignVictimGenerator.generateVictimVehicle();

        TankVictoryBuilder outOfMissionVictoryGenerator = new TankVictoryBuilder(newCrewMember, victimVehicle);
        Victory victory = outOfMissionVictoryGenerator.generateOutOfMissionVictory(victoryDate);
        return victory;
    }
    
    private Date generateVictoryDate(int i) throws PWCGException
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(campaign.getDate());
        calendar.add(Calendar.DAY_OF_YEAR, (3 * i * -1));
        Date victoryDate = calendar.getTime();

        victoryDate = BeforeCampaignDateFinder.useEarliestPossibleDate(victoryDate);
        return victoryDate;
    }
}
