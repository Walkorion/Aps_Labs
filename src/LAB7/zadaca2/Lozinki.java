package LAB7.zadaca2;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.Buffer;
import java.util.stream.IntStream;

class SLLNode<E> {
    protected E element;
    protected SLLNode<E> succ;

    public SLLNode(E elem, SLLNode<E> succ) {
        this.element = elem;
        this.succ = succ;
    }

    @Override
    public String toString() {
        return element.toString();
    }
}

class SLL<E> {
    private SLLNode<E> first;

    public SLL() {
        // Construct an empty SLL
        this.first = null;
    }

    public void deleteList() {
        first = null;
    }

    public int size() {
        int listSize = 0;
        SLLNode<E> tmp = first;
        while (tmp != null) {
            listSize++;
            tmp = tmp.succ;
        }
        return listSize;
    }

    @Override
    public String toString() {
        String ret = new String();
        if (first != null) {
            SLLNode<E> tmp = first;
            ret += tmp;
            while (tmp.succ != null) {
                tmp = tmp.succ;
                ret += " " + tmp;
            }
        } else
            ret = "Prazna lista!!!";
        return ret;
    }

    public void insertFirst(E o) {
        SLLNode<E> ins = new SLLNode<E>(o, null);
        ins.succ = first;
        //SLLNode<E> ins = new SLLNode<E>(o, first);
        first = ins;
    }

    public void insertAfter(E o, SLLNode<E> node) {
        if (node != null) {
            SLLNode<E> ins = new SLLNode<E>(o, node.succ);
            node.succ = ins;
        } else {
            System.out.println("Dadenot jazol e null");
        }
    }

    public void insertBefore(E o, SLLNode<E> before) {

        if (first != null) {
            SLLNode<E> tmp = first;
            if (first == before) {
                this.insertFirst(o);
                return;
            }
            //ako first!=before
            while (tmp.succ != before && tmp.succ != null)
                tmp = tmp.succ;
            if (tmp.succ == before) {
                tmp.succ = new SLLNode<E>(o, before);
                ;
            } else {
                System.out.println("Elementot ne postoi vo listata");
            }
        } else {
            System.out.println("Listata e prazna");
        }
    }

    public void insertLast(E o) {
        if (first != null) {
            SLLNode<E> tmp = first;
            while (tmp.succ != null)
                tmp = tmp.succ;
            tmp.succ = new SLLNode<E>(o, null);
        } else {
            insertFirst(o);
        }
    }

    public E deleteFirst() {
        if (first != null) {
            SLLNode<E> tmp = first;
            first = first.succ;
            return tmp.element;
        } else {
            System.out.println("Listata e prazna");
            return null;
        }
    }


    public E delete(SLLNode<E> node) {
        if (first != null) {
            SLLNode<E> tmp = first;
            if (first == node) {
                return this.deleteFirst();
            }
            while (tmp.succ != node && tmp.succ.succ != null)
                tmp = tmp.succ;
            if (tmp.succ == node) {
                tmp.succ = tmp.succ.succ;
                return node.element;
            } else {
                System.out.println("Elementot ne postoi vo listata");
                return null;
            }
        } else {
            System.out.println("Listata e prazna");
            return null;
        }

    }
}

    class MapEntry<K extends Comparable<K>, E> implements Comparable<K> {

        // Each MapEntry object is a pair consisting of a key (a Comparable
        // object) and a value (an arbitrary object).
        public K key;
        public E value;

        public MapEntry(K key, E val) {
            this.key = key;
            this.value = val;
        }

        public int compareTo(K that) {
            // Compare this map entry to that map entry.
            @SuppressWarnings("unchecked")
            MapEntry<K, E> other = (MapEntry<K, E>) that;
            return this.key.compareTo(other.key);
        }

        public String toString() {
            return "<" + key + "," + value + ">";
        }
    }

    class CBHT<K extends Comparable<K>, E> {

        // An object of class CBHT is a closed-bucket hash table, containing
        // entries of class MapEntry.
        public SLLNode<MapEntry<K, E>>[] buckets;

        @SuppressWarnings("unchecked")
        public CBHT(int m) {
            // Construct an empty CBHT with m buckets.
            buckets = (SLLNode<MapEntry<K, E>>[]) new SLLNode[m];
        }

        private int hash(K key) {
            // Translate key to an index of the array buckets.
            return Math.abs(key.hashCode()) % buckets.length;
        }

        public SLLNode<MapEntry<K, E>> search(K targetKey) {
            // Find which if any node of this CBHT contains an entry whose key is
            // equal
            // to targetKey. Return a link to that node (or null if there is none).
            int b = hash(targetKey);
            for (SLLNode<MapEntry<K, E>> curr = buckets[b]; curr != null; curr = curr.succ) {
                if (targetKey.equals(((MapEntry<K, E>) curr.element).key))
                    return curr;
            }
            return null;
        }

        public void insert(K key, E val) {        // Insert the entry <key, val> into this CBHT.
            MapEntry<K, E> newEntry = new MapEntry<K, E>(key, val);
            int b = hash(key);
            for (SLLNode<MapEntry<K, E>> curr = buckets[b]; curr != null; curr = curr.succ) {
                if (key.equals(((MapEntry<K, E>) curr.element).key)) {
                    // Make newEntry replace the existing entry ...
                    curr.element = newEntry;
                    return;
                }
            }
            // Insert newEntry at the front of the 1WLL in bucket b ...
            buckets[b] = new SLLNode<MapEntry<K, E>>(newEntry, buckets[b]);
        }

        public void delete(K key) {
            // Delete the entry (if any) whose key is equal to key from this CBHT.
            int b = hash(key);
            for (SLLNode<MapEntry<K, E>> pred = null, curr = buckets[b]; curr != null; pred = curr, curr = curr.succ) {
                if (key.equals(((MapEntry<K, E>) curr.element).key)) {
                    if (pred == null)
                        buckets[b] = curr.succ;
                    else
                        pred.succ = curr.succ;
                    return;
                }
            }
        }

        public String toString() {
            String temp = "";
            for (int i = 0; i < buckets.length; i++) {
                temp += i + ":";
                for (SLLNode<MapEntry<K, E>> curr = buckets[i]; curr != null; curr = curr.succ) {
                    temp += curr.element.toString() + " ";
                }
                temp += "\n";
            }
            return temp;
        }

    }

   class Lozinki{
       public static void main (String[] args) throws IOException {
           BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
           int N = Integer.parseInt(br.readLine());
           int kofi = najdiProst(N);

           CBHT<String, String> pass = new CBHT<>(kofi+1);

           //INPUT
           for(int i=1;i<=N;i++){
               String imelozinka = br.readLine();
               String[] pom = imelozinka.split(" ");
               //polnime hesh table
               pass.insert(pom[0], pom[1]);
           }

           //BARANJE
           while(true){
               //da zavrsime koa ke procita kraj
               String linija = br.readLine();
               if(linija.equals("KRAJ")){
                   break;
               }
               String[] delovi = linija.split("\\s+");
               //mora vakov pristap za klucot
               SLLNode<MapEntry<String, String>> kluc  = pass.search(delovi[0]);

               if(kluc!=null){ //ako go ima klucot
                   if(delovi[0].equals(kluc.element.key) && delovi[1].equals(kluc.element.value)){
                       System.out.println("Najaven");
                       break;  //ako dobieme najaven zapirame
                   }else{
                       System.out.println("Nenajaven");
                   }
               }else{  //ako go nema vo tabelata
                   System.out.println("Nenajaven");
               }

           }

       }

       //proverka za broj na koficki
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

