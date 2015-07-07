import java.awt.*;
import java.awt.event.*;

public class CenterText extends Frame {
    final Font f = new Font("SansSerif", Font.BOLD, 18);
    
    public static void main(String args[]) {
        new CenterText();
    }
    
    public CenterText() {
        super("Центрирование текста");
        setSize(200, 100);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        setVisible(true);
    }
    
    public void paint(Graphics g) {
        Dimension d = getSize();
        
        g.setColor(Color.white);
        g.fillRect(0, 0, d.width, d.height);
        g.setColor(Color.black);
        g.setFont(f);
        drawCenteredString("Центрировано.", d.width, d.height, g);
        g.drawRect(0, 0, d.width-1, d.height-1);
    }
    
    public void drawCenteredString(String s, int w, int h, Graphics g) {
        FontMetrics fm = g.getFontMetrics();
        int x = (w - fm.stringWidth(s))/2;
        int y = (fm.getAscent() + (h - (fm.getAscent() + fm.getDescent()))/2);
        g.drawString(s, x, y);
    }
}
