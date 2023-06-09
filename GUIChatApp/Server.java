
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.HeadlessException;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
import java.nio.Buffer;

import javax.lang.model.SourceVersion;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class Server extends JFrame{
    ServerSocket server;
    Socket socket;
    BufferedReader br;
    PrintWriter out;

    private JLabel heading=new JLabel("Server Side");
    private JTextArea messageArea=new JTextArea();
    private JTextField messageInput=new JTextField();
    private Font font=new Font("Robot",Font.PLAIN,20);

    public Server(){
       try{
        server=new ServerSocket(7778);
        System.out.println("server is ready to accept connection");
        System.out.println("Waiting ....");
        socket=server.accept();

        br=new BufferedReader(new InputStreamReader(socket.getInputStream()));

        out=new PrintWriter(socket.getOutputStream());

        createGUI();
        handleEvents();
        
        startReading();
        //startWriting();


       }catch(Exception e){
        e.printStackTrace();
       }   }    

       private void handleEvents(){
        messageInput.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {
                // TODO Auto-generated method stub
                
            }

            @Override
            public void keyPressed(KeyEvent e) {
                // TODO Auto-generated method stub
                
            }

            @Override
            public void keyReleased(KeyEvent e) {
                // TODO Auto-generated method stub
                if(e.getKeyCode()==10){
                    System.out.println("You have Presss Enter Key Button");
                    String contentToSend=messageInput.getText();
                    messageArea.append("Me : "+contentToSend+"\n");
                    out.println(contentToSend);
                    out.flush();
                    messageInput.setText("");
                    messageInput.requestFocus();
                    
                }
                
            }
            
        });

    }

    private void createGUI(){
        this.setTitle("Server Massenger[END]");
        this.setSize(600,8000);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        heading.setIcon(new ImageIcon("Logo.jpg"));
        heading.setHorizontalTextPosition(SwingConstants.CENTER);
        heading.setVerticalTextPosition(SwingConstants.BOTTOM);
        heading.setHorizontalTextPosition(SwingConstants.CENTER);
        
        heading.setFont(font);
        messageArea.setFont(font);
        messageInput.setFont(font);

        heading.setHorizontalAlignment(SwingConstants.CENTER);
        heading.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));

        messageArea.setEditable(false);
        messageInput.setHorizontalAlignment(SwingConstants.CENTER);

        this.setLayout(new BorderLayout());

        this.add(heading, BorderLayout.NORTH);
        JScrollPane jScrollPane=new JScrollPane(messageArea);
        this.add(jScrollPane,BorderLayout.CENTER);
        this.add(messageInput,BorderLayout.SOUTH);
        this.setVisible(true);



       }
 
       

    

	private void startReading() {
		Runnable r1=()->{
            System.out.println("Reader Started...");
            try{
            while(!socket.isClosed()){
            
                    String msg=br.readLine();
                if(msg.equals("exit"))
                {
                    System.out.println("Clinet terminated the chat");
                    JOptionPane.showMessageDialog(this,"Server Terminate");
                    messageInput.setEnabled(false);
                    socket.close();
                    break;

                }
                //System.out.println("Client : "+msg);
                messageArea.append("Client :"+msg+"\n");
                }

            }catch(Exception e){
               // e.printStackTrace();
               System.out.println("Connection is closed");
            }

        };
        new Thread(r1).start();

    }
    private void startWriting() {
    	 Runnable r2=()->
         {
             System.out.println("Writetr Started....");
             try{
             while(!socket.isClosed()){
            
                     BufferedReader br1=new BufferedReader(new InputStreamReader(System.in));
                     
                     String content=br1.readLine();
                     out.println(content);
                     out.flush();

                     if(content.equals("exit")){
                        socket.close();
                        break;
                     }
                     

                 }
             }catch(Exception e){
               // e.printStackTrace();
               System.out.println("Connection is closed");
            }
            

         };
         new Thread(r2).start();
        
    }
	
	public static void main(String [] args)
    {
        System.out.println("This is server...going to Start Server");
        new Server();
    }
}
