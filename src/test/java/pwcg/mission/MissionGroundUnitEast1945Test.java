package pwcg.mission;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.FrontMapIdentifier;
import pwcg.campaign.context.PWCGContext;
import pwcg.core.exception.PWCGException;
import pwcg.mission.target.TargetType;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.CompanyTestProfile;
import pwcg.testutils.TestMissionBuilderUtility;

public class MissionGroundUnitEast1945Test
{
    public MissionGroundUnitEast1945Test() throws PWCGException
    {
        
    }
    
    @Test
    public void verifySmallerDistanceToFront () throws PWCGException
    {
        Campaign campaign = CampaignCache.makeCampaign(CompanyTestProfile.TANK_DIVISION_147_PROFILE_END);
        FrontMapIdentifier map = PWCGContext.getInstance().getCurrentMap().getMapIdentifier();
        assert(map == FrontMapIdentifier.EAST1945_MAP);
        
        MissionGenerator missionGenerator = new MissionGenerator(campaign);
        Mission mission = missionGenerator.makeTestMissionFromMissionType(
                TestMissionBuilderUtility.buildTestParticipatingHumans(campaign));

        List<Side> sides = Arrays.asList(Side.ALLIED, Side.AXIS);
        for (Side side : sides)
        {
            List<TargetType> availableGroundUnitTypes = mission.getGroundUnitBuilder().getAvailableGroundUnitTargetTypesForMissionForSide(side);
            
            List<TargetType> expectedGroundUnitTypes = Arrays.asList(TargetType.TARGET_INFANTRY, TargetType.TARGET_TRANSPORT);
            boolean allExist = validateExpectedGroundUnits(side , availableGroundUnitTypes, expectedGroundUnitTypes);
            assert(allExist);
            
            List<TargetType> unexpectedGroundUnitTypes = Arrays.asList(TargetType.TARGET_DRIFTER, TargetType.TARGET_SHIPPING, TargetType.TARGET_BALLOON);
            boolean allDoNotExist = validateUnexpectedGroundUnits(side , availableGroundUnitTypes, unexpectedGroundUnitTypes);
            assert(allDoNotExist);
        }
    }
    
    private boolean validateExpectedGroundUnits(Side side, List<TargetType> availableGroundUnitTypes, List<TargetType> expectedGroundUnitTypes)
    {
        boolean allExist = true;
        for (TargetType expectedGroundUnitType : expectedGroundUnitTypes)
        {
            boolean thisTargetExists = false;
            for (TargetType availableGroundUnitType : availableGroundUnitTypes)
            {
                if (expectedGroundUnitType == availableGroundUnitType)
                {
                    thisTargetExists = true;
                }
            }
            
            if (!thisTargetExists)
            {
                allExist = false;
                System.out.println("No target type for side " + side + " Type: " + expectedGroundUnitType);
            }
            
        }
        return allExist;
    }
    
    
    private boolean validateUnexpectedGroundUnits(Side side, List<TargetType> availableGroundUnitTypes, List<TargetType> unexpectedGroundUnitTypes)
    {
        boolean allDoNotExist = true;
        for (TargetType unexpectedGroundUnitType : unexpectedGroundUnitTypes)
        {
            boolean thisTargetExists = false;
            for (TargetType availableGroundUnitType : availableGroundUnitTypes)
            {
                if (unexpectedGroundUnitType == availableGroundUnitType)
                {
                    thisTargetExists = true;
                }
            }
            
            if (thisTargetExists)
            {
                allDoNotExist = false;
                System.out.println("Unexpected target type for side " + side + " Type: " + unexpectedGroundUnitType);
            }
            
        }
        return allDoNotExist;
    }

}
