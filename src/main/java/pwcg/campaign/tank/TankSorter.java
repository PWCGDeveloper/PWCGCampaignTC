package pwcg.campaign.tank;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeMap;

import pwcg.core.exception.PWCGException;

public class TankSorter
{

    public static List<TankTypeInformation> sortTanksByGoodness(List<TankTypeInformation> tanks) throws PWCGException
    {
        List<TankTypeInformation> tanksByGoodness = new ArrayList<>();
        TreeMap<Integer, TankTypeInformation> tanksByGoodnessMap = new TreeMap<>();
        
        for (TankTypeInformation tank : tanks)
        {
            int tankGoodnessKey = tank.getGoodness() * 1000;
            while (tanksByGoodnessMap.containsKey(tankGoodnessKey))
            {
                ++tankGoodnessKey;
            }
            tanksByGoodnessMap.put(tankGoodnessKey, tank);
        }
        
        tanksByGoodness.addAll(tanksByGoodnessMap.values());
        Collections.reverse(tanksByGoodness);

        return tanksByGoodness;
    }

    public static List<EquippedTank> sortEquippedTanksByGoodness(List<EquippedTank> tanks) throws PWCGException
    {
        List<EquippedTank> tanksByGoodness = new ArrayList<>();
        TreeMap<String, EquippedTank> tanksByGoodnessMap = new TreeMap<>();
        
        for (EquippedTank tank : tanks)
        {
            tanksByGoodnessMap.put(formKey(tank), tank);
        }
        
        tanksByGoodness.addAll(tanksByGoodnessMap.values());
        
        return tanksByGoodness;
    }
    
    private static String formKey(EquippedTank tank)
    {
        String key = "" + (10000 - tank.getGoodness()) + tank.getType() + tank.getSerialNumber();
        return key;
    }

}
