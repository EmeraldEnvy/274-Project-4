/**
 * Alexander Pham
 * November 28, 2019
 * Travels from start of a maze to end, marking all paths using Depth and Breadth search
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Scanner;
import java.awt.Point;
import java.util.Stack;
import java.util.Queue;

public class Main {
    public static void main(String args[]) {
        while( true ) {
            System.out.print("Enter a maze # : ");
            int mazeNumber = CheckInput.getIntRange(1, 4);
            //Turn user's response into file name
            String file = "maze" + mazeNumber + ".txt";
            Character[][] mazeArray = readMaze(file);
            displayMaze( mazeArray );
            Point startLocation = new Point( findStart( mazeArray ) );
            Point finishLocation = new Point ( findFinish( mazeArray) );

            //Menu
            System.out.println( "1. Depth First Search \n" +
                    "2. Breadth Search First\n" +
                    "3. Solve it yourself\n" +
                    "4. Quit" );
            System.out.print( "Choose an option : " );
            int userChoice = CheckInput.getIntRange(1 , 4 );

            //Depth First Search
            if ( userChoice == 1 ){
                mazeArray = depthFirstSearch( mazeArray , startLocation );
                displayMaze( mazeArray );
            }
            //Breadth First Search
            if ( userChoice == 2 ){
                breadthFirstSearch( mazeArray , startLocation );
                displayMaze( mazeArray );
            }
            //Manual solve
            if ( userChoice == 3 ){
                Point userLocation = new Point(manualSolve( mazeArray , startLocation ));
                //Reset the maze with the user's position as starting point
                mazeArray = readMaze( file );
                mazeArray[startLocation.x][startLocation.y] = ' ';
                mazeArray[userLocation.x][userLocation.y] = 's';
                //Congratulates user for finishing maze
                if ( userLocation.x==finishLocation.x && userLocation.y==finishLocation.y ) {
                    System.out.println( "Congratulations" );
                }
                //Solves the maze from the users location, if user gives up
                else{
                    mazeArray = depthFirstSearch( mazeArray, userLocation );
                    displayMaze(mazeArray);
                }

            }
            if ( userChoice == 4 ){
                break;
            }
        }
        System.out.println( "Later loser" );
    }

    /**
     * Read file
     * @param file
     * @return maze array
     */
    public static Character[][] readMaze( String file ){
        Character [][] mazeArray = new Character [0][0];
        try {
            //Find the rows and columns in file
            Scanner read = new Scanner(new File(file));
            String rowsString = read.next();
            String columnsString = read.next();
            //Turn string to int
            int rows = Integer.parseInt(rowsString);
            int columns = Integer.parseInt(columnsString);
            mazeArray = new Character [rows][columns];
            //Create the maze
            int count = 0;
            do {
                String line = read.nextLine();
                for ( int i = 0 ; i < rows ; i++ ) {
                    line = read.nextLine();
                    for ( int j = 0 ; j < columns ; j++ ) {
                        char character = line.charAt( j );
                        mazeArray[i][j] = character;
                    }
                }
            } while (read.hasNext());
        } catch (FileNotFoundException fnf) {
            System.out.println("File was not found");
        }
        return mazeArray;
    }

    /**
     * Display maze
     * @param mazeArray
     */
    public static void displayMaze( Character[][] mazeArray ){
        for ( int i = 0 ; i < mazeArray.length ; i++ ){
            for (int j = 0 ; j < mazeArray[0].length ; j++ ){
                System.out.print( mazeArray[i][j] + " " );
            }
            System.out.println();
        }
    }

    /**
     * Find start
     * @param mazeArray
     * @return start point
     */
    public static Point findStart( Character[][] mazeArray ){
        Point startLocation = new Point( 0 , 0 );
        for ( int i = 0 ; i < mazeArray.length ; i++ ){
            for (int j = 0 ; j < mazeArray[0].length ; j++ ){
                if ( mazeArray[i][j].equals('s') ){
                    startLocation = new Point( i , j );
                }
            }
        }
        return startLocation;
    }

    /**
     * Find finish
     * @param mazeArray
     * @return finish point
     */
    public static Point findFinish( Character[][] mazeArray ){
        Point finishLocation = new Point( 0 , 0 );
        for ( int i = 0 ; i < mazeArray.length ; i++ ){
            for (int j = 0 ; j < mazeArray[0].length ; j++ ){
                if ( mazeArray[i][j].equals('f') ){
                    finishLocation = new Point( i , j );
                }
            }
        }
        return finishLocation;
    }

    /**
     * Depth first search
     * @param mazeArray
     * @param currentLocation
     * @return maze array
     */
    public static Character[][] depthFirstSearch( Character[][] mazeArray , Point currentLocation ){
        Stack <Point> stack = new Stack<Point>();
        stack.push( currentLocation );
        while( mazeArray[currentLocation.x][currentLocation.y] != ('f') ){
            Point remove = stack.pop();
            int x = remove.x;
            int y = remove.y;
            //Marks every visited blank space
            if ( mazeArray[x][y] == ' ' ){
                mazeArray[x][y] = '.';
            }
            //up
            if ( mazeArray[x-1][y]!=('*') && mazeArray[x-1][y]!=('.') && mazeArray[x-1][y]!=('s') ){
                currentLocation = new Point(x-1 , y);
                stack.push(currentLocation);
            }
            //down
            if ( mazeArray[x+1][y]!=('*') && mazeArray[x+1][y]!=('.') && mazeArray[x+1][y]!=('s') ){
                currentLocation = new Point(x+1 , y);
                stack.push(currentLocation);
            }
            //left
            if ( mazeArray[x][y-1]!=('*') && mazeArray[x][y-1]!=('.') && mazeArray[x][y-1]!=('s') ){
                currentLocation = new Point(x , y-1);
                stack.push(currentLocation);
            }
            //right
            if ( mazeArray[x][y+1]!=('*') && mazeArray[x][y+1]!=('.') && mazeArray[x][y+1]!=('s') ){
                currentLocation = new Point(x , y+1);
                stack.push(currentLocation);
            }
        }
        return mazeArray;
    }

    /**
     * Breadth first search
     * @param mazeArray
     * @param currentLocation
     * @return maze array
     */
    public static Character[][] breadthFirstSearch( Character[][] mazeArray , Point currentLocation ){
        Queue <Point> queue = new LinkedList<Point>();
        queue.add( currentLocation );

        while( mazeArray[(int)currentLocation.getX()][(int)currentLocation.getY()] != ('f') ){
            Point remove = queue.remove();
            int x = remove.x;
            int y = remove.y;
            //Marks every visited blank space
            if ( mazeArray[x][y] == ' ' ){
                mazeArray[x][y] = '.';
            }
            //up
            if ( mazeArray[x-1][y]!=('*') && mazeArray[x-1][y]!=('.') && mazeArray[x-1][y]!=('s') ){
                currentLocation = new Point(x-1 , y);
                queue.add(currentLocation);
            }
            //down
            if ( mazeArray[x+1][y]!=('*') && mazeArray[x+1][y]!=('.') && mazeArray[x+1][y]!=('s') ){
                currentLocation = new Point(x+1 , y);
                queue.add(currentLocation);
            }
            //left
            if ( mazeArray[x][y-1]!=('*') && mazeArray[x][y-1]!=('.') && mazeArray[x][y-1]!=('s')  ){
                currentLocation = new Point(x , y-1);
                queue.add(currentLocation);
            }
            //right
            if ( mazeArray[x][y+1]!=('*') && mazeArray[x][y+1]!=('.') && mazeArray[x][y+1]!=('s') ){
                currentLocation = new Point(x , y+1);
                queue.add(currentLocation);
            }
        }
        return mazeArray;
    }

    /**
     * User manually solves maze
     * @param mazeArray
     * @param currentLocation
     * @return current location
     */
    public static Point manualSolve ( Character[][] mazeArray , Point currentLocation ){
        Stack<Point> stack = new Stack<Point>();
        stack.push( currentLocation );
        while( mazeArray[currentLocation.x][currentLocation.y] != ('f') ){
            Point remove = stack.pop();
            int x = remove.x;
            int y = remove.y;
            //Marks every visited blank space
            mazeArray[x][y] = 's';
            displayMaze( mazeArray );
            System.out.println("Choose a direction\n" +
                    "1. Up\n" +
                    "2. Down\n" +
                    "3. Left\n" +
                    "4. Right\n" +
                    "5. Give up");
            int userDirection = CheckInput.getIntRange(1 , 5 );
            mazeArray[x][y] = '.';
            System.out.println();
            //left
            if ( userDirection == 3 ){
                if ( mazeArray[x][y-1]!=('*') ) {
                    currentLocation = new Point(x, y - 1);
                    stack.push(currentLocation);
                }
                else{
                    System.out.println( "Dead end" );
                    mazeArray[x][y] = 's';
                    stack.push(currentLocation);
                }
            }
            //right
            if ( userDirection == 4 ){
                if ( mazeArray[x][y+1]!=('*') ) {
                    currentLocation = new Point(x, y + 1);
                    stack.push(currentLocation);
                }
                else{
                    System.out.println( "Dead end" );
                    mazeArray[x][y] = 's';
                    stack.push(currentLocation);
                }
            }
            //up
            if ( userDirection == 1 ){
                if ( mazeArray[x-1][y]!=('*') ) {
                    currentLocation = new Point(x - 1, y);
                    stack.push(currentLocation);
                }
                else{
                    System.out.println( "Dead end" );
                    mazeArray[x][y] = 's';
                    stack.push(currentLocation);
                }
            }
            //down
            if ( userDirection == 2 ){
                if( mazeArray[x+1][y]!=('*') ) {
                    currentLocation = new Point(x + 1, y);
                    stack.push(currentLocation);
                }
                else{
                    System.out.println( "Dead end" );
                    mazeArray[x][y] = 's';
                    stack.push(currentLocation);
                }
            }
            if ( userDirection == 5 ){
                return currentLocation;
            }
        }
        displayMaze( mazeArray );
        return currentLocation;
    }
}
