package gui;

import javax.swing.*;

public class InfoDialog {
    public static void showInstructions(JFrame parent) {
        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.append("Instruções de Uso:\n\n");
        textArea.append("Para cadastrar um novo aluno:\n");
        textArea.append("Preencha todos os campos corretamente e clique em 'Inserir'.\n\n");

        textArea.append("Para excluir um aluno:\n");
        textArea.append("Digite apenas o CPF na caixa de entrada e clique em 'Excluir'.\n\n");

        textArea.append("Para atualizar os dados de um aluno:\n");
        textArea.append("Preencha os campos corretamente e clique em 'Atualizar'.\n\n");

        textArea.append("Para visualizar todos os alunos cadastrados:\n");
        textArea.append("Clique em 'Listar Todos'.\n\n");

        textArea.append("Possíveis erros:\n");
        textArea.append("Ao tentar cadastrar um novo aluno, o botão 'Inserir' pode ficar desabilitado.\n");
        textArea.append("Caso isso ocorra, apague o último número do CPF e digite novamente.\n");

        JScrollPane scrollPane = new JScrollPane(textArea);

        JOptionPane.showMessageDialog(parent, scrollPane, "Instruções de Uso", JOptionPane.INFORMATION_MESSAGE);
    }
}
