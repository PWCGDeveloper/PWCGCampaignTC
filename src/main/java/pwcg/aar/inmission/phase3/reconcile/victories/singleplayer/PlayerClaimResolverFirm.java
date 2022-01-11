package pwcg.aar.inmission.phase3.reconcile.victories.singleplayer;

import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogTank;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogVictory;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.tank.PwcgRoleCategory;
import pwcg.campaign.tank.TankType;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;
import pwcg.core.utils.PWCGLogger.LogLevel;

public class PlayerClaimResolverFirm
{    
    private PlayerClaimPlaneNameFinder claimPlaneNameFinder = new PlayerClaimPlaneNameFinder();
    
    public String getShotDownPlaneDisplayNameAsFirm (CrewMember player, PlayerVictoryDeclaration playerDeclaration, LogVictory resultVictory) throws PWCGException
    {
        String shotDownPlaneDisplayName = "";
        
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
                            TankType shotDownPlane = PWCGContext.getInstance().getTankTypeFactory().createTankTypeByAnyName(victimPlane.getVehicleType());
                            TankType claimedPlane = PWCGContext.getInstance().getTankTypeFactory().createTankTypeByAnyName(playerDeclaration.getAircraftType());
            
                            if (shotDownPlane == null || claimedPlane == null)
                            {
                                PWCGLogger.log(LogLevel.ERROR, 
                                                "resolveAsFirmVictory: No plane found for claimed type " + playerDeclaration.getAircraftType() );
                                
                            }
                            else if (shotDownPlane.getType().equals(claimedPlane.getType()))
                            {
                                shotDownPlaneDisplayName = claimPlaneNameFinder.getShotDownPlaneDisplayName(playerDeclaration, resultVictory);
                            }
                        }
                    }
                }
            }
        }
        
        return shotDownPlaneDisplayName;
    }
        

    public String getShotDownPlaneDisplayNameAsFirmNotExact(CrewMember player, PlayerVictoryDeclaration playerDeclaration, LogVictory resultVictory) throws PWCGException
    {
        String shotDownPlaneDisplayName = "";
        if (!resultVictory.isConfirmed())
        {
            if (!VictoryResolverSameSideDetector.isSameSide(player, resultVictory))
            {
                if (PlayerVictoryResolver.isPlayerVictory(player, resultVictory.getVictor()))
                {
                    PwcgRoleCategory victimApproximateRole = resultVictory.getVictim().getRoleCategory();
                    
                    TankType declaredPlane = PWCGContext.getInstance().getTankTypeFactory().createTankTypeByAnyName(playerDeclaration.getAircraftType());
                    if (declaredPlane != null)
                    {
                        PwcgRoleCategory declarationApproximateRole = declaredPlane.determinePrimaryRoleCategory();
                        
                        if (declarationApproximateRole == victimApproximateRole)
                        {
                            shotDownPlaneDisplayName = claimPlaneNameFinder.getShotDownPlaneDisplayName(playerDeclaration, resultVictory);
                        }
                    }
                }
            }
        }
        
        return shotDownPlaneDisplayName;
    }
}
