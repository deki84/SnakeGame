import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class SnakeGame extends JPanel implements ActionListener, KeyListener {

    // Represents each segment of the snake and food
    private class Tile {
        int x;
        int y;

        Tile(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    // Dimensions and tile size for the board
    int boardWidth;
    int boardHeight;
    int tileSize = 25;

    // Snake components
    Tile snakeHead;
    ArrayList<Tile> snakeBody;

    // Food object
    Tile food;
    Random random;

    // Game control variables
    int velocityX; // Snake movement on x-axis
    int velocityY; // Snake movement on y-axis
    Timer gameLoop; // Controls game loop timing

    boolean gameOver = false; // Tracks game state

    // Initializes the game panel and game variables
    SnakeGame(int boardWidth, int boardHeight) {
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        setPreferredSize(new Dimension(this.boardWidth, this.boardHeight));
        setBackground(Color.black);
        addKeyListener(this); // Adds key listener for snake controls
        setFocusable(true);

        snakeHead = new Tile(5, 5); // Initial position of snake head
        snakeBody = new ArrayList<Tile>(); // Snake body starts empty

        food = new Tile(10, 10); // Initial food position
        random = new Random();
        placeFood(); // Randomly places food

        velocityX = 1; // Initial movement direction to the right
        velocityY = 0;

        // Starts the game loop with 200 ms delay
        gameLoop = new Timer(200, this);
        gameLoop.start();
    }

    // Paints the game components (grid, snake, food, score)
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    // Draws the grid, food, snake, and score on the screen
    public void draw(Graphics g) {
        // Draws grid lines to represent each tile
        for (int i = 0; i < boardWidth / tileSize; i++) {
            g.drawLine(i * tileSize, 0, i * tileSize, boardHeight); // Vertical lines
            g.drawLine(0, i * tileSize, boardWidth, i * tileSize); // Horizontal lines
        }

        // Draws the food in red
        g.setColor(Color.red);
        g.fill3DRect(food.x * tileSize, food.y * tileSize, tileSize, tileSize, true);

        // Draws the snake head in green
        g.setColor(Color.green);
        g.fill3DRect(snakeHead.x * tileSize, snakeHead.y * tileSize, tileSize, tileSize, true);

        // Draws each segment of the snake's body
        for (int i = 0; i < snakeBody.size(); i++) {
            Tile snakePart = snakeBody.get(i);
            g.fill3DRect(snakePart.x * tileSize, snakePart.y * tileSize, tileSize, tileSize, true);
        }

        // Draws score and game over message if applicable
        g.setFont(new Font("Arial", Font.PLAIN, 16));
        if (gameOver) {
            g.setColor(Color.red);
            g.drawString("Game Over: " + String.valueOf(snakeBody.size()), tileSize - 16, tileSize);
        } else {
            g.drawString("Score: " + String.valueOf(snakeBody.size()), tileSize - 16, tileSize);
        }
    }

    // Randomly places food on the board
    public void placeFood() {
        food.x = random.nextInt(boardWidth / tileSize);
        food.y = random.nextInt(boardHeight / tileSize);
    }

    // Controls snake movement and checks game conditions
    public void move() {
        // Checks if snake eats food, grows if true
        if (collision(snakeHead, food)) {
            snakeBody.add(new Tile(food.x, food.y)); // Adds new body segment
            placeFood(); // Places new food
        }

        // Moves each part of the snake's body to the position of the previous part
        for (int i = snakeBody.size() - 1; i >= 0; i--) {
            Tile snakePart = snakeBody.get(i);
            if (i == 0) { // The segment right after the head
                snakePart.x = snakeHead.x;
                snakePart.y = snakeHead.y;
            } else {
                Tile prevSnakePart = snakeBody.get(i - 1);
                snakePart.x = prevSnakePart.x;
                snakePart.y = prevSnakePart.y;
            }
        }

        // Updates snake head position
        snakeHead.x += velocityX;
        snakeHead.y += velocityY;

        // Checks for collisions with itself or walls
        for (int i = 0; i < snakeBody.size(); i++) {
            Tile snakePart = snakeBody.get(i);
            if (collision(snakeHead, snakePart)) { // Collision with body
                gameOver = true;
            }
        }

        // Checks if snake hits the border, ends game if true
        if (snakeHead.x * tileSize < 0 || snakeHead.x * tileSize >= boardWidth || // Left or right border
            snakeHead.y * tileSize < 0 || snakeHead.y * tileSize >= boardHeight) { // Top or bottom border
            gameOver = true;
        }
    }

    // Checks if two tiles are at the same position
    public boolean collision(Tile tile1, Tile tile2) {
        return tile1.x == tile2.x && tile1.y == tile2.y;
    }

    // Called by the timer at a regular interval, updating game state and rendering
    @Override
    public void actionPerformed(ActionEvent e) {
        move(); // Moves the snake
        repaint(); // Repaints the board
        if (gameOver) { // Stops the timer if game is over
            gameLoop.stop();
        }
    }

    // Responds to arrow keys to change snake direction
    @Override
    public void keyPressed(KeyEvent e) {
        // Sets direction based on key press, avoiding opposite direction moves
        if (e.getKeyCode() == KeyEvent.VK_UP && velocityY != 1) {
            velocityX = 0;
            velocityY = -1;
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN && velocityY != -1) {
            velocityX = 0;
            velocityY = 1;
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT && velocityX != 1) {
            velocityX = -1;
            velocityY = 0;
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT && velocityX != -1) {
            velocityX = 1;
            velocityY = 0;
        }
    }

    // Unused methods required by KeyListener interface
    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}
}
