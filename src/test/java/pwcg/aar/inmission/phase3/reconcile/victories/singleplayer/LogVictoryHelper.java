package pwcg.aar.inmission.phase3.reconcile.victories.singleplayer;

import java.util.ArrayList;
import java.util.List;

import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogAIEntity;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogBalloon;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogGroundUnit;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogTank;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogUnknown;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogVictory;
import pwcg.campaign.context.Country;
import pwcg.campaign.crewmember.SerialNumber;
import pwcg.core.location.Coordinate;
import pwcg.product.bos.country.BoSCountry;
import pwcg.testutils.CompanyTestProfile;

public class LogVictoryHelper
{
    private List<LogVictory> logVictories = new ArrayList<>();

    public void createPlaneVictory()
    {
        LogTank victor = makeVictor();
        
        LogTank victim = new LogTank(1);
        victim.setCrewMemberSerialNumber(SerialNumber.AI_STARTING_SERIAL_NUMBER + 100);
        victim.setVehicleType("albatrosd5");
        victim.setCountry(new BoSCountry(Country.GERMANY));
        victim.setCompanyId(CompanyTestProfile.GROSS_DEUTSCHLAND_PROFILE.getCompanyId());
        victim.intializeCrewMember(SerialNumber.AI_STARTING_SERIAL_NUMBER + 100);

        makeVictory(victor, victim);
    }

    public void createFuzzyPlaneVictory()
    {
        LogUnknown victor = new LogUnknown();
        
        LogTank victim = new LogTank(1);
        victim.setCrewMemberSerialNumber(SerialNumber.AI_STARTING_SERIAL_NUMBER + 100);
        victim.setVehicleType("albatrosd5");
        victim.setCountry(new BoSCountry(Country.GERMANY));
        victim.setCompanyId(CompanyTestProfile.GROSS_DEUTSCHLAND_PROFILE.getCompanyId());
        victim.intializeCrewMember(SerialNumber.AI_STARTING_SERIAL_NUMBER + 100);

        makeVictory(victor, victim);
    }

    public void createBalloonVictory()
    {
        LogTank victor = makeVictor();
        
        LogBalloon victim = new LogBalloon(10000);
        victim.setVehicleType("drachen");
        victim.setCountry(new BoSCountry(Country.GERMANY));

        makeVictory(victor, victim);
    }

    public void createFuzzyBalloonVictory()
    {
        LogUnknown victor = new LogUnknown();
        
        LogBalloon victim = new LogBalloon(10000);
        victim.setVehicleType("drachen");
        victim.setCountry(new BoSCountry(Country.GERMANY));

        makeVictory(victor, victim);
    }

    public void createGroundVictory()
    {
        LogTank victor = makeVictor();
        
        LogGroundUnit victim = new LogGroundUnit(1000);
        victim.setVehicleType("tank");
        victim.setCountry(new BoSCountry(Country.GERMANY));

        makeVictory(victor, victim);
    }

    public LogTank makeVictor()
    {
        LogTank victor = new LogTank(1);
        victor.setCrewMemberSerialNumber(SerialNumber.PLAYER_STARTING_SERIAL_NUMBER);
        victor.setVehicleType("spad13");
        victor.setCountry(new BoSCountry(Country.FRANCE));
        victor.setCompanyId(CompanyTestProfile.THIRD_DIVISION_PROFILE.getCompanyId());
        victor.intializeCrewMember(SerialNumber.PLAYER_STARTING_SERIAL_NUMBER);
        return victor;
    }

    public void makeVictory(LogAIEntity victor, LogAIEntity victim)
    {
        LogVictory resultVictory = new LogVictory(10);
        resultVictory.setLocation(new Coordinate(100.0, 0.0, 100.0));
        resultVictory.setVictor(victor);
        resultVictory.setVictim(victim);
        logVictories.add(resultVictory);
    }

    public List<LogVictory> getLogVictories()
    {
        return logVictories;
    }
}
