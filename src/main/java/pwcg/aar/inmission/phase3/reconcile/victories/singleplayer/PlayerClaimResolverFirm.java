package pwcg.aar.inmission.phase3.reconcile.victories.singleplayer;

import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogTank;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogVictory;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.tank.TankTypeInformation;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;
import pwcg.core.utils.PWCGLogger.LogLevel;

public class PlayerClaimResolverFirm
{    
    private PlayerClaimPlaneNameFinder claimPlaneNameFinder = new PlayerClaimPlaneNameFinder();
    
    public String getDestroyedTankDisplayNameAsFirm (CrewMember player, PlayerVictoryDeclaration playerDeclaration, LogVictory resultVictory) throws PWCGException
    {
        String destroyedTankDisplayName = "";
        
        if (!resultVictory.isConfirmed())
        {
            if (!VictoryResolverSameSideDetector.isSameSide(player, resultVictory))
            {
                if (resultVictory.getVictor() instanceof LogTank)
                {
                    if (PlayerVictoryResolver.isPlayerVictory(player, resultVictory.getVictor()))
                    {
                        if (resultVictory.getVictim() instanceof LogTank)
                        {
                            LogTank victimPlane = (LogTank)resultVictory.getVictim();
                            TankTypeInformation destroyedTank = PWCGContext.getInstance().getFullTankTypeFactory().createTankTypeByAnyName(victimPlane.getVehicleType());
                            TankTypeInformation claimedTank = PWCGContext.getInstance().getFullTankTypeFactory().createTankTypeByAnyName(playerDeclaration.getTankType());
            
                            if (destroyedTank == null || claimedTank == null)
                            {
                                PWCGLogger.log(LogLevel.ERROR, 
                                                "resolveAsFirmVictory: No tank found for claimed type " + playerDeclaration.getTankType() );
                                
                            }
                            else if (destroyedTank.getType().equals(claimedTank.getType()))
                            {
                                destroyedTankDisplayName = claimPlaneNameFinder.getShotDownPlaneDisplayName(playerDeclaration, resultVictory);
                            }
                        }
                    }
                }
            }
        }
        
        return destroyedTankDisplayName;
    }
        

    public String getDestroyedTankDisplayNameAsFirmNotExact(CrewMember player, PlayerVictoryDeclaration playerDeclaration, LogVictory resultVictory) throws PWCGException
    {
        String shotDownPlaneDisplayName = "";
        if (!resultVictory.isConfirmed())
        {
            if (!VictoryResolverSameSideDetector.isSameSide(player, resultVictory))
            {
                if (PlayerVictoryResolver.isPlayerVictory(player, resultVictory.getVictor()))
                {                    
                    TankTypeInformation declaredPlane = PWCGContext.getInstance().getFullTankTypeFactory().createTankTypeByAnyName(playerDeclaration.getTankType());
                    if (declaredPlane != null)
                    {
                        shotDownPlaneDisplayName = claimPlaneNameFinder.getShotDownPlaneDisplayName(playerDeclaration, resultVictory);
                    }
                }
            }
        }
        
        return shotDownPlaneDisplayName;
    }
}
