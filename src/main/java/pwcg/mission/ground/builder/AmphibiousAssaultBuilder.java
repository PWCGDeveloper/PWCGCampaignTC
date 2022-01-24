package pwcg.mission.ground.builder;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.battle.AmphibiousAssault;
import pwcg.campaign.battle.AmphibiousAssaultShipDefinition;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.Mission;
import pwcg.mission.ground.org.GroundUnitCollection;

public class AmphibiousAssaultBuilder implements IBattleBuilder
{
    private Mission mission;
    private AmphibiousAssault amphibiousAssault;
    private GroundUnitCollection amphibiousAssaultUnits;
    
    public AmphibiousAssaultBuilder(Mission mission, AmphibiousAssault amphibiousAssault)
    {
        this.mission = mission;
        this.amphibiousAssault = amphibiousAssault;
    }

    @Override
    public GroundUnitCollection generateBattle() throws PWCGException
    {
        return generateAmphibiousAssault();
    }

    private GroundUnitCollection generateAmphibiousAssault() throws PWCGException
    {
        List<AmphibiousAssaultShipDefinition> shipsForMission = getLandingCraftForAssault();

        makeLandingCraft(shipsForMission);
        makeLanding(shipsForMission);
        makeDefense(shipsForMission);
        
        return amphibiousAssaultUnits;
    }

    private void makeLandingCraft(List<AmphibiousAssaultShipDefinition> shipsForMission) throws PWCGException
    {
        AmphibiousAssaultShipBuilder amphibiousAssaultShipBuilder = new AmphibiousAssaultShipBuilder(mission, amphibiousAssault, shipsForMission);
        amphibiousAssaultUnits = amphibiousAssaultShipBuilder.generateAmphibiousAssautShips();
    }

    private void makeLanding(List<AmphibiousAssaultShipDefinition> shipsForMission) throws PWCGException
    {
        for (AmphibiousAssaultShipDefinition shipDefinition :shipsForMission)
        {
            if (shipDefinition.getShipType().startsWith("land"))
            {
                AmphibiousAssaultAttackBuilder amphibiousAssaultShipBuilder = new AmphibiousAssaultAttackBuilder(mission, amphibiousAssault, shipDefinition);
                GroundUnitCollection assault = amphibiousAssaultShipBuilder.generateAmphibiousAssaultAttack();
                amphibiousAssaultUnits.merge(assault);
            }
        }
    }

    private void makeDefense(List<AmphibiousAssaultShipDefinition> shipsForMission) throws PWCGException
    {
        for (AmphibiousAssaultShipDefinition shipDefinition : shipsForMission)
        {
            if (shipDefinition.getShipType().startsWith("land"))
            {
                AmphibiousDefenseBuilder amphibiousDefenseBuilder = new AmphibiousDefenseBuilder(mission, amphibiousAssault, shipDefinition);
                GroundUnitCollection defense = amphibiousDefenseBuilder.generateAmphibiousAssaultDefense();
                amphibiousAssaultUnits.merge(defense);
            }
        }
    }

    private List<AmphibiousAssaultShipDefinition> getLandingCraftForAssault() throws PWCGException
    {
        int numLandingCraft = 8 + RandomNumberGenerator.getRandom(6);
        if (numLandingCraft > amphibiousAssault.getShipDefinitions().size())
        {
            numLandingCraft = amphibiousAssault.getShipDefinitions().size();
        }
        
        amphibiousAssault.shuffleLandingCraft();
        List<AmphibiousAssaultShipDefinition> shipsForMission = new ArrayList<>();
        for (int i = 0; i < numLandingCraft; ++ i)
        {
            if (i < (amphibiousAssault.getShipDefinitions().size()-1))
            {
                shipsForMission.add(amphibiousAssault.getShipDefinitions().get(i));
            }
        }
        return shipsForMission;
    }
 }
