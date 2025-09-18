package com.todo.gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

import com.todo.dao.TodoAppDAO;
import com.todo.model.Todo;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;
public class TodoAppGUI extends JFrame{
    private TodoAppDAO todoDAO;
    private JTable todoTable;
    private DefaultTableModel tableModel;
    private JTextField titleField;
    private JTextArea descriptionArea;
    private JCheckBox completedCheckBox;
    private JButton AddButton;
    private JButton updateButton;
    private JButton deleteButton;
    private JButton refereshButton;
    private JComboBox<String> filterComboBox;

    public TodoAppGUI(){
        this.todoDAO = new TodoAppDAO();
        initializeComponents();
        setupLayout();
        setVisible(true);
        loadTodos();
    }

    private void initializeComponents(){
        setTitle("Todo Application");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setMinimumSize(new Dimension(600, 400));
        setLocationRelativeTo(null);
        setPreferredSize(new Dimension(800, 600));

        String[] columnNames={"ID","Title","Description","Completed","Created At","Updated At"};
        tableModel=new DefaultTableModel(columnNames,0){
            @Override
            public boolean isCellEditable(int row,int column){
                return false;
            }
        };

        todoTable=new JTable(tableModel);
        todoTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        todoTable.getSelectionModel().addListSelectionListener(
            (e)->{
                if (!e.getValueIsAdjusting()){
                    //loadSelectedtodo();
                }
            }
        );

        titleField=new JTextField(20);
        descriptionArea=new JTextArea(3,20);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);

        completedCheckBox=new JCheckBox("Completed");

        AddButton=new JButton("Add Todo");
        updateButton=new JButton("Update Todo");
        deleteButton=new JButton("Delete Todo");
        refereshButton=new JButton("Refersh Todo");

        String[] filterOptions={"All","Completed","Pending"};
        filterComboBox=new JComboBox<>(filterOptions);
        filterComboBox.addActionListener((e)->{
            //filter
    });

    }

    private void setupLayout(){
        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.gridx=0;
        gbc.gridy=0;
        gbc.anchor = GridBagConstraints.WEST;
        inputPanel.add(new JLabel("Title"),gbc);
        gbc.gridx=1;
        gbc.gridy=0;
        gbc.fill=GridBagConstraints.HORIZONTAL;
        inputPanel.add(titleField,gbc);
        gbc.gridx=0;
        gbc.gridy=1;
        inputPanel.add(new JLabel("Description"),gbc);
        gbc.gridx=1;
        inputPanel.add(new JScrollPane(descriptionArea),gbc);

        gbc.gridx=1;
        gbc.gridy=2;
        inputPanel.add(completedCheckBox,gbc);

        JPanel buttonPanel=new JPanel(new FlowLayout());
        buttonPanel.add(AddButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refereshButton);

        JPanel filterPanel=new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.add(new JLabel("Filter"));
        filterPanel.add(filterComboBox);

        JPanel northPanel = new JPanel(new BorderLayout());
        northPanel.add(inputPanel,BorderLayout.CENTER);
        northPanel.add(buttonPanel,BorderLayout.SOUTH);
        northPanel.add(filterPanel,BorderLayout.WEST);
        add(northPanel,BorderLayout.NORTH);
        add (new JScrollPane(todoTable),BorderLayout.CENTER);

        JPanel statusJPanel= new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusJPanel.add(new JLabel("Select a todo to edit or delete"));
        add(statusJPanel,BorderLayout.SOUTH);

    }
    private void setupEventListeners(){
        AddButton.addActionListener((e)->{addTodo();});
        updateButton.addActionListener((e)->{updateTodo();});
        deleteButton.addActionListener((e)->{deleteTodo();});
        refereshButton.addActionListener((e)->{refreshTodo();});

    }
    private void updateTodo(){

    }
    private void deleteTodo(){

    }
    private void refreshTodo(){

    }
    private void addTodo(){

    }
    private void loadTodos() {
        try{
        List<Todo> todos=todoDAO.getAllTodos();
        updateTable(todos);

        }
        catch(Exception e){
            JOptionPane.showMessageDialog(rootPane, e);
        }


    }
    private void updateTable(List<Todo> todos){
        tableModel.setRowCount(0);
        for(Todo t : todos){
            Object[] row={
                t.getId(),
                t.getTitle(),
                t.getDescription(),
                t.isCompleted() ? "Yes" : "No",
                t.getCreatedAt() != null ? t.getCreatedAt().toString() : "N/A",
                t.getUpdatedAt() != null ? t.getUpdatedAt().toString() : "N/A"
            };
            tableModel.addRow(row);
        }
    }
    
}