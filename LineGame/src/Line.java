import javax.swing.JFrame;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

public class Line extends JFrame{
    private static BufferedImage[] rules;
    private static Point[] posis;

    private int stage;
    private boolean paint;

    private int level;
    private int power;
    private int score;

    private double hor;
    private double ver;

    private double newX;
    private double newY;
    private double rotate;
    private double speed;

    private BufferStrategy strategy;
    private BufferedImage img;
    private Graphics imG;
    private byte[] pixels;

    static{
        rules = new BufferedImage[9];
        posis = new Point[9];
        Graphics g;

        rules[0] = new BufferedImage(250, 55, 5);
        posis[0] = new Point(100, 150);
        g = rules[0].getGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(1, 1, 248, 53);
        g.setColor(Color.BLACK);
        g.drawString("Welcome to Line Smasher!", 10, 15);
        g.drawString("In this game, you must destroy blue areas.", 10, 30);
        g.drawString("by moving your character over them.", 10, 45);

        rules[1] = new BufferedImage(140, 55, 5);
        posis[1] = new Point(208, 231);
        g = rules[1].getGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(1, 1, 138, 53);
        g.setColor(Color.BLACK);
        g.drawString("This is your character.", 10, 15);
        g.drawString("It will clear any blue", 10, 30);
        g.drawString("areas beneath it.", 10, 45);

        rules[2] = new BufferedImage(160, 55, 5);
        posis[2] = new Point(209, 421);
        g = rules[2].getGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(1, 1, 158, 53);
        g.setColor(Color.BLACK);
        g.drawString("This is the controller.", 10, 15);
        g.drawString("Move your mouse around", 10, 30);
        g.drawString("it to change direction.", 10, 45);

        rules[3] = new BufferedImage(230, 40, 5);
        posis[3] = new Point(100, 150);
        g = rules[3].getGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(1, 1, 228, 38);
        g.setColor(Color.BLACK);
        g.drawString("If you move off the edge of the screen,", 10, 15);
        g.drawString("You will reappear on the other side.", 10, 30);

        rules[4] = new BufferedImage(240, 55, 5);
        posis[4] = new Point(190, 40);
        g = rules[4].getGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(1, 1, 238, 53);
        g.setColor(Color.BLACK);
        g.drawString("This bar is the power bar. When it fills up,", 10, 15);
        g.drawString("click to demolish a large area around you.", 10, 30);
        g.drawString("As you clear blue matter, this will fill up", 10, 45);

        rules[5] = new BufferedImage(190, 70, 5);
        posis[5] = new Point(270, 120);
        g = rules[5].getGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(1, 1, 188, 68);
        g.setColor(Color.BLACK);
        g.drawString("This bar is the danger bar.", 10, 15);
        g.drawString("It fills up as more blue appears.", 10, 30);
        g.drawString("When it fills up, the game ends.", 10, 45);
        g.drawString("Keep an eye on it!", 10, 60);

        rules[6] = new BufferedImage(130, 40, 5);
        posis[6] = new Point(300, 500);
        g = rules[6].getGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(1, 1, 128, 38);
        g.setColor(Color.BLACK);
        g.drawString("This is your score.", 10, 15);
        g.drawString("It increases with time.", 10, 30);

        rules[7] = new BufferedImage(300, 55, 5);
        posis[7] = new Point(70, 150);
        g = rules[7].getGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(1, 1, 298, 53);
        g.setColor(Color.BLACK);
        g.drawString("Every once in a while, a large blue area will appear.", 10, 15);
        g.drawString("This will raise the danger level significantly.", 10, 30);
        g.drawString("Clear them to gain a huge amount of power.", 10, 45);

        rules[8] = new BufferedImage(240, 40, 5);
        posis[8] = new Point(100, 150);
        g = rules[8].getGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(1, 1, 238, 38);
        g.setColor(Color.BLACK);
        g.drawString("That's it! Make sure your mouse is over", 10, 15);
        g.drawString("the controller, then click to start.", 10, 30);
    }

    public Line(){
        super();
        setSize(476, 564);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ML ml = new ML();
        addMouseListener(ml);
        stage = 0;
        hor = 200;
        ver = 200;

        newX = 200;
        newY = 200;
        rotate = Math.random() * Math.PI * 2;
        speed = Math.random() / 16;

        img = new BufferedImage(400, 400, 5);
        imG = img.getGraphics();
        pixels = ((DataBufferByte)img.getRaster().getDataBuffer()).getData();
        for(int i = 0; i < 1024; i++) {
            addPoint();
        }
        level = 0;
        for(int i = 0; i < 480000; i += 3) {
            if(pixels[i] == -1){
                level++;
            }
        }
        power = 0;
        score = 0;
        setVisible(true);
        createBufferStrategy(2);
        strategy = getBufferStrategy();
        paint = true;
    }

    private void addPoint() {
        imG.setColor(Color.BLUE);
        imG.fillOval((int)newX - 1, (int)newY - 1, 3, 3);
        newX = (newX + Math.cos(rotate) + 400) % 400;
        newY = (newY + Math.sin(rotate) + 400) % 400;
        rotate = (rotate + speed) % (Math.PI * 2);
        speed -= speed / 32 + (Math.random() - 0.5) / 64;
    }

    @Override
    public void paint(Graphics g){
        if(paint) {
            draw();
        }
    }

    private void draw(){
        Graphics g = strategy.getDrawGraphics();

        if(stage == 3){
            g.clearRect(209, 421, 160, 60);
        }
        if(stage == 6){
            g.clearRect(270, 100, 190, 60);
        }
        if(stage == 7){
            g.clearRect(300, 500, 130, 40);
        }

        g.drawImage(img, 8, 31, null);

        g.setColor(Color.WHITE);
        g.fillOval(169, 441, 80, 80);
        g.fillRect(413, 31, 25, 500);
        g.fillRect(443, 31, 25, 500);
        g.fillRect(413, 536, 55, 20);
        g.fillRect(3, 26, 405, 5);
        g.fillRect(408, 26, 5, 405);
        g.fillRect(3, 31, 5, 405);
        g.fillRect(8, 431, 405, 5);
        if(power < 1024){
            g.fillOval((int)hor + 3, (int)ver + 26, 11, 11);
            g.setColor(Color.RED);
        }
        else{
            g.setColor(Color.RED);
            g.fillOval((int)hor + 3, (int)ver + 26, 11, 11);
        }
        int val2 = (int)(power * 125 / 256.0);
        g.fillRect(413, 531 - val2, 25, val2);

        g.setColor(Color.BLUE);
        int val = (int)(level * 125 / 1024.0);
        g.fillRect(443, 531 - val, 25, val);

        g.setColor(Color.BLACK);
        g.drawRect(413, 31, 25, 500);
        g.drawRect(443, 31, 25, 500);
        g.drawOval(169, 441, 80, 80);
        g.drawString("" + score, 413, 551);
        g.drawRect(413, 536, 55, 20);

        Point mouse = getMousePosition();
        if(stage < 9){
            g.drawImage(rules[stage], posis[stage].x, posis[stage].y, null);
            g.drawString("START", 191, 488);
        }
        else if(stage == 10){
            g.setColor(Color.WHITE);
            g.fillRect(100, 200, 200, 100);
            g.setColor(Color.BLACK);
            g.drawRect(0, 0, 200, 100);
            g.setFont(new Font("Sans_Serif", Font.BOLD, 50));
            g.drawString("" + score, 110, 275);

        }
        else if(mouse != null && (mouse.x - 209) * (mouse.x - 209) + (mouse.y - 481) * (mouse.y - 481) < 1024){
            g.fillOval(mouse.x - 8, mouse.y - 8, 17, 17);
        }
        g.dispose();
        strategy.show();
    }

    public static void main(String[] args){
        new Line();
    }

    private class ML extends Thread implements MouseListener{
        @Override
        public void run(){
            int pause = 500;
            long start = System.currentTimeMillis();
            while(true) {
                Point mouse = getMousePosition();
                if(mouse != null) {
                    double direction = Math.atan2(mouse.y - 481, mouse.x - 209);
                    hor = (hor + Math.cos(direction) + 400) % 400;
                    ver = (ver + Math.sin(direction) + 400) % 400;

                    imG.setColor(Color.BLACK);
                    imG.fillOval((int) hor - 5, (int) ver - 5, 11, 11);

                    int count = 0;
                    for (int i = 0; i < 480000; i += 3) {
                        if (pixels[i] == -1) {
                            count++;
                        }
                    }

                    power += level - count;
                    if (power > 1024) {
                        power = 1024;
                    }

                    addPoint();
                    if(pause == 0){
                        imG.fillOval((int)(Math.random() * 380), (int)(Math.random() * 380),20, 20);
                        pause = 500;
                    }
                    else{
                        pause--;
                    }

                    level = 0;
                    for(int i = 0; i < 480000; i += 3) {
                        if(pixels[i] == -1){
                            level++;
                        }
                    }
                    if(level > 4096){
                        level = 4096;
                        stage = 10;
                        repaint();
                        break;
                    }
                    repaint();
                    try{
                        Thread.sleep(25 + start - System.currentTimeMillis());
                    }
                    catch(Exception ex){
                    }
                    start = System.currentTimeMillis();
                    score++;
                }
            }
        }
        @Override
        public void mouseClicked(MouseEvent e){
        }

        @Override
        public void mousePressed(MouseEvent e){
            Point mouse = getMousePosition();
            if(stage != 9 && mouse != null
                    && (mouse.x - 209) * (mouse.x - 209) + (mouse.y - 481) * (mouse.y - 481) < 1600){
                stage = 9;
                start();
            }
            else if(stage < 8) {
                stage++;
                repaint();
            }
            else if(power == 1024){
                power = 0;
                imG.setColor(Color.BLACK);
                imG.fillOval((int) hor - 50, (int) ver - 50, 101, 101);

            }
        }

        @Override
        public void mouseReleased(MouseEvent e){
        }

        @Override
        public void mouseEntered(MouseEvent e){
        }

        @Override
        public void mouseExited(MouseEvent e){
        }
    }
}