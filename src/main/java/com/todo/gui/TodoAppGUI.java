package com.todo.gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import com.todo.dao.TodoAppDAO;
import com.todo.model.Todo;
import java.util.List;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

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
        setupEventListeners();
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
                    loadSelectedtodo();
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
            String selectedFilter=(String)filterComboBox.getSelectedItem();
            filterTodo(selectedFilter);
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
        int row = todoTable.getSelectedRow();
        if (row==-1){
            JOptionPane.showMessageDialog(this,"Please select a row to update","Validation error",JOptionPane.WARNING_MESSAGE);
            return;
        }
        String title= titleField.getText().trim();
        if (title.isEmpty()){
            JOptionPane.showMessageDialog(this,"Please enter the title for the todo","Validation error",JOptionPane.WARNING_MESSAGE);
            return;
        }
        int id= (int) todoTable.getValueAt(row, 0);
        try{
            Todo todo= todoDAO.getTodoById(id);
            if (todo!=null){
                todo.setTitle(title);
                todo.setDescription(descriptionArea.getText().trim());
                todo.setCompleted(completedCheckBox.isSelected());

                if(todoDAO.updateTodo(todo)){
                    JOptionPane.showMessageDialog(this, "Todo updated sucessfully","Sucess",JOptionPane.INFORMATION_MESSAGE);
                    loadTodos();
                }
                else{
                    JOptionPane.showMessageDialog(this, "Failed to update todo","Update Error",JOptionPane.ERROR_MESSAGE);
                }

            }
        }
            catch(SQLException e){
                JOptionPane.showMessageDialog(this, "Failed to udpate","error",JOptionPane.ERROR_MESSAGE);
                
            }
        


    }
    private void filterTodo(String filter){
        int i=0;
        if (filter=="Completed"){
            i=1;
            JOptionPane.showMessageDialog(this, "Completed tasks","Showing",JOptionPane.INFORMATION_MESSAGE);
        }
        else if(filter=="All"){
            loadTodos();
            return;
        }
        try{
            List<Todo> todos=todoDAO.filterTodo(i);
            updateTable(todos);
    
            }
            catch(Exception e){
                JOptionPane.showMessageDialog(rootPane, e);
            }
    


    }
    private void deleteTodo(){
        int row = todoTable.getSelectedRow();
        if (row==-1){
            JOptionPane.showMessageDialog(this,"Please select a row to delete","Validation error",JOptionPane.WARNING_MESSAGE);
            return;
        }
        String title= titleField.getText().trim();
        if (title.isEmpty()){
            JOptionPane.showMessageDialog(this,"Please enter the title for the todo","Validation error",JOptionPane.WARNING_MESSAGE);
            return;
        }
        int id= (int) todoTable.getValueAt(row, 0);
        try{
            Todo todo= todoDAO.getTodoById(id);
            if (todo!=null){
                todo.setTitle(title);
                todo.setDescription(descriptionArea.getText().trim());
                todo.setCompleted(completedCheckBox.isSelected());

                if(todoDAO.deleteTodo(todo)){
                    JOptionPane.showMessageDialog(this, "Todo deleted sucessfully","Sucess",JOptionPane.INFORMATION_MESSAGE);
                    titleField.setText("");
                    descriptionArea.setText("");
                    loadTodos();
                }
                else{
                    JOptionPane.showMessageDialog(this, "Failed to deleted todo","Deletetion Error",JOptionPane.ERROR_MESSAGE);
                }

            }
        }
            catch(SQLException e){
                JOptionPane.showMessageDialog(this, "Failed to udpate","error",JOptionPane.ERROR_MESSAGE);
                
            }
        

    }
    private void refreshTodo(){
        filterComboBox.setSelectedIndex(0);
        loadTodos();
    }
    private void addTodo(){
        String title = titleField.getText().trim();
        String description = descriptionArea.getText().trim();
        boolean completed = completedCheckBox.isSelected();
        
        if (title.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Title cannot be empty", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            Todo todo = new Todo(title, description);
            todo.setCompleted(completed);
            todoDAO.createtodo(todo);
            JOptionPane.showMessageDialog(this, "Todo added successfully");
            
            // Clear input fields after adding
            titleField.setText("");
            descriptionArea.setText("");
            completedCheckBox.setSelected(false);
            
            // Refresh the todo list
            loadTodos();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error adding todo: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void loadSelectedtodo(){
        int row = todoTable.getSelectedRow();
        if(row != -1){  // Changed condition to check for valid row selection
            String title = (String) tableModel.getValueAt(row, 1);
            String description = (String) tableModel.getValueAt(row, 2);
            String completed = (String) tableModel.getValueAt(row, 3);

            titleField.setText(title);
            descriptionArea.setText(description);
            completedCheckBox.setSelected("Yes".equals(completed));
        }
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