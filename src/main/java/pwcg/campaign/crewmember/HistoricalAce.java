package pwcg.campaign.crewmember;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pwcg.campaign.ArmedService;
import pwcg.campaign.api.IArmedServiceManager;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.api.IRankHelper;
import pwcg.campaign.company.Company;
import pwcg.campaign.context.Country;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.factory.ArmedServiceFactory;
import pwcg.campaign.factory.CountryFactory;
import pwcg.campaign.factory.RankFactory;
import pwcg.campaign.medals.Medal;
import pwcg.campaign.skin.Skin;
import pwcg.core.constants.AiSkillLevel;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.RandomNumberGenerator;

public class HistoricalAce extends CrewMember
{
    protected List<HistoricalAceCompany> companys = new ArrayList<HistoricalAceCompany>();
    protected List<HistoricalAceRank> ranks = new ArrayList<HistoricalAceRank>();

    public HistoricalAce()
    {
        super();
        aiSkillLevel = AiSkillLevel.ACE;
    }

    public TankAce getAtDate(Date date) throws PWCGException
    {
        TankAce aceNow = new TankAce();

        determineAceName(aceNow, date);
        determineAcePicture(aceNow);
        determineAceVictories(date, aceNow);
        determineAceMissionsCompleted(aceNow);
        determineAceMedals(date, aceNow);
        Company referenceCompany = determineHistoricalAceCompany(date, aceNow);
        determineAceCountry(date, aceNow, referenceCompany);
        determineHistoricalAceRank(date, aceNow, referenceCompany);
        determineAceSkins(aceNow);
        
        return aceNow;
    }

    private void determineAceName(TankAce aceNow, Date date) throws PWCGException
    {
        aceNow.setName(name);
        aceNow.setSerialNumber(serialNumber);
        aceNow.setCrewMemberActiveStatus(activeStatus, date, null);
    }

    private void determineAcePicture(TankAce aceNow)
    {
        String picName = name + ".jpg";
        aceNow.setPicName(picName);
    }

    private void determineAceVictories(Date date, TankAce aceNow)
    {
        for (int i = 0; i < airVictories.size(); ++i)
        {
            Victory aceVictory = airVictories.get(i);

            if (date.after(aceVictory.getDate()))
            {
                aceNow.airVictories.add(aceVictory);
            }
        }
    }

    private void determineAceMissionsCompleted(TankAce aceNow) throws PWCGException
    {
        int missionsCompleted = aceNow.getCrewMemberVictories().getAirToAirVictoryCount() * 3;
        missionsCompleted += RandomNumberGenerator.getRandom(20);
        aceNow.setBattlesFought(missionsCompleted);
    }

    private void determineAceMedals(Date date, TankAce aceNow)
    {
        for (int i = 0; i < medals.size(); ++i)
        {
            Medal medal = medals.get(i);
            if (date.after(medal.getMedalDate()))
            {
                aceNow.medals.add(medal);
            }
        }
    }

    private Company determineHistoricalAceCompany(Date date, TankAce aceNow) throws PWCGException
    {
        aceNow.setCompanyId(-1);

        HistoricalAceCompany lastHistoricalAceCompany = null;
        for (int i = 0; i < companys.size(); ++i)
        {
            HistoricalAceCompany aceCompany = companys.get(i);
            if (date.after(aceCompany.date))
            {
                if (aceCompany.company == CrewMemberStatus.STATUS_ON_LEAVE)
                {
                    aceNow.setCrewMemberActiveStatus(CrewMemberStatus.STATUS_ON_LEAVE, null, null);
                }
                else if (aceCompany.company == CrewMemberStatus.STATUS_KIA)
                {
                    aceNow.setCrewMemberActiveStatus(CrewMemberStatus.STATUS_KIA, date, null);
                }
                else
                {
                    aceNow.setCrewMemberActiveStatus(CrewMemberStatus.STATUS_ACTIVE, null, null);
                    lastHistoricalAceCompany = aceCompany;
                    aceNow.setCompanyId(lastHistoricalAceCompany.company);
                }
            }
        }

        Company company = null;
        if (lastHistoricalAceCompany != null)
        {
            company = PWCGContext.getInstance().getCompanyManager().getCompany(lastHistoricalAceCompany.company);
        }

        return company;
    }

    private void determineHistoricalAceRank(Date date, TankAce aceNow, Company referenceCompany) throws PWCGException
    {
        if (ranks.size() > 5)
        {
            throw new PWCGException("malformed entry for ace " + name);
        }
        
        
        ArmedService service = null;
        if (referenceCompany != null)
        {
            service = referenceCompany.determineServiceForCompany(date);
        }
        else
        {
            ICountry country = CountryFactory.makeCountryByCountry(aceNow.getCountry());
            IArmedServiceManager armedServiceManager = ArmedServiceFactory.createServiceManager();
            service = armedServiceManager.getPrimaryServiceForNation(country.getCountry(), date);
        }

        if (service != null)
        {
            IRankHelper rankLists = RankFactory.createRankHelper();
            ArrayList<String> rankStrings = rankLists.getRanksByService(service);

            for (int i = 0; i < ranks.size(); ++i)
            {
                HistoricalAceRank aceRank = ranks.get(i);
                if (!date.before(aceRank.date))
                {
                    aceNow.setRank(rankStrings.get(aceRank.rank));
                }
            }
        }
    }

    private void determineAceCountry(Date date, TankAce aceNow, Company referenceCompany) throws PWCGException
    {
        if (referenceCompany != null)
        {
            aceNow.setCountry(referenceCompany.determineCompanyCountry(date).getCountry());
        }
        else
        {
            aceNow.setCountry(country);
        }
    }

    private void determineAceSkins(TankAce aceNow)
    {
        List<Skin> skinsForAce = new ArrayList<Skin>();
        for (Skin skin : skins)
        {
            skinsForAce.add(skin);
        }
        aceNow.setSkins(skinsForAce);
    }

    public int getStatus(Date date)
    {
        for (HistoricalAceCompany company : getCompanys())
        {
            if (company.company == CrewMemberStatus.STATUS_KIA)
            {
                if (date.after(company.date))
                {
                    return CrewMemberStatus.STATUS_KIA;
                }
            }
        }

        return activeStatus;
    }

    public List<Victory> getVictories(Date date)
    {
        List<Victory> victoriesAtDate = new ArrayList<Victory>();
        for (Victory victory : airVictories)
        {
            if (!victory.getDate().after(date))
            {
                victoriesAtDate.add(victory);
            }
        }
        return victoriesAtDate;
    }

    public List<Medal> getMedals(Date date)
    {
        List<Medal> medalsAtDate = new ArrayList<Medal>();
        for (Medal medal : medals)
        {
            if (!medal.getMedalDate().after(date))
            {
                medalsAtDate.add(medal);
            }
        }
        return medalsAtDate;
    }

    public int getCurrentCompany(Date date, boolean useOnLeave)
    {
        int currentCompany = -1;

        for (int i = 0; i < companys.size(); ++i)
        {
            HistoricalAceCompany company = companys.get(i);

            if (!company.date.after(date))
            {
                if (useOnLeave == true)
                {
                    currentCompany = company.company;
                }
                else
                {
                    if (company.company != CrewMemberStatus.STATUS_ON_LEAVE)
                    {
                        currentCompany = company.company;
                    }
                }
            }
        }

        return currentCompany;
    }

    public String getCurrentRank(Date date) throws PWCGException
    {
        HistoricalAceRank currentRank = null;

        for (HistoricalAceRank rank : ranks)
        {
            if (!rank.date.after(date))
            {
                currentRank = rank;
            }
        }

        String rankName = "";

        int squadId = getCurrentCompany(date, false);
        Company company = PWCGContext.getInstance().getCompanyManager().getCompany(squadId);

        if (company != null && currentRank != null)
        {
            IRankHelper rankObj = RankFactory.createRankHelper();
            rankName = rankObj.getRankByService(currentRank.rank, company.determineServiceForCompany(date));
        }

        return rankName;
    }

    public int getAceNumVictories(Date date)
    {
        int numVictories = 0;

        for (int i = 0; i < airVictories.size(); ++i)
        {
            Victory victory = airVictories.get(i);

            if (victory.getDate().before(date))
            {
                ++numVictories;
            }
        }

        return numVictories;
    }

    public void addHistoricalAceCompany(int company, Date date)
    {
        HistoricalAceCompany as = new HistoricalAceCompany();
        as.company = company;
        as.date = date;
        companys.add(as);
    }

    public List<HistoricalAceCompany> getCompanys()
    {
        return companys;
    }

    public void setCompanys(ArrayList<HistoricalAceCompany> companys)
    {
        this.companys = companys;
    }

    public List<HistoricalAceRank> getRanks()
    {
        return ranks;
    }

    public void setRanks(ArrayList<HistoricalAceRank> ranks)
    {
        this.ranks = ranks;
    }

    public void setCountry(Country country)
    {
        this.country = country;
    }

    public Country getCountry()
    {
        return country;
    }

    public void setCompanys(List<HistoricalAceCompany> companys)
    {
        this.companys = companys;
    }

    public void setRanks(List<HistoricalAceRank> ranks)
    {
        this.ranks = ranks;
    }
}
