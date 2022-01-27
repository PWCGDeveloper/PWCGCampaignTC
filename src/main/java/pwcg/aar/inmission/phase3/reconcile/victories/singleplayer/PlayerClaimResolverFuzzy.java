package pwcg.aar.inmission.phase3.reconcile.victories.singleplayer;

import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogVictory;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.tank.TankTypeInformation;
import pwcg.core.exception.PWCGException;

public class PlayerClaimResolverFuzzy
{
    private PlayerClaimPlaneNameFinder claimPlaneNameFinder = new PlayerClaimPlaneNameFinder();

    public String getDestroyedTankDisplayNameAsFuzzy(PlayerVictoryDeclaration playerDeclaration, LogVictory resultVictory) throws PWCGException
    {
        String shotDownPlaneDisplayName = "";

        if (!resultVictory.isConfirmed())
        {
            TankTypeInformation shotDownPlane = PWCGContext.getInstance().getFullTankTypeFactory()
                    .createTankTypeByAnyName(resultVictory.getVictim().getVehicleType());
            TankTypeInformation claimedPlane = PWCGContext.getInstance().getFullTankTypeFactory().createTankTypeByAnyName(playerDeclaration.getTankType());

            if (shotDownPlane != null && claimedPlane != null)
            {
                if (shotDownPlane.getType().equals(claimedPlane.getType()))
                {
                    shotDownPlaneDisplayName = claimPlaneNameFinder.getShotDownPlaneDisplayName(playerDeclaration, resultVictory);
                }
            }
        }

        return shotDownPlaneDisplayName;
    }

    public String getDestroyedTankDisplayNameAsFuzzyNotExact(CrewMember player, PlayerVictoryDeclaration playerDeclaration, LogVictory resultVictory)
            throws PWCGException
    {
        String shotDownPlaneDisplayName = "";
        if (!resultVictory.isConfirmed())
        {
            if (!VictoryResolverSameSideDetector.isSameSide(player, resultVictory))
            {
                TankTypeInformation declaredPlane = PWCGContext.getInstance().getFullTankTypeFactory().createTankTypeByAnyName(playerDeclaration.getTankType());
                if (declaredPlane != null)
                {
                    shotDownPlaneDisplayName = claimPlaneNameFinder.getShotDownPlaneDisplayName(playerDeclaration, resultVictory);
                }
            }
        }

        return shotDownPlaneDisplayName;
    }

}
