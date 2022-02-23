package pwcg.aar.inmission.prelim;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.aar.prelim.AARPreliminaryData;
import pwcg.aar.prelim.PwcgMissionData;
import pwcg.aar.prelim.PwcgMissionDataEvaluator;
import pwcg.aar.prelim.claims.AARClaimPanelData;
import pwcg.aar.prelim.claims.AARClaimPanelEventTabulator;
import pwcg.campaign.Campaign;
import pwcg.campaign.api.Side;
import pwcg.core.exception.PWCGException;
import pwcg.mission.data.MissionHeader;

@ExtendWith(MockitoExtension.class)
public class AARClaimPanelEventTabulatorTest
{
    @Mock private Campaign campaign;
    @Mock private PwcgMissionDataEvaluator pwcgMissionDataEvaluator;
    @Mock private AARPreliminaryData aarPreliminarytData;
    @Mock private PwcgMissionData pwcgMissionData;
    @Mock private MissionHeader missionHeader;

    @BeforeEach
    public void setupTest() throws PWCGException
    {
        Mockito.when(aarPreliminarytData.getPwcgMissionData()).thenReturn(pwcgMissionData);

        List<String> missionTanks  = new ArrayList<>();
        missionTanks.add("t34-76stz");
        missionTanks.add("t34-76stz");
        missionTanks.add("kv1-s");
        missionTanks.add("pziii-h");

        Mockito.when(pwcgMissionData.getTankTypesInMission()).thenReturn(missionTanks);
    }

    @Test
    public void germanMission () throws PWCGException
    {
        AARClaimPanelEventTabulator claimPanelEventTabulator = new AARClaimPanelEventTabulator(campaign, aarPreliminarytData, Side.AXIS);
        AARClaimPanelData claimPanelData = claimPanelEventTabulator.tabulateForAARClaimPanel();
        Assertions.assertTrue (claimPanelData.getEnemyTankTypesInMission().size() == 2);

    }

    @Test
    public void russianMission () throws PWCGException
    {
        AARClaimPanelEventTabulator claimPanelEventTabulator = new AARClaimPanelEventTabulator(campaign, aarPreliminarytData, Side.ALLIED);
        AARClaimPanelData claimPanelData = claimPanelEventTabulator.tabulateForAARClaimPanel();
        Assertions.assertTrue (claimPanelData.getEnemyTankTypesInMission().size() == 1);

    }
}
