package pwcg.mission;

import java.util.Date;

import pwcg.campaign.api.ICountry;
import pwcg.campaign.api.Side;
import pwcg.campaign.company.CompanyRoleSet;
import pwcg.campaign.tank.PwcgRoleCategory;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;

public interface ICompanyMission
{
    Coordinate determinePosition(Date date) throws PWCGException;
    String determineDisplayName(Date date) throws PWCGException;
    String determineBaseName(Date date);
    ICountry getCountry();
    int getCompanyId();
    CompanyRoleSet getCompanyRoles();
    boolean isPlayercompany();
    Side determineSide() throws PWCGException;
    Coordinate determineCurrentPosition(Date campaignDate) throws PWCGException;
    String getDivisionName();
    PwcgRoleCategory getCompanyPrimaryRoleForMission(Date date) throws PWCGException;
    void setCompanyPrimaryRoleForMission(PwcgRoleCategory role) throws PWCGException;
}
