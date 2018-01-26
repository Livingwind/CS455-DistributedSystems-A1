package cs455.overlay.node;

import cs455.overlay.transport.TCPConnectionsCache;

public class Registry extends Node {
  private TCPConnectionsCache cache;

  Registry (int port) {
    super(port);

    System.out.println("STARTING REGISTRY...");
    startThreads();

    String msg;
    do {
        msg = command.getMessage();
        if (msg != null)
          System.out.println("ENTERED: " + msg);
        if (msg != null && msg.equals("exit")) {
          System.out.println("RECEIVED EXIT");
          break;
        }
    } while (true);

    stopAllThreads();
  }

  public static void main(String[] args){
    int port = 33000;
    if(args.length == 1)
      port = Integer.parseInt(args[0]);
    else
      System.out.println("PORT NOT SPECIFIED");
    System.out.println("STARTING SERVER ON PORT " + port);
    new Registry(port);
  }
}