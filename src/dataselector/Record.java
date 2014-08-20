/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dataselector;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author eskhesse
 */
  public class Record{
      private SimpleStringProperty fieldMonth;
      private SimpleIntegerProperty fieldValue;
     
      public Record(String fMonth, Integer fValue){
          this.fieldMonth = new SimpleStringProperty(fMonth);
          this.fieldValue = new SimpleIntegerProperty(fValue);
      }

    Record() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
     
      public String getFieldMonth() {
          return fieldMonth.get();
      }
     
      public Integer getFieldValue() {
          return fieldValue.get();
      }
      
      
           public void setFieldMonth(String setFieldMonth) {
          this.fieldMonth = fieldMonth;
      }
     
      public void setFieldValue() {
          this.fieldValue = fieldValue;
      }
     
  }
