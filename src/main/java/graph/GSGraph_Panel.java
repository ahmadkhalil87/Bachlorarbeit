/**
 * 
 */
package graph;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.JPanel;
import javax.swing.OverlayLayout;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;

import org.graphstream.ui.geom.Point2;
import org.graphstream.ui.geom.Point3;
import org.graphstream.ui.swingViewer.View;
import org.graphstream.ui.swingViewer.Viewer;
import org.graphstream.ui.swingViewer.ViewerListener;
import org.graphstream.ui.swingViewer.ViewerPipe;
import org.graphstream.ui.swingViewer.util.Camera;

import searchalgo.SNode;
import utilities.GSEventBus;
import utilities.Node_Info;
import utilities.PumpThread;


/**
 * @author ahmad_khalil
 * @param <T>
 *
 */

public class GSGraph_Panel<T> extends JPanel{

	private static final long serialVersionUID = 1L;
	private final GSEventBus<T> eventSupplier;
	private final GSGraph<T> graph;
	private final Viewer viewer;
	private final View view;
	private final Container graphContainer = new Container();
	private final ViewerPipe viewerPipe;
	private Camera cam;
	
	public GSGraph_Panel(GSEventBus<T> e) {
		super();
		this.eventSupplier = e;
		this.eventSupplier.register(this);
		setLayout(new OverlayLayout(this));
		setBorder(new LineBorder(new Color(0, 0, 0)));
		System.setProperty("org.graphstream.ui", "swing");
		this.graph = new GSGraph<T>("Search Algorithm Visualiser", e);
		this.viewer = new Viewer(this.graph.get_graph(), Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
		this.viewer.enableAutoLayout();
		this.view = this.viewer.addDefaultView(false);
		this.view.getCamera().setAutoFitView(true);
		graphContainer.setLayout(new BorderLayout());
		graphContainer.add((JPanel) this.view, BorderLayout.CENTER);
		add(graphContainer);
		
		/* add listener for mouse events */
		viewerPipe = this.viewer.newViewerPipe();
		
		/* attach a listener */
		new PumpThread(viewerPipe, 50).start();
		this.viewerPipe.addViewerListener(new ViewerListener() {
			
			@Override
			public void viewClosed(String arg0) {
			}

			@Override
			public void buttonReleased(String arg0) {
			}

			@Override
			public void buttonPushed(final String arg0) {
				Runnable doButtonPushedAction = new Runnable() {				
				    public void run() {
				    	//Show node begin
				    	new Node_Info((SNode) GSGraph_Panel.this.graph.get_node(arg0));
				    	//Show node end
					}
				};			
				SwingUtilities.invokeLater(doButtonPushedAction);				 
			}
		});
		
		// zoom
		((Component) this.view).addMouseWheelListener(new MouseWheelListener() {
			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				e.consume();
				int i = e.getWheelRotation();
				double factor = Math.pow(1.25, i);
				cam = view.getCamera();
				double zoom = cam.getViewPercent() * factor;
				Point2 pxCenter  = cam.transformGuToPx(cam.getViewCenter().x, cam.getViewCenter().y, 0);
				Point3 guClicked = cam.transformPxToGu(e.getX(), e.getY());
				double newRatioPx2Gu = cam.getMetrics().ratioPx2Gu/factor;
				double x = guClicked.x + (pxCenter.x - e.getX())/newRatioPx2Gu;
				double y = guClicked.y - (pxCenter.y - e.getY())/newRatioPx2Gu;
				cam.setViewCenter(x, y, 0);
				cam.setViewPercent(zoom);
			}
		});
	}
	
	public void reset_view() {
		if (cam!=null) cam.resetView();
	}
	
}
