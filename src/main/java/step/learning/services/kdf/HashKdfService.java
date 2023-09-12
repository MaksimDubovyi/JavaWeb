package step.learning.services.kdf;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import step.learning.services.HashService;

@Singleton
public class HashKdfService implements KdfService{
    private final HashService hashService;
@Inject
    public HashKdfService(HashService hashService) {
        this.hashService = hashService;
    }

    @Override
    public String getDerivedKey(String password, String salt) {
        return hashService.hash(String.format( "%S-%s",password,salt ));
    }
}
