package pwcg.gui.maingui.config;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.tank.TankType;
import pwcg.campaign.utils.TanksOwnedManager;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;
import pwcg.gui.CampaignGuiContextManager;
import pwcg.gui.ScreenIdentifier;
import pwcg.gui.UiImageResolver;
import pwcg.gui.colors.ColorMap;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.dialogs.PWCGMonitorSupport;
import pwcg.gui.dialogs.PWCGMonitorSupport.MonitorSize;
import pwcg.gui.maingui.PwcgMainScreen;
import pwcg.gui.utils.ContextSpecificImages;
import pwcg.gui.utils.ImageButton;
import pwcg.gui.utils.ImagePanelLayout;
import pwcg.gui.utils.ImageResizingPanel;
import pwcg.gui.utils.PWCGButtonFactory;
import pwcg.gui.utils.PWCGLabelFactory;
import pwcg.gui.utils.ScrollBarWrapper;

public class TanksOwnedConfigurationScreen extends ImageResizingPanel implements ActionListener
{
    private static final long serialVersionUID = 1L;

    private PwcgMainScreen parent = null;
    private Map<String, TankOwned> selectionBoxes = new HashMap<String, TankOwned>();
    
	public TanksOwnedConfigurationScreen(PwcgMainScreen parent) 
	{
        super("");
        this.setLayout(new BorderLayout());

		this.parent = parent;
	}

    public void makePanels()
    {
        try
        {
            String imagePath = UiImageResolver.getImage(ScreenIdentifier.PwcgTanksOwnedConfigurationScreen);
            this.setImageFromName(imagePath);
            
            this.add(makeButtonPanel(), BorderLayout.WEST);
            this.add(makeCenterPanel(), BorderLayout.CENTER);

            this.setCheckBoxes();
        }
        catch (Exception e)
        {
            PWCGLogger.logException(e);
            ErrorDialog.internalError(e.getMessage());
        }
    }

	public void setCheckBoxes()
	{
		try
		{
			TanksOwnedManager tanksOwnedManager = TanksOwnedManager.getInstance();
	
			for (TankOwned selectionBox : selectionBoxes.values())
			{				
				if (tanksOwnedManager.isTankOwned(selectionBox.tank.getType()))
				{
					selectionBox.checkBox.setSelected(true);
				}
				else			
				{
					selectionBox.checkBox.setSelected(false);
				}
			}
			
		}
		catch (Exception e)
		{
			PWCGLogger.logException(e);
			ErrorDialog.internalError(e.getMessage());
		}
	}

	public JPanel makeButtonPanel() throws PWCGException 
	{
        JPanel navPanel = new JPanel(new BorderLayout());
        navPanel.setOpaque(false);

        JPanel buttonPanel = new JPanel(new GridLayout(0,1));
        buttonPanel.setOpaque(false);

        for (int i = 0; i < 2; ++i)
        {
            buttonPanel.add(PWCGLabelFactory.makeDummyLabel());
        }
        
        JButton selectAllButton = PWCGButtonFactory.makeTranslucentMenuButton("Select All", "Select All", "Select all tanks as owned", this);
        buttonPanel.add(selectAllButton);

        JButton deselectAllButton = PWCGButtonFactory.makeTranslucentMenuButton("Deselect All", "Deselect All", "Select all tanks as not owned", this);
        buttonPanel.add(deselectAllButton);
        

        for (int i = 0; i < 3; ++i)
        {
            buttonPanel.add(PWCGLabelFactory.makeDummyLabel());
        }

        JButton acceptButton = PWCGButtonFactory.makeTranslucentMenuButton("Accept", "Accept", "Accept tanks owned", this);
        buttonPanel.add(acceptButton);

        JButton cancelButton = PWCGButtonFactory.makeTranslucentMenuButton("Cancel", "Cancel", "Cancel tanks owned edits", this);
        buttonPanel.add(cancelButton);

        navPanel.add (buttonPanel, BorderLayout.NORTH);
        
        return navPanel;
 	}

    public JPanel makeCenterPanel() throws PWCGException 
    {
        String imagePath = ContextSpecificImages.imagesMisc() + "paperFull.jpg";
        JPanel tankSelectionPanel = new ImagePanelLayout(imagePath, new BorderLayout());

        JComponent alliedPanel = makeAlliedPanel();
        JComponent axisPanel = makeAxisPanel();
        JComponent blankPanel = makeBlankPanel();

        tankSelectionPanel.add (alliedPanel, BorderLayout.WEST);
        tankSelectionPanel.add (blankPanel, BorderLayout.CENTER);
        tankSelectionPanel.add (axisPanel, BorderLayout.EAST);
        
        return tankSelectionPanel;
    }

    public JPanel makeBlankPanel() throws PWCGException 
    {        
        JPanel blankPanel = new JPanel(new GridLayout(0, 2));

        blankPanel.setOpaque(false);


        return blankPanel;
    }

    public JPanel makeAxisPanel() throws PWCGException 
    {
        List<TankType> axisPlanes = PWCGContext.getInstance().getTankTypeFactory().getAxisTanks();
        return makePlanePanel(axisPlanes);
    }

    public JPanel makeAlliedPanel() throws PWCGException 
    {
        List<TankType> alliedPlanes = PWCGContext.getInstance().getTankTypeFactory().getAlliedTanks();
        return makePlanePanel(alliedPlanes);
    }

	public JPanel makePlanePanel(List<TankType> tanks) throws PWCGException 
	{
        JPanel tankListOuterPanel = new JPanel(new BorderLayout());
        tankListOuterPanel.setOpaque(false);
		
		TreeMap<String, TankType> tankMap = sortPlanesByType(tanks);
		
		JPanel tankListPanel = createPlanePanel(tankMap);
		
        JScrollPane tankListScroll = ScrollBarWrapper.makeScrollPane(tankListPanel);
        
        tankListOuterPanel.add(tankListScroll, BorderLayout.CENTER);
        
        return tankListOuterPanel;
	}

    private TreeMap<String, TankType> sortPlanesByType(List<TankType> tanks)
    {
        TreeMap<String, TankType> tankMap = new TreeMap<String, TankType>();
        for (int i = 0; i < tanks.size(); ++i)
        {
            TankType tank = tanks.get(i);
            tankMap.put(tank.getType(), tank);
        }
        return tankMap;
    }

    private JPanel createPlanePanel(TreeMap<String, TankType> tankMap) throws PWCGException
    {
        MonitorSize monitorSize = PWCGMonitorSupport.getFrameWidth();
        int columns = 2;
        if (monitorSize == MonitorSize.FRAME_LARGE)
        {
            columns = 3;
        }
        
        JPanel tankListPanel = new JPanel(new GridLayout(0, columns));
        tankListPanel.setOpaque(false);

        Color buttonBG = ColorMap.PAPERPART_BACKGROUND;
        
        for (TankType tank : tankMap.values())
		{
			JCheckBox b1 = ImageButton.makeCheckBox(tank.getDisplayName(), tank.getType());

			b1.addActionListener(this);
			b1.setBackground(buttonBG);
			b1.setOpaque(false);
			tankListPanel.add(b1);
			
			TankOwned owned = new TankOwned();
			owned.tank = tank;
			owned.checkBox = b1;
			selectionBoxes.put(tank.getDisplayName(), owned);
		}
        
        return tankListPanel;
    }

	public void actionPerformed(ActionEvent ae)
	{
		try
		{
			if (ae.getActionCommand().equalsIgnoreCase("Select All"))
			{
				for (TankOwned box: selectionBoxes.values())
				{
					box.checkBox.setSelected(true);
				}
			}
			if (ae.getActionCommand().equalsIgnoreCase("Deselect All"))
			{
				for (TankOwned box: selectionBoxes.values())
				{
					box.checkBox.setSelected(false);
				}
			}
			if (ae.getActionCommand().equalsIgnoreCase("Accept"))
			{
					TanksOwnedManager tanksOwnedManager = TanksOwnedManager.getInstance();
					tanksOwnedManager.clear();
					
					for (TankOwned box: selectionBoxes.values())
					{
						JCheckBox selectionBox = box.checkBox;
						
						if (selectionBox.isSelected())
						{
							tanksOwnedManager.setTankOwned(box.tank.getType());
						}
					}
					
					tanksOwnedManager.write();
 
					parent.refresh();
					
			        CampaignGuiContextManager.getInstance().popFromContextStack();
			}
			if (ae.getActionCommand().equalsIgnoreCase("Cancel"))
			{
                parent.refresh();

                CampaignGuiContextManager.getInstance().popFromContextStack();
			}
		}
		catch (Exception e)
		{
			PWCGLogger.logException(e);
			ErrorDialog.internalError(e.getMessage());
		}
	}

	private class TankOwned
	{
		JCheckBox checkBox = null;
		TankType tank;
	}
}

