package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.Objects;

import static javax.swing.JOptionPane.showMessageDialog;

public class GUI_Menu extends JFrame {
    //Initializing the JFrame components
    JButton viewButton, consultButton, historyButton, exitButton, clearButton;

    public GUI_Menu() {
        //Initialize an event handler
        MenuHandler handler = new MenuHandler();
        this.setLayout(null);
        this.setTitle("Westminster Skin Consultation Manager");

        //Adding JLabels
        addLabel("Welcome to the",190,50,500,30,"American Typewriter",0);
        addLabel("Westminster Skin Consultation Center",110,75,500,30,"American Typewriter",2);
        addLabel("MENU",225,120,150,50,"SansSerif",1);

        //Adding JButtons
        viewButton = new JButton("View Doctors");
        addButton(viewButton, handler, 175, 190, Color.blue, Color.decode("#E4EEFF"));

        consultButton = new JButton("Consult a Doctor");
        addButton(consultButton, handler, 175, 270, Color.blue, Color.decode("#E4EEFF"));

        historyButton = new JButton("Consultation History");
        addButton(historyButton, handler, 175, 350, Color.blue, Color.decode("#E4EEFF"));

        exitButton = new JButton("Exit from GUI");
        addButton(exitButton, handler, 175, 430, Color.red, Color.decode("#FFB7B7"));

        clearButton = new JButton("Clear Data");
        clearButton.addActionListener(handler);
        clearButton.setBounds(200, 563, 100, 35);
        this.add(clearButton, BorderLayout.CENTER);

        //Adding images
        addImage("logo.png", 310,370,1000,421);
        addImage("menu.jpg", 0,0,500,700);

        //Adding hover effects to buttons
        btnHoverEffect(viewButton, Color.blue, Color.decode("#CDE0FF"), Color.blue);
        btnHoverEffect(consultButton, Color.blue, Color.decode("#CDE0FF"), Color.blue);
        btnHoverEffect(historyButton, Color.blue, Color.decode("#CDE0FF"), Color.blue);
        btnHoverEffect(exitButton, Color.red, Color.decode("#FFA6A6"), Color.red);
    }

    private class MenuHandler implements ActionListener {
        public void actionPerformed(ActionEvent evt) {
            JButton actionSource = (JButton) evt.getSource();

            if (actionSource.equals(viewButton)) {
                GUI_Table table = new GUI_Table();
                table.guiTable();
                dispose();
            } else if (actionSource.equals(consultButton)) {
                GUI_Consult consult = new GUI_Consult();
                consult.guiConsult();
                dispose();
            } else if (actionSource.equals(historyButton)) {
                GUI_Consult_History history = new GUI_Consult_History();
                history.consultHistory();
                dispose();
            } else if (actionSource.equals(exitButton)) {
                dispose();
            } else if (actionSource.equals(clearButton)) {
                //Deleting all the GUI details
                deleteTextFiles("GUI_Doctor_Data.txt");
                deleteTextFiles("GUI_Consultation_Data.txt");
                deleteTextFiles("Patient_Note_Names.txt");
                deleteFiles(new File("/Users/lakinduindipa/Desktop/w1912890_OOP_CW/Patient_Images"));
                deleteFiles(new File("/Users/lakinduindipa/Desktop/w1912890_OOP_CW/Patient_Texts"));
                showMessageDialog(null, "NOTE:\nAll the GUI data has been cleared!", "For Your Information", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    //This method adds hover effects to buttons
    public void btnHoverEffect(JButton btn, Color textClr, Color bgClr, Color borderClr){
        btn.addMouseListener(new MouseAdapter() {
            final Color color1 = btn.getForeground();
            final Color color2 = btn.getBackground();
            final Color color3 = borderClr;
            public void mouseEntered(MouseEvent me) {
                btn.setForeground(textClr);
                btn.setBackground(bgClr);
                btn.setBorder(BorderFactory.createLineBorder(Color.white));
            }
            public void mouseExited(MouseEvent me) {
                btn.setForeground(color1);
                btn.setBackground(color2);
                btn.setBorder(BorderFactory.createLineBorder(color3));
            }
        });
    }

    //This method is to add and place the JLabels in the JFrame
    public void addLabel(String text, int x, int y, int width, int height, String fontName, int font){
        JLabel label = new JLabel(text);
        label.setBounds(x,y,width,height);
        label.setFont(new Font(fontName, font, 16));
        this.add(label, BorderLayout.CENTER);
    }

    //This method is to add and place the JButtons in the JFrame
    public void addButton(JButton btn, MenuHandler handler, int x, int y, Color borderClr, Color bgClr){
        btn.addActionListener(handler);
        btn.setBounds(x, y, 150, 60);
        btn.setBorder(BorderFactory.createLineBorder(borderClr));
        btn.setBackground(bgClr);
        btn.setOpaque(true);
        this.add(btn, BorderLayout.CENTER);
    }

    //This method is to add and place the images in the JFrame
    public void addImage(String fileName, int x, int y, int width, int height){
        JLabel label = new JLabel();
        label.setIcon(new ImageIcon(fileName));
        label.setBounds(x, y, width, height);
        this.add(label);
    }

    //This method is to delete the selected text files when the user pressed "Clear Data" button
    public void deleteTextFiles(String fileName){
        File file = new File(fileName);
        if (file.delete()){
            System.out.println();
        }
    }

    //This method is to delete the selected files when the user pressed "Clear Data" button
    public void deleteFiles(File dir){
        boolean isTrue = false;
        for (File file : Objects.requireNonNull(dir.listFiles())) {
            if (!file.isDirectory()) {
                isTrue = file.delete();
            }
        }
        if (isTrue){
            System.out.println();
        }
    }

    public void guiMenu() {
        GUI_Menu menu = new GUI_Menu();
        menu.setVisible(true);
        menu.setSize(500, 720);
        menu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}