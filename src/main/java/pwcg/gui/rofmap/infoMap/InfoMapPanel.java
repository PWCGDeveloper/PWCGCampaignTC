package pwcg.gui.rofmap.infoMap;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import pwcg.campaign.api.ICountry;
import pwcg.campaign.api.Side;
import pwcg.campaign.company.Company;
import pwcg.campaign.company.CompanyManager;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.factory.CountryFactory;
import pwcg.campaign.group.AirfieldManager;
import pwcg.campaign.group.Block;
import pwcg.campaign.group.Bridge;
import pwcg.campaign.group.GroupManager;
import pwcg.campaign.group.airfield.Airfield;
import pwcg.campaign.tank.PwcgRoleCategory;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.PWCGLocation;
import pwcg.core.utils.PWCGLogger;
import pwcg.gui.colors.ColorMap;
import pwcg.gui.colors.IServiceColorMap;
import pwcg.gui.rofmap.MapGUI;
import pwcg.gui.rofmap.MapPanelBase;

public class InfoMapPanel extends MapPanelBase
{
    public static int DISPLAY_FRONT = 0;
	public static int DISPLAY_AIRFIELDS = 1;
	public static int DISPLAY_TOWNS = 2;
    public static int DISPLAY_RAILROADS = 3;
    public static int DISPLAY_BRIDGES = 4;
    
    public static int DISPLAY_FIGHTER = 5;
    public static int DISPLAY_ATTACK = 6;
    public static int DISPLAY_BOMBER = 7;
    
	private static final long serialVersionUID = 1L;
	
	private ICountry country = CountryFactory.makeMapReferenceCountry(Side.ALLIED);

    private Boolean[] whatToDisplay = new Boolean[9];

    private InfoMapCompanyMover companyMover = new InfoMapCompanyMover();
    
    private boolean enableEditing = true;

	public InfoMapPanel(MapGUI parent) throws PWCGException  
	{
		super(parent);
		
		for (int i = 0; i < whatToDisplay.length; ++i)
		{
		    whatToDisplay[i] = false;
		}
	}

	public void setData() throws PWCGException 
	{
        setMapBackground(100);

		repaint();
	}

    private void repaintMap()
    {
        makeVisible(false);
        makeVisible(true);
    }

	public void paintComponent(Graphics g)
	{
		try
		{
			paintBaseMap(g);
			
			g.setColor(Color.black);
			
			if (whatToDisplay[DISPLAY_AIRFIELDS])
			{
		        AirfieldManager airfieldData =  PWCGContext.getInstance().getCurrentMap().getAirfieldManager();
		        Map<String, Airfield> allAF = airfieldData.getAllAirfields();
		        for (Airfield af : allAF.values())
		        {
		            drawPointsByCountry(g, af.getPosition(), af.determineCountryOnDate(parent.getMapDate()));
		        }        
			}
			
			if (whatToDisplay[DISPLAY_TOWNS])
			{
		        GroupManager groupData =  PWCGContext.getInstance().getCurrentMap().getGroupManager();
		        List<PWCGLocation> mapLegends = groupData.getTownLocations().getLocations();
		        for (PWCGLocation mapLegend : mapLegends)
		        {
		            drawPointsByCountry(g, mapLegend.getPosition(), null);
		        }        
			}
            
            if (whatToDisplay[DISPLAY_RAILROADS])
            {
                GroupManager groupData =  PWCGContext.getInstance().getCurrentMap().getGroupManager();
                List<Block> railroads = groupData.getRailroadList();
                for (Block railroad : railroads)
                {
                    drawPointsByCountry(g, railroad.getPosition(), railroad.determineCountryOnDate(parent.getMapDate()));
                }        
            }
            
            if (whatToDisplay[DISPLAY_BRIDGES])
            {
                GroupManager groupData =  PWCGContext.getInstance().getCurrentMap().getGroupManager();
                List<Bridge> bridges = groupData.getBridgeFinder().findAllBridges();
                for (Bridge bridge : bridges)
                {
                    drawPointsByCountry(g, bridge.getPosition(), bridge.determineCountryOnDate(parent.getMapDate()));
                }        
            }
            
            if (whatToDisplay[DISPLAY_FIGHTER])
            {
                CompanyManager companyManager =  PWCGContext.getInstance().getCompanyManager();
                List<Company> allCompanys = companyManager.getActiveCompaniesForCurrentMap(parent.getMapDate());
                for (Company company : allCompanys)
                {
                    PwcgRoleCategory companyPrimaryRole = company.determineCompanyPrimaryRoleCategory(parent.getMapDate());
                    if (companyPrimaryRole == PwcgRoleCategory.FIGHTER)
                    {
                        drawPointsByCompany(g, company);
                    }
                }        
            }
            
            if (whatToDisplay[DISPLAY_BOMBER])
            {
                CompanyManager companyManager =  PWCGContext.getInstance().getCompanyManager();
                List<Company> allCompanys = companyManager.getActiveCompaniesForCurrentMap(parent.getMapDate());
                for (Company company : allCompanys)
                {
                    PwcgRoleCategory companyRole = company.determineCompanyPrimaryRoleCategory(parent.getMapDate());
                    if ((companyRole == PwcgRoleCategory.BOMBER) || 
                        (companyRole == PwcgRoleCategory.TRANSPORT))
                    {
                        drawPointsByCompany(g, company);
                    }
                }        
            }
            
            if (whatToDisplay[DISPLAY_ATTACK])
            {
                CompanyManager companyManager =  PWCGContext.getInstance().getCompanyManager();
                List<Company> allCompanys = companyManager.getActiveCompaniesForCurrentMap(parent.getMapDate());
                for (Company company : allCompanys)
                {
                    PwcgRoleCategory companyPrimaryRole = company.determineCompanyPrimaryRoleCategory(parent.getMapDate());
                    if (companyPrimaryRole == PwcgRoleCategory.ATTACK)
                    {
                        drawPointsByCompany(g, company);
                    }
                }        
            }
		}
		catch (Exception e)
		{
			PWCGLogger.logException(e);
		}
	}

    private void drawPointsByCompany(Graphics g, Company company) throws PWCGException 
    {
        IServiceColorMap serviceColorMap = company.determineServiceForCompany(parent.getMapDate()).getServiceColorMap();
        
        Color color = serviceColorMap.getColorForCompany(company, parent.getMapDate());
        
        drawPoints(g, company.determineCurrentPosition(parent.getMapDate()), color);
    }

    private void drawPointsByCountry(Graphics g, Coordinate coordinate, ICountry country) 
    {
        Color color = ColorMap.UNKNOWN;
                        
        if (country == null)
        {
            color = ColorMap.PAPER_FOREGROUND;
        }
        else if (country.getSide() == Side.AXIS)
        {
            color = ColorMap.GERMAN_AIRFIELD_PINK;
        }
        else if (country.getSide() == Side.ALLIED)
        {
            color = ColorMap.ALLIED_AIRFIELD_PINK;
        }
        else if (country.isNeutral())
        {
            color = ColorMap.NEUTRAL;
        }
        
        drawPoints(g, coordinate, color);
    }

	private void drawPoints(Graphics g, Coordinate coordinate, Color color) 
	{
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(3));
        
        g.setColor(color);

        int size = 20;
        
        int halfWay = size / 2;

        Point point = super.coordinateToPoint(coordinate);

        Ellipse2D.Double circle = new Ellipse2D.Double(point.x - halfWay, point.y - halfWay, size, size);
        g2.fill(circle);
	}
    
    /**
     * @param e
     */
    private void displayCoordinates(MouseEvent e)
    {
        try
        {
            Coordinate coordinate = getCoordinates(e);

            String text = "Coordinates: " + coordinate.getXPos() + ", " + coordinate.getZPos();

            InfoMapPopup menu = new InfoMapPopup(this, text);
            menu.show(e.getComponent(), e.getX(), e.getY());
        }
        catch (Exception exp)
        {
            PWCGLogger.logException(exp);
        }
    }
    
    /**
     * @param e
     */
    private Coordinate getCoordinates(MouseEvent e)
    {
        try
        {
            Point point = new Point();
            point.x = e.getX();
            point.y = e.getY();
            Coordinate coordinate = pointToCoordinate(point);
            return coordinate;
        }
        catch (Exception exp)
        {
            PWCGLogger.logException(exp);
        }
        
        return null;
    }

    @Override
    public void mouseMovedCallback(MouseEvent e) 
    {       
    }

    @Override
    public void mouseDraggedCallback(MouseEvent e)
    {
        super.mouseDraggedCallback(e);
    }

    @Override
    public void leftClickReleasedCallback(MouseEvent e) throws PWCGException  
    {       
        super.leftClickReleasedCallback(e);
    }


    /**
     * @param e
     */
    @Override
    public void leftClickCallback(MouseEvent e) 
    {       
        try
        {
            Point clickPoint = new Point();
            clickPoint.x = e.getX();
            clickPoint.y = e.getY();
    
            if(!showCompanyInfo(e, clickPoint))
            {
                super.leftClickCallback(e);
            }
        }
        catch (PWCGException exp)
        {
            exp.printStackTrace();
        }
    }

    @Override
    public void rightClickCallback(MouseEvent e) 
    {       
        Point clickPoint = new Point();
        clickPoint.x = e.getX();
        clickPoint.y = e.getY();
        
        if (!showAirfieldName(e, clickPoint))
        {
            displayCoordinates(e);
        }
    }

    private boolean showAirfieldName(MouseEvent e, Point clickPoint)
    {
        if (whatToDisplay[DISPLAY_AIRFIELDS] == true)
        {
            Airfield field = null;
            try
            {
                Coordinate clickCoord = this.pointToCoordinate(clickPoint);

               field = PWCGContext.getInstance().getCurrentMap().getAirfieldManager().getAirfieldFinder().getNearbyAirfield(clickCoord, 5000.0);
                if (field == null)
                {
                    field = PWCGContext.getInstance().getCurrentMap().getAirfieldManager().getAirfieldFinder().getNearbyAirfield(clickCoord, 5000.0);
                }
                    
            }
            catch (PWCGException e1)
            {
                e1.printStackTrace();
            }
            
            if (field != null)
            {
                if (enableEditing)
                {
                    showEditAirfieldLocation(e, field);
                }
                else
                {
                    showAirfieldInfo(e, field);
                }

                return true;
            }
        }
        
        return false;
    }

    private void showEditAirfieldLocation(MouseEvent e, Airfield field)
    {
        InfoAirfieldSelectPopup menu = new InfoAirfieldSelectPopup(this, field.getName());
        menu.show(e.getComponent(), e.getX(), e.getY());
    }

    private void showAirfieldInfo(MouseEvent e, Airfield field)
    {
        InfoMapPopup menu = new InfoMapPopup(this, field.getName());
        menu.show(e.getComponent(), e.getX(), e.getY());
        
    }

    private boolean showCompanyInfo(MouseEvent e, Point clickPoint) throws PWCGException
    {
        List<Company> selectedCompanys = buildSelectedCompanys(clickPoint);        
        if (selectedCompanys.size() > 0)
        {
            if (enableEditing)
            {
                showEditCompanyLocation(e, selectedCompanys);
            }
            else
            {
                showCompanyInfo(e, selectedCompanys);
            }
            
            return true;
        }
        
        return false;
    }

    private List<Company> buildSelectedCompanys(Point clickPoint) throws PWCGException
    {
        List <Company> selectedCompanys = new ArrayList<Company>();
            
        CompanyManager companyManager =  PWCGContext.getInstance().getCompanyManager();
        List<Company> allCompanys = companyManager.getActiveCompaniesForCurrentMap(parent.getMapDate());
                
        for (Company company : allCompanys)
        {
            PwcgRoleCategory companyPrimaryRole = company.determineCompanyPrimaryRoleCategory(parent.getMapDate());

            if ((whatToDisplay[DISPLAY_FIGHTER] == true && companyPrimaryRole == PwcgRoleCategory.FIGHTER)    || 
                (whatToDisplay[DISPLAY_BOMBER] == true && companyPrimaryRole == PwcgRoleCategory.BOMBER)      || 
                (whatToDisplay[DISPLAY_BOMBER] == true && companyPrimaryRole == PwcgRoleCategory.TRANSPORT)   || 
                (whatToDisplay[DISPLAY_ATTACK] == true && companyPrimaryRole == PwcgRoleCategory.ATTACK))
            {
                // Is the company based near a field that was clicked on
                Coordinate clickCoord = this.pointToCoordinate(clickPoint);
                Airfield nearbyField = PWCGContext.getInstance().getCurrentMap().getAirfieldManager().getAirfieldFinder().getNearbyAirfield(clickCoord, 5000.0);
                if (nearbyField != null)
                {
                    String companyFieldName = company.determineCurrentAirfieldName(parent.getMapDate());
                    if (companyFieldName.equals(nearbyField.getName()))
                    {
                        selectedCompanys.add(company);
                    }
                }
            }
        }
        return selectedCompanys;
    }

    private void showCompanyInfo(MouseEvent e, List<Company> selectedCompanys) throws PWCGException
    {
        String displayString = "";
        for (Company company : selectedCompanys)
        {                    
            String fieldName = company.determineCurrentAirfieldName(parent.getMapDate());
            String companyName = company.determineDisplayName(parent.getMapDate());
                
            String info = companyName + " at " + fieldName;
            displayString += info + "   ";
        }

        InfoMapPopup companyPopup = new InfoMapPopup(this, displayString);
        companyPopup.show(e.getComponent(), e.getX(), e.getY());
    }

    private void showEditCompanyLocation(MouseEvent e, List<Company> selectedCompanys)
    {
        InfoCompanySelectPopup menu = new InfoCompanySelectPopup(this, selectedCompanys, parent.getMapDate());
        menu.show(e.getComponent(), e.getX(), e.getY());
    }

	public Dimension getMapSize()
	{
		Dimension mapSize = new Dimension();
		mapSize.height =  image.getHeight(null);
		mapSize.width =  image.getWidth(null);
		return mapSize;
	}


	/**
	 * @param whatToDisplay
	 */
	public void setWhatToDisplay(int displayItem, boolean displayIt)
	{
		this.whatToDisplay[displayItem] = displayIt;
	    		
		repaintMap();
	}

	@Override
	public void actionPerformed(ActionEvent arg0) 
	{
        try
        {
            String action = arg0.getActionCommand();
            if (action.contains("Select Company:"))
            {
                String[] parts = action.split(":");
                int companyId = Integer.valueOf(parts[1]);
                companyMover.setCompanyIdToMove(companyId);
            }
            if (action.contains("Select Target Airfield:"))
            {
                String[] parts = action.split(":");
                String airfield = parts[1];
                companyMover.moveCompany(airfield, parent.getMapDate());
                InfoMapGUI infoMapGUI = (InfoMapGUI)parent;
                infoMapGUI.refreshCompanyPlacement();
            }
            else if (action.contains("Cancel"))
            {
            }
        }
        catch (Exception e)
        {
            PWCGLogger.logException(e);
        }
	}

	public ICountry getCountry() {
		return country;
	}

	public void setCountry(ICountry country) {
		this.country = country;
	}


    @Override
    public void rightClickReleasedCallback(MouseEvent e)
    {        
    }

    @Override
    public void centerClickCallback(MouseEvent e) 
    {
    }

    @Override
    public Point upperLeft()
    {
        return null;
    }

    public void resetFromActual()
    {
        repaintMap();
    }

}
