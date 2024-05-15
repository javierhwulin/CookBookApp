package edu.ub.pis2324.projecte.domain.usecases.implementation;



import edu.ub.pis2324.projecte.data.repositories.UserRepository;
import edu.ub.pis2324.projecte.domain.exceptions.AppThrowable;
import edu.ub.pis2324.projecte.domain.exceptions.AppThrowableMapper;
import edu.ub.pis2324.projecte.domain.model.values.ClientId;
import edu.ub.pis2324.projecte.domain.usecases.ChangePasswordUseCase;
import edu.ub.pis2324.projecte.domain.usecases.ChangePremiumUseCase;
import edu.ub.pis2324.projecte.domain.usecases.FetchClientUsecase;
import io.reactivex.rxjava3.core.Observable;

public class ChangePremiumUseCaseImpl implements ChangePremiumUseCase {
    private final AppThrowableMapper throwableMapper;
    private final FetchClientUsecase fetchClientUseCase;

    private final UserRepository userRepository;

    public ChangePremiumUseCaseImpl(FetchClientUsecase fetchClientUseCase) {
        this.fetchClientUseCase = fetchClientUseCase;
        throwableMapper = new AppThrowableMapper();
        throwableMapper.add(UserRepository.Error.USER_NOT_FOUND, Error.CLIENTS_DATA_ACCESS_ERROR);
        userRepository = new UserRepository();
    }

    @Override
    public Observable<Boolean> execute(ClientId clientId,boolean isPremium) {

        return checkIdEmpty(clientId)
                .concatMap(ignored -> changePremium(clientId, isPremium))
                .onErrorResumeNext(throwable -> Observable.error(throwableMapper.map(throwable)));

    }

    private Observable<Boolean> changePremium(ClientId clientId,boolean isPremium) {
        return userRepository.changePremium(clientId, isPremium);
    }
    private Observable<Boolean> checkIdEmpty(ClientId clientId) {
        if (clientId == null || clientId.toString().isEmpty()) {
            return Observable.error(new AppThrowable(Error.CLIENTS_DATA_ACCESS_ERROR));
        }
        return Observable.just(true);
    }
}
