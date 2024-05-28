package uni;

import java.awt.*;
import javax.swing.*;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import net.miginfocom.swing.MigLayout;

import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.JButton;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.table.DefaultTableModel;

import controle.DAOFile;

import javax.swing.JComboBox;
import javax.swing.JFileChooser;

public class TelaInicial extends JFrame {
	private DefaultListModel<String> listModel;
	private DefaultTableModel tableModel;
    private JList<String> fileList;
	private JTable dataTable;
	private JComboBox<String> fileComboBox;

	public TelaInicial() {
		setTitle("Java Swing");
		setVisible(true);
		setSize(800, 400);
		
		JPanel panel = new JPanel();
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(Alignment.LEADING).addComponent(panel,
				GroupLayout.DEFAULT_SIZE, 747, Short.MAX_VALUE));
		groupLayout.setVerticalGroup(
				groupLayout.createParallelGroup(Alignment.LEADING).addGroup(groupLayout.createSequentialGroup()
						.addComponent(panel, GroupLayout.DEFAULT_SIZE, 373, Short.MAX_VALUE).addGap(0)));

		JScrollPane scrollPane = new JScrollPane();

		JButton btnArquivo = new JButton("Inserir Arquivo");
		btnArquivo.addActionListener(e -> carregarArquivo());
		
		listModel = new DefaultListModel<>();
		fileList = new JList<>(listModel);
		scrollPane.add(new JScrollPane(fileList));

		fileComboBox = new JComboBox<>();
		scrollPane.add(fileComboBox);
		
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel.createSequentialGroup()
					.addGap(26)
					.addComponent(btnArquivo)
					.addGap(18)
					.addComponent(fileComboBox, 0, 137, Short.MAX_VALUE)
					.addGap(28)
					.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 425, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel.createSequentialGroup()
							.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
								.addComponent(btnArquivo, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(fileComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
							.addGap(317))
						.addComponent(scrollPane, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 340, Short.MAX_VALUE))
					.addContainerGap())
		);
		
		String[] columnNames = {"Dados"};
        tableModel = new DefaultTableModel(columnNames, 0);
        dataTable = new JTable(tableModel);
		scrollPane.setViewportView(dataTable);
		panel.setLayout(gl_panel);
		getContentPane().setLayout(groupLayout);
		
		atualizarArquivosNoComboBox();
		
		fileComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedFile = (String) fileComboBox.getSelectedItem();
                exibirInformacoesDoArquivo(selectedFile);
            }
        });

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

        // Salva o conteúdo do arquivo no banco de dados
        DAOFile daoFile = new DAOFile();
        daoFile.saveToDatabase(content.toString(), file.getName());

        // Atualiza a lista de dados exibida na interface gráfica
        listModel.clear();
        listModel.addElement(file.getName());

        // Atualiza o JComboBox com os arquivos do banco de dados
        atualizarArquivosNoComboBox();

        // Atualiza a tabela com os dados do arquivo
        tableModel.setRowCount(0); // Limpa a tabela
        String[] lines = content.toString().split("\n");
        for (String line : lines) {
            tableModel.addRow(new Object[]{line}); // Adiciona cada linha como uma nova linha na tabela
        }
    }

    private void atualizarArquivosNoComboBox() {
        DAOFile daoFile = new DAOFile();
        fileComboBox.removeAllItems();
        for (String fileItem : daoFile.getFilesFromDatabase()) {
            fileComboBox.addItem(fileItem);
        }
    }

    private void exibirInformacoesDoArquivo(String fileName) {
        DAOFile daoFile = new DAOFile();
        String content = daoFile.getContentFromFile(fileName);

        if (content != null) {
            // Atualiza a tabela com os dados do arquivo selecionado
            tableModel.setRowCount(0); // Limpa a tabela
            String[] lines = content.split("\n");
            for (String line : lines) {
                tableModel.addRow(new Object[]{line}); // Adiciona cada linha como uma nova linha na tabela
            }
        } else {
            // Adicione um tratamento de erro apropriado, como uma mensagem para o usuário
            JOptionPane.showMessageDialog(this, "Erro ao carregar o conteúdo do arquivo.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }


    public static void main(String[] args) {
        // Garante que a interface gráfica seja construída na thread de despacho de eventos Swing
        SwingUtilities.invokeLater(TelaInicial::new);
    }
}

