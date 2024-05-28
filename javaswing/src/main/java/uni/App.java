package uni;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.table.DefaultTableModel;

import controle.DAOFile;

public class App extends JFrame {
    private DefaultListModel<String> listModel;
    private DefaultTableModel tableModel;
    private JList<String> fileList;
    private JTable dataTable;
    private static JComboBox<String> fileComboBox;
    static DAOFile daoFile = new DAOFile();

    public App() {
        setTitle("Java Swing");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout());

        JPanel leftPanel = new JPanel(new GridLayout(2, 1));

        listModel = new DefaultListModel<>();
        fileList = new JList<>(listModel);
        leftPanel.add(new JScrollPane(fileList));

        fileComboBox = new JComboBox<>();
        fileComboBox.addActionListener(e -> {
            String selectedFile = (String) fileComboBox.getSelectedItem();
            if (selectedFile != null) {
                exibirInformacoesDoArquivo(selectedFile);
            }
        });
        leftPanel.add(fileComboBox);

        panel.add(leftPanel, BorderLayout.WEST);

        String[] columnNames = {"Dados"};
        tableModel = new DefaultTableModel(columnNames, 0);
        dataTable = new JTable(tableModel);
        panel.add(new JScrollPane(dataTable), BorderLayout.CENTER);
        
        JButton loadButton = new JButton("Carregar Arquivo");
        loadButton.addActionListener(e -> carregarArquivo());
        panel.add(loadButton, BorderLayout.NORTH);

        getContentPane().add(panel);

        setVisible(true);
    }

    private void carregarArquivo() {
        JFileChooser fileChooser = new JFileChooser();
        int returnValue = fileChooser.showOpenDialog(this);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            salvarArquivoNoBanco(file);
        }
    }

    private void salvarArquivoNoBanco(File file) {
        StringBuilder content = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                content.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao ler o arquivo.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        
        daoFile.saveToDatabase(content.toString(), file.getName());

        listModel.clear();
        listModel.addElement(file.getName());

        fileComboBox.removeAllItems();
        for (String fileItem : daoFile.getFilesFromDatabase()) {
            fileComboBox.addItem(fileItem);
        }

        tableModel.setRowCount(0);
        String[] lines = content.toString().split("\n");
        for (String line : lines) {
            tableModel.addRow(new Object[]{line});
        }
    }
    
    private void iniciarSelect() {
    	for (String fileItem : daoFile.getFilesFromDatabase()) {
            fileComboBox.addItem(fileItem);
        }
    }

    private void exibirInformacoesDoArquivo(String fileName) {
        String content = daoFile.getContentFromFile(fileName);

        tableModel.setRowCount(0);
        String[] lines = content.split("\n");
        for (String line : lines) {
            tableModel.addRow(new Object[]{line});
        }
        
        listModel.clear();
        listModel.addElement(fileName);
    }

    public static void main(String[] args) {
 
        SwingUtilities.invokeLater(App::new);
        for (String fileItem : daoFile.getFilesFromDatabase()) {
            fileComboBox.addItem(fileItem);
        }
    }
}
