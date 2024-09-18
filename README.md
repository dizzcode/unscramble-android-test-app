# Unscramble android test app

<img src="https://github.com/dizzcode/unscramble-android-test-app/blob/main/screenshots/img.png" width="400" height="800" />

##

### Inside  
ViewModel  
UI State  
StateFlow  
Backing property  

### ViewModel test units
Test strategy  
- Success path: The success path tests  
- Error path: The error path tests focus on testing the functionality for a negative flow  

* Boundary case: *  
A boundary case focuses on testing boundary conditions in the app. In the Unscramble app, a boundary is checking the UI state when the app loads and the UI state after the user plays a maximum number of words.

### A good unit test typically has following four properties:
- Focused:
It should focus on testing a unit, such as a piece of code. This piece of code is often a class or a method.  

- Understandable: 
It should be simple and easy to understand when you read the code. 

- Deterministic: 
It should consistently pass or fail. When you run the tests any number of times, without making any code changes, the test should yield the same result.  

- Self-contained: 
It does not require any human interaction or setup and runs in isolation.  



