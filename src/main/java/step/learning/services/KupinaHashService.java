package step.learning.services;

public class KupinaHashService implements HashService {
    private final Kupina kupina = new Kupina( 128 ) ;
    @Override
    public String hash(String input) {
        kupina.update( input.getBytes() ) ;
        return kupina.digestHex() ;
    }
}
