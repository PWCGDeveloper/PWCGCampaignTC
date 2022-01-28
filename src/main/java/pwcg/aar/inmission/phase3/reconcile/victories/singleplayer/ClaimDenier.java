package pwcg.aar.inmission.phase3.reconcile.victories.singleplayer;

import pwcg.aar.ui.events.model.ClaimDeniedEvent;
import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.core.exception.PWCGException;
import pwcg.mission.ground.vehicle.VehicleDefinition;

public class ClaimDenier
{
    private Campaign campaign;

    public ClaimDenier(Campaign campaign)
    {
        this.campaign = campaign;
    }

    public ClaimDeniedEvent determineClaimDenied(Integer playerSerialNumber, PlayerVictoryDeclaration declaration) throws PWCGException
    {
        if (!declaration.isConfirmed())
        {
            return createPlaneDenied(playerSerialNumber, declaration);
        }

        return null;
    }

    private ClaimDeniedEvent createPlaneDenied(Integer playerSerialNumber, PlayerVictoryDeclaration declaration) throws PWCGException
    {
        String tankDesc = getPlaneDescription(declaration);
        CrewMember player = campaign.getPersonnelManager().getAnyCampaignMember(playerSerialNumber);

        boolean isNewsworthy = false;
        ClaimDeniedEvent claimDenied = new ClaimDeniedEvent(campaign, tankDesc, player.getCompanyId(), player.getSerialNumber(), campaign.getDate(),
                isNewsworthy);

        return claimDenied;
    }

    private String getPlaneDescription(PlayerVictoryDeclaration playerDeclaration) throws PWCGException
    {
        String tankDesc = "Unknown";
        VehicleDefinition vehicleDefinition = PWCGContext.getInstance().getVehicleDefinitionManager().getVehicleDefinition(playerDeclaration.getTankType());
        if (vehicleDefinition != null)
        {
            tankDesc = vehicleDefinition.getDisplayName();
        }
        return tankDesc;
    }
}
