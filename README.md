# SafeCam

Safe cam is showcase app created for recruitment purposes.
Main goals:
"Safely camera." - the app allows to 
take photos (using built-in device camera(s)) 
and browse taken photos (just a simple grid is enough) - 
upon launching the app the user is asked for a password (setting the password is out of scope), only the correct password is accepted - all the taken photos must be encrypted using a key (somehow) derived from or protected by the password - pls use 
Kotlin, 
MVVM, 
coroutines, 
Data Binding, 
Material Design, 
AndroidX if possible - that's all the requirements -> all the other decisions should be taken by you 

Basic architecture.
I'm using koin for dependency injections, with modules:
- app module - common stuff
- crypto module - cryptography related classes
- viewmodel module - viewmodels
- glide module - classess required by glide custom decoder.

To log in, use super secure password "1111" - its hardcoded in app

After login, you can see camerax preview - you can either take photo or browse previously taken photos.

When user takes photo, it's first stored into internal cache dir (private folder for app process)
Then file gets encrypted using androidx security framework and stored into per-app media folder (files are encrypted, so we don't care 
about destination.
To browse files, we list all files inside app media folder. I created custom glide loader, responsible for decrypting data.
To handle navigation, I'm using navcomponent.
I had to use coroutines, although normally I'd prefer rxjava.

Project has meaningfull unit tests, please take a look into LoginViewModelTest
Decided not to test camerax api, androidx security and glide modules.
