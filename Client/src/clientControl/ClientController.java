package clientControl;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Paint;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import model.ServerHandle;
import java.io.*;
import java.net.ConnectException;
import java.net.Socket;
import java.util.ArrayList;


public class ClientController {
    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private Object toSent = null;
    private String dataTosave;
    @FXML
    protected Button connectBtn;
    @FXML
    protected Button viewBtn;
    @FXML
    protected Button uploadBtn;
    @FXML
    protected Button browseBtn;
    @FXML
    protected Button deleteBtn;
    @FXML
    protected Button downloadBtn;
    @FXML
    protected Button chatBtn;
    @FXML
    protected ListView<String> filenameView = new ListView<>();
    @FXML
    protected TextField serverIp;
    @FXML
    protected TextField serverPort;
    @FXML
    protected TextArea textViewer;
    @FXML
    protected Label connect;
    @FXML
    protected TextField path;
    @FXML
    protected TextField chatField;
    @FXML
    protected TextField chatName;
    @FXML
    protected TextArea chatArea;

    private void connect(String ip,int port) {
        try {
            socket = new Socket(ip, port);
            System.out.println("Connect to server");
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
            ArrayList<Object> sentObj = new ArrayList<>();
            String status = "100";
            sentObj.add(status);
            Object sent = sentObj;
            this.sentObjectToServer(sent,"Connect");
            Thread c = new Thread(new ServerHandle(in,this));
            c.start();
        } catch (ConnectException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR!!!");
            alert.setHeaderText("No server online");
            alert.setContentText("Program will be terminate");
            alert.showAndWait();
            System.exit(1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sentObjectToServer(Object obj,String statusPhrase){
        try {
            out.writeObject(obj);
            out.flush();
            System.out.println(statusPhrase);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String readFile(String fileName) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(fileName));
        try {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append("\n");
                line = br.readLine();
            }
            return sb.toString();
        } finally {
            br.close();
        }
    }
    @FXML
    public void chatBtnAction (ActionEvent event){

        if (out != null){
            String text = this.chatField.getText();
            text = this.chatName.getText()+" : "+text;
            ArrayList<Object> objtosent = new ArrayList<>();
            objtosent.add("800");
            objtosent.add(text);
            Object sent = objtosent;
            this.sentObjectToServer(sent,"chat");
            this.chatField.clear();
            System.out.println("Chat");
        }else{
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Connection");
            alert.setHeaderText("No server connection");
            alert.setContentText("Please connect to server");
            alert.showAndWait();
        }
    }
    public void enterKeyAction (KeyEvent event){
        if (event.getCode() == KeyCode.ENTER)  {
            if (out != null){
                String text = this.chatField.getText();
                text = this.chatName.getText()+" : "+text;
                ArrayList<Object> objtosent = new ArrayList<>();
                objtosent.add("800");
                objtosent.add(text);
                Object sent = objtosent;
                this.sentObjectToServer(sent,"chat");
                this.chatField.clear();
                System.out.println("Chat");
            }
        }
    }






    @FXML
    public void connectBtnAction (ActionEvent event){
        this.connect(this.serverIp.getText(),Integer.parseInt(this.serverPort.getText()));
        this.serverIp.setDisable(true);
        this.serverPort.setDisable(true);
        this.connectBtn.setDisable(true);
    }
    @FXML
    public void updateFilenameView(ArrayList<String> filename){
        ObservableList<String> items =FXCollections.observableArrayList ();
        for (int i = 0 ; i < filename.size(); i++){
            String f = filename.get(i);
            items.add(f);
        }
        this.filenameView.setItems(items);
    }
    @FXML
    public void browseBtnAction (ActionEvent event){
        File file = null;
        Node source = (Node) event.getSource();
        Window theStage = source.getScene().getWindow();
        FileChooser.ExtensionFilter imageFilter = new FileChooser.ExtensionFilter("Txt Files", "*.txt");
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(imageFilter);
        file = fileChooser.showOpenDialog(theStage);
        if (file != null){
            String filename = file.getName();
            String p = file.getPath();
            String data = "";
            try {
                data = this.readFile(p);
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.path.setText(p);
            ArrayList<Object> objtosent = new ArrayList<>();
            objtosent.add("300");
            objtosent.add(filename);
            objtosent.add(data);
            toSent = objtosent;
        }

    }
    @FXML
    public void downloadBtnAction(ActionEvent event){

        String name = this.filenameView.getSelectionModel().getSelectedItem();
        if (name != null){
            ArrayList<Object> objtosent = new ArrayList<>();
            objtosent.add("700");
            objtosent.add(name);
            this.sentObjectToServer(objtosent,"Request download file");
            Node source = (Node) event.getSource();
            Window theStage = source.getScene().getWindow();
            FileChooser fileChooser = new FileChooser();
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
            fileChooser.getExtensionFilters().add(extFilter);
            fileChooser.setTitle("Save file");
            File file = fileChooser.showSaveDialog(theStage);
            if (file != null){
                this.saveFile(dataTosave,file);
            }
        }else{
            System.out.println("Please select data");
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning !!!");
            alert.setHeaderText(null);
            alert.setContentText("Please select data");
            alert.showAndWait();
        }

    }
    @FXML
    public void uploadBtnAction(){
        if (!(this.path.getText().equals(""))) {
            if (out != null){
                this.sentObjectToServer(toSent, "send file to server");
            }else{
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("No Connection");
                alert.setHeaderText("No server connection");
                alert.setContentText("Please connect to server");
                alert.showAndWait();
            }
        }else{
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("No File");
            alert.setHeaderText("No file to upload");
            alert.setContentText("Please select file");
            alert.showAndWait();
        }
    }
    @FXML
    public void viewBtnAction(){

        String name = this.filenameView.getSelectionModel().getSelectedItem();
        if (name != null){
            ArrayList<Object> objtosent = new ArrayList<>();
            objtosent.add("400");
            objtosent.add(name);
            this.sentObjectToServer(objtosent,"Request view file");
        }else{
            System.out.println("Please select data");
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning !!!");
            alert.setHeaderText(null);
            alert.setContentText("Please select data");
            alert.showAndWait();
        }
    }
    @FXML
    public void deleteBtnAction(){
        String name = this.filenameView.getSelectionModel().getSelectedItem();
        if (name != null){
            ArrayList<Object> objtosent = new ArrayList<>();
            objtosent.add("500");
            objtosent.add(name);
            this.sentObjectToServer(objtosent,"Request delete file");
        }else{
            System.out.println("Please select data");
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning !!!");
            alert.setHeaderText(null);
            alert.setContentText("Please select data");
            alert.showAndWait();
        }
    }
    public void updateTextViewer(String data){
        this.textViewer.clear();
        this.textViewer.setWrapText(true);
        this.textViewer.appendText(data);
    }
    public void setTextConnect(){
        this.connect.setText("Connected");
        this.connect.setTextFill(Paint.valueOf("#1aff00"));
    }
    public void dataDuplicate(){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Data duplicate");
        alert.setHeaderText("File already in server");
        alert.setContentText("Please select other file");
        alert.showAndWait();
    }
    private void saveFile(String content, File file){
        try {
            FileWriter fileWriter = null;
            fileWriter = new FileWriter(file);
            fileWriter.write(content);
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public void setSaveData(String s){
        this.dataTosave = s;
    }
    public void updateChat(String s){
        this.chatArea.appendText(s+"\n");
    }
    public void connectionLost(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Connection lost");
        alert.setHeaderText("Connection lost");
        alert.setContentText("Program will be terminate");
        alert.showAndWait();
        System.exit(1);
    }




}
