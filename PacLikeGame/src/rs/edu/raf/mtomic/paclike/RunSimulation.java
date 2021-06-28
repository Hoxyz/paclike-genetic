package rs.edu.raf.mtomic.paclike;

import rs.edu.raf.mtomic.paclike.agent.player.PlayerOne;

/**
 * Test klasa:
 * <p>
 * Ovde možete ubaciti proizvoljni sistem za optimizaciju.
 * <p>
 * Zadatak je naslediti klasu Player i implementirati metodu generateNextMove,
 * koja će za svaki poziv vratiti jednu od sledećih metoda:
 * this::goUp, this::goLeft, this::goDown, this::goRight
 * te na taj način upravljati igračem u lavirintu.
 * <p>
 * Može se i (umesto nove klase) izmeniti klasa PlayerOne, koja sada samo ide levo.
 * <p>
 * U metodi se smeju koristiti svi dostupni geteri, ali ne smeju se
 * koristiti metode koje na bilo koji način menjaju stanje protivničkih
 * agenata ili stanje igre.
 * <p>
 * Matrica fields iz gameState: prvi indeks je kolona (X), drugi je vrsta (Y)
 * <p>
 * Zadatak: koristiti genetski algoritam za optimizaciju parametara
 * koji mogu odlučivati o sledećem potezu igrača. Generisanje poteza se
 * vrši na svaki frejm. Cilj je pokupiti svih 244 tačkica iz lavirinta.
 * Ako igrač naleti na protivnika, igra se prekida.
 * <p>
 * Savet: pogledajte kako Ghost agenti odlučuju o tome kada treba napraviti
 * skretanje (mada oni imaju jednostavna ponašanja) u njihovoj metodi Ghost::playMove.
 * <p>
 * Takođe, u implementacijama njihove metode calculateBest mogu se videti
 * primeri korišćenja GameState, iz koga se čitaju svi parametri.
 * <p>
 * Konačno stanje igre generiše se pokretanjem igre preko konstruktora i
 * pozivom join(), pa onda getTotalPoints().
 * <p>
 * Igrač se inicijalizuje GameState-om null, a PacLike će obezbediti
 * odgovarajuće stanje.
 * <p>
 * Ukoliko želite da pogledate simulaciju igre, promenite polje render
 * u klasi PacLike na true, a fps podesite po želji (ostalo ne treba
 * dirati).
 * <p>
 * Ograničenja:
 * - Svi parametri koji se koriste u generateNextMove() moraju biti
 * ili nepromenjeni (automatski generisani i menjani od strane igre),
 * ili inicijalizovani pomoću genetskog algoritma, ili eventualno
 * ako dodajete nove promenljive u klasu inicijalizovani u konstruktoru.
 * <p>
 * - Kalkulacije pomoću tih parametara i na osnovu onoga što igrač vidi
 * na osnovu GameState klase (i svih getera odatle i od objekata do
 * kojih odatle može da se dospe) su dozvoljene i poželjne; nije
 * dozvoljeno oslanjati se na unutrašnju logiku drugih agenata i hardkodovati
 * ponašanja ili šablone koji postoje za ovu igru (mada je engine
 * dosta promenjen u odnosu na original, iako liči, tako da je
 * većina šablona u suštini neupotrebljiva).
 **/
public class RunSimulation {

    public static void main(String[] args) {
        PlayerOne p = new PlayerOne(null, new Chromosome(-0.8019021f, -0.4740576f, -1.167013f, -0.83776075f, 0.2833132f));
        PacLike pacLike = new PacLike(p);
        try {
            pacLike.join();
            System.out.println("Done: " + pacLike.getTotalPoints());
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }

}
