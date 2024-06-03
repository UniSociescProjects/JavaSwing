package uni;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import controle.DAOFile;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import modelos.BubbleSort;

public class TelaInicial extends JFrame {
    private DefaultListModel<String> listModel;
    private DefaultTableModel tableModel;
    private JList<String> fileList;
    private JTable dataTable;
    private JComboBox<String> fileComboBox;
    private JComboBox<String> searchSortComboBox;
    private JComboBox<String> optionsComboBox;
    private static DAOFile daoFile = new DAOFile();
    private int[] dados;

    public TelaInicial() {
        setTitle("Java Swing");
        setSize(800, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        JPanel panel = new JPanel();
        GroupLayout groupLayout = new GroupLayout(getContentPane());
        groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(panel,
                GroupLayout.DEFAULT_SIZE, 747, Short.MAX_VALUE));
        groupLayout.setVerticalGroup(
                groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(groupLayout.createSequentialGroup()
                        .addComponent(panel, GroupLayout.DEFAULT_SIZE, 373, Short.MAX_VALUE).addGap(0)));

        JScrollPane scrollPane = new JScrollPane();

        JButton btnArquivo = new JButton("Inserir Arquivo");
        btnArquivo.addActionListener(e -> carregarArquivo());

        listModel = new DefaultListModel<>();
        fileList = new JList<>(listModel);
        JScrollPane fileListScrollPane = new JScrollPane(fileList);

        fileComboBox = new JComboBox<>();
        fileComboBox.addActionListener(e -> {
            String selectedFile = (String) fileComboBox.getSelectedItem();
            if (selectedFile != null) {
                exibirInformacoesDoArquivo(selectedFile);
            }
        });

        searchSortComboBox = new JComboBox<>(new String[]{"Pesquisa", "Ordenação"});
        searchSortComboBox.addActionListener(e -> atualizarOptionsComboBox());

        optionsComboBox = new JComboBox<>();
        optionsComboBox.addActionListener(e -> {
            if (searchSortComboBox.getSelectedItem().equals("Ordenação")) {
                System.out.println("Selecionado: Ordenação");
                String selectedSortMethod = (String) optionsComboBox.getSelectedItem();
                System.out.println("Método de Ordenação: " + selectedSortMethod);
                String selectedFile = (String) fileComboBox.getSelectedItem();
                if (selectedFile != null && selectedSortMethod != null) {
                    if (selectedSortMethod.equals("BubbleSort")) {
                        System.out.println("Executando BubbleSort");
                        ordenarComBubbleSort(selectedFile);
                    }
                }
            }
        });

        JLabel lblNewLabel = new JLabel("Método de Pesquisa/Ordenação:");
        
        JLabel lblSelecionarPesquisaordenao = new JLabel("Selecionar Pesquisa/Ordenação:");
        
        JLabel lblArquivosSalvos = new JLabel("Arquivos Salvos:");

        GroupLayout gl_panel = new GroupLayout(panel);
        gl_panel.setHorizontalGroup(
            gl_panel.createParallelGroup(Alignment.TRAILING)
                .addGroup(gl_panel.createSequentialGroup()
                    .addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
                        .addGroup(gl_panel.createSequentialGroup()
                            .addGap(85)
                            .addComponent(btnArquivo))
                        .addGroup(gl_panel.createSequentialGroup()
                            .addContainerGap()
                            .addComponent(fileComboBox, 0, 150, Short.MAX_VALUE))
                        .addGroup(gl_panel.createSequentialGroup()
                            .addContainerGap()
                            .addComponent(optionsComboBox, 0, 150, Short.MAX_VALUE))
                        .addGroup(gl_panel.createSequentialGroup()
                            .addGap(20)
                            .addComponent(lblNewLabel))
                        .addGroup(gl_panel.createSequentialGroup()
                            .addContainerGap()
                            .addComponent(searchSortComboBox, 0, 150, Short.MAX_VALUE))
                        .addGroup(gl_panel.createSequentialGroup()
                            .addGap(22)
                            .addComponent(lblSelecionarPesquisaordenao, GroupLayout.PREFERRED_SIZE, 157, GroupLayout.PREFERRED_SIZE))
                        .addGroup(gl_panel.createSequentialGroup()
                            .addGap(20)
                            .addComponent(lblArquivosSalvos, GroupLayout.PREFERRED_SIZE, 157, GroupLayout.PREFERRED_SIZE)))
                    .addPreferredGap(ComponentPlacement.RELATED)
                    .addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 425, GroupLayout.PREFERRED_SIZE)
                    .addContainerGap())
        );
        gl_panel.setVerticalGroup(
            gl_panel.createParallelGroup(Alignment.LEADING)
                .addGroup(gl_panel.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
                        .addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 351, Short.MAX_VALUE)
                        .addGroup(gl_panel.createSequentialGroup()
                            .addComponent(btnArquivo)
                            .addGap(9)
                            .addComponent(lblSelecionarPesquisaordenao)
                            .addGap(7)
                            .addComponent(searchSortComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                            .addGap(5)
                            .addComponent(lblNewLabel)
                            .addGap(8)
                            .addComponent(optionsComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                            .addGap(28)
                            .addComponent(lblArquivosSalvos)
                            .addGap(8)
                            .addComponent(fileComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                            .addGap(160)))
                    .addContainerGap())
        );

        String[] columnNames = {"Dados"};
        tableModel = new DefaultTableModel(columnNames, 0);
        dataTable = new JTable(tableModel);
        scrollPane.setViewportView(dataTable);

        panel.setLayout(gl_panel);
        getContentPane().setLayout(groupLayout);

        setVisible(true);
        iniciarSelect();
        atualizarOptionsComboBox();
    }

    private void carregarArquivo() {
        JFileChooser fileChooser = new JFileChooser();
        int returnValue = fileChooser.showOpenDialog(this);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            String fileName = file.getName();
            salvarArquivoNoBanco(file);
            exibirInformacoesDoArquivo(fileName);
        }
    }

    private void salvarArquivoNoBanco(File file) {
        StringBuilder content = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (isLong(line.trim())) {
                    content.append(line).append("\n");
                } else {
                    System.out.println("Ignorando linha inválida: " + line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao ler o arquivo.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        daoFile.saveToDatabase(content.toString(), file.getName());
        atualizarComboBoxELista();
        tableModel.setRowCount(0);
        
        long[] longDados = Arrays.stream(content.toString().split("\n"))
                                 .mapToLong(Long::parseLong)
                                 .toArray();

        for (long dado : longDados) {
            tableModel.addRow(new Object[]{dado});
        }
    }
    
    private boolean isLong(String str) {
        try {
            Long.parseLong(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private void ordenarComBubbleSort(String fileName) {
        String content = daoFile.getContentFromFile(fileName);
        if (content == null || content.isEmpty()) {
            JOptionPane.showMessageDialog(this, "O conteúdo do arquivo está vazio ou não foi encontrado.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String[] lines = content.split("\n");
        long[] data = new long[lines.length];
        for (int i = 0; i < lines.length; i++) {
            data[i] = parseLongWithoutLeadingZeros(lines[i].trim());
        }

        BubbleSort.sort(data);
        System.out.println("Dados após a ordenação: " + Arrays.toString(data));

        StringBuilder sortedContent = new StringBuilder();
        for (long num : data) {
            sortedContent.append(num).append("\n");
        }

        daoFile.saveToDatabase(sortedContent.toString(), fileName);
        
        exibirInformacoesDoArquivo(fileName);
    }


    private long parseLongWithoutLeadingZeros(String str) {
        return Long.parseLong(str.replaceFirst("^0+(?!$)", ""));
    }



    private void atualizarComboBoxELista() {
        listModel.clear();
        fileComboBox.removeAllItems();

        for (String fileItem : daoFile.getFilesFromDatabase()) {
            listModel.addElement(fileItem);
            fileComboBox.addItem(fileItem);
        }
    }

    private void iniciarSelect() {
        for (String fileItem : daoFile.getFilesFromDatabase()) {
        	fileComboBox.addItem(fileItem);
        }
    }

    private void atualizarOptionsComboBox() {
        optionsComboBox.removeAllItems();
        if (searchSortComboBox.getSelectedItem().equals("Pesquisa")) {
            optionsComboBox.addItem("Pesquisa Linear");
            optionsComboBox.addItem("Pesquisa Recursiva");
        } else {
            optionsComboBox.addItem("InsertionSort");
            optionsComboBox.addItem("QuickSort");
            optionsComboBox.addItem("BubbleSort");
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
        SwingUtilities.invokeLater(TelaInicial::new);
    }
}
