package pwcg.gui.rofmap.intelmap;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pwcg.campaign.Campaign;
import pwcg.campaign.company.Company;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.group.AirfieldManager;
import pwcg.campaign.group.airfield.Airfield;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.PWCGLogger;
import pwcg.gui.colors.ColorMap;
import pwcg.gui.colors.IServiceColorMap;
import pwcg.gui.rofmap.MapPanelBase;
import pwcg.gui.utils.MapPointInfoPopup;

public class IntelMapPanel extends MapPanelBase
{
	private static final long serialVersionUID = 1L;
	
	private Map <String, IntelCompanyMapPoint> companyPoints = new HashMap<String, IntelCompanyMapPoint>();
	private Map <String, IntelAirfieldMapPoint> airfieldPoints = new HashMap<String, IntelAirfieldMapPoint>();

	private IntelMapGUI parent;
	private Campaign campaign;

	public IntelMapPanel(IntelMapGUI parent, Campaign campaign) throws PWCGException  
	{
		super(parent);

        this.parent = parent;       
        this.campaign = campaign;       
	}

	public void setData() throws PWCGException 
	{
	    Date date = campaign.getDate();

		companyPoints.clear();
		List<Company> allCompanys =  PWCGContext.getInstance().getCompanyManager().getActiveCompaniesForCurrentMap(date);
		for (Company company : allCompanys)
		{
			addCompanyPoint(company);
		}

		airfieldPoints.clear();
		AirfieldManager airfieldData =  PWCGContext.getInstance().getCurrentMap().getAirfieldManager();
        Map<String, Airfield> allAF = airfieldData.getAllAirfields();
	    for (Airfield af : allAF.values())
	    {
	    	addAirfieldPoint(af);
	    }
	    
	    setMapBackground(100);
	    
        repaint();
	}

	public void paintComponent(Graphics g)
	{
		try
		{
			paintBaseMapWithMajorGroups(g);
			
			g.setColor(Color.black);
			drawAF(g);
            drawPoints(g);
		}
		catch (Exception e)
		{
			PWCGLogger.logException(e);
		}
	}
	
	
	private void drawAF(Graphics g) 
	{
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(3));
         
	    for (IntelAirfieldMapPoint airfieldPoint : airfieldPoints.values())
	    {
    		g.setColor(ColorMap.EMPTY_AIRFIELD);

	    	Point point = super.coordinateToPoint(airfieldPoint.coord);
	    	
	    	int size = 10;
	    	int halfWay = size / 2;

	    	Ellipse2D.Double circle = new Ellipse2D.Double(point.x - halfWay, point.y - halfWay, size, size);
	    	g2.fill(circle);
	    }
	}

	
	private void drawPoints(Graphics g) throws PWCGException 
	{
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(3));
        
	    for (IntelCompanyMapPoint mapPoint : companyPoints.values())
	    {
	        IServiceColorMap serviceColorMap = mapPoint.service.getServiceColorMap();
	        
	        // We will always have an active campaign on the intel map
	        Color color = serviceColorMap.getColorForCompany(mapPoint.company, parent.getMapDate());
	                        
	        g.setColor(color);
	    	
	    	int size = 20;
	    	
	    	if (mapPoint.isPlayerCompany)
	    	{
		    	size = 30;
	    		g.setColor(Color.MAGENTA);
	    	}
	    	
	    	int halfWay = size / 2;

	    	Point point = super.coordinateToPoint(mapPoint.coord);

	    	Ellipse2D.Double circle = new Ellipse2D.Double(point.x - halfWay, point.y - halfWay, size, size);
	    	g2.fill(circle);
	    }
	}

	public void mouseMovedCallback(int x, int y) 
	{
		List<IntelCompanyMapPoint> mapPoints = getCompanyMapPoints(x, y);

		if (mapPoints.size() > 0)
		{
			setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
		}
		else
		{
			setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		}
	}

	@Override
	public void leftClickCallback(MouseEvent e) 
	{		
		List<IntelCompanyMapPoint> mapPoints = getCompanyMapPoints(e.getX(), e.getY());
		if (mapPoints.size() > 0)
		{
			CompanyInfoPopup menu = new CompanyInfoPopup(this, mapPoints);
			menu.show(e.getComponent(), e.getX(), e.getY());
		}
		else
		{
			super.leftClickCallback(e);
		}
	}

	@Override
	public void rightClickCallback(MouseEvent e) 
	{		
		List<IntelAirfieldMapPoint> mapPoints = getAirfieldMapPoints(e.getX(), e.getY());
		if (mapPoints.size() > 0)
		{
			AirfieldInfoPopup menu = new AirfieldInfoPopup(this, mapPoints);
			menu.show(e.getComponent(), e.getX(), e.getY());
		}
		else
		{
		    try
		    {
    		    Point point = new Point();
                point.x = e.getX();
                point.y = e.getY();
    		    Coordinate coordinate = pointToCoordinate(point);
    		    MapPointInfoPopup menu = new MapPointInfoPopup(this, coordinate);
                menu.show(e.getComponent(), e.getX(), e.getY());
		    }
		    catch (Exception exp)
		    {
                PWCGLogger.logException(exp);
		    }
		}
	}

	private List<IntelCompanyMapPoint> getCompanyMapPoints (int x, int y)
	{
		List<IntelCompanyMapPoint> selectedMapPoints = new ArrayList<IntelCompanyMapPoint>();
		
		for (IntelCompanyMapPoint mapPoint : companyPoints.values())
		{
	    	Point point = super.coordinateToPoint(mapPoint.coord);

			if ((Math.abs(point.x - x) < 10) && 
				(Math.abs(point.y - y) < 10))
			{
				selectedMapPoints.add(mapPoint);
			}
		}
		
		return selectedMapPoints;
	}

	private List<IntelAirfieldMapPoint> getAirfieldMapPoints (int x, int y)
	{
		List<IntelAirfieldMapPoint> selectedMapPoints = new ArrayList<IntelAirfieldMapPoint>();
		
		for (IntelAirfieldMapPoint mapPoint : airfieldPoints.values())
		{
	    	Point point = super.coordinateToPoint(mapPoint.coord);

			if ((Math.abs(point.x - x) < 10) && 
				(Math.abs(point.y - y) < 10))
			{
				selectedMapPoints.add(mapPoint);
			}
		}
		
		return selectedMapPoints;
	}

	private void addAirfieldPoint(Airfield field) throws PWCGException 
	{
		if (field != null)
		{
            IntelAirfieldMapPoint mapPoint = new IntelAirfieldMapPoint();
            mapPoint.name = field.getName();
            mapPoint.coord = field.getPosition().copy();
            mapPoint.countryName = field.determineCountryOnDate(parent.getMapDate()).getCountryName();

            airfieldPoints.put(mapPoint.name, mapPoint);
		}

	}

	private void addCompanyPoint(Company company) throws PWCGException 
	{
		String fieldName = company.determineBaseName(parent.getMapDate());
		Airfield field =  PWCGContext.getInstance().getCurrentMap().getAirfieldManager().getAirfield(fieldName);

		if (field != null)
		{
            IntelCompanyMapPoint mapPoint = new IntelCompanyMapPoint();
            mapPoint.desc = company.determineDisplayName(parent.getMapDate());
            mapPoint.coord = field.getPosition().copy();
            mapPoint.service = company.determineServiceForCompany(parent.getMapDate());
            mapPoint.company = company;
            
            if (company.getCompanyId() == campaign.findReferencePlayer().getCompanyId())
            {
                mapPoint.isPlayerCompany = true;
            }

            companyPoints.put(mapPoint.desc, mapPoint);
		}

	}

	public Point upperLeft()
	{
		Point upperLeft = new Point();
		
		upperLeft.x = 10000000;
		upperLeft.y = 10000000;
		for (IntelCompanyMapPoint mapPoint: companyPoints.values())
		{
	    	Point point = super.coordinateToPoint(mapPoint.coord);
			
			if (point.x < upperLeft.x)
			{
				upperLeft.x = point.x;
			}
			if (point.y < upperLeft.y)
			{
				upperLeft.y = point.y;
			}
		}
		
		return upperLeft;
	}


	@Override
	public void actionPerformed(ActionEvent arg0) 
	{
		try
		{
			String action = arg0.getActionCommand();
			if (action.equals("Cancel"))
			{
			}
			else if (action.contains("Company:"))
			{
				String squadName = action.substring(action.indexOf(":") + 1);
				IntelCompanyMapPoint mapPoint = companyPoints.get(squadName);
				if (mapPoint != null)
				{
					parent.updateInfoPanel(mapPoint.company.getCompanyId());
				}
			}
		}
		catch (Exception e)
		{
			PWCGLogger.logException(e);
		}
	}

	public Map<String, IntelCompanyMapPoint> getCompanyPoints() {
		return companyPoints;
	}

	public Map<String, IntelAirfieldMapPoint> getAirfieldPoints() {
		return airfieldPoints;
	}

	@Override
	public void mouseMovedCallback(MouseEvent e) 
	{		
	}

	@Override
	public void rightClickReleasedCallback(MouseEvent e)  
	{		
	}

    @Override
    public void centerClickCallback(MouseEvent e) 
    {
    }
}
