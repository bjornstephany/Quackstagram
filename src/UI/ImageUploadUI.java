package src.UI;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import src.model.PostStore;
import src.model.SessionStore;
import src.model.User;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;

public class ImageUploadUI extends JFrame {

	private static final int WIDTH = 300;
	private static final int HEIGHT = 500;
	private static final int NAV_ICON_SIZE = 20; // Size for navigation icons
	private JLabel imagePreviewLabel;
	private JTextArea bioTextArea;
	private JButton uploadButton;
	private JButton saveButton;
	private boolean imageUploaded = false;
	private final SessionStore sessionStore = new SessionStore();
	private final PostStore postStore = new PostStore();

	public ImageUploadUI() {
		setTitle("Upload Image");
		setSize(WIDTH, HEIGHT);
		setMinimumSize(new Dimension(WIDTH, HEIGHT));
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLayout(new BorderLayout());
		initializeUI();
	}

	private void initializeUI() {
		JPanel headerPanel = createHeaderPanel(); // Reuse the createHeaderPanel method
		JPanel navigationPanel = createNavigationPanel(); // Reuse the createNavigationPanel method

		// Main content panel
		JPanel contentPanel = new JPanel();
		contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));

		// Image preview
		imagePreviewLabel = new JLabel();
		imagePreviewLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		imagePreviewLabel.setPreferredSize(new Dimension(WIDTH, HEIGHT / 3));

		// Set an initial empty icon to the imagePreviewLabel
		ImageIcon emptyImageIcon = new ImageIcon();
		imagePreviewLabel.setIcon(emptyImageIcon);

		contentPanel.add(imagePreviewLabel);

		// Bio text area
		bioTextArea = new JTextArea("Enter a caption");
		bioTextArea.setAlignmentX(Component.CENTER_ALIGNMENT);
		bioTextArea.setLineWrap(true);
		bioTextArea.setWrapStyleWord(true);
		JScrollPane bioScrollPane = new JScrollPane(bioTextArea);
		bioScrollPane.setPreferredSize(new Dimension(WIDTH - 50, HEIGHT / 6));
		contentPanel.add(bioScrollPane);

		// Upload button
		uploadButton = new JButton("Upload Image");
		uploadButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		uploadButton.addActionListener(this::uploadAction);
		contentPanel.add(uploadButton);

		// Save button (for bio)
		saveButton = new JButton("Save Caption");
		saveButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		saveButton.addActionListener(this::saveBioAction);

		// Add panels to frame
		add(headerPanel, BorderLayout.NORTH);
		add(contentPanel, BorderLayout.CENTER);
		add(navigationPanel, BorderLayout.SOUTH);
	}

	private void uploadAction(ActionEvent event) {
		System.out.println("Test");
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogTitle("Select an image file");
		fileChooser.setAcceptAllFileFilterUsed(false);
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Image files", "png", "jpg", "jpeg");
		fileChooser.addChoosableFileFilter(filter);

		int returnValue = fileChooser.showOpenDialog(null);
		if (returnValue == JFileChooser.APPROVE_OPTION) {
			File selectedFile = fileChooser.getSelectedFile();
			String username = sessionStore.getLoggedInUsername();

			postStore.saveUploadedPost(username, selectedFile, bioTextArea.getText());

			imageUploaded = true;
			JOptionPane.showMessageDialog(this, "Image uploaded successfully.");
		}
	}

	private void saveBioAction(ActionEvent event) {
		// Here you would handle saving the bio text
		String bioText = bioTextArea.getText();
		// For example, save the bio text to a file or database
		JOptionPane.showMessageDialog(this, "Caption saved: " + bioText);
	}

	private JPanel createHeaderPanel() {

		// Header Panel (reuse from InstagramProfileUI or customize for home page)
		// Header with the Register label
		JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		headerPanel.setBackground(new Color(51, 51, 51)); // Set a darker background for the header
		JLabel lblRegister = new JLabel(" Upload Image 🐥");
		lblRegister.setFont(new Font("Arial", Font.BOLD, 16));
		lblRegister.setForeground(Color.WHITE); // Set the text color to white
		headerPanel.add(lblRegister);
		headerPanel.setPreferredSize(new Dimension(WIDTH, 40)); // Give the header a fixed height
		return headerPanel;
	}

	private JPanel createNavigationPanel() {
		// Create and return the navigation panel
		// Navigation Bar
		JPanel navigationPanel = new JPanel();
		navigationPanel.setBackground(new Color(249, 249, 249));
		navigationPanel.setLayout(new BoxLayout(navigationPanel, BoxLayout.X_AXIS));
		navigationPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		navigationPanel.add(createIconButton("img/icons/home.png", "home"));
		navigationPanel.add(Box.createHorizontalGlue());
		navigationPanel.add(createIconButton("img/icons/search.png", "explore"));
		navigationPanel.add(Box.createHorizontalGlue());
		navigationPanel.add(createIconButton("img/icons/add.png", " "));
		navigationPanel.add(Box.createHorizontalGlue());
		navigationPanel.add(createIconButton("img/icons/heart.png", "notification"));
		navigationPanel.add(Box.createHorizontalGlue());
		navigationPanel.add(createIconButton("img/icons/profile.png", "profile"));

		return navigationPanel;
	}

	private JButton createIconButton(String iconPath, String buttonType) {
		ImageIcon iconOriginal = new ImageIcon(iconPath);
		Image iconScaled = iconOriginal.getImage().getScaledInstance(NAV_ICON_SIZE, NAV_ICON_SIZE, Image.SCALE_SMOOTH);
		JButton button = new JButton(new ImageIcon(iconScaled));
		button.setBorder(BorderFactory.createEmptyBorder());
		button.setContentAreaFilled(false);

		// Define actions based on button type
		if ("home".equals(buttonType)) {
			button.addActionListener(e -> openHomeUI());
		} else if ("profile".equals(buttonType)) {
			button.addActionListener(e -> openProfileUI());
		} else if ("notification".equals(buttonType)) {
			button.addActionListener(e -> notificationsUI());
		} else if ("explore".equals(buttonType)) {
			button.addActionListener(e -> exploreUI());
		}
		return button;

	}

	private void openProfileUI() {
		this.dispose();

		String loggedInUsername = sessionStore.getLoggedInUsername();
		User user = new User(loggedInUsername);

		QuackstagramProfileUI profileUI = new QuackstagramProfileUI(user);
		profileUI.setVisible(true);
	}

	private void notificationsUI() {
		// Open InstagramProfileUI frame
		this.dispose();
		NotificationsUI notificationsUI = new NotificationsUI();
		notificationsUI.setVisible(true);
	}

	private void openHomeUI() {
		// Open InstagramProfileUI frame
		this.dispose();
		HomeFeedUI homeUI = new HomeFeedUI();
		homeUI.setVisible(true);
	}

	private void exploreUI() {
		// Open InstagramProfileUI frame
		this.dispose();
		ExploreUI explore = new ExploreUI();
		explore.setVisible(true);
	}

}
