import java.util.ArrayList;

public class tets {

    public static void main(String[] args) throws Exception {
        ArrayList<Integer> a1 = new ArrayList<>();
        a1.add(1);
        a1.add(2);
        a1.add(3);
        a1.add(4);
        ArrayList<Integer> a2 = new ArrayList<>();
        a2.add(11);
        a2.add(22);
        a2.add(1);
        a2.add(1);
        a2.add(3);
        a2.add(4);
        ArrayList<String> a3 = new ArrayList<>();
        a3.add("a");
        a3.add("b");
        a3.add("c");

        for(String s: a3){
            System.out.println("String S: " + s + " _______________________"  );
            for (Integer ii: a1){
                System.out.println("ii:  " + ii);
                for(Integer jj: a2){
                    System.out.println("jj: " + jj);
                    if(ii == jj){
                        System.out.println("equal:" + ii + " " + jj);
                        break;
                    }
                    else{
                        System.out.println("Not equal:" + ii + " " + jj);
                    }
                }
            }
        }






    }


}
