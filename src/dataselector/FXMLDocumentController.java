/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataselector;

import dataselector.mouseHooverAnimationPieChart.MouseExitAnimation;
import dataselector.mouseHooverAnimationPieChart.MouseHoverAnimation;
import java.net.URL;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;
import org.controlsfx.dialog.Dialogs;
import org.fxmisc.easybind.EasyBind;

/**
 *
 * @author Eskil
 */
public class FXMLDocumentController implements Initializable {

    SQL_manager sql_manager = new SQL_manager();

    ObservableList<List<String>> dataen = FXCollections.observableArrayList();
    static SortedList<Record> sortedData;
    List<TableColumn> lstClm = new ArrayList<TableColumn>();

    static XYChart.Series series1 = new XYChart.Series();
    static ObservableList<String> rows = FXCollections.observableArrayList();

    int userChosenDataNameColumnIndex = 0;
    int userChosenDataValueColumnIndex = 0;

    @FXML
    protected Label caption;
    @FXML
    protected TextField filterField;
    @FXML
    TableView tableView;
    @FXML
    LineChart lineChart;
    @FXML
    TextField t;
    @FXML
    TableView tableView2;
    @FXML
    ChoiceBox choiceBoxTable;
    @FXML
    ChoiceBox choiceBoxName;
    @FXML
    ChoiceBox choiceBoxValue;
    @FXML
    protected BarChart barChart;
    @FXML
    protected TableColumn Month;
    @FXML
    protected TableColumn Value;
    @FXML
    protected PieChart pieChart;

    @FXML
    protected TextField txtIPadress;
    @FXML
    protected TextField txtPortNumber;
    @FXML
    protected TextField txtSQLinstance;
    @FXML
    protected Group visualizationGroup;
    @FXML
    protected Group dataSelectGroup;
    @FXML
    protected Group sqlConnectGroup;

    @FXML
    protected void handleButtonAction(ActionEvent event) throws ClassNotFoundException, SQLException {
        Connection conn = sql_manager.getConnection("localhost", 8889, "mysql");
        getAllTables();

    }

    @FXML
    protected void btnSQlConnect(ActionEvent event) throws ClassNotFoundException, SQLException {
        visualizationGroup.visibleProperty().set(false);
        dataSelectGroup.visibleProperty().set(false);
        sqlConnectGroup.visibleProperty().set(true);

    }

    @FXML
    protected void btnDataSelect(ActionEvent event) throws ClassNotFoundException, SQLException {
        visualizationGroup.visibleProperty().set(false);
        dataSelectGroup.visibleProperty().set(true);
        sqlConnectGroup.visibleProperty().set(false);

    }

    @FXML
    protected void handleButtonAction5(ActionEvent event) throws SQLException, ClassNotFoundException {
        Connection conn = sql_manager.getConnection(txtIPadress.getText(), Integer.parseInt(txtPortNumber.getText()), txtSQLinstance.getText());
        getAllTables();

    }

    @FXML
    protected void handleButtonAction1(ActionEvent event) throws SQLException, ClassNotFoundException {
        //filterData();
    }

    @FXML
    protected void handleButtonPieChart(ActionEvent event) throws SQLException, ClassNotFoundException {
        visualizationGroup.visibleProperty().set(true);
        dataSelectGroup.visibleProperty().set(false);
        sqlConnectGroup.visibleProperty().set(false);

        barChart.visibleProperty().set(false);
        pieChart.visibleProperty().set(true);
        lineChart.visibleProperty().set(false);

        if (checkWhichIndexOnUserSelectedColumns() == true) {
            getPieChartData(userChosenDataValueColumnIndex, userChosenDataNameColumnIndex);
        }

    }

    @FXML
    protected void handeButtonBarChart(ActionEvent event) throws SQLException, ClassNotFoundException {
        visualizationGroup.visibleProperty().set(true);
        dataSelectGroup.visibleProperty().set(false);
        sqlConnectGroup.visibleProperty().set(false);

        barChart.visibleProperty().set(true);
        pieChart.visibleProperty().set(false);
        lineChart.visibleProperty().set(false);
        if (checkWhichIndexOnUserSelectedColumns() == true) {
            getBarChartData(userChosenDataValueColumnIndex, userChosenDataNameColumnIndex);
        }

    }

    @FXML
    protected void handleButtonLineChart(ActionEvent event) throws SQLException, ClassNotFoundException {
        visualizationGroup.visibleProperty().set(true);
        dataSelectGroup.visibleProperty().set(false);
        sqlConnectGroup.visibleProperty().set(false);

        barChart.visibleProperty().set(false);
        pieChart.visibleProperty().set(false);
        lineChart.visibleProperty().set(true);
        if (checkWhichIndexOnUserSelectedColumns() == true) {
            getLineChartData(userChosenDataValueColumnIndex, userChosenDataNameColumnIndex);
        }

    }

    private boolean checkWhichIndexOnUserSelectedColumns() throws NumberFormatException {

        Iterator itr = lstClm.iterator();
        int counter = 0;
        Boolean thereIsAValue = false;
        Boolean thereIsAName = false;
        Boolean thereIsOnlyTwo = true;
        while (itr.hasNext()) {
            if (counter > 1) {
                thereIsOnlyTwo = false;
                System.out.println("FEIL");
            } else {
                if (lstClm.get(counter).getUserData() == "Value") {
                    thereIsAValue = true;
                    userChosenDataValueColumnIndex = Integer.parseInt(lstClm.get(counter).getId());

                } else if ((lstClm.get(counter).getUserData() == "Name")) {
                    thereIsAName = true;
                    userChosenDataNameColumnIndex = Integer.parseInt(lstClm.get(counter).getId());
                }
            }
            counter++;
            itr.next();
        }
        if (thereIsAName == true && thereIsAValue== true && thereIsOnlyTwo == true) {
            return true;
        }
        Dialogs.create()
                .title("Feil i datavalg")
                .message("Du har valgt en feil i tabellen. Det må kun velges ett felt med navn og ett med verdier")
                .showInformation();
                visualizationGroup.visibleProperty().set(false);
        dataSelectGroup.visibleProperty().set(true);
        sqlConnectGroup.visibleProperty().set(false);
        
        return false;
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

        addListenerToChoiceBox(choiceBoxTable);

    }

    private void addListenerToChoiceBox(ChoiceBox choicebox) {
        //adding a listener to the coicebox to pull out the choice the user selected
        choicebox.valueProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                String newSelection = (String) newValue;
                try {
                    getDataForSelectedTable(newSelection);
                } catch (SQLException ex) {

                }
                System.out.println("choiceBoxSelected by Listener: " + newSelection);
            }
        });
    }

    private void getAllTables() throws SQLException {
        //Henter ut alle tabellene i databasen ved hjelp av metadata
        DatabaseMetaData md = SQL_manager.conn.getMetaData();
        ResultSet rs = md.getTables(null, null, "%", null);

        while (rs.next()) {
            //deretter legger jeg til alle tabellene i en choicebox så brukeren kan velge hvilken tabell
            choiceBoxTable.getItems().add(rs.getString(3));
        }

    }

    private void getDataForSelectedTable(String tableName) throws SQLException {
        // henter ut all data for en spesifikk tabell.
        tableView.getColumns().removeAll(tableView.getColumns());

        String SQL = "SELECT * FROM " + tableName;
        ResultSet rs = sql_manager.getDataFromSQL("localhost", 8889, "mysql", SQL);

        //først henter vi ut alle kolonnene og legger til de i tableview
        for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
            //We are using non property style for making dynamic table
            System.out.print("her kommern " + i);
            final int j = i;
            TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i + 1));
            System.out.println(col);
            col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
                public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param) {
                    return new SimpleStringProperty(param.getValue().get(j).toString());
                }
            });

            col.setId((String.valueOf(i)));
            col.prefWidthProperty().bind(tableView.widthProperty().divide(4)); //for å automatisere bredden på kolonnene 
            //col.setGraphic(new CheckBox());
            tableView.getColumns().addAll(col);
            addCheckBoxesToColumns(col);

        }

        //deretter legges all dataen til i kolonnene ved hjelp av rader
        while (rs.next()) {
            ObservableList<String> row = FXCollections.observableArrayList();
            for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                if (rs.getString(i) != null) {
                    row.add(rs.getString(i));
                    rows.add(rs.getString(i));

                    System.out.println(rs.getString(i));
                } else {
                    row.add(" ");
                }

            }
            dataen.add(row);
        }

        tableView.setItems(dataen);

    }

    private void addCheckBoxesToColumns(TableColumn col) {
        EventHandler<ActionEvent> handler = new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                ComboBox cb = (ComboBox) event.getSource();
                TableColumn column = (TableColumn) cb.getUserData();

                if (cb.getValue() == " ") {
                    lstClm.remove(column);
                } else {
                       lstClm.remove(column);
                    column.setUserData(cb.getValue().toString());
                    System.out.println("kanskje her? " + column.getUserData());
                    System.out.println(column + " " + column.getId());

                    lstClm.add(column);
                }

            }
        };
        ComboBox cb = new ComboBox();
        cb.getItems().add("Name");
        cb.getItems().add("Value");
        cb.getItems().add(" ");
        cb.setUserData(col);
        cb.setOnAction(handler);
        col.setGraphic(cb);

    }

    protected void getPieChartData(Integer userChosenDataValueColumnIndex, Integer userChosenDataNameColumnIndex) {

        System.out.println("Kolonne id for data er " + userChosenDataValueColumnIndex + "Kolonne id for name er " + userChosenDataNameColumnIndex);
        ObservableList<PieChart.Data> pieChartData
                = FXCollections.observableArrayList(EasyBind.map(dataen, rowData -> {
                    String name = (String) rowData.get(userChosenDataNameColumnIndex);
                    int value = Integer.parseInt(rowData.get(userChosenDataValueColumnIndex));
                    return new PieChart.Data(name, value);
                }));
        System.out.println("aa " + pieChartData.get(0));
        pieChart.setData(pieChartData);

        for (PieChart.Data d : pieChartData) {
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

    protected void getBarChartData(Integer userChosenDataValueColumnIndex, Integer userChosenDataNameColumnIndex) {

        ObservableList<XYChart.Data<String, Number>> barChartData = EasyBind.map(dataen, rowData -> {
            String name = rowData.get(userChosenDataNameColumnIndex);
            Double value = new Double(rowData.get(userChosenDataValueColumnIndex));
            return new XYChart.Data(name, value);
        });

        XYChart.Series series1 = new XYChart.Series();
        series1.getData().addAll(barChartData);
        barChart.getData().addAll(series1);

        //series1.getData().clear();
        // series1.getData().add(new XYChart.Data(itr.next(), dataen.get(1)));
        barChart.setData(FXCollections.observableArrayList(series1));
    }

    protected void getLineChartData(Integer userChosenDataValueColumnIndex, Integer userChosenDataNameColumnIndex) {

        ObservableList<XYChart.Data<String, Number>> barChartData = EasyBind.map(dataen, rowData -> {
            String name = rowData.get(userChosenDataNameColumnIndex);
            Double value = new Double(rowData.get(userChosenDataValueColumnIndex));
            return new XYChart.Data(name, value);
        });

        XYChart.Series series1 = new XYChart.Series();
        series1.getData().addAll(barChartData);
        lineChart.getData().addAll(series1);

        //series1.getData().clear();
        // series1.getData().add(new XYChart.Data(itr.next(), dataen.get(1)));
        lineChart.setData(FXCollections.observableArrayList(series1));
    }

    /* private void filterData() {
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
     */
}
