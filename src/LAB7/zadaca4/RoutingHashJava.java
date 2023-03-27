package LAB7.zadaca4;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;





class OBHT<K extends Comparable<K>,E> {

    // An object of class OBHT is an open-bucket hash table, containing entries
    // of class MapEntry.
    public MapEntry<K,E>[] buckets;

    // buckets[b] is null if bucket b has never been occupied.
    // buckets[b] is former if bucket b is formerly-occupied
    // by an entry that has since been deleted (and not yet replaced).

    static final int NONE = -1; // ... distinct from any bucket index.

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private static final MapEntry former = new MapEntry(null, null);
    // This guarantees that, for any genuine entry e,
    // e.key.equals(former.key) returns false.

    private int occupancy = 0;
    // ... number of occupied or formerly-occupied buckets in this OBHT.

    @SuppressWarnings("unchecked")
    public OBHT (int m) {
        // Construct an empty OBHT with m buckets.
        buckets = (MapEntry<K,E>[]) new MapEntry[m];
    }


    private int hash (K key) {
        // Translate key to an index of the array buckets.
        return Math.abs(key.hashCode()) % buckets.length;
    }


    public int search (K targetKey) {
        // Find which if any bucket of this OBHT is occupied by an entry whose key
        // is equal to targetKey. Return the index of that bucket.
        int b = hash(targetKey); int n_search=0;
        for (;;) {
            MapEntry<K,E> oldEntry = buckets[b];
            if (oldEntry == null)
                return NONE;
            else if (targetKey.equals(oldEntry.key))
                return b;
            else
            {
                b = (b + 1) % buckets.length;
                n_search++;
                if(n_search==buckets.length)
                    return NONE;

            }
        }
    }


    public void insert (K key, E val) {
        // Insert the entry <key, val> into this OBHT.
        MapEntry<K,E> newEntry = new MapEntry<K,E>(key, val);
        int b = hash(key); int n_search=0;
        for (;;) {
            MapEntry<K,E> oldEntry = buckets[b];
            if (oldEntry == null) {
                if (++occupancy == buckets.length) {
                    System.out.println("Hash tabelata e polna!!!");
                }
                buckets[b] = newEntry;
                return;
            }
            else if (oldEntry == former
                    || key.equals(oldEntry.key)) {
                buckets[b] = newEntry;
                return;
            }
            else
            {
                b = (b + 1) % buckets.length;
                n_search++;
                if(n_search==buckets.length)
                    return;

            }
        }
    }


    @SuppressWarnings("unchecked")
    public void delete (K key) {
        // Delete the entry (if any) whose key is equal to key from this OBHT.
        int b = hash(key); int n_search=0;
        for (;;) {
            MapEntry<K,E> oldEntry = buckets[b];

            if (oldEntry == null)
                return;
            else if (key.equals(oldEntry.key)) {
                buckets[b] = former;//(MapEntry<K,E>)former;
                return;
            }
            else{
                b = (b + 1) % buckets.length;
                n_search++;
                if(n_search==buckets.length)
                    return;

            }
        }
    }


    public String toString () {
        String temp = "";
        for (int i = 0; i < buckets.length; i++) {
            temp += i + ":";
            if (buckets[i] == null)
                temp += "\n";
            else if (buckets[i] == former)
                temp += "former\n";
            else
                temp += buckets[i] + "\n";
        }
        return temp;
    }


    public OBHT<K,E> clone () {
        OBHT<K,E> copy = new OBHT<K,E>(buckets.length);
        for (int i = 0; i < buckets.length; i++) {
            MapEntry<K,E> e = buckets[i];
            if (e != null && e != former)
                copy.buckets[i] = new MapEntry<K,E>(e.key, e.value);
            else
                copy.buckets[i] = e;
        }
        return copy;
    }
}

    class MapEntry<K extends Comparable<K>,E> implements Comparable<K> {

        // Each MapEntry object is a pair consisting of a key (a Comparable
        // object) and a value (an arbitrary object).
        public K key;
        public E value;

        public MapEntry (K key, E val) {
            this.key = key;
            this.value = val;
        }

        public int compareTo (K that) {
            // Compare this map entry to that map entry.
            @SuppressWarnings("unchecked")
            MapEntry<K,E> other = (MapEntry<K,E>) that;
            return this.key.compareTo(other.key);
        }

        public String toString () {
            return "<" + key + "," + value + ">";
        }
    }


class RoutingHashJava {

    public static void main(String[] args) throws IOException {
        //ocekuva nekoj dr
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(br.readLine());
        int kofi = najdiProst(n);
        OBHT<String, List<String>> ruteri = new OBHT<>(kofi+1);

        //INPUT
        for(int i=0; i<=n; i+=2){
            String kluc = br.readLine();
            String vrednost = br.readLine();

            //koga delime dava niza od stringovi
            String[] IP = vrednost.split(",");

            List<String> vrednosti = new ArrayList<>(Arrays.asList(IP));
            /*for(int j=0; j<IP.length; j++){
                vrednosti.add(IP[j]);
            }*/
            ruteri.insert(kluc, vrednosti);
        }

        int vlez =Integer.parseInt(br.readLine());

        //nemora while znaeme kolku pati ke vrtime
        for(int i=0; i<vlez*2; i++){
            //kluc pa vrednost stavame
            String Tablekluc = br.readLine();
            String Tablevrednost = br.readLine();
            int  baraj = ruteri.search(Tablekluc);

            String filteredValue = filterValue(Tablevrednost);
            if (baraj != -1) {
                boolean found = false;
                List<String> values = ruteri.buckets[baraj].value;
                for (String value : values) {
                    String filtered = filterValue(value);
                    if (filtered.equals(filteredValue)) {
                        found = true;
                    }
                }

                if (found) {
                    System.out.println("postoi");
                }
                else {
                    System.out.println("ne postoi");
                }
            } else {
                System.out.println("ne postoi");
            }
        }
    }

    private static String filterValue(String inputValue) {
        StringBuilder result = new StringBuilder();
        int counter = 0;
        for (char c : inputValue.toCharArray()) {
            if (c == '.') {
                counter++;
            }
            result.append(c);
            if (counter == 3) {
                break;
            }
        }
        return result.toString();
         }



    public static int najdiProst(int n){
        for(int i=n; ; i++){
            if(daliProst(i)){
                return i;
            }
        }
    }

    public static boolean daliProst(int n){
        if(n==1){
            return true;
        }
        if(n==2){
            return false;
        }
        return IntStream.range(2, n-1).allMatch(i -> n % i != 0);//mesto for
    }
}
