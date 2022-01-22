package pwcg.campaign.company;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.CompanyHistory;
import pwcg.campaign.CompanyHistoryEntry;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.api.IRankHelper;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.Country;
import pwcg.campaign.context.FrontLinePoint;
import pwcg.campaign.context.FrontMapIdentifier;
import pwcg.campaign.context.MapForAirfieldFinder;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGMap;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.crewmember.CrewMembers;
import pwcg.campaign.crewmember.TankAce;
import pwcg.campaign.factory.ArmedServiceFactory;
import pwcg.campaign.factory.CountryFactory;
import pwcg.campaign.factory.RankFactory;
import pwcg.campaign.group.airfield.Airfield;
import pwcg.campaign.personnel.CompanyPersonnel;
import pwcg.campaign.personnel.CrewMemberFilter;
import pwcg.campaign.skin.Skin;
import pwcg.campaign.tank.CompanyTankAssignment;
import pwcg.campaign.tank.EquippedTank;
import pwcg.campaign.tank.PwcgRole;
import pwcg.campaign.tank.PwcgRoleCategory;
import pwcg.campaign.tank.TankArchType;
import pwcg.campaign.tank.TankTypeInformation;
import pwcg.core.constants.Callsign;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.DateUtils;
import pwcg.core.utils.MathUtils;
import pwcg.mission.ICompanyMission;
import pwcg.product.bos.config.TCProductSpecificConfiguration;

public class Company implements ICompanyMission 
{
    public static final int HOME_ASSIGNMENT = -2;
    public static final int REPLACEMENT = -1;
    public static final int Company_ID_ANY = -1;
    public static final int COMPANY_STAFF_SIZE = 16;
    public static final int COMPANY_EQUIPMENT_SIZE = 16;
    public static final int MIN_REEQUIPMENT_SIZE = 10;
    public static final int REPLACEMENTS_TANKS_PER_COMPANY = 3;
    public static final int DEPOT = -1;
    public static final int AI = 0;
    private Country country = Country.NEUTRAL;
	private int companyId = 0;
    private String name = "";
    private String fileName = "";
	private int skill = 50;
	private String unitIdCode;
	private String subUnitIdCode;
	private List<CompanyTankAssignment> tankAssignments = new ArrayList<>();
    private Map<Date, String> bases = new TreeMap<>();
	private List<Skin> skins = new ArrayList<Skin>();
	private CompanyHistory companyHistory;
	private int serviceId;
    private CompanyRoleSet companyRoles = new CompanyRoleSet();
    private NightMissionSet nightMissionOdds = new NightMissionSet();
	private List<CompanyConversionPeriod> conversionPeriods = new ArrayList<>();
    private Map<Date, Callsign> callsigns = new TreeMap<>();
	
	public static boolean isPlayerCompany (Campaign campaign, int companyId)
	{
	    CompanyPersonnel companyPersonnel = campaign.getPersonnelManager().getCompanyPersonnel(companyId);
        if (companyPersonnel != null && companyPersonnel.isPlayerCompany())
        {
            return true;
        }
        
        return false;
	}

	public List<TankTypeInformation> determineCurrentTankList(Date now) throws PWCGException
	{
		TreeMap<String, TankTypeInformation> currentAircraftByGoodness = new TreeMap<>();
		
		for (CompanyTankAssignment tankAssignment : tankAssignments)
		{
			Date introduction = tankAssignment.getCompanyIntroduction();
			Date withdrawal = tankAssignment.getCompanyWithdrawal();
			
			if (introduction.before(now) || introduction.equals((now)))
			{
				if (withdrawal.after(now) || withdrawal.equals((now)))
				{
				    List<TankTypeInformation> tankTypesForArchType = PWCGContext.getInstance().getPlayerTankTypeFactory().createActiveTankTypesForArchType(tankAssignment.getArchType(), now);
				    for (TankTypeInformation tankType : tankTypesForArchType)
				    {
			            if (tankType.isPlayer())
			            {
			                TankTypeInformation tank = PWCGContext.getInstance().getPlayerTankTypeFactory().createTankTypeByAnyName(tankType.getType());
			                currentAircraftByGoodness.put("" + tank.getGoodness() + tankType.getType(), tank);
			            }
				    }
				}
			}			
		}
		
		List<TankTypeInformation> currentAircraftByQuality = new ArrayList<TankTypeInformation>();
		currentAircraftByQuality.addAll(currentAircraftByGoodness.values());
		
		return currentAircraftByQuality;
	}

    public List<TankArchType> determineCurrentTankArchTypes(Date now) throws PWCGException
    {
        List<TankArchType> currentTankArchTypes = new ArrayList<>();
        
        for (CompanyTankAssignment tankAssignment : tankAssignments)
        {
            Date introduction = tankAssignment.getCompanyIntroduction();
            Date withdrawal = tankAssignment.getCompanyWithdrawal();
            
            if (introduction.before(now) || introduction.equals((now)))
            {
                if (withdrawal.after(now) || withdrawal.equals((now)))
                {
                    TankArchType tankArchType = PWCGContext.getInstance().getPlayerTankTypeFactory().getTankArchType(tankAssignment.getArchType());
                    currentTankArchTypes.add(tankArchType);
                }
            }           
        }

        return currentTankArchTypes;
    }


    public boolean isPlaneInActiveCompanyArchTypes(Date date, EquippedTank tank) throws PWCGException
    {
        boolean isActiveArchType = false;
        for (TankArchType archType : determineCurrentTankArchTypes(date))
        {
            if (tank.getArchType().equals(archType.getTankArchTypeName()))
            {
                isActiveArchType = true;
                break;
            }
        }
        return isActiveArchType;
    }

	public String determineCurrentAirfieldName(Date campaignDate)
	{
        String currentAirFieldName = null;
        
        for (Date airfieldStartDate : bases.keySet())
        {
            if (!airfieldStartDate.after(campaignDate))
            {
                currentAirFieldName = bases.get(airfieldStartDate);
            }
            else
            {
                break;
            }
        }
        
        return currentAirFieldName;
	}

    public void assignAirfield(Date assignmentDate, String airfield) throws PWCGException 
    {
        bases.put(assignmentDate, airfield);
        TreeMap<Date, String> airfieldModified = new TreeMap<>();
        for (Date fieldDate : bases.keySet())
        {
            airfieldModified.put(fieldDate, (bases.get(fieldDate)));
        }
        bases = airfieldModified;
    }

    public Airfield determineCurrentAirfieldAnyMap(Date campaignDate) throws PWCGException 
    {
        Airfield field = null;
        
        String airfieldName = determineCurrentAirfieldName(campaignDate);
        if (airfieldName != null)
        {
            field =  PWCGContext.getInstance().getAirfieldAllMaps(airfieldName);
        }
        
        return field;
    }

    public Airfield determineCurrentAirfieldCurrentMap(Date campaignDate)
    {
        Airfield field = null;
        
        String airfieldName = determineCurrentAirfieldName(campaignDate);
        if (airfieldName != null)
        {
            field =  PWCGContext.getInstance().getCurrentMap().getAirfieldManager().getAirfield(airfieldName);
        }
        
        return field;
    }

    public Coordinate determineCurrentPosition(Date campaignDate) throws PWCGException 
    {
        Airfield field = determineCurrentAirfieldAnyMap(campaignDate);
        if (field != null)
        {
            return field.getPosition().copy();
        }
        return null;
    }

    public Date determineActivetDate() throws PWCGException 
    {
        Date firstPlane = determineFirstAircraftDate();
        Date firstAirfield = determineFirstAirfieldDate();

        Date earliest = firstPlane;
        if (firstPlane.before(firstAirfield))
        {
            earliest = firstAirfield;
        }
        
        if (earliest.before(DateUtils.getBeginningOfGame()))
        {
            earliest = DateUtils.getBeginningOfGame();
        }
        
        return earliest;
    }

    public Date determineFirstAircraftDate() throws PWCGException 
    {
        Date firstPlaneDate = DateUtils.getEndOfWar();
        for (CompanyTankAssignment tankAssignment : tankAssignments)
        {
            if (tankAssignment.getCompanyIntroduction().before(firstPlaneDate))
            {
                firstPlaneDate = tankAssignment.getCompanyIntroduction();
            }
        }
        
        return firstPlaneDate;
    }

    public Date determineFirstAirfieldDate() throws PWCGException 
    {
        for (Date airfieldAssignmentDate : bases.keySet())
        {
            return airfieldAssignmentDate;
        }
        
        return null;
    }

	public boolean isCommandedByAce(List<TankAce> aces, Date date) throws PWCGException
	{
		boolean commanded = false;
		for (TankAce ace : aces)
		{
			IRankHelper rankObj = RankFactory.createRankHelper();			
			int rankPos = rankObj.getRankPosByService(ace.getRank(), determineServiceForCompany(date));
			if (rankPos == 0)
			{
				commanded = true;
				break;
			}
		}
		
		return commanded;
	}
	
	public String determineDisplayName (Date date) throws PWCGException 
	{
		String displayName = name;
        CompanyHistoryEntry companyHistoryEntry = getCompanyHistoryEntryForDate(date);
        if (companyHistoryEntry != null)
		{
            displayName = companyHistoryEntry.getSquadName();
		}
		
		return displayName;
	}
	
	private CompanyHistoryEntry getCompanyHistoryEntryForDate(Date date) throws PWCGException
	{
        if (companyHistory != null)
        {
            CompanyHistoryEntry companyHistoryEntry = companyHistory.getSquadHistoryEntry(date);
            if (companyHistoryEntry != null)
            {
                return companyHistoryEntry;
            }
        }
        
        return null;
	}

	public String determineCompanyDescription(Date date) throws PWCGException 
	{
		String companyDescription = "";
		
		companyDescription += "\nCompany: " + determineDisplayName(date) + "\n\n";
		
        String status = determineSkillDescription();
        if (status != null && status.length() > 0)
        {
            companyDescription += "Status: " + status + "\n\n";
        }
        
        Callsign callsign = determineCurrentCallsign(date);
        if (callsign != Callsign.NONE)
        {
            companyDescription += "Callsign: " + callsign + "\n\n";
        }

		companyDescription += "Stationed at: ";
		String fieldName = determineCurrentAirfieldName(date);
		companyDescription += fieldName + "\n\n";
		
		List<TankTypeInformation> tanks = determineCurrentTankList(date);
		companyDescription += "Operating the:\n";
		for (TankTypeInformation tank : tanks)
		{
			companyDescription += "    " + tank.getDisplayName() + "\n";
		}

		Campaign campaign =     PWCGContext.getInstance().getCampaign();
		List<TankAce> aces =  PWCGContext.getInstance().getAceManager().
		                getActiveAcesForCompany(campaign.getPersonnelManager().getCampaignAces(), campaign.getDate(), getCompanyId());

		companyDescription += "\nAces on staff:\n";
		for (TankAce ace : aces)
		{
			companyDescription += "    " + ace.getNameAndRank() + "\n";
		}

		return companyDescription;
	}

	public List<Date> determineDatesCompanyAtField(Airfield field)
	{
		List<Date> datesCompanyAtField = new ArrayList<Date>();
		
		for (String airfieldName : bases.values())
		{
			if (airfieldName.equals(field.getName()))
			{
				datesCompanyAtField.add(field.getStartDate());
			}
		}
		
		return datesCompanyAtField;
	}

    public ICountry determineEnemyCountry(Date date) throws PWCGException
    {
        List<Company> companys = null;
        
        ICountry companyCountry = CountryFactory.makeCountryByCountry(country);
        Side enemySide = companyCountry.getSideNoNeutral().getOppositeSide();
        Airfield field = determineCurrentAirfieldCurrentMap(date);
        companys =  PWCGContext.getInstance().getCompanyManager().getActiveCompaniesBySideAndProximity(enemySide, date, field.getPosition(), 30000);

        // Use an enemy company as a reference country.
        // If no enemy company use the enemy map reference nation
        ICountry enemyCountry = CountryFactory.makeNeutralCountry();
        if (companys.size() == 0)
        {
            enemyCountry = CountryFactory.makeMapReferenceCountry(enemySide);
        }
        else
        {
            enemyCountry = companys.get(0).determineCompanyCountry(date);
        }
        
        
        return enemyCountry;
    }

    public Side determineEnemySide() throws PWCGException
    {
        ICountry companyCountry = CountryFactory.makeCountryByCountry(country);
        Side enemySide = companyCountry.getSideNoNeutral().getOppositeSide();
        return enemySide;
    }

    public Side determineSide() throws PWCGException
    {
        ICountry companyCountry = CountryFactory.makeCountryByCountry(country);
        return companyCountry.getSideNoNeutral();
    }

	public ArmedService determineServiceForCompany(Date date) throws PWCGException 
	{
	    ArmedService service = ArmedServiceFactory.createServiceManager().getArmedServiceById(serviceId);

		if (date != null)
		{
	        CompanyHistoryEntry companyHistoryEntry = getCompanyHistoryEntryForDate(date);
	        if (companyHistoryEntry != null)
	        {
                String serviceName = companyHistoryEntry.getArmedServiceName();
                service = ArmedServiceFactory.createServiceManager().getArmedServiceByName(serviceName);
	        }
		}
		
		return service;
	}

	public boolean isHomeDefense(Date date) throws PWCGException
	{
		boolean isHomeDefense = false;
		String name = this.determineDisplayName(date);
		if (name.contains("(HD)") || name.contains("Kest"))
		{
		    isHomeDefense = true;
		}

		return isHomeDefense;
	}

	public boolean isInConversionPeriod(Date date) throws PWCGException
    {
        for (CompanyConversionPeriod conversionPeriod: conversionPeriods)
        {
            if (conversionPeriod.isConversionPeriodActive(date))
            {
                return true;
            }
        }
        
        return false;
    }

    public void setConversionPeriods(List<CompanyConversionPeriod> conversionPeriods)
    {
        this.conversionPeriods = conversionPeriods;
    }

    public TankTypeInformation determineBestPlane(Date date) throws PWCGException
    {
        TankTypeInformation bestPlane = null;
        List<TankTypeInformation> tanks = this.determineCurrentTankList(date);
        for (TankTypeInformation tank : tanks)
        {
            if (bestPlane == null)
            {
                bestPlane = tank;
            }
            else
            {
                if (tank.getGoodness() > bestPlane.getGoodness())
                {
                    bestPlane = tank;
                }
            }
        }
        
        return bestPlane;
    }
    
    public TankTypeInformation determineEarliestTank() throws PWCGException
    {
        TreeMap<Date, TankTypeInformation> tankTypesTypeByIntroduction = new TreeMap<>();
        for (CompanyTankAssignment tankAssignment : tankAssignments)
        {
            List<TankTypeInformation> tankTypesForArchType = PWCGContext.getInstance().getPlayerTankTypeFactory().createTanksByIntroduction(tankAssignment.getArchType());
            for (TankTypeInformation tankType : tankTypesForArchType)
            {
                tankTypesTypeByIntroduction.put(tankType.getIntroduction(), tankType);
            }
        }
 
        List<TankTypeInformation> tanks = new ArrayList<>(tankTypesTypeByIntroduction.values());
        return tanks.get(0);
    }

    public String determineCompanyInfo(Date campaignDate) throws PWCGException 
    {
        StringBuffer companyInfo = new StringBuffer("");
        
        companyInfo.append(determineDisplayName(campaignDate) + "\n");
        
        String status = determineSkillDescription();
        if (status != null && status.length() > 0)
        {
            companyInfo.append("Status: " + status + "\n");
        }
        
        companyInfo.append(DateUtils.getDateString(campaignDate) + "\n");
        List <TankTypeInformation> tanks = determineCurrentTankList(campaignDate);
        for (TankTypeInformation tank : tanks)
        {
            companyInfo.append(tank.getDisplayName() + "   ");
        }
        companyInfo.append("\n");

        companyInfo.append("\n");
        companyInfo.append("\n");

        return companyInfo.toString();
    }

    public PWCGMap getMapForBase(Date campaignDate)
    {
        String airfieldName = determineCurrentAirfieldName(campaignDate);
    	List<FrontMapIdentifier> airfieldMapIdentifiers = MapForAirfieldFinder.getMapForAirfield(airfieldName);
    	PWCGMap map = PWCGContext.getInstance().getMapByMapId(airfieldMapIdentifiers.get(0));
        return map;
    }

    public boolean isStartsCloseToFront(Date date) throws PWCGException
    {
        Side enemySide = determineEnemyCountry(date).getSide();
        Coordinate companyPosition = determineCurrentPosition(date);
        FrontLinePoint closestFrontPosition = PWCGContext.getInstance().getCurrentMap().getFrontLinesForMap(date).findClosestFrontPositionForSide(companyPosition, enemySide);
        double distanceToFront = MathUtils.calcDist(companyPosition, closestFrontPosition.getPosition());
        
        TCProductSpecificConfiguration productSpecificConfiguration = new TCProductSpecificConfiguration();
        int closeToFrontDistance = productSpecificConfiguration.getCloseToFrontDistance();
        if (distanceToFront <= closeToFrontDistance)
        {
            return true;
        }
        
        return false;
    }

    public String determineSkillDescription()
    {
        if (skill >= 90)
        {
            return "Elite";
        }
        else if (skill >= 70)
        {
            return "Veteran";
        }
        else if (skill >= 50)
        {
            return "Competent";
        }
        else
        {
            return "Novice";
        }
    }

    public ICountry determineCompanyCountry(Date date) throws PWCGException 
    {
        ICountry companyCountry = CountryFactory.makeCountryByCountry(country);
        
        if (date != null)
        {
            CompanyHistoryEntry companyHistoryEntry = getCompanyHistoryEntryForDate(date);
            if (companyHistoryEntry != null)
            {
                String serviceName = companyHistoryEntry.getArmedServiceName();
                ArmedService service = ArmedServiceFactory.createServiceManager().getArmedServiceByName(serviceName);
                companyCountry = CountryFactory.makeCountryByService(service);
            }
        }
        
        return companyCountry;
    }

	public ICountry getCountry() 
	{
		return CountryFactory.makeCountryByCountry(country);
	}

	public int getCompanyId() 
	{
		return companyId;
	}

	public void setCompanyId(int id) 
	{
		this.companyId = id;
	}

	public int determineCompanySkill(Date date) throws PWCGException 
	{
	    int skillNow = skill;
        CompanyHistoryEntry companyHistoryEntry = getCompanyHistoryEntryForDate(date);
        if (date != null)
        {
            if (companyHistoryEntry != null)
            {
                int skillAtDate = companyHistoryEntry.getSkill();
                if (skillAtDate != CompanyHistoryEntry.NO_Company_SKILL_CHANGE && skillAtDate > 20)
                {
                    skillNow = skillAtDate;
                }
            }
        }
        
        return skillNow;
	}

	public void setSkill(int skill) 
	{
		this.skill = skill;
	}

    public List<String> getActiveArchTypes(Date date) throws PWCGException 
    {
        List<String> activeArchTypes = new ArrayList<>();
        for (CompanyTankAssignment tankAssignment : tankAssignments)
        {
            if (DateUtils.isDateInRange(date, tankAssignment.getCompanyIntroduction(), tankAssignment.getCompanyWithdrawal()))
            {
                activeArchTypes.add(tankAssignment.getArchType());
            }
        }
        return activeArchTypes;
    }

    public List<String> getAllArchTypes() throws PWCGException 
    {
        List<String> activeArchTypes = new ArrayList<>();
        for (CompanyTankAssignment tankAssignment : tankAssignments)
        {
            activeArchTypes.add(tankAssignment.getArchType());
        }
        return activeArchTypes;
    }

	public Callsign determineCurrentCallsign(Date campaignDate)
	{
		Callsign currentCallsign = Callsign.NONE;

		for (Date callsignStartDate : callsigns.keySet())
		{
			if (!callsignStartDate.after(campaignDate))
			{
				currentCallsign = callsigns.get(callsignStartDate);
			}
			else
			{
				break;
			}
		}

		return currentCallsign;
	}

	public HashMap<String, String> getNamesInUse(Campaign campaign) throws PWCGException
    {
	    CompanyPersonnel companyPersonnel = campaign.getPersonnelManager().getCompanyPersonnel(this.getCompanyId());
        HashMap<String, String> namesUsed = new HashMap <String, String>();
	    if (companyPersonnel != null)
	    {
            CrewMembers companyMembers = CrewMemberFilter.filterActiveAIAndPlayerAndAces(companyPersonnel.getCrewMembersWithAces().getCrewMemberCollection(), campaign.getDate());
            for (CrewMember crewMember : companyMembers.getCrewMemberList())
            {
                int index = crewMember.getName().indexOf(" ");
                String lastName = crewMember.getName().substring(index + 1);
                namesUsed.put(lastName, lastName);
            }
	    }
        return namesUsed;
    }

	public List<CompanyTankAssignment> getPlaneAssignments() 
	{
		return tankAssignments;
	}

	public Map<Date, String> getAirfields() 
	{
		return bases;
	}

	public void setAirfields(Map<Date, String> airfields) 
	{
		this.bases = airfields;
	}

	public CompanyHistory getSquadHistory() 
	{
		return companyHistory;
	}

	public void setSquadHistory(CompanyHistory companyHistory) 
	{
		this.companyHistory = companyHistory;
	}

	public int getService() 
	{
		return serviceId;
	}

	public void setService(int serviceId) 
	{
		this.serviceId = serviceId;
	}

    public int getNightOdds(Date date)
    {
        if (nightMissionOdds == null)
        {
            return 0;
        }
        else
        {
            return nightMissionOdds.determineNighMissionOdds(date);
        }
    }

    public List<Skin> getSkins()
    {
        return skins;
    }

    public void setSkins(List<Skin> skins)
    {
        this.skins = skins;
    }

    public CompanyRoleSet getCompanyRoles()
    {
        return companyRoles;
    }

    public String getFileName()
    {
        return fileName;
    }

    public void setFileName(String fileName)
    {
        this.fileName = fileName;
    }

    public void setTankAssignments(List<CompanyTankAssignment> tankAssignments)
    {
        this.tankAssignments = tankAssignments;
    }

    public String determineUnitIdCode(Date date) throws PWCGException
    {
        String code = unitIdCode;

        if (date != null)
        {
            CompanyHistoryEntry companyHistoryEntry = getCompanyHistoryEntryForDate(date);
            if (companyHistoryEntry != null)
            {
                code = companyHistoryEntry.getUnitIdCode();
                if (code == null)
                {
                    code = "";
                }
            }
        }

        return code;
    }

    public String determineSubUnitIdCode(Date date) throws PWCGException
    {
        String code = subUnitIdCode;

        if (date != null)
        {
            CompanyHistoryEntry companyHistoryEntry = getCompanyHistoryEntryForDate(date);
            if (companyHistoryEntry != null)
            {
                code = companyHistoryEntry.getSubUnitIdCode();
            }
        }

        return code;
    }

    public boolean isCompanyThisRole (Date date, PwcgRole requestedRole) throws PWCGException 
    {
        return companyRoles.isCompanyThisRole(date, requestedRole);
    }
    
    public PwcgRoleCategory determineCompanyPrimaryRoleCategory(Date date) throws PWCGException
    {
        return companyRoles.selectCompanyPrimaryRoleCategory(date);
    }

    @Override
    public Coordinate determinePosition(Date campaignDate) throws PWCGException
    {
        return this.determineCurrentPosition(campaignDate);
    }

    @Override
    public String determineBaseName(Date campaignDate)
    {
        return this.determineCurrentAirfieldName(campaignDate);
    }

    @Override
    public boolean isPlayercompany()
    {
        return true;
    }
}
