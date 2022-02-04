package pwcg.gui.rofmap;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;


public class PWCGMouseMotionListener extends MouseMotionAdapter
{
	private IMouseCallback parent = null;
	
	PWCGMouseMotionListener(IMouseCallback parent)
	{
		this.parent = parent;
	}

	@Override
	public void mouseDragged(MouseEvent e) 
	{
		parent.mouseDraggedCallback(e);
	}

	@Override
	public void mouseMoved(MouseEvent e) 
	{	
		parent.mouseMovedCallback(e);
	}

}
