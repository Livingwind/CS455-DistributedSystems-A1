package cs455.overlay.transport;


import cs455.overlay.wireformats.Event;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;

public class TCPConnection extends Thread {
  private TCPReceiverThread threadRecv;
  private TCPSenderThread threadSend;
  private Socket sock;

  private LinkedBlockingQueue<Event> queueRecv = new LinkedBlockingQueue<>();
  private LinkedBlockingQueue<Event> queueSend = new LinkedBlockingQueue<>();

  public TCPConnection(String host, int port) {
    try {
      this.sock = new Socket(host, port);
    } catch (IOException e) {
      System.err.println(e);
    }

    threadRecv = new TCPReceiverThread(queueRecv, sock);
    threadSend = new TCPSenderThread(queueSend, sock);
  }

  public TCPConnection(Socket sock) {
    this.sock = sock;
    threadRecv = new TCPReceiverThread(queueRecv, sock);
    threadSend = new TCPSenderThread(queueSend, sock);
  }

  @Override
  public void run() {
    System.out.println("STARTING TCPCONNECTION");
    threadRecv.start();
    threadSend.start();

    while (!Thread.interrupted() && !sock.isClosed()) {}

    try {
      sock.close();
      threadRecv.killMe();
      threadSend.interrupt();

      threadRecv.join();
      threadSend.join();
    } catch (Exception e) {
      System.err.println(e);
    }

    System.out.println("ENDING TCPCONNECTION");
  }

  public String getHost () {
    return sock.getInetAddress().getCanonicalHostName();
  }

  @Override
  public boolean equals (Object obj) {
    if (obj instanceof String) {
      String other = (String) obj;
      return getHost().equals(other);
    }
    else if (obj instanceof TCPConnection) {
      TCPConnection other = (TCPConnection) obj;

      // The socket should have a JVM hashcode that's unique
      return sock == other.sock;
    }
    return false;
  }

  public Socket getSock () {
    return sock;
  }

  @Override
  public int hashCode() {
    return getHost().hashCode();
  }

  public synchronized void sendMessage (Event e) {
    queueSend.add(e);
  }

  public synchronized Event receiveMessage () {
    return queueRecv.poll();
  }

  public synchronized byte checkMessage () {
    Event event = queueRecv.peek();
    if (event != null)
      return event.getType();
    return 0;
  }
}
