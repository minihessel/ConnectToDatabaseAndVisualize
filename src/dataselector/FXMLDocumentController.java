/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataselector;

import dataselector.mouseHooverAnimationPieChart.MouseExitAnimation;
import dataselector.mouseHooverAnimationPieChart.MouseHoverAnimation;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

/**
 *
 * @author Eskil
 */
public class FXMLDocumentController implements Initializable {

    SQL_manager sql_manager = new SQL_manager();

    static ObservableList<Record> dataen = FXCollections.observableArrayList();
    static SortedList<Record> sortedData;
    static ObservableList<PieChart.Data> dataList = FXCollections.observableArrayList();
    static XYChart.Series series1 = new XYChart.Series();
    @FXML
    protected Label caption;
    @FXML
    protected TextField filterField;
    @FXML
    TableView tableView;
    @FXML
    protected BarChart barChart;
    @FXML
    protected TableColumn Month;
    @FXML
    protected TableColumn Value;
    @FXML
    protected PieChart pieChart;
    @FXML
    protected AnchorPane sqlConnectionPane;
    @FXML
    protected TextField txtIPadress;
    @FXML
    protected TextField txtPortNumber;
    @FXML
    protected TextField txtSQLinstance;

    @FXML
    protected void handleButtonAction(ActionEvent event) throws ClassNotFoundException, SQLException {
        sql_manager.connectToSql("localhost", 8889, "mysql");
        filterData();

    }

    @FXML
    protected void handleButtonAction4(ActionEvent event) throws ClassNotFoundException {
        sqlConnectionPane.setVisible(true);
    }

    @FXML
    protected void handleButtonAction5(ActionEvent event) throws SQLException, ClassNotFoundException {
        sql_manager.connectToSql(txtIPadress.getText(), Integer.parseInt(txtPortNumber.getText()), txtSQLinstance.getText());
        sqlConnectionPane.setVisible(false);
    }

    @FXML
    protected void handleButtonAction1(ActionEvent event) throws SQLException, ClassNotFoundException {
        filterData();
    }

    @FXML
    protected void handleButtonAction2(ActionEvent event) throws SQLException, ClassNotFoundException {
        barChart.visibleProperty().set(false);
        pieChart.visibleProperty().set(true);
        getPieChartData();
    }

    @FXML
    protected void handleButtonAction3(ActionEvent event) throws SQLException, ClassNotFoundException {
        barChart.visibleProperty().set(true);
        pieChart.visibleProperty().set(false);
        getBarChartData();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        /* MÅ være med for å binde radene i TableView til standardverdiene */
        Month.setCellValueFactory(new PropertyValueFactory("fieldMonth"));
        Value.setCellValueFactory(new PropertyValueFactory("fieldValue"));
        tableView.setItems(dataen);
        /* Added functionality for filtering */
        filterField.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
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

    protected void getPieChartData() {
        pieChart.getData().clear();
        for (Record record : sortedData) {
            dataList.add(new PieChart.Data(record.getFieldMonth(), record.getFieldValue()));
        }
        pieChart.setData(dataList);
        for (PieChart.Data d : dataList) {
            d.getNode().setOnMouseEntered(new MouseHoverAnimation(d, pieChart));
            d.getNode().setOnMouseExited(new MouseExitAnimation());
            d.getNode().addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent e) {
                    caption.setText("Verdi for valgt pai: " + String.valueOf(d.getPieValue()));
                }
            });
        }
    }

    protected void getBarChartData() {
        series1.getData().clear();
        for (Record record : sortedData) {
            series1.getData().add(new XYChart.Data(record.getFieldMonth(), record.getFieldValue()));
        }
        barChart.setData(FXCollections.observableArrayList(series1));
    }

    private void filterData() {
        FilteredList<Record> filteredData = new FilteredList<>(dataen, (Record p) -> true);
        filteredData.setPredicate((Record record) -> {
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

}
