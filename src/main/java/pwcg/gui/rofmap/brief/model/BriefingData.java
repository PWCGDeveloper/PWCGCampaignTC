package pwcg.gui.rofmap.brief.model;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pwcg.campaign.api.Side;
import pwcg.core.exception.PWCGException;
import pwcg.mission.Mission;
import pwcg.mission.platoon.ITankPlatoon;

public class BriefingData
{
    private String missionTime = "08:30";
    private Map<Integer, IBriefingPlatoon> briefingPlatoons = new HashMap<>();
    private int selectedPlayerCompanyId = 0;
    private int selectedMapEditCompanyId = 0;
    private Mission mission;
    private Image displayMapImage= null;
    private BufferedImage displayBufferedMapImage = null;

    public BriefingData(Mission mission, Map<Integer, IBriefingPlatoon> briefingPlatoons)
    {
        this.mission = mission;
        this.briefingPlatoons = briefingPlatoons;

    }

    public IBriefingPlatoon getActiveBriefingMapPlatoon()
    {
        return briefingPlatoons.get(selectedMapEditCompanyId);
    }

    public IBriefingPlatoon getActiveBriefingPlayerPlatoon()
    {
        return briefingPlatoons.get(selectedPlayerCompanyId);
    }

    public ITankPlatoon getSelectedPlayerPlatoon() throws PWCGException
    {
        ITankPlatoon platoon = mission.getPlatoons().getPlayerPlatoonForCompany(selectedPlayerCompanyId);
        return platoon;
    }

    public BriefingPlayerPlatoon getActivePlayerBriefingPlatoon() throws PWCGException
    {
        for(BriefingPlayerPlatoon briefingPlayerPlatoon : getPlayerBriefingPlatoons())
        {
            if (briefingPlayerPlatoon.getCompanyId() == selectedPlayerCompanyId)
            {
                return briefingPlayerPlatoon;
            }
        }

        throw new PWCGException("No player platoon for id " + selectedPlayerCompanyId);
    }

    public List<BriefingPlayerPlatoon> getPlayerBriefingPlatoons()
    {
        List<BriefingPlayerPlatoon> playerPlatoons = new ArrayList<>();
        for(IBriefingPlatoon briefingPlatoon : briefingPlatoons.values())
        {
            if (briefingPlatoon instanceof BriefingPlayerPlatoon)
            {
                playerPlatoons.add((BriefingPlayerPlatoon)briefingPlatoon);
            }
        }
        return playerPlatoons;
    }

    public List<IBriefingPlatoon> getBriefingPlatoonsForSide(Side side)
    {
        List<IBriefingPlatoon> platoonsForSide = new ArrayList<>();
        for(IBriefingPlatoon briefingPlatoon : briefingPlatoons.values())
        {
            if (briefingPlatoon.getSide() == side)
            {
                platoonsForSide.add(briefingPlatoon);
            }
        }
        return platoonsForSide;
    }

    public List<IBriefingPlatoon> getBriefingPlatoons()
    {
        return new ArrayList<>(briefingPlatoons.values());
    }

    public IBriefingPlatoon getBriefingPlatoon(int companyId)
    {
        return briefingPlatoons.get(companyId);
    }

    public int getSelectedMapEditPlatoon()
    {
        return selectedMapEditCompanyId;
    }

    public void setSelectedMapEditPlatoon(int selectedMapEditCompanyId)
    {
        this.selectedMapEditCompanyId = selectedMapEditCompanyId;
    }

    public void changeSelectedPlayerPlatoon(int selectedPlayerCompanyId)
    {
        this.selectedPlayerCompanyId = selectedPlayerCompanyId;
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

    public Image getDisplayMapImage()
    {
        return displayMapImage;
    }

    public void setDisplayMapImage(Image displayMapImage)
    {
        this.displayMapImage = displayMapImage;
    }

    public BufferedImage getDisplayBufferedMapImage()
    {
        return displayBufferedMapImage;
    }

    public void setDisplayBufferedMapImage(BufferedImage displayBufferedMapImage)
    {
        this.displayBufferedMapImage = displayBufferedMapImage;
    }
}
