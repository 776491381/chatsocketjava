package socket;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ChatCliente extends JFrame {

	JTextField textoParaEnviar;
	Socket socket;
	PrintWriter w;
	String nome;
	JTextArea textoRecebido;
	Scanner r;
	
	private class EscutaServidor implements Runnable{

		public void run() {
			try{
				String texto;
				while((texto = r.nextLine()) != null){
					textoRecebido.append(texto + "\r\n");
				}
			}
			catch(Exception p){}
		}
		
	}
	
	public ChatCliente(String nome) throws IOException{
		super("Chat UOL: "+nome );
		this.nome = nome;
		Font font = new Font("Segoe UI", Font.PLAIN, 26);
		textoParaEnviar = new JTextField();
		textoParaEnviar.setFont(font);
		JButton botao = new JButton("Enviar");
		botao.setFont(font);
		botao.addActionListener(new EnviarListener());
		
		Container envio = new JPanel();
		envio.setLayout(new BorderLayout());
		envio.add(BorderLayout.CENTER, textoParaEnviar);
		envio.add(BorderLayout.EAST, botao);
				
		textoRecebido = new JTextArea();
		textoRecebido.setFont(font);
		JScrollPane scroll = new JScrollPane(textoRecebido);
		
		getContentPane().add(BorderLayout.CENTER, scroll);
		getContentPane().add(BorderLayout.SOUTH, envio);
		
		configurarCliente();
		
		
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(500, 500);
		
	}
	
	private class EnviarListener implements ActionListener{

		public void actionPerformed(ActionEvent e) {
			w.println(nome+": "+textoParaEnviar.getText());
			w.flush();
			textoParaEnviar.setText("");
			textoParaEnviar.requestFocus();
		}
		
	}

	private void configurarCliente(){
		try{
		socket = new Socket("127.0.0.1", 5000);
		w = new PrintWriter(socket.getOutputStream());
		r = new Scanner(socket.getInputStream());
		new Thread(new EscutaServidor()).start();
		}
		catch(Exception e){}
	}
	
	public static void main(String[] args) throws IOException {
		new ChatCliente("Alisson");
		new ChatCliente("Mateus");
	}
}
