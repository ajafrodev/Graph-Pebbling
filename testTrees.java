import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

class testTrees {

    public static boolean testHeight(String inputfile, String solutionfile) {
        Scanner solnin=null;
        try {
            solnin=new Scanner(new File(solutionfile));
        }
        catch (FileNotFoundException e){
            System.out.println("Testcase file not found: "+inputfile);
            System.out.println("Setup vocareum properly");
            e.printStackTrace();
            return false;
        }


        try {
            Graph g = new Graph(inputfile);
            // assume the root is vertex 0
            int height = solnin.nextInt();
            int studentHeight = g.height(0);
            if (height != studentHeight) {
                return false;
            }
            height = solnin.nextInt();
            studentHeight = g.height(1);
            if (height != studentHeight) {
                return false;
            }
            height = solnin.nextInt();
            studentHeight = g.height(4);
            if (height != studentHeight) {
                return false;
            }
            height = solnin.nextInt();
            studentHeight = g.height(7);
            if (height != studentHeight) {
                return false;
            }
            height = solnin.nextInt();
            studentHeight = g.height(10);
            if (height != studentHeight) {
                return false;
            }
        }
        catch (Exception e){
            e.printStackTrace();
            return false;
        }



        return true;
    }

    public static double testPebbling(String inputfile, String solutionfile){

        //Get student solution
        ArrayList<Integer> pebbled=null;
        try {
            Graph g = new Graph(inputfile);
            pebbled = g.pebbling(0);
        }
        catch (Exception e){
            e.printStackTrace();
            return 0;
        }

        if(pebbled==null){
            System.out.println("Pebbled list cannot be null");
            return 0;
        }

        ///load input data
        Scanner in=null;
        Scanner solnin=null;
        try {
            in=new Scanner(new File(inputfile));
            solnin=new Scanner(new File(solutionfile));
        }
        catch (FileNotFoundException e){
            System.out.println("Testcase file not found: "+inputfile);
            System.out.println("Setup vocareum properly");
            e.printStackTrace();
            return 0;
        }

        int n=in.nextInt();
        int [][]G_adj=new int[n][n];

        for (int i=0;i<n;i++) {
            for (int j=0;j<n;j++) {
                int u=in.nextInt();
                G_adj[i][j]=u;
            }
        }

        double[] profit=new double[n];
        for(int i=0;i<n;i++) {
            profit[i]=in.nextDouble();
        }

        ///load solution
        int height = solnin.nextInt();
        height = solnin.nextInt();
        height = solnin.nextInt();
        height = solnin.nextInt();
        height = solnin.nextInt();

        double maxProfit=solnin.nextDouble();

        ArrayList<Integer> solutions=new ArrayList<Integer>();

        while(solnin.hasNextInt()){
            solutions.add(solnin.nextInt());
        }


        ///verify neighborhood
        try {
            boolean flag=true;
            for(int i=0;i<pebbled.size();i++){
                for(int j=0;j<pebbled.size();j++){
                    if(G_adj[pebbled.get(i)][pebbled.get(j)]==1||G_adj[pebbled.get(j)][pebbled.get(i)]==1){
                        System.out.println("pebbled vertex ("+pebbled.get(i)+","+pebbled.get(j)+") are neighbors");
                        flag=false;
                        break;
                    }
                }
                if(flag==false){
                    break;
                }
            }
            if(flag==false){
                return 0;
            }
        }
        catch (Exception e){
            e.printStackTrace();
            return 0;
        }

        ///verify maxprofit
        double studentProfit=0;
        try {
            for (int i = 0; i < pebbled.size(); i++) {
                studentProfit+=profit[pebbled.get(i)];
            }
        }
        catch (Exception e){
            e.printStackTrace();
            return 0;
        }

        if(Math.abs(maxProfit-studentProfit)<0.001){
            System.out.println("Passed");
            return 10.0;
        }
        else{
            System.out.println("Failed: expected "+maxProfit+" got "+studentProfit);

            System.out.println("One possible solution is: ");
            for(int i=0;i<solutions.size();i++){
                System.out.print(solutions.get(i)+" ");
            }
            System.out.println();

            return 0;
        }
    }

    public static void main(String[] args) {
        double score=0;
        int testcases=5;

        for(int i=1;i<=testcases;i++){
            System.out.println("Running task 2 test case "+i);
            if (testHeight("task2input"+i+".txt", "task2input"+i+"soln.txt")) {
                double s=testPebbling("task2input"+i+".txt", "task2input"+i+"soln.txt");
                System.out.println("score: "+s+"/10.0");
                System.out.println();
                score+=s;
            } else {
                System.out.println("height is not correct");
                System.out.println();
                System.out.println("Total score: "+score);
                return;
            }

        }

        System.out.println("Total score: "+score);

    }
}