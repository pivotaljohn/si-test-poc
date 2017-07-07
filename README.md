
# The Question

Is there a way to temporarily disable/shutdown/stop Spring Integration components while they sit in the Application Context cache between tests without having to completely shutdown the context (i.e. mark the test with `@DirtiesContext`)?

# Background

- When a suite of tests cause more than one flavor of the Application Context to initialize (e.g. a test introduces a `@MockBean`), all of those flavors remain in a running state between tests.
- This means that components like listeners from all the cached Application Contexts compete for messages as they arrive on an input channel.
- This results in a kind of test pollution and these tests yield a false negative.

# Overview

- Two flavors of tests in `./src/test/java/.../base`
  - `RealMagicJazzTest` -- a test with an unmodified application context.
  - `MockedMagicJazzTest` -- a test which injects a `@MockBean`.
- Four different scenarios in `./src/test/java/.../scenario...`
  - demonstrating how combinations of these two tests (along with targetted use of `@DirtiesContext`) results in a suite that either predictably succeeds or fails.
  - each package is intended to be executed as its own test suite.
- `./src/main/java/.../MagicMaker` contains a 20 second sleep in the constructor to illustrate expensive app ctx start-up.

# Current Results

## Scenarios 1 and 2 -- one application context

In these two scenarios, there's only one app ctx running during the test.

Here to demonstrate that context caching is in effect and works fine when there's only one flavor of context.

## Scenario 3 -- two application contexts with no restart

In this scenario the second test fails because it's `IntegrationFlow` components are competing with those of the first test.  In tests runs I've examined, each flow gets 3 of the 6 total messages.

## Scenario 4 -- two application context, only one cached at a time

We "fix" scenario 3 by strategically placing `@DirtiesContext` annotations on tests that will be followed by a new flavor of Application Context.

In practice, we would not try to be so surgical (least we attempt to track when the subsequent test class will have a different context!).  In practice, we'd mark all such tests with an `@DirtiesContext`.

This quickly lengthens the duration of the test system execution as we've invalidated the application context.

# Desired Result

Rework scenario 4 so that all contexts remain cached (avoiding expensive start-up), but their components are "frozen" so they do not consume messages.