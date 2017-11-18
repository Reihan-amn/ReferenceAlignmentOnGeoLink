/*
Author: Reihaneh Amini
Nov 18, 2017
This code extract all sameAs and CloseMatches entity pairs from Geolink endpoint
 */

import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.sparql.engine.http.QueryEngineHTTP;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.logging.Logger;

public class ReferenceExctraction implements Serializable {

    static HashSet<String> cruiseProperties;
    static String activeQuery; // used for debugging
    static String sparqlEndpoint = "http://data.geolink.org/sparql";

    //defining general data structures

    private static ResultSet query(String sparqlQuery) {
        activeQuery = sparqlQuery;
        Logger.getGlobal().severe(activeQuery);
        QueryEngineHTTP httpQuery = new QueryEngineHTTP(sparqlEndpoint, sparqlQuery);
        return httpQuery.execSelect();
    }

    public static void getIndividuals_sameAs_pair(String type) throws Exception {
        /*
        * this function queries for all the individuals of referred type to extract the pairs connected with sameAs
        */


        // defining the name of the cache file
        String[] splitType = type.split("#");
        String cacheAddr = "Cache/" +splitType[1] + "_sameAs_Individuals.dat";

        // load what is already in the cache into ArrayList<String> individualCache
        ArrayList<ArrayList<String>> result =  new ArrayList<>();
        result = readCache_Individuals(cacheAddr);



        try{
            String sparqlQuery = "" + "SELECT ?indiv1 ?indiv2  \n"
                + "WHERE { \n"
                + "?indiv1 ?prop ?indiv2. \n"
                + "?indiv1 a <" + type + ">\n"
                + "FILTER(CONTAINS(str(?prop), 'sameAs')).\n"
                + "FILTER(?indiv1 != ?indiv2).\n"
                + "}";
               // + "} LIMIT 20.";

            ResultSet results = query(sparqlQuery);


            int i = 0;
            while (results.hasNext()) {
                i++;
                QuerySolution qs = results.next();
                String indiv1 = qs.get("indiv1").toString();
                String indiv2 = qs.get("indiv2").toString();

                ArrayList<String> pair =  new ArrayList<>();
                pair.add(indiv1);
                pair.add(indiv2);


                if(result.contains(pair)) continue;
                else {
                    result.add(pair);
                    System.out.println(i + "  " + pair );
                }
             }
        }
        catch (Exception e){
            e.printStackTrace();
            System.err.println("I will try to write out the individual property cache!");
            writeCache_Individuals(result, cacheAddr);
        }
        // loading the cache with what we have in individualCache
        writeCache_Individuals(result, cacheAddr);
    }

    public static void getIndividuals_closeMatch_pair(String type) throws Exception {
        /*
        * this function queries for all the individuals of referred type to extract the pairs connected with closeMatch
        */


        // defining the name of the cache file
        String[] splitType = type.split("#");
        String cacheAddr = "Cache/" +splitType[1] + "_closeMatch_Individuals.dat";

        // load what is already in the cache into ArrayList<String> individualCache
        ArrayList<ArrayList<String>> result =  new ArrayList<>();
        result = readCache_Individuals(cacheAddr);



        try{
            String sparqlQuery = "" + "SELECT ?indiv1 ?indiv2  \n"
                    + "WHERE { \n"
                    + "?indiv1 ?prop ?indiv2. \n"
                    + "?indiv1 a <" + type + ">\n"
                    + "FILTER(CONTAINS(str(?prop), 'closeMatch')).\n"
                    + "FILTER(?indiv1 != ?indiv2).\n"
                    + "}";
            // + "} LIMIT 20.";

            ResultSet results = query(sparqlQuery);


            int i = 0;
            while (results.hasNext()) {
                i++;
                QuerySolution qs = results.next();
                String indiv1 = qs.get("indiv1").toString();
                String indiv2 = qs.get("indiv2").toString();

                ArrayList<String> pair =  new ArrayList<>();
                pair.add(indiv1);
                pair.add(indiv2);


                if(result.contains(pair)) continue;
                else {
                    result.add(pair);
                    System.out.println(i + "  " + pair );
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
            System.err.println("I will try to write out the individual property cache!");
            writeCache_Individuals(result, cacheAddr);
        }
        // loading the cache with what we have in individualCache
        writeCache_Individuals(result, cacheAddr);
    }




    private static void writeCache_Individuals(ArrayList<ArrayList<String>> result, String cacheAddr) throws Exception {
        try {
            System.out.print("\n writing in individual cache...");
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(cacheAddr));
            oos.writeObject(result);
            oos.close();
        } catch(Exception e){
            System.out.print("\n cannot write in individual cache/ may be path directory is not found...");
        }
    }
    private static ArrayList<ArrayList<String>> readCache_Individuals(String cacheAddr) {

        ObjectInputStream ois = null;
        ArrayList<ArrayList<String>> sameAs_individualsCache = new ArrayList<>();

        try {
            System.out.print("\n reading from  cache...");
            ois = new ObjectInputStream(new FileInputStream(cacheAddr));
            sameAs_individualsCache = (ArrayList<ArrayList<String>>) ois.readObject();
            ois.close();
        } catch (Exception e) {
            System.out.print("\n cannot read from  cache...");
            sameAs_individualsCache = new ArrayList<>();
        }
        return  sameAs_individualsCache;
    }

    public static void main(String[] args) throws Exception {

        long startTime = System.currentTimeMillis();

        // entity type that we want to investigate
        ArrayList<String > types =  new ArrayList<>();
        types.add("http://schema.geolink.org/1.0/base/main#Cruise");
        types.add("http://schema.geolink.org/1.0/base/main#Person");
        types.add("http://schema.geolink.org/1.0/base/main#Organization");

        for (String type : types) {
            // querying for the uri of all individuals of this type
            System.out.println("investigating type " + type);
            getIndividuals_sameAs_pair(type);
            System.out.println("Finished extracting the sameAs the pairs for type: " + type );

            getIndividuals_closeMatch_pair(type);
            System.out.println("Finished extracting the closeMatch the pairs for type: " + type );
        }
    }
}
