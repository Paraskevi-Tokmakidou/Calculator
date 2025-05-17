package com.mycompany.calculator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.*;
import java.io.*;

public class Calc extends JFrame {

    private JLabel label = new JLabel("  ");
    private final JPanel panel, operation, panelofButtons, panelforfiles, binary;
    public JButton add, sub, div, mod, mult, popButton, AC, equal, dot;
    public JButton supplement, binary_add, binary_sub, binary_mult;
    private final JButton load, save;
    public final JButton b[];
    private Integer i, memory1;
    Double memory;
    private Stack<String> stack = new Stack<>();

    //Συνάρτηση για την εμφάνιση της στοίβας stack στο label
    void showStack() {
        int k;
        String info = "";
        for (k = 0; k < stack.size(); k++) {
            info += stack.get(k);
        }
        label.setText(info);
    }

    //για αποθήκευση αριθμών σε αρχείο
    void save() {
        JFileChooser chooser = new JFileChooser();
        int returnVal = chooser.showSaveDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            try {
                String filename = chooser.getSelectedFile().getAbsolutePath();
                try (FileWriter fw = new FileWriter(filename)) {
                    PrintWriter pw = new PrintWriter(fw);
                    for (String x : stack) {
                        pw.println("" + x);
                    }
                }
            } catch (IOException ex) {
            }
        }
    }

    // για την εμφάνιση αριθμών από το αρχείο στο label
    void load() {
        JFileChooser chooser = new JFileChooser();
        int returnVal = chooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            try {
                String filename = chooser.getSelectedFile().getAbsolutePath();
                FileReader rw = new FileReader(filename);
                Scanner in = new Scanner(rw);
                while (in.hasNextLine()) {
                    String line = in.nextLine();
                    label.setText(line);
                }
            } catch (IOException ex) {
            }
        }
    }

    Calc(String title) {
        super(title);
        setSize(300, 300);
        setResizable(false);

        setLayout(new GridLayout(6, 1));
        add(label);

        //JPanel για τους τελεστές ανάμεσα σε δυαδικούς αριθμούς
        binary = new JPanel();
        binary.setLayout(new GridLayout(1, 3));
        add(binary);

        //Κουμπί για το συμπλήρωμα δυαδικών αριθμών
        supplement = new JButton("suppl");
        supplement.setBackground(Color.cyan);
        binary.add(supplement);
        supplement.addActionListener((ActionEvent e) -> {
            //JOptionPane.showMessageDialog(null,"sumplirwma");
            String x = label.getText();
            //JOptionPane.showMessageDialog(null,x);
            for (int i = 0; i < x.length(); i++) {
                int k = Character.getNumericValue(x.charAt(i));
                if (k == 0 || k == 1) {
                    //JOptionPane.showMessageDialog(null,"ok");
                    if (k == 1) {
                        label.setText("0");
                    } else {
                        label.setText("1");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Wrong system");
                    break;
                }
            }
        });

        //Κουμπί για την πρόσθεση δυαδικών αριθμών        
        binary_add = new JButton("+(bin)");
        binary_add.setBackground(Color.cyan);
        binary.add(binary_add);
        binary_add.addActionListener((ActionEvent e) -> {
            this.concatLabelText("+", 6);
        });

        //Κουμπί για την αφαίρεση δυαδικών αριθμών        
        binary_sub = new JButton("-(bin)");
        binary_sub.setBackground(Color.cyan);
        binary.add(binary_sub);
        binary_sub.addActionListener((ActionEvent e) -> {
            this.concatLabelText("-", 7);
        });

        //Κουμπί για τον πολλαπλασιασμό δυαδικών αριθμών
        binary_mult = new JButton("*(bin)");
        binary_mult.setBackground(Color.cyan);
        binary.add(binary_mult);
        binary_mult.addActionListener((ActionEvent e) -> {
            this.concatLabelText("*", 8);
        });

        //JPanel για τα σύμβολα των πράξεων
        operation = new JPanel();
        operation.setLayout(new GridLayout(1, 5));
        add(operation);

        //Κουμπί για την πρόσθεση δεκαδικών αριθμών
        add = new JButton("+");
        add.setBackground(Color.yellow);
        operation.add(add);
        add.addActionListener((ActionEvent e) -> {
            this.concatLabelText("+", 1);
        });

        //Κουμπί για την αφαίρεση δεκαδικών αριθμών
        sub = new JButton("-");
        sub.setBackground(Color.yellow);
        operation.add(sub);
        sub.addActionListener((ActionEvent e) -> {
            this.concatLabelText("-", 2);
            //JOptionPane.showMessageDialog(null,label.getText());
        });

        //Κουμπί για την διαίρεση δεκαδικών αριθμών
        div = new JButton("/");
        div.setBackground(Color.yellow);
        operation.add(div);
        div.addActionListener((ActionEvent e) -> {
            this.concatLabelText("/", 3);
        });

        //Κουμπί για το υπόλοιπο της διαίρεσης
        mod = new JButton("%");
        mod.setBackground(Color.yellow);
        operation.add(mod);
        mod.addActionListener((ActionEvent e) -> {
            this.concatLabelText("%", 4);
        });

        //Κουμπί για τον πολλαπλασιασμό δεκαδικών αριθμών
        mult = new JButton("*");
        mult.setBackground(Color.yellow);
        operation.add(mult);
        mult.addActionListener((ActionEvent e) -> {
            this.concatLabelText("*", 5);
        });

        //Panel για τους αριθμούς
        panel = new JPanel();
        panel.setLayout(new GridLayout(2, 5));//2 γραμμές 5 στήλες
        add(panel);

        //Υλοποίηση κουμπιών με αριθμούς
        b = new JButton[10];
        for (int i = 0; i <= b.length - 1; i++) {
            b[i] = new JButton(String.valueOf(i));
            panel.add(b[i]);
            b[i].setBackground(Color.green);
            String bt = b[i].getText();
            b[i].addActionListener((ActionEvent e) -> {
                this.concatLabelText(bt);
                stack.push(bt);
            });
        }

        //Υλοποίηση του JPanel για την ισότητα, την διαγραφή και τον μηδενισμό
        panelofButtons = new JPanel();
        panelofButtons.setLayout(new GridLayout(1, 4));
        add(panelofButtons);

        popButton = new JButton("DEL");
        popButton.setBackground(Color.cyan);
        panelofButtons.add(popButton);
        popButton.addActionListener((ActionEvent e) -> {
            if (stack.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Stack is empty");
            } else {
                System.out.println("Popped element: " + stack.pop());
                System.out.println("Stack after pop operation " + stack);
                showStack();
            }
        });

        AC = new JButton("AC");
        panelofButtons.add(AC);
        AC.addActionListener((ActionEvent e) -> {
            label.setText("0");
        });
        AC.setBackground(Color.cyan);

        equal = new JButton("=");
        equal.setBackground(Color.cyan);
        panelofButtons.add(equal);
        equal.addActionListener((ActionEvent e) -> {
            this.calculate();
        });

        dot = new JButton(".");
        dot.setBackground(Color.cyan);
        panelofButtons.add(dot);
        dot.addActionListener((ActionEvent e) -> {
            this.concatLabelText(".");
        });

        panelforfiles = new JPanel();
        panelforfiles.setLayout(new GridLayout(1, 2));
        add(panelforfiles);

        load = new JButton("Load");
        load.setBackground(Color.PINK);
        panelforfiles.add(load);
        load.addActionListener((ActionEvent e) -> {
            load();
        });

        save = new JButton("Save");
        save.setBackground(Color.PINK);
        panelforfiles.add(save);
        save.addActionListener((ActionEvent e) -> {
            save();
        });

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void concatLabelText(String text) {
        label.setText(label.getText() + text);
    }

    private void concatLabelText(String text, Integer iter) {
        this.concatLabelText(text);
        this.i = iter;

    }

    private void calculate() {
        String x, a, b;
        Double value1, value2, result;
        int res;
        switch (i) {
            case 1 -> {
                x = label.getText();
                a = x.substring(0, x.indexOf("+"));
                b = x.substring(x.indexOf("+"));
                value1 = Double.valueOf(a);
                value2 = Double.valueOf(b);
                result = value1 + value2;
                //JOptionPane.showMessageDialog(null,result);
                memory = result;
                label.setText(memory.toString());
            }
            case 2 -> {
                x = label.getText();
                a = x.substring(0, x.indexOf("-"));
                b = x.substring(x.indexOf("-") + 1);
                value1 = Double.valueOf(a);
                value2 = Double.valueOf(b);
                result = value1 - value2;
                memory = result;
                label.setText(memory.toString());
            }
            case 3 -> {
                x = label.getText();
                a = x.substring(0, x.indexOf("/"));
                b = x.substring(x.indexOf("/") + 1);
                value1 = Double.valueOf(a);
                value2 = Double.valueOf(b);
                result = value1 / value2;
                //JOptionPane.showMessageDialog(null,result);
                memory = result;
                label.setText(memory.toString());
            }
            case 4 -> {
                x = label.getText();
                a = x.substring(0, x.indexOf("%"));
                b = x.substring(x.indexOf("%") + 1);
                value1 = Double.valueOf(a);
                value2 = Double.valueOf(b);
                result = value1 % value2;
                Integer result1 = result.intValue();
                //JOptionPane.showMessageDialog(null,result1);
                memory = result;
                label.setText(memory.toString());
            }
            case 5 -> {
                x = label.getText();
                a = x.substring(0, x.indexOf("*"));
                b = x.substring(x.indexOf("*") + 1);
                value1 = Double.valueOf(a);
                value2 = Double.valueOf(b);
                result = value1 * value2;
                //JOptionPane.showMessageDialog(null,result);
                memory = result;
                label.setText(memory.toString());
            }
            case 6 -> {
                x = label.getText();
                //JOptionPane.showMessageDialog(null,x);
                a = x.substring(0, x.indexOf("+"));
                b = x.substring(x.indexOf("+"));
                value1 = Double.valueOf(a);
                value2 = Double.valueOf(b);
                //System.out.println(value1);
                //System.out.println(value2);
                int v1 = value1.intValue();
                //System.out.println(v1);
                int v2 = value2.intValue();
                // System.out.println(v2);
                if ((v1 == 0 || v1 == 1) && (v2 == 0 || v2 == 1)) {
                    if (v1 == 1) {
                        if (v2 == 1) {
                            res = 1;
                        } else {
                            res = 0;
                        }
                    } else {
                        res = 0;
                    }
                    //JOptionPane.showMessageDialog(null,res);
                    memory1 = res;
                    label.setText(memory1.toString());
                } else {
                    JOptionPane.showMessageDialog(null, "Wrong system");
                }
            }
            case 7 -> {
                x = label.getText();
                a = x.substring(0, x.indexOf("-"));
                b = x.substring(x.indexOf("-") + 1);
                value1 = Double.valueOf(a);
                value2 = Double.valueOf(b);
                int v1 = value1.intValue();
                System.out.println(v1);
                int v2 = value2.intValue();
                System.out.println(v2);
                if ((v1 == 0 || v1 == 1) && (v2 == 0 || v2 == 1)) {
                    if (v1 == 1) {
                        if (v2 == 1) {
                            res = 0;
                        } else {
                            res = 1;
                        }
                    } else {
                        if (v2 == 1) {
                            res = 1;
                        } else {
                            res = 0;
                        }
                    }
                    //JOptionPane.showMessageDialog(null,res);
                    memory1 = res;
                    label.setText(memory1.toString());
                } else {
                    JOptionPane.showMessageDialog(null, "Wrong system");
                }
            }
            case 8 -> {
                x = label.getText();
                a = x.substring(0, x.indexOf("*"));
                b = x.substring(x.indexOf("*") + 1);
                value1 = Double.valueOf(a);
                value2 = Double.valueOf(b);
                int v1 = value1.intValue();
                int v2 = value2.intValue();
                if ((v1 == 0 || v1 == 1) && (v2 == 0 || v2 == 1)) {
                    if (v1 == 1) {
                        if (v2 == 1) {
                            res = 1;
                        } else {
                            res = 0;
                        }
                    } else {
                        if (v2 == 0) {
                            res = 0;
                        } else {
                            res = 0;
                        }
                    }
                    //JOptionPane.showMessageDialog(null,res);
                    memory1 = res;
                    label.setText(memory1.toString());
                } else {
                    JOptionPane.showMessageDialog(null, "Wrong system");
                }
            }
        }
    }
}
