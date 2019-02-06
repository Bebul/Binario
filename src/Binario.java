import java.util.ArrayList;

interface ZpracujZadani {
    public boolean zpracuj(ArrayList<Integer> kombinace);
}

public class Binario {

    public Binario() {
        init(6, new ArrayList<>());
    }

    public boolean jeToSestka(int[] adept) {
        int pocetJednicek = 0;
        int sekvence = 0;
        int posledni = -1;
        for (int bit : adept) {
            pocetJednicek += bit;
            if (bit == posledni) {
                sekvence++;
                if (sekvence == 3) break;
            } else {
                posledni = bit;
                sekvence = 1;
            }
        }
        return (pocetJednicek == 3 && sekvence != 3);
    }

    // prvni zpusob jak vygenerovat sestky
    ArrayList<int[]> initSestky() {
        ArrayList<int[]> sestky = new ArrayList<int[]>();
        for (int a=0; a<2; a++)
            for (int b=0; b<2; b++)
                for (int c=0; c<2; c++)
                    for (int d=0; d<2; d++)
                        for (int e=0; e<2; e++)
                            for (int f=0; f<2; f++) {
                                int[] sestka = {a, b, c, d, e, f};
                                if (jeToSestka(sestka)){
                                    sestky.add(sestka);
                                }
                            }
        return sestky;
    }
    ArrayList<int[]> sestky = new ArrayList<>();

    // druhy zpusob jak vygenerovat sestky
    void init(int n, ArrayList<Integer> acc) {
        if (n==0) {
            int[] sestka = new int[6];
            for (int i=0; i<6; i++) sestka[i] = acc.get(i);
            // Jina moznost, jak prevest ArrayList<Integer> na int[] je:
            // int[] sestka = acc.stream().mapToInt(x -> x).toArray();
            if (jeToSestka(sestka)) sestky.add(sestka);
        } else {
            for (int a=0; a<2; a++) {
                ArrayList<Integer> acc2 = new ArrayList<Integer>(acc);
                acc2.add(a);
                init(n-1, acc2);
            }
        }
    }

    void forEachKombinaci(int n, int k, ArrayList<Integer> list, ZpracujZadani f) {
        //z cisel 0,1,2,...n-1 vybere k ruznych
        if (k==0) {
            f.zpracuj(list);
            return;
        }
        for (int cislo = n-1; cislo >= k-1; cislo--) {
            ArrayList list2 = new ArrayList(list);
            list2.add(cislo);
            forEachKombinaci(cislo,k-1, list2, f);
        }
    }
    void forEachKombinaci(int n, int k, ZpracujZadani f) {
        forEachKombinaci(n,k,new ArrayList<>(), f);
    }
    void forEachVariaci(int n, int k, ArrayList<Integer> list, ZpracujZadani f) {
        //z cisel 0,1,2,...n-1 vybere k ruznych, přičemž závisí na pořadí
        if (k==0) {
            f.zpracuj(list);
            return;
        }
        for (int cislo = 0; cislo < n; cislo++) {
            if (list.contains(cislo)) continue;
            ArrayList list2 = new ArrayList(list);
            list2.add(cislo);
            forEachVariaci(n,k-1, list2, f);
        }
    }
    void forEachVariaci(int n, int k, ZpracujZadani f) {
        forEachVariaci(n,k,new ArrayList<>(), f);
    }

    void vypisSestku(int[] s) {
        for (int bit: s) System.out.print(bit);
        System.out.println();
    }
    void vypisZadani(ArrayList<Integer> radky) {
        for (int radek: radky) vypisSestku(sestky.get(radek));
    }

    boolean zkontrolujZadani(ArrayList<Integer> radky) {
        int[] sloupec = new int[6];
        for (int i=0; i<6; i++) {
            for (int j=0; j<6; j++) {
                sloupec[j] = sestky.get(radky.get(j))[i];
            }
            if (!jeToSestka(sloupec)) return false;
        }
        return true;
    }

    int pocetZadani = 0;
    void generujBinario() {
        int[][] sachovnice = new int[6][6];
        forEachVariaci(sestky.size(), 6, l -> {
            if (zkontrolujZadani(l)) {
                vypisZadani(l);
                pocetZadani++;
                System.out.println();
            }
            return true;
        });

        System.out.println("Pocet binario je: " + pocetZadani);
    }

    public static void main(String[] args) {
        Binario binario = new Binario();
        System.out.println("Hello World!");
        binario.sestky.forEach( sestka -> { for (int bit: sestka) { System.out.print(bit); }; System.out.println(); } );
        System.out.println(binario.sestky);

        int[] test = {0,1,1,0,1,0};
        int[] test1 = {0,1,1,0,1,0};
        if (binario.sestky.contains(test)) System.out.println("Woooo hoooo"); //nefunguje

        boolean obsahujeTest = false;
        for ( int[] sestka: binario.sestky) {
            if (sestka.equals(test)) {
                obsahujeTest = true;
                break;
            }
        }
        if (obsahujeTest) System.out.println("A co tohle?"); //taky nefunguje

        binario.forEachVariaci(5,3, v -> { System.out.println(v); return true; } );
        binario.generujBinario();
    }
}
