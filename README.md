# 토스 android-sdk
토스 Android SDK 를 위한 모노레포입니다.

## 설치 하기

### 요구 사항
토스 Android SDK를 설치하기 전에 최소 요구 사항을 확인하세요.
- Android 6.0 (API 23) 이상 
- (TODO) gradle 설정 채워야 함

## 사전 설정

### Redirect URI 설정
토스 로그인 기능을 구현하기 위해서는 리다이렉션(Redirection)을 통해 인가 코드를 받아야 합니다. 이를 위해 `AndroidManifest.xml`에 액티비티(Activity) 설정이 필요합니다. 아래 예제를 참고합니다.
```xml
<activity 
    android:name="com.vivarepublica.loginsdk.activity.TossAuthCodeHandlerActivity"
    android:exported="true">

    <intent-filter android:autoVerify="true">
        <action android:name="android.intent.action.VIEW" />

        <category android:name="android.intent.category.BROWSABLE" />
        <category android:name="android.intent.category.DEFAULT" />

        <!-- Redirect URI: "toss{NATIVE_APP_KEY}://oauth" -->
        <data
            android:host="oauth"
            android:scheme="toss${NATIVE_APP_KEY}" />
    </intent-filter>

</activity>
```
새로운 액티비티를 추가하고, name 요소의 값은 `com.vivarepublica.loginsdk.activity.AuthCodeHandlerActivity`로 입력합니다. 
Android 12(API 31) 이상을 타깃으로 하는 앱인 경우, exported 요소를 반드시 "true"로 선언해야 합니다.
해당 액티비티 하위에 `<intent-filter>` 요소를 추가하고, host와 scheme 요소 값으로 토스 로그인을 위한 Redirect URI를 설정합니다. 
scheme 속성의 값은 `toss{NATIVE_APP_KEY}` 형식으로 입력합니다. 예를 들어 앱 키가 "123456789"라면 "toss123456789"를 입력합니다.


## 시작하기

### SDK 초기화

네이티브 앱 키로 초기화를 해야합니다.
`Application` 을 상속한 클래스를 사용하고 있다면 다음과 같이 초기화할 수 있습니다.
```kotlin
class TossLoginApplication : Application() {
  override fun onCreate() {
    super.onCreate()
    // 다른 초기화 코드들

    // Toss SDK 초기화
    TossSdk.init(this, "{NATIVE_APP_KEY}")
  }
}
```
만약 Application 클래스에서 초기화를 하셨다면,
`AndroidManifest.xml`의 application 클래스에도 Toss SDK 초기화를 수행한 클래스의 이름을 설정해야 합니다. 
위 예제에서는 `TossLoginApplication` 클래스에서 초기화를 했으므로 아래와 같이 동일한 이름을 설정에 추가합니다.

```xml
<application
    <!-- android:name 설정 -->
    android:name=".TossLoginApplication"
    ...
>
```

### 로그인 요청

토스로 로그인을 하기 위해선 `TossSdk.login(context: Context, onResult: (TossLoginResult) -> Unit)` 함수를 호출하세요,
`isLoginAvailable(context: Context)` 함수로 토스앱 실행 가능 여부를 확인할 수 있어요.
```kotlin

if (TossLoginController.isLoginAvailable(context).not()) {
    return TossLoginController.moveToPlaystore(context)
}

TossLoginController.login(context) { resunt -> 
    when (result) {
        is TossLoginResult.Success -> {
            // authCode 를 통해 accessToken을 발급받으세요.
            val accessToken = result.authCode
        }
        is TossLoginResult.Error -> {
            Log.e("TossLogin", "error: ${result.error}")
        }
        is TossLoginResult.Cancel -> {
            // 사용자가 취소했어요.
            Log.e("TossLogin", "cancel")
        }
    }
}
```
