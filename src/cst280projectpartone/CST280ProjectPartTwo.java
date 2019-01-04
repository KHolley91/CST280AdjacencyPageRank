package cst280projectpartone;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Kyle Holley - CST280 - Project Part 2
 */
public class CST280ProjectPartTwo {

    //delecring some global stuff
    static final HashMap<Integer, Double> pageRankMap = new HashMap<>();
    static final int ITERATIONS = 50;
    static final DecimalFormat df = new DecimalFormat("0.0000");

    static class Graph {

        int vertex;
        LinkedList<Integer>[] adjacencyListArrayLL;

        //constructor for our graph class
        Graph(int V) {
            this.vertex = V;

            //declare the array and set it equal to number of vertexs
            adjacencyListArrayLL = new LinkedList[V];

            // Create a new LinkedList for each vertex 
            for (int i = 0; i < V; i++) {
                adjacencyListArrayLL[i] = new LinkedList<>();

            }
        }
    }

    // Adds an edge 
    static void addEdge(Graph graph, int start, int finish) {
        // Add an edge from start to finish. 
        graph.adjacencyListArrayLL[start].add(finish);
        //If the graph was undirected we would just do the same thing backwards

    }

    //We will print everything here, both as output to console
    //and output to file
    static void printGraph(Graph graph, int totalVertex, HashMap map) {
        int sendVertex = 0;

        try {
            //declaring a writer in order to write to file
            Writer wr = new FileWriter("Pagerank.txt");

            //just by way i intitlized everything this is weeding out any nodes 
            //that dont point to anything. Since the start of file says 9 is the
            //highest vertex but not all of 0-9 are used. So if the size of LL
            //is 0 then that means its not used and I can get rid of it, which
            //brings us to the case of our test file which is 0;
            for (int i = 0; i < graph.vertex; i++) {
                if (graph.adjacencyListArrayLL[i].size() != 0) {

                    sendVertex++;
                }
            }

            for (int i = 0; i < graph.vertex; i++) {

                //I do something similar here to the logic above, I just want the
                //variable holding it to be local to the print graph function in all
                //so I do it above.
                if (graph.adjacencyListArrayLL[i].size() != 0) {
                    testPageRankAgain(graph, totalVertex, i, sendVertex);

                    //output to console for vertex name, rank (just in text bc not calculating yet) 
                    //and the out degree just by taking size since we basically split each node into
                    //its own linked list
                    System.out.println("Vertex " + i + ": " + "rank = " + df.format((Double) pageRankMap.get(i)) + "  " + "out-degree = "
                            + graph.adjacencyListArrayLL[i].size());

                    //same thing that is printed to console but printed to file in the
                    //format that was asked for
                    wr.write(i + "(" + df.format((pageRankMap.get(i))) + ") " + graph.adjacencyListArrayLL[i].size() + " ");

                    //output to console and logic to give adjacency list, then the 
                    //same thing written to a text file
                    System.out.print("Edges from " + i + " to: ");
                    for (Integer workThru : graph.adjacencyListArrayLL[i]) {
                        // wr.write(pageRankMap.get(i).toString());
                        System.out.print(workThru + " ");
                        wr.write(workThru + " ");
                    }

                    //new line for console output
                    System.out.println("\n");
                    //new line for file we write to
                    wr.write(System.lineSeparator());
                }
            }
            //flush then close file we wrote to now that were done
            wr.flush();
            wr.close();

        } catch (IOException e) {
        }
    }

    static void testPageRankAgain(Graph graph, int totalVertex, int curNode, int refurbVert) {

        ArrayList<Integer> pointingTo = new ArrayList<>();

        double rank = 1 / refurbVert;
        double rankOfBeingHeld = 0;

        for (int i = 0; i <= totalVertex - 1; i++) {
            if (graph.adjacencyListArrayLL[i].contains(curNode)) {

                pointingTo.add(i);
            }
        }

        final double HOLD_START_OF_RANK_FNC = 0.9;
        final double POINT_ONE_FOR_FNC = 0.1;

        try {
            for (int i = 0; i <= pointingTo.size() - 1; i++) {
                rankOfBeingHeld += ((Double) pageRankMap.get(pointingTo.get(i)) / graph.adjacencyListArrayLL[pointingTo.get(i)].size());

            }
            rank = (HOLD_START_OF_RANK_FNC * rankOfBeingHeld) + (POINT_ONE_FOR_FNC / refurbVert);

        } catch (Exception e) {

        }

        pageRankMap.put(curNode, rank);

    }

    public static void main(String[] args) {
        final int ITERATIONS = 50;
        int holdRank = 0;
        double rank = 1;
        ArrayList<Integer> holdNodesFrom = new ArrayList<>();
        ArrayList<Integer> holdNodesTo = new ArrayList<>();

        try {
            //using our scanner class to read input from file

            Scanner sc = new Scanner(new File("vertexNumbers.txt"));

            //I basically just burn the first line because thats not important
            //to the way in which my logic works. I don't need to know the 
            //number of highest vertex to make it work.
            int waste = sc.nextInt();

            //adding the to and from for each edge
            while (sc.hasNext()) {
                holdNodesFrom.add(sc.nextInt());
                holdNodesTo.add(sc.nextInt());
            }

            //since we know that the first thing on it is the total number of
            //vertices we can set that variable equal to it
            //System.out.println(holdNodesFrom.size());
            int totalVertex = holdNodesFrom.size() - 1;

            //making a new graph object equal to totalVertex (total number of edges basically)
            Graph graph = new Graph(totalVertex);

            //Scanner sc = new Scanner(new File("vertexNumbers.txt"));
            //reading each new line off of input file and using add edge
            //method to add them to our graph object
            //int waste = sc.nextInt();
            //System.out.println("fsdkvnmdfklvm" + waste);
            for (int i = 0; i <= totalVertex; i++) {

                //This commented line I was just making sure I got each node correctly
                //System.out.println(holdNodesFrom.get(i) + " " + holdNodesTo.get(i));
                //adding each node to the graph that I made above
                addEdge(graph, holdNodesFrom.get(i), holdNodesTo.get(i));
            }

            for (int i = 1; i <= waste; i++) {
                if (graph.adjacencyListArrayLL[i].size() != 0) {
                    holdRank++;
                }

            }
            rank = rank / holdRank;

            for (int i = 0; i <= totalVertex - 1; i++) {
                pageRankMap.put(i, rank);
            }

            //can uncomment this to make sure intial rank is correct in each slot
            //System.out.println(pageRankMap);
            //calling printgraph method where all of our logic is actually done
            //i just run my iterations here so you can watch it happen, can be 
            //easilly placed elseware but that way its checkable with ease. =1
            for (int j = 0; j <= ITERATIONS; j++) {
                printGraph(graph, totalVertex, pageRankMap);
            }

        } catch (FileNotFoundException e) {
        }

    }
}
