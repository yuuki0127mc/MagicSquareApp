package ex10;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class MagicSquareApp extends Application {

    public static String gakuban = ""; // 学籍番号を入力すること
    public static String yourname = ""; // 氏名を入力すること

    // 問題の定義
    String[] tasks = {
      "81635-492", "-7215-8-4", "-3-9-1-7-", "2------1-"
      //, "8341--672"
    };
    int[] ans = new int[9];

    Button[] btn;
    GridPane gp;
    ComboBox<String> cbox;
    Label lb;
    HBox hb1, hb2;
    VBox vb1, vb2;

    @Override
    public void start(Stage primaryStage) {
     reset();
     comboBoxLayout();
     buttonLayout();

     vb2 = new VBox(10, hb1, hb2);
     vb2.setPadding(new Insets(10));
     vb2.setAlignment(Pos.CENTER);

     primaryStage.setScene(new Scene(vb2));
     primaryStage.setTitle("MagicSquareApp");
     primaryStage.setResizable(false);
     primaryStage.show();
    }

    //コンボボックスの表示
    public void comboBoxLayout() {
     lb = new Label("問題選択");
     lb.setFont(new Font(13));
     lb.setAlignment(Pos.CENTER_LEFT);

     cbox = new ComboBox<>();
     cbox.getItems().addAll(tasks);
     
     //表示ボタン
     Button showBtn = new Button("表示する");
     showBtn.setTextFill(Color.GREEN);

     showBtn.setOnAction(e -> showTasks());
     
     hb1 = new HBox(10, lb, cbox,showBtn);
     hb1.setAlignment(Pos.CENTER_LEFT);
     
     
    }

    //ボタンの表示
    public void buttonLayout() {
     btn = new Button[9];
     gp = new GridPane();
     for(int i = 0; i < 9; i++) {
      btn[i] = new Button();
      btn[i].setPrefSize(50,50);
      btn[i].setFont(new Font(20));
      gp.add(btn[i], i % 3, 1 + i / 3);
      
     }
     gp.setHgap(5);
     gp.setVgap(5);
    
     

     Button checkBtn = new Button("Check");
     checkBtn.setOnAction(e -> checkBtnClicked());
     checkBtn.setPrefSize(100, 25);
     checkBtn.setFont(new Font(15));
     checkBtn.setTextFill(Color.RED);
     Button resetBtn = new Button("Reset");
     resetBtn.setOnAction(e -> showTasks());
     resetBtn.setPrefSize(100, 25);
     resetBtn.setFont(new Font(15));
     resetBtn.setTextFill(Color.BLUE);

     vb1 = new VBox(30, checkBtn, resetBtn);
     vb1.setAlignment(Pos.CENTER);

     hb2 = new HBox(15, gp, vb1);
     
     
    }

    //問題の表示
    public void showTasks() {
     try {
      reset();
      char[] task = getTask();
      for(int i = 0; i < 9; i++) {
       int index = i;
       if(task[i] != '-') {
        btn[i].setText(String.valueOf(task[i]));
        btn[i].setTextFill(Color.BLACK);
        btn[i].setOnAction(null);
       }
       else {
        btn[i].setText(null);
        btn[i].setTextFill(Color.BLUE);
        btn[i].setOnAction(e -> {
         btn[index].setText(String.valueOf(answerToggle(index)));
        });
       }
      }
     }catch(Exception e) {}
     
     
    }

    //問題の取得
    public char[] getTask() {
     char[] task = new char[9];
     int index = getTaskIndex();
     for(int i = 0; i < 9; i++) {
      task[i] = tasks[index].charAt(i);
     }
     return task;
    }

    public int getTaskIndex() {
     int i = 0;
     for(; i < tasks.length; i++) {
      if(tasks[i].equals(cbox.getValue()))
       break;
     }
     return i;
    }

    //インデックスの取得
    public int answerToggle(int index) {
     int tmp = ans[index];
     if(ans[index] < 9) {
      ans[index]++;
     }else {
      ans[index] = 1;
     }
     return tmp;
    }

    //ボタンクリック時の動作
    public void checkBtnClicked() {
     try {
      boolean flag = answerCheck();
      if(flag) {
       for(int i = 0; i < 9; i++){
        btn[i].setOnAction(null);
       }
       Alert alert = new Alert(Alert.AlertType.INFORMATION);
       alert.setTitle("Check");
       alert.setHeaderText("正解です！");
       alert.showAndWait();
      }
      else {
       Alert alert = new Alert(Alert.AlertType.ERROR);
       alert.setTitle("Check");
       alert.setHeaderText("不正解です");
       alert.showAndWait();
      }
     }catch(Exception e) {}
    }

    //答えの確認（各列、行の合計が15であるか）
    public boolean answerCheck() {
     boolean flag = true;
     int[][] check = getAnsArray();
     int tmp = 0;
     
     for(int i = 0; i < 3; i++) {
      for(int j = 0; j < 3; j++) {
       tmp += check[i][j];
      }
      if(tmp != 15) flag = false;
      tmp = 0;
     }
     
     for(int i = 0; i < 3; i++) {
      for(int j = 0; j < 3; j++) {
       tmp += check[j][i];
      }
      if(tmp != 15) flag = false;
      tmp = 0;
     }

     if((check[0][0] + check[1][1] + check[2][2]) != 15) flag = false;
     if((check[0][2] + check[1][1] + check[2][0]) != 15) flag = false;

     return flag;
    }

    //解答の値の取得
    public int[][] getAnsArray(){
     int[][] check = new int[3][3];
     String tmpstr;
     for(int i = 0; i < 3; i++) {
      for(int j = 0; j < 3; j++){
       tmpstr = btn[3 * i + j].getText();
       if(tmpstr != null)
        check[i][j] = Integer.parseInt(tmpstr);
       else
        check[i][j] = 0;
      }
     }
     return check;
    }

    public void reset() {
     for(int i = 0; i < 9; i++) {
      ans[i] = 1;
     }
    }

    public static void main(String[] args) {
        // アプリケーションを起動する
        Application.launch(args);
        System.out.println("完了--MagicSquareApp");
    }
}

/* 考察 -- 調査したこと、工夫したこと、確認したことを記述
今回のプログラムを作成するにあたり、まず魔法陣の仕組みを調査した。3×3の魔法陣はどの行の数を合計しても、どの列の数を合計しても15であることが分かった。
また工夫した点は、問題を選択した後にすぐに問題が出るのではなく、問題の表示ボタンを作成した。
また各ボタンの文字の色を変更し、見やすくした。
プログラムを実行し、動作を確認した。
その結果、選択する数字は1～9までであり、どの列、行の数字の合計も15にすることで答えを導くことができた。また表示ボタンも正しく機能していることが確認できた。
 */
