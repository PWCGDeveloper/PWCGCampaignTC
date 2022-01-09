package pwcg.mission;

import java.util.List;

import pwcg.campaign.company.Company;
import pwcg.campaign.context.PWCGContext;
import pwcg.core.exception.PWCGException;
import pwcg.mission.unit.IPlayerUnit;
import pwcg.mission.unit.IUnitPackage;
import pwcg.mission.unit.UnitInformation;
import pwcg.mission.unit.UnitInformationFactory;
import pwcg.mission.unit.UnitMissionType;
import pwcg.mission.unit.aaa.AAAPackage;
import pwcg.mission.unit.assault.AssaultPackage;
import pwcg.mission.unit.defense.DefensePackage;
import pwcg.mission.unit.infantrysupport.InfantrySupportPackage;

public class MissionUnitBuilder
{
    private Mission mission;

    public MissionUnitBuilder(Mission mission)
    {
        this.mission = mission;
    }

    public MissionUnits createPlayerUnits(MissionHumanParticipants participatingPlayers) throws PWCGException
    {
        MissionUnits missionUnits = new MissionUnits(mission);
        
        for(int companyId :participatingPlayers.getParticipatingCompanyIds())
        {
            Company playerCompany = PWCGContext.getInstance().getCompanyManager().getCompany(companyId);
            
            UnitInformation UnitInformation = UnitInformationFactory.buildUnitInformation(mission, playerCompany, participatingPlayers);

            List<IPlayerUnit> playerUnits = buildPlayerUnit(UnitInformation);
            for (IPlayerUnit playerUnit : playerUnits)
            {
                missionUnits.addPlayerUnit(playerUnit);
            }
        }
        
        return missionUnits;
    }
    
    private List<IPlayerUnit> buildPlayerUnit(UnitInformation unitInformation) throws PWCGException
    {
        IUnitPackage unitPackage = null;
        if (unitInformation.getMissionType() == UnitMissionType.ASSAULT)
        {
            unitPackage = new AssaultPackage();
        }
        else if (unitInformation.getMissionType() == UnitMissionType.DEFENSE)
        {
            unitPackage = new DefensePackage();
        }
        else if (unitInformation.getMissionType() == UnitMissionType.INFANTRY_SUPPORT)
        {
            unitPackage = new InfantrySupportPackage();
        }
        else if (unitInformation.getMissionType() == UnitMissionType.AAA)
        {
            unitPackage = new AAAPackage();
        }
        
        List<IPlayerUnit> playerUnits = unitPackage.createUnitPackage(unitInformation);
        return playerUnits;
    }
}
