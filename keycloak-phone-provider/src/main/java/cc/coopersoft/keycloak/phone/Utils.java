package cc.coopersoft.keycloak.phone;

import cc.coopersoft.keycloak.phone.providers.spi.PhoneProvider;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;
import org.keycloak.models.UserProvider;

import java.util.Comparator;
import java.util.Optional;


/**
 *
 *
 *
 */


public class Utils {


    public static Optional<UserModel> findUserByPhone(UserProvider userProvider, RealmModel realm, String phoneNumber){
        return userProvider
            .searchForUserByUserAttributeStream(realm,"phoneNumber", phoneNumber)
            .max(comparatorUser());
    }

    public static Optional<UserModel> findUserByPhone(UserProvider userProvider, RealmModel realm, String phoneNumber, String notIs){
        return userProvider
            .searchForUserByUserAttributeStream(realm, "phoneNumber", phoneNumber)
            .filter(u -> !u.getId().equals(notIs))
            .max(comparatorUser());
    }

    private static Comparator<UserModel> comparatorUser() {
        return (u1, u2) ->
            Boolean.compare(u1.getAttributeStream("phoneNumberVerified").anyMatch("true"::equals),
                u2.getAttributeStream("phoneNumberVerified").anyMatch("true"::equals));
    }

    public static boolean isDuplicatePhoneAllowed(KeycloakSession session){
        return session.getProvider(PhoneProvider.class).isDuplicatePhoneAllowed(session.getContext().getRealm().getName());
    }

    public static Optional<String> getPhoneNumberRegx(KeycloakSession session){
        return session.getProvider(PhoneProvider.class).phoneNumberRegx(session.getContext().getRealm().getName());
    }

}
