package pwcg.mission.mcu;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.api.Side;

public enum Coalition
{
    COALITION_ALLIED(1, Side.ALLIED),
    COALITION_AXIS(2, Side.AXIS),
    COALITION_ENTENTE(3, Side.ALLIED),
    COALITION_CENTRAL(4, Side.AXIS);

    private int coalitionValue;
    private Side side;

    private Coalition(int coalitionValue, Side side)
    {
        this.coalitionValue = coalitionValue;
        this.side = side;
    }

    public int getCoalitionValue() 
    {
        return coalitionValue;
    }
    
    public Side getSide()
    {
        return side;
    }

    public static List<Coalition> getCoalitions()
    {
        List<Coalition> coalitions = new ArrayList<>();
        coalitions.add(COALITION_ALLIED);
        coalitions.add(COALITION_AXIS);
        return coalitions;
    }

    public static List<Coalition> getCoalitionsForSide(Side side)
    {
        List<Coalition> coalitions = new ArrayList<>();
        if (side == Side.ALLIED)
        {
            coalitions.add(COALITION_ALLIED);
        }
        else
        {
            coalitions.add(COALITION_AXIS);
        }
        return coalitions;
    }
}
