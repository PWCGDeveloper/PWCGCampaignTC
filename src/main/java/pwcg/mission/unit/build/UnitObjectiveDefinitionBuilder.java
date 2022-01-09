package pwcg.mission.unit.build;

import pwcg.campaign.company.Company;
import pwcg.campaign.tank.PwcgRoleCategory;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.Mission;
import pwcg.mission.target.AssaultDefinition;
import pwcg.mission.unit.UnitMissionType;
import pwcg.mission.unit.UnitObjectiveDefinition;

public class UnitObjectiveDefinitionBuilder
{
    private Mission mission;
    private Company playerCompany;

    public UnitObjectiveDefinitionBuilder (Mission mission, Company playerCompany)
    {
        this.mission = mission;
        this.playerCompany = playerCompany;
    }
    
    public UnitObjectiveDefinition buildUnitObjective() throws PWCGException
    {
        AssaultDefinition assaultDefinition = mission.getBattleManager().getMissionAssaultDefinitions().get(0);
        assaultDefinition.getAssaultingCountry();
        
        Coordinate startLocation = getStartLocation(assaultDefinition);
        Coordinate endLocation = startLocation.copy();
        if(isAssault(assaultDefinition))
        {
            endLocation = assaultDefinition.getDefensePosition().copy();
        }

        UnitMissionType unitMissionType = UnitMissionType.ASSAULT;
        PwcgRoleCategory companyPrimaryRole = playerCompany.getSquadronRoles().selectSquadronPrimaryRoleCategory(mission.getCampaign().getDate());
        if(companyPrimaryRole == PwcgRoleCategory.MAIN_BATTLE_TANK)
        {
            if(!isAssault(assaultDefinition))
            {
                unitMissionType = UnitMissionType.DEFENSE;
            }
        }
        else if(companyPrimaryRole == PwcgRoleCategory.SELF_PROPELLED_GUN)
        {
            unitMissionType = UnitMissionType.INFANTRY_SUPPORT;
        }
        else if(companyPrimaryRole == PwcgRoleCategory.SELF_PROPELLED_AAA)
        {
            unitMissionType = UnitMissionType.AAA;
        }
        
        boolean isDefending = true;
        if(isAssault(assaultDefinition))
        {
            isDefending = false;
        }
        
        UnitObjectiveDefinition playerUnitObjectiveDefinition = new UnitObjectiveDefinition(unitMissionType, startLocation, endLocation, isDefending);
        return playerUnitObjectiveDefinition;
 
    }

    private boolean isAssault(AssaultDefinition assaultDefinition)
    {
        if (assaultDefinition.getAssaultingCountry().getSide() == playerCompany.getCountry().getSide())
        {
            return true;
        }
        return false;
    }

    private Coordinate getStartLocation(AssaultDefinition assaultDefinition) throws PWCGException
    {
        Coordinate unitPosition = assaultDefinition.getPositionForSide(playerCompany.getCountry().getSide());
        double angleFromEnemy = assaultDefinition.getOrientationFromEnemy(playerCompany.getCountry().getSide());
        double distanceBehindLines = 1200 + RandomNumberGenerator.getRandom(800);
        Coordinate unitPositionBehindLines = MathUtils.calcNextCoord(unitPosition, angleFromEnemy, distanceBehindLines);
        
        double angleAlongBattle = MathUtils.adjustAngle(angleFromEnemy, 90);
        if(RandomNumberGenerator.getRandom(100) < 50)
        {
            angleAlongBattle = MathUtils.adjustAngle(angleFromEnemy, 270);
        }
        double distanceAlongLines = 200 + RandomNumberGenerator.getRandom(800);       
        Coordinate finalUnitPosition = MathUtils.calcNextCoord(unitPositionBehindLines, angleAlongBattle, distanceAlongLines);

        return finalUnitPosition;
    }
}
