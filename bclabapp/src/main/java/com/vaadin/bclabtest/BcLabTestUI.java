package com.vaadin.bclabtest;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.annotations.Widgetset;
import com.vaadin.bclabtest.backend.Plate;
import com.vaadin.bclabtest.backend.PlateService;
import com.vaadin.event.SelectionEvent;
import com.vaadin.event.SelectionEvent.SelectionListener;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.shared.ui.combobox.FilteringMode;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.Grid;

/* 
 *
 * This class build the UI for bclabtestapp
 * 
 * User Interface written in Vaadin and Java.
 * 
 */
@Title("BC Platform App")
@Theme("valo")
@Widgetset("com.vaadin.v7.Vaadin7WidgetSet")
public class BcLabTestUI extends UI {

	private static final long serialVersionUID = 1L;
	ComboBox combobox = new ComboBox("Sample plate Ids:");
	Button editSample = new Button("Move Sample");
    Grid plateGrid = new Grid();
    PlateForm plateForm = new PlateForm();
    PlateService service = PlateService.createDemoService();
    
    List<Plate> plates = new ArrayList<Plate>();
    List<String> plateIds = new ArrayList<String>();
    
    double[][] volndoubles = new double[8][];
    String[][] volnStr = new String[8][];
    long[][] idnlongs = new long[8][];
    
    @Override
    protected void init(VaadinRequest request) {
        configureComponents();
        buildLayout();
    }

    private void configureComponents() {
    	
    	plates = service.findAll("");
    	for(int i=0; i<plates.size(); i++){
    		plateIds.add(plates.get(i).getPlateId());
    	}
    	
    	HashSet plateIdsHashset = new HashSet();
    	plateIdsHashset.addAll(plateIds);
    	plateIds.clear();
    	plateIds.addAll(plateIdsHashset);
    	
    	for(int i=0; i<plateIds.size(); i++){
    		combobox.addItem(plateIds.get(i));
    	}
    	
    	combobox.setNullSelectionAllowed(false);
    	
    	combobox.setValue(plateIds.get(0));
    	
    	combobox.setFilteringMode(FilteringMode.CONTAINS);
    	combobox.addListener(new Property.ValueChangeListener() {
			
			@Override
			public void valueChange(ValueChangeEvent event) {
				StringBuffer sb = new StringBuffer();
		        sb.append(combobox.getValue());
		        Notification.show("Selected Plate = " + sb);
		        updateGrid((String)combobox.getValue());
			}
		});
    	plateGrid.addSelectionListener(new SelectionListener() {

            @Override
               public void select(SelectionEvent event) {
                   Notification.show("Select row: "+plateGrid.getSelectedRow());
               }
        });
        
    	editSample.addClickListener(e -> plateForm.edit(new Plate()));
    	
        for (int i = 0; i < 8; i++) {
        	volndoubles[i] = new double[12];
        	volnStr[i] = new String[12];
        	idnlongs[i] = new long[12];
        	for (int j = 0; j < 12; j++) {
        		volnStr[i][j] = "Empty";
        	}
        }
        
        Map<String, Integer> rowMap = new HashMap<String, Integer>();
        rowMap.put("A", 0);
        rowMap.put("B", 1);
        rowMap.put("C", 2);
        rowMap.put("D", 3);
        rowMap.put("E", 4);
        rowMap.put("F", 5);
        rowMap.put("G", 6);
        rowMap.put("H", 7);
        
        DecimalFormat df = new DecimalFormat("#0.00");
        
        for(int i=0; i<plates.size(); i++){
    		int rowNum = (int) rowMap.get(plates.get(i).getRowId());
    		int colNum = plates.get(i).getColumnId();
    		volndoubles[rowNum][colNum] = plates.get(i).getVolume();
    		volnStr[rowNum][colNum] = String.valueOf(df.format(plates.get(i).getVolume())) + "(ID0000"+plates.get(i).getId() + ")";
        	idnlongs[rowNum][colNum] = plates.get(i).getId();
    	}
        
        for (int j = 0; j < 12; j++) {
        	plateGrid.addColumn(""+(j+1)+"", String.class);
        	plateGrid.getColumn(""+(j+1)+"").setSortable(false);
        }
        
        for (int i = 0; i < volnStr.length; i++) {
            plateGrid.addRow(volnStr[i][0],
            		volnStr[i][1],
            		volnStr[i][2],
            		volnStr[i][3],
            		volnStr[i][4],
            		volnStr[i][5],
            		volnStr[i][6],
            		volnStr[i][7],
            		volnStr[i][8],
            		volnStr[i][9],
            		volnStr[i][10],
            		volnStr[i][11]);

            
        }
        
        plateGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
        plateGrid.addSelectionListener(new SelectionListener() {

        @Override
           public void select(SelectionEvent event) {
               Notification.show("Select row: "+plateGrid.getSelectedRow());
           }
        });
    }

    /*
     * Building layout.
     */
    private void buildLayout() {
    	HorizontalLayout actions = new HorizontalLayout(combobox, editSample);
        actions.setWidth("100%");
        combobox.setWidth("40%");
        actions.setExpandRatio(combobox, 1);
    	
        VerticalLayout left = new VerticalLayout(actions, plateGrid);
        left.setSizeFull();
        plateGrid.setSizeFull();
        left.setExpandRatio(plateGrid, 1);

        HorizontalLayout mainLayout = new HorizontalLayout(left, plateForm);
        mainLayout.setSizeFull();
        mainLayout.setExpandRatio(left, 1);

        setContent(mainLayout);
    }
    
    private void updateGrid(String plateIdFromCombo){
    	
    	plates = service.findAll(plateIdFromCombo);
    	
    	for (int i = 0; i < 8; i++) {
        	volnStr[i] = new String[12];
        	for (int j = 0; j < 12; j++) {
        		volnStr[i][j] = "Empty";
        	}
        }
        
        Map<String, Integer> rowMap = new HashMap<String, Integer>();
        rowMap.put("A", 0);
        rowMap.put("B", 1);
        rowMap.put("C", 2);
        rowMap.put("D", 3);
        rowMap.put("E", 4);
        rowMap.put("F", 5);
        rowMap.put("G", 6);
        rowMap.put("H", 7);
        
        DecimalFormat df = new DecimalFormat("#0.00");
        
        for(int i=0; i<plates.size(); i++){
    		int rowNum = (int) rowMap.get(plates.get(i).getRowId());
    		int colNum = plates.get(i).getColumnId();
    		volnStr[rowNum][colNum] = String.valueOf(df.format(plates.get(i).getVolume())) + "(ID0000"+plates.get(i).getId() + ")";
    	}
        System.out.println("volnStr array updated: " + Arrays.deepToString(volnStr));
       
        for (int i = 0; i < volnStr.length; i++) {
            plateGrid.addRow(volnStr[i][0],
            		volnStr[i][1],
            		volnStr[i][2],
            		volnStr[i][3],
            		volnStr[i][4],
            		volnStr[i][5],
            		volnStr[i][6],
            		volnStr[i][7],
            		volnStr[i][8],
            		volnStr[i][9],
            		volnStr[i][10],
            		volnStr[i][11]);
            
        }
    }
    
    void refreshContacts() {
        plateForm.setVisible(false);
    }
    
    @WebServlet(urlPatterns = "/*")
    @VaadinServletConfiguration(ui = BcLabTestUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }

}
