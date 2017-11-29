import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class CacheProcessing {
    private static ArrayList<ArrayList<String>> readCache_Individuals(String cacheAddr) {

        //reading from cache to data structure

        ObjectInputStream ois = null;
        ArrayList<ArrayList<String>> individualsCache = new ArrayList<>();

        try {
            System.out.print("\nreading from  cache...");
            ois = new ObjectInputStream(new FileInputStream(cacheAddr));
            individualsCache = (ArrayList<ArrayList<String>>) ois.readObject();
            ois.close();
        } catch (Exception e) {
            System.out.print("\ncannot read from  cache...");
            individualsCache = new ArrayList<>();
        }
        return  individualsCache;
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
    private static HashSet<ArrayList<String>> manual_Reasoner(ArrayList<ArrayList<String>> matched_pairs){
        HashSet<ArrayList<String>> all_possible_matched =  new HashSet<>();
        System.out.println("\nRunning Reasoner...");

        // for each pair of current matches
        for (int i = 0; i < matched_pairs.size() ; i++) {
            ArrayList<String> current_pairs = matched_pairs.get(i);
            String f_pair = current_pairs.get(0).toLowerCase();
            String s_pair = current_pairs.get(1).toLowerCase();

            // compare it with all pair after the current pair, one by one
            for (int j = i+1; j < matched_pairs.size() ; j++) {
                ArrayList<String> candidate_pirs = matched_pairs.get(j);
                String c1_pair = candidate_pirs.get(0).toLowerCase();
                String c2_pair = candidate_pirs.get(1).toLowerCase();


                //1 out of 5 condition
                if (c1_pair.equalsIgnoreCase(f_pair) && !c2_pair.equalsIgnoreCase(s_pair)) {
                    ArrayList transitiv_pair = new ArrayList<>();
                    transitiv_pair.add(c2_pair);
                    transitiv_pair.add(s_pair);

                    all_possible_matched.add(current_pairs);
                    all_possible_matched.add(candidate_pirs);
                    all_possible_matched.add(transitiv_pair);
//                    System.out.println("1 | for pairs: " + f_pair+ " and  " + s_pair);
//                    System.out.println("2 | for pairs: " + c1_pair+ " and  " + c2_pair);
//
//                    System.out.println("@ 1 new pair is: "+ transitiv_pair);
                    continue;

                }
                //2 out of 5 condition
                if (c2_pair.equalsIgnoreCase(f_pair) && !c1_pair.equalsIgnoreCase(s_pair)) {
                    ArrayList transitiv_pair = new ArrayList<>();
                    transitiv_pair.add(c1_pair);
                    transitiv_pair.add(s_pair);

                    all_possible_matched.add(current_pairs);
                    all_possible_matched.add(candidate_pirs);
                    all_possible_matched.add(transitiv_pair);
//                    System.out.println("1 | for pairs: " + f_pair+ " and  " + s_pair);
//                    System.out.println("2 | for pairs: " + c1_pair+ " and  " + c2_pair);
//
//                    System.out.println("@ 2 new pair is: "+ transitiv_pair);
                    continue;

                }

                //3 out of 5 condition
                if (c1_pair.equalsIgnoreCase(s_pair) && !c2_pair.equalsIgnoreCase(f_pair)) {
                    ArrayList transitiv_pair = new ArrayList<>();
                    transitiv_pair.add(c2_pair);
                    transitiv_pair.add(f_pair);

                    all_possible_matched.add(current_pairs);
                    all_possible_matched.add(candidate_pirs);
                    all_possible_matched.add(transitiv_pair);
//                    System.out.println("1 | for pairs: " + f_pair+ " and  " + s_pair);
//                    System.out.println("2 | for pairs: " + c1_pair+ " and  " + c2_pair);
//
//                    System.out.println("@ 3 new pair is: "+ transitiv_pair);
                    continue;

                }

                //4 out of 5 condition
                if (c2_pair.equalsIgnoreCase(s_pair) && !c1_pair.equalsIgnoreCase(f_pair)) {
                    ArrayList transitiv_pair = new ArrayList<>();
                    transitiv_pair.add(c1_pair);
                    transitiv_pair.add(f_pair);

                    all_possible_matched.add(current_pairs);
                    all_possible_matched.add(candidate_pirs);
                    all_possible_matched.add(transitiv_pair);
//                    System.out.println("1 | for pairs: " + f_pair+ " and  " + s_pair);
//                    System.out.println("2 | for pairs: " + c1_pair+ " and  " + c2_pair);
//
//                    System.out.println("@ 4 new pair is: "+ transitiv_pair);
                    continue;

                } else {
                    //no transitive equality will be generated
                    all_possible_matched.add(current_pairs);
                    all_possible_matched.add(candidate_pirs);
                    continue;
                }
            }
        }
        return all_possible_matched;
    }
    private static ArrayList<ArrayList<String>> removing_duplication(ArrayList<ArrayList<String>> matched_pairs){
        ArrayList<ArrayList<String>> all_unique_matched =  new ArrayList<ArrayList<String>>();
        Set<ArrayList<String>> deduplicated = new HashSet<>();

        System.out.println("initial array size before deduplication: " + matched_pairs.size());
        for (int i = 0; i < matched_pairs.size() ; i++) {
            ArrayList<String> current_pairs = matched_pairs.get(i);
            String f_pair = current_pairs.get(0).toLowerCase();
            String s_pair = current_pairs.get(1).toLowerCase();
            for (int j = i+1; j < matched_pairs.size() ; j++) {
                ArrayList<String> candidate_pirs = matched_pairs.get(j);
                String c1_pair = candidate_pirs.get(0).toLowerCase();
                String c2_pair = candidate_pirs.get(1).toLowerCase();

                if (c1_pair.equalsIgnoreCase(f_pair) && c2_pair.equalsIgnoreCase(s_pair)) {
                    deduplicated.add(current_pairs);
                    continue; //compare with next pair
                }
                if (c1_pair.equalsIgnoreCase(s_pair) && c2_pair.equalsIgnoreCase(f_pair)) {
                    deduplicated.add(current_pairs);
                    continue; //compare with next pair
                }
                else{ // if not equal
                    deduplicated.add(candidate_pirs);
                    deduplicated.add(current_pairs);
                    continue; //compare with next pair
                }
            }
        }
        all_unique_matched.addAll(deduplicated);
        System.out.println("Size of duplicate free list is :" + all_unique_matched.size());
        return all_unique_matched;
    }

    public static void main(String[] args) throws Exception {

       // defining the type we want to process
//        String fileAdd = "./Cache/Person_sameAs_Individuals.dat";
        File[] files = new File("./Cache/").listFiles();
        ArrayList<String> file_name_list =  new ArrayList<>();
        for (File file : files) {
            System.out.println("files to process: ");
            if (file.isFile()) {
                if (!file.getName().contains("DS_Store")){
                 file_name_list.add(file.getPath());
                    System.out.println(file.getPath());
                }
            }
        }



        for(String file : file_name_list){

            // get the files name and build the final output file name
            System.out.println(file);
            String specific_part = file.split("/")[2].split("\\.")[0];
            String outFileAddr = "./FiinalCachedFiles/final_" + specific_part + ".dat" ;
            System.out.println("processing file: "+ outFileAddr);


            //read the file to arraylist
            ArrayList<ArrayList<String>> readCache_Individuals = new ArrayList<>();
            readCache_Individuals = readCache_Individuals(file);

            // printing the cache content
    //        for(ArrayList al: readCache_Individuals){
    //            System.out.println(al +  "\n");
    //        }


            //running reasoner for infering more pairs (transitive equality)
            HashSet<ArrayList<String>> reasoner_pairs = new HashSet<>(manual_Reasoner(readCache_Individuals));
            ArrayList<ArrayList<String>> filter_by_reasoner = new ArrayList<>();
            filter_by_reasoner.addAll(reasoner_pairs);

            //removing duplication from list
            ArrayList<ArrayList<String>> deduplicated_pairs = new ArrayList<>();
            deduplicated_pairs = removing_duplication(filter_by_reasoner);
//            for(ArrayList al: deduplicated_pairs){
//                System.out.println(al +  "\n");
//            }

            //writing in the cache
            writeCache_Individuals(deduplicated_pairs, outFileAddr);
            System.out.println("____________________________________________________________________");


        }
    }
}
