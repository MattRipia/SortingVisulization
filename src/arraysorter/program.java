package arraysorter;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

public class program extends JPanel implements ActionListener{
    
    public JButton sortButton, randomiseButton, stopButton;
    public DrawPanel drawPanel;
    public JComboBox comboBox;
    public JLabel sortMethodsLabel;
    public ArraySorter sorter = null;
    public int numElements = 265;
    Thread threadSorter;

    public program()
    {
        super(new BorderLayout());

        sorter = new ArraySorter(numElements);

        // create two seperate panels, add these to the frame in the center
        drawPanel = new DrawPanel();
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.GRAY);
        
        JPanel sidePanel = new JPanel();
        sidePanel.setBackground(Color.GRAY);

        // creating a button for sorting / randomising
        sortButton = new JButton("Sort");
        sortButton.addActionListener(this);
        sortButton.setSize(30, 50);
        buttonPanel.add(sortButton);

        // creating a button for adding balls
        randomiseButton = new JButton("Randomise");
        randomiseButton.addActionListener(this);
        randomiseButton.setSize(30, 50);
        buttonPanel.add(randomiseButton);
        
        sortMethodsLabel = new JLabel("Sort Method:");
        sidePanel.add(sortMethodsLabel);
        
        // creating a dropdownn list
        String[] sortMethods = {"Merge Sort","Quick Sort","Insertion Sort","Shell Sort","Cocktail Sort","Bubble Sort","Selection Sort","Bogo Sort"};
        comboBox = new JComboBox(sortMethods);
        comboBox.addActionListener(this);
        comboBox.setSize(120, 40);
        sidePanel.add(comboBox);
        

        add(buttonPanel,BorderLayout.SOUTH);
        add(drawPanel,BorderLayout.CENTER);
        add(sidePanel,BorderLayout.NORTH);

        // call actionPerformed method every 10ms using Swing timer
        Timer timer = new Timer(1, this);
        timer.start();
    }
   
    public void actionPerformed(ActionEvent e)
   {
       Object source = e.getSource();

        if(source == sortButton)
        {
            if(!sorter.running)
            {
                sorter.running = true;
                sorter.sortMethod = (String)comboBox.getSelectedItem();
                threadSorter = new Thread(sorter);
                threadSorter.start();
            }
        }
        
        if(source == randomiseButton)
        {
            sorter.running = false;
            sorter.randList(numElements);
        }
        
       // this gets called from our timer
       drawPanel.repaint();
   }

    private class DrawPanel extends JPanel
    {
        public DrawPanel()
        {
            setPreferredSize(new Dimension(1600, 600));
            setBackground(Color.GRAY);
        }

        @Override
        public void paintComponent(Graphics g)
        {
            super.paintComponent(g);
            
            int yOff = 605;
            int xOff = 10;
            g.setColor(Color.BLUE);
            for(Integer num : sorter.list)
            {
                g.fillRect(xOff, yOff - num, 5, num);
                xOff += 6;
            }
        }
   }
    
    public static void main(String[] args)
    {
        // creates a new frame for us to add components to
        JFrame frame = new JFrame("Sorter Visulization");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // gets the main content pane -> balls and buttons
        frame.getContentPane().add(new program());
        frame.pack();

        // sets the size and location of the frame in which the panel contains
        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension screenDimension = tk.getScreenSize();
        Dimension frameDimension = frame.getSize();
        frame.setLocation((screenDimension.width-frameDimension.width)/2, (screenDimension.height-frameDimension.height)/2);
        frame.setVisible(true);
        frame.setResizable(false);
    }
}
