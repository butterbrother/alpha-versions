import java.awt.*;
import java.awt.event.*;
import java.util.LinkedList;
import java.util.StringTokenizer;
import java.io.*;
import java.nio.file.*;

class MouseDraw extends Frame {
	private int previousCoordX = 0, previousCoordY = 0;
	private int currentCoordX = 0, currentCoordY = 0;
	private class line {
		public int beginX, beginY, endX, endY;

		public line(int beginX, int beginY, int endX, int endY) {
			this.beginX = beginX;
			this.beginY = beginY;

			this.endX = endX;
			this.endY = endY;
		}
	}
	LinkedList<line> lines = new LinkedList<>();
	boolean isDraw = false;

	public static void main(String args[]) {
		new MouseDraw();
	}

	public void restoreSession() {
		try (BufferedReader dLoader = new BufferedReader(new InputStreamReader(Files.newInputStream(Paths.get("draw.dat")), "UTF-8"), 4096)) {
			String buffer;
			String rawCoord[] = new String[4];
			int pointer;
			while ((buffer = dLoader.readLine()) != null) {
				StringTokenizer pars = new StringTokenizer(buffer, ";");
				pointer = 0;
				while (pars.hasMoreElements()) {
					if (pointer < rawCoord.length)
						rawCoord[pointer++] = pars.nextToken();
				}
				if (!buffer.isEmpty())
					try {
						lines.add(new line(
							Integer.parseInt(rawCoord[0]),
							Integer.parseInt(rawCoord[1]),
							Integer.parseInt(rawCoord[2]),
							Integer.parseInt(rawCoord[3])
						));
					} catch (NumberFormatException ignore) {}
			}
		} catch (IOException ignore) {}
	}

	public MouseDraw() {
		super("Рисуем мышью");
		setSize(640,480);
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				currentCoordX = e.getX();
				currentCoordY = e.getY();

				if (isDraw) {
					isDraw = false;
					lines.add(new line(previousCoordX, previousCoordY, currentCoordX, currentCoordY));
				} else {
					isDraw = true;
					previousCoordX = currentCoordX;
					previousCoordY = currentCoordY;
				}

				repaint();
			}
		});

		addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				currentCoordX = e.getX();
				currentCoordY = e.getY();
				repaint(e.getX(), e.getY() - 20, e.getX() + 50, e.getY());
			}
		});

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				try (BufferedWriter dSaver = new BufferedWriter(new OutputStreamWriter(Files.newOutputStream(Paths.get("draw.dat")), "UTF-8"), 4096)) {
					for (line item : lines) {
						dSaver.append(Integer.toString(item.beginX)).append(";");
						dSaver.append(Integer.toString(item.beginY)).append(";");
						dSaver.append(Integer.toString(item.endX)).append(";");
						dSaver.append(Integer.toString(item.endY)).append("\n");
					}
				} catch (IOException err) {
				}
				System.exit(0);
			}
		});
		setVisible(true);
		restoreSession();
	}

	public void paint(Graphics g) {
		g.drawString(currentCoordX + ":" + currentCoordY, currentCoordX+5, currentCoordY-3);
		for (line item : lines) {
			g.drawLine(item.beginX, item.beginY,	item.endX, item.endY);
		}
	}
}
