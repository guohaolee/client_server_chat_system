
import java.awt.EventQueue;
import java.awt.event.ActionListener;
import java.net.*;
import java.util.*;
import java.io.*;
import java.lang.reflect.Array;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.DefaultCaret;


import java.awt.event.ActionEvent;
import java.awt.Font;
import java.awt.Color;


public class Server extends JFrame {

	private JFrame frame;
	public javax.swing.JTextArea status;
	public javax.swing.JTextArea ServerText;

	ServerSocket server = null;
	int port = 5000;
	PrintWriter client;
	String a=null;

    public Vector socketList = new Vector();
    public Vector<String> clientList = new Vector();

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Server window = new Server();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	public Server() {
		initialize();
	}
	public void initialize() {

		UIManager.put("OptionPane.messageFont", new FontUIResource(new Font(
		          "Arial", Font.BOLD, 27)));
		UIManager.put("OptionPane.buttonFont", new FontUIResource(new Font(
		          "Arial", Font.BOLD, 23)));

		frame = new JFrame("Server Gui");
		frame.setResizable(false);
		frame.setBounds(100, 500, 735, 631);
		frame.setSize(1000,900);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(31, 34, 926, 594);
		frame.getContentPane().add(scrollPane);

		Font font = new Font("Verdana", Font.BOLD, 24);

		//start button
		JButton btnStart = new JButton("Start");
		btnStart.setFont(new Font("Tahoma", Font.PLAIN, 29));
		btnStart.setBounds(21, 653, 149, 49);
		frame.getContentPane().add(btnStart);
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				Thread starter = new Thread(new ServerStart());
		        starter.start();
			}
		});

		//stop button
		JButton btnStop = new JButton("Stop");
		btnStop.setFont(new Font("Tahoma", Font.PLAIN, 29));
		btnStop.setBounds(21, 754, 149, 49);
		frame.getContentPane().add(btnStop);
		btnStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ServerStart server = new ServerStart();
				server.serverstop();
			}
		});

		//clear button
		JButton btnClear = new JButton("Clear");
		btnClear.setFont(new Font("Tahoma", Font.PLAIN, 29));
		btnClear.setBounds(832, 651, 141, 52);
		frame.getContentPane().add(btnClear);
		btnClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				status.setText(null);
			}
		});

		//send button
		JButton btnSend = new JButton("Send");
		btnSend.setFont(new Font("Tahoma", Font.PLAIN, 29));
		btnSend.setBounds(832, 766, 141, 48);
		frame.getContentPane().add(btnSend);
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				a = ServerText.getText();
				ServerStart server = new ServerStart();
				server.SendToAll("\n [SERVER MESSAGE] " + a);
				ServerText.setText(null);
			}
		});

		//Server write area
		ServerText = new JTextArea();
		ServerText.setBounds(176, 653, 646, 161);
		ServerText.setFont(new Font("Tahoma", Font.PLAIN, 29));
		frame.getContentPane().add(ServerText);

      	//server status area
		status = new JTextArea();
		status.setEditable(false);
		status.setLineWrap(true);
		status.setFont(new Font("Tahoma", Font.PLAIN, 29));
		status.setBounds(21, 24, 952, 608);
		//frame.getContentPane().add(status);
		status.setColumns(20);
		status.setRows(5);
		scrollPane.setViewportView(status);

		//block button
		JButton btnBlock = new JButton("Block");
		btnBlock.setFont(new Font("Tahoma", Font.PLAIN, 29));
		btnBlock.setBounds(832, 711, 141, 49);
		frame.getContentPane().add(btnBlock);
		//add this to make the status area always show the latest message
		DefaultCaret caret = (DefaultCaret)status.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		btnBlock.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JTextField field1 = new JTextField();
				field1.setFont(new Font("Tahoma", Font.PLAIN, 25));
				Object[] fields = { "User To Block: ",field1};
				JOptionPane.showConfirmDialog(null,fields,"Block User",JOptionPane.OK_CANCEL_OPTION);
				String blockuser = (field1.getText());
				ServerStart server = new ServerStart();
				server.BlockUser(blockuser);
			}
		});
	}
	//display message at message area in server GUI
	public void displayMessage(String message)
	{
		status.append(message);
	}

	//fire this upon button start clicked
	public class ServerStart implements Runnable
	{
		Socket ServerSocket;
		public void run()
		{
		        try
		       	{
		           	server = new ServerSocket(5000);
		           	InetAddress ip;
		           	ip = InetAddress.getLocalHost();
		            status.append("Server Startup @IP: " + ip.getHostAddress() + "\n");
		            status.append("Server Socket: 5000 \n");
		       	}
		        catch(IOException e)
		        {
		           e.printStackTrace();
		        }

		        try
		        {
		        	while(true)
		        	{
		        		ServerSocket = server.accept();

		        		//read message from client
			            DataInputStream dis = new DataInputStream(ServerSocket.getInputStream());
			            String status = dis.readUTF();
			            if(status.contains("@connected"));
			            {
			            	//extract the username when connected
			            	String[] getusername=status.split(" ",2);
			            	String username= getusername[0];
			            	displayMessage("Got Connection from:" + username + "\n");

			            	//assign client & username list to a vector
			            	setSocketList(ServerSocket);
			            	setClientList(username);
			            	SendToAll("\n[Status] " + username + " is now online");
			            	String onlineuser = String.join(",",clientList);
			            	SendToAll(onlineuser + " @onlineuser");
			            	onlineuser = " ";
			            }
		        		DataOutputStream dos = new DataOutputStream(ServerSocket.getOutputStream());
		        		//new thread is allocated to each connected client
		        		new MultithreadServer2(this,ServerSocket);
		        	}
		        }
		        catch(IOException e)
		        {
		        	e.printStackTrace();
		        }
		}

		//broadcast received message or server message to all connected client
		public void SendToAll( String message )
		{
			DataOutputStream dos;
			for(int x=0; x < clientList.size(); x++)
			{
				Socket sock = (Socket) socketList.elementAt(x);
				try
				{
					 dos = new DataOutputStream(sock.getOutputStream());
					 dos.writeUTF( message + "\n" );
				}
				catch (IOException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		//broadcast message to selected user
		public void SendToOne(String fromuser,String touser, String message)
		{
			for(int x=0; x < clientList.size(); x++)
			{
				 if(clientList.get(x).equals(touser))
				 {
					Socket sock = (Socket) socketList.get(x);
					DataOutputStream dos;
					try {
						dos = new DataOutputStream(sock.getOutputStream());
						dos.writeUTF("[Private Message from " + fromuser + "] : " + message);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				 }
			}
		}
		//block user and disconnect them from the server
		public void BlockUser(String blockuser)
		{
			displayMessage("Server Blocking User : " + blockuser + "\n");
			for(int x=0; x < clientList.size(); x++)
			{
				 if(clientList.get(x).equals(blockuser))
				 {
					 Socket sock = (Socket) socketList.get(x);
					 DataOutputStream dos;
						try {
							dos = new DataOutputStream(sock.getOutputStream());
							dos.writeUTF("@blocked");
							SendToAll("[WARNING] " + blockuser + " has been blocked and now disconnected\n");
							removeConnection(sock);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							JOptionPane.showMessageDialog(null,"Error! Removing User. Please Try Again","ERROR",JOptionPane.ERROR_MESSAGE);
							e.printStackTrace();
						}
				 }
			}
		}
		//assign new connected client name to a vector list
		public void setClientList(String client){
	        try {
	            clientList.add(client);
	            displayMessage("[setClientList]: Added\n");

	        } catch (Exception e) { displayMessage("[setClientList]: "+ e.getMessage()); }
	    }
		//assign new connected client socket to a vector list
		public void setSocketList(Socket socket){
	        try {
	            socketList.add(socket);
	            displayMessage("[setSocketList]: Added \n");
	        } catch (Exception e) { displayMessage("[setSocketList]: "+ e.getMessage()); }
	    }

		//Stopping server and shutdown
		public void serverstop()
		{
			try
			{
				SendToAll("@shutdown");
				displayMessage("\nServer is shutting down");
				Thread.sleep(3000);
				System.exit(0);
				ServerSocket.close();
			}
			catch (IOException e)
			{
				// TODO Auto-generated catch block
				JOptionPane.showMessageDialog(null,"Error, Server logged on","ERROR",JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
			}
			catch
			(InterruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		//remove client's connection upon disconnecting
		public void removeConnection(Socket soc){
	        try {

	            for(int x=0; x < socketList.size(); x++){
	                if(socketList.elementAt(x).equals(soc))
	                {
	                	String name = clientList.get(x);
	                	displayMessage("[Removed user]: " + name + "\n");
	                	clientList.removeElementAt(x);
	                    socketList.removeElementAt(x);
	                    String onlineuser = String.join(",",clientList);
		            	SendToAll(onlineuser + " @onlineuser");
		            	onlineuser = " ";
	                    break;
	                }
	            }
	        } catch (Exception e) {
	        	displayMessage("[RemovedException]: "+ e.getMessage());
	        }
	    }
	}

	//the new thread when client is connected
	public class MultithreadServer2 extends Thread
	{
		private ServerStart server;
		private Socket socket;

		public MultithreadServer2(ServerStart server, Socket socket)
		{
			this.server=server;
			this.socket=socket;

			start();
		}
		public void run()
		{
			try
			{
				//read message from client
	            DataInputStream dis = new DataInputStream(socket.getInputStream());

	            while(true)
	            {
	            	//read message from client
	            	String clienttext;
	            	clienttext = dis.readUTF();

	            	if(clienttext.contains("@private"))
	            	{
	            		String[] split=clienttext.split(" ",4);
	            		String fromuser = split[0];
	            		String touser = split[2];
	            		String message = split[3];

	            		server.SendToOne(fromuser,touser,message);
	            		split = new String[split.length];
	            		clienttext = null;
	            	}
	            	else
	            	{
	            		//display message at server's status
	            		status.append("Text from client: " + clienttext + "\n");
	            		server.SendToAll(clienttext);
	            		clienttext = null;
	            	}
	            }
			}
			catch (IOException e)
			{
				System.out.println(e);
			}
			server.removeConnection(socket);
		}
	}
}
