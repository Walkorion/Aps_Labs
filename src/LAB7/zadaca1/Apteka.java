package LAB7.zadaca1;

import java.util.Scanner;
import java.util.stream.IntStream;

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
class OBHT<K extends Comparable<K>,E> {

    // An object of class OBHT is an open-bucket hash table, containing entries
    // of class MapEntry.
    public MapEntry<K, E>[] buckets;

    // buckets[b] is null if bucket b has never been occupied.
    // buckets[b] is former if bucket b is formerly-occupied
    // by an entry that has since been deleted (and not yet replaced).

    static final int NONE = -1; // ... distinct from any bucket index.

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static final MapEntry former = new MapEntry(null, null);
    // This guarantees that, for any genuine entry e,
    // e.key.equals(former.key) returns false.

    private int occupancy = 0;
    // ... number of occupied or formerly-occupied buckets in this OBHT.

    @SuppressWarnings("unchecked")
    public OBHT(int m) {
        // Construct an empty OBHT with m buckets.
        buckets = (MapEntry<K, E>[]) new MapEntry[m];
    }


    private int hash(K key) {
        // Translate key to an index of the array buckets.
        return Math.abs(key.hashCode()) % buckets.length;
    }


    public int search(K targetKey) {
        // Find which if any bucket of this OBHT is occupied by an entry whose key
        // is equal to targetKey. Return the index of that bucket.
        int b = hash(targetKey);
        int n_search = 0;
        for (; ; ) {
            MapEntry<K, E> oldEntry = buckets[b];
            if (oldEntry == null)
                return NONE;
            else if (targetKey.equals(oldEntry.key))
                return b;
            else {
                b = (b + 1) % buckets.length;
                n_search++;
                if (n_search == buckets.length)
                    return NONE;

            }
        }
    }


    public void insert(K key, E val) {
        // Insert the entry <key, val> into this OBHT.
        MapEntry<K, E> newEntry = new MapEntry<K, E>(key, val);
        int b = hash(key);
        int n_search = 0;
        for (; ; ) {
            MapEntry<K, E> oldEntry = buckets[b];
            if (oldEntry == null) {
                if (++occupancy == buckets.length) {
                    System.out.println("Hash tabelata e polna!!!");
                }
                buckets[b] = newEntry;
                return;
            } else if (oldEntry == former
                    || key.equals(oldEntry.key)) {
                buckets[b] = newEntry;
                return;
            } else {
                b = (b + 1) % buckets.length;
                n_search++;
                if (n_search == buckets.length)
                    return;

            }
        }
    }


    @SuppressWarnings("unchecked")
    public void delete(K key) {
        // Delete the entry (if any) whose key is equal to key from this OBHT.
        int b = hash(key);
        int n_search = 0;
        for (; ; ) {
            MapEntry<K, E> oldEntry = buckets[b];

            if (oldEntry == null)
                return;
            else if (key.equals(oldEntry.key)) {
                buckets[b] = former;//(MapEntry<K,E>)former;
                return;
            } else {
                b = (b + 1) % buckets.length;
                n_search++;
                if (n_search == buckets.length)
                    return;

            }
        }
    }


    public String toString() {
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


    public OBHT<K, E> clone() {
        OBHT<K, E> copy = new OBHT<K, E>(buckets.length);
        for (int i = 0; i < buckets.length; i++) {
            MapEntry<K, E> e = buckets[i];
            if (e != null && e != former)
                copy.buckets[i] = new MapEntry<K, E>(e.key, e.value);
            else
                copy.buckets[i] = e;
        }
        return copy;
    }
}




//value na key za heshot (nemoze 3 vrednosti pa zatoa edna klasa)
class Lek {
    int daliEpozitiven;
    int cena;
    int kolicina;

    public Lek(int daliEpozitiven, int cena, int kolicina) {
        this.daliEpozitiven = daliEpozitiven;
        this.cena = cena;
        this.kolicina = kolicina;
    }

    public void setKolicina(int kolicina) {
        this.kolicina = kolicina;
        //kolicina = kolicina - kolku pobaral
    }

    @Override
    public String toString() {
        String daliPoz;
        if (daliEpozitiven == 1) {
            daliPoz = "POZ";
        } else {
            daliPoz = "NEG";
        }
        return String.format("%s\n%d\n%d", daliPoz, cena, kolicina);
    }
}


 class Apteka {

     public static void main(String[] args) {
         Scanner scanner = new Scanner(System.in);

         int n = Integer.parseInt(scanner.nextLine());
         int kofi = najdiProst(n);

         //kluc ni e string, a value ni e lekot -> (klasa za da bide eden)
         OBHT<String, Lek> apteka = new OBHT<>(kofi+1); //ako se zali

         //VLEZOT:
         for(int i=0; i<n; i++){
             String linija = scanner.nextLine();
             String[] del = linija.split("\\s+");

             //HYDROCYKLIN 0 55 10
             // (kluc)HYDROCYKLIN (daliPoz)0  (cena)55  (kolicina)10
             String kluc = del[0].toUpperCase();
             //LEKOT
             int daliPozitien = Integer.parseInt(del[1]);
             int cena = Integer.parseInt(del[2]);
             int kolicina = Integer.parseInt(del[3]);

             Lek lekovi = new Lek(daliPozitien, cena, kolicina);

             //vnesueme aptekata
             apteka.insert(kluc,lekovi);

         }

         //baranjeto
         while(true){
             String lek = scanner.nextLine().toUpperCase();
             //koa ke stasa do kraj so citanje
             if(lek.equals("KRAJ")){
                 break;
             }
             int kolicina = Integer.parseInt(scanner.nextLine());



             lek = lek.toUpperCase(); //TAKA GO BARAAT

             //go barame lekot vo aptekata
             int index = apteka.search(lek);
             if(index != -1){

                 System.out.println(lek);
                 System.out.println(apteka.buckets[index].value);

                 int kolicinaApteka =  apteka.buckets[index].value.kolicina;
                 if(kolicina > kolicinaApteka){
                     System.out.println("Nema dovolno lekovi");
                 }else{
                     //namaluvame kolicinata
                     apteka.buckets[index].value.setKolicina( apteka.buckets[index].value.kolicina - kolicina);
                     System.out.println("Napravena naracka");
                 }


             }else{
                 System.out.println("Nema takov lek");
             }
         }

         scanner.close();
    }

    //za odreduvanje na validen broj na koficki
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
