
import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import java.awt.Font;


import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.util.Vector;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.DefaultCaret;



import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JList;
import javax.swing.JPanel;
import java.awt.Color;
import javax.swing.AbstractListModel;
import javax.swing.JDialog;
import javax.swing.SwingConstants;

public class Client extends JFrame implements Runnable {

	public Vector socketList = new Vector();
	public Vector<String> clientList;
	String users[];
	final DefaultListModel model = new DefaultListModel();


	private JFrame frame;
	public javax.swing.JTextArea ChatArea;
	public javax.swing.JTextArea TextArea;
	public javax.swing.JTextArea onlineList;
	public JTextField username;
	public JPasswordField password;
	public JButton btnSend = new JButton("SEND");
	public JButton btnDisconnect = new JButton("Disconnect");
	public JButton btnprivatemessage = new JButton("Private Message");
	public JButton btnLogin = new JButton("Login");
	public JButton btnReset = new JButton("Reset");
	public JButton btnExit = new JButton("Exit");
	public JButton btnClear = new JButton("Clear");
	public JLabel DisplayUsername = new JLabel("");
	public JLabel usernamelabel = new JLabel("Username");
	public JLabel passwordlabel = new JLabel("Password");
	public JList Online_List2 = new JList(model);
	private final JTextField ServerIP = new JTextField();
	private final JTextField serversocket = new JTextField();
	public JTextField serverIP;
	public JTextField port;

	private DataOutputStream dos;
	private DataInputStream dis;

	static String serverip;
	static int socketserver;


	Socket client;
	String userText;
	String getusername=null;
	String getpassword=null;



	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Client window = new Client();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	/**
	 * Create the application.
	 */
	public Client()
	{
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		serversocket.setFont(new Font("Tahoma", Font.PLAIN, 27));
		serversocket.setBounds(704, 21, 222, 55);
		serversocket.setColumns(10);
		ServerIP.setFont(new Font("Tahoma", Font.PLAIN, 27));
		ServerIP.setBounds(237, 12, 366, 50);
		ServerIP.setColumns(10);
		frame = new JFrame("Chat Page");
		frame.setBounds(900, 800, 1257, 862);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		UIManager.put("OptionPane.messageFont", new FontUIResource(new Font(
		          "Arial", Font.BOLD, 27)));
		UIManager.put("OptionPane.buttonFont", new FontUIResource(new Font(
		          "Arial", Font.BOLD, 23)));

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 260, 1035, 419);
		frame.getContentPane().add(scrollPane);

//*******************************TEXT FIELD*******************************************

		ChatArea = new JTextArea();
		ChatArea.setRows(10);
		ChatArea.setLineWrap(true);
		ChatArea.setEditable(false);
		ChatArea.setBounds(10, 260, 1016, 333);
		ChatArea.setFont(new Font("Tahoma", Font.PLAIN, 25));
		scrollPane.setViewportView(ChatArea);
		//add this to make the status area always show the latest message
		DefaultCaret caret = (DefaultCaret)ChatArea.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

				TextArea = new JTextArea();
				TextArea.setFont(new Font("Tahoma", Font.PLAIN, 25));
				TextArea.setBounds(10, 700, 1035, 70);
				frame.getContentPane().add(TextArea);
				TextArea.setVisible(false);

		username = new JTextField();
		username.setFont(new Font("Tahoma", Font.PLAIN, 30));
		username.setColumns(10);
		username.setBounds(146, 92, 305, 63);
		frame.getContentPane().add(username);

		password = new JPasswordField();
		password.setBounds(605, 91, 305, 63);
		frame.getContentPane().add(password);

//*******************************Label*************************************************

		usernamelabel.setFont(new Font("Tahoma", Font.PLAIN, 29));
		usernamelabel.setBounds(10, 105, 127, 26);
		frame.getContentPane().add(usernamelabel);


		passwordlabel.setFont(new Font("Tahoma", Font.PLAIN, 29));
		passwordlabel.setBounds(472, 105, 131, 26);
		frame.getContentPane().add(passwordlabel);

		DisplayUsername.setVisible(false);
		DisplayUsername.setFont(new Font("Tahoma", Font.PLAIN, 29));
		DisplayUsername.setBounds(10, 92, 901, 63);
		frame.getContentPane().add(DisplayUsername);

		JLabel lblOnlineUsers = new JLabel("Online Users");
		lblOnlineUsers.setFont(new Font("Tahoma", Font.PLAIN, 25));
		lblOnlineUsers.setBounds(1055, 265, 155, 26);
		frame.getContentPane().add(lblOnlineUsers);

		JLabel lblIpAddress = new JLabel("Server IP Address");
		lblIpAddress.setFont(new Font("Tahoma", Font.PLAIN, 27));
		lblIpAddress.setBounds(10, 21, 216, 26);
		frame.getContentPane().add(lblIpAddress);

		JLabel lblPort = new JLabel("Port");
		lblPort.setFont(new Font("Tahoma", Font.PLAIN, 27));
		lblPort.setBounds(648, 32, 55, 26);
		frame.getContentPane().add(lblPort);


//*******************************Buttons*************************************************

		//send button
		btnSend.setFont(new Font("Tahoma", Font.PLAIN, 29));
		btnSend.setBounds(1066, 698, 127, 72);
		frame.getContentPane().add(btnSend);
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				userText = TextArea.getText();
				sendMessage(userText);
			}
		});

		//disconnect button
		btnDisconnect.setFont(new Font("Tahoma", Font.PLAIN, 27));
		btnDisconnect.setBounds(195, 176, 181, 55);
		frame.getContentPane().add(btnDisconnect);
		btnDisconnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					String exit = ("\n" + getusername + " has now been disconnected");
					sendMessage(exit);
					JOptionPane.showMessageDialog(null,"You are now disconnected","Logout",JOptionPane.WARNING_MESSAGE);
					TextArea.setVisible(false);
					ChatArea.setText(null);
					username.setVisible(true);
					password.setVisible(true);
					username.setText(null);
	      			password.setText(null);
	      			usernamelabel.setVisible(true);
          			passwordlabel.setVisible(true);
          			DisplayUsername.setVisible(false);
          			btnLogin.setEnabled(true);
          			btnReset.setEnabled(true);
          			btnClear.setEnabled(false);
          			ServerIP.setEditable(true);
           			serversocket.setEditable(true);
           			ServerIP.setText(null);
           			serversocket.setText(null);
           			model.removeAllElements();
           			model.clear();
           			Online_List2.removeAll();
					client.close();
				} catch (Exception e1) {
					e1.printStackTrace();
					JOptionPane.showMessageDialog(null,"Error, you are not logged in","Logout",JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		//private message button
		btnprivatemessage.setEnabled(false);
		btnprivatemessage.setFont(new Font("Tahoma", Font.PLAIN, 27));
		btnprivatemessage.setBounds(408, 176, 246, 55);
		frame.getContentPane().add(btnprivatemessage);
		btnprivatemessage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JTextField field1 = new JTextField();
				JTextField field2 = new JTextField();
				field1.setFont(new Font("Tahoma", Font.PLAIN, 25));
				field2.setFont(new Font("Tahoma", Font.PLAIN, 25));
				Object[] fields = { "To: ",field1 ,"Message:",field2};

				JOptionPane.showConfirmDialog(null,fields,"Private Message",JOptionPane.OK_CANCEL_OPTION);

				String touser = field1.getText();
				String message = field2.getText();
				String complete = ("@private " + touser + " " + message );
				sendMessage(complete);
			}
		});

		//login button
		btnLogin.setFont(new Font("Tahoma", Font.PLAIN, 27));
		btnLogin.setBounds(10, 181, 141, 50);
		frame.getContentPane().add(btnLogin);
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				getusername = username.getText();
				getpassword = password.getText();
				CredentialCheck(getusername,getpassword);
			}
		});

		//exit button
		btnExit.setFont(new Font("Tahoma", Font.PLAIN, 27));
		btnExit.setBounds(938, 172, 151, 59);
		frame.getContentPane().add(btnExit);
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				try
				{
					client.close();

				} catch (IOException e1)
				{
					e1.printStackTrace();
				}
				JOptionPane.showMessageDialog(null,"You are now disconnected\nSystem will exit","Logout",JOptionPane.WARNING_MESSAGE);
				System.exit(0);
			}
		});

		//reset button
		btnReset.setFont(new Font("Tahoma", Font.PLAIN, 27));
		btnReset.setBounds(941, 86, 148, 63);
		frame.getContentPane().add(btnReset);
		btnReset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				username.setText(null);
				password.setText(null);
			}
		});

		//clear button
		btnClear.setEnabled(false);
		btnClear.setFont(new Font("Tahoma", Font.PLAIN, 27));
		btnClear.setBounds(686, 176, 181, 55);
		frame.getContentPane().add(btnClear);
		btnClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ChatArea.setText(null);
			}
		});

		Online_List2.setFont(new Font("Tahoma", Font.PLAIN, 28));
		Online_List2.setBounds(1055, 295, 138, 384);
		frame.getContentPane().add(Online_List2);






		frame.getContentPane().add(ServerIP);
		frame.getContentPane().add(serversocket);
	}

//********************************************Functions****************************************************************************************

	//trigger upon client connect
	public void ClientConnect(String hostip,int port)
	{
		try
		{
			client = new Socket(hostip,socketserver);
			dos = new DataOutputStream(client.getOutputStream());
			dis = new DataInputStream(client.getInputStream());
			String connected =(getusername + " @connected");
			dos.writeUTF(connected);
			ChatArea.append("[Status] You are now connected");
			new Thread(this).start();


		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	//create a new thread at background to do listening
	public void run()
	{
		try
		{
			// Receive messages one-by-one, forever
			while (true)
			{
				// Get the next message
				String message = dis.readUTF();
				if(message.contains("@blocked"))
				{
					JOptionPane.showMessageDialog(null,"You have been blocked by server!","Blocked",JOptionPane.WARNING_MESSAGE );
					client.close();
					TextArea.setVisible(false);
					ChatArea.setText(null);
					username.setVisible(true);
					password.setVisible(true);
					username.setText(null);
	      			password.setText(null);
	      			usernamelabel.setVisible(true);
          			passwordlabel.setVisible(true);
          			DisplayUsername.setVisible(false);
          			btnLogin.setEnabled(true);
          			btnReset.setEnabled(true);
          			btnClear.setEnabled(false);
          			ServerIP.setEditable(true);
           			serversocket.setEditable(true);
           			ServerIP.setText(null);
           			serversocket.setText(null);
				}
				else if(message.contains("@shutdown"))
				{
					ChatArea.append("\n[WARNING] Server is shutting down!\n");
					try {
						Thread.sleep(3000);
						client.close();
						TextArea.setVisible(false);
						ChatArea.setText(null);
						username.setVisible(true);
						password.setVisible(true);
						username.setText(null);
		      			password.setText(null);
		      			usernamelabel.setVisible(true);
	          			passwordlabel.setVisible(true);
	          			DisplayUsername.setVisible(false);
	          			btnLogin.setEnabled(true);
	          			btnReset.setEnabled(true);
	          			btnClear.setEnabled(false);
	          			ServerIP.setEditable(true);
	           			serversocket.setEditable(true);
	           			ServerIP.setText(null);
	           			serversocket.setText(null);
	           			model.removeAllElements();
	           			model.clear();
	           			Online_List2.removeAll();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				else if(message.contains("@onlineuser"))
				{
					updateOnlineList(message);
				}
				else
				{
					// Print it to our text window
					ChatArea.append( "\n"+message);
				}
			}
		}

		catch( IOException ie )
		{
			System.out.println( ie );
		}
	}
	private void updateOnlineList(String message)
	{

		String split = message.split(" ",2)[0];
		users= split.split(",");
		model.clear();
		Online_List2.setModel(model);
		for(int x=0; x < users.length; x++)
    	{
			model.addElement("- " + users[x]);
    	}
	}
	//gather user's message and send out
	private void sendMessage(String message)
	{
		if (message.contains("@private"))
		{
			String prvmessage;
			prvmessage = getusername + " " + message;
			try
			{
				dos.writeUTF(prvmessage);
				TextArea.setText(null);
				prvmessage = null;
			}
			catch( IOException ie )
			{
				System.out.println( ie );
			}
		}
		else
		{
			try
			{
				String complete= (getusername + " : " + message);
				dos.writeUTF(complete);
				TextArea.setText(null);
				complete = null;
			}
			catch( IOException ie )
			{
				System.out.println( ie );
			}
		}
	}
	//check client's credentials
	public void CredentialCheck(String name, String passwd)
	{
		try
		{
			Scanner read = new Scanner(new File("src/credentials.txt"));
			String line=null;
	        String line2=null;
	        boolean login = false;
	        while(read.hasNextLine())
           {
	        	while(line != name)
	        	{
	        		line = read.nextLine();
	        		if(line.equals(name))
		        	{
	        			line2=read.nextLine();
	           		  	if(line2.equals(passwd))
	           		  	{
	           			  JOptionPane.showMessageDialog(null,"Login Successful","Login System",JOptionPane.INFORMATION_MESSAGE );
	           			  TextArea.setVisible(true);
	           			  username.setVisible(false);
	           			  password.setVisible(false);
	           			  btnClear.setEnabled(true);
	           			  btnReset.setEnabled(false);
	           			  btnLogin.setEnabled(false);
	           			  btnprivatemessage.setEnabled(true);
	           			  usernamelabel.setVisible(false);
	           			  passwordlabel.setVisible(false);
	           			  DisplayUsername.setVisible(true);
	           			  serverip = ServerIP.getText();
	           			  String socket =serversocket.getText();
	           			  socketserver = Integer.parseInt(socket);
	           			//call client.java upon successful login
	           			  ClientConnect(serverip,socketserver);
	           			  DisplayUsername.setText("Welcome: " + name );
	           			  ServerIP.setEditable(false);
	           			  serversocket.setEditable(false);
	           			  break;
	           		  	}
	           		  	else
	           		  	{
	           			  JOptionPane.showMessageDialog(null,"Unsuccessful Login. \nPlease try again","Login System",JOptionPane.ERROR_MESSAGE );
	           			  password.setText(null);
	           			  username.setText(null);
	           			  break;
	           		  	}
		        	}
	        	}
      			 break;
           }
	        read.close();
		}
	    catch (FileNotFoundException e)
		{

			e.printStackTrace();
		}
	}
}