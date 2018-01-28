package cs455.overlay.wireformats;

    import java.io.*;

public class RegistryReportsDeregistrationStatus implements Event {
  private byte type;
  private int status;   // ID if successful, -1 otherwise
  private String info;

  public RegistryReportsDeregistrationStatus (int status, String info) {
    this.type = Protocol.REGISTRY_REPORTS_DEREGISTRATION_STATUS;
    this.status = status;
    this.info = info;
  }

  // MESSAGE PROTOCOL
  /*
  byte: REGISTRY_REPORTS_REGISTRATION_STATUS
  int: Success status
  byte: Length of information field
  byte[^^]: Information string
   */

  public RegistryReportsDeregistrationStatus(byte[] bytes) {
    try (ByteArrayInputStream instream = new ByteArrayInputStream(bytes);
         DataInputStream din
             = new DataInputStream(new BufferedInputStream(instream))) {

      type = din.readByte();
      status = din.readInt();

      byte infoLen = din.readByte();
      byte[] infoBytes = new byte[infoLen];
      din.readFully(infoBytes);
      info = new String(infoBytes);
    } catch (IOException e) {
      System.err.println(e);
    }
  }

  @Override
  public byte[] getBytes() {
    byte[] bytes = null;
    try (ByteArrayOutputStream outstream = new ByteArrayOutputStream();
         DataOutputStream dout
             = new DataOutputStream(new BufferedOutputStream(outstream))) {

      dout.writeByte(type);
      dout.writeInt(status);
      dout.writeByte(info.length());
      dout.writeBytes(info);

      bytes = outstream.toByteArray();
    } catch (IOException e) {
      System.err.println(e);
    }
    return bytes;
  }

  public int getStatus () {
    return status;
  }
  public String getInfo () {
    return info;
  }

  @Override
  public byte getType() {
    return type;
  }

  @Override
  public String toString () {
    return String.format("TYPE: %1\n" + "STATUS: %2\n" + "INFO: %3", type, status, info);
  }
}