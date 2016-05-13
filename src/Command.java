import jdk.nashorn.internal.scripts.JO;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.PrintWriter;

/**
 * Created by sean on 5/12/16.
 */
public class Command extends JFrame {

    JTextField network, wildcard, area, processID, hostName;
    JTextArea output;
    JList list1;
    JPanel panel1, panel2,panel3;
    JLabel ospfLabel, eigrpLabel
            , networkLabel, wildcardLabel, areaLabel, processIDLabel, hostNameLabel;


    public Command(){
        setTitle("Command Output");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setSize(400,400);

        buildPanel1();
        buildPanel2();
        buildPanel3();

        add(panel1,BorderLayout.WEST);
        add(panel2, BorderLayout.CENTER);
        add(panel3, BorderLayout.SOUTH);

        setVisible(true);

    }

    public void buildPanel1() {
        panel1 = new JPanel();
        String[] items = {"OSPF", "EIGRP"};

        list1 = new JList(items);

        list1.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        list1.addListSelectionListener(new ListSelection());


        panel1.add(new JScrollPane(list1));

    }

    public static void main(String[] args) {
        new Command();

    }

    public void buildPanel2(){
        panel2 = new JPanel(new GridLayout(5,2));

        network = new JTextField();
        wildcard = new JTextField();
        area = new JTextField();
        processID = new JTextField();

        area.addActionListener(new UserHitsEnterOnArea());

        networkLabel = new JLabel("Network: ");
        wildcardLabel = new JLabel("Wildcard Mask: ");

        areaLabel = new JLabel("Area: ");
        area.setText("0");

        processIDLabel = new JLabel("Process ID: "); //range dependent, actionlistener
        processID.setText("1");

        panel2.setVisible(false);

        hostName = new JTextField();
        hostNameLabel = new JLabel("Hostname (default:network):  ");

        panel2.add(hostNameLabel);
        panel2.add(hostName);
        panel2.add(processIDLabel);
        panel2.add(processID);
        panel2.add(networkLabel);
        panel2.add(network);
        panel2.add(wildcardLabel);
        panel2.add(wildcard);
        panel2.add(areaLabel);
        panel2.add(area);

    }

    public void buildPanel3(){
        panel3 = new JPanel();

        output = new JTextArea();
        output.setPreferredSize(new Dimension(400,100));
        output.setEditable(false);

        panel3.add(output);


    }

    private class ListSelection implements ListSelectionListener{
        public void valueChanged(ListSelectionEvent event) {

            panel2.setVisible(false);
            output.setText("");


            int index = list1.getSelectedIndex();

            if(index == 0){
                panel2.setVisible(true);

                //add events for ospf
                //ask for network, wildcard mask, areaZ¸

            }
            else{
                //add events for eigrp
                //disp

            }
        }
    }

    private class UserHitsEnterOnArea implements ActionListener{
        public void actionPerformed(ActionEvent e) {

            String fileName;
            try{
                int processIDvalue = Integer.parseInt(processID.getText());

                if(processIDvalue >= 1 && processIDvalue <= 65535){
                    String string = new String("en\nconf t\nrouter ospf " + processID.getText() + "\nnetwork " + network.getText()
                            + " " + wildcard.getText() + " area " + area.getText());
                    output.setText(string);

                    StringSelection selection = new StringSelection(string);
                    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                    clipboard.setContents(selection,selection);

                        if (hostName.getText().isEmpty()) {
                            fileName = network.getText(); //TODO Make required field for network
                        } else {
                            fileName = hostName.getText();
                        }

                    PrintWriter out = new PrintWriter(new FileWriter(fileName+".txt",true));
                    out.println(string);
                    out.println("");
                    out.close();




                }
                else
                {
                    JOptionPane.showMessageDialog(null, "Please enter a value between 1-65535");
                }
            }
            catch(Exception ex){
                JOptionPane.showMessageDialog(null, "Please enter a value between 1-65535");
            }



        }
    }

}
