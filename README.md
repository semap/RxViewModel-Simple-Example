# RxViewModel Simple Example - Login (Android)

This is an simple Android app using [RxViewModel](https://github.com/semap/RxViewModel-Android). RxViewModel is a library that making MVVM implementation easier. 

The requirements are
- User needs to enter username and password to login
- The minimum length of username is 3
- The minimum length of password is 5
- If login button is disabled if the username or password is invalid
- Show a "Login Successful" toast if username is **admin** and password is **admin**
- Show a "Login Failed" toast if username is not **admin** or password is not **admin**
- Show a "Login Error" toast if there is an unexpected exception happens.
- The app produces an unexpected exception when the username is **error** and password is **error** 


# Clean Code - View layer
The view ([LoginActivity](src/main/java/rxviewmodel/example/login/ui/LoginActivity.kt)) is super clean. It only does two things.
- Render the Live Data (coming from RxJava Observable) in the ViewModel
- Tell ViewModel to execute actions

There is no any logic in the view.

# Clean code - ViewModel
The main idea of the RxViewModel is **Action**, **State** and **ViewModel**.
- We have [three Actions](src/main/java/rxviewmodel/example/login/viewmodel/LoginAction.kt) in this simple app. They are SetUsername, SetPassword and Login.
- The [State](src/main/java/rxviewmodel/example/login/viewmodel/LoginState.kt) represent the data of the ViewModel. 
- The ([LoginViewModel](src/main/java/rxviewmodel/example/login/viewmodel/LoginViewModel.kt)) is the core. It generate a next State based on action and current state. ViewModel also reveals the LiveData for View to render. And those LiveData are coming form the RxJava observables that RxViewMode provides.


# The RxJava observables that RxViewModel provides
 - **stateObservable**
 - **actionOnCompleteObservable**
 - **actionOnNextObservable**
 - **isLoadingObservable**
 - **errorObservable**
 - **actionErrorObservable**  



 # More information

 - You can read [this article](https://medium.com/aeqdigital/reactive-programming-with-mvvm-for-mobile-apps-9d5476f9ecc7) to have a deeper look.
