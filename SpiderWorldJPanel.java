import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelListener;
import java.io.IOException;
import java.util.Map;
import java.util.List;
class SpiderPanel extends JPanel {
    public static Map<String, Object> spiderWorldInfo;
    private int gridCellSize = 75;
    private int gridX = 20;
    private int gridY = 200;
    private Shape currentShape = null;
    //private int offsetX, offsetY;

    public SpiderPanel(Map<String, Object> spiderWorldInfo) {
        this.spiderWorldInfo = spiderWorldInfo;
    }

    public static Map<String, Object> getSpiderInfo(){
        return spiderWorldInfo;
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        int panelWidth = getWidth();
        int panelHeight = getHeight();

        // Draw 5x5 grid with gray outline
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 5; col++) {
                int x = gridX + col * this.gridCellSize;
                int y = this.gridY + row * this.gridCellSize;
                g.setColor(Color.BLACK);
                g.fillRect(x, y, this.gridCellSize, this.gridCellSize);
                g.setColor(Color.GRAY);
                g.drawRect(x, y, this.gridCellSize, this.gridCellSize);
            }
        }

        // panel for the blocks
        int blockWidth = 450;
        int blockHeight = 550;
        int blockX = panelWidth - blockWidth - 200; // Adjust the values as needed
        int blockY = panelHeight / 2 - blockHeight / 2;
        g.setColor(Color.WHITE);
        g.fillRect(blockX, blockY, blockWidth, blockHeight);
        g.setColor(Color.GRAY);
        g.drawRect(blockX, blockY, blockWidth, blockHeight);

        

        // Draw "Spider World" text
        g.setColor(Color.BLACK);
        g.drawString("Spider World", 30, 50);

        //draw blocks with text
        //****draw step, turn, and paint block****
        DrawBlocks.drawCenteredText(g2d, 1000, 50, 100, 50, "step", Color.GRAY);
        DrawBlocks.drawCenteredText(g2d, 1000, 110, 100, 50, "turn", Color.GRAY);
        DrawBlocks.drawCenteredText(g2d, 1000, 170, 100, 50, "paint", Color.RED);

        //****draw the 4 blocks with color up top****

        //world info variables
        int[]spider_loc = LoadInfo.getSpiderLocation(spiderWorldInfo);
        int num_red_diamonds = LoadInfo.getDiamondCount("red", spiderWorldInfo);
        int num_blue_diamonds = LoadInfo.getDiamondCount("blue", spiderWorldInfo);
        int num_green_diamonds = LoadInfo.getDiamondCount("green", spiderWorldInfo);

        int curr_level = LoadInfo.getCurrentLevel(spiderWorldInfo);
        List<int[]> red_diamond_locs = LoadInfo.getDiamondLocationsByColor("red", spiderWorldInfo);
        List<int[]> blue_diamond_locs = LoadInfo.getDiamondLocationsByColor("blue", spiderWorldInfo);
        List<int[]> green_diamond_locs = LoadInfo.getDiamondLocationsByColor("green", spiderWorldInfo);

        //draw spider
        int x = this.gridX + spider_loc[0] * this.gridCellSize;
        int y = this.gridY + spider_loc[1] * this.gridCellSize;
        g.setColor(Color.white);
        g.fillRect(x, y, 20, 20);

        //draw red diamonds
        for (int i = 0; i < num_red_diamonds; i++) {
            x = this.gridX + red_diamond_locs.get(i)[0] * this.gridCellSize;
            y = this.gridY + red_diamond_locs.get(i)[1] * this.gridCellSize;
            g.setColor(Color.RED);
            g.fillRect(x, y, this.gridCellSize, this.gridCellSize);
        }
        //draw blue diamonds
        for (int i = 0; i < num_blue_diamonds; i++) {
            x = this.gridX + blue_diamond_locs.get(i)[0] * this.gridCellSize;
            y = this.gridY + blue_diamond_locs.get(i)[1] * this.gridCellSize;
            g.setColor(Color.BLUE);
            g.fillRect(x, y, this.gridCellSize, this.gridCellSize);
        }
        //draw green diamonds
        for (int i = 0; i < num_green_diamonds; i++) {
            x = this.gridX + green_diamond_locs.get(i)[0] * this.gridCellSize;
            y = this.gridY + green_diamond_locs.get(i)[1] * this.gridCellSize;
            g.setColor(Color.GREEN);
            g.fillRect(x, y, this.gridCellSize, this.gridCellSize);
        }

        if (currentShape != null) {
            currentShape.draw((g2d));
        }
    }
}

class DragDrop extends JPanel {
    private Map<String, Object> spiderWorldInfo = SpiderPanel.getSpiderInfo();
    private int gridCellSize = 75;
    private int gridX = 20;
    private int gridY = 200;
    private Shape currentShape = null;
    private int offsetX, offsetY;
    private boolean isDragging = false;
    public DragDrop() {
        //setTransferHandler(new ShapeTransferHandler());
        setShape(this.currentShape);
        // Add mouse listener
        addMouseListener(new MyMouseListener());
        addMouseMotionListener(new MyMouseMotionListener());

    }

    public void setShape(Shape shape) {
        this.currentShape = shape;
        repaint();
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        int panelWidth = getWidth();
        int panelHeight = getHeight();

        // Draw 5x5 grid with gray outline
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 5; col++) {
                int x = gridX + col * this.gridCellSize;
                int y = this.gridY + row * this.gridCellSize;
                g.setColor(Color.BLACK);
                g.fillRect(x, y, this.gridCellSize, this.gridCellSize);
                g.setColor(Color.GRAY);
                g.drawRect(x, y, this.gridCellSize, this.gridCellSize);
            }
        }

        // panel for the blocks
        int blockWidth = 450;
        int blockHeight = 550;
        int blockX = panelWidth - blockWidth - 200; // Adjust the values as needed
        int blockY = panelHeight / 2 - blockHeight / 2;
        g.setColor(Color.WHITE);
        g.fillRect(blockX, blockY, blockWidth, blockHeight);
        g.setColor(Color.GRAY);
        g.drawRect(blockX, blockY, blockWidth, blockHeight);



        // Draw "Spider World" text
        g.setColor(Color.BLACK);
        g.drawString("Spider World", 30, 50);

        //draw blocks with text
        //****draw step, turn, and paint block****
        DrawBlocks.drawCenteredText(g2d, 1000, 50, 100, 50, "step", Color.GRAY);
        DrawBlocks.drawCenteredText(g2d, 1000, 110, 100, 50, "turn", Color.GRAY);
        DrawBlocks.drawCenteredText(g2d, 1000, 170, 100, 50, "paint", Color.RED);

        //****draw the 4 blocks with color up top****

        //world info variables
        int[]spider_loc = LoadInfo.getSpiderLocation(spiderWorldInfo);
        int num_red_diamonds = LoadInfo.getDiamondCount("red", spiderWorldInfo);
        int num_blue_diamonds = LoadInfo.getDiamondCount("blue", spiderWorldInfo);
        int num_green_diamonds = LoadInfo.getDiamondCount("green", spiderWorldInfo);

        int curr_level = LoadInfo.getCurrentLevel(spiderWorldInfo);
        List<int[]> red_diamond_locs = LoadInfo.getDiamondLocationsByColor("red", spiderWorldInfo);
        List<int[]> blue_diamond_locs = LoadInfo.getDiamondLocationsByColor("blue", spiderWorldInfo);
        List<int[]> green_diamond_locs = LoadInfo.getDiamondLocationsByColor("green", spiderWorldInfo);

        //draw spider
        int x = this.gridX + spider_loc[0] * this.gridCellSize;
        int y = this.gridY + spider_loc[1] * this.gridCellSize;
        g.setColor(Color.white);
        g.fillRect(x, y, 20, 20);

        //draw red diamonds
        for (int i = 0; i < num_red_diamonds; i++) {
            x = this.gridX + red_diamond_locs.get(i)[0] * this.gridCellSize;
            y = this.gridY + red_diamond_locs.get(i)[1] * this.gridCellSize;
            g.setColor(Color.RED);
            g.fillRect(x, y, this.gridCellSize, this.gridCellSize);
        }
        //draw blue diamonds
        for (int i = 0; i < num_blue_diamonds; i++) {
            x = this.gridX + blue_diamond_locs.get(i)[0] * this.gridCellSize;
            y = this.gridY + blue_diamond_locs.get(i)[1] * this.gridCellSize;
            g.setColor(Color.BLUE);
            g.fillRect(x, y, this.gridCellSize, this.gridCellSize);
        }
        //draw green diamonds
        for (int i = 0; i < num_green_diamonds; i++) {
            x = this.gridX + green_diamond_locs.get(i)[0] * this.gridCellSize;
            y = this.gridY + green_diamond_locs.get(i)[1] * this.gridCellSize;
            g.setColor(Color.GREEN);
            g.fillRect(x, y, this.gridCellSize, this.gridCellSize);
        }

        if (currentShape != null) {
            currentShape.draw((g2d));
        }
    }


    private class MyMouseListener extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
            if (currentShape != null && currentShape.contains(e.getPoint())) {
                offsetX = e.getX() - currentShape.getX();
                offsetY = e.getY() - currentShape.getY();
                isDragging = true;
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (isDragging) {
                // Handle the drop here
                setShape(currentShape); // Clear the current shape
                isDragging = false;
            }
        }
    }

    private class MyMouseMotionListener extends MouseAdapter {
        @Override
        public void mouseDragged(MouseEvent e) {
            if (isDragging) {
                currentShape.setLocation(e.getX() - offsetX, e.getY() - offsetY);
                repaint();
            }
        }
    }
}

public class SpiderWorldJPanel {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Spider World");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 1000);
        frame.setLayout(new BorderLayout());

        //load level info
        String levelFile = "./loadinfotest.txt";
        Map<String, Object> spiderWorldInfo = LoadInfo.readSpiderWorldInfo(levelFile);

        //draw the world
        SpiderPanel spiderPanel = new SpiderPanel(spiderWorldInfo);

        //draw blocks

        // Create a container panel to hold both components
        JPanel containerPanel = new JPanel(new BorderLayout());
        containerPanel.add(spiderPanel);
        frame.add(containerPanel);


        //DragDrop
        SwingUtilities.invokeLater(() -> {
            DragDrop d1 = new DragDrop();
            d1.setShape(new Blocks(1000, 50, 100, 50,"step", Color.GRAY));
            DragDrop d2 = new DragDrop();
            d2.setShape(new Blocks(1000, 110, 100, 50, "turn", Color.GRAY));
            DragDrop d3 = new DragDrop();
            d3.setShape(new Blocks(1000, 170, 100, 50, "paint", Color.RED));
            frame.add(d1);
            frame.add(d2);
            frame.add(d3);
        });
        frame.setVisible(true);
    }
}
