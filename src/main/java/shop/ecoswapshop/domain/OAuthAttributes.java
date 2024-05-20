package shop.ecoswapshop.domain;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;

public enum OAuthAttributes {


//    GOOGLE("google", (attribute) -> {
//        UserProfile userProfile = new UserProfile();
//        userProfile.setUserName((String)attribute.get("name"));
//        userProfile.setEmail((String)attribute.get("email"));
//
//        return userProfile;
//    }),

    NAVER("naver", (attribute) -> {
        UserProfile userProfile = new UserProfile();

        @SuppressWarnings("unchecked")
        Map<String, String> responseValue = (Map<String ,String>)attribute.get("response");

        userProfile.setUserName(responseValue.get("name"));
        userProfile.setEmail(responseValue.get("email"));

        return userProfile;
    }),

    KAKAO("kakao", (attribute) -> {
        @SuppressWarnings("unchecked")
        Map<String, Object> account = (Map<String, Object>)attribute.get("kakao_account");
        @SuppressWarnings("unchecked")
        Map<String, String> profile = (Map<String, String>)account.get("profile");

        UserProfile userProfile = new UserProfile();
        userProfile.setUserName(profile.get("nickname"));
        userProfile.setEmail((String)account.get("email"));

        return userProfile;
    });

    private final String registrationId; // 로그인한 서비스(ex) google, naver..)
    private final Function<Map<String, Object>, UserProfile> of; // 로그인한 사용자의 정보를 통하여 UserProfile을 가져옴

    OAuthAttributes(String registrationId, Function<Map<String, Object>, UserProfile> of) {
        this.registrationId = registrationId;
        this.of = of;
    }

    public static UserProfile extract(String registrationId, Map<String, Object> attributes) {
        return Arrays.stream(values())
                .filter(value -> registrationId.equals(value.registrationId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unsupported registrationId: " + registrationId))
                .of.apply(attributes);
    }
}
