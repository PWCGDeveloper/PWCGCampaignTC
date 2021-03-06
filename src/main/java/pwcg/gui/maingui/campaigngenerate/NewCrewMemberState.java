package pwcg.gui.maingui.campaigngenerate;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.CampaignMode;
import pwcg.core.exception.PWCGException;
import pwcg.gui.utils.PWCGStringValidator;

public class NewCrewMemberState
{
    private CrewMemberGeneratorWorkflow currentStep = CrewMemberGeneratorWorkflow.CHOOSE_PLAYER_NAME;
    private Campaign campaign;
    private NewCrewMemberGeneratorDO newCrewMemberGeneratorDO;
    private List<CrewMemberGeneratorWorkflow> stateStack = new ArrayList<>();
    private int stateIndex = 0;
    
    public enum CrewMemberGeneratorWorkflow
    {
        CHOOSE_PLAYER_NAME,
        CHOOSE_COOP_USER,
        CHOOSE_REGION,
        CHOOSE_ROLE,
        CHOOSE_RANK,
        CHOOSE_Company,
        COMPLETE
    }
    
    public NewCrewMemberState(Campaign campaign, NewCrewMemberGeneratorDO campaignGeneratorDO)
    {
        this.campaign = campaign;
        this.newCrewMemberGeneratorDO = campaignGeneratorDO;
    }

    public void buildStateStack() throws PWCGException
    {
        stateStack.add(CrewMemberGeneratorWorkflow.CHOOSE_PLAYER_NAME);

        if (campaign.getCampaignData().getCampaignMode() != CampaignMode.CAMPAIGN_MODE_SINGLE)
        {
            stateStack.add(CrewMemberGeneratorWorkflow.CHOOSE_COOP_USER);
        }

        stateStack.add(CrewMemberGeneratorWorkflow.CHOOSE_ROLE);
        stateStack.add(CrewMemberGeneratorWorkflow.CHOOSE_RANK);
        stateStack.add(CrewMemberGeneratorWorkflow.CHOOSE_Company);
        stateStack.add(CrewMemberGeneratorWorkflow.COMPLETE);
    }

    public void goToNextStep() throws PWCGException
    {
        if (stateIndex < (stateStack.size()-1))
        {
            ++stateIndex;
        }
        currentStep = stateStack.get(stateIndex);
    }
    
    public void goToPreviousStep() throws PWCGException
    {   
        if (stateIndex > 0)
        {
            --stateIndex;
        }
        currentStep = stateStack.get(stateIndex);        
    }

    public CrewMemberGeneratorWorkflow getCurrentStep()
    {
        return currentStep;
    }

    public boolean isComplete()
    {
        if ((currentStep != CrewMemberGeneratorWorkflow.COMPLETE))
        {
            return false;
        }
        else if (!PWCGStringValidator.isValidName(newCrewMemberGeneratorDO.getPlayerCrewMemberName()))
        {
            return false;
        }
        else if (!(campaign.getCampaignData().getCampaignMode() == CampaignMode.CAMPAIGN_MODE_SINGLE) &&
                !(PWCGStringValidator.isValidDescriptor(newCrewMemberGeneratorDO.getCoopUser())))
        {
            return false;
        }

        
        return true;
    }
}

