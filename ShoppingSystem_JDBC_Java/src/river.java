import java.rmi.UnexpectedException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;
import java.text.DecimalFormat;





public class river {      //程序流
    public static void main(String[] args){


    welcome.hello();

    firstChoose.loginOrBrowse();

    buying.createOrders();

    Jdbc.showOrders();

    }


}


















class welcome{                     //问候
    static void hello(){
        System.out.println("欢迎来购物，您是要先登录？ 浏览商品？  还是说....");
        justALine.line();
    }

}












class firstChoose{                          //第一次选择
    static void login(){             //登录
//        System.out.print("登录");

        logIN.loginAll();
        browse();

    }
    static void browse(){                //浏览类别，然后浏览商品
        browseGoods.showGoodstype();
        browseGoods.chooseAndShowGoodslist();
    }

    static void loginOrBrowse(){
        System.out.println("1 —— 登录");
        System.out.println("2 —— 浏览商品");
        justALine.line();
        int value = lever.getOneOrTwo();
        if(value == 1){
            login();
        } else if(value == 2){
            browse();
        }
    }
}







class browseGoods{

    static int typeID;

    static void showGoodstype(){           //展示数据库中的商品类别
        justALine.line();
        System.out.println("请输入你想要浏览的商品类别，输入ID后回车\n");




        Jdbc.showGoodsType();



        justALine.line();

    }
    static void chooseAndShowGoodslist(){            //根据输入的类别号，展示不同类别的商品
        typeID = getID.getRightID("goodstype");

        Jdbc.showGoodsList(typeID);







    }




}




class buying{

    static double getprice(){
        double price = 0;

        //JDBC 根据商品编号获得该商品的价格


        return price;

    }


    static int getQuantity(){





        int Quantity = 0;
        System.out.print("输入购买数量：");
        Scanner sc = new Scanner(System.in);
        if(sc.hasNextInt()){
            Quantity = sc.nextInt();
        } else {
            System.out.println("输入的数据有误，程序已退出，请输入您要购买的商品数量");
            System.exit(1);
        }


        //获取购买数量


        return Quantity;
    }

    static void lastConfirm(){
        int a =0;
        System.out.println("输入1确认订单，输入其他字符重新下订单，按下回车确认，确认后不可更改");
        Scanner sc = new Scanner(System.in);
        if (sc.hasNextInt()){
            a = sc.nextInt();
            if(a != 1){
                firstChoose.browse();

                buying.createOrders();

                Jdbc.showOrders();
            }
        } else {
            firstChoose.browse();

            buying.createOrders();

            Jdbc.showOrders();
        }


        //创建订单前的最后确定    1  继续   其他按键  重新选择
    }




    static void createOrders(){

        int goodID = getID.getGoodsID();    //从键盘中获取输入的商品号
        int quantity = getQuantity();       //从键盘中获取输入的购买数量
        double price = getprice();     //从数据库中获取所选商品的单价
        lastConfirm();          //最后一次确认购买
        checkLoginStatus();     //验证是否登录
        Jdbc.writeOrders(goodID,quantity,logIN.Username);     //写入数据库

        //JDBC，生成一个订单


    }


    static void checkLoginStatus(){
        while (true){
            if(logIN.check() == 1){

                break;
            } else {
                logIN.loginAll();
            }



        }

    }
}




class getID{

    static int getRightID(String tableName){            //获取输入的类别ID
        int getID = 0;
        int ID = 0;
        Scanner sc = new Scanner(System.in);
        if(sc.hasNextInt()){

            getID = sc.nextInt();


            if(       getID <= Jdbc.getAnyTableCount(tableName)         ){         //JDBC，判断输入的值不大于商品列的count
                ID = getID;

            } else {
                System.out.println("输入的数不在编号中，程序已退出，请输入商品类别前方的类别ID");
                System.exit(1);
            }
        } else {
            System.out.println("输入的格式有误，程序已退出，请输入商品类别前方的类别ID");
            System.exit(1);
        }

        return ID;
    }

    static int goodsID = 0;

    static int getGoodsID(){        //获取输入的商品ID

        int getID = 0;

        Scanner sc = new Scanner(System.in);
        if(sc.hasNextInt()){

            getID = sc.nextInt();


            if(      Jdbc.checkGoodsID(getID, browseGoods.typeID)        ){         //JDBC，判断输入的值是根据类别删选后的good表的ID
                goodsID = getID;

            } else {
                System.out.println("输入的数不在编号中，程序已退出，请输入商品ID前方的商品ID");
                System.exit(1);
            }
        } else {
            System.out.println("输入的格式有误，程序已退出，请输入商品前方的商品ID");
            System.exit(1);
        }






        return goodsID;
    }

}




class Jdbc {

    public static final String URL = "jdbc:mysql://localhost:3306/test3";

    static void showGoodsType(){
        String Name = "cfdxkk";
        String Pwd = "88zxcvbnm88";
        try {
//            System.out.println(URL);
//            System.out.println(Name);
//            System.out.println(Pwd);
            Class.forName("com.mysql.jdbc.Driver");

            Connection conn = DriverManager.getConnection(URL, Name, Pwd);
            String SQL = "SELECT * FROM goodstype";
            PreparedStatement ptmt = conn.prepareStatement(SQL);
            ResultSet rs = ptmt.executeQuery();

            while (rs.next()){
                String value1 = rs.getString(1);
                String value2 = rs.getString(2);
                System.out.println(value1+"    "+value2);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }



    static int getAnyTableCount( String input){
        String Name = "cfdxkk";
        String Pwd = "88zxcvbnm88";
        int count = 0;
        try {
            Class.forName("com.mysql.jdbc.Driver");

            Connection conn = DriverManager.getConnection(URL, Name, Pwd);
            String SQL = "SELECT COUNT(*) FROM ";
            String SQLfin = SQL+input;
            PreparedStatement ptmt = conn.prepareStatement(SQLfin);

            ResultSet rs = ptmt.executeQuery();

            while (rs.next()) {
                String value1 = rs.getString(1);
                count =  Integer.parseInt(value1);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        return count;

    }



    static void showGoodsList(int typeID){
        String Name = "cfdxkk";
        String Pwd = "88zxcvbnm88";

        try {
            Class.forName("com.mysql.jdbc.Driver");

            Connection conn = DriverManager.getConnection(URL, Name, Pwd);
            String SQL = "SELECT * FROM goods WHERE typeid = ?";
            PreparedStatement ptmt = conn.prepareStatement(SQL);
            ptmt.setInt(1,typeID);
            ResultSet rs = ptmt.executeQuery();

            justALine.line();
            System.out.println("查询到的商品如下：");
            while (rs.next()) {


                System.out.print(rs.getString(1)+"      ");
                System.out.print(textleftAlign.leftAlign(rs.getString(2),25,' '));
                System.out.print(rs.getString(3)+"      ");
                System.out.print(rs.getString(4)+"      ");
                System.out.print(rs.getString(5)+"      ");
                System.out.println(rs.getString(6)+"      ");


            }
            System.out.print("在此输入你想要的购买的商品的商品ID：");


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    static boolean SearchUserAndPwd(String username,String password){
        String Name = "cfdxkk";
        String Pwd = "88zxcvbnm88";

        int a = 0;

        try {
            Class.forName("com.mysql.jdbc.Driver");

            Connection conn = DriverManager.getConnection(URL, Name, Pwd);
            String SQL = "SELECT COUNT(*) FROM user WHERE username = ? AND password = ?";
            PreparedStatement ptmt = conn.prepareStatement(SQL);
            ptmt.setString(1,username);
            ptmt.setString(2,password);
            ResultSet rs = ptmt.executeQuery();

            while (rs.next()) {
                a = rs.getInt(1);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        if(a == 1){

            return true;
        } else {

            return false;
        }

    }



    static boolean checkGoodsID(int goodsID,int typeID){
        String Name = "cfdxkk";
        String Pwd = "88zxcvbnm88";

        int a = 0;

        try {
            Class.forName("com.mysql.jdbc.Driver");

            Connection conn = DriverManager.getConnection(URL, Name, Pwd);
            String SQL = "SELECT COUNT(*) FROM goods WHERE goodsid = ? AND typeid = ?";
            PreparedStatement ptmt = conn.prepareStatement(SQL);
            ptmt.setInt(1,goodsID);
            ptmt.setInt(2,typeID);

            ResultSet rs = ptmt.executeQuery();

            while (rs.next()) {
                a = rs.getInt(1);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        if(a == 1){
            return true;
        } else {
            return false;
        }
    }



    static double getGoodsPrice(){
        String Name = "cfdxkk";
        String Pwd = "88zxcvbnm88";

        double a = 0;
        try {
            Class.forName("com.mysql.jdbc.Driver");

            Connection conn = DriverManager.getConnection(URL, Name, Pwd);
            String SQL = "SELECT price FROM goods WHERE goodsid = ?";
            PreparedStatement ptmt = conn.prepareStatement(SQL);
            ptmt.setInt(1,getID.goodsID);


            ResultSet rs = ptmt.executeQuery();

            while (rs.next()) {
                a = rs.getInt(1);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        return a;

    }


    static int line1 = wu_qing_de_ji_shu_qi.primaryKeyNeverRepeat();    //line1是orders表的主键列最高值+1

    static void writeOrders(int goodsID,int quantity,String username){



        String Name = "cfdxkk";
        String Pwd = "88zxcvbnm88";




        double sum;
        double price = getGoodsPrice();
        sum = quantity * price;

        //0.00表示小数点后面保留两位小数
        DecimalFormat df = new DecimalFormat(".00");
        //保留两位小数




        String sum_str = df.format(sum);
        double sum_2;
        sum_2 = Double.valueOf(sum_str);





        try {
            Class.forName("com.mysql.jdbc.Driver");

            Connection conn = DriverManager.getConnection(URL, Name, Pwd);
            //sql, 每行加空格
            String sql = "INSERT INTO orders(orderid, goodsid, count, sum, username) values(?,?,?,?,?)";
            //预编译
            PreparedStatement ptmt = conn.prepareStatement(sql); //预编译SQL，减少sql执行

            //传参


            ptmt.setInt(1, line1);
            ptmt.setInt(2, goodsID);
            ptmt.setInt(3, quantity);
            ptmt.setDouble(4, sum_2);
            ptmt.setString(5, username);



            //执行
            ptmt.execute();

        } catch (Exception e) {
            e.printStackTrace();
        }




        //将订单写入数据库
    }


    static void showOrders(){
        justALine.line();

        String Name = "cfdxkk";
        String Pwd = "88zxcvbnm88";

        try {
            Class.forName("com.mysql.jdbc.Driver");

            Connection conn = DriverManager.getConnection(URL, Name, Pwd);
            String SQL = "SELECT * FROM orders WHERE orderid = ?";
            PreparedStatement ptmt = conn.prepareStatement(SQL);
            ptmt.setInt(1,line1);
            ResultSet rs = ptmt.executeQuery();

            System.out.println("订单已确认！");
            while (rs.next()) {

                System.out.print(rs.getInt(1) + "\t");
                System.out.print(rs.getInt(2) + "\t");
                System.out.print(rs.getInt(3) + "\t");
                System.out.print(rs.getDouble(4) + "\t");
                System.out.println(rs.getString(5) + "\t");


            }


            justALine.line();
            System.exit(1);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }



}




class logIN{
    static String Username;
    static String Pwd;

    static int maxlogin = 3;

    static void getKey(){



        System.out.print("在此输入用户名：");
        Scanner scUname = new Scanner(System.in);
        Username = scUname.next();

        System.out.print("在此输入密码：");
        Scanner scPwd = new Scanner(System.in);
        Pwd = scPwd.next();


    }
    static int testfirstlogin = 0;

    static int check(){           //用户验证登录名和密码，成功返回1，不成功返回0

        if( Jdbc.SearchUserAndPwd(Username,Pwd) ){
            System.out.println("登录成功");
            return 1;

        } else {
            if(lever.value == 1 || testfirstlogin == 1 ){
                System.out.println("登录失败,请重新登录");

                System.out.println("您还有"+ --maxlogin +"次机会");
                if(maxlogin==0){
                    System.out.println("次数用尽，登录失败，程序已退出");
                    System.exit(1);
                } else {
                    loginAll();
                }
            } else if(lever.value == 2){
                System.out.println("您尚未登录，请先登录");
                testfirstlogin = 1;
                return 0;
            }


            return 0;

        }

    }
    static void loginAll(){
        getKey();
        check();
    }
}




class lever{               //判断输入的值是否是1或2
    static int value;
    static int getOneOrTwo(){
        int lever = 0;

        Scanner sc = new Scanner(System.in);
        if(sc.hasNextInt()){
            lever = sc.nextInt();

            if(lever == 1 || lever == 2){
                value = lever;

            } else {
                System.out.println("输入的格式有误，程序已退出，请输入不同操作所对应的编号");
                System.exit(1);
            }
        } else {
            System.out.println("输入的格式有误，程序已退出，请输入不同操作所对应的编号");
            System.exit(1);
        }
//        System.out.print(value);
        return value;
    }
}








class justALine{
    static void line(){
        System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");    //  just a line, really a line
    }
}


class textleftAlign{          //谁不喜欢整齐的输出格式呢
    public static String leftAlign(String str, int len,char c) {
        char array[] = str.toCharArray();


        for (int i = 0; i < array.length; i++) {//半角转全角
                         if (array[i] == ' ') {
                                 array[i] = '\u3000';
                            } else if (array[i] < '\177') {
                                 array[i] = (char) (array[i] + 65248);
                             }
        }



               int sub = len - str.length();
                 if (sub <= 0) {
                         return new String(array);//大于等于len返回
                     }
                 char[] temp = new char[len];
                 System.arraycopy(array, 0, temp, 0, str.length());
                 for (int j = str.length();j < len; j++) {//左对齐右填充
                        if (c == ' ') {
                                temp[j] = '\u3000';
                            } else if (c < '\177') {
                            temp[j] = (char) (c + 65248);
                            }
                    }
               return new String(temp);
             }
}



class wu_qing_de_ji_shu_qi{

    public static final String URL = "jdbc:mysql://localhost:3306/test3";

    static int primaryKeyNeverRepeat(){
        String Name = "cfdxkk";
        String Pwd = "88zxcvbnm88";

        int a = 0;
        try {
            Class.forName("com.mysql.jdbc.Driver");

            Connection conn = DriverManager.getConnection(URL, Name, Pwd);
            String SQL = "SELECT COUNT(*) FROM orders ";
            PreparedStatement ptmt = conn.prepareStatement(SQL);


            ResultSet rs = ptmt.executeQuery();

            while (rs.next()) {
                int count = rs.getInt(1);
                a = count + 1;
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        return a;

    }
}