
import java.sql.*;
import java.util.*;

public class LibraryManagementSystem{

    static final String DB_USER = "root";
    static final String DB_PASS = "";
    static final String DB_NAME="LibraryManagementSystem";
    static final String DB_URL = "jdbc:mysql://localhost:3306/"+DB_NAME;
    private static java.sql.Connection connection= null;
    public static void establishConnection(){
        try{
            connection= DriverManager.getConnection(DB_URL,DB_USER,DB_PASS);
        }
        catch(SQLException e) {
            e.printStackTrace();
        }
    }
    private static void closeConnection() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static void Menu() throws SQLException{
        Scanner scanner = new Scanner(System.in);

        System.out.println("Main Menu");
        System.out.println("1. Add User");
        System.out.println("2. Remove User");
        System.out.println("3. Create Book");
        System.out.println("4. Remove Book");
        System.out.println("5. List All Books with Stock Information");
        System.out.println("6. List All Books from a Specific Genre, with Stock Information");
        System.out.println("7. Get Currently Borrowed Books");
        System.out.println("8. Get Currently Borrowed and Borrow History of a User");
        System.out.println("9. Checkout Book");
        System.out.println("10. Return Book");
        System.out.println("");
        System.out.println("Enter the number of the requested action: ");


        int choose=0;
        choose=scanner.nextInt();
        System.out.println("");
        while(choose<1 || choose>10) {
            System.out.println("Please enter a valid action number.");
            choose=scanner.nextInt();
        }

        Choose(choose);
        scanner.close();
    }

    public static void Choose(int i) throws SQLException {
        if(i==1) {
            addUser();
        }
        else if(i==2){
            removeUser();
        }
        else if(i==3) {
            createBook();
        }
        else if(i==4) {
            removeBook();
        }
        else if(i==5) {
            listBooksWStock();
        }
        else if(i==6) {
            listBooksGenreWStock();
        }
        else if(i==7) {
            getCurrentlyBorrowed();
        }
        else if(i==8) {
            getBorrowHist();
        }
        else if(i==9) {
            checkoutBook();
        }
        else if(i==10) {
            returnBook();
        }
        else {
            System.out.println("An error has occurred. You're redirecting to main menu."); // Not possible but if happenns
            Menu();
        }
    }

    public static void main(String args[]) throws SQLException{
        Menu();
    }
    public static void returnMain() throws SQLException{
        Scanner scanner=new Scanner(System.in);
        System.out.println("Press 1 and Enter in order to return to Main Menu. Else press any other number to exit.");
        int m=scanner.nextInt();
        if(m==1){
            Menu();
        }
    }

    public static void addUser() throws SQLException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter name, surname, email and address to add user.");
        System.out.println("Name Surname: ");
        String user_name=scanner.nextLine();
        System.out.println("Email: ");
        String email=scanner.nextLine();
        System.out.println("Address: ");
        String address=scanner.nextLine();
        establishConnection();

        Statement statement;

        statement=connection.createStatement();
        String addUserSql="INSERT INTO User(user_name,email,address)" + "VALUES(\""+user_name+"\",\""+email+"\",\""+address+"\")";
        statement.executeUpdate(addUserSql);
        returnMain();
        statement.close();
        closeConnection();
        scanner.close();

    }

    public static void removeUser() throws SQLException{
        Scanner scanner = new Scanner(System.in);
        establishConnection();
        Statement statement;
        statement=connection.createStatement();
        ResultSet userList = statement.executeQuery("SELECT * FROM User");
        ResultSetMetaData userListMD = userList.getMetaData();
        int column = userListMD.getColumnCount();
        System.out.println("User ID - Name - Email - Address");
        while (userList.next()){
            for (int d=1; d<= column; d++){
                if(d>1) {
                    System.out.print(" ");
                }
                String colval = userList.getString(d);
                System.out.print(colval+" ");
            }System.out.println("");
        }
        System.out.println("");
        System.out.println("Enter the ID of the user that will be removed.");
        int user_id=scanner.nextInt();
        String removeUserSql ="DELETE FROM User WHERE user_id ="+user_id+"";
        statement.executeUpdate(removeUserSql);
        returnMain();
        statement.close();
        closeConnection();
        scanner.close();
    }

    public static void createBook() throws SQLException{
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter book name, stock amount and choose genre to add book.");
        System.out.println("Book Name: ");
        String book_name=scanner.nextLine();
        System.out.println("Stock Amount: ");
        String stock_amount=scanner.nextLine();
        establishConnection();
        Statement statement;
        statement=connection.createStatement();
        ResultSet genreList = statement.executeQuery("SELECT * FROM Genre ORDER BY genre_id ASC");
        ResultSetMetaData genreListMD = genreList.getMetaData();
        int column = genreListMD.getColumnCount();
        System.out.println("Genre List");
        while (genreList.next()){
            for (int d=1; d<= column; d++){
                if(d>1) {
                    System.out.print(" ");
                }
                String colval = genreList.getString(d);
                System.out.print(colval+" ");
            }System.out.println("");
        }

        System.out.println("Choose Genre from Genre List and enter Genre ID: ");
        int genreID=scanner.nextInt();

        String genreName="SELECT genre_name FROM Genre WHERE genre_id=?";
        PreparedStatement ps = connection.prepareStatement(genreName);
        ps.setInt(1, genreID);
        ResultSet rs=ps.executeQuery();
        rs.next();
        String genre = rs.getString("genre_name");

        String addBookSql="INSERT INTO Book(book_name,stock_amount,genre_name)" + "VALUES(\""+book_name+"\",\""+stock_amount+"\",\""+genre+"\")";
        statement.executeUpdate(addBookSql);
        System.out.println("Book added successfully!");
        returnMain();
        statement.close();
        closeConnection();
        scanner.close();

    }
    public static void removeBook() throws SQLException{
        Scanner scanner = new Scanner(System.in);
        establishConnection();
        Statement statement;
        statement=connection.createStatement();
        ResultSet bookList = statement.executeQuery("SELECT * FROM Book");
        ResultSetMetaData bookListMD = bookList.getMetaData();
        System.out.println("Book ID - Name - Stock - Genre");
        int column = bookListMD.getColumnCount();
        while (bookList.next()){
            for (int d=1; d<= column; d++){
                if(d>1) {
                    System.out.print(" ");
                }
                String colval = bookList.getString(d);
                System.out.print(colval+" ");
            }System.out.println("");
        }

        System.out.println("Enter the ID of the book that will be removed.");
        int book_id=scanner.nextInt();
        String removeUserSql ="DELETE FROM Book WHERE book_id ="+book_id+"";
        statement.executeUpdate(removeUserSql);
        System.out.println("Book removed successfully.");
        returnMain();
        statement.close();
        closeConnection();
        scanner.close();
    }
    public static void listBooksWStock() throws SQLException{
        establishConnection();
        Statement statement;
        statement=connection.createStatement();
        ResultSet bookList = statement.executeQuery("SELECT book_id,book_name,stock_amount FROM Book");
        ResultSetMetaData bookListMD = bookList.getMetaData();
        int column = bookListMD.getColumnCount();
        System.out.println("Book ID - Name - Stock Amount");
        while (bookList.next()){
            for (int d=1; d<= column; d++){
                if(d>1) {
                    System.out.print(" ");
                }
                String colval = bookList.getString(d);
                System.out.print(colval+" ");
            }System.out.println("");
        }
        returnMain();
        statement.close();
        closeConnection();
    }
    public static void listBooksGenreWStock() throws SQLException{
        establishConnection();
        Statement statement;
        Scanner scanner=new Scanner(System.in);
        statement=connection.createStatement();
        ResultSet genreList = statement.executeQuery("SELECT * FROM Genre ORDER BY genre_id ASC");
        ResultSetMetaData genreListMD = genreList.getMetaData();
        int column = genreListMD.getColumnCount();
        System.out.println("Choose Genre from Genre List and enter Genre ID to view registered books.");
        System.out.println("");
        System.out.println("Genre List");
        while (genreList.next()){
            for (int d=1; d<= column; d++){
                if(d>1) {
                    System.out.print(" ");
                }
                String colval = genreList.getString(d);
                System.out.print(colval+" ");
            }System.out.println("");
        }
        System.out.println("");
        System.out.println("Genre ID: ");
        int genreID=scanner.nextInt();
        System.out.println("");
        String genreName="SELECT genre_name FROM Genre WHERE genre_id=?";
        PreparedStatement ps = connection.prepareStatement(genreName);
        ps.setInt(1, genreID);
        ResultSet rs=ps.executeQuery();
        rs.next();
        String genre = rs.getString("genre_name");
        ResultSet bookList = statement.executeQuery("SELECT book_id,book_name,stock_amount FROM Book WHERE genre_name='"+genre + "'");
        ResultSetMetaData bookListMD = bookList.getMetaData();
        int column1 = bookListMD.getColumnCount();
        System.out.println("Book ID - Name - Stock Amount");
        while (bookList.next()){
            for (int d=1; d<= column1; d++){
                if(d>1) {
                    System.out.print(" ");
                }
                String colval = bookList.getString(d);
                System.out.print(colval+" ");
            }System.out.println("");
        }
        System.out.println("");
        returnMain();
        statement.close();
        closeConnection();
        scanner.close();
    }

    public static void getCurrentlyBorrowed() throws SQLException{
        establishConnection();
        Statement statement;
        statement=connection.createStatement();
        ResultSet borrowedList = statement.executeQuery("SELECT book_name,user_name FROM Book JOIN Checkout ON Book.book_id=Checkout.book_id JOIN User ON Checkout.user_id=User.User_id WHERE Checkout.isCurrentlyBorrowed='Yes'");
        ResultSetMetaData borrowedListMD = borrowedList.getMetaData();
        int column = borrowedListMD.getColumnCount();
        System.out.println("Currently Borrowed Books List");
        System.out.println("Book Name - User Name");
        while (borrowedList.next()){
            for (int d=1; d<= column; d++){
                if(d>1) {
                    System.out.print(" ");
                }
                String colval = borrowedList.getString(d);
                System.out.print(colval+" ");
            }System.out.println("");
        }
        System.out.println("");
        returnMain();
        statement.close();
        closeConnection();
    }
    public static void getBorrowHist() throws SQLException{
        establishConnection();
        Scanner scanner=new Scanner(System.in);
        Statement statement;
        statement=connection.createStatement();
        ResultSet userList = statement.executeQuery("SELECT user_id,user_name FROM User");
        ResultSetMetaData userListMD = userList.getMetaData();
        int column = userListMD.getColumnCount();
        System.out.println("User ID - Name");
        while (userList.next()){
            for (int d=1; d<= column; d++){
                if(d>1) {
                    System.out.print(" ");
                }
                String colval = userList.getString(d);
                System.out.print(colval+" ");
            }System.out.println("");
        }
        System.out.println("");
        System.out.println("Enter the ID of the user to see Borrow History.");
        int user_id=scanner.nextInt();

        ResultSet borrowedList = statement.executeQuery("SELECT book_name,user_name,isCurrentlyBorrowed,checkout_date FROM Book JOIN Checkout ON Book.book_id=Checkout.book_id JOIN User ON Checkout.user_id=User.user_id WHERE Checkout.user_id="+user_id+"");
        ResultSetMetaData borrowedListMD = borrowedList.getMetaData();
        int column1 = borrowedListMD.getColumnCount();
        System.out.println("Borrow History");
        System.out.println("Book Name- User Name - Is Currently Borrowed - Checkout Date");
        while (borrowedList.next()){
            for (int d=1; d<= column1; d++){
                if(d>1) {
                    System.out.print(" ");
                }
                String colval = borrowedList.getString(d);
                System.out.print(colval+" ");
            }System.out.println("");
        }
        returnMain();
        statement.close();
        closeConnection();
        scanner.close();
    }

    public static void checkoutBook() throws SQLException{
        establishConnection();
        Statement statement;
        Scanner scanner=new Scanner(System.in);
        statement=connection.createStatement();
        ResultSet bookList = statement.executeQuery("SELECT * FROM Book");
        ResultSetMetaData bookListMD = bookList.getMetaData();
        int column = bookListMD.getColumnCount();
        System.out.println("Book ID - Name - Stock - Genre");
        while (bookList.next()){
            for (int d=1; d<= column; d++){
                if(d>1) {
                    System.out.print(" ");
                }
                String colval = bookList.getString(d);
                System.out.print(colval+" ");
            }System.out.println("");
        }

        System.out.println("Enter Book ID: ");
        int book_id=scanner.nextInt();
        String genreName="SELECT stock_amount FROM Book WHERE book_id=?";
        PreparedStatement ps = connection.prepareStatement(genreName);
        ps.setInt(1, book_id);
        ResultSet rs=ps.executeQuery();
        rs.next();
        int stock = rs.getInt("stock_amount");
        if(stock<1){
            System.out.println("This book is out of stock.");
        }
        else {
            ResultSet userList = statement.executeQuery("SELECT user_id,user_name FROM User");
            ResultSetMetaData userListMD = userList.getMetaData();
            int column1 = userListMD.getColumnCount();
            System.out.println("User ID - Name");
            while (userList.next()) {
                for (int d = 1; d <= column1; d++) {
                    if (d > 1) {
                        System.out.print(" ");
                    }
                    String colval = userList.getString(d);
                    System.out.print(colval + " ");
                }
                System.out.println("");
            }
            System.out.println("");
            System.out.println("Enter User ID: ");
            int user_id = scanner.nextInt();

            statement = connection.createStatement();
            String addCheckoutSql = "INSERT INTO Checkout(user_id,book_id,isCurrentlyBorrowed)" + "VALUES(\"" + user_id + "\",\"" + book_id + "\",'Yes')";
            statement.executeUpdate(addCheckoutSql);
            String stockMinus1 = "UPDATE Book SET stock_amount= (stock_amount-1) WHERE book_id =" +book_id+"";
            statement.executeUpdate(stockMinus1);
            System.out.println("Checkout has successfully completed!");
        }
        returnMain();
        statement.close();
        closeConnection();
        scanner.close();

    }
    public static void returnBook() throws SQLException{
        establishConnection();
        Scanner scanner=new Scanner(System.in);
        Statement statement;
        statement=connection.createStatement();
        ResultSet borrowedList = statement.executeQuery("SELECT Checkout.checkout_id,book_name,user_name FROM Book JOIN Checkout ON Book.book_id=Checkout.book_id JOIN User ON Checkout.user_id=User.User_id WHERE Checkout.isCurrentlyBorrowed='Yes'");
        ResultSetMetaData borrowedListMD = borrowedList.getMetaData();
        int column = borrowedListMD.getColumnCount();
        System.out.println("Currently Borrowed Books List");
        System.out.println("Checkout ID - Book Name - User Name");
        while (borrowedList.next()){
            for (int d=1; d<= column; d++){
                if(d>1) {
                    System.out.print(" ");
                }
                String colval = borrowedList.getString(d);
                System.out.print(colval+" ");
            }System.out.println("");
        }
        System.out.println("Enter Checkout ID to return book.");
        int checkout_id=scanner.nextInt();
        String isCurrentlyBorrowed = "UPDATE Checkout SET isCurrentlyBorrowed='No' WHERE checkout_id =" +checkout_id+"";
        statement.executeUpdate(isCurrentlyBorrowed);
        String getBookID="SELECT Book.book_id FROM Book JOIN Checkout ON Book.book_id=Checkout.book_id WHERE Checkout.checkout_id="+checkout_id+"";
        PreparedStatement ps = connection.prepareStatement(getBookID);
        ResultSet rs=ps.executeQuery();
        rs.next();
        int book_id = rs.getInt("book_id");
        String stockPlus1 = "UPDATE Book SET stock_amount= (stock_amount+1) WHERE book_id =" +book_id+"";
        statement.executeUpdate(stockPlus1);
        System.out.println("Book is successfully returned!");
        returnMain();
        statement.close();
        closeConnection();
        scanner.close();

    }




}


