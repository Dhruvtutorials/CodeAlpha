import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Map;

public class ChatBotGUI extends JFrame {
    private JTextArea chatArea;
    private JTextField inputField;
    private Map<String, String> knowledgeBase;

    public ChatBotGUI() {
        setTitle("Java ChatBot");
        setSize(500, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        
        chatArea = new JTextArea();
        chatArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(chatArea);

      
        inputField = new JTextField();
        inputField.addActionListener(e -> handleInput());

        add(scrollPane, BorderLayout.CENTER);
        add(inputField, BorderLayout.SOUTH);

        initializeKnowledgeBase();
        chatArea.append("Bot: Hello! How can I help you today?\n");
    }

    private void initializeKnowledgeBase() {
        knowledgeBase = new HashMap<>();

        knowledgeBase.put("hello", "Hi there! How can I assist you?");
        knowledgeBase.put("hi", "Hello! How can I help?");
        knowledgeBase.put("how are you", "I'm just a bot, but I'm here to help!");
        knowledgeBase.put("your name", "I'm JavaBot, your virtual assistant.");
        knowledgeBase.put("help", "You can ask me about courses, time, or general questions.");
        knowledgeBase.put("course", "We offer Java, Python, and Web Development courses.");
        knowledgeBase.put("time", "I'm available 24/7 to help you.");
        knowledgeBase.put("bye", "Goodbye! Have a great day.");
    }

    private void handleInput() {
        String userInput = inputField.getText().toLowerCase().trim();
        chatArea.append("You: " + userInput + "\n");
        inputField.setText("");

        String response = getResponse(userInput);
        chatArea.append("Bot: " + response + "\n");
    }

    private String getResponse(String input) {
        for (String key : knowledgeBase.keySet()) {
            if (input.contains(key)) {
                return knowledgeBase.get(key);
            }
        }
        return "I'm sorry, I don't understand that. Can you please rephrase?";
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ChatBotGUI().setVisible(true));
    }
}
