package pwcg.aar.inmission;

import java.util.Map;

import pwcg.aar.data.AARContext;
import pwcg.aar.inmission.phase2.logeval.AAREvaluator;
import pwcg.aar.inmission.phase2.logeval.AARMissionEvaluationData;
import pwcg.aar.inmission.phase3.AARInMissionResultGenerator;
import pwcg.aar.inmission.phase3.reconcile.victories.singleplayer.PlayerDeclarations;
import pwcg.campaign.Campaign;
import pwcg.core.exception.PWCGException;

public class AARCoordinatorInMission
{
    private Campaign campaign;
    private AARContext aarContext;

    public AARCoordinatorInMission (Campaign campaign, AARContext aarContext)
    {
        this.campaign = campaign;
        this.aarContext = aarContext;
    }
    
    public void coordinateInMissionAAR(Map<Integer, PlayerDeclarations> playerDeclarations) throws PWCGException
    {        
        evaluateMissionEvents();  
        generateInMissionResults(playerDeclarations);
    }

    private void evaluateMissionEvents() throws PWCGException
    {
        AAREvaluator evaluator = new AAREvaluator(campaign, aarContext);
        AARMissionEvaluationData evaluationData = evaluator.determineMissionResults();
        aarContext.setMissionEvaluationData(evaluationData);
    }

    private void generateInMissionResults(Map<Integer, PlayerDeclarations> playerDeclarations) throws PWCGException
    {
        AARInMissionResultGenerator inMissionResultGenerator = new AARInMissionResultGenerator(campaign, aarContext);
        inMissionResultGenerator.generateInMissionResults(playerDeclarations);
    }
}
