package com.vaadin.bclabtest;

import com.vaadin.bclabtest.backend.Plate;
import com.vaadin.event.ShortcutAction;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.fieldgroup.FieldGroup;

/*
 * This class is for editing cell
 *  
 */
public class PlateForm extends FormLayout {

	private static final long serialVersionUID = 1L;
	Button save = new Button("Modify", this::save);
    Button cancel = new Button("Cancel", this::cancel);
    TextField plateId = new TextField("Plate Id");
    TextField sampleId = new TextField("Sample Id");
    TextField oldRowId = new TextField("Old Row Id");
    TextField oldColumnId = new TextField("Old Column Id");
    TextField newRowId = new TextField("New Row Id");
    TextField newColumnId = new TextField("New Column Id");

    Plate plate;
    BeanFieldGroup<Plate> formFieldBindings;

    public PlateForm() {
        configureComponents();
        buildLayout();
    }

    private void configureComponents() {
        save.setStyleName(ValoTheme.BUTTON_PRIMARY);
        save.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        setVisible(false);
    }

    private void buildLayout() {
        setSizeUndefined();
        setMargin(true);

        HorizontalLayout actions = new HorizontalLayout(save, cancel);
        actions.setSpacing(true);

        addComponents(actions, plateId, sampleId, oldRowId, oldColumnId, newRowId, newColumnId);
    }

    public void save(Button.ClickEvent event) {
        //try {
            // Commit the fields from UI to DAO
            //formFieldBindings.commit();
            //getUI().service.save(plate);

            String msg = String.format("Moving '%s' TBD.", sampleId.getValue());
            Notification.show(msg, Type.TRAY_NOTIFICATION);
            plateId.clear();
            sampleId.clear();
            oldRowId.clear();
            oldColumnId.clear();
            newRowId.clear();
            newColumnId.clear();
            getUI().refreshContacts();
        //} catch (FieldGroup.CommitException e) {
            // Validation exceptions
        //}
    }

    public void cancel(Button.ClickEvent event) {
        Notification.show("Cancelled", Type.TRAY_NOTIFICATION);
        plateId.clear();
        sampleId.clear();
        oldRowId.clear();
        oldColumnId.clear();
        newRowId.clear();
        newColumnId.clear();
        getUI().refreshContacts();
    }

    void edit(Plate plate) {
        this.plate = plate;
        if (plate != null) {
            /*formFieldBindings = BeanFieldGroup.bindFieldsBuffered(plate,
                    this);*/
            plateId.focus();
        }
        setVisible(plate != null);
    }

    @Override
    public BcLabTestUI getUI() {
        return (BcLabTestUI) super.getUI();
    }

}
