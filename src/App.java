import javax.swing.*;

public class App {
    public static void main(String[] args) {
        // Define the dimensions for the game board
        int boardWidth = 602;
        int boardHeight = boardWidth; // Making the board a square

        // Create a new JFrame to hold the Snake game
        JFrame frame = new JFrame("Snake Game");
        frame.setVisible(true); // Make the frame visible
        frame.setSize(boardWidth, boardHeight); // Set the initial size of the frame
        frame.setLocationRelativeTo(null); // Center the frame on the screen
        frame.setResizable(false); // Prevent resizing to maintain game layout
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Close application on exit

        // Create an instance of the SnakeGame class, which contains the game logic and rendering
        SnakeGame snakeGame = new SnakeGame(boardWidth, boardHeight);
        frame.add(snakeGame); // Add the SnakeGame panel to the frame
        frame.pack(); // Adjust the frame size based on the preferred size of components

        // Request focus on the game panel so it can capture keyboard inputs immediately
        snakeGame.requestFocus();
    }
}
