package dao;

import java.sql.SQLException;
import modelo.Aluno;
import modelo.HistoricoPeso;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import factory.ConnectionFactory;
import gui.PesoHistorico;

public class AlunoDAO {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    public void inserir(Aluno aluno) throws SQLException {
        String sql = "INSERT INTO Aluno (cpf, nome, data_nascimento, peso, altura) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, aluno.getCpf());
            stmt.setString(2, aluno.getNome());
            stmt.setDate(3, new java.sql.Date(dateFormat.parse(aluno.getDataNascimento()).getTime()));
            stmt.setDouble(4, aluno.getPeso());
            stmt.setDouble(5, aluno.getAltura());
            stmt.executeUpdate();

            // Adiciona o peso inicial ao histórico de peso
            HistoricoPesoDAO historicoPesoDAO = new HistoricoPesoDAO();
            historicoPesoDAO.inserir(new HistoricoPeso(aluno.getCpf(), new Date(0), aluno.getPeso()));

            // Salvar os dados no arquivo texto
            salvarNoArquivo(aluno);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void excluir(String cpf) throws SQLException, ParseException {
        String sql = "DELETE FROM Aluno WHERE cpf = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            // Excluir histórico de peso antes de excluir o aluno
            HistoricoPesoDAO historicoPesoDAO = new HistoricoPesoDAO();
            historicoPesoDAO.excluirPorCPF(cpf);

            stmt.setString(1, cpf);
            stmt.executeUpdate();

            // Atualizar o arquivo texto após a exclusão
            atualizarArquivo();
        }
    }

    public void atualizar(Aluno aluno) throws SQLException {
        String sql = "UPDATE Aluno SET nome = ?, data_nascimento = ?, peso = ?, altura = ? WHERE cpf = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, aluno.getNome());
            stmt.setDate(2, new java.sql.Date(dateFormat.parse(aluno.getDataNascimento()).getTime()));
            stmt.setDouble(3, aluno.getPeso());
            stmt.setDouble(4, aluno.getAltura());
            stmt.setString(5, aluno.getCpf());
            stmt.executeUpdate();

            // Adiciona o novo peso ao histórico de peso
            HistoricoPesoDAO historicoPesoDAO = new HistoricoPesoDAO();
            historicoPesoDAO.inserir(new HistoricoPeso(aluno.getCpf(), new Date(0), aluno.getPeso()));

            // Atualizar o arquivo texto após a atualização
            atualizarArquivo();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public Aluno consultar(String cpf) throws SQLException, ParseException {
        String sql = "SELECT * FROM Aluno WHERE cpf = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, cpf);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Aluno(
                        rs.getString("cpf"),
                        rs.getString("nome"),
                        dateFormat.format(rs.getDate("data_nascimento")),
                        rs.getDouble("peso"),
                        rs.getDouble("altura")
                    );
                }
            }
        }
        return null;
    }

    public List<Aluno> listar() throws SQLException, ParseException {
        List<Aluno> alunos = new ArrayList<>();
        String sql = "SELECT * FROM Aluno";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Aluno aluno = new Aluno(
                    rs.getString("cpf"),
                    rs.getString("nome"),
                    dateFormat.format(rs.getDate("data_nascimento")),
                    rs.getDouble("peso"),
                    rs.getDouble("altura")
                );
                alunos.add(aluno);
            }
        }
        return alunos;
    }

    public List<PesoHistorico> consultarHistoricoPeso(String cpf) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'consultarHistoricoPeso'");
    }

    // Método para salvar os dados do aluno no arquivo texto
    private void salvarNoArquivo(Aluno aluno) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("alunos.txt", true))) {
            writer.write(aluno.getCpf() + "," + aluno.getNome() + "," + aluno.getDataNascimento() + "," + aluno.getPeso() + "," + aluno.getAltura());
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Método para atualizar o arquivo texto após exclusões ou atualizações
    private void atualizarArquivo() throws SQLException, ParseException {
        List<Aluno> alunos = listar();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("alunos.txt"))) {
            for (Aluno aluno : alunos) {
                writer.write(aluno.getCpf() + "," + aluno.getNome() + "," + aluno.getDataNascimento() + "," + aluno.getPeso() + "," + aluno.getAltura());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
