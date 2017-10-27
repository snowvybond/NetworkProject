package clientControl;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import model.ServerHandle;

import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.util.ArrayList;


public class ClientController {
    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private Object toSent = null;
    @FXML
    protected Button connectBtn;
    @FXML
    protected Button viewBtn;
    @FXML
    protected Button uploadBtn;
    @FXML
    protected ListView<String> filenameView = new ListView<>();
    @FXML
    protected TextField serverIp;
    @FXML
    protected TextField serverPort;
    @FXML
    protected Button browseBtn;
    @FXML
    protected ImageView imgView;
    @FXML
    protected TextField path;

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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sentObjectToServer(Object obj,String statusPhrase){
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
        if (!(file.equals(null))){
            String filename = file.getName();
            String p = file.getPath();
            String data = "";
            try {
                data = this.readFile(p);
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println(data);
            this.path.setText(p);
            ArrayList<Object> objtosent = new ArrayList<>();
            objtosent.add("300");
            objtosent.add(filename);
            objtosent.add(data);
            toSent = objtosent;
        }

    }
    @FXML
    public void uploadBtnAction(){
        if (!(this.path.getText().equals(""))) {
            this.sentObjectToServer(toSent, "send file to server");
        }
    }


}
