package pwcg.aar.inmission.phase3.reconcile.victories.singleplayer;

import java.util.Map;

import pwcg.aar.data.AARContext;
import pwcg.aar.inmission.phase2.logeval.AARMissionEvaluationData;
import pwcg.aar.inmission.phase3.reconcile.victories.common.ConfirmedVictories;
import pwcg.aar.inmission.phase3.reconcile.victories.common.GroundDeclarationResolver;
import pwcg.aar.inmission.phase3.reconcile.victories.common.VictorySorter;
import pwcg.campaign.Campaign;
import pwcg.core.exception.PWCGException;

public class VerifiedVictoryGenerator
{
    private Campaign campaign;
    private AARContext aarContext;

    public VerifiedVictoryGenerator (Campaign campaign, AARContext aarContext)
    {
        this.campaign = campaign;
        this.aarContext = aarContext;
    }
    
    public ConfirmedVictories createVerifiedictories(Map<Integer, PlayerDeclarations> playerDeclarations) throws PWCGException
    {
        AARMissionEvaluationData evaluationData = aarContext.getMissionEvaluationData();

        VictorySorter victorySorter = new VictorySorter();
        victorySorter.sortVictories(evaluationData.getVictoryResults());

        PlayerDeclarationResolution playerClaimResolution = new PlayerDeclarationResolution(campaign, evaluationData, victorySorter, playerDeclarations);
        ConfirmedVictories verifiedPlayerMissionResultVictorys = playerClaimResolution.determinePlayerResultsWithClaims();
        
        AiDeclarationResolver aiDeclarationResolution = createAiDeclarationResolver();        
        ConfirmedVictories verifiedAIMissionResultVictorys = aiDeclarationResolution.determineAiTankResults(victorySorter);

        GroundDeclarationResolver groundDeclarationResolver = new GroundDeclarationResolver(victorySorter);
        ConfirmedVictories verifiedGroundMissionResultVictorys = groundDeclarationResolver.determineGroundResults();
        
        ConfirmedVictories verifiedMissionResultVictorys = new ConfirmedVictories();
        verifiedMissionResultVictorys.addConfirmedVictories(verifiedPlayerMissionResultVictorys);
        verifiedMissionResultVictorys.addConfirmedVictories(verifiedAIMissionResultVictorys);
        verifiedMissionResultVictorys.addConfirmedVictories(verifiedGroundMissionResultVictorys);
        
        return verifiedMissionResultVictorys;
    }

    private AiDeclarationResolver createAiDeclarationResolver() throws PWCGException
    {
        return new AiDeclarationResolver(campaign, aarContext);
    }

}
