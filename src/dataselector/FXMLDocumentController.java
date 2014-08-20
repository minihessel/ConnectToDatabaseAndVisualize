/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataselector;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

/**
 *
 * @author Eskil
 */
public class FXMLDocumentController extends SQL_manager implements Initializable {

    static ObservableList<Record> dataen = FXCollections.observableArrayList();
    static SortedList<Record> sortedData;
    static ObservableList<PieChart.Data> dataList
            = FXCollections.observableArrayList();

    static XYChart.Series series1 = new XYChart.Series();
    ;

    @FXML
    private Label label;

    @FXML
    private TextField filterField;

    @FXML
    TableView tableView;

    @FXML
    private BarChart barChart;

    @FXML
    private TableColumn Month;
    @FXML
    private TableColumn Value;
    @FXML
    private PieChart pieChart;
    @FXML
    private AnchorPane sqlConnectionPane;
      @FXML
    private TextField txtIPadress;

    @FXML
    private void handleButtonAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        SQL_manager sql_manager = new SQL_manager();
             String myDriver = "org.gjt.mm.mysql.Driver";
            Class.forName(myDriver);
        sql_manager.connectToSql(txtIPadress.getText());

    }

    @FXML
    private void handleButtonAction4(ActionEvent event) throws SQLException, ClassNotFoundException {
        sqlConnectionPane.setVisible(true);
    }
      @FXML
    private void handleButtonAction5(ActionEvent event) throws SQLException, ClassNotFoundException {
        
    }

    @FXML
    private void handleButtonAction1(ActionEvent event) throws SQLException, ClassNotFoundException {
        FilteredList<Record> filteredData = new FilteredList<>(dataen, p -> true);
        filteredData.setPredicate(record -> {
            // If filter text is empty, display all persons.
            if (filterField.getText() == null || filterField.getText().isEmpty()) {
                return true;
            }

            // Compare first name and last name of every person with filter text.
            String lowerCaseFilter = filterField.getText().toLowerCase();

            if (record.getFieldMonth().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                return true; // Filter matches first name.
            }
            return false; // Does not match.
        });

        // 3. Wrap the FilteredList in a SortedList. 
        sortedData = new SortedList<Record>(filteredData);

        // 4. Bind the SortedList comparator to the TableView comparator.
        sortedData.comparatorProperty().bind(tableView.comparatorProperty());

        // 5. Add sorted (and filtered) data to the table.
        tableView.setItems(sortedData);

    }

    @FXML
    private void handleButtonAction2(ActionEvent event) throws SQLException, ClassNotFoundException {
        barChart.visibleProperty().set(false);
        pieChart.visibleProperty().set(true);
        getPieChartData();

    }

    @FXML
    private void handleButtonAction3(ActionEvent event) throws SQLException, ClassNotFoundException {
        barChart.visibleProperty().set(true);
        pieChart.visibleProperty().set(false);

        getBarChartData();

    }

    private void getPieChartData() {
        pieChart.getData().clear();

        for (Record record : sortedData) {
            dataList.add(new PieChart.Data(record.getFieldMonth(), record.getFieldValue()));
        }
        pieChart.setData(dataList);

    }

    private void getBarChartData() {
        series1.getData().clear();

        for (Record record : sortedData) {
            series1.getData().add(new XYChart.Data(record.getFieldMonth(), record.getFieldValue()));
        }
        barChart.setData(FXCollections.observableArrayList(series1));

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        /* MÅ være med for å binde radene i TableView til standardverdiene */
        Month.setCellValueFactory(new PropertyValueFactory("fieldMonth"));
        Value.setCellValueFactory(new PropertyValueFactory("fieldValue"));
        tableView.setItems(dataen);

        /* Added functionality for filtering */
        filterField.textProperty().addListener((observable, oldValue, newValue) -> {

        });

        ContextMenu menu = new ContextMenu();
        MenuItem item = new MenuItem("Filter");
        item.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

            }
        });
        menu.getItems().add(item);
        tableView.setContextMenu(menu);

    }

}
