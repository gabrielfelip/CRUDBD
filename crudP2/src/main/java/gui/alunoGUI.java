package gui;

import component.TipTextField;
import dao.AlunoDAO;
import modelo.Aluno;
import modelo.HistoricoPeso;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.awt.Color;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.util.List;
import java.util.Date;
import java.text.DecimalFormat;


public class alunoGUI extends JFrame {
    private TipTextField txtCpf, txtNome, txtDataNascimento, txtPeso, txtAltura;
    private JButton btnInserir, btnExcluir, btnAtualizar, btnConsultar, btnListarTodos, btnAjuda, btnConsultarHistorico, btnCalcularIMC;
    private AlunoDAO alunoDAO;
    private JLabel lblResultadoIMC;

    public alunoGUI() {
        alunoDAO = new AlunoDAO();
        setTitle("Gestão de alunos ");
        setSize(700, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLayout(null);

        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Color minhaCor = new Color(240,255,255);
        getContentPane().setBackground(minhaCor);

        lblResultadoIMC = new JLabel("");
        lblResultadoIMC.setBounds(20, 300, 400, 25);
        add(lblResultadoIMC);


        // Adicionando componentes
        JLabel lblCpf = new JLabel("CPF: ");
        lblCpf.setBounds(20, 20, 100, 25);
        add(lblCpf);
        txtCpf = new TipTextField("Digite apenas números");
        txtCpf.setBounds(150, 20, 200, 25);
        add(txtCpf);

        JLabel lblNome = new JLabel("Nome: ");
        lblNome.setBounds(20, 60, 100, 25);
        add(lblNome);
        txtNome = new TipTextField("Digite o nome completo");
        txtNome.setBounds(150, 60, 200, 25);
        add(txtNome);

        JLabel lblDataNascimento = new JLabel("Data de nascimento: ");
        lblDataNascimento.setBounds(20, 100, 100, 25);
        add(lblDataNascimento);
        txtDataNascimento = new TipTextField("dd/mm/yyyy");
        txtDataNascimento.setBounds(150, 100, 200, 25);
        add(txtDataNascimento);

        JLabel lblPeso = new JLabel("Peso (kg): ");
        lblPeso.setBounds(20, 140, 100, 25);
        add(lblPeso);
        txtPeso = new TipTextField("Ex: 75.0");
        txtPeso.setBounds(150, 140, 200, 25);
        add(txtPeso);

        JLabel lblAltura = new JLabel("Altura (m): ");
        lblAltura.setBounds(20, 180, 100, 25);
        add(lblAltura);
        txtAltura = new TipTextField("Ex: 1.75");
        txtAltura.setBounds(150, 180, 200, 25);
        add(txtAltura);

        btnInserir = new JButton("Inserir");
        btnInserir.setBounds(20, 220, 80, 25);
        add(btnInserir);
        btnInserir.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    Aluno aluno = new Aluno(txtCpf.getText(), txtNome.getText(), txtDataNascimento.getText(),
                            Double.parseDouble(txtPeso.getText()), Double.parseDouble(txtAltura.getText()));
                    alunoDAO.inserir(aluno);
                    JOptionPane.showMessageDialog(null, "Aluno inserido com sucesso! ");
                } catch (ParseException | SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Erro ao inserir aluno.");
                }
            }
        });

        btnExcluir = new JButton("Excluir");
        btnExcluir.setBounds(110, 220, 80, 25);
        add(btnExcluir);
        btnExcluir.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    alunoDAO.excluir(txtCpf.getText());
                    JOptionPane.showMessageDialog(null, "Aluno excluído com sucesso!");
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Erro ao excluir aluno.");
                } catch (ParseException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }
        });

        btnCalcularIMC = new JButton("Calcular IMC");
        btnCalcularIMC.setBounds(290, 260, 150, 25);
        add(btnCalcularIMC);
        btnCalcularIMC.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                calcularIMC();
            }
        });

        btnAtualizar = new JButton("Atualizar");
        btnAtualizar.setBounds(200, 220, 80, 25);
        add(btnAtualizar);
        btnAtualizar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    Aluno aluno = new Aluno(txtCpf.getText(), txtNome.getText(), txtDataNascimento.getText(),
                            Double.parseDouble(txtPeso.getText()), Double.parseDouble(txtAltura.getText()));
                    alunoDAO.atualizar(aluno);
                    JOptionPane.showMessageDialog(null, "Aluno atualizado com sucesso!");
                } catch (ParseException | SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Erro ao atualizar aluno.");
                }
            }
        });

        btnConsultarHistorico = new JButton("Consultar Histórico de Peso");
        btnConsultarHistorico.setBounds(20, 260, 200, 25);
        add(btnConsultarHistorico);
        btnConsultarHistorico.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                consultarHistoricoPeso();
            }
        });

        btnConsultar = new JButton("Consultar");
        btnConsultar.setBounds(290, 220, 100, 25);
        add(btnConsultar);
        btnConsultar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    Aluno aluno = alunoDAO.consultar(txtCpf.getText());
                    if (aluno != null) {
                        JFrame consultaFrame = new JFrame("Detalhes do aluno");
                        consultaFrame.setSize(400, 300);
                        consultaFrame.setLayout(null);
                        JLabel lblConsultaNome = new JLabel("Nome: " + aluno.getNome());
                        lblConsultaNome.setBounds(20, 20, 300, 25);
                        consultaFrame.add(lblConsultaNome);

                        JLabel lblConsultaDataNascimento = new JLabel("Data de Nascimento: " + aluno.getDataNascimento());
                        lblConsultaDataNascimento.setBounds(20, 60, 300, 25);
                        consultaFrame.add(lblConsultaDataNascimento);

                        JLabel lblConsultaPeso = new JLabel("Peso: " + aluno.getPeso());
                        lblConsultaPeso.setBounds(20, 100, 300, 25);
                        consultaFrame.add(lblConsultaPeso);

                        JLabel lblConsultaAltura = new JLabel("Altura: " + aluno.getAltura());
                        lblConsultaAltura.setBounds(20, 140, 300, 25);
                        consultaFrame.add(lblConsultaAltura);

                        consultaFrame.setVisible(true);
                    } else {
                        JOptionPane.showMessageDialog(null, "Aluno não encontrado.");
                    }
                } catch (SQLException | ParseException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Erro ao consultar aluno.");
                }
            }
        });

        btnListarTodos = new JButton("Listar Todos");
        btnListarTodos.setBounds(400, 220, 120, 25);
        add(btnListarTodos);
        btnListarTodos.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    List<Aluno> alunos = alunoDAO.listar();

                    JFrame listaAlunosFrame = new JFrame("Lista de Alunos");
                    listaAlunosFrame.setSize(600, 500);

                    JTable tabelaAlunos = new JTable();
                    DefaultTableModel model = new DefaultTableModel();
                    model.addColumn("CPF");
                    model.addColumn("Nome");
                    model.addColumn("Data de Nascimento");
                    model.addColumn("Peso");
                    model.addColumn("Altura");

                    for (Aluno aluno : alunos) {
                        model.addRow(new Object[] { aluno.getCpf(), aluno.getNome(), aluno.getDataNascimento(),
                                aluno.getPeso(), aluno.getAltura() });
                    }

                    tabelaAlunos.setModel(model);
                    JScrollPane scrollPane = new JScrollPane(tabelaAlunos);
                    listaAlunosFrame.add(scrollPane);

                    listaAlunosFrame.setVisible(true);
                } catch (SQLException | ParseException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Erro ao listar alunos. ");
                }
            }
        });

        txtCpf.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                checkFields();
            }

            public void removeUpdate(DocumentEvent e) {
                checkFields();
            }

            public void changedUpdate(DocumentEvent e) {
            }

            private void checkFields() {
                if (txtCpf.getText().length() != 11 || txtNome.getText().isEmpty()
                        || txtDataNascimento.getText().isEmpty() || txtPeso.getText().isEmpty()
                        || txtAltura.getText().isEmpty()) {
                    btnInserir.setEnabled(false);
                } else {
                    btnInserir.setEnabled(true);
                }
            }
        });

        btnAjuda = new JButton();
        btnAjuda.setText("?");
        btnAjuda.setBorder(null);
        btnAjuda.setBounds(646, 20, 35, 28);
        add(btnAjuda);
        btnAjuda.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                InfoDialog.showInstructions(alunoGUI.this);
            }
        });

        JButton btnLimpar = new JButton("Limpar");
        btnLimpar.setBounds(531, 220, 100, 25);
        add(btnLimpar);
        btnLimpar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                txtCpf.setText("");
                txtNome.setText("");
                txtDataNascimento.setText("");
                txtPeso.setText("");
                txtAltura.setText("");

            }
        });

        setVisible(true);
    }

    private String interpretarResultadoIMC(double imc) {
        if (imc < 18.5) {
            return "Abaixo do peso";
        } else if (imc < 24.9) {
            return "Peso normal";
        } else if (imc < 29.9) {
            return "Sobrepeso";
        } else if (imc < 34.9) {
            return "Obesidade grau I";
        } else if (imc < 39.9) {
            return "Obesidade grau II";
        } else {
            return "Obesidade grau III (mórbida)";
        }
    }

    private void salvarDadosEmArquivo(String cpf, String nome, double imc) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("dados_imc.txt", true))) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy  HH:mm:ss");
            String dataAtual = sdf.format(new Date());
            String resultadoIMC = interpretarResultadoIMC(imc);

            writer.write(String.format("Data: %s | CPF: %s | Nome: %s | IMC: %.2f | Resultado: %s%n",
                    dataAtual, cpf, nome, imc, resultadoIMC));
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao salvar dados em arquivo.");
        }
    }

    private void calcularIMC() {
        try {
            Aluno aluno = alunoDAO.consultar(txtCpf.getText());
            if (aluno != null) {
                String pesoText = txtPeso.getText();
                String alturaText = txtAltura.getText();

                if (pesoText.isEmpty() || alturaText.isEmpty()){
                    JOptionPane.showMessageDialog(null, "Por favor preencha os campos de peso e altura.");
                    return;
                }
                double peso = Double.parseDouble(txtPeso.getText());
                double altura = Double.parseDouble(txtAltura.getText());
                double imc = peso / (altura * altura);

                DecimalFormat df = new DecimalFormat("#.##");
                String imcFormatado = df.format(imc);
                

                JOptionPane.showMessageDialog(null, "O IMC é: " + imcFormatado);

                salvarDadosEmArquivo(txtCpf.getText(), aluno.getNome(), imc);
            } else {
                JOptionPane.showMessageDialog(null, "Aluno não encontrado.");
            }
        } catch (SQLException | ParseException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao consultar aluno.");
        }
    }

    private void consultarHistoricoPeso() {
        String cpf = txtCpf.getText();

        try {
            Aluno aluno = alunoDAO.consultar(cpf);
            if (aluno != null) {
                List<HistoricoPeso> historicoPeso = aluno.getHistoricoPeso();
                if (!historicoPeso.isEmpty()) {
                    StringBuilder mensagem = new StringBuilder();
                    mensagem.append("Histórico de Peso do Aluno ").append(aluno.getNome()).append(":\n");
                    for (HistoricoPeso hp : historicoPeso) {
                        mensagem.append("Data: ").append(hp.getDataRegistro()).append(", Peso: ").append(hp.getPeso())
                                .append(" kg\n");
                    }
                    JOptionPane.showMessageDialog(null, mensagem.toString(), "Histórico de Peso",
                            JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null,
                            "Histórico de Peso não encontrado para o aluno " + aluno.getNome(), "Histórico de Peso",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Aluno não encontrado", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException | ParseException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao consultar histórico de peso do aluno.", "Erro",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        new alunoGUI();
    }
}
