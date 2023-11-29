package com.sacms.sacmsood.Controllers;

import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.ArrayList;

public class ListController {
    public VBox listVbox;
    public AnchorPane mainPane;
    public ArrayList<AnchorPane> records=new ArrayList<AnchorPane>();

    public Text createField(int layoutX,int layoutY,int wrappingWidth,String text,String styleClass){
        Text field=new Text();
        field.setLayoutX(layoutX);
        field.setLayoutY(layoutY);
        field.setWrappingWidth(wrappingWidth);
        field.setText(text);
        field.getStyleClass().add(styleClass);
        return field;
    }

    public AnchorPane createRecord(String id,String[] texts,int[] layoutXs,int[] wWidth){
        AnchorPane record=new AnchorPane();
        record.setAccessibleText(id);
        record.setPrefWidth(728);
        record.setPrefHeight(40);
        record.getStyleClass().add("event");
        record.managedProperty().bind(record.visibleProperty());
        for (int i = 0; i < texts.length; i++) {
            record.getChildren().add(createField(layoutXs[i],25,wWidth[i], texts[i],""));
        }
        records.add(record);
        return record;
    }

    public Button createButton(int layoutX,int layoutY,String text,String styleClass,String id){
        Button button = new Button();
        button.setLayoutX(layoutX);
        button.setLayoutY(layoutY);
        button.setText(text);
        button.getStyleClass().add(styleClass);
        button.setAccessibleText(id);
        return button;
    }

    public AnchorPane peekRecord(String id){
        for (int i = 0; i < records.size(); i++) {
            if(records.get(i).getAccessibleText().equals(id)){
                return records.get(i);
            }
        }
        return null;
    }


}
