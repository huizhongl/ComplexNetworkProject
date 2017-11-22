/*
 * Airports Information Retrivial
 */
package complexnetwork1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Keyvan
 */
public class ComplexNetwork1 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        // TODO code application logic here
        String fileName1 = new String(args[0]);
        String fileName2 = new String(args[1]);
        String line = null;
        FileReader fileReader;
        String[] columnDetail1;
        String[] columnDetail2;
        Path p;
        Airport airport;
        ArrayList<Airport> airports = new ArrayList<Airport>();
        ArrayList<Path> paths = new ArrayList<Path>();
        ArrayList<Path> fpaths = new ArrayList<Path>();
        //Find Airports
        try {
            fileReader = new FileReader(new File(fileName1));
            BufferedReader br = new BufferedReader(fileReader);
            // if no more lines the readLine() returns null
            while ((line = br.readLine()) != null) {
                // reading lines until the end of the file
                columnDetail1 = line.split(",");
                if(columnDetail1[3].contains("Canada")||columnDetail1[3].contains("United States")){
                    airport=new Airport();
                    airport.setcName(columnDetail1[5].substring(2,5));
                    airport.setCountry(columnDetail1[3]);
                    airports.add(airport);
                }                
            }
        } catch (FileNotFoundException ex) {
            System.out.println("I can not find the file");
            Logger.getLogger(ComplexNetwork1.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            System.out.println("It is an IO Exception");
            Logger.getLogger(ComplexNetwork1.class.getName()).log(Level.SEVERE, null, ex);
        }
        for(Airport a:airports){
        System.out.println(a.getCountry()+a.getcName());
        }
        //Find Paths
        try {
            fileReader = new FileReader(new File(fileName2));
            BufferedReader br = new BufferedReader(fileReader);
            // if no more lines the readLine() returns null
            while ((line = br.readLine()) != null) {
                // reading lines until the end of the file
                columnDetail2 = line.split(",");                
                if(check(airports,columnDetail2[2])&&check(airports,columnDetail2[2])){
                    p=new Path();
                    p.setSource(columnDetail2[2]);
                    p.setDestination(columnDetail2[4]);
                    paths.add(p);
                }                
            }
        } catch (FileNotFoundException ex) {
            System.out.println("I can not find the file");
            Logger.getLogger(ComplexNetwork1.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            System.out.println("It is an IO Exception");
            Logger.getLogger(ComplexNetwork1.class.getName()).log(Level.SEVERE, null, ex);
        }
        for(Path pa:paths){
            System.out.println(pa.getSource()+" to "+pa.getDestination());
        }
        //make new Standard Name for source and Destination Airports in Graph
        for(Path pa:paths){
           for(Airport a:airports){
               if(a.getcName().equals(pa.getSource())){
                   if (a.getCountry().contains("United States")){
                       pa.setSource("US"+pa.getSource());
                   }if (a.getCountry().contains("Canada")){
                       pa.setSource("CA"+pa.getSource());
                   }
               }
               else if(a.getcName().equals(pa.getDestination())){
                   if (a.getCountry().contains("United States")){
                       pa.setDestination("US"+pa.getDestination());
                   }if (a.getCountry().contains("Canada")){
                       pa.setDestination("CA"+pa.getDestination());
                   }
               }
           }
        }
        for(Path pa:paths){
            if ((pa.getSource().length()==5)&&(pa.getDestination().length()==5))
                fpaths.add(pa);
        }
        System.out.println("---------------------------");
        for(Path pa:fpaths){
            System.out.println(pa.getSource()+" ******* "+pa.getDestination());
        }
        //write on the File
        FileWriter writer = new FileWriter("output.txt"); 
        for(Path pa: fpaths) {
            writer.write(pa.getSource()+"\t"+pa.getDestination()+"\n");
            }
writer.close();
    }
    public static boolean check(ArrayList<Airport> airports,String ch){
        boolean test=false;
        for(Airport a:airports){
        if(a.getcName().equals(ch)) 
            test=true;
        }
        return test;
    }
}
