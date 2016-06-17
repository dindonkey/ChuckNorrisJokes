# ChuckNorrisJokes
My proposal for a testable Android application architecture.

## What's in
* MVP Architecture and Repository Pattern
* RxAndroid with Observable cache
* Retrofit2 and Gson with TypeAdapterFactory
* Package-per-feature structure
* Network unit test with MockWebServer
* Gradle Mock Flavor to run App with fake data and NetworkBehaviour
* Fragment comunication with RxJava event bus

## Test coverage strategy
* JVM unit test for Presenter and Network Logic
* Activity unit test, mock test and e2e test with MockWebServer 
