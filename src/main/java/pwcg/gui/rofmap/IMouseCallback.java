package pwcg.gui.rofmap;

import java.awt.event.MouseEvent;

import pwcg.core.exception.PWCGException;

public interface IMouseCallback
{
    void leftClickCallback(MouseEvent e);
    void centerClickCallback(MouseEvent e);
    void rightClickCallback(MouseEvent e);
    void leftClickReleasedCallback(MouseEvent e) throws PWCGException;
    void mouseDraggedCallback(MouseEvent e);
    void mouseMovedCallback(MouseEvent e);
    void increaseZoom();
    void decreaseZoom();
}
