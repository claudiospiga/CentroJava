package TestConTabbedPane;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.*;

public class Clienti extends JFrame{
	private static final  String DB_URL ="jdbc:mysql://localhost:3306/centroestetico1";
	private static final String DB_USER = "root";
	private static final String DB_PASSWORD = "SQLpassword10_";
	
	private JTextArea outputArea;
	
	//Query per inserire un servizio
		private static final String INSERT_QUERY = "INSERT INTO clienti(nome, cognome, telefono, indirizzo,trattamenti_preferito, pec, codicefiscale, partitaiva) VALUES(?,?,?,?,?,?,?,?)";
		//Query per leggere tutti i servizi
		private static final String SELECT_ALL_QUERY = "SELECT * FROM clienti";
		//QUery per aggiornare
		
		private static final String UPDATE_QUERY = "UPDATE clienti SET nome = ?, cognome = ?, telefono = ?, indirizzo = ?, trattamenti_preferito = ?, pec = ?, codicefiscale = ?, partitaiva = ? WHERE id = ?";

		//Query per eliminare
		private static final String DELETE_QUERY = "DELETE FROM clienti WHERE id = ?";
	
		
	public Clienti(Main main) {
		
		setSize(900, 720);
		setTitle("Clienti");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		
		//Output
		outputArea = new JTextArea();
		outputArea.setEditable(false);
		
		String clientiBenvenuto = "Qui potrai gestire i tuoi clienti";
				
		
		JLabel fraseIniziale = new JLabel(clientiBenvenuto, SwingConstants.CENTER);
		fraseIniziale.setFont(new Font("Arial", Font.BOLD, 20));
		fraseIniziale.setOpaque(true);
		fraseIniziale.setBackground(Color.decode("#ffd1dc"));
		
		//Creazione pannello per contenere nel frame il nostro output
		JScrollPane scrollPane = new JScrollPane(outputArea);
		
		//Bottoni CRUD
		JButton addButton = new JButton("Aggiungi Cliente");
		JButton viewButton = new JButton("Visualizza Clienti");
		JButton updateButton = new JButton("Modifica Cliente");
		JButton deleteButton = new JButton("Elimina Cliente");
		
		//Aggiunta dei bottoni ad un pannello
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(1, 4));
		
		buttonPanel.add(addButton);
		buttonPanel.add(viewButton);
		buttonPanel.add(updateButton);
		buttonPanel.add(deleteButton);
		
		//-------------------------------
		//Cambio colore bottoni
		addButton.setBackground(Color.decode("#157347"));
		addButton.setForeground(Color.BLACK);
		
		//AddButton Hoover
		addButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				//QUando il mouse entra nel bottone cambia colore
				addButton.setForeground(Color.WHITE);
			}
			@Override
			public void mouseExited(MouseEvent e) {
				 // Quando il mouse esce dal pulsante, il colore del testo ritorna al colore predefinito BLACK
				addButton.setForeground(Color.BLACK);
			}
		});
		
		//Event Listenere dei bottoni
		addButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				aggiungi();
			}
		});
		//----------------------------------------
		//Cambio colore bottone view
		viewButton.setBackground(Color.decode("#31d2f2"));
		viewButton.setForeground(Color.BLACK);
		
		viewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				//QUando il mouse entra nel bottone il testo cambia colore da nero a bianco
				viewButton.setForeground(Color.WHITE);
			}
			@Override
			public void mouseExited(MouseEvent e) {
				viewButton.setForeground(Color.BLACK);
			}
		});
		
		
		viewButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				printClienti();
			}
		});
		
		//----------------------
		updateButton.setBackground(Color.decode("#ffca2c"));
		updateButton.setForeground(Color.BLACK);
		
		//Button hoover
		updateButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				updateButton.setForeground(Color.WHITE);
			}
			 @Override
			 public void mouseExited(MouseEvent e) {
				 updateButton.setForeground(Color.BLACK);
			 }
		});
		updateButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				modifica();
			}
		});
		//------------------
		deleteButton.setBackground(Color.decode("#dc3545"));
		deleteButton.setForeground(Color.BLACK);
		//Event listener Hoover mouse
		deleteButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				deleteButton.setForeground(Color.white);
			}
			@Override
			public void mouseExited(MouseEvent e) {
				deleteButton.setForeground(Color.black);
			}
		});
		deleteButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				elimina();
			}
		});
		//------------------
		//Creazione bottnoi e pannello per collegare le finestre
		
		JButton home = new JButton("Home");
		home.setBackground(Color.decode("#6f2cf3"));
		home.setForeground(Color.WHITE);
		
		 //Cambio granezza font
        Font font = new Font(home.getFont().getName(), Font.PLAIN, 20); // Imposta il font a 20 punti
        home.setFont(font);
        
		//Event listener
		home.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				home.setForeground(Color.BLACK);
			}
			@Override
			public void mouseExited(MouseEvent e) {
				home.setForeground(Color.white);
			}
		});
		

		JPanel buttonPanel2 = new JPanel();
		buttonPanel2.setLayout(new GridLayout(2, 4));
		buttonPanel2.add(home);
		buttonPanel2.add(fraseIniziale);
		//EventListener Per cambiare pagina
		home.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				main.setVisible(true);
			}
		});
		
		
		
		
		//Aggiunta del tutto al frame
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(scrollPane, BorderLayout.CENTER);
		getContentPane().add(buttonPanel, BorderLayout.SOUTH);
		getContentPane().add(buttonPanel2, BorderLayout.NORTH);
		
		
	}
	//Metodi per inserire dati clienti
	private void insertClienti(String nome, String  cognome, String telefono, String indirizzo,String trattamenti_preferito, String pec, String codicefiscale, String partitaiva) {
		try {
			Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
			
			PreparedStatement pstmt = conn.prepareStatement(INSERT_QUERY);
			pstmt.setString(1, nome);
			pstmt.setString(2, cognome);
			pstmt.setString(3, telefono);
			pstmt.setString(4, indirizzo);
			pstmt.setString(5, trattamenti_preferito);
			pstmt.setString(6, pec);
			pstmt.setString(7, codicefiscale);
			pstmt.setString(8, partitaiva);
			pstmt.executeUpdate();
			
		}catch(SQLException e) {
			e.printStackTrace();	
		}
		
	}
	
	//Metodo aggiorna cliente
		private void updateCliente(String nome, String  cognome, String telefono, String indirizzo,String trattamenti_preferito, String pec, String codicefiscale, String partitaiva,int id) {
			try {
				Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
				
				PreparedStatement pstmt = conn.prepareStatement(UPDATE_QUERY);
				pstmt.setString(1, nome);
				pstmt.setString(2, cognome);
				pstmt.setString(3, telefono);
				pstmt.setString(4, indirizzo);
				pstmt.setString(5, trattamenti_preferito);
				pstmt.setString(6, pec);
				pstmt.setString(7, codicefiscale);
				pstmt.setString(8, partitaiva);
				pstmt.setInt(9, id);
				pstmt.executeUpdate();
			}catch(SQLException e) {
				e.printStackTrace();
			}
		}
		
		//Metodi per stampare lista clienti
		private void printClienti() {
			outputArea.setText("");
			try {
				Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(SELECT_ALL_QUERY);
				while(rs.next()) {
					int id = rs.getInt("id");
					String nome = rs.getString("nome");
					String cognome = rs.getString("cognome");
					String indirizzo = rs.getString("indirizzo");
					String trattamenti_preferito = rs.getString("trattamenti_preferito");
					String telefono = rs.getString("telefono");
					String pec = rs.getString("pec");
					String codicefiscale = rs.getString("codicefiscale");
					String partitaiva = rs.getString("codicefiscale");
					
					outputArea.append("Riepilogo Dati: ID: " + id  + "\nNome: " + nome + ", \nCognome: " + cognome +
							"\nTelefono: " + telefono + "\nIndirizzo: " + indirizzo + "\ntrattamenti: " + trattamenti_preferito + "\nPec: " + pec + "\nCodice Fiscale: " + codicefiscale +
							"\nPartita Iva " + partitaiva);
				}
			}catch(SQLException e) {
				e.printStackTrace();	
			}
		}
		
		
		//Metodo elimina clienti
		private void deleteCliente(int id) {
			try {
				Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
				PreparedStatement pstmt = conn.prepareStatement(DELETE_QUERY);
				pstmt.setInt(1, id);
				pstmt.executeUpdate();
			}catch(SQLException e) {
				e.printStackTrace();
			}
		}
	private void aggiungi() { 
		String nome = JOptionPane.showInputDialog("Inserisci il nome:");
		String cognome = JOptionPane.showInputDialog("Inserisci il cognome:");
		String telefono = JOptionPane.showInputDialog("Inserisci il numero di telefono:");
		String indirizzo = JOptionPane.showInputDialog("Inserisci l'indirizzo:");
		String trattamenti_preferito = JOptionPane.showInputDialog("Inserisci il trattamento:");
		String pec = JOptionPane.showInputDialog("Inserisci la pec:");
		String codicefiscale = JOptionPane.showInputDialog("Inserisci il codice fiscale:");
		String partitaiva = JOptionPane.showInputDialog("Inserisci la PartitaIva:");
		if(nome != null && !nome.isEmpty()) {
			//Conversione di una stringa in una variabile double
			insertClienti(nome, cognome, telefono, indirizzo,trattamenti_preferito, pec, codicefiscale, partitaiva); //I dati vengono salvati nel database
			//Aggiunta dei dati nella finestra per visualizzare i dati
			outputArea.append("Cliente aggiunto \nNome: " + nome + ", \nCognome: " + cognome +
					"\nTelefono: " + telefono + "\nIndirizzo: " + indirizzo +"\ntrattamento preferito: " + trattamenti_preferito+ "\nPec: " + pec + "\nCodice Fiscale: " + codicefiscale +
					"\nPartita Iva " + partitaiva);
		}
	}

	private void modifica() {
		//Conversiona da valore stringa in numero intero
		int id = Integer.parseInt(JOptionPane.showInputDialog("Inserisci l'id del cliente da modificare: "));
		String nome = JOptionPane.showInputDialog("Inserisci il nuovo nome del cliente: ");
		String cognome = JOptionPane.showInputDialog("Inserisci il nuovo cognome del cliente:");
		String telefono = JOptionPane.showInputDialog("Inserisci il nuovo numero di telefono del cliente: \nSchiaccia OK se non vuoi inserire niente");
		String indirizzo = JOptionPane.showInputDialog("Inserisci il nuovo indirizzo del cliente: \nSchiaccia OK se non vuoi inserire niente");
		String trattamenti_preferito = JOptionPane.showInputDialog("Inserisci il nuovo trattamento del cliente: \nSchiaccia OK se non vuoi inserire niente");
		String pec = JOptionPane.showInputDialog("Inserisci la nuova pec: \nSchiaccia OK se non vuoi inserire niente");
		String codicefiscale = JOptionPane.showInputDialog("Inserisci il nuovo Codice Fiscale del cliente: \nSchiaccia OK se non vuoi inserire niente");
		String partitaiva = JOptionPane.showInputDialog("Inserisci la nuova Partita Iva: \nSchiaccia OK se non vuoi inserire niente");
		updateCliente(nome, cognome, telefono, indirizzo,trattamenti_preferito, pec, codicefiscale, partitaiva,id);
		outputArea.append("Cliente Modificato: ID: " + id  + "\nNome: " + nome + ", \nCognome: " + cognome +
				"\nTelefono: " + telefono + "\nIndirizzo: " + indirizzo +"\ntrattamento: " + trattamenti_preferito + "\nPec: " + pec + "\nCodice Fiscale: " + codicefiscale +
				"\nPartita Iva " + partitaiva + "\n");
	}
	
	private void elimina() {
		int id = Integer.parseInt(JOptionPane.showInputDialog("Inserisci l'id del servizio da eliminare: "));
		deleteCliente(id);
		outputArea.append("Cliente eliminato: ID" + id + "\n" );
	}
}

