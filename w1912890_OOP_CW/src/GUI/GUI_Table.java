package GUI;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.util.Scanner;

public class GUI_Table extends JFrame {
    //Initializing the JFrame components
    JButton sortFirstNameButton, sortLastNameButton, refreshButton, goBackButton;
    JTable table;

    //Create a menu object
    GUI_Menu menuObj = new GUI_Menu();

    public GUI_Table() {
        //Initialize an event handler
        buttonHandler handler = new buttonHandler();
        this.setLayout(null);
        this.setTitle("View Doctors");

        //Adding the Back to Menu button
        goBackButton = new JButton("Back to Menu");
        addButton(goBackButton,100,120, handler, Color.red, Color.decode("#FFB7B7"));
        menuObj.btnHoverEffect(goBackButton, Color.red, Color.decode("#FFA6A6"), Color.red);

        //Creating the columns for the table
        String[] columnNames = {"First Name", "Last Name", "Date of Birth", "Mobile Number", "Medical Licence Number", "Specialization"};
        Object[][] data = tableData();

        //Checking whether there are doctors in the center or not
        if (data[0][0] == null){
            addLabel("Oops...Currently there are no doctors in the center!",560, 367, 500, 50,"Chalkboard",0,16);
            addImage("notFound.jpg",470, -30, 600, 500);

        }else {
            addLabel("Doctors in the Center",610, 30, 400,50,"SansSerif",1,22);

            //Adding the table rows
            table = new JTable();
            TableModel model = new DefaultTableModel(data, columnNames);
            table.setModel(model);
            JScrollPane scrollPane = new JScrollPane(table);
            table.setAutoCreateRowSorter(true);
            table.setGridColor(Color.black);
            table.setRowHeight(30);
            scrollPane.setBounds(0, 110, 1440, 322);
            JTableHeader header = table.getTableHeader();
            header.setFont(new Font("Serif", Font.BOLD, 15));
            header.setBackground(Color.decode("#E4EEFF"));
            ((DefaultTableCellRenderer)table.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);
            ((DefaultTableCellRenderer)table.getDefaultRenderer(Object.class)).setHorizontalAlignment( SwingConstants.CENTER );
            this.add(scrollPane);

            //Adding the sorting JButtons
            sortFirstNameButton = new JButton("Sort by First Name");
            sortLastNameButton = new JButton("Sort by Last Name");
            refreshButton = new JButton("Refresh to Default");
            addButton(sortFirstNameButton,800,150, handler, Color.blue, Color.decode("#E4EEFF"));
            addButton(sortLastNameButton,1000,150, handler, Color.blue, Color.decode("#E4EEFF"));
            addButton(refreshButton,1200,150, handler, Color.blue, Color.decode("#E4EEFF"));

            //Adding hover effects to the sorting buttons
            menuObj.btnHoverEffect(sortFirstNameButton, Color.blue, Color.decode("#CDE0FF"), Color.blue);
            menuObj.btnHoverEffect(sortLastNameButton, Color.blue, Color.decode("#CDE0FF"), Color.blue);
            menuObj.btnHoverEffect(refreshButton, Color.blue, Color.decode("#CDE0FF"), Color.blue);
        }
        //Adding images
        addImage("logo.png",50, 0, 200, 100);
        addImage("background.jpg",0, -20, 1440, 500);
        addImage("background.jpg",440, -20, 1440, 500);
        addImage("background.jpg",840, -20, 1440, 500);
        addImage("background.jpg",0,200,1440,500);
        addImage("background.jpg",440,200,1440,500);
        addImage("background.jpg",840,200,1440,500);
    }

    //This method is to add and place the JButtons in the JFrame
    public void addButton(JButton btn, int x, int width, buttonHandler handler, Color borderClr, Color bgClr){
        btn.addActionListener(handler);
        btn.setBounds(x,480,width,40);
        btn.setBorder(BorderFactory.createLineBorder(borderClr));
        btn.setBackground(bgClr);
        btn.setOpaque(true);
        this.add(btn);
    }

    //This method is to add and place the images in the JFrame
    public void addImage(String fileName, int x, int y, int width, int height){
        JLabel label = new JLabel();
        label.setIcon(new ImageIcon(fileName));
        label.setBounds(x, y, width, height);
        this.add(label);
    }

    //This method is to add and place the JLabels in the JFrame
    public void addLabel(String text, int x, int y, int width, int height, String fontName, int font, int fontSize){
        JLabel label = new JLabel(text);
        label.setBounds(x, y, width, height);
        label.setFont(new Font(fontName, font, fontSize));
        this.add(label);
    }

    //This method counts the number of lines in the doctor text file
    public int lineCount(){
        int count = 0;
        try {
            File file = new File("GUI_Doctor_Data.txt");   //Opening the text file.
            Scanner read_File = new Scanner(file);    //Reads the content in the text file.
            while (read_File.hasNext()){
                read_File.nextLine();
                count++;
            }
            return count;
        } catch (FileNotFoundException e) {
            return count;
        }
    }

    //This method loads the doctors from the doctor text file
    public Object[][] tableData(){
        int lineCount = lineCount();
        Object[][] data = new Object[10][6];
        if (lineCount != 0) {
            data = new Object[lineCount/6][6];
        }
        try {
            File file = new File("GUI_Doctor_Data.txt");   //Opening the text file.
            Scanner read_File = new Scanner(file);    //Reads the content in the text file.

            int array = 0;
            while (read_File.hasNext()){
                int subArray = 0;
                data[array][subArray] = read_File.nextLine();
                data[array][subArray + 1] = read_File.nextLine();
                data[array][subArray + 2] = LocalDate.parse(read_File.nextLine());
                data[array][subArray + 3] = read_File.nextLine();
                data[array][subArray + 4] = read_File.nextLine();
                data[array][subArray + 5] = read_File.nextLine();
                array++;
            }
            return data;
        } catch (FileNotFoundException e) {
            return data;
        }
    }

    private class buttonHandler implements ActionListener {
        public void actionPerformed(ActionEvent evt) {
            JButton actionSource = (JButton) evt.getSource();

            if (actionSource.equals(sortFirstNameButton)) {
                table.getRowSorter().toggleSortOrder(0);
            } else if (actionSource.equals(sortLastNameButton)) {
                table.getRowSorter().toggleSortOrder(1);
            } else if (actionSource.equals(refreshButton)) {
                guiTable();
                dispose();
            } else if (actionSource.equals(goBackButton)) {
                GUI_Menu menu = new GUI_Menu();
                menu.guiMenu();
                dispose();
            }
        }
    }

    public void guiTable() {
        GUI_Table table = new GUI_Table();
        table.setVisible(true);
        table.setSize(1440, 610);
        table.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
