package pwcg.mission.platoon.tank;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.api.ICountry;
import pwcg.campaign.company.Company;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.crewmember.TankAce;
import pwcg.campaign.tank.Equipment;
import pwcg.campaign.tank.EquippedTank;
import pwcg.core.constants.AiSkillLevel;
import pwcg.core.constants.Callsign;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;
import pwcg.core.utils.PWCGLogger.LogLevel;
import pwcg.mission.platoon.PlatoonInformation;
import pwcg.mission.playerunit.crew.PlatoonCrewBuilder;

public class PlayerTankMcuFactory
{    
    private PlatoonInformation platoonInformation;
    
    public PlayerTankMcuFactory(PlatoonInformation platoonInformation)
    {
        this.platoonInformation = platoonInformation;
    }

    public List<TankMcu> createTanksForUnit(int numTanks) throws PWCGException
    {
        List<CrewMember> crewsForPlatoon = buildUnitCrews(numTanks);
        if (crewsForPlatoon.size() < numTanks)
        {
            numTanks = crewsForPlatoon.size();
        }
        List<EquippedTank> tanksTypesForPlatoon = buildEquipmentForPlatoon(numTanks);
        List<TankMcu> tanksForPlatoon = createTanks(tanksTypesForPlatoon, crewsForPlatoon);
        
        return tanksForPlatoon;
    }
    
    public static TankMcu createTankMcuByTankType (EquippedTank equippedTank, ICountry country, CrewMember tankCommander) throws PWCGException
    {
        TankMcu tank = new TankMcu(equippedTank, country, tankCommander);
        return tank;
    }

    private List<EquippedTank> buildEquipmentForPlatoon(int numTanks) throws PWCGException 
    {
        Equipment equipmentForCompany = platoonInformation.getCampaign().getEquipmentManager().getEquipmentForCompany(platoonInformation.getCompany().getCompanyId());
        PlayerPlatoonTankTypeBuilder tankTypeBuilder = new PlayerPlatoonTankTypeBuilder(equipmentForCompany, numTanks);
        List<EquippedTank> tanksTypesForPlatoon = tankTypeBuilder.getTankListForPlatoon();
        return tanksTypesForPlatoon;
    }

    private List<CrewMember> buildUnitCrews(int numTanks) throws PWCGException 
    {
        PlatoonCrewBuilder platoonCrewBuilder = new PlatoonCrewBuilder(platoonInformation);
        List<CrewMember> crewsForPlatoon = platoonCrewBuilder.createCrewAssignmentsForPlatoon(numTanks);
        return crewsForPlatoon;
    }

    private List<TankMcu> createTanks(List<EquippedTank> tanksTypesForPlatoon, List<CrewMember> crewsForPlatoon) throws PWCGException
    {        
        List<TankMcu> tanksForPlatoon = new ArrayList<>();
        for (int index = 0; index < tanksTypesForPlatoon.size(); ++index)
        {
            try
            {
                EquippedTank equippedTank = tanksTypesForPlatoon.get(index);
                CrewMember tankCommander = crewsForPlatoon.get(index);            
                TankMcu tank = createTankMcuByTankType(equippedTank, platoonInformation.getCompany().getCountry(), tankCommander);
                if (index > 0)
                {
                    TankMcu leadTank = tanksForPlatoon.get(0);
                    tank.setTarget(leadTank.getLinkTrId());
                }
                tanksForPlatoon.add(tank);
            }
            catch (Exception e)
            {
                e.printStackTrace();
                PWCGLogger.log(LogLevel.ERROR, e.getMessage());
            }
        }
        
        initializeTankParameters(tanksForPlatoon);
        return tanksForPlatoon;
    }

    private void initializeTankParameters(List<TankMcu> tanksForPlatoon) throws PWCGException
    {
        int numInFormation = 1;
        for (TankMcu tank : tanksForPlatoon)
        {
            setPlaceInFormation(numInFormation, tank);
            setTankDescription(tank);
            setTankCallsign(numInFormation, tank);
            setAiSkillLevelForTank(tank);
            ++numInFormation;
        }
    }

    private void setPlaceInFormation(int numInFormation, TankMcu aiTank)
    {
        aiTank.setNumberInFormation(numInFormation);
    }

    private void setTankDescription(TankMcu tank) throws PWCGException
    {
        tank.setDescription(tank.getTankCommander().getNameAndRank());
    }

    private void setTankCallsign(int numInFormation, TankMcu tank)
    {
        Company company = (Company)platoonInformation.getCompany();
        Callsign callsign = company.determineCurrentCallsign(platoonInformation.getCampaign().getDate());

        tank.setCallsign(callsign);
        tank.setCallnum(numInFormation);
    }

    private void setAiSkillLevelForTank(TankMcu tank) throws PWCGException
    {
        AiSkillLevel aiLevel = AiSkillLevel.COMMON;
        if (tank.getTankCommander().isPlayer())
        {
            aiLevel = AiSkillLevel.PLAYER;

        }
        else if (tank.getTankCommander() instanceof TankAce)
        {
            aiLevel = AiSkillLevel.ACE;

        }
        else
        {
            aiLevel = assignAiSkillLevel(tank);
        }

        tank.setAiLevel(aiLevel);
    }

    private AiSkillLevel assignAiSkillLevel(TankMcu tank) throws PWCGException
    {
        AiSkillLevel aiLevel = tank.getTankCommander().getAiSkillLevel();
        return aiLevel;
    }
 }
