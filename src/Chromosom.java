import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Dictionary;
import java.util.Random;

public class Chromosom implements Comparable {

    int iloscGenow;
    int dlugoscGenu;
    int [] tabChromosomu;
    double [] tabGenow;
    double wartPrzystowowania = 0;
    double zakresDolny;
    double zakresGorny;
    int dokladnosc;
    static Chromosom najlepszy;


    public static double funkcjaCelu(Chromosom x) {

        int a = 10, n = x.tabGenow.length;
        double sum = 0;

        for( int i=0; i<x.tabGenow.length; i++ )
            sum += (x.tabGenow[i] * x.tabGenow[i]) - a * Math.cos(2 * Math.PI * x.tabGenow[i]);

        return a * n + sum;
    }

    Chromosom(int iloscGen, double zakrDolny, double zakrGorny, int dokladn) {
        iloscGenow = iloscGen;
        dlugoscGenu = obliczDlugosc(zakrDolny, zakrGorny, dokladn);
        tabChromosomu = new int[iloscGenow * dlugoscGenu];
        tabGenow = new double[iloscGenow];
        zakresDolny = zakrDolny;
        zakresGorny = zakrGorny;
        dokladnosc = dokladn;
    }
        private int obliczDlugosc( double zakDolny, double zakGorny, int dokladno ) {

        double d = Math.pow(10, (-1 * dokladno));
        double gora = Math.abs( zakGorny / d );
        double dol = Math.abs( zakDolny / d );

        double wynik = Math.log(gora+dol) / Math.log(2);    // logarytm o podstawie z 2 z czalego zakresu dopuszczlnych wartosci podzielonych przez odwrotnosc dokladnosci

        if(wynik % 1 != 0){                 // zaokraglenie w gore
            int wy = (int)wynik;
            wy++;
            wynik = (double)wy; }

        return (int)wynik;


    }


    public void losujChromosom() {

        if(dlugoscGenu != 0) {              // wykona sie tylko dla chromosomu obslugujacego wartosci binarne
            Random rand = new Random();
            for (int k = 0; k < iloscGenow * dlugoscGenu; k++)
                this.tabChromosomu[k] = rand.nextInt(2);
            zdekodujChromosom();
        }

        else {
            for(int k=0; k<iloscGenow; k++) {
                Random rand = new Random();
                this.tabGenow[k] = zakresDolny + (double)rand.nextInt((int)zakresGorny - (int)zakresDolny + 1);
            }
        }
    }
    public void zdekodujChromosom() {

        for(int i=0; i<iloscGenow; i++) {

            double x2d = 0;

            for (int j = (dlugoscGenu * i); j < (dlugoscGenu*(i+1)); j++)
                if (tabChromosomu[j] == 1)
                    x2d = x2d + Math.pow(2, j - ((dlugoscGenu * i)));

            double gen = ((zakresGorny - zakresDolny) * x2d) / (Math.pow(2, dlugoscGenu) - 1) + zakresDolny;
            int d = (int)Math.pow(10,dokladnosc);
            gen *= d;
            gen = Math.round(gen);
            gen /= d;
            tabGenow[i] = gen;
        }
    }
    public static void najlepszyGlobal(Chromosom [] kolekcja){

        if(najlepszy == null)
            najlepszy = kolekcja[0].clone();

        for(Chromosom i : kolekcja)
            if(i.compareTo(najlepszy) == -1)
                najlepszy = i.clone();
            najlepszy.zdekodujChromosom();
    }
    public static Chromosom najlepszyLocal(Chromosom [] kolekcja){

        Chromosom naj = kolekcja[0].clone();

        for(Chromosom i : kolekcja)
            if(i.compareTo(naj) == -1)
                naj = i.clone();

        return naj;
    }

    public static void krzyzowanieWieloPkt(Chromosom x, Chromosom y){
        int [] a = x.tabChromosomu;
        int [] b = y.tabChromosomu;
        Random generator = new Random();

        for(int aktGen = 0; aktGen<x.iloscGenow; aktGen++) {

            int pktPrzeciecia = (aktGen * x.dlugoscGenu + 1) + generator.nextInt(x.dlugoscGenu - 1);

            //System.out.println("Krzyżowanie wielo-punkt w genie nr: " + aktGen + ", w punktcie: " + pktPrzeciecia);
            // WYŚWIETLENIE RODZICÓW I PKT PRZECIECIA
            /*
            System.out.println("Rodzic Gen A: " + aktGen);
            for (int i = (aktGen * x.dlugoscGenu); i < ((aktGen + 1) * x.dlugoscGenu); i++) {
                if(i == pktPrzeciecia)
                    System.out.print("| ");
                System.out.print(a[i] + " "); }
            System.out.println();
            System.out.println("Rodzic Gen B: " + aktGen);
            for (int i = (aktGen * x.dlugoscGenu); i < ((aktGen + 1) * x.dlugoscGenu); i++){
                if(i == pktPrzeciecia)
                    System.out.print("| ");
                System.out.print(b[i] + " "); }
            System.out.println();
             */

            for (int i = (aktGen * x.dlugoscGenu); i < ((aktGen + 1) * x.dlugoscGenu); i++){
                if(i >= pktPrzeciecia) {
                    int tmp = a[i];
                    a[i] = b[i];
                    b[i] = tmp; }
            }

            // WYŚWIETLENIE DZIECI
            /*
            System.out.println("Dziecko Gen A: " + aktGen);
            for (int i = (aktGen * x.dlugoscGenu); i < ((aktGen + 1) * x.dlugoscGenu); i++) {
                if(i == pktPrzeciecia)
                    System.out.print("| ");
                System.out.print(a[i] + " "); }
            System.out.println();
            System.out.println("Dziecko Gen B: " + aktGen);
            for (int i = (aktGen * x.dlugoscGenu); i < ((aktGen + 1) * x.dlugoscGenu); i++){
                if(i == pktPrzeciecia)
                    System.out.print("| ");
                System.out.print(b[i] + " "); }
            System.out.println();
             */
        }
    }
    public static void krzyzowanieJednoPkt(Chromosom x, Chromosom y) {

        int [] a = x.tabChromosomu;
        int [] b = y.tabChromosomu;

        Random generator = new Random();
        int pktCiecia = 1 + (generator.nextInt(a.length-2)); // losujemy pkt przec <1, tab.length-2>

        //System.out.println("Krzyzowanie jedno-punkt w punktcie: " + pktCiecia);
        // WYŚWIETLENIE RODZICÓW I PKT PRZECIECIA
        /*
        System.out.print("RODZIC A:  [ ");
        for( int i=0; i<a.length; i++ ){
            if(i == pktCiecia)
                System.out.print("| ");
            System.out.print(a[i] + " "); }
        System.out.println("]");
        System.out.print("RODZIC B:  [ ");
        for( int i=0; i<a.length; i++ ){
            if(i == pktCiecia)
                System.out.print("| ");
            System.out.print(b[i] + " "); }
        System.out.println("]");
         */

        for( int i=0; i<a.length; i++ )
            if(i >= pktCiecia){
                int tmp = b[i];
                b[i] = a[i];
                a[i] = tmp; }

        // WYŚWIETLENIE DZIECI
        /*
        System.out.print("DZIECKO A: [ ");
        for( int i=0; i<a.length; i++ ){
            if(i == pktCiecia)
                System.out.print("| ");
            System.out.print(a[i] + " "); }
        System.out.println("]");
        System.out.print("DZIECKO B: [ ");
        for( int i=0; i<a.length; i++ ){
            if(i == pktCiecia)
                System.out.print("| ");
            System.out.print(b[i] + " "); }
        System.out.println("]");
         */

    }
    public void mutacja(double prawdopodobienstwo) {

        Random generator = new Random();

        for(int aktGen = 0; aktGen<this.iloscGenow; aktGen++) {

            if(generator.nextDouble() <= prawdopodobienstwo){
                int pktMutacji = (aktGen * this.dlugoscGenu + 1) + generator.nextInt(this.dlugoscGenu - 1);
                //System.out.println("mutacja w genie nr " + aktGen + ", na punkcie: " + String.valueOf(pktMutacji - this.dlugoscGenu * aktGen));

                if (this.tabChromosomu[pktMutacji] == 0)
                    this.tabChromosomu[pktMutacji] = 1;
                else
                    this.tabChromosomu[pktMutacji] = 0;
            }
            //else
               // System.out.println("nie doszlo do mutacji w genie nr " + aktGen);
        }
    }

    public static void funkcjaPrzystosowania(Chromosom [] kolekcja) {

        // gdy szukamy -> MIN
        double mianownik = 0;
        for(Chromosom i : kolekcja)
            mianownik += Math.abs(funkcjaCelu(i));

        double sumaOdwrotnosci = 0;

        for(Chromosom i : kolekcja) {
            double licznik = Math.abs(funkcjaCelu(i));
            sumaOdwrotnosci += mianownik / licznik;
        }

        for(Chromosom i : kolekcja) {
            double licznik = Math.abs(funkcjaCelu(i));
            i.wartPrzystowowania = (mianownik / licznik) / sumaOdwrotnosci;
        }


        /*
        // gdy szukamy -> MAX
        double mianownik = 0;

        for( Chromosom i : kolekcja )
            mianownik += Math.abs(funkcjaCelu(i));

        for( Chromosom i : kolekcja )
            i.wartPrzystowowania = Math.abs( funkcjaCelu(i) / mianownik );
         */
    }
    public static Chromosom[] ruletka(Chromosom [] kolekcja) {

        Chromosom [] wylosowani = new Chromosom[kolekcja.length];
        Random losowanie = new Random();

        for( int i=0; i<kolekcja.length; i++ ){

            double wylosowane = losowanie.nextDouble();
            double tmp = 0;

            for( int k=0; k<kolekcja.length; k++ ){
                if(kolekcja[k].wartPrzystowowania + tmp >= wylosowane && tmp < wylosowane){
                    wylosowani[i] = kolekcja[k].clone();
                    //System.out.println("Wylosowano: " + (k+1) + " element");
                }
                tmp += kolekcja[k].wartPrzystowowania; }

        }

        return wylosowani;
    }

    @Override
    public int compareTo(Object o) {
        double w1 = funkcjaCelu(this);
        double w2 = funkcjaCelu((Chromosom)o);

        if(w1 < w2) return -1;
        else if(w2 > w1) return 1;
        else return 0;
    }
    @Override
    public Chromosom clone(){

        Chromosom clon = new Chromosom(this.iloscGenow, this.zakresDolny, this.zakresGorny, this.dokladnosc);

        for(int i=0; i<this.tabChromosomu.length; i++){
            if(this.tabChromosomu[i] == 1)
                clon.tabChromosomu[i] = 1;
            else
                clon.tabChromosomu[i] = 0;
        }
        clon.zdekodujChromosom();
        return clon;
    }
    @Override
    public String toString() {
        String s = "[";
        for(int i=0; i<dlugoscGenu * iloscGenow; i++)
            s = s + " " + String.valueOf(this.tabChromosomu[i]);
        s = s + " ]";
        return s;
    }
    public static void pelneInfo(Chromosom [] kolekcja){
        for( int i=0; i<kolekcja.length; i++ ) {
            System.out.print(kolekcja[i]);
            System.out.println();
            for( int j=0; j<kolekcja[i].iloscGenow; j++ )
                System.out.print("Gen" + j + ": " + kolekcja[i].tabGenow[j] + ", ");
            System.out.println();
            System.out.println("Wartość funkcji celu dla danego chromosomu: " + funkcjaCelu(kolekcja[i]));
            System.out.println("wartość funkcji przystosowania: " + kolekcja[i].wartPrzystowowania);
            System.out.println();}
        System.out.println("Najlepszy (geny):");
        for( double gen : najlepszy.tabGenow)
        System.out.print( gen + ", " );
        System.out.println("Wartość funkcji: " + funkcjaCelu(najlepszy));
    }


    public static void main(String[] args) throws FileNotFoundException, IOException {


        int populacja = 40;
        int generacje = 300;

        PrintWriter zapis1 = new PrintWriter("best_global.txt");
        PrintWriter zapis2 = new PrintWriter("best_local.txt");
        PrintWriter zapis3 = new PrintWriter("wszytskie.txt");


        Chromosom [] tab = new Chromosom[populacja];

        for (int i = 0; i < populacja; i++) {
            tab[i] = new Chromosom(10, -5.21, 5.21, 3);
            tab[i].losujChromosom();
            tab[i].zdekodujChromosom(); }

        najlepszyGlobal(tab);



        for( int gen=0; gen<generacje; gen++ ) {

            System.out.println();
            System.out.println("***********************************");
            System.out.println("           " + (gen+1) + " GENERACJA");
            System.out.println("***********************************");
            System.out.println();

            funkcjaPrzystosowania(tab);
            tab = ruletka(tab);

            for(int i=0; i<tab.length-1; i+=2) {
                krzyzowanieWieloPkt(tab[i], tab[i+1]);
                //krzyzowanieJednoPkt(tab[i], tab[i+1]);
            }

            for(int i=0; i<tab.length; i++){
                tab[i].mutacja(0.1);
                tab[i].zdekodujChromosom();
            }

            funkcjaPrzystosowania(tab);
            najlepszyGlobal(tab);
            pelneInfo(tab);
            System.out.println();

            zapis1.println(funkcjaCelu(najlepszy));
            zapis2.println(funkcjaCelu(najlepszyLocal(tab)));
            for(Chromosom i : tab)
                zapis3.println(funkcjaCelu(i));


        }

        zapis1.close();
        zapis2.close();
        zapis3.close();

    }
}
