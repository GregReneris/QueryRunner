/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package queryrunner;

import javax.management.Query;
import java.util.ArrayList;
import java.util.Scanner;


/**
 *
 * QueryRunner takes a list of Queries that are initialized in it's constructor
 * and provides functions that will call the various functions in the QueryJDBC class
 * which will enable MYSQL queries to be executed. It also has functions to provide the
 * returned data from the Queries. Currently the eventHandlers in QueryFrame call these
 * functions in order to run the Queries.
 */
public class QueryRunner {


    public QueryRunner()
    {
        this.m_jdbcData = new QueryJDBC();
        m_updateAmount = 0;
        m_queryArray = new ArrayList<>();
        m_error="";


        this.m_projectTeamApplication="Coffee Shop";    // THIS NEEDS TO CHANGE FOR YOUR APPLICATION

        // Each row that is added to m_queryArray is a separate query. It does not work on Stored procedure calls.
        // The 'new' Java keyword is a way of initializing the data that will be added to QueryArray. Please do not change
        // Format for each row of m_queryArray is: (QueryText, ParamaterLabelArray[], LikeParameterArray[], IsItActionQuery, IsItParameterQuery)

        //    QueryText is a String that represents your query. It can be anything but Stored Procedure
        //    Parameter Label Array  (e.g. Put in null if there is no Parameters in your query, otherwise put in the Parameter Names)
        //    LikeParameter Array  is an array I regret having to add, but it is necessary to tell QueryRunner which parameter has a LIKE Clause. If you have no parameters, put in null. Otherwise put in false for parameters that don't use 'like' and true for ones that do.
        //    IsItActionQuery (e.g. Mark it true if it is, otherwise false)
        //    IsItParameterQuery (e.g.Mark it true if it is, otherwise false)

//        m_queryArray.add(new QueryData("Select * from contact", null, null, false, false));   // THIS NEEDS TO CHANGE FOR YOUR APPLICATION
//        m_queryArray.add(new QueryData("Select * from contact where contact_id=?", new String [] {"CONTACT_ID"}, new boolean [] {false},  false, true));        // THIS NEEDS TO CHANGE FOR YOUR APPLICATION
//        m_queryArray.add(new QueryData("Select * from contact where contact_name like ?", new String [] {"CONTACT_NAME"}, new boolean [] {true}, false, true));        // THIS NEEDS TO CHANGE FOR YOUR APPLICATION
//        m_queryArray.add(new QueryData("insert into contact (contact_id, contact_name, contact_salary) values (?,?,?)",new String [] {"CONTACT_ID", "CONTACT_NAME", "CONTACT_SALARY"}, new boolean [] {false, false, false}, true, true));// THIS NEEDS TO CHANGE FOR YOUR APPLICATION

        m_queryArray.add(new QueryData("Select \n" +
                "\tidCustomers AS 'Customer ID',\n" +
                "    Cust_First_Name AS 'First Name',\n" +
                "    Cust_Last_Name AS 'Last Name',\n" +
                "    Cust_Points AS 'Points',\n" +
                "    Cust_Phone AS 'Phone'\n" +
                "FROM customers", null, null, false, false)); //Query 1

        m_queryArray.add(new QueryData("Select\n" +
                "\tTransaction_ID AS 'Trans ID',\n" +
                "    idStaff AS 'Staff ID',\n" +
                "    idCustomers AS 'Customer ID',\n" +
                "    Shop_Information_Shop_ID AS 'Shop ID',\n" +
                "    Transaction_Date_Time AS 'Date',\n" +
                "    Transaction_Sale_Price AS 'Sale Price',\n" +
                "    Transaction_Point_Value AS 'Points',\n" +
                "    Transaction_Tax AS 'Tax',\n" +
                "    Transaction_Discount AS 'Discount',\n" +
                "    Transaction_Total AS 'Total',\n" +
                "    Transaction_Note AS 'Notes'\n" +
                "FROM sale_transactions\n" +
                "where Transaction_ID=?", new String [] {"Enter Transaction ID:"}, new boolean [] {false},  false, true)); //QUERY 2

        m_queryArray.add(new QueryData("Select\n" +
                "\tTransaction_ID AS 'Trans ID',\n" +
                "    idStaff AS 'Staff ID',\n" +
                "    idCustomers AS 'Customer ID',\n" +
                "    Shop_Information_Shop_ID AS 'Shop ID',\n" +
                "    Transaction_Date_Time AS 'Date',\n" +
                "    Transaction_Sale_Price AS 'Sale Price',\n" +
                "    Transaction_Point_Value AS 'Points',\n" +
                "    Transaction_Tax AS 'Tax',\n" +
                "    Transaction_Discount AS 'Discount',\n" +
                "    Transaction_Total AS 'Total',\n" +
                "    Transaction_Note AS 'Notes'\n" +
                "FROM sale_transactions\n" +
                "where idCustomers=?", new String [] {"Enter Customer ID:"}, new boolean [] {true}, false, true));        // Query 3

        m_queryArray.add(new QueryData("SELECT \n" +
                "S.Transaction_ID AS 'Trans ID',\n" +
                "\tpit.Products_in_Transaction_ID AS 'Line ID',\n" +
                "   \tpit.Product_Price AS 'Line item Price',\n" +
                "\tconcat (customers.Cust_First_Name, ' ', customers" +
                ".Cust_Last_Name) as 'Customer Name',\n" +
                "    products.product_Description AS 'Product Description',\n" +
                "    pit.Quantity AS 'Quantity Sold',\n" +
                "    staff.First_Name AS 'Employee'\n" +
                "FROM sale_transactions AS S\n" +
                "INNER JOIN staff ON S.idStaff = staff.idStaff\n" +
                "INNER JOIN customers ON S.idCustomers = customers" +
                ".idCustomers\n" +
                "INNER JOIN products_in_transaction AS pit ON pit" +
                ".Transaction_ID = S.Transaction_ID\n" +
                "INNER JOIN products ON pit.Product_ID = products" +
                ".Product_ID\n" +
                "WHERE customers.idCustomers = ?;\n", new String [] {"Enter Customer ID:"}, new boolean [] {true}, false, true));        // Query 4

        m_queryArray.add(new QueryData("SELECT\n" + "\tDISTINCT Reservations_ID AS 'Reservation#',\n" + " Shop_ID AS 'Shop ID',\n" + " Reservation_Start_Time AS 'Arrival Time',\n" + " Reservation_Party_Size AS 'Party Size'\n" + "FROM reservations\n" + "WHERE Reservation_Start_Time > CURDATE()\n" + "ORDER BY Reservation_Start_Time;", null, null, false, false)); //Query 5
        //query 5 ^^

        m_queryArray.add(new QueryData("SELECT  S.idStaff,\n" +
                "\tconcat (S.First_Name, ' ', S.Last_Name) as 'Staff Name',\n" +
                "\tS.Position_Title as 'Position Title',\n" +
                "\tM.First_Name as 'Manager Name',\n" +
                "\tC.Start_Time as 'Start Time',\n" +
                "\tC.End_Time as 'End Time'\n" +
                "FROM staff as S\n" +
                "INNER JOIN staff_schedule as C ON C.idStaff = S.idStaff \n" +
                "INNER JOIN staff as M ON S.Manager_Staff_ID = M.idStaff\n" +
                "WHERE S.Last_Name like ? \n" +
                "ORDER BY C.Start_Time;\n", new String [] {"Enter Staff Last Name:"},
                new boolean [] {true}, false, true)); // query 6, "like" requirment.

        m_queryArray.add(new QueryData("UPDATE customers SET \n" +
                "Cust_First_Name = ?, \n" +
                "Cust_Last_Name = ?,\n" +
                "Cust_Phone = ?\n" +
                "WHERE idCustomers = ?",
                new String [] {"Customer First Name", "Customer Last Name", "Customer Phone Number", "Customer ID" }, new boolean []
                {false, false, false, false}, true, true)); //query 7 update

        //proper sales transaction inserted.


    }


    public int GetTotalQueries()
    {
        return m_queryArray.size();
    }

    public int GetParameterAmtForQuery(int queryChoice)
    {
        QueryData e=m_queryArray.get(queryChoice);
        return e.GetParmAmount();
    }

    public String  GetParamText(int queryChoice, int parmnum )
    {
        QueryData e=m_queryArray.get(queryChoice);
        return e.GetParamText(parmnum);
    }

    public String GetQueryText(int queryChoice)
    {
        QueryData e=m_queryArray.get(queryChoice);
        return e.GetQueryString();
    }

    /**
     * Function will return how many rows were updated as a result
     * of the update query
     * @return Returns how many rows were updated
     */

    public int GetUpdateAmount()
    {
        return m_updateAmount;
    }

    /**
     * Function will return ALL of the Column Headers from the query
     * @return Returns array of column headers
     */
    public String [] GetQueryHeaders()
    {
        return m_jdbcData.GetHeaders();
    }

    /**
     * After the query has been run, all of the data has been captured into
     * a multi-dimensional string array which contains all the row's. For each
     * row it also has all the column data. It is in string format
     * @return multi-dimensional array of String data based on the resultset
     * from the query
     */
    public String[][] GetQueryData()
    {
        return m_jdbcData.GetData();
    }

    public String GetProjectTeamApplication()
    {
        return m_projectTeamApplication;
    }
    public boolean  isActionQuery (int queryChoice)
    {
        QueryData e=m_queryArray.get(queryChoice);
        return e.IsQueryAction();
    }

    public boolean isParameterQuery(int queryChoice)
    {
        QueryData e=m_queryArray.get(queryChoice);
        return e.IsQueryParm();
    }


    public boolean ExecuteQuery(int queryChoice, String [] parms)
    {
        boolean bOK = true;
        QueryData e=m_queryArray.get(queryChoice);
        bOK = m_jdbcData.ExecuteQuery(e.GetQueryString(), parms, e.GetAllLikeParams());
        return bOK;
    }

    public boolean ExecuteUpdate(int queryChoice, String [] parms)
    {
        boolean bOK = true;
        QueryData e=m_queryArray.get(queryChoice);
        bOK = m_jdbcData.ExecuteUpdate(e.GetQueryString(), parms);
        m_updateAmount = m_jdbcData.GetUpdateCount();
        return bOK;
    }


    public boolean Connect(String szHost, String szUser, String szPass, String szDatabase)
    {

        boolean bConnect = m_jdbcData.ConnectToDatabase(szHost, szUser, szPass, szDatabase);
        if (bConnect == false)
            m_error = m_jdbcData.GetError();
        return bConnect;
    }

    public boolean Disconnect()
    {
        // Disconnect the JDBCData Object
        boolean bConnect = m_jdbcData.CloseDatabase();
        if (bConnect == false)
            m_error = m_jdbcData.GetError();
        return true;
    }

    public String GetError()
    {
        return m_error;
    }

    private QueryJDBC m_jdbcData;
    private String m_error;
    private String m_projectTeamApplication;
    private ArrayList<QueryData> m_queryArray;
    private int m_updateAmount;
    private int holdingFunction;
    private int printFunction;



    /*
    printAttributes prints a string of attributes spaced appropriately to
    create a table for the console output.
     */
    private static int printAttributes(String[] attributes)
    {
        for (int i = 0; i < attributes.length; i++)
        {
            //reformat into printf statements.
//            System.out.print(attributes[i] +"  |  ");
            System.out.printf("      %18s  ", attributes[i]);
//            System.out.print(" | ");

        }
        System.out.println(" ");

    return 1;
    }

    /*
    printFunction prints the database response to the relevant query. It's a
    [][] 2d array response, and there are 2 for loops printing to console.
     */
    private static int printFunction (String[][] result)
    {
        //reformat into printf statements.
        //give each attribute / result 15 spaces.
        for(int rowIndex = 0; rowIndex<result.length; rowIndex++)
        {
            String[] row = result[rowIndex];
            System.out.printf( "%1d:", rowIndex);
            for(int colIndex = 0; colIndex< row.length; colIndex++)
                System.out.printf("   %20s   ", row[colIndex]);
            System.out.println();
        }
        return 1;
    }

/*
holdingFunction takes in an integer, QuerryRunner, and Scanner to hold the
majority of the logic for the console program. Those are the parameters needed
in order to run the logic. It calls the relevant functions and prints query
responses to the console.
 */
    private static int holdingFunction(int i, QueryRunner queryrunner, Scanner keyboard)
    {

        if (queryrunner.GetParameterAmtForQuery(i) > 0)
        {
            int amt = queryrunner.GetParameterAmtForQuery(i);
            String[] params = new String[amt];
            System.out.println();
            System.out.println("The query " + (i+1)  + " has " + amt + " parameters.");


            for (int j = 0; j < amt; j++)
            {
                System.out.println("Param " + (j+1) + ":");
                System.out.print(queryrunner.GetParamText(i, j));
                System.out.println();
                System.out.println("Please input a value for the query: ");

                params[j] = keyboard.next();
            }

            if (queryrunner.isActionQuery(i))
            {
                queryrunner.ExecuteUpdate(i, params);
                System.out.println("Rows updated: " + queryrunner.GetUpdateAmount());
            }
            else
            {
                queryrunner.ExecuteQuery(i, params);
                var result = queryrunner.GetQueryData();
                var attributes = queryrunner.GetQueryHeaders();
                if(result == null)
                {
                    System.out.println("There were no results.");
                }
                else
                {
                    printAttributes(attributes);
                    printFunction(result);
                }
            }

        }
        else
        {

            System.out.println("The Query response of query " + (i+1) + " is :");


            String[] params = new String[0]; // needed for an empty params array.
            queryrunner.ExecuteQuery(i, params);
            var result = queryrunner.GetQueryData();
            var attributes = queryrunner.GetQueryHeaders();

            if(result == null)
            {
                System.out.println("There were no results.");
            }
            else
            {
                printAttributes(attributes);

                printFunction(result);
            }
        }

        return 1;
    }





    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        final QueryRunner queryrunner = new QueryRunner();

        if (args.length == 0)
        {
            java.awt.EventQueue.invokeLater(new Runnable() {
                public void run() {

                    new QueryFrame(queryrunner).setVisible(true);
                }
            });
        }
        else
        {
            if (args[0].equals ("-console"))
            {
                Scanner keyboard = new Scanner(System.in);

                String hostname;
                String username;
                String password;
                String databasename = "mm_cpsc502102team03"; //this is to print proper welcome message.
                boolean connected = false;

                while (!connected)
                {

                    System.out.println("Welcome. Please input hostname: ");
                    hostname = keyboard.nextLine();
                    System.out.println("host is : " + hostname);

                    System.out.println("Please input username: ");
                    username = keyboard.nextLine();
                    System.out.println("username is : " + username);

                    System.out.println("Please input password: ");
                    password = keyboard.nextLine();
                    System.out.println("password is : " + password);

                    System.out.println("Please input database name: ");
                    databasename = keyboard.nextLine();
                    System.out.println("database name is : " + databasename);

                    connected = queryrunner.Connect(hostname, username, password, databasename);

                    if(!connected)
                        System.out.println("Error connecting. Please try again");
                }
                System.out.println("Welcome to " + databasename);

                int n = queryrunner.GetTotalQueries();
                int i = 0;

                do
                {
                    System.out.println("Please enter your query number, 1-7.");
                    i = keyboard.nextInt();
                    i--;
                } while (i < 0);

                do
                {
                    holdingFunction(i,queryrunner, keyboard);
                    System.out.println("Please enter your next query number, 1-7, or -1 to exit");
                    i = keyboard.nextInt();
                    i--; //so users can input the proper numerical query number, but the array offset is -1.

                } while (i < n && i >= 0);

                keyboard.close();

                try
                {
                    queryrunner.Disconnect();
                } catch (Exception e)
                {
                    System.out.println("Goodbye!");
                };


                // NOTE - IF THERE ARE ANY ERRORS, please print the Error output
                // NOTE - The QueryRunner functions call the various JDBC Functions that are in QueryJDBC. If you would rather code JDBC
                // functions directly, you can choose to do that. It will be harder, but that is your option.
                // NOTE - You can look at the QueryRunner API calls that are in QueryFrame.java for assistance. You should not have to
                //    alter any code in QueryJDBC, QueryData, or QueryFrame to make this work.

            }
        }
    }
}
