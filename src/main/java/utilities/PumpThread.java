package utilities;

import org.graphstream.ui.swingViewer.ViewerPipe;

public final class PumpThread extends Thread {
	
	private final int interval;
	private final ViewerPipe pipe;

	public PumpThread(ViewerPipe pipe, int interval) {
		super("PumpThread");
		setDaemon(true);
		this.pipe = pipe;
		this.interval = interval;
	}

	@Override
	public void run() {
		while (!isInterrupted()) {
			try {
				Thread.sleep(interval);
				pipe.pump();
			} catch (InterruptedException iex) {
				interrupt();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

}
