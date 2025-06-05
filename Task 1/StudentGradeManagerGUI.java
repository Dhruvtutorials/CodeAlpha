import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

class Student {
    String name;
    double score;

    public Student(String name, double score) {
        this.name = name;
        this.score = score;
    }
}

public class StudentGradeManagerGUI extends JFrame {
    private ArrayList<Student> students = new ArrayList<>();
    private DefaultTableModel tableModel;
    private JLabel averageLabel, highestLabel, lowestLabel;

    public StudentGradeManagerGUI() {
        setTitle("Student Grade Manager");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new FlowLayout());
        JTextField nameField = new JTextField(10);
        JTextField scoreField = new JTextField(5);
        JButton addButton = new JButton("Add Student");

        inputPanel.add(new JLabel("Name:"));
        inputPanel.add(nameField);
        inputPanel.add(new JLabel("Score:"));
        inputPanel.add(scoreField);
        inputPanel.add(addButton);

       
        tableModel = new DefaultTableModel(new String[]{"Name", "Score"}, 0);
        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

       
        JPanel summaryPanel = new JPanel(new GridLayout(3, 1));
        averageLabel = new JLabel("Average Score: ");
        highestLabel = new JLabel("Highest Score: ");
        lowestLabel = new JLabel("Lowest Score: ");
        summaryPanel.add(averageLabel);
        summaryPanel.add(highestLabel);
        summaryPanel.add(lowestLabel);

        add(inputPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(summaryPanel, BorderLayout.SOUTH);

      
        addButton.addActionListener(e -> {
            String name = nameField.getText().trim();
            String scoreText = scoreField.getText().trim();

            if (name.isEmpty() || scoreText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter both name and score.");
                return;
            }

            try {
                double score = Double.parseDouble(scoreText);
                Student student = new Student(name, score);
                students.add(student);
                tableModel.addRow(new Object[]{name, score});
                updateSummary();
                nameField.setText("");
                scoreField.setText("");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Score must be a number.");
            }
        });
    }

    private void updateSummary() {
        if (students.isEmpty()) return;

        double total = 0;
        double highest = Double.MIN_VALUE;
        double lowest = Double.MAX_VALUE;
        String highestName = "", lowestName = "";

        for (Student s : students) {
            total += s.score;
            if (s.score > highest) {
                highest = s.score;
                highestName = s.name;
            }
            if (s.score < lowest) {
                lowest = s.score;
                lowestName = s.name;
            }
        }

        double average = total / students.size();

        averageLabel.setText(String.format("Average Score: %.2f", average));
        highestLabel.setText(String.format("Highest Score: %.2f by %s", highest, highestName));
        lowestLabel.setText(String.format("Lowest Score: %.2f by %s", lowest, lowestName));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new StudentGradeManagerGUI().setVisible(true);
        });
    }
}
