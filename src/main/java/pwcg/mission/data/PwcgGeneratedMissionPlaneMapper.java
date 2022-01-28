package pwcg.mission.data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.Side;
import pwcg.campaign.company.Company;
import pwcg.campaign.context.PWCGContext;
import pwcg.core.exception.PWCGException;

public class PwcgGeneratedMissionPlaneMapper
{
    private Map<String, String> alliedPlaneMap = new HashMap<String, String>();
    private Map<String, String> axisPlaneMap = new HashMap<String, String>();

    public void createPlaneMapforFlight(Campaign campaign, List<PwcgGeneratedMissionVehicleData> missionTanks) throws PWCGException
    {
        for (PwcgGeneratedMissionVehicleData missionTankData : missionTanks)
        {
            Side side = determinePlaneSide(campaign, missionTankData);

            if (side == Side.AXIS)
            {
                axisPlaneMap.put(missionTankData.getVehicleType(), missionTankData.getVehicleType());
            }
            else
            {
                alliedPlaneMap.put(missionTankData.getVehicleType(), missionTankData.getVehicleType());
            }

        }
    }

    private Side determinePlaneSide(Campaign campaign, PwcgGeneratedMissionVehicleData missionTank) throws PWCGException
    {
        Company company = PWCGContext.getInstance().getCompanyManager().getCompany(missionTank.getCompanyId());
        Side side = company.determineCompanyCountry(campaign.getDate()).getSide();
        return side;
    }

    public Map<String, String> getAlliedPlaneMap()
    {
        return alliedPlaneMap;
    }

    public Map<String, String> getAxisPlaneMap()
    {
        return axisPlaneMap;
    }

}
