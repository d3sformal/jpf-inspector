package gov.nasa.jpf.shell.util;

import gov.nasa.jpf.shell.ShellManager;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.logging.Level;

/**
 * This text component allows for text to be associated with an object.
 * This allows for chunks of text to be hidden or a different style.
 */
public class StreamDisplay extends FilterableTextComponent{

  HashMap<InputStream, StreamReader> streams = new HashMap<InputStream, StreamReader>();

	//Output Reader uses this to keep track of how many readers we have
	private static int count = 0;

	public StreamDisplay(){
    super();
  }

	/**
   * Reads strings from the InputStream and displays it.
	 * To apply a style to the contents of the stream use
	 * {@link #getStreamStyle(java.lang.String) } to change the style of the text.
   * @param is stream to read and display
   */
	public void addStream(InputStream is, boolean isVisible){
		if (streams.containsKey(is)) { return; }

    setVisible(is, isVisible);
    StreamReader or = new StreamReader(is);
		streams.put(is, or);
    or.start();
	}


	/**
	 * Remove all of the text and streams in the display.
	 */
	public void reset() {
		setText(null);
		for (StreamReader streamData : streams.values()) {
			streamData.kill();
		}
		streams.clear();
	}

	//Arggg three inner classes coming up!!
	//----------------------------------------------------------------------------

	private class StreamReader extends Thread{

    private InputStream is;
		private boolean run = true;

    public StreamReader(InputStream sd){
      super("OutputReader - " + streams.size());
      this.is = sd;
    }

    @Override
    public void run() {
      BufferedReader reader = new BufferedReader(new InputStreamReader(is));
      String output =  null;
      try{
        while( run && (output = reader.readLine()) != null){
					synchronized(StreamDisplay.this){
						appendText(is, output + "\n");
					}
        }
			}catch(IOException ioe){
        //All done here
				//ShellManager.getManager().getLogger().log(Level.INFO, "Closing Stream", ioe);
      }catch(Exception e){
				ShellManager.getManager().getLogger().log(Level.SEVERE, "In OutputReader", e);
      }
			try {
				reader.close();
			} catch (IOException ex) {
				ShellManager.getManager().getLogger().log(Level.WARNING, "Closing OutputReader", ex);
			}
    }

    public void kill(){
      this.run = false;
    }

	}
}