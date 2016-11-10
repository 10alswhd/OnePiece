package project;

import java.awt.Button;
import java.awt.Frame;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Properties;

/*
 * IP, Port ���� SystemProperties�� �̿��ؼ� �����ð�
 * 
 * */

public class Client extends Frame implements ActionListener {

	private TextArea ta = new TextArea();
	private TextField tf = new TextField();
	private Button sendB = new Button("Send");
	private Panel p = new Panel();
	private Socket socket = null;
	private ObjectOutputStream oos = null;
	private ObjectInputStream ois = null;

	//private String ip;
	private int port;
	private String name;

	
	
	public Client() {
		createGUI();
	}

	// GUI �����ϰ� Event ���,ó��
	public void createGUI() {
		p.add(sendB);
		add(ta, "West");
		add(p, "Center");

		add(tf, "South");

		setBounds(200, 200, 400, 400);
		setVisible(true);

		// Event ���
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				close();
				System.exit(0);
			}
		});

		// ��ư�� �ڵ鷯 ���
		sendB.addActionListener(this);
		tf.addActionListener(this);

	}
	
	private void close(){
		try {
			ois.close();
			oos.close();
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void go(String ip, int port, String name) {
		
		this.name = name;
		
		try {
			// 1. Socket ����
			socket = new Socket(ip, port);
			
			// 2. I/O stream ����
			oos = new ObjectOutputStream(socket.getOutputStream());
			ois = new ObjectInputStream(socket.getInputStream());
			
			// Thread �����...
			new ChatClientThread().start();

			
		} catch (Exception e) {
			ta.append("������ ����������,IP��port�� �´��� Ȯ�� �ٶ��ϴ�.");
		}
	}

	public void actionPerformed(ActionEvent e) {

		Object selObj = e.getSource();
		if (selObj == tf || selObj == sendB) {
			String sendMsg = tf.getText().trim();
			try {
				oos.writeObject(" " +name+" )" + sendMsg);
				tf.setText("");
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} else {

		}

	}

	public static void main(String[] args) {
		
		String ip = "210.119.33.102";
		
//		try {
//			ip = "210.119.33.102";
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		Properties prop = System.getProperties();

		int port = 8888;
		String name = "C";
		
		System.out.println(ip+", "+port+", "+name);
		new Client().go(ip, port, name);
	}

	class ChatClientThread extends Thread {
		@Override
		public void run() {

			while (true) {
				try {
					String recMsg = ois.readObject().toString();
					ta.append(recMsg + "\n");
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}
	}// Inner
}// outer