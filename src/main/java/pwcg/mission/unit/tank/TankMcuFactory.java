package pwcg.mission.unit.tank;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.crewmember.TankAce;
import pwcg.campaign.tank.Equipment;
import pwcg.campaign.tank.EquippedTank;
import pwcg.core.constants.AiSkillLevel;
import pwcg.core.constants.Callsign;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;
import pwcg.core.utils.PWCGLogger.LogLevel;
import pwcg.mission.playerunit.crew.UnitCrewBuilder;
import pwcg.mission.unit.TankMcu;
import pwcg.mission.unit.PlatoonInformation;

public class TankMcuFactory
{    
    private PlatoonInformation platoonInformation;
    
    public TankMcuFactory(PlatoonInformation platoonInformation)
    {
        this.platoonInformation = platoonInformation;
    }

    public List<TankMcu> createTanksForUnit(int numTanks) throws PWCGException
    {
        List<CrewMember> crewsForFlight = buildUnitCrews(numTanks);
        if (crewsForFlight.size() < numTanks)
        {
            numTanks = crewsForFlight.size();
        }
        List<EquippedTank> tanksTypesForFlight = buildEquipmentForFllght(numTanks);
        List<TankMcu> tanksForFlight = createTanks(tanksTypesForFlight, crewsForFlight);
        
        return tanksForFlight;
    }
    
    public static TankMcu createTankMcuByTankType (Campaign campaign, EquippedTank equippedTank, ICountry country, CrewMember tankCommander) throws PWCGException
    {
        TankMcu tank = new TankMcu(campaign, tankCommander);
        tank.buildTank(equippedTank, country);
        return tank;
    }

    private List<EquippedTank> buildEquipmentForFllght(int numTanks) throws PWCGException 
    {
        Equipment equipmentForCompany = platoonInformation.getCampaign().getEquipmentManager().getEquipmentForCompany(platoonInformation.getCompany().getCompanyId());
        UnitTankTypeBuilder tankTypeBuilder = new UnitTankTypeBuilder(equipmentForCompany, numTanks);
        List<EquippedTank> tanksTypesForFlight =tankTypeBuilder.getTankListForFlight();
        return tanksTypesForFlight;
    }

    private List<CrewMember> buildUnitCrews(int numTanks) throws PWCGException 
    {
        UnitCrewBuilder unitCrewBuilder = new UnitCrewBuilder(platoonInformation);
        List<CrewMember> crewsForFlight = unitCrewBuilder.createCrewAssignmentsForFlight(numTanks);
        return crewsForFlight;
    }

    private List<TankMcu> createTanks(List<EquippedTank> tanksTypesForFlight, List<CrewMember> crewsForFlight) throws PWCGException
    {        
        List<TankMcu> tanksForFlight = new ArrayList<>();
        for (int index = 0; index < tanksTypesForFlight.size(); ++index)
        {
            try
            {
                EquippedTank equippedTank = tanksTypesForFlight.get(index);
                CrewMember tankCommander = crewsForFlight.get(index);            
                TankMcu tank = createTankMcuByTankType(platoonInformation.getCampaign(), equippedTank, platoonInformation.getCompany().getCountry(), tankCommander);
                if (index > 0)
                {
                    TankMcu leadTank = tanksForFlight.get(0);
                    tank.setTarget(leadTank.getLinkTrId());
                }
                tanksForFlight.add(tank);
            }
            catch (Exception e)
            {
                e.printStackTrace();
                PWCGLogger.log(LogLevel.ERROR, e.getMessage());
            }
        }
        
        initializeTankParameters(tanksForFlight);
        return tanksForFlight;
    }

    private void initializeTankParameters(List<TankMcu> tanksForFlight) throws PWCGException
    {
        int numInFormation = 1;
        for (TankMcu tank : tanksForFlight)
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
        tank.setDesc(tank.getTankCommander().getNameAndRank());
    }

    private void setTankCallsign(int numInFormation, TankMcu tank)
    {
        Callsign callsign = platoonInformation.getCompany().determineCurrentCallsign(platoonInformation.getCampaign().getDate());

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
