package socket;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ChatServer {

	List<PrintWriter> escritores = new ArrayList<>();

	private void sendToAll(String texto){
		for (PrintWriter q : escritores) {
			try{
				q.println(texto);
				q.flush();
			}
			catch(Exception k){}
			
		}
	}
	
	public ChatServer(){
		ServerSocket server;
		Scanner leitor;
		try {
			server = new ServerSocket(5000);
			System.out.println("Servidor rodando na porta 5000.");
			while(true){
				Socket socket = server.accept();
				new Thread(new EscutaCliente(socket)).start();
				PrintWriter p = new PrintWriter(socket.getOutputStream());
				escritores.add(p);
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	private class EscutaCliente implements Runnable{

		Scanner leitor;
		public EscutaCliente(Socket socket){
			try{
				leitor = new Scanner(socket.getInputStream());
			}
			catch(Exception e){}
		}
		
		public void run() {
			try{
				String texto;	
				while((texto = leitor.nextLine()) != null){
					System.out.println(texto);
					sendToAll(texto);
				}
			}
			catch(Exception x){}
		}
		
	}
	
	public static void main(String[] args) {
		new ChatServer();
	}

}
