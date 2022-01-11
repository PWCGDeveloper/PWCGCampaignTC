package pwcg.coop.model;

import pwcg.campaign.crewmember.CrewMemberStatus;

public class CoopDisplayRecord
{
    private String username = "unknown";
    private String pilorNameAndRank = "unknown";
    private String campaignName = "unknown";
    private String companyName = "unknown";
    private int crewMemberStatus = CrewMemberStatus.STATUS_ACTIVE;
    private int crewMemberSerialNumber = 0;

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getCrewMemberNameAndRank()
    {
        return pilorNameAndRank;
    }

    public void setPilorNameAndRank(String pilorNameAndRank)
    {
        this.pilorNameAndRank = pilorNameAndRank;
    }

    public String getCampaignName()
    {
        return campaignName;
    }

    public void setCampaignName(String campaignName)
    {
        this.campaignName = campaignName;
    }

    public String getCompanyName()
    {
        return companyName;
    }

    public void setCompanyName(String companyName)
    {
        this.companyName = companyName;
    }

    public int getCrewMemberStatus()
    {
        return crewMemberStatus;
    }

    public void setCrewMemberStatus(int crewMemberStatus)
    {
        this.crewMemberStatus = crewMemberStatus;
    }

    public int getCrewMemberSerialNumber()
    {
        return crewMemberSerialNumber;
    }

    public void setCrewMemberSerialNumber(int crewMemberSerialNumber)
    {
        this.crewMemberSerialNumber = crewMemberSerialNumber;
    }
}
