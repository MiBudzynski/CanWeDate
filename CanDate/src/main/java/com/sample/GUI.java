package com.sample;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.border.Border;
import javax.swing.BoxLayout;
import javax.swing.JScrollPane;



import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class GUI extends JFrame implements ActionListener {

  private static final long serialVersionUID = 3378774311250822914L;

  private JButton button;
  private String[] possibleAnswers;
  
  public boolean ready = false;
  public int n = -1;
	
  public ArrayList<JRadioButton> options = new ArrayList<>();
   
  public GUI(String question) {
      super("Can We Date? - Result");
      
      /// QUESTION ///
      
      JLabel label = new JLabel(question);
      Font font = new Font("Courier", Font.BOLD,20);
      label.setFont(font);
      
      JPanel contentPanel = new JPanel();
      Border padding = BorderFactory.createEmptyBorder(10, 10, 10, 10);
      contentPanel.setBorder(padding);
      contentPanel.add(label);
      this.setContentPane(contentPanel);
      
      /// if IMAGE ///
      
      if (question.startsWith("<")) {
          question = question.substring(1, question.length()-1);
          String path = "src/main/resources/imgs/" + question + ".png";
          this.addImage(path);
      }
      
      pack();
      
      this.setDefaultCloseOperation(EXIT_ON_CLOSE);  
      this.centerWindow();
      this.setVisible(true);
  }
  
  public GUI(String question, String[] possibleAnswers) {
	    super("Can We Date? - Question");

	    this.possibleAnswers = possibleAnswers;

	    /// QUESTION ///
	    JPanel panel_Q = new JPanel();
	    JLabel label = new JLabel(question);
	    panel_Q.add(label);

	    /// LIST ///
	    JPanel panel_L = new JPanel(new FlowLayout(FlowLayout.LEFT)); // Arrange horizontally

	    for (String str_option : possibleAnswers) {
	        if (str_option.endsWith(".png")) {
	            try {
	                String resourcePath = "/imgs/" + str_option;
	                BufferedImage img = ImageIO.read(getClass().getResource(resourcePath));
	                ImageIcon resizedIcon = resizeImage(img, 100, 100); // Resize images

	                JRadioButton radioButton = new JRadioButton(" "); // Add placeholder text
	                radioButton.setIcon(resizedIcon);
	                radioButton.setPreferredSize(new Dimension(120, 120)); // Ensure consistent size
	                radioButton.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

	                options.add(radioButton);
	            } catch (IOException | NullPointerException e) {
	                e.printStackTrace();
	                options.add(new JRadioButton("[Image not found]"));
	            }
	        } else {
	            options.add(new JRadioButton(str_option));
	        }
	    }

	    ButtonGroup group = new ButtonGroup();
	    for (JRadioButton option : options) {
	        group.add(option);
	        panel_L.add(option);
	    }

	    /// CONTINUE ///
	    JPanel panel_B = new JPanel();
	    button = new JButton("Continue");
	    button.addActionListener(this);
	    panel_B.add(button);
	    JScrollPane scrollPane = new JScrollPane(panel_L, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	    this.add(scrollPane, BorderLayout.CENTER);

	    /// REGISTER ///
	    this.setLayout(new BorderLayout()); // Use BorderLayout for main layout
	    this.add(panel_Q, BorderLayout.PAGE_START);
	    this.add(panel_L, BorderLayout.CENTER);
	    this.add(panel_B, BorderLayout.PAGE_END);

	    pack(); // Adjust layout to fit components
	    this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	    this.centerWindow();
	    this.setVisible(true);
	}
  public GUI(String picturePath,int J) {
      super("Can We Date? - End State");

      this.setLayout(new BorderLayout());

      /// IMAGE ///
      try {
          String resourcePath = "/imgs/" + picturePath;
          BufferedImage img = ImageIO.read(getClass().getResource(resourcePath));
          ImageIcon resizedIcon = resizeImage(img, 400, 400); // Resize for end state

          JLabel imageLabel = new JLabel(resizedIcon);
          this.add(imageLabel, BorderLayout.CENTER);
      } catch (IOException | NullPointerException e) {
          e.printStackTrace();
          JLabel errorLabel = new JLabel("[Image not found]");
          this.add(errorLabel, BorderLayout.CENTER);
      }

      /// CLOSE BUTTON ///
      JPanel panel_B = new JPanel();
      JButton closeButton = new JButton("Close");
      closeButton.addActionListener(e -> System.exit(0)); // Close the application
      panel_B.add(closeButton);
      this.add(panel_B, BorderLayout.PAGE_END);

      pack(); // Adjust layout to fit components
      this.setDefaultCloseOperation(EXIT_ON_CLOSE);
      this.centerWindow();
      this.setVisible(true);
  }

  
  public void addImage(String path) {
      try {
          BufferedImage wPic = ImageIO.read(new File(path));
          JLabel wIcon = new JLabel(new ImageIcon(wPic));
          this.add(wIcon);
      } catch (IOException e) {
          e.printStackTrace();
      }
  }
  
  public void centerWindow() {
      Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
      int x = (int) ((dimension.getWidth() - this.getWidth()) / 2);
      int y = (int) ((dimension.getHeight() - this.getHeight()) / 2);
      this.setLocation(x, y);
  }

  public void waitForAnswer() {
      try {
          while (!this.ready) { TimeUnit.MILLISECONDS.sleep(25); }
      } catch (InterruptedException e) {
          e.printStackTrace();
      }
  }
  
  public String getAnswer() {
      if(this.n >= 0) {
          System.out.println(this.possibleAnswers[this.n]);
          return this.possibleAnswers[this.n];
      }
      return this.possibleAnswers[0]; //default answer
  }
  
  public void actionPerformed(ActionEvent e) {
      System.out.println(e.getSource());
      if (e.getSource() == button) {
          System.out.println("[GUI] clicked!");
          for (int counter = 0; counter < options.size(); counter++) {               
              JRadioButton current = options.get(counter);
              if (current.isSelected()) {
                  this.n = counter;
                  break;
              }
          }   
          this.ready = true;
          this.dispose();
      }
  }
  private ImageIcon resizeImage(BufferedImage image, int width, int height) {
	    Image scaledImage = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
	    return new ImageIcon(scaledImage);
	}


}
