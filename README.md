# hello-mvp-dagger-2

MVP example code using RxJava, Retrolambda, Dagger 2, and Compartment (https://github.com/grandstaish/compartment).

The app demonstrates all parts of MVP (Model, View, and Presenter). It demostrates getting data from a fake service and caching it in memory and to disk. The cached data is valud for 5 seconds, or until manually cleared by the user. 

The project demonstrates techniques (as I understand them) from Dan Lew's blog such as loading data from multiple sources (http://blog.danlew.net/2015/06/22/loading-data-from-multiple-sources-with-rxjava/), using compose (http://blog.danlew.net/2015/03/02/dont-break-the-chain/), and deferring code until subscription (http://blog.danlew.net/2015/07/23/deferring-observable-code-until-subscription-in-rxjava/) to name a few.

The project also has working Espresso tests that run with Spoon, as well as Unit Tests that run with Robolectric. The testing side of things I think some of you might find interesting as I had to solve a few problems that I hadn't seen very good solutions for:

- Sharing code between unit and instrumentation tests. I have created a new directory called 'sharedTest' (the naming sucks) and let gradle know that both 'test' and 'androidTest' use it.
- Some animations prevented Espresso from working as the main thread was never idle. Solutions I have seen to this problem involve reflectively disabling animations and granting permissions to your application as part of the built script. Unfortunately this doesn't solve the problem in some cases (e.g. an indeterminate ProgressBar continues to keep the main thread busy even if animations are disabled). Not to mention this solution is pretty fragile. I use a custom layout inflation trick instead to replace my ProgressBar with a plain old View at runtime.
- Testing with Dagger 2 in general is pretty interesting
- Ability to Espresso test at a fragment level rather than an Activity level, e.g. pick the fragment to start your test at rather than having to start the activity from scratch and navigate to the fragment you want to test. This is big for me because I often don't have a 1-1 relationship between Activities and Fragments.

It is intended as a sample for my own best practices
