package pwcg.gui.rofmap.brief.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pwcg.core.exception.PWCGException;
import pwcg.mission.Mission;
import pwcg.mission.platoon.ITankPlatoon;

public class BriefingData
{
    private String missionTime = "08:30";
    private Map<Integer, BriefingPlatoon> briefingMissionUnits = new HashMap<>();
    private int selectedCompanyId = 0;
    private Mission mission;
    private Map<Integer, String> aiUnitsToDisplay = new HashMap<>();

    public BriefingData(Mission mission, Map<Integer, BriefingPlatoon> briefingMissionFlights)
    {
        this.mission = mission;
        this.briefingMissionUnits = briefingMissionFlights;
    }

    public BriefingPlatoon getActiveBriefingPlatoon()
    {
        return briefingMissionUnits.get(selectedCompanyId);
    }
    
    public ITankPlatoon getSelectedUnit() throws PWCGException
    {
        ITankPlatoon playerFlight = mission.getPlatoons().getPlayerUnitForCompany(selectedCompanyId);
        return playerFlight;
    }

    public List<BriefingPlatoon> getBriefingPlatoons()
    {
        return new ArrayList<>(briefingMissionUnits.values());
    }
    
    public void changeSelectedUnit(int companyId)
    {
        selectedCompanyId = companyId;
    }

    public Mission getMission()
    {
        return mission;
    }
    
    public String getMissionTime()
    {
        return missionTime;
    }

    public void setMissionTime(String selectedTime)
    {
        this.missionTime = selectedTime;
    }
    
    public void setAiFlightsToDisplay(Map<Integer, String> aiFlightsToDisplay)
    {
        this.aiUnitsToDisplay = aiFlightsToDisplay;
    }

    public void clearAiFlightsToDisplay()
    {
        aiUnitsToDisplay.clear();
    }

    public Map<Integer, String> getAiPlatoonsToDisplay()
    {
        return aiUnitsToDisplay;
    }
}
