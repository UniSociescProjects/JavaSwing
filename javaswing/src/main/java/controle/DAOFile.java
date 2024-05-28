package controle;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DAOFile {

    public String readFile(String filePath) {
        StringBuilder content = new StringBuilder();
        File file = new File(filePath);

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                content.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content.toString();
    }

    public void saveToDatabase(String content, String fileName) {
        String sql = "INSERT INTO Dados (dados, arquivo) VALUES (?, ?)";
        Conexao conexaoDB = Conexao.getInstancia();
        Connection conn = conexaoDB.conectar();

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, content);
            pstmt.setString(2, fileName);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            conexaoDB.fecharConexao();
        }
    }

    public List<String> getFilesFromDatabase() {
        List<String> files = new ArrayList<>();
        String sql = "SELECT DISTINCT arquivo FROM Dados";

        try (Connection conn = Conexao.getInstancia().conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                files.add(rs.getString("arquivo"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return files;
    }
    
    public String getContentFromFile(String fileName) {
        String content = null;
        String sql = "SELECT dados FROM Dados WHERE arquivo = ?";

        try (Connection conn = Conexao.getInstancia().conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, fileName);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    content = rs.getString("dados");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return content;
    }

}
