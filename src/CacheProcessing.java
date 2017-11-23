import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class CacheProcessing {
    private static ArrayList<ArrayList<String>> readCache_Individuals(String cacheAddr) {

        //reading from cache to data structure

        ObjectInputStream ois = null;
        ArrayList<ArrayList<String>> individualsCache = new ArrayList<>();

        try {
            System.out.print("\n reading from  cache...");
            ois = new ObjectInputStream(new FileInputStream(cacheAddr));
            individualsCache = (ArrayList<ArrayList<String>>) ois.readObject();
            ois.close();
        } catch (Exception e) {
            System.out.print("\n cannot read from  cache...");
            individualsCache = new ArrayList<>();
        }
        return  individualsCache;
    }
    private static void writeCache_Individuals(HashSet<ArrayList<String>> result, String cacheAddr) throws Exception {
        try {
            System.out.print("\n writing in individual cache...");
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(cacheAddr));
            oos.writeObject(result);
            oos.close();
        } catch(Exception e){
            System.out.print("\n cannot write in individual cache/ may be path directory is not found...");
        }
    }
    private static HashSet<ArrayList<String>> manual_Resoner(ArrayList<ArrayList<String>> matched_pairs){
        HashSet<ArrayList<String>> all_possible_matched =  new HashSet<ArrayList<String>>();

        for (int i = 0; i < matched_pairs.size() ; i++) {
            ArrayList<String> current_pairs = matched_pairs.get(i);
            String f_pair = current_pairs.get(0).toLowerCase();
            String s_pair = current_pairs.get(1).toLowerCase();

            for (int j = 0; j < i+1 ; j++) {
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
                }
                //2 out of 5 condition
                if (c2_pair.equalsIgnoreCase(f_pair) && !c1_pair.equalsIgnoreCase(s_pair)) {
                    ArrayList transitiv_pair = new ArrayList<>();
                    transitiv_pair.add(c1_pair);
                    transitiv_pair.add(s_pair);

                    all_possible_matched.add(current_pairs);
                    all_possible_matched.add(candidate_pirs);
                    all_possible_matched.add(transitiv_pair);
                }

                //3 out of 5 condition
                if (c1_pair.equalsIgnoreCase(s_pair) && !c2_pair.equalsIgnoreCase(f_pair)) {
                    ArrayList transitiv_pair = new ArrayList<>();
                    transitiv_pair.add(c2_pair);
                    transitiv_pair.add(f_pair);

                    all_possible_matched.add(current_pairs);
                    all_possible_matched.add(candidate_pirs);
                    all_possible_matched.add(transitiv_pair);
                }

                //4 out of 5 condition
                if (c2_pair.equalsIgnoreCase(s_pair) && !c1_pair.equalsIgnoreCase(f_pair)) {
                    ArrayList transitiv_pair = new ArrayList<>();
                    transitiv_pair.add(c1_pair);
                    transitiv_pair.add(f_pair);

                    all_possible_matched.add(current_pairs);
                    all_possible_matched.add(candidate_pirs);
                    all_possible_matched.add(transitiv_pair);
                } else {
                    //no transitive equality will be generated
                    all_possible_matched.add(current_pairs);
                    all_possible_matched.add(candidate_pirs);
                }
            }
        }
        return all_possible_matched;
    }
    public static void main(String[] args) throws Exception {

       // defining the type we want to process
        String fileAdd = "./Cache/Person_sameAs_Individuals.dat";

        //read the file to arraylist
        ArrayList<ArrayList<String>> readCache_Individuals = new ArrayList<>();
        readCache_Individuals = readCache_Individuals(fileAdd);

        System.out.println("\nbefore reseanor the size is: " + readCache_Individuals.size());

//        for(ArrayList al : readCache_Individuals){
//            System.out.println(al);
//        }

        //running reasoner for infering more pairs (transitive equality)
        HashSet<ArrayList<String>> reasoner_pairs = new HashSet<>(manual_Resoner(readCache_Individuals));
        System.out.println("after reseanor the size is:" + reasoner_pairs.size());

        //writing in the cache
        writeCache_Individuals(reasoner_pairs, "./Cache/Person_sameAs_Individuals(afterreasoning).dat");


    }
}
