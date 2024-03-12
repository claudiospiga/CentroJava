package TestConTabbedPane;



import java.sql.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.*;

public class Login extends JFrame {
	private static final String url = "jdbc:mysql://localhost:3306/centroestetico1";
	private static final String username = "root";
	private static final String password = "SQLpassword10_";

	private static final String RUOLO = "SELECT * FROM login WHERE nome_utente = '%s' AND passworddipendenti = '%s'";
	
	private JTextField txtUsername;
	private JLabel lblUsername, lblPassword, lblDescription, lblEmpty;
	private JButton btnLogin;
	private JPasswordField passwordField;
	
	public Login() {
		setTitle("Login Centro Estetico Amministratore");
		setSize(900,720);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		
		lblDescription = new JLabel("Login:");
		lblEmpty = new JLabel("");
		
		lblUsername = new JLabel("Username");
		txtUsername = new JTextField(50);

		lblPassword = new JLabel("Password");
		passwordField = new JPasswordField(50);
		
		btnLogin = new JButton("Login");
		
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				

				if (!txtUsername.getText().isEmpty()) {
					char[] pass = passwordField.getPassword();
					String passwordUser = new String(pass);
					String userName = txtUsername.getText();
					if (pass.length != 0) {
						int rs = login(txtUsername.getText(), passwordUser);
						switch(rs) {
						case 1: 
							JOptionPane.showMessageDialog(null, "Benvenuto Amministratore "  + txtUsername.getText());
							cleanData(txtUsername, passwordField);
							dispose();
							new Main();
						    break;
						case 0:
			            	JOptionPane.showMessageDialog(null, "Password o Username non validi.");
							break;
						default:
							JOptionPane.showMessageDialog(null, "Errore.");
							break;
						}						
					} else {
						JOptionPane.showMessageDialog(null, "Non è stata inserito lo username");
					}
				}
			}
		}

		);
		
		JPanel panel = new JPanel(new GridLayout(4, 2));
		panel.add(lblDescription);
		panel.add(lblEmpty);
		panel.add(lblUsername);
		panel.add(txtUsername);
		panel.add(lblPassword);
		panel.add(passwordField);

		panel.add(btnLogin);

		add(panel);
		setVisible(true);
		
	}
	 

	public static void cleanData(JTextField txtUsername, JPasswordField passwordField) {
		txtUsername.setText("");
		passwordField.setText("");
	}

	private static int login(String nome_utente, String passworddipendenti) {
		String query = String.format(RUOLO, nome_utente, passworddipendenti);
		System.out.println(query);
		try (Connection conn = DriverManager.getConnection(url, username, password);
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(query)) {
			if(rs.next()) {
				String ruolo = rs.getString("ruolo");
				if(ruolo.equals("amministratore")) {
					return 1;
				} else
					return 0;
			}
			return 0;
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null,
					"Errore durante la connessione al database");
			e.printStackTrace();
		}
		return 0;
	}
}

