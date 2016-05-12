package gov.nasa.jpf.shell.panels;

import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;

import gov.nasa.jpf.Config;
import gov.nasa.jpf.shell.ShellManager;
import gov.nasa.jpf.shell.ShellPanel;
import gov.nasa.jpf.shell.util.LinkDestination;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.net.URL;
import java.util.Map.Entry;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

/**
 *	This panel is used to display the current values of the current Config
 *	instance and where the source of the values.
 *  @author Sandro Badame <a href="mailto:s.badame@gmail.com">s.badame&amp;gmail.com</a>
 */
public class ConfigPanel extends ShellPanel {

	private static final String[] columns = {"Key", "Value"};
	private JTable table;
	private Object[][] data;
  private ConfigTableModel configTableModel;
  
  private DefaultListModel sourceListModel = new DefaultListModel();
	private JList sourceList;
	private TitledBorder sourceListBorder = BorderFactory.createTitledBorder("Files that define the selected Property");

	private LinkedHashMap<Properties, Object> loadedProperties;
	private LinkedHashMap<String, LinkedHashMap<Object, Integer>> cachedPropertyLocations;

  private JLabel propertyName = new JLabel();
  private JTextArea propertyValue = new JTextArea(){{
    setEditable(false);
    setLineWrap(true);
    setWrapStyleWord(true);
  }};


	public ConfigPanel(){
		super("Config", null, "View the current Config Object.");

		initData( ShellManager.getManager().getConfig());
    configTableModel = new ConfigTableModel(data, columns);
    
		table = new JTable(configTableModel);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent lse) {
				updatePropertySelection(lse);
			}

		});

    sourceListModel.addElement("None");
		sourceList = new JList(sourceListModel);
		sourceList.setBorder(sourceListBorder);
		sourceListBorder.setTitleJustification(TitledBorder.CENTER);
		sourceList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		sourceList.setBackground(Color.WHITE);
		sourceList.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent me){
				//Double click with the left mousebutton
				if (me.getButton() == MouseEvent.BUTTON1 && me.getClickCount() == 2) {
					sourceList.setSelectedIndex(sourceList.locationToIndex(me.getPoint()));
					openSelectedSource();
				}
			}

			@Override
			public void mousePressed(MouseEvent me){ mouseReleased(me); }

			@Override
			public void mouseReleased(MouseEvent me){
				if (!me.isPopupTrigger())
					return;
				sourceList.setSelectedIndex(sourceList.locationToIndex(me.getPoint()));
				JPopupMenu sourceRightClickMenu = new JPopupMenu();
				JMenuItem popupItem = new JMenuItem("Open this source file");
				popupItem.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent ae) {
						openSelectedSource();
					}
				});
				sourceRightClickMenu.add(popupItem);
				sourceRightClickMenu.show(sourceList, (int)me.getPoint().getX(),
																							(int)me.getPoint().getY());

			}

		});
    JPanel propertyDisplay = new JPanel();
    propertyDisplay.add(propertyName);
    propertyDisplay.add(propertyValue);
    propertyDisplay.add(sourceList);

		JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT, new JScrollPane(table), propertyDisplay);
    SpringLayout springLayout = new SpringLayout();
    //Name
    springLayout.putConstraint(SpringLayout.NORTH, propertyName, 0, SpringLayout.NORTH, propertyDisplay);
    springLayout.putConstraint(SpringLayout.WEST, propertyName, 0, SpringLayout.WEST, propertyDisplay);

    //Value
    springLayout.putConstraint(SpringLayout.NORTH, propertyValue, 0, SpringLayout.NORTH, propertyDisplay);
    springLayout.putConstraint(SpringLayout.SOUTH, propertyValue, 0, SpringLayout.NORTH, sourceList);
    springLayout.putConstraint(SpringLayout.EAST,  propertyValue, 0, SpringLayout.EAST, propertyDisplay);
    springLayout.putConstraint(SpringLayout.WEST,  propertyValue, 0, SpringLayout.EAST, propertyName);

    //Sources
    springLayout.putConstraint(SpringLayout.SOUTH, sourceList, 0, SpringLayout.SOUTH, propertyDisplay);
    springLayout.putConstraint(SpringLayout.EAST, sourceList, 0, SpringLayout.EAST, propertyDisplay);
    springLayout.putConstraint(SpringLayout.WEST, sourceList, 0, SpringLayout.WEST, propertyDisplay);

		setLayout(new BorderLayout());
    propertyDisplay.setLayout(springLayout);

    split = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true, new JScrollPane(table), new JScrollPane(propertyDisplay));
		split.getBottomComponent().setMinimumSize(new Dimension(0, sourceList.getPreferredSize().height + propertyValue.getPreferredSize().height));
		split.getTopComponent().setMinimumSize(new Dimension(0,250));
		split.getBottomComponent().setPreferredSize(new Dimension(300, 300));
		split.getTopComponent().setPreferredSize(new Dimension(500, 300));

		add(split, BorderLayout.CENTER);
	}

  protected void initData (Config config){
		//As many rows as there are loadedProperties, 3 entries, key, value, source
		data = new Object[config.size()][2];
    loadedProperties = new LinkedHashMap<Properties, Object>();
    cachedPropertyLocations = new LinkedHashMap<String, LinkedHashMap<Object, Integer>>();

		LinkedHashMap<Properties, String> sourceMap = new LinkedHashMap<Properties, String>();
		for (Object source : config.getSources()) {
			Properties props = new Properties();
			InputStream is = null;
			try{
				if (source instanceof File) {
					is = new FileInputStream((File)source);
				}else if (source instanceof URL){
					is = ((URL)source).openStream();
				}
			}catch(FileNotFoundException fnfe){
				getShell().error("File not found: " + source, fnfe);
			}catch(IOException ioe){
				getShell().error("IOException: " + source, ioe);
			}
			try {
				props.load(is);
				sourceMap.put(props, config.getSourceName(source));
			} catch (IOException ex) {
				getShell().error("Couldn't load properties from " + source, ex);
			}
			loadedProperties.put(props, source);
		}

		int i = 0;
		for (Entry<Object, Object> entry : config.entrySet()) {
			data[i][0] = entry.getKey().toString();
			data[i][1] = entry.getValue().toString();
			i++;
		}

		//Until we start editing values, I don't think that we need a row sorter
		Arrays.sort(data, new Comparator<Object[]>(){
			public int compare(Object[] t, Object[] t1) {
				return ((String)t[0]).compareTo(((String)t1[0]));
			}
		});
  }

  public void configChanged(Config config){
    initData(config);
    
    configTableModel.setDataVector(data, columns);    
    sourceListModel.clear();
  }
  
	private void updatePropertySelection(ListSelectionEvent lse) {
		if (lse.getValueIsAdjusting()) { 
      return;
    }
    
    int selectedRow = table.getSelectedRow();
    if (selectedRow >= 0) {

      String key = data[selectedRow][0].toString();
      String value = data[selectedRow][1].toString();
      sourceListBorder.setTitle("Property: \"" + key + "\" is defined in");
      sourceListBorder.setTitleColor(Color.BLUE);
      propertyName.setText(key + " = ");
      propertyValue.setText(value);

      sourceListModel.clear();
      //Remove any selection
      sourceList.setSelectedIndex(-1);

      //Check if we've seen this property before
      if (!cachedPropertyLocations.containsKey(key)) {
        LinkedHashMap<Object, Integer> cached_locations = new LinkedHashMap<Object, Integer>();
        for (Properties properties : loadedProperties.keySet()) {
          if (properties.containsKey(key)) {
            cached_locations.put(loadedProperties.get(properties), null);
          }
        }
        cachedPropertyLocations.put(key, cached_locations);
      }

      if (cachedPropertyLocations.get(key).keySet().isEmpty()) {
      } else {
        for (Object source : cachedPropertyLocations.get(key).keySet()) {
          sourceListModel.addElement(source);
        }
      }
    }
    
		sourceList.repaint();
	}

	private void openSelectedSource(){
		Object source = sourceList.getSelectedValue();

		//Get the selected key
		String key = data[table.getSelectedRow()][0].toString();

		if (source instanceof File) {
			int line_number = 1;
			LinkedHashMap<Object, Integer> locations = cachedPropertyLocations.get(key);
			if (locations.get(source) != null) {
				line_number = locations.get(source);
			}else{
				File sourceFile = (File) source;
				try {
					//Time to find the line number
					BufferedReader fileReader = new BufferedReader(new FileReader(sourceFile));
					String in;
					while ( (in = fileReader.readLine()) != null){
						if (in.trim().startsWith(key)) {
							break;
						}else{
							line_number++;
						}
					}
					fileReader.close();
				} catch (FileNotFoundException ex) {
					getShell().error("Couldn't find line number beecause source file"+
									" does not exist: " + sourceFile, ex);
				} catch (IOException ioe){
					getShell().error("Couldn't close file reader for "+ sourceFile, ioe);
				}
				locations.put(source, line_number);
			}
			ShellManager.getManager().printLinkCommand(new LinkDestination(((File)source).getAbsolutePath(), line_number));
		}else if (source instanceof URL){
			//TODO
		}
	}

	/**
	 * This is the only way to make the cells in the JTable non-editable.
	 */
	class ConfigTableModel extends DefaultTableModel{

		private ConfigTableModel(Object[][] data, String[] columns) {
			super(data, columns);
		}
		@Override
		public boolean isCellEditable(int row, int column){
			return false;
		}

	}
}
