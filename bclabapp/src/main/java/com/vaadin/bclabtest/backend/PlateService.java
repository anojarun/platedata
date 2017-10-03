package com.vaadin.bclabtest.backend;

import org.apache.commons.beanutils.BeanUtils;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PlateService {

    // Create dummy data by randomly combining first and last names
    static String[] plateids = { "plate12345"};

    private static PlateService instance;

    public static PlateService createDemoService() {
        if (instance == null) {

            final PlateService plateService = new PlateService();
            
            Map<Integer, String> rowMap = new HashMap<Integer, String>();
            rowMap.put(0, "A");
            rowMap.put(1, "B");
            rowMap.put(2, "C");
            rowMap.put(3, "D");
            rowMap.put(4, "E");
            rowMap.put(5, "F");
            rowMap.put(6, "G");
            rowMap.put(7, "H");
            
            double dbl = 0.33;
            for (int i = 0; i < plateids.length; i++) {
            	for (int j = 0; j < 8; j++) {
            		dbl = dbl + 0.05;
            		for (int k = 0; k < 12; k++) {
            			if(i==1 && j==1 && k==1) continue;
            			if((j==0 && k==1)	|| (j==1 && k==3)) continue;
		                Plate plate = new Plate();
		                plate.setPlateId(plateids[i]);
		                plate.setRowId(rowMap.get(j));
		                plate.setColumnId(k);
		                plate.setVolume((dbl/(k+1)));
		                plateService.save(plate);
            		}
            	}
            }
            instance = plateService;
        }

        return instance;
    }

    private HashMap<Long, Plate> plates = new HashMap<>();
    private long nextId = 0;

    public synchronized List<Plate> findAll(String stringFilter) {
        ArrayList arrayList = new ArrayList();
        for (Plate plate : plates.values()) {
            try {
                boolean passesFilter = (stringFilter == null || stringFilter.isEmpty())
                        || plate.toString().toLowerCase()
                                .contains(stringFilter.toLowerCase());
                if (passesFilter) {
                    arrayList.add(plate.clone());
                }
            } catch (CloneNotSupportedException ex) {
                Logger.getLogger(PlateService.class.getName()).log(
                        Level.SEVERE, null, ex);
            }
        }
        Collections.sort(arrayList, new Comparator<Plate>() {

            @Override
            public int compare(Plate o1, Plate o2) {
                return (int) (o2.getId() - o1.getId());
            }
        });
        return arrayList;
    }

    public synchronized long count() {
        return plates.size();
    }

    public synchronized void delete(Plate value) {
        plates.remove(value.getId());
    }

    public synchronized void save(Plate entry) {
        if (entry.getId() == null) {
            entry.setId(nextId++);
        }
        try {
            entry = (Plate) BeanUtils.cloneBean(entry);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        
        plates.put(entry.getId(), entry);
    }

}
