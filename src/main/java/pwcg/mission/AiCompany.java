package pwcg.mission;

import java.util.Date;

import pwcg.campaign.api.ICountry;
import pwcg.campaign.api.Side;
import pwcg.campaign.company.CompanyRoleSet;
import pwcg.campaign.tank.PwcgRoleCategory;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;

public class AiCompany implements ICompanyMission
{

    private String name;
    private String baseName;
    private Coordinate position;
    private ICountry country;
    private int companyId;
    private PwcgRoleCategory role = PwcgRoleCategory.MAIN_BATTLE_TANK;

    public AiCompany(String name, String baseName, Coordinate position, ICountry country, int companyId, PwcgRoleCategory role)
    {
        this.name = name;
        this.baseName = baseName;
        this.position = position;
        this.country = country;
        this.companyId = companyId;
        this.role = role;
    }

    @Override
    public Coordinate determinePosition(Date date) throws PWCGException
    {
        return position;
    }

    @Override
    public String determineDisplayName(Date date) throws PWCGException
    {
        return this.name;
    }

    @Override
    public String determineBaseName(Date date)
    {
        return baseName;
    }

    @Override
    public ICountry getCountry()
    {
        return country;
    }

    @Override
    public int getCompanyId()
    {
        return companyId;
    }

    @Override
    public CompanyRoleSet getCompanyRoles()
    {
        return null;
    }

    @Override
    public boolean isPlayercompany()
    {
        return false;
    }

    @Override
    public Side determineSide() throws PWCGException
    {
        return country.getSide();
    }

    @Override
    public Coordinate determineCurrentPosition(Date campaignDate) throws PWCGException
    {
        return position;
    }

    @Override
    public String getDivisionName()
    {
        return name;
    }

    @Override
    public PwcgRoleCategory getCompanyPrimaryRoleForMission(Date date) throws PWCGException
    {
        return role;
    }

    @Override
    public void setCompanyPrimaryRoleForMission(PwcgRoleCategory role) throws PWCGException
    {
        this.role = role;
    }
}
