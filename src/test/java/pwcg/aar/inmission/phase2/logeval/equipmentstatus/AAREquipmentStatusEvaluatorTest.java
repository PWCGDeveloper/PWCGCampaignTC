package pwcg.aar.inmission.phase2.logeval.equipmentstatus;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import pwcg.aar.inmission.phase2.logeval.AARVehicleBuilder;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogTank;
import pwcg.campaign.Campaign;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.context.Country;
import pwcg.campaign.factory.CountryFactory;
import pwcg.campaign.tank.TankStatus;
import pwcg.core.exception.PWCGException;
import pwcg.core.logfiles.LogEventData;
import pwcg.core.logfiles.event.AType3;
import pwcg.core.utils.DateUtils;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class AAREquipmentStatusEvaluatorTest
{
    @Mock private LogEventData logEventData;
    @Mock private AARVehicleBuilder aarVehicleBuilder;
    @Mock private Campaign campaign;
    
    @BeforeEach
    public void setupTest() throws PWCGException
    {
        
         
        Mockito.when(campaign.getDate()).thenReturn(DateUtils.getDateYYYYMMDD("19420601"));

        Map <String, LogTank> planeAiEntities = makePlaneEntities();
        Mockito.when(aarVehicleBuilder.getLogTanks()).thenReturn(planeAiEntities);
    }

    @Test
    public void testPlaneDestroyed () throws PWCGException
    {
        AType3 atype3 = new AType3("T:54877 AType:3 AID:-1 TID:35839 POS(112150.266,93.277,111696.758)");
        Mockito.when(logEventData.getDestroyedEvent(ArgumentMatchers.<String>any())).thenReturn(atype3);

        runTestWithStatusCheck(TankStatus.STATUS_DESTROYED);
    }

    @Test
    public void testNotPlaneDestroyed () throws PWCGException
    {
        Mockito.when(logEventData.getDestroyedEvent(ArgumentMatchers.<String>any())).thenReturn(null);
        runTestWithStatusCheck(TankStatus.STATUS_DEPLOYED);
    }

    private void runTestWithStatusCheck(int expectedStatus) throws PWCGException
    {
        AAREquipmentStatusEvaluator.determineFateOfPlanesInMission(aarVehicleBuilder, logEventData);        
        for (LogTank resultPlaneAfter : aarVehicleBuilder.getLogTanks().values())
        {
            Assertions.assertTrue (resultPlaneAfter.getTankStatus() == expectedStatus);
        }
    }

    private Map <String, LogTank> makePlaneEntities() throws PWCGException
    {
        ICountry country = CountryFactory.makeCountryByCountry(Country.RUSSIA);

        LogTank resultPlane = new LogTank(1);
        resultPlane.setCountry(country);
        resultPlane.setCompanyId(10131132);
        resultPlane.setTankStatus(TankStatus.STATUS_DEPLOYED);

        Map <String, LogTank> planeAiEntities = new HashMap <>();
        planeAiEntities.put("11111", resultPlane);        
        return planeAiEntities;
    }
}
