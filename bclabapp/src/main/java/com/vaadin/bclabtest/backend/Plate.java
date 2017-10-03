package com.vaadin.bclabtest.backend;

import org.apache.commons.beanutils.BeanUtils;

import java.io.Serializable;


public class Plate implements Serializable, Cloneable {

	private static final long serialVersionUID = 1L;

	private Long id;

    private String plateId = "";
    private String rowId = "";
    private int columnId = 1;
    private double volume = 0.0;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPlateId() {
        return plateId;
    }

    public void setPlateId(String plateId) {
        this.plateId = plateId;
    }

    public String getRowId() {
        return rowId;
    }

    public void setRowId(String rowId) {
        this.rowId = rowId;
    }

    public int getColumnId() {
        return columnId;
    }

    public void setColumnId(int columnId) {
        this.columnId = columnId;
    }

    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }
    

    @Override
    public Plate clone() throws CloneNotSupportedException {
        try {
            return (Plate) BeanUtils.cloneBean(this);
        } catch (Exception ex) {
            throw new CloneNotSupportedException();
        }
    }

    @Override
    public String toString() {
        return "Plate{" + "id=" + id + ", plateId=" + plateId
                + ", rowId=" + rowId + ", columnId=" + columnId + ", volume="
                + volume + '}';
    }

}
