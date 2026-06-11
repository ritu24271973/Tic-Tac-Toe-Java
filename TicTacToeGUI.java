import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class TicTacToeGUI extends JFrame implements ActionListener {
    private JButton[][] buttons = new JButton[3][3];
    private char currentPlayer;
    private JLabel statusLabel;
    private String playerXName, playerOName;
    private int xWins = 0, oWins = 0;
    private int totalSets;

    // 2. Constructor - Creating the Game Window
    public TicTacToeGUI() {
        setTitle("Tic Tac Toe");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Background color
        getContentPane().setBackground(Color.LIGHT_GRAY);

        // Step 1: Get Player Names
        playerXName = JOptionPane.showInputDialog(this, "Enter Player X Name:");
        if (playerXName == null || playerXName.trim().isEmpty()) playerXName = "Player X";

        playerOName = JOptionPane.showInputDialog(this, "Enter Player O Name:");
        if (playerOName == null || playerOName.trim().isEmpty()) playerOName = "Player O";

        // Step 2: Ask how many matches to play
        String matchInput = JOptionPane.showInputDialog(this, "How many matches do you want to play?");
        try {
            totalSets = Integer.parseInt(matchInput);
            if (totalSets <= 0) totalSets = 3;
        } catch (Exception e) {
            totalSets = 3;
        }

        currentPlayer = 'X';

        // Status Label
        statusLabel = new JLabel(playerXName + "'s Turn (X)", SwingConstants.CENTER);
        statusLabel.setFont(new Font("Arial", Font.BOLD, 22));
        statusLabel.setForeground(Color.BLACK);
        add(statusLabel, BorderLayout.NORTH);

        // Board Panel
        JPanel boardPanel = new JPanel(new GridLayout(3, 3));
        boardPanel.setBackground(Color.LIGHT_GRAY);
        Font btnFont = new Font("Arial", Font.BOLD, 60);

        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                buttons[r][c] = new JButton("");
                buttons[r][c].setFont(btnFont);
                buttons[r][c].setForeground(Color.BLACK);
                buttons[r][c].setBackground(Color.LIGHT_GRAY);
                buttons[r][c].setFocusPainted(false);
                buttons[r][c].addActionListener(this);
                boardPanel.add(buttons[r][c]);
            }
        }

        add(boardPanel, BorderLayout.CENTER);

        // Reset Button
        JButton resetBtn = new JButton("Reset Game");
        resetBtn.setFont(new Font("Arial", Font.BOLD, 18));
        resetBtn.setForeground(Color.BLACK);
        resetBtn.setBackground(Color.GRAY);
        resetBtn.setFocusPainted(false);
        resetBtn.addActionListener(e -> resetGame());
        add(resetBtn, BorderLayout.SOUTH);

        setVisible(true);
    }

    // 3. Handling Player Moves
    @Override
    public void actionPerformed(ActionEvent e) {
        JButton clickedBtn = (JButton) e.getSource();

        if (!clickedBtn.getText().equals("")) {
            return;
        }

        clickedBtn.setText(String.valueOf(currentPlayer));

        // 4. Checking Win Condition
        if (checkWin()) {
            String winnerName = (currentPlayer == 'X') ? playerXName : playerOName;
            if (currentPlayer == 'X') xWins++; else oWins++;
            statusLabel.setText(winnerName + " Wins this Round!");
            disableBoard();

            // Check if overall winner
            if (xWins == totalSets || oWins == totalSets) {
                String finalWinner = (xWins > oWins) ? playerXName : playerOName;
                JOptionPane.showMessageDialog(this, "🏆 " + finalWinner + " Wins the Game!",
                        "Game Over", JOptionPane.INFORMATION_MESSAGE);
                xWins = 0;
                oWins = 0;
            } else {
                JOptionPane.showMessageDialog(this, "Set Result:\n" +
                        playerXName + ": " + xWins + " | " + playerOName + ": " + oWins +
                        "\nSets to Win: " + totalSets);
                resetGame();
            }
            return;
        }

        // 5. Checking for a Draw
        if (checkDraw()) {
            statusLabel.setText("It's a Draw!");
            JOptionPane.showMessageDialog(this, "Draw! Try Again!");
            resetGame();
            return;
        }

        // Switch player
        currentPlayer = (currentPlayer == 'X') ? 'O' : 'X';
        String playerName = (currentPlayer == 'X') ? playerXName : playerOName;
        statusLabel.setText(playerName + "'s Turn (" + currentPlayer + ")");
    }

    // 4. Checking Win Condition
    private boolean checkWin() {
        for (int r = 0; r < 3; r++) {
            if (!buttons[r][0].getText().equals("") &&
                    buttons[r][0].getText().equals(buttons[r][1].getText()) &&
                    buttons[r][1].getText().equals(buttons[r][2].getText())) {
                highlightWinningButtons(buttons[r][0], buttons[r][1], buttons[r][2]);
                return true;
            }
        }
        for (int c = 0; c < 3; c++) {
            if (!buttons[0][c].getText().equals("") &&
                    buttons[0][c].getText().equals(buttons[1][c].getText()) &&
                    buttons[1][c].getText().equals(buttons[2][c].getText())) {
                highlightWinningButtons(buttons[0][c], buttons[1][c], buttons[2][c]);
                return true;
            }
        }
        if (!buttons[0][0].getText().equals("") &&
                buttons[0][0].getText().equals(buttons[1][1].getText()) &&
                buttons[1][1].getText().equals(buttons[2][2].getText())) {
            highlightWinningButtons(buttons[0][0], buttons[1][1], buttons[2][2]);
            return true;
        }
        if (!buttons[0][2].getText().equals("") &&
                buttons[0][2].getText().equals(buttons[1][1].getText()) &&
                buttons[1][1].getText().equals(buttons[2][0].getText())) {
            highlightWinningButtons(buttons[0][2], buttons[1][1], buttons[2][0]);
            return true;
        }
        return false;
    }

    // Highlight winning buttons in green
    private void highlightWinningButtons(JButton b1, JButton b2, JButton b3) {
        b1.setBackground(Color.GREEN);
        b2.setBackground(Color.GREEN);
        b3.setBackground(Color.GREEN);
    }

    // 5. Checking for a Draw
    private boolean checkDraw() {
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                if (buttons[r][c].getText().equals("")) {
                    return false;
                }
            }
        }
        return true;
    }

    // 6. Resetting the Game
    private void resetGame() {
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                buttons[r][c].setText("");
                buttons[r][c].setEnabled(true);
                buttons[r][c].setBackground(Color.LIGHT_GRAY);
            }
        }
        currentPlayer = 'X';
        statusLabel.setText(playerXName + "'s Turn (X)");
    }

    // Disable board after win
    private void disableBoard() {
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                buttons[r][c].setEnabled(false);
            }
        }
    }

    // 7. Main Method
    public static void main(String[] args) {
        new TicTacToeGUI();
    }
}