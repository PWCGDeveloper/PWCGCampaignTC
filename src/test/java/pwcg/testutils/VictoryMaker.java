package pwcg.testutils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pwcg.campaign.crewmember.CrewMemberStatus;
import pwcg.campaign.crewmember.Victory;
import pwcg.campaign.crewmember.VictoryEntity;
import pwcg.campaign.tank.TankAttributeMapping;

public class VictoryMaker
{
    public static List<Victory> makeMultipleAlliedVictories(int numVictories, Date date)
    {
        List<Victory> victories = new ArrayList<>();
        for (int i = 0; i < numVictories; ++i)
        {
            Victory victory  = russianVictorGermanVictim(date);
            victories.add(victory);
        }
        
        return victories;
    }
    
    public static List<Victory> makeMultipleCentralVictories(int numVictories, Date date)
    {
        List<Victory> victories = new ArrayList<>();
        for (int i = 0; i < numVictories; ++i)
        {
            Victory victory  = germanVictorRussianVictim(date);
            victories.add(victory);
        }
        
        return victories;
    }
    
    public static Victory russianVictorGermanVictim(Date date)
    {
        Victory victory = new Victory();
        victory.setCrashedInSight(true);
        victory.setDate(date);
        victory.setLocation("Near something");
        
        VictoryEntity victor = new VictoryEntity();
        victor.setAirOrGround(Victory.VEHICLE);
        victor.setCrewMemberStatus(CrewMemberStatus.STATUS_ACTIVE);
        victor.setCompanyName("147th Division");
        victor.setType(TankAttributeMapping.KV1_S.getTankType());
        victory.setVictor(victor);

        VictoryEntity victim = new VictoryEntity();
        victim.setAirOrGround(Victory.VEHICLE);
        victim.setCrewMemberStatus(CrewMemberStatus.STATUS_ACTIVE);
        victim.setCompanyName("");
        victor.setType(TankAttributeMapping.PZKW_IV_G.getTankType());
        victory.setVictim(victim);
        
        return victory;
    }
    
    
    public static Victory germanVictorRussianVictim(Date date)
    {
        Victory victory = new Victory();
        victory.setCrashedInSight(true);
        victory.setDate(date);
        victory.setLocation("Near something");
        
        VictoryEntity victor = new VictoryEntity();
        victor.setAirOrGround(Victory.VEHICLE);
        victor.setCrewMemberStatus(CrewMemberStatus.STATUS_ACTIVE);
        victor.setCompanyName("Gross Deutschland");
        victor.setType(TankAttributeMapping.PZKW_IV_G.getTankType());
        victory.setVictor(victor);

        VictoryEntity victim = new VictoryEntity();
        victim.setAirOrGround(Victory.VEHICLE);
        victim.setCrewMemberStatus(CrewMemberStatus.STATUS_ACTIVE);
        victim.setCompanyName("");
        victor.setType(TankAttributeMapping.T34_EARLY.getTankType());
        victory.setVictim(victim);
        
        return victory;
    }
}
