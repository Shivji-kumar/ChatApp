import java.net.*;

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
import javax.swing.border.Border;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.TrayIcon.MessageType;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class Client extends JFrame {
    Socket socket;
    BufferedReader br;
    
    PrintWriter out;

    private JLabel heading=new JLabel("Client Side");
    private JTextArea messageArea=new JTextArea();
    private JTextField messageInput=new JTextField();
    private Font font=new Font("Robot",Font.PLAIN,20);




    public Client(){
       try{
        System.out.println("Sendig Request to Server");
        socket=new Socket("127.0.0.1",7778);

        System.out.println("Connection done. ");

        br=new BufferedReader(new InputStreamReader(socket.getInputStream()));

        out=new PrintWriter(socket.getOutputStream());

        createGUI();
        handleEvents();

         startReading();
        // startWriting();

       }catch(Exception e){
        e.printStackTrace();

       }
    }

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
                //System.out.println("Key Released "+e.getKeyCode());
                if(e.getKeyCode()==10){
                    System.out.println("You have Pressed Enter Button");
                    String contentToSend=messageInput.getText();
                    messageArea.append("Me :"+contentToSend+"\n");
                    out.println(contentToSend);
                    out.flush();
                    messageInput.setText("");
                    messageInput.requestFocus();
                }

        
                
            }
            
        });

    }
    private void createGUI(){
        this.setTitle("Client Messanger[END]");
        this.setSize(600,800);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //coding for component
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

        //frame Layeout
        this.setLayout(new BorderLayout());

        //adding the component to frame
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
                   System.out.println("Server terminated the chat");
                   JOptionPane.showMessageDialog(this," Server Terminated");
                   messageInput.setEnabled(false);
                   socket.close();
                   break;

               }
              // System.out.println("Server : "+msg);
              messageArea.append("Server : "+msg+"\n");
            }
        }catch(Exception e){
                //e.printStackTrace();
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
                System.out.println("Connection closed");
            }catch(Exception e){
                //e.printStackTrace();
                System.out.println("Connection is closed");
            }

        };
        new Thread(r2).start();
       
   }

    public static void main(String [] args)
    {
        System.out.println("This is Client Side ");
        new Client();
    }

    
}
