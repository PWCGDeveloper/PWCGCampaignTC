package pwcg.campaign.tank;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.resupply.depot.EquipmentWeightCalculator;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;

@ExtendWith(MockitoExtension.class)
public class EquipmentWeightCalculatorTest
{
    @Mock Campaign campaign;
    
    public EquipmentWeightCalculatorTest() throws PWCGException
    {
              
    }

    @Test
    public void testEquipCompany() throws PWCGException
    {
        TankTypeInformation tiger = PWCGContext.getInstance().getPlayerTankTypeFactory().getTankById(TankAttributeMapping.TIGER_I.getTankType());
        TankTypeInformation pziv = PWCGContext.getInstance().getPlayerTankTypeFactory().getTankById(TankAttributeMapping.PZKW_IV_G.getTankType());
        List<TankTypeInformation> planeTypes = new ArrayList<>();
        planeTypes.add(tiger);
        planeTypes.add(pziv);
        
        Mockito.when(campaign.getDate()).thenReturn(DateUtils.getDateYYYYMMDD("19420801"));
        EquipmentWeightCalculator equipmentWeightCalculator = new EquipmentWeightCalculator(campaign.getDate());
        equipmentWeightCalculator.determineTankWeightsForTanks(planeTypes);
        
        Map<String, Integer> weightedPlaneOdds = equipmentWeightCalculator.getWeightedPlaneOdds();
        assert(weightedPlaneOdds.get(TankAttributeMapping.TIGER_I.getTankType()) == 31);
        assert(weightedPlaneOdds.get(TankAttributeMapping.PZKW_IV_G.getTankType()) == 100);
        
        int tigerCount = 0;
        int pzivCount = 0;
        for (int i = 0; i < 10000; ++i)
        {
            String planeTypeName = equipmentWeightCalculator.getTankTypeFromWeight();
            if (planeTypeName.equals(TankAttributeMapping.TIGER_I.getTankType()))
            {
                ++tigerCount;
            }
            else if (planeTypeName.equals(TankAttributeMapping.PZKW_IV_G.getTankType()))
            {
                ++pzivCount;
            }
            else
            {
                throw new PWCGException("Unexpected plane from plane calculator: " + planeTypeName);
            }
        }
        
        assert(pzivCount > tigerCount);
    }
}
