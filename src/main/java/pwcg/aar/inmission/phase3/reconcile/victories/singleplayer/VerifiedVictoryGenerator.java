package pwcg.aar.inmission.phase3.reconcile.victories.singleplayer;

import java.util.Map;

import pwcg.aar.data.AARContext;
import pwcg.aar.inmission.phase2.logeval.AARMissionEvaluationData;
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

    // TODO COOP CLAIM rethink player declarations.  Definitely keep for SP.  
    // Probably keep for MP too but for MP assign without claim.
    // Need factory to create variants.
    public ConfirmedVictories createVerifiedictories(Map<Integer, PlayerDeclarations> playerDeclarations) throws PWCGException
    {
        AARMissionEvaluationData evaluationData = aarContext.getMissionEvaluationData();

        VictorySorter victorySorter = new VictorySorter();
        victorySorter.sortVictories(evaluationData.getVictoryResults());
        PlayerDeclarationResolution playerClaimResolution = new PlayerDeclarationResolution(campaign, evaluationData, victorySorter, playerDeclarations);
        
        AiDeclarationResolver aiDeclarationResolution = createAiDeclarationResolver();        
        GroundDeclarationResolver groundDeclarationResolver = new GroundDeclarationResolver(victorySorter);
        
        ConfirmedVictories verifiedMissionResultVictorys = new ConfirmedVictories();
        ConfirmedVictories verifiedPlayerMissionResultVictorys = playerClaimResolution.determinePlayerAirResultsWithClaims();
        ConfirmedVictories verifiedAIMissionResultVictorys = aiDeclarationResolution.determineAiAirResults(victorySorter);
        ConfirmedVictories verifiedGroundMissionResultVictorys = groundDeclarationResolver.determineGroundResults();
        
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