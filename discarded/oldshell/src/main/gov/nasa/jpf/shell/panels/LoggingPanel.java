package gov.nasa.jpf.shell.panels;

import gov.nasa.jpf.shell.ShellManager;
import gov.nasa.jpf.shell.ShellPanel;
import gov.nasa.jpf.shell.util.ErrorPanelHandler;
import gov.nasa.jpf.shell.util.HyperlinkEditorPane;
import gov.nasa.jpf.shell.util.Tuple;
import gov.nasa.jpf.shell.util.hyperlinks.JavaSourceFileHyperlinkPattern;
import gov.nasa.jpf.shell.util.hyperlinks.StacktraceHyperlinkPattern;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * Displays all of the errors logged during the execution of jpf-shell
 * @author Sandro Badame <a href="mailto:s.badame@gmail.com">s.badame&amp;gmail.com</a>
 */
public class LoggingPanel extends ShellPanel{

  private static final String TITLE = "Logger";

  private final int VISIBLE_ERRORS = 5; //How many error rows to show by default

  //What the columns in the table are
  private static final ArrayList<Tuple<String, ErrorInfo2Cell>> COLUMNS = new ArrayList<Tuple<String, ErrorInfo2Cell>>(){{
    add(new Tuple<String, ErrorInfo2Cell>("Time",    new ErrorInfo2Cell<Date>(){public Date convert(ErrorInfo ei){return ei.time;}}));
    add(new Tuple<String, ErrorInfo2Cell>("Type",    new ErrorInfo2Cell<ErrorType>(){public ErrorType convert(ErrorInfo ei){return ei.type;}}));
    add(new Tuple<String, ErrorInfo2Cell>("Level",   new ErrorInfo2Cell<Level>(){public Level convert(ErrorInfo ei){return ei.level;}}));
    add(new Tuple<String, ErrorInfo2Cell>("Message", new ErrorInfo2Cell<String>(){public String convert(ErrorInfo ei){return ei.message;}}));
  }};

  public enum ErrorType {
    //Text followed by background color
    UNKNOWN ("Unknown"),
    Shell ("Shell"),
    JPF ("JPF");

    private String str;
    ErrorType(String str){this.str = str;}
    @Override
    public String toString(){return str;}
  }

  //Displays the information about the error
	private HyperlinkEditorPane errorPane = new HyperlinkEditorPane();
  private int errorCount = 0;

  //Error table
  ErrorTableModel errors = new ErrorTableModel();
  JTable errorTable = new JTable(errors){{
    setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    getSelectionModel().addListSelectionListener(new ListSelectionListener() {
      public void valueChanged(ListSelectionEvent lse) {
        updateErrorSelection(lse);
      }
    });
    setAutoCreateRowSorter(true);
    setDefaultRenderer(ErrorType.class, new CenteredCellRenderer());
    setDefaultRenderer(Level.class, new CenteredCellRenderer());
    setDefaultRenderer(Date.class, new CenteredCellRenderer());
  }};

	public LoggingPanel(){
		super(TITLE, null, "View any Errors that occured during JPF's run");
    setLayout(new GridLayout());

		JScrollPane scroller = new JScrollPane(errorPane);
		scroller.getViewport().setBackground(Color.WHITE);
    JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT, new JScrollPane(errorTable), scroller);
		add(split);

    //I got this code from http://www.javalobby.org/java/forums/t19559.html
    errorTable.setPreferredScrollableViewportSize(new Dimension(errorTable.getPreferredScrollableViewportSize().width, VISIBLE_ERRORS*errorTable.getRowHeight()));

    ShellManager.getManager().getLogger().addHandler(new ErrorPanelHandler(ErrorType.Shell, this));
    Logger.getLogger("gov.nasa.jpf").addHandler(new ErrorPanelHandler(ErrorType.JPF, this));

    errorPane.addHyperlinkPattern(new JavaSourceFileHyperlinkPattern());
    errorPane.addHyperlinkPattern(new StacktraceHyperlinkPattern());
	}

  public void updateErrorSelection(ListSelectionEvent lse){
    if (lse.getValueIsAdjusting() || errorTable.getSelectedRow() < 0) { return;}
    ErrorInfo error = errors.getErrorInfo(errorTable.getSelectedRow());

    StringBuilder info = new StringBuilder();
    info.append("Message: ").append(error.message);
    info.append("\nTime: ").append(error.time);
    info.append("\nType: ").append(error.type);

    if (error.thrown != null) {
      info.append("\n=================== Stack Trace ========================\n");
      StringWriter stringWriter = new StringWriter();
      if (error.thrown != null) {
        error.thrown.printStackTrace(new PrintWriter(stringWriter));
      }
      info.append(stringWriter.getBuffer());
    }

    errorPane.setText(info.toString());
    errorPane.setCaretPosition(0);
  }
	
  public void publish(LogRecord lr) { publish(ErrorType.UNKNOWN, lr); }

  public void publish(ErrorType category, LogRecord record){
    errors.addError(new ErrorInfo(category, record));
    errorTable.tableChanged(new TableModelEvent(errors));
    if (isVisibleInShell() == false && getShell() != null) {
      errorCount++;
      setTitle(TITLE + " (" + errorCount + ")");
      getShell().updateShellPanel(this);
    }
  }

  @Override
  protected void receivedFocus(){
    errorCount = 0;
    setTitle(TITLE);
    getShell().updateShellPanel(this);
  }

  private class ErrorInfo {
    public Level level;
    public Throwable thrown;
    public ErrorType type;
    public Date time;
    public String message;

    public ErrorInfo(ErrorType type, LogRecord record) {
      level = record.getLevel();
      thrown = record.getThrown();
      this.type = type;
      this.time = new Date(record.getMillis());
      this.message = record.getMessage();
    }

  }

  private class ErrorTableModel extends AbstractTableModel{
    private  ArrayList<ErrorInfo> errorList = new ArrayList<ErrorInfo>();

    private ErrorTableModel() {}

    public int getRowCount() {
      return errorList.size();
    }

    public int getColumnCount() {
      return COLUMNS.size();
    }

    public Object getValueAt(int row, int column) {
      return COLUMNS.get(column).b.convert(errorList.get(row));
    }

    public void addError(ErrorInfo ei){
      errorList.add(ei);
    }

    @Override
    public boolean isCellEditable(int r, int c){ return false;}

    @Override
    public String getColumnName(int col){ return COLUMNS.get(col).a; }

    public ErrorInfo getErrorInfo(int index){ 
      return errorList.get(index);
    }

    @Override
    /**
     * JTable uses this to determine what "Class" a column is representing
     * My hack, just return the class of the first row of the given column
     */
    public Class getColumnClass(int c){
      return COLUMNS.get(c).b.getReturningClass();
    }
  }

  abstract static class ErrorInfo2Cell<A>{
    abstract A convert(ErrorInfo ei);
    public Class<A> getReturningClass(){
      ParameterizedType pt = (ParameterizedType) getClass().getGenericSuperclass();
      return (Class) pt.getActualTypeArguments()[0];
    }
  }

  private class CenteredCellRenderer extends DefaultTableCellRenderer{
    public CenteredCellRenderer(){
      super();
      setHorizontalAlignment(DefaultTableCellRenderer.CENTER);
    }
  }

}
