package pwcg.aar.inmission.phase3.reconcile.victories.singleplayer;

import java.util.Map;

import pwcg.aar.inmission.phase2.logeval.AARMissionEvaluationData;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogTank;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogVictory;
import pwcg.aar.inmission.phase3.reconcile.victories.common.ConfirmedVictories;
import pwcg.aar.inmission.phase3.reconcile.victories.common.VictorySorter;
import pwcg.campaign.Campaign;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.core.exception.PWCGException;

public class PlayerDeclarationResolution
{
    private ConfirmedVictories confirmedPlayerVictories = new ConfirmedVictories();
    private AARMissionEvaluationData evaluationData;
    private Campaign campaign;
    private VictorySorter victorySorter;
    private Map<Integer, PlayerDeclarations> playerDeclarations;
    
    private PlayerClaimResolverFirm claimResolverFirm = new PlayerClaimResolverFirm();
    private PlayerClaimResolverFuzzy claimResolverFuzzy = new PlayerClaimResolverFuzzy();
    
    PlayerDeclarationResolution (Campaign campaign, AARMissionEvaluationData evaluationData, VictorySorter victorySorter, Map<Integer, PlayerDeclarations> playerDeclarations)
    {
        this.campaign = campaign;
        this.evaluationData = evaluationData;
        this.victorySorter = victorySorter;
        this.playerDeclarations = playerDeclarations;
    }

    ConfirmedVictories determinePlayerResultsWithClaims  () throws PWCGException 
    {
        for (Integer playerSerialNumber : playerDeclarations.keySet())
        {
            PlayerDeclarations playerDeclaration = playerDeclarations.get(playerSerialNumber);
            for (PlayerVictoryDeclaration victoryDeclaration : playerDeclaration.getDeclarations())
            {
                resolvePlayerTankClaim(playerSerialNumber, victoryDeclaration);
            }
        }
        
        PlayerVictoryReassigner playerVictoryReassigner = new PlayerVictoryReassigner(campaign);
        playerVictoryReassigner.resetUnclamedPlayerVictoriesForAssignmentToOthers(victorySorter);
        
        return confirmedPlayerVictories;
    }

    private void resolvePlayerTankClaim(Integer playerSerialNumber, PlayerVictoryDeclaration victoryDeclaration) throws PWCGException
    {        
        if (!resolveAsFirmVictory(playerSerialNumber, victoryDeclaration))
        {
            if (!resolveAsFirmVictoryNotExact(playerSerialNumber, victoryDeclaration))
            {
                if (!resolveAsFuzzyVictory(playerSerialNumber, victoryDeclaration))
                {
                    resolveAsFuzzyVictoryNotExact(playerSerialNumber, victoryDeclaration);
                }
            }
        }
    }

    private boolean resolveAsFirmVictory(Integer playerSerialNumber, PlayerVictoryDeclaration victoryDeclaration) throws PWCGException
    {
        for (LogVictory resultVictory : victorySorter.getFirmTankVictories())
        {
            CrewMember player = campaign.getPersonnelManager().getAnyCampaignMember(playerSerialNumber);
            if (!VictoryResolverSameSideDetector.isSameSide(player, resultVictory))
            {
                if (!resultVictory.isConfirmed())
                {
                    String destroyedTankDisplayName = claimResolverFirm.getDestroyedTankDisplayNameAsFirm(player, victoryDeclaration, resultVictory);
                    if (!destroyedTankDisplayName.isEmpty())
                    {
                        generatePlayerVictoryIfNotAlreadyConfirmed(playerSerialNumber, victoryDeclaration, resultVictory, destroyedTankDisplayName);
                        return true;
                    }
                }
            }
        }
        
        return false;
    }

    private boolean resolveAsFirmVictoryNotExact(Integer playerSerialNumber, PlayerVictoryDeclaration victoryDeclaration) throws PWCGException
    {
        for (LogVictory resultVictory : victorySorter.getFirmTankVictories())
        {
            CrewMember player = campaign.getPersonnelManager().getAnyCampaignMember(playerSerialNumber);
            if (!VictoryResolverSameSideDetector.isSameSide(player, resultVictory))
            {
                String destroyedTankDisplayName = claimResolverFirm.getDestroyedTankDisplayNameAsFirmNotExact(player, victoryDeclaration, resultVictory);
                if (!destroyedTankDisplayName.isEmpty())
                {
                    generatePlayerVictoryIfNotAlreadyConfirmed(playerSerialNumber, victoryDeclaration, resultVictory, destroyedTankDisplayName);
                    return true;
                }
            }
        }
        
        return false;
    }

    private boolean resolveAsFuzzyVictory(Integer playerSerialNumber, PlayerVictoryDeclaration victoryDeclaration) throws PWCGException
    {
        for (LogVictory resultVictory : victorySorter.getFuzzyTankVictories())
        {
            CrewMember player = campaign.getPersonnelManager().getAnyCampaignMember(playerSerialNumber);
            if (didPlayerDamageTank(playerSerialNumber, resultVictory))
            {
                if (!VictoryResolverSameSideDetector.isSameSide(player, resultVictory))
                {
                    String destroyedTankDisplayName = claimResolverFuzzy.getDestroyedTankDisplayNameAsFuzzy(victoryDeclaration, resultVictory);
                    if (!destroyedTankDisplayName.isEmpty())
                    {
                        generatePlayerVictoryIfNotAlreadyConfirmed(playerSerialNumber, victoryDeclaration, resultVictory, destroyedTankDisplayName);
                        return true;
                    }
                }
            }
        }
        
        return false;
    }

    private boolean resolveAsFuzzyVictoryNotExact(Integer playerSerialNumber, PlayerVictoryDeclaration victoryDeclaration) throws PWCGException
    {
        for (LogVictory resultVictory : victorySorter.getFuzzyTankVictories())
        {
            CrewMember player = campaign.getPersonnelManager().getAnyCampaignMember(playerSerialNumber);
            if (didPlayerDamageTank(playerSerialNumber, resultVictory))
            {
                if (!VictoryResolverSameSideDetector.isSameSide(player, resultVictory))
                {
                    String destroyedTankDisplayName = claimResolverFuzzy.getDestroyedTankDisplayNameAsFuzzyNotExact(player, victoryDeclaration, resultVictory);
                    if (!destroyedTankDisplayName.isEmpty())
                    {
                        generatePlayerVictoryIfNotAlreadyConfirmed(playerSerialNumber, victoryDeclaration, resultVictory, destroyedTankDisplayName);
                        return true;
                    }
                }
            }
        }
        
        return false;
    }

    private boolean didPlayerDamageTank(Integer playerSerialNumber, LogVictory resultVictory) throws PWCGException
    {
        LogTank playerTank = evaluationData.getTankInMissionBySerialNumber(playerSerialNumber);
        boolean didPlayerDamageTank = resultVictory.didCrewMemberDamageTank(playerTank.getId());
        return didPlayerDamageTank;
    }

    private void generatePlayerVictoryIfNotAlreadyConfirmed(Integer playerSerialNumber, PlayerVictoryDeclaration victoryDeclaration, LogVictory resultVictory, String destroyedTankName) throws PWCGException
    {
        if (!resultVictory.isConfirmed())
        {
            victoryDeclaration.confirmDeclaration(true, destroyedTankName);
    
            LogTank playerTank = evaluationData.getTankInMissionBySerialNumber(playerSerialNumber);
            if (playerTank != null)
            {
                resultVictory.setVictor(playerTank);
                resultVictory.setConfirmed(true);
        
                confirmedPlayerVictories.addVictory(resultVictory);
            }
        }
    }

}
